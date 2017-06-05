package com.yidao.monitor.monitorJob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.perf4j.StopWatch;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.yidao.monitor.pojo.MonitorContacts;
import com.yidao.monitor.pojo.MonitorExceptionRecord;
import com.yidao.monitor.pojo.MonitorItems;
import com.yidao.monitor.service.MonitorContactsService;
import com.yidao.monitor.service.MonitorExceptionRecordService;
import com.yidao.monitor.service.MonitorHostsService;
import com.yidao.monitor.service.MonitorItemsService;
import com.yidao.monitor.util.HttpUtil;
import com.yidao.monitor.util.NotifyInfoUtils;
import com.yidao.monitor.util.PSFClient;
import com.yidao.monitor.util.PSFClient.PSFRPCRequestData;


@Component
public class SchedulerExecution implements Job {

	private final static Logger logger = LoggerFactory.getLogger(SchedulerExecution.class);
	private MonitorItemsService monitorItemsService;
	public MonitorItems monitorItems = null;
	public MonitorContactsService monitorContactsService = null;
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		//获取任务需要的数据
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		//监控项信息
		monitorItems = (MonitorItems) jobDataMap.get("monitorItems");
		//监控系统host service
		MonitorHostsService monitorHostsService = (MonitorHostsService) jobDataMap.get("monitorHostsService");
		//错误记录 service
		MonitorExceptionRecordService monitorExceptionRecordService = (MonitorExceptionRecordService) jobDataMap.get("monitorExceptionRecordService");
		//报警组 service
		monitorContactsService = (MonitorContactsService) jobDataMap.get("monitorContactsService");		
		monitorItemsService = (MonitorItemsService)jobDataMap.get("monitorItemsService");	
		String exceptionHost = "";
			try {
				List<String> monitorHosts = monitorHostsService.getMonitorHosts(monitorItems.getMonitorSystem(),monitorItems.getRequestAgreement().split("-")[0]);
				if(monitorItems.getRequestAgreement().equalsIgnoreCase("psf")){
					//PSF请求结果
					String[] array = new String[monitorHosts.size()];
					for (int i = 0; i < monitorHosts.size(); i++) {
						array[i]=monitorHosts.get(i);
					}
					JSONObject psfOrHTTPRequest = this.PSFOrHTTPRequest(null,array,monitorItems);
					//返回解析结果
					int resultAnalysis = this.ResultAnalysis(psfOrHTTPRequest, monitorItems.getMonitorAssert(), monitorItems.getMonitorTimeout());
					String inform = this.inform(resultAnalysis, monitorItems.getMonitorSystem(), monitorItems.getMonitorId(), monitorItems.getMonitorName(), "PSF调度", psfOrHTTPRequest.getIntValue("time"),psfOrHTTPRequest.getString("result"), monitorItems.getPsfUri());
					if(inform!=null){
						//保存错误记录
						boolean addMonitorExceptionNotes = this.addMonitorExceptionNotes(monitorExceptionRecordService, resultAnalysis, monitorItems, "PSF调度", psfOrHTTPRequest);
						if(!addMonitorExceptionNotes) 
						logger.error("添加异常记录失败------！");
						//判断是否发送报警邮件，如果满足，发送邮件
						interNotify(inform,resultAnalysis);
					}
				}else{
					for (String host : monitorHosts) {
						exceptionHost = host;
						//HTTP请求结果
						JSONObject psfOrHTTPRequest = this.PSFOrHTTPRequest(host,null,monitorItems);
						//返回解析结果
						int resultAnalysis = this.ResultAnalysis(psfOrHTTPRequest, monitorItems.getMonitorAssert(), monitorItems.getMonitorTimeout());
						String inform = this.inform(resultAnalysis, monitorItems.getMonitorSystem(), monitorItems.getMonitorId(), monitorItems.getMonitorName(), host, psfOrHTTPRequest.getIntValue("time"),psfOrHTTPRequest.getString("result"), monitorItems.getHttpUrl());
						if(inform!=null){
							//保存错误记录
							boolean addMonitorExceptionNotes = this.addMonitorExceptionNotes(monitorExceptionRecordService, resultAnalysis, monitorItems, host, psfOrHTTPRequest);
							if(!addMonitorExceptionNotes) logger.error("添加异常记录失败------！");
					        interNotify(inform,resultAnalysis);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				//保存错误记录
				JSONObject exceptionJson = new JSONObject();
				exceptionJson.put("result",e.getMessage() );
				exceptionJson.put("time", "-1");
				logger.error("Scheduler Job ERROR------！");
				String errorMsg = "";
				if("".equals(exceptionHost)){
					exceptionHost = "PSF调度";
					 errorMsg ="接口："+monitorItems.getPsfUri()+"发生异常,报错信息："+e.getMessage();	
				}else{
					errorMsg ="接口："+monitorItems.getHttpUrl()+"发生异常,报错host:"+exceptionHost+",报错信息："+e.getMessage();	  	
				}					
				boolean addMonitorExceptionNotes = this.addMonitorExceptionNotes(monitorExceptionRecordService, 2, monitorItems, exceptionHost, exceptionJson);
				if(!addMonitorExceptionNotes) logger.error("添加异常记录失败------！");
			     interNotify(errorMsg,2);
				//接入自动化组
				
			}
	
	}
	public void interNotify(String inform,int resultAnalysis){
		if(resultAnalysis==0){
			monitorItems.setFailTimes(0);
			monitorItemsService.updateMonitorItems(monitorItems);	
		}else{
			//接入报警组
			int failTimes = monitorItems.getFailTimes();
			System.out.println("failTimes:"+failTimes);
			if(failTimes>1&&failTimes<7){
				monitorItems.setFailTimes(failTimes+1);
				System.out.println("interNotify monitorItemsService==null:"+(monitorItemsService==null));
				monitorItemsService.updateMonitorItems(monitorItems);	
				List<MonitorContacts> monitorContactsList = monitorContactsService.getMonitorContacts(monitorItems.getMonitorSystem());
				if(monitorContactsList!=null && !monitorContactsList.isEmpty()){
					NotifyInfoUtils notifyInfoUtils = new NotifyInfoUtils(true);
					String phone = null;
					String mail = null;
					for (MonitorContacts monitorContacts : monitorContactsList) {
						if(monitorContacts.getContactsPhone()!=null && !monitorContacts.getContactsPhone().isEmpty()){
							phone=monitorContacts.getContactsPhone()+",";
							mail=monitorContacts.getContactsMail()+",";
						}
					}
					String substringPhone = phone.substring(0, phone.length()-1);
					String substringMail = mail.substring(0, mail.length()-1);
					boolean doSendSMS = notifyInfoUtils.doSendSMS(substringPhone, inform);
					boolean doSendHtmlEmail = notifyInfoUtils.doSendHtmlEmail("【易道】监控系统平台报警邮件", inform, substringMail, null);
					if(doSendSMS) logger.info(String.format("报警短信发送成功,monitorId:%d,短信发送手机号：%s,短信内容:%s", monitorItems.getMonitorId(),substringPhone,inform));
					if(doSendHtmlEmail) logger.info(String.format("报警邮件发送成功,monitorId:%d,邮箱地址：%s,邮件内容:%s", monitorItems.getMonitorId(),substringMail,inform));	
			}					
		}else{
			monitorItems.setFailTimes(failTimes+1);
			monitorItemsService.updateMonitorItems(monitorItems);	
			logger.error("模块："+monitorItems.getMonitorSystem()+" 接口："+monitorItems.getPsfUri()+monitorItems.getHttpUrl()+"失败次数："+failTimes);
		}	
		}
		
	}
	/**
	 * PSF or HTTP 请求
	 * @param requestAgreement
	 * @param hosts
	 * @param monitorItems
	 * @return
	 * @throws Exception
	 */
	public JSONObject PSFOrHTTPRequest(String HTTPhost, String[] PSFhost ,MonitorItems monitorItems) throws Exception{
		JSONObject jsonObject = new JSONObject();
		if(monitorItems.getRequestAgreement().equalsIgnoreCase("psf")){
			if(PSFhost!=null && PSFhost.length > 0){
					PSFClient psf = new PSFClient(PSFhost);	
					PSFRPCRequestData request = new PSFClient.PSFRPCRequestData();
					request.service_uri = monitorItems.getPsfUriParam().isEmpty() ? monitorItems.getPsfUri() : monitorItems.getPsfUri()+"?"+monitorItems.getPsfUriParam();
					request.data = monitorItems.getPsfData().isEmpty() ? "" : monitorItems.getPsfData();
					if(monitorItems.getPsfServiceType()!=null && !monitorItems.getPsfServiceType().isEmpty()){
						StopWatch stopWatch = new StopWatch();
						String response = psf.call(monitorItems.getPsfServiceType(), request);
						long time = stopWatch.getElapsedTime();
						psf.close();
						jsonObject.put("code", 0);
						jsonObject.put("time", time);
						jsonObject.put("result", response);
						return jsonObject;
					}else{
						jsonObject.put("code", 404);
						jsonObject.put("result", monitorItems.getMonitorName() + "psf service type null");
						return jsonObject;
					}
				}
			}else if(monitorItems.getRequestAgreement().equalsIgnoreCase("http-get")){
			if(HTTPhost!=null && !HTTPhost.isEmpty()){
					if(!monitorItems.getHttpUrl().isEmpty()&&!monitorItems.getHttpParam().isEmpty()){
						String url = !monitorItems.getHttpUrl().substring(0,1).equals("/")?"/"+monitorItems.getHttpUrl():monitorItems.getHttpUrl();
						StopWatch stopWatch = new StopWatch();
						String response = HttpUtil.getIntance().get("http://" + HTTPhost + url +"?"+ monitorItems.getHttpParam());
						long time = stopWatch.getElapsedTime();
						jsonObject.put("code", 0);
						jsonObject.put("time", time);
						jsonObject.put("result", response);
						return jsonObject;
					}else{
						jsonObject.put("code", 404);
						jsonObject.put("result", monitorItems.getMonitorName() + "http-get url or param is null");
						return jsonObject;
					}
				}
			}else if(monitorItems.getRequestAgreement().equalsIgnoreCase("http-post")){
			if(HTTPhost!=null && !HTTPhost.isEmpty()){
					if(!monitorItems.getHttpUrl().isEmpty()&&!monitorItems.getHttpParam().isEmpty()){
						String url = !monitorItems.getHttpUrl().substring(0,1).equals("/")?"/"+monitorItems.getHttpUrl():monitorItems.getHttpUrl();
						Map<String, String> hashMap = this.toHashMap(monitorItems.getHttpParam());
						StopWatch stopWatch = new StopWatch();
						String response = HttpUtil.getIntance().post("http://" + HTTPhost + url , hashMap, null);
						long time = stopWatch.getElapsedTime();
						jsonObject.put("code", 0);
						jsonObject.put("time", time);
						jsonObject.put("result", response);
						return jsonObject;
					}else{
						jsonObject.put("code", 404);
						jsonObject.put("result", monitorItems.getMonitorName() + "http-post url or param is null");
						return jsonObject;
					}
			}else{
				jsonObject.put("code", 404);
				jsonObject.put("result", monitorItems.getMonitorName() + "host null");
				return jsonObject;
			}
		}else  if(monitorItems.getRequestAgreement().equalsIgnoreCase("http-post-body")){
			if(HTTPhost!=null && !HTTPhost.isEmpty()){
					if(!monitorItems.getHttpUrl().isEmpty()&&!monitorItems.getHttpParam().isEmpty()){
						String url = !monitorItems.getHttpUrl().substring(0,1).equals("/")?"/"+monitorItems.getHttpUrl():monitorItems.getHttpUrl();
						StopWatch stopWatch = new StopWatch();
						String response = HttpUtil.getIntance().postBody("http://" + HTTPhost + url , monitorItems.getHttpParam());
						long time = stopWatch.getElapsedTime();
						jsonObject.put("code", 0);
						jsonObject.put("time", time);
						jsonObject.put("result", response);
						return jsonObject;
					}else{
						jsonObject.put("code", 404);
						jsonObject.put("result", monitorItems.getMonitorName() + "http-post-body url or param is null");
						return jsonObject;
					}
			}else{
				jsonObject.put("code", 404);
				jsonObject.put("result", monitorItems.getMonitorName() + "host null");
				return jsonObject;
			}
		
		}
		jsonObject.put("code", 500);
		jsonObject.put("result", monitorItems.getMonitorName() + "request agreement illegal parameter");
		return jsonObject;
	}
	
	/**
	 * 解析接口返回结果
	 * @param jsonObject
	 * @param monitorAssert
	 * @param outTime
	 * @return 结果解析标识
	 * @throws Exception
	 */
	public int ResultAnalysis(JSONObject jsonObject,String monitorAssert, int outTime) throws Exception{
		if(!jsonObject.isEmpty()){
			int code = jsonObject.getIntValue("code");
			if(code==0){
				String result = jsonObject.getString("result");
				Long time = jsonObject.getLong("time");
				int timeFlag = time >= outTime ? 1 : 0;
				int resultFlag = 0;
				if(StringUtils.isEmpty(result)){
				  resultFlag=2;	
				}else{
					resultFlag=(result.indexOf(monitorAssert) == -1 ? 2: 0);
				}						
				return timeFlag + resultFlag;
			}else{
				logger.error("--------PSFOrHTTPRequest method return "+jsonObject.toString());
				return 400;
			}
		}
		return 400;
	}
	
	/**
	 * log输出与报警信息
	 * @param resultAnalysis
	 * @param model
	 * @param monitorId
	 * @param jobName
	 * @param ip
	 * @param resultTime
	 * @param url
	 * @return 返回null接口成功不需要警报
	 * @throws Exception
	 */ 
	public String inform(int resultAnalysis,String monitorSystem,int monitorId,String jobName,String ip,long resultTime,String result, String url) {
		String messageAndLog = null;
		switch(resultAnalysis) {
			case 0 : 
				messageAndLog = String.format("【易道监控平台】接口成功！所属系统:%s,监控项id:%d,监控项名称:%s,ip端口:%s,耗时:%s毫秒,接口:%s", monitorSystem,monitorId,jobName,ip,resultTime,url);
				logger.info(messageAndLog);
				; return null; 
			case 1 : 
				messageAndLog = String.format("【易道监控平台】接口超时！所属系统:%s,监控项id:%d,监控项名称:%s,ip端口:%s,耗时:%s毫秒,返回结果:%s,接口:%s", monitorSystem,monitorId,jobName,ip,resultTime,result,url);
				logger.info(messageAndLog);
				; return messageAndLog; 
			case 2 : 
				messageAndLog = String.format("【易道监控平台】接口失败！所属系统:%s,监控项名称:%s,ip端口:%s,返回结果:%s,接口:%s", monitorSystem,jobName,ip,result,url);
				logger.info(messageAndLog);
				return messageAndLog; 
			case 3 : 
				messageAndLog = String.format("【易道监控平台】接口失败与超时！所属系统:%s,监控项id:%d,监控项名称:%s,ip端口:%s,耗时:%s毫秒,返回结果:%s,接口:%s", monitorSystem,monitorId,jobName,ip,resultTime,result,url);
				logger.info(messageAndLog);
				; return messageAndLog; 
			case 400 : 
				messageAndLog = String.format("【易道监控平台】获取接口耗时与结果异常!所属系统:%s,监控项id:%d,监控项名称:%s,ip端口:%s,接口:%s", monitorSystem,monitorId,jobName,ip,url);
				logger.info(messageAndLog);
				; return messageAndLog; 
			default:
				messageAndLog = String.format("【易道监控平台】未知异常，请及时查询日志!所属系统:%s,监控项id:%d,监控项名称:%s,ip端口:%s,接口:%s", monitorSystem,monitorId,jobName,ip,url);
				logger.info(messageAndLog);
				; return messageAndLog; }
	}
	
	/**
	 * 添加错误记录到DB
	 * @param monitorExceptionRecordService
	 * @param resultAnalysis
	 * @param monitorItems
	 * @param host
	 * @param jsonObject
	 * @return
	 */
	public boolean addMonitorExceptionNotes(MonitorExceptionRecordService monitorExceptionRecordService, int resultAnalysis,MonitorItems monitorItems,String host,JSONObject jsonObject){
		String monitor_exception_type = "";
		if(resultAnalysis==1)monitor_exception_type="超时";
		if(resultAnalysis==2)monitor_exception_type="失败";
		if(resultAnalysis==3)monitor_exception_type="超时并失败";
		if(resultAnalysis==400)monitor_exception_type="位置错误";
		if(!monitor_exception_type.isEmpty()){
			MonitorExceptionRecord monitorExceptionRecord = new MonitorExceptionRecord();
			monitorExceptionRecord.setExceptionType(monitor_exception_type);
			monitorExceptionRecord.setMonitorId(monitorItems.getMonitorId());
			monitorExceptionRecord.setExceptionName(monitorItems.getMonitorName());
			monitorExceptionRecord.setExceptionHost(host);
			monitorExceptionRecord.setExceptionRequest(monitorItems.getRequestAgreement());
			if(monitorItems.getRequestAgreement().equalsIgnoreCase("psf")){
				monitorExceptionRecord.setExceptionUri(monitorItems.getPsfUri());
				monitorExceptionRecord.setExceptionData(monitorItems.getPsfData());
				monitorExceptionRecord.setExceptionPsfParam(monitorItems.getPsfUriParam());
			}else{
				monitorExceptionRecord.setExceptionUrl(monitorItems.getHttpUrl());
				monitorExceptionRecord.setExceptionParam(monitorItems.getHttpParam());
			}
			monitorExceptionRecord.setMonitorAssert(monitorItems.getMonitorAssert());
			monitorExceptionRecord.setExceptionResult(jsonObject.getString("result"));
			monitorExceptionRecord.setExceptionTimeConsuming(jsonObject.getIntValue("time"));
			monitorExceptionRecord.setExceptionOutTime(monitorItems.getMonitorTimeout());
			
			monitorExceptionRecord.setMonitorPhoneContacts(monitorItems.getMonitorPhoneContacts());
			monitorExceptionRecord.setMonitorMailContacts(monitorItems.getMonitorMailContacts());
			return monitorExceptionRecordService.addMonitorExceptionRecord(monitorExceptionRecord);
		}
		return false;
	}
			
	/**
	 * json 字符串  转 Map
	 * */
	private Map<String, String> toHashMap(String object){  
	    HashMap<String, String> data = new HashMap<String, String>();  
	    // 将json字符串转换成jsonObject  
	    JSONObject jb = JSONObject.parseObject(object);
	    // 遍历jsonObject数据，添加到Map对象  
	    Set<String> keySet = jb.keySet();
	    for (String jsonKey : keySet) {
	        String value = jb.getString(jsonKey);  
	        data.put(jsonKey, value);  
	    }
	    return data;  
	 } 
}

package com.yidao.monitor.controller;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yidao.monitor.monitorJob.MonitorSettings;
import com.yidao.monitor.monitorJob.SchedulerExecution;
import com.yidao.monitor.pojo.MonitorExceptionRecord;
import com.yidao.monitor.pojo.MonitorItems;
import com.yidao.monitor.service.MonitorContactsService;
import com.yidao.monitor.service.MonitorExceptionRecordService;
import com.yidao.monitor.service.MonitorHostsService;
import com.yidao.monitor.service.MonitorItemsService;

@Controller
@RequestMapping("/scheduler")
public class SchedulerController {

	private final static Logger logger = LoggerFactory.getLogger(SchedulerController.class); 
	@Resource
	private MonitorItemsService monitorItemsService;
	@Resource
	private MonitorHostsService monitorHostsService;
	@Resource
	private MonitorExceptionRecordService monitorExceptionRecordService;
	@Resource
	private MonitorContactsService monitorContactsService;

	
	/**
	 * 系统监控接口列表
	 * @param monitorSystem
	 * @return
	 */
	@RequestMapping("/listItems")
	@ResponseBody
	public JSONObject listItems(String monitorSystem) {
		logger.info("----进入list controller");
		System.out.println("monitorSystem:"+monitorSystem);
		List<MonitorItems> monitorItemsBySystem = monitorItemsService.getMonitorItemsBySystemAll(monitorSystem);
		JSONObject jsonObject = new JSONObject();
		if(monitorItemsBySystem!=null){
			jsonObject.put("total", monitorItemsBySystem.size());
			List<Object> list = new ArrayList<Object>();
			for (int i = 0; i < monitorItemsBySystem.size(); i++) {
				Object json = JSON.toJSON(monitorItemsBySystem.get(i));
				list.add(json);
			}
			jsonObject.put("rows",list);
			return jsonObject;
		}else{
			jsonObject.put("total", 0);
			jsonObject.put("rows", "");
			return jsonObject;
		}
	}
	
	/**
	 * 添加监控项接口
	 * @param monitorItems
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addItems")
	public JSONObject addItems(MonitorItems monitorItems) {
		JSONObject jsonObject = new JSONObject();
		if(monitorItems==null
			|| monitorItems.getMonitorCrontab()==null || monitorItems.getMonitorCrontab().isEmpty()
			|| monitorItems.getRequestAgreement()==null || monitorItems.getRequestAgreement().isEmpty()
			|| monitorItems.getMonitorAssert()==null || monitorItems.getMonitorAssert().isEmpty()){
			if(monitorItems!=null){
				logger.error(String.format("添加监控项参数异常，Crontab：%s,协议类型:%s,结果断言:%s", monitorItems.getMonitorCrontab(),monitorItems.getRequestAgreement(),monitorItems.getMonitorAssert()));
			}else{
				logger.error("添加监控项参数异常，monitorItems为空");
			}
			jsonObject.put("code", 500);
			jsonObject.put("msg", "form is null");
			return jsonObject;
		}
		if(monitorItems.getRequestAgreement().equalsIgnoreCase("psf")){
			List<String> monitorHosts = monitorHostsService.getMonitorHosts(monitorItems.getMonitorSystem(), "psf");
			String a = "";
			if(monitorHosts != null){
				for (int i = 0; i < monitorHosts.size(); i++) {
					a+=monitorHosts.get(i)+",";
				}
				a = a.substring(0, a.length()-1);
				JSONObject debugging = this.debugging(monitorItems, a);
				if(debugging.getIntValue("code")!=0){
					jsonObject.put("code", 500);
					jsonObject.put("msg", "assertion failure");
					logger.error("添加接口返回响应json错误，json："+debugging);
					return jsonObject;
				}
			}
		}else{
			List<String> monitorHosts = monitorHostsService.getMonitorHosts(monitorItems.getMonitorSystem(), "http");
			if(monitorHosts!=null && !monitorHosts.get(0).isEmpty()){
				JSONObject debugging = this.debugging(monitorItems,monitorHosts.get(0));
				if(debugging.getIntValue("code")!=0){
					jsonObject.put("code", 500);
					jsonObject.put("msg", "assertion failure");
					logger.error("添加接口返回响应json错误，json："+debugging);
					return jsonObject;
				}
			}
		}

		int addMonitorItems = monitorItemsService.addMonitorItems(monitorItems);
		if(addMonitorItems>0){
			JSONObject updateTask = this.updateTask(monitorItems.getMonitorId()+"");
			if(updateTask.getIntValue("code")==0 || updateTask.getIntValue("code")==9){
				jsonObject.put("code", 0);
				jsonObject.put("msg", "success");
				return jsonObject;
			}else{
				logger.error("任务更新方法结果返回json："+updateTask.toString());
				jsonObject.put("code", 400);
				jsonObject.put("msg", "update Job fail");
				return jsonObject;
			}
		}else{
			logger.error("DB 添加记录失败");
			jsonObject.put("code", 500);
			jsonObject.put("msg", "add monitorItems fail");
			return jsonObject;
		}
	}
	
	/**
	 * 修改监控项接口
	 * @param monitorItems
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateItems")
	public JSONObject updateItems(MonitorItems monitorItems ,Model model) {
		JSONObject jsonObject = new JSONObject();
		if(monitorItems==null){
			jsonObject.put("code", 500);
			jsonObject.put("msg", "form is null");
			return jsonObject;
		}
		boolean updateMonitorItems = monitorItemsService.updateMonitorItems(monitorItems);
		if(updateMonitorItems){
			JSONObject updateTask = this.updateTask(monitorItems.getMonitorId()+"");
			if(updateTask.getIntValue("code")==0 || updateTask.getIntValue("code")==9){
				jsonObject.put("code", 0);
				jsonObject.put("msg", "success");
				return jsonObject;
			}else{
				jsonObject.put("code", 400);
				jsonObject.put("msg", "update Job fail");
				return jsonObject;
			}
		}else{
			jsonObject.put("code", 500);
			jsonObject.put("msg", "update monitorItems fail");
			return jsonObject;
		}
	}
	/**
	 * 删除监控项
	 * @param monitorId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delItems")
	public JSONObject delItems(int monitorId) {
		JSONObject jsonObject = new JSONObject();
		if (monitorId > 0) {
			Scheduler scheduler = MonitorSettings.scheduler;				
			MonitorItems item = monitorItemsService
					.getMonitorItemsById(monitorId + "");
			if (item == null) {
				logger.error("找不到该监控项记录----" + monitorId);
				jsonObject.put("code", 500);
				jsonObject.put("mag", "monitorId not found");
				return jsonObject;
			}
			String jobName = String.format("yidaoRequestJob_%d",
					item.getMonitorId());
			String groupName = String.format("yidaoRequestGroup_%s",
					item.getMonitorSystem());
			String triggerName = String.format("yidaoRequestTrigger_%d",
					item.getMonitorId());

			JobKey jobKey = new JobKey(jobName, groupName);
			TriggerKey triggerKey = new TriggerKey(triggerName, groupName);
			JobDetail jobDetail = null;
			try {
				jobDetail = scheduler.getJobDetail(jobKey);
			} catch (SchedulerException ex) {
				jobDetail = null;
			}
		
			try {
				if (jobDetail != null) {
					// 如果任务已经存在，那么删除
					scheduler.unscheduleJob(triggerKey);
					scheduler.deleteJob(jobKey);
					logger.info(String.format("删除JOB成功!JObKey:%s", jobKey));
				}
					boolean delMonitorItems = monitorItemsService
							.delMonitorItems(monitorId);
					if (delMonitorItems) {
						jsonObject.put("code", 0);
						jsonObject.put("mag", "success");
						return jsonObject;
					} else {
						jsonObject.put("code", 400);
						jsonObject.put("mag", "delete items fail");
						return jsonObject;
					}
				} catch (SchedulerException ex) {
					logger.error(
							String.format(
									"删除JOB出现错误：%s ， jobName: %s ， groupName: %s ，jobKey:%s",
									ex.toString(), jobName, groupName,
									jobKey.toString()), ex);
					jsonObject.put("code", 500);
					jsonObject.put("mag", "SchedulerException");
					return jsonObject;
				}
			
		}else{
			jsonObject.put("code", 500);
			jsonObject.put("mag", "monitorId not found");
			return jsonObject;
		}
	}
	
	/**
	 * 更新任务接口
	 * @param monitorId 监控项id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateTask")
	public JSONObject updateTask(String monitorId) {
		JSONObject jsonObject = new JSONObject();
		try {

			Scheduler scheduler = MonitorSettings.scheduler;				
			MonitorItems item = monitorItemsService.getMonitorItemsById(monitorId);
			if(item==null){
				jsonObject.put("code", 400);
				jsonObject.put("msg", "record was not found");
				logger.info("找不到该监控项记录----"+jsonObject.toString());
				return jsonObject;
			}
			String jobName = String.format("yidaoRequestJob_%d", item.getMonitorId());
			String groupName = String.format("yidaoRequestGroup_%s",
					item.getMonitorSystem());
			String triggerName = String.format("yidaoRequestTrigger_%d",
					 item.getMonitorId());

			JobKey jobKey = new JobKey(jobName, groupName);
			TriggerKey triggerKey = new TriggerKey(triggerName, groupName);
			Trigger trigger = null;
			try {
				trigger = scheduler.getTrigger(triggerKey);
			} catch (SchedulerException ex) {
				trigger = null;
			}
			JobDetail jobDetail = null;
			try {
				jobDetail = scheduler.getJobDetail(jobKey);
			} catch (SchedulerException ex) {
				jobDetail = null;
			}
			if (jobDetail != null) {
				try {
					// 如果任务已经存在，那么删除
					scheduler.unscheduleJob(triggerKey);
					scheduler.deleteJob(jobKey);
					logger.info(String.format("删除JOB成功!JObKey:%s", jobKey));
				} catch (SchedulerException ex) {
					logger.error(String.format("删除JOB出现错误：%s ， jobName: %s ， groupName: %s ，jobKey:%s",
									ex.toString(), jobName, groupName,
									jobKey.toString()), ex);
				}
			}
			if (item.getMonitorStart()!=1) {
				jsonObject.put("code", 9);
				jsonObject.put("monitorId", item.getMonitorId());
				jsonObject.put("msg", "The job is not open");
				logger.info(jobName+"----"+jsonObject);
			}else{
				// 初始化JOB
				jobDetail = newJob(SchedulerExecution.class).withIdentity(jobName,
						groupName).build();
				// 设置要传递的参数
				jobDetail.getJobDataMap().put("monitorItems",item);
	            jobDetail.getJobDataMap().put("monitorHostsService",monitorHostsService);
	            jobDetail.getJobDataMap().put("monitorExceptionRecordService",monitorExceptionRecordService);
	            jobDetail.getJobDataMap().put("monitorContactsService",monitorContactsService);
	            jobDetail.getJobDataMap().put("monitorItemsService",monitorItemsService);
				// 初始化触发器
				trigger = newTrigger().withIdentity(triggerName, groupName)
						.withSchedule(cronSchedule(item.getMonitorCrontab()))
						.forJob(jobName, groupName).build();
				try {
					scheduler.scheduleJob(jobDetail, trigger);
					scheduler.start();
					logger.info(String.format("启动JOB成功!JObKey:%s", jobKey));
				} catch (Exception ex) {
					logger.error(String.format("任务运行出现错误：%s", ex.toString()), ex);
				}
				jsonObject.put("code", 0);
				jsonObject.put("monitorId", monitorId);
				jsonObject.put("msg", "Update job success");
				logger.info(jobName+"----"+jsonObject.toString());
			}
		} catch (Exception e) {
			jsonObject.put("code", 500);
			jsonObject.put("monitorId", monitorId);
			jsonObject.put("msg", "updateTask Exception");
			logger.error( "异常"+e+",updateTask----"+jsonObject.toString());
		}
		return jsonObject;
	}

	/**
	 * 暂停/恢复接口
	 * @param monitorId
	 * @param type
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pauseRecovery")
	public JSONObject pauseRecovery(String monitorId,String start) {
		int parseInt = Integer.parseInt(start);
		JSONObject jsonObject = new JSONObject();
		//parseInt=1 暂停
		if(parseInt==1){
				Scheduler scheduler = null;
				try {
					scheduler = new StdSchedulerFactory().getScheduler();
					MonitorItems item = monitorItemsService.getMonitorItemsById(monitorId);
					if(item==null){
						jsonObject.put("code", 400);
						jsonObject.put("msg", "record was not found");
						logger.info(jsonObject.toString());
						return jsonObject;
					}
					String jobName = String.format("yidaoRequestJob_%d",item.getMonitorId());
					String groupName = String.format("yidaoRequestGroup_%s",item.getMonitorSystem());
					JobKey jobKey = new JobKey(jobName, groupName);
					boolean updateMonitorStart = monitorItemsService.updateMonitorStart(2, Integer.parseInt(monitorId));
					if(updateMonitorStart){
						scheduler.pauseJob(jobKey);// 停止任务
						jsonObject.put("code", 0);
						jsonObject.put("msg", "Success stopJob");
					}else{
						jsonObject.put("code", 500);
						jsonObject.put("msg", "DB update stop Exception");
						logger.info("异常---"+jsonObject.toString());
					}
				} catch (Exception e) {
					jsonObject.put("code", 500);
					jsonObject.put("msg", "stopJob Exception");
					logger.info("异常---"+jsonObject.toString());
					e.printStackTrace();
				}
			return jsonObject;
		}
		//parseInt=2 恢复
		if (parseInt == 2) {
			Scheduler scheduler = null;
			try {
				scheduler = new StdSchedulerFactory().getScheduler();
				MonitorItems item = monitorItemsService.getMonitorItemsById(monitorId);
				if(item==null){
					jsonObject.put("code", 400);
					jsonObject.put("msg", "record was not found");
					return jsonObject;
				}
				String jobName = String.format("yidaoRequestJob_%d",item.getMonitorId());
				String groupName = String.format("yidaoRequestGroup_%s",item.getMonitorSystem());
				JobKey jobKey = new JobKey(jobName, groupName);
				boolean updateMonitorStart = monitorItemsService.updateMonitorStart(1, Integer.parseInt(monitorId));
				if(updateMonitorStart){
					scheduler.resumeJob(jobKey);// 恢复任务
					jsonObject.put("code", 0);
					jsonObject.put("msg", "Success resumeJob");
				}else{
					jsonObject.put("code", 500);
					jsonObject.put("msg", "DB update start Exception");
					logger.info("异常---"+jsonObject.toString());
				}
			} catch (Exception e) {
				jsonObject.put("code", 500);
				jsonObject.put("msg", "resumeJob Exception");
				logger.info("异常---"+jsonObject.toString());
				e.printStackTrace();
			}
			return jsonObject;
		}
	jsonObject.put("code", 9);
	jsonObject.put("msg", "Monitor Job not start");
	return jsonObject;
	}
	
	/**
	 * 接口调试
	 * @param monitorItems
	 * @param hosts
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/debugging")
	public JSONObject debugging(MonitorItems monitorItems, String testHost) {
		JSONObject jsonObject = new JSONObject();
		try {
			 SchedulerExecution schedulerExecution = new SchedulerExecution();
			if (monitorItems != null && monitorItems.getRequestAgreement().equalsIgnoreCase("psf")) {
				List<String> monitorHosts = monitorHostsService.getMonitorHosts(monitorItems.getMonitorSystem(), "psf");
				String[] array = new String[2];
				for (int i = 0; i < monitorHosts.size(); i++) {
					array[i]=monitorHosts.get(i);
				}
				JSONObject psfOrHTTPRequest =schedulerExecution.PSFOrHTTPRequest(null, array, monitorItems);
				int resultAnalysis = schedulerExecution.ResultAnalysis(psfOrHTTPRequest, monitorItems.getMonitorAssert(), monitorItems.getMonitorTimeout());
				if(resultAnalysis == 0 || resultAnalysis == 1){
					return psfOrHTTPRequest;
				}else{
					psfOrHTTPRequest.put("code", 400);
					return psfOrHTTPRequest;
				}
			}else if(monitorItems != null){
				JSONObject psfOrHTTPRequest =schedulerExecution.PSFOrHTTPRequest(testHost, null, monitorItems);
				int resultAnalysis = schedulerExecution.ResultAnalysis(psfOrHTTPRequest, monitorItems.getMonitorAssert(), monitorItems.getMonitorTimeout());
				if(resultAnalysis == 0 || resultAnalysis == 1){
					return psfOrHTTPRequest;
				}else{
					psfOrHTTPRequest.put("code", 400);
					jsonObject.put("result", "Debugging Error");
					return psfOrHTTPRequest;
				}
			}else{
				jsonObject.put("code", 500);
				jsonObject.put("result", "Debugging Error");
				return	jsonObject;
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", 500);
			jsonObject.put("result", "Debugging Exception");
			return jsonObject;
		}
	}
	
	/**
	 * 查看监控项跳转弹窗
	 * @param monitorId
	 * @param model
	 * @return
	 */
	@RequestMapping("/selectMonitor")
	public String selectMonitorById(String monitorId,Map<String,Object> model) {
		//基本信息
		MonitorItems monitorItems = monitorItemsService.getMonitorItemsById(monitorId);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//时间戳格式化
		String monitorItemsCreateTime = df.format(monitorItems.getMonitorCreateTime());
		String monitorItemsLastUpdateTime = df.format(monitorItems.getMonitorLastUpdateTime());
		//联系人
  //		if(monitorItems.getMonitorPhoneContacts()!=null && !monitorItems.getMonitorPhoneContacts().isEmpty()){
  //		System.out.println("contact:"+monitorItems.getMonitorPhoneContacts());
			
	/*  alter by wangfang ,查看现在是按照模块来的，不需要有邮箱和手机号的查询*/
	//		List<MonitorContacts> monitorPhoneContacts = monitorContactsService.getMonitorContacts(monitorItems.getMonitorPhoneContacts());
	//		List<MonitorContacts> monitorMailContacts = monitorContactsService.getMonitorContacts(monitorItems.getMonitorMailContacts());
	//		List<MonitorContacts> monitorPhoneContacts = monitorContactsService.getMonitorContactsByType(monitorItems.getMonitorPhoneContacts());
	//		List<MonitorContacts> monitorMailContacts = monitorContactsService.getMonitorContactsByType(monitorItems.getMonitorMailContacts());
	//		model.put("monitorPhoneContacts",monitorPhoneContacts);
	//		model.put("monitorMailContacts",monitorMailContacts);
	//	}
		//该接口最近错误信息展示
		List<MonitorExceptionRecord> monitorExceptionByMonitorId = monitorExceptionRecordService.getMonitorExceptionByMonitorId(monitorItems.getMonitorId());
		model.put("monitorItems",monitorItems);
		System.out.println("monitorExceptionByMonitorId:"+monitorExceptionByMonitorId+"MonitorId:"+monitorId);
		if(monitorExceptionByMonitorId==null || monitorExceptionByMonitorId.isEmpty()){
			model.put("monitorException",0);
		}else{
			model.put("monitorException",1);
		}
		model.put("monitorExceptionByMonitorId",monitorExceptionByMonitorId);
		model.put("monitorItemsCreateTime",monitorItemsCreateTime);
		model.put("monitorItemsLastUpdateTime",monitorItemsLastUpdateTime);
		return "selectMonitor";
	}
	
	@RequestMapping("/listTest")
	public String list(String monitorSystem,Map<String,Object> model) {
		System.out.println("listTest:"+monitorSystem);
		model.put("monitorSystem",monitorSystem);
		return "list";
	}
	
	@RequestMapping("/homepage")
	public String homepage(Map<String,Object> model) {
		return "homepage";
	}
	
	@RequestMapping("/toAddMonitor")
	public String toAddMonitor(String monitorSystem,Map<String,Object> model) {
		try {
			List<String> MonitorHosts = monitorHostsService.getMonitorHosts(monitorSystem, "http");
			model.put("MonitorHosts",MonitorHosts);
			model.put("monitorSystem",monitorSystem);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "addMonitor";
	}
	
	@RequestMapping("/editMonitor")
	public String editMonitor(String monitorId,Map<String,Object> model) {
		try {
			MonitorItems monitorItems = monitorItemsService.getMonitorItemsById(monitorId);
			model.put("monitorItems",monitorItems);
			List<String> MonitorHosts = monitorHostsService.getMonitorHosts(monitorItems.getMonitorSystem(), "http");
			model.put("MonitorHosts",MonitorHosts);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "editMonitor";
	}
}

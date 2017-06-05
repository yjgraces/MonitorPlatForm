package com.yidao.monitor.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yidao.monitor.pojo.MonitorContacts;
import com.yidao.monitor.service.MonitorContactsService;

@RequestMapping("contacts")
@Controller
public class ContactsController {

	private final static Logger logger = LoggerFactory.getLogger(SchedulerController.class); 
	@Resource
	private MonitorContactsService monitorContactsService;
	
	@RequestMapping("toContactsList")
	public String toContactsList(String monitorSystem, Map<String,Object> model){
		List<MonitorContacts> monitorContacts = monitorContactsService.getMonitorContacts(monitorSystem);
		model.put("monitorSystem", monitorSystem);
		model.put("monitorContacts", monitorContacts);
		model.put("monitorContactsCount", monitorContacts.size());
		return "contactsList";
	}
	@RequestMapping("toAddContacts")
	public String toAddContacts(String monitorSystem, Map<String,Object> model){
		model.put("monitorSystem", monitorSystem);
		return "addContacts";
	}
	@ResponseBody
	@RequestMapping("addContacts")
	public String addContacts(MonitorContacts monitorContacts, Map<String,Object> model){
		if (monitorContacts == null
				|| monitorContacts.getContactsName() == null
				|| monitorContacts.getContactsName().isEmpty()) {
			logger.error("addContacts parameter null monitorContacts="+monitorContacts);
			return "parameter null";
		}
		boolean addMonitorContacts = monitorContactsService.addMonitorContacts(monitorContacts);
		if(addMonitorContacts){
			return "success";
		}else{
			return "Insert DB fail";
		}
	}
	@RequestMapping("toUpdateContacts")
	public String toUpdateContacts(String contactsId, Map<String,Object> model){
		MonitorContacts monitorContacts = monitorContactsService.getMonitorContactsById(contactsId);
		model.put("monitorContacts", monitorContacts);
		return "editContacts";
	}
	@ResponseBody
	@RequestMapping("updateContacts")
	public String updateContacts(MonitorContacts monitorContacts, Map<String,Object> model){
		if (monitorContacts == null
				|| monitorContacts.getContactsName() == null
				|| monitorContacts.getContactsName().isEmpty()) {
			logger.error("updateContacts parameter null monitorContacts="+monitorContacts);
			return "parameter null";
		}
		boolean addMonitorContacts = monitorContactsService.updateMonitorContacts(monitorContacts);
		if(addMonitorContacts){
			return "success";
		}else{
			return "Update DB fail";
		}
	}
	
	@ResponseBody
	@RequestMapping("/delContactsItem")
	public String delHostItem(String contactsId,String monitorSystem){
		if(contactsId==null || contactsId.isEmpty()){
			logger.error("delHostItem parameter null contactsId="+contactsId);
			return "非法参数";
		} 
		boolean delMonitorHost = monitorContactsService.delMonitorContacts(contactsId);
		if(delMonitorHost){
			return "success";
		}else{
			return "删除失败";
		}
	}
}

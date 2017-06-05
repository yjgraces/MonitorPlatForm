package com.yidao.monitor.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yidao.monitor.common.HttpHeader;
import com.yidao.monitor.pojo.MonitorSystem;
import com.yidao.monitor.service.MonitorSystemService;

@Controller
@RequestMapping("/system")
public class SystemController {

	@Resource
	private MonitorSystemService monitorSystemService;
	
	@RequestMapping("/getSystem")
	public String getSystemList(Map<String,Object> model){
		List<MonitorSystem> startMonitorSystem = monitorSystemService.getStartMonitorSystem();
		model.put("startMonitorSystem", startMonitorSystem);
		return "homepage";
	}
	
	@RequestMapping("/toAddSystem")
	public String toAddSystem(){
		return "addSystem";
	}
	
	@ResponseBody
	@RequestMapping("/addSystem")
	public String addSystem(MonitorSystem monitorSystem, Map<String,Object> model){
		if(monitorSystem!=null && monitorSystem.getSystemId()!=null && !monitorSystem.getSystemId().isEmpty()){
			boolean addMonitorSystem = monitorSystemService.addMonitorSystem(monitorSystem);
			if(addMonitorSystem){
				return "success";
			}else{
				return "Insert DB fail";
			}
		}else{
			return "parameter null";
		}
	}
	
	@RequestMapping("/systemList")
	public String systemList(Map<String,Object> model){
		List<MonitorSystem> monitorSystem = monitorSystemService.getStartMonitorSystem();
		model.put("monitorSystem", monitorSystem);
		model.put("monitorSystemCount", monitorSystem.size());
		return "systemList";
	}
	
	@ResponseBody
	@RequestMapping("/delSystem")
	public String delSystem(String systemId){
		if(systemId!=null && !systemId.isEmpty()){
			boolean delMonitorSystem = monitorSystemService.delMonitorSystem(systemId);
			if(delMonitorSystem){
				return "success";
			}else{
				return "DELETE DB fail";
			}
		}else{
			return "parameter null";
		}
	}
}

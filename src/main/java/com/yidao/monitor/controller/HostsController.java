package com.yidao.monitor.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yidao.monitor.pojo.MonitorHosts;
import com.yidao.monitor.service.MonitorHostsService;

@Controller
@RequestMapping("/hosts")
public class HostsController {

	private final static Logger logger = LoggerFactory.getLogger(HostsController.class); 
	@Resource
	private MonitorHostsService monitorHostsService;
	
	@RequestMapping("/hostsItems")
	public String systemItems(String monitorSystem, Map<String,Object> model){
		List<MonitorHosts> psfHostArray = monitorHostsService.getMonitorHostsAll(monitorSystem, "psf");
		model.put("psfHostArray", psfHostArray);
		model.put("psfHostCount", psfHostArray.size());
		List<MonitorHosts> httpHostArray = monitorHostsService.getMonitorHostsAll(monitorSystem, "http");
		model.put("httpHostArray", httpHostArray);
		model.put("httpHostCount", httpHostArray.size());
		model.put("monitorSystem", monitorSystem);
		return "hostList";
	}
	
	@RequestMapping("/toAddHostItems")
	public String toAddHostItems(String hostSystem, Map<String,Object> model){
		model.put("hostSystem", hostSystem);
		return "addHost";
	}
	
	@RequestMapping("/toUpdateHostItems")
	public String toUpdateHostItems(String hostId, Map<String,Object> model){
		MonitorHosts monitorHosts = monitorHostsService.getMonitorHostsById(hostId);
		model.put("monitorHosts", monitorHosts);
		return "editHost";
	}
	
	@ResponseBody
	@RequestMapping("/addHosts")
	public String addHosts(MonitorHosts monitorHosts) {
		if (monitorHosts == null || monitorHosts.getMonitorHost() == null
				||monitorHosts.getMonitorHost().isEmpty()) {
			logger.error("updateHosts 非法参数！");
			return "fail";
		}
		boolean addMonitorHost = monitorHostsService.addMonitorHost(monitorHosts);
		if(addMonitorHost) return "success";
		logger.error("Host信息添加DB失败,HostsId:"+monitorHosts.getHostId());
		return "fail";
		
	}
	
	@ResponseBody
	@RequestMapping("/updateHosts")
	public String updateHosts(MonitorHosts monitorHosts) {
		if (monitorHosts == null || monitorHosts.getMonitorHost() == null
				||monitorHosts.getMonitorHost().isEmpty()) {
			logger.error("updateHosts 非法参数！");
			return "fail";
		}
		boolean updateMonitorHost = monitorHostsService.updateMonitorHost(monitorHosts);
		if(updateMonitorHost) return "success";
		logger.error("Host信息修改DB失败,HostsId:"+monitorHosts.getHostId());
		return "fail";
	}
	
	@ResponseBody
	@RequestMapping("/delHostItem")
	public String delHostItem(String hostId,String monitorSystem){
		if(hostId==null || hostId.isEmpty())  return "非法参数";
		boolean delMonitorHost = monitorHostsService.delMonitorHost(hostId);
		if(delMonitorHost){
			return "success";
		}else{
			return "删除失败";
		}
	}
	
}

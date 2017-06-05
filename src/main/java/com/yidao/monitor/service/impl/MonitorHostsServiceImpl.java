package com.yidao.monitor.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yidao.monitor.controller.HostsController;
import com.yidao.monitor.controller.SchedulerController;
import com.yidao.monitor.dao.MonitorHostsDao;
import com.yidao.monitor.dao.MonitorItemsDao;
import com.yidao.monitor.pojo.MonitorHosts;
import com.yidao.monitor.pojo.MonitorItems;
import com.yidao.monitor.service.MonitorHostsService;
import com.yidao.monitor.service.MonitorItemsService;

@Service
public class MonitorHostsServiceImpl implements MonitorHostsService {

	@Resource
	private MonitorHostsDao monitorHostsDao;

	@Override
	public List<MonitorHosts> getMonitorSystem(String monitorSystem) {
		return monitorHostsDao.getMonitorSystem(monitorSystem);
	}

	@Override
	public List<String> getMonitorHosts(String monitorSystem, String serverype) {
		return monitorHostsDao.getMonitorHosts(monitorSystem, serverype);
	}

	@Override
	public List<MonitorHosts> getMonitorHostsAll(String monitorSystem,
			String serverype) {
		return monitorHostsDao.getMonitorHostsAll(monitorSystem, serverype);
	}

	@Override
	public boolean addMonitorHost(MonitorHosts monitorHosts) {
		return monitorHostsDao.addMonitorHost(monitorHosts);
	}

	@Override
	public boolean updateMonitorHost(MonitorHosts monitorHosts) {
		return monitorHostsDao.updateMonitorHost(monitorHosts);
	}

	@Override
	public MonitorHosts getMonitorHostsById(String hostId) {
		return monitorHostsDao.getMonitorHostsById(hostId);
	}

	@Override
	public boolean delMonitorHost(String hostId) {
		return monitorHostsDao.delMonitorHost(hostId);
	}

}

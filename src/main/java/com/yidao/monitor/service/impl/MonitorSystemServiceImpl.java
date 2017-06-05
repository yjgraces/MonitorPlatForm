package com.yidao.monitor.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yidao.monitor.dao.MonitorSystemDao;
import com.yidao.monitor.pojo.MonitorSystem;
import com.yidao.monitor.service.MonitorSystemService;
@Service
public class MonitorSystemServiceImpl implements MonitorSystemService {

	@Resource
	private MonitorSystemDao monitorSystemDao;
	
	@Override
	public MonitorSystem getMonitorSystem(String systemId) {
		return monitorSystemDao.getMonitorSystem(systemId);
	}

	@Override
	public String getSystemName(String systemId) {
		return monitorSystemDao.getSystemName(systemId);
	}

	@Override
	public List<MonitorSystem> getStartMonitorSystem() {
		return monitorSystemDao.getStartMonitorSystem();
	}

	@Override
	public boolean addMonitorSystem(MonitorSystem monitorSystem) {
		return monitorSystemDao.addMonitorSystem(monitorSystem);
	}

	@Override
	public boolean delMonitorSystem(String systemId) {
		return monitorSystemDao.delMonitorSystem(systemId);
	}

}

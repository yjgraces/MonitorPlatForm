package com.yidao.monitor.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yidao.monitor.dao.MonitorItemsDao;
import com.yidao.monitor.pojo.MonitorItems;
import com.yidao.monitor.service.MonitorItemsService;

@Service
public class MonitorItemsServiceImpl implements MonitorItemsService{

	@Resource
	private MonitorItemsDao monitorItemsDao;
	
	@Override
	public List<MonitorItems> getMonitorItems() {
		return monitorItemsDao.getMonitorItems();
	}

	@Override
	public List<MonitorItems> getMonitorItemsBySystem(String monitorSystem) {
		return monitorItemsDao.getMonitorItemsBySystem(monitorSystem);
	}

	@Override
	public MonitorItems getMonitorItemsById(String monitorId) {
		return monitorItemsDao.getMonitorItemsById(monitorId);
	}

	@Override
	public int addMonitorItems(MonitorItems monitorItems) {
		return monitorItemsDao.addMonitorItems(monitorItems);
	}

	@Override
	public boolean delMonitorItems(int monitorId) {
		return monitorItemsDao.delMonitorItems(monitorId);
	}

	@Override
	public List<MonitorItems> getMonitorItemsBySystemAll(String monitorSystem) {
		return monitorItemsDao.getMonitorItemsBySystemAll(monitorSystem);
	}

	@Override
	public boolean updateMonitorItems(MonitorItems monitorItems) {
		return monitorItemsDao.updateMonitorItems(monitorItems);
	}

	@Override
	public boolean updateMonitorStart(int monitorStart, int monitorId) {
		return monitorItemsDao.updateMonitorStart(monitorStart, monitorId);
	}

	@Override
	public boolean correctFlag() {
		return monitorItemsDao.correctFlag();
	}

	@Override
	public List<Integer> getMonitorIdBySystem(String monitorSystem) {
		return monitorItemsDao.getMonitorIdBySystem(monitorSystem);
	}
}

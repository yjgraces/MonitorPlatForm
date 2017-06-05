package com.yidao.monitor.service;

import java.util.List;

import com.yidao.monitor.pojo.MonitorItems;

public interface MonitorItemsService {

	public List<MonitorItems> getMonitorItems();
	public List<MonitorItems> getMonitorItemsBySystem(String monitorSystem);
	public List<MonitorItems> getMonitorItemsBySystemAll(String monitorSystem);
	public MonitorItems getMonitorItemsById(String monitorId);
	public int addMonitorItems(MonitorItems monitorItems);
	public boolean delMonitorItems(int monitorId);
	public boolean updateMonitorItems(MonitorItems monitorItems);
	public boolean updateMonitorStart(int monitorStart,int monitorId);
	public boolean correctFlag();
	public List<Integer> getMonitorIdBySystem(String monitorSystem);
}

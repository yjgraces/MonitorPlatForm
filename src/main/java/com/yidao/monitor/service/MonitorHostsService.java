package com.yidao.monitor.service;

import java.util.List;

import com.yidao.monitor.pojo.MonitorHosts;

public interface MonitorHostsService {

	public List<MonitorHosts> getMonitorSystem(String monitorSystem);
	public List<String> getMonitorHosts(String monitorSystem,String serverype);
	public List<MonitorHosts> getMonitorHostsAll(String monitorSystem,String serverype);
	public boolean addMonitorHost(MonitorHosts monitorHosts);
	public boolean updateMonitorHost(MonitorHosts monitorHosts);
	public MonitorHosts getMonitorHostsById(String hostId);
	public boolean delMonitorHost(String hostId);
}

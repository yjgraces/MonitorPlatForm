package com.yidao.monitor.service;


import java.util.List;

import com.yidao.monitor.pojo.MonitorSystem;

public interface MonitorSystemService {

	public MonitorSystem getMonitorSystem(String systemId);
	public String getSystemName(String systemId);
	public List<MonitorSystem> getStartMonitorSystem();
	public boolean addMonitorSystem(MonitorSystem monitorSystem);
	public boolean delMonitorSystem(String systemId);
}

package com.yidao.monitor.service;

import java.util.List;

import com.yidao.monitor.pojo.MonitorExceptionRecord;

public interface MonitorExceptionRecordService {

	public boolean addMonitorExceptionRecord(MonitorExceptionRecord monitorExceptionRecord);
	public List<MonitorExceptionRecord> getMonitorExceptionByMonitorId(int monitorId);
}

package com.yidao.monitor.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yidao.monitor.dao.MonitorExceptionRecordDao;
import com.yidao.monitor.pojo.MonitorExceptionRecord;
import com.yidao.monitor.service.MonitorExceptionRecordService;

@Service
public class MonitorExceptionRecordServiceImpl implements
		MonitorExceptionRecordService {

	@Resource
	private MonitorExceptionRecordDao monitorExceptionRecordDao;
	
	@Override
	public boolean addMonitorExceptionRecord(
			MonitorExceptionRecord monitorExceptionRecord) {
		return monitorExceptionRecordDao.addMonitorExceptionRecord(monitorExceptionRecord);
	}

	@Override
	public List<MonitorExceptionRecord> getMonitorExceptionByMonitorId(
			int monitorId) {
		return monitorExceptionRecordDao.getMonitorExceptionByMonitorId(monitorId);
	}

}

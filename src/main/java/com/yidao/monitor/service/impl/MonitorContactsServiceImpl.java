package com.yidao.monitor.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yidao.monitor.dao.MonitorContactsDao;
import com.yidao.monitor.pojo.MonitorContacts;
import com.yidao.monitor.service.MonitorContactsService;

@Service
public class MonitorContactsServiceImpl implements MonitorContactsService{

	@Resource
	private MonitorContactsDao monitorContactsDao;
	
	@Override
	public List<MonitorContacts> getMonitorContacts(String contactsSystem) {
		return monitorContactsDao.getMonitorContacts(contactsSystem);
	}

	@Override
	public List<MonitorContacts> getMonitorContactsByType(String contactsIds) {
		return monitorContactsDao.getMonitorContactsByType(contactsIds);
	}

	@Override
	public boolean addMonitorContacts(MonitorContacts monitorContacts) {
		return monitorContactsDao.addMonitorContacts(monitorContacts);
	}

	@Override
	public MonitorContacts getMonitorContactsById(String contactsIds) {
		return monitorContactsDao.getMonitorContactsById(contactsIds);
	}

	@Override
	public boolean updateMonitorContacts(MonitorContacts monitorContacts) {
		return monitorContactsDao.updateMonitorContacts(monitorContacts);
	}

	@Override
	public boolean delMonitorContacts(String contactsId) {
		return monitorContactsDao.delMonitorContacts(contactsId);
	}

}

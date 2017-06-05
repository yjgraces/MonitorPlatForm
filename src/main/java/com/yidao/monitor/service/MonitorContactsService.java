package com.yidao.monitor.service;

import java.util.List;

import com.yidao.monitor.pojo.MonitorContacts;

public interface MonitorContactsService {

	public List<MonitorContacts> getMonitorContacts(String contactsSystem);
	public List<MonitorContacts> getMonitorContactsByType(String contactsIds);
	public boolean addMonitorContacts(MonitorContacts monitorContacts);
	public MonitorContacts getMonitorContactsById(String contactsIds);
	public boolean updateMonitorContacts(MonitorContacts monitorContacts);
	public boolean delMonitorContacts(String contactsId);
}

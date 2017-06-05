package com.yidao.monitor.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.yidao.monitor.pojo.MonitorContacts;

public interface MonitorContactsDao {

	static final String ID = "contacts_id";
	
	static final String FIELD = "contacts_name,"+"contacts_system,"+"contacts_start,"+"contacts_phone,"
			+"contacts_mail,"+"contacts_time";
			
	static final String SELECTID = "contacts_id as contactsId";
	
	static final String SELECTFIELD = "contacts_name as contactsName,"+"contacts_system as contactsSystem,"
	+"contacts_start as contactsStart,"+"contacts_phone as contactsPhone,"+"contacts_mail as contactsMail,"
	+"contacts_time as contactsTime";
	
	
	@Select("SELECT "+SELECTID+","+SELECTFIELD+" FROM monitor_contacts where contacts_system=#{0}")
	public List<MonitorContacts> getMonitorContacts(String contactsSystem);
	
	@Select("SELECT "+SELECTID+","+SELECTFIELD+" FROM monitor_contacts where contacts_id in (${contactsIds})")
	public List<MonitorContacts> getMonitorContactsByType(@Param("contactsIds")String contactsIds);
	
	@Select("SELECT "+SELECTID+","+SELECTFIELD+" FROM monitor_contacts where contacts_id=#{0}")
	public MonitorContacts getMonitorContactsById(String contactsIds);
	
	@Insert("INSERT INTO monitor_contacts ("+FIELD+") VALUES (#{contactsName}, #{contactsSystem}, #{contactsStart}, #{contactsPhone}, #{contactsMail}, now())")
	public boolean addMonitorContacts(MonitorContacts monitorContacts);
	
	@Update("UPDATE monitor_contacts SET contacts_name=#{contactsName}, contacts_system=#{contactsSystem}, contacts_start=#{contactsStart}, contacts_phone=#{contactsPhone}, contacts_mail=#{contactsMail}, contacts_time=now() WHERE contacts_id=#{contactsId}")
	public boolean updateMonitorContacts(MonitorContacts monitorContacts);
	
	@Delete("DELETE FROM monitor_contacts WHERE contacts_id=#{0}")
	public boolean delMonitorContacts(String contactsId);
}

package com.yidao.monitor.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.yidao.monitor.pojo.MonitorItems;


public interface MonitorItemsDao {

	static final String ID = "monitor_id";
	static final String FIELD = "monitor_system,"+"monitor_name,"
				+"request_agreement,"+"http_url,"+"http_param,"+"psf_uri,"
				+"psf_uri_param,"+"psf_data,"+"psf_service_type,"+"monitor_crontab,"+"monitor_timeout,"
				+"monitor_assert,"+"monitor_start,"+"monitor_create_time,"
				+"monitor_last_update_time,"+"monitor_phone_contacts,"+"monitor_mail_contacts,failTimes";
	
	static final String SELECTID = "monitor_id as monitorId";
	static final String SELECTFIELD = "monitor_system as monitorSystem,"+"monitor_name as monitorName,"
			+"request_agreement as requestAgreement,"+"http_url as httpUrl,"+"http_param as httpParam,"+"psf_uri as psfUri,"
			+"psf_uri_param as psfUriParam,"+"psf_data as psfData,"+"psf_service_type as psfServiceType,"+"monitor_crontab as monitorCrontab,"+"monitor_timeout as monitorTimeout,"
			+"monitor_assert as monitorAssert,"+"monitor_start as monitorStart,"+"monitor_create_time as monitorCreateTime,"
			+"monitor_last_update_time as monitorLastUpdateTime,"+"monitor_phone_contacts as monitorPhoneContacts,"+"monitor_mail_contacts as monitorMailContacts"+",failTimes as failTimes";
	
	@Select("SELECT "+SELECTID+","+SELECTFIELD+" FROM monitor_items where monitor_start=1")
	public List<MonitorItems> getMonitorItems();
	
	@Select("SELECT "+SELECTID+","+SELECTFIELD+" FROM monitor_items where monitor_start=1 and monitor_system=#{0}")
	public List<MonitorItems> getMonitorItemsBySystem(String monitorSystem);
	
	@Select("SELECT "+SELECTID+" FROM monitor_items where monitor_system=#{0}")
	public List<Integer> getMonitorIdBySystem(String monitorSystem);
	
	@Select("SELECT "+SELECTID+","+SELECTFIELD+" FROM monitor_items where monitor_system=#{0}")
	public List<MonitorItems> getMonitorItemsBySystemAll(String monitorSystem);
	
	@Select("SELECT "+SELECTID+","+SELECTFIELD+" FROM monitor_items where monitor_id=#{0}")
	public MonitorItems getMonitorItemsById(String monitorId);
	
	@Insert("INSERT INTO monitor_items ("+FIELD+") VALUES " +
			"(#{monitorItems.monitorSystem},#{monitorItems.monitorName},#{monitorItems.requestAgreement},#{monitorItems.httpUrl},#{monitorItems.httpParam},#{monitorItems.psfUri},#{monitorItems.psfUriParam}," +
			"#{monitorItems.psfData},#{monitorItems.psfServiceType},#{monitorItems.monitorCrontab},#{monitorItems.monitorTimeout},#{monitorItems.monitorAssert},#{monitorItems.monitorStart},now()," +
			"now(),#{monitorItems.monitorPhoneContacts},#{monitorItems.monitorMailContacts},0);")
	@Options(useGeneratedKeys = true, keyProperty ="monitorItems.monitorId")
	public int addMonitorItems(@Param("monitorItems")MonitorItems monitorItems);
	
	@Update("UPDATE monitor_items SET "+
			"monitor_system=#{monitorSystem},monitor_name=#{monitorName},request_agreement=#{requestAgreement},http_url=#{httpUrl},http_param=#{httpParam},psf_uri=#{psfUri},psf_uri_param=#{psfUriParam}," +
			"psf_data=#{psfData},psf_service_type=#{psfServiceType},monitor_crontab=#{monitorCrontab},monitor_timeout=#{monitorTimeout},monitor_assert=#{monitorAssert},monitor_start=#{monitorStart}," +
			"monitor_last_update_time=now(),monitor_phone_contacts=#{monitorPhoneContacts},monitor_mail_contacts=#{monitorMailContacts} ,failTimes=#{failTimes} WHERE monitor_id=#{monitorId};")
	public boolean updateMonitorItems(MonitorItems monitorItems);
	
	@Update("UPDATE monitor_items SET monitor_start=#{0} WHERE monitor_id=#{1};")
	public boolean updateMonitorStart(int monitorStart,int monitorId);
	
	@Update("UPDATE monitor_items SET monitor_start=0 WHERE monitor_start<>0 and monitor_start<>1;")
	public boolean correctFlag();
	
	@Delete("DELETE FROM monitor_items WHERE "+ID+"=#{0}")
	public boolean delMonitorItems(int monitorId);
}

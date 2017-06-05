package com.yidao.monitor.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.yidao.monitor.pojo.MonitorExceptionRecord;
public interface MonitorExceptionRecordDao {

	static final String ID = "exception_id";

	static final String FIELD = "monitor_id,"+"exception_type,"+"exception_name,"+"exception_host,"
			+"exception_request,"+"exception_url,"+"exception_param,"+"exception_uri,"
			+"exception_data,"+"exception_psf_param,"+"monitor_assert,"+"exception_result,"
			+"exception_out_time,"+"exception_time_consuming,"+"exception_time,"
			+"monitor_phone_contacts,"+"monitor_mail_contacts";
	
	static final String SELECTID = "exception_id as exceptionId";
	
	static final String SELECTFIELD = "monitor_id as monitorId,"+"exception_type as exceptionType,"+"exception_name as exceptionName,"+"exception_host as exceptionHost,"
			+"exception_request as exceptionRequest,"+"exception_url as exceptionUrl,"+"exception_param as exceptionParam,"+"exception_uri as exceptionUri,"
			+"exception_data as exceptionData,"+"exception_psf_param as exceptionPsfParam,"+"monitor_assert as monitorAssert,"+"exception_result as exceptionResult,"
			+"exception_out_time as exceptionOutTime,"+"exception_time_consuming as exceptionTimeConsuming,"+"exception_time as exceptionTime,"
			+"monitor_phone_contacts as monitorPhoneContacts,"+"monitor_mail_contacts as monitorMailContacts";
	
	@Insert("INSERT INTO monitor_exception_record ("+FIELD+") VALUES (#{monitorId},#{exceptionType},#{exceptionName},#{exceptionHost},#{exceptionRequest},#{exceptionUrl},#{exceptionParam},#{exceptionUri},#{exceptionData},#{exceptionPsfParam},#{monitorAssert},#{exceptionResult},#{exceptionOutTime},#{exceptionTimeConsuming},now(),#{monitorPhoneContacts},#{monitorMailContacts})")
	public boolean addMonitorExceptionRecord(MonitorExceptionRecord monitorExceptionRecord);
	
	@Select("SELECT "+SELECTID+","+SELECTFIELD+" FROM monitor_exception_record where monitor_id=#{0} order by exception_time desc limit 0,9")
	public List<MonitorExceptionRecord> getMonitorExceptionByMonitorId(int monitorId);
	
}

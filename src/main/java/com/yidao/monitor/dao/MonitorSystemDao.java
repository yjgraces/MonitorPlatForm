package com.yidao.monitor.dao;


import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.yidao.monitor.pojo.MonitorSystem;

public interface MonitorSystemDao {

	static final String ID = "system_id";
	static final String SELECTID = "system_id as systemId";
	
	static final String FIELD = "system_name,system_start,create_time";
	static final String SELECTFIELD = "system_name as systemName,system_start as systemStart,create_time as createTime";
	
	@Select("SELECT "+SELECTID+","+SELECTFIELD+" FROM monitor_system where system_id=#{0}")
	public MonitorSystem getMonitorSystem(String systemId);
	
	@Select("SELECT system_name as systemName  FROM monitor_system where system_id=#{0}")
	public String getSystemName(String systemId);
	
	@Select("SELECT "+SELECTID+","+SELECTFIELD+" FROM monitor_system")
	public List<MonitorSystem> getAllMonitorSystem();
	
	@Select("SELECT "+SELECTID+","+SELECTFIELD+" FROM monitor_system where system_start=1")
	public List<MonitorSystem> getStartMonitorSystem();
	
	@Insert("INSERT INTO monitor_system ("+ID+","+FIELD+") VALUES (#{systemId}, #{systemName}, #{systemStart}, now())")
	public boolean addMonitorSystem(MonitorSystem monitorSystem);

	@Delete("DELETE FROM monitor_system WHERE system_id=#{0}")
	public boolean delMonitorSystem(String systemId);
}

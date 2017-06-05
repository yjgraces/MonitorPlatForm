package com.yidao.monitor.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.yidao.monitor.pojo.MonitorHosts;

public interface MonitorHostsDao {

	static final String ID = "host_id";
	static final String FIELD = "monitor_system,"
			+"monitor_host,"+"server_type,"+"host_start,"+"create_time";
	
	static final String SELECTID = "host_id as hostId";
	static final String SELECTFIELD = "monitor_system as monitorSystem,"+"monitor_host as monitorHost,"
			+"server_type as serverType,"+"host_start as hostStart,"+"create_time as createTime";
	
	@Select("SELECT "+SELECTID+","+SELECTFIELD+" FROM monitor_hosts where host_start=1 and monitor_system=#{0}")
	public List<MonitorHosts> getMonitorSystem(String monitorSystem);
	
	@Select("SELECT monitor_host FROM monitor_hosts where host_start=1 and monitor_system=#{0} and server_type=#{1}")
	public List<String> getMonitorHosts(String monitorSystem,String serverype);
	
	@Select("SELECT "+SELECTID+","+SELECTFIELD+" FROM monitor_hosts where monitor_system=#{0} and server_type=#{1}")
	public List<MonitorHosts> getMonitorHostsAll(String monitorSystem,String serverype);

	@Select("SELECT "+SELECTID+","+SELECTFIELD+" FROM monitor_hosts where host_id=#{0}")
	public MonitorHosts getMonitorHostsById(String hostId);
	
	@Insert("INSERT INTO monitor_hosts ("+FIELD+") VALUES (#{monitorSystem}, #{monitorHost}, #{serverType}, #{hostStart}, now())")
	public boolean addMonitorHost(MonitorHosts monitorHosts);

	@Update("UPDATE monitor_hosts SET monitor_system=#{monitorSystem}, monitor_host=#{monitorHost}, server_type=#{serverType}, host_start=#{hostStart} WHERE host_id=#{hostId}")
	public boolean updateMonitorHost(MonitorHosts monitorHosts);
	
	@Delete("DELETE FROM monitor_hosts WHERE host_id=#{0}")
	public boolean delMonitorHost(String hostId);

}
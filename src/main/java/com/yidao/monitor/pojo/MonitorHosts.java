package com.yidao.monitor.pojo;

import java.util.Date;

/**
 * 监控系统bean
 * @author Administrator
 */

public class MonitorHosts {

	/**---hostid---*/
	private int hostId;
	
	/**---监控系统(模块)---*/
	private String monitorSystem;
	
	/**---系统host（ip+port）---*/
	private String monitorHost;
	
	/**---服务器类型（psf or http）---*/
	private String serverType;
	
	/**---host是否使用---*/
	private int hostStart;
	
	/**---创建时间---*/
	private Date createTime;

	public int getHostId() {
		return hostId;
	}

	public void setHostId(int hostId) {
		this.hostId = hostId;
	}

	public String getMonitorSystem() {
		return monitorSystem;
	}

	public void setMonitorSystem(String monitorSystem) {
		this.monitorSystem = monitorSystem;
	}

	public String getMonitorHost() {
		return monitorHost;
	}

	public void setMonitorHost(String monitorHost) {
		this.monitorHost = monitorHost;
	}

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	public int getHostStart() {
		return hostStart;
	}

	public void setHostStart(int hostStart) {
		this.hostStart = hostStart;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}

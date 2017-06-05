package com.yidao.monitor.pojo;

import java.util.Date;

public class MonitorSystem {

	/**---系统（模块）唯一标识--*/
	private String systemId;
	/**---系统（模块）名称--*/
	private String systemName;
	/**---状态--*/
	private int systemStart;
	/**---创建时间--*/
	private Date createTime;
	
	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public int getSystemStart() {
		return systemStart;
	}
	public void setSystemStart(int systemStart) {
		this.systemStart = systemStart;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}

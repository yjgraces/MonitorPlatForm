package com.yidao.monitor.pojo;

import java.util.Date;
/**
 * 监控项bean
 * @author weilingjie
 */
public class MonitorItems {

	/**------监控项id-------*/
	private int monitorId;
	
	/**------监控项所属系统-------*/
	private String monitorSystem;
	
	/**------监控项名称-------*/
	private String monitorName;
	
	/**------监控项请求类型-------*/
	private String requestAgreement;
	
	/**------监控项http url-------*/
	private String httpUrl;
	
	/**------监控项http 参数-------*/
	private String httpParam;
	
	/**------监控项psf uri-------*/
	private String psfUri;
	
	/**------监控项psf get格式参数-------*/
	private String psfUriParam;
	
	/**------监控项psf json格式参数-------*/
	private String 	psfData;
	
	/**------监控项psf 系统类型-------*/
	private String 	psfServiceType;

	/**------监控项crontab时间表达式-------*/
	private String monitorCrontab;
	
	/**------监控项超时时间-------*/
	private int monitorTimeout;
	
	/**------监控项期望返回结果-------*/
	private String monitorAssert;
	
	/**------监控项是否启动-------*/
	private int monitorStart;
	
	/**------监控项创建时间-------*/
	private Date monitorCreateTime;
	
	/**------监控项最后修改时间-------*/
	private Date monitorLastUpdateTime;
	
	/**------监控项报警组短信联系人-------*/
	private String monitorPhoneContacts;
	
	/**------监控项报警组邮件联系人-------*/
	private String monitorMailContacts;
	private int failTimes;

	public int getFailTimes() {
		return failTimes;
	}

	public void setFailTimes(int failTimes) {
		this.failTimes = failTimes;
	}

	public int getMonitorId() {
		return monitorId;
	}

	public void setMonitorId(int monitorId) {
		this.monitorId = monitorId;
	}

	public String getMonitorSystem() {
		return monitorSystem;
	}

	public void setMonitorSystem(String monitorSystem) {
		this.monitorSystem = monitorSystem.trim();
	}

	public String getMonitorName() {
		return monitorName;
	}

	public void setMonitorName(String monitorName) {
		this.monitorName = monitorName.trim();
	}

	public String getRequestAgreement() {
		return requestAgreement;
	}

	public void setRequestAgreement(String requestAgreement) {
		this.requestAgreement = requestAgreement.trim();
	}

	public String getHttpUrl() {
		return httpUrl;
	}

	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl.trim();
	}

	public String getHttpParam() {
		return httpParam;
	}

	public void setHttpParam(String httpParam) {
		this.httpParam = httpParam.trim();
	}

	public String getPsfUri() {
		return psfUri;
	}

	public void setPsfUri(String psfUri) {
		this.psfUri = psfUri.trim();
	}

	public String getPsfUriParam() {
		return psfUriParam;
	}

	public void setPsfUriParam(String psfUriParam) {
		this.psfUriParam = psfUriParam.trim();
	}

	public String getPsfData() {
		return psfData;
	}

	public void setPsfData(String psfData) {
		this.psfData = psfData.trim();
	}

	public String getPsfServiceType() {
		return psfServiceType;
	}

	public void setPsfServiceType(String psfServiceType) {
		this.psfServiceType = psfServiceType.trim();
	}

	public String getMonitorCrontab() {
		return monitorCrontab;
	}

	public void setMonitorCrontab(String monitorCrontab) {
		this.monitorCrontab = monitorCrontab.trim();
	}

	public int getMonitorTimeout() {
		return monitorTimeout;
	}

	public void setMonitorTimeout(int monitorTimeout) {
		this.monitorTimeout = monitorTimeout;
	}

	public String getMonitorAssert() {
		return monitorAssert;
	}

	public void setMonitorAssert(String monitorAssert) {
		this.monitorAssert = monitorAssert;
	}

	public int getMonitorStart() {
		return monitorStart;
	}

	public void setMonitorStart(int monitorStart) {
		this.monitorStart = monitorStart;
	}

	public Date getMonitorCreateTime() {
		return monitorCreateTime;
	}

	public void setMonitorCreateTime(Date monitorCreateTime) {
		this.monitorCreateTime = monitorCreateTime;
	}

	public Date getMonitorLastUpdateTime() {
		return monitorLastUpdateTime;
	}

	public void setMonitorLastUpdateTime(Date monitorLastUpdateTime) {
		this.monitorLastUpdateTime = monitorLastUpdateTime;
	}

	public String getMonitorPhoneContacts() {
		return monitorPhoneContacts;
	}

	public void setMonitorPhoneContacts(String monitorPhoneContacts) {
		this.monitorPhoneContacts = monitorPhoneContacts.trim();
	}

	public String getMonitorMailContacts() {
		return monitorMailContacts;
	}

	public void setMonitorMailContacts(String monitorMailContacts) {
		this.monitorMailContacts = monitorMailContacts.trim();
	}
}

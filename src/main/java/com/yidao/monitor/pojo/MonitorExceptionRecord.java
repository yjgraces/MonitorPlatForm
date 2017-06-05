package com.yidao.monitor.pojo;

import java.util.Date;
/**
 * 异常记录bean
 * @author weilingjie
 *
 */
public class MonitorExceptionRecord {

	/**---异常id--*/
	private int exceptionId;
	/**---监控项id--*/
	private int monitorId;
	/**---异常类型--*/
	private String exceptionType;
	/**---异常名称--*/
	private String exceptionName;
	/**---异常所属host--*/
	private String exceptionHost;
	/**---异常请求方式--*/
	private String exceptionRequest;
	/**---异常http url--*/
	private String exceptionUrl;
	/**---异常http 参数--*/
	private String exceptionParam;
	/**---异常psf uri--*/
	private String exceptionUri;
	/**---异常psf data（get格式）--*/
	private String exceptionData;
	/**---异常psf json参数--*/
	private String exceptionPsfParam;
	/**---该接口正常断言结果--*/
	private String monitorAssert;
	/**---接口异常返回--*/
	private String exceptionResult;
	/**---超时时间--*/
	private long exceptionOutTime;
	/**---接口耗时--*/
	private long exceptionTimeConsuming;
	/**---异常创建时间--*/
	private Date exceptionTime;
	/**---异常对应所属联系人phone--*/
	private String monitorPhoneContacts;
	/**---异常对应所属联系人mail--*/
	private String monitorMailContacts;
	
	public int getExceptionId() {
		return exceptionId;
	}
	public void setExceptionId(int exceptionId) {
		this.exceptionId = exceptionId;
	}
	public int getMonitorId() {
		return monitorId;
	}
	public void setMonitorId(int monitorId) {
		this.monitorId = monitorId;
	}
	public String getExceptionType() {
		return exceptionType;
	}
	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}
	public String getExceptionName() {
		return exceptionName;
	}
	public void setExceptionName(String exceptionName) {
		this.exceptionName = exceptionName;
	}
	public String getExceptionHost() {
		return exceptionHost;
	}
	public void setExceptionHost(String exceptionHost) {
		this.exceptionHost = exceptionHost;
	}
	public String getExceptionRequest() {
		return exceptionRequest;
	}
	public void setExceptionRequest(String exceptionRequest) {
		this.exceptionRequest = exceptionRequest;
	}
	public String getExceptionUrl() {
		return exceptionUrl;
	}
	public void setExceptionUrl(String exceptionUrl) {
		this.exceptionUrl = exceptionUrl;
	}
	public String getExceptionParam() {
		return exceptionParam;
	}
	public void setExceptionParam(String exceptionParam) {
		this.exceptionParam = exceptionParam;
	}
	public String getExceptionUri() {
		return exceptionUri;
	}
	public void setExceptionUri(String exceptionUri) {
		this.exceptionUri = exceptionUri;
	}
	public String getExceptionData() {
		return exceptionData;
	}
	public void setExceptionData(String exceptionData) {
		this.exceptionData = exceptionData;
	}
	public String getExceptionPsfParam() {
		return exceptionPsfParam;
	}
	public void setExceptionPsfParam(String exceptionPsfParam) {
		this.exceptionPsfParam = exceptionPsfParam;
	}
	public String getMonitorAssert() {
		return monitorAssert;
	}
	public void setMonitorAssert(String monitorAssert) {
		this.monitorAssert = monitorAssert;
	}
	public String getExceptionResult() {
		return exceptionResult;
	}
	public void setExceptionResult(String exceptionResult) {
		this.exceptionResult = exceptionResult;
	}
	public long getExceptionOutTime() {
		return exceptionOutTime;
	}
	public void setExceptionOutTime(long exceptionOutTime) {
		this.exceptionOutTime = exceptionOutTime;
	}
	public long getExceptionTimeConsuming() {
		return exceptionTimeConsuming;
	}
	public void setExceptionTimeConsuming(long exceptionTimeConsuming) {
		this.exceptionTimeConsuming = exceptionTimeConsuming;
	}
	public Date getExceptionTime() {
		return exceptionTime;
	}
	public void setExceptionTime(Date exceptionTime) {
		this.exceptionTime = exceptionTime;
	}
	public String getMonitorPhoneContacts() {
		return monitorPhoneContacts;
	}
	public void setMonitorPhoneContacts(String monitorPhoneContacts) {
		this.monitorPhoneContacts = monitorPhoneContacts;
	}
	public String getMonitorMailContacts() {
		return monitorMailContacts;
	}
	public void setMonitorMailContacts(String monitorMailContacts) {
		this.monitorMailContacts = monitorMailContacts;
	}
}

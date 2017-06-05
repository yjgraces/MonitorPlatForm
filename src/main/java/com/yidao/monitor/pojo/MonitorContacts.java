package com.yidao.monitor.pojo;

import java.util.Date;
/**
 * 报警组联系人bean
 * @author weilingjie
 */
public class MonitorContacts {

	/**-----联系人id-----*/
	private int contactsId;
	/**-----联系人姓名-----*/
	private String contactsName;
	/**-----联系人所属系统-----*/
	private String contactsSystem;
	/**-----联系人状态-----*/
	private int contactsStart;
	/**-----联系人手机-----*/
	private String contactsPhone;
	/**-----联系人邮箱-----*/
	private String contactsMail;
	/**-----创建时间-----*/
	private Date contactsTime;
	
	public int getContactsId() {
		return contactsId;
	}
	public void setContactsId(int contactsId) {
		this.contactsId = contactsId;
	}
	public String getContactsName() {
		return contactsName;
	}
	public void setContactsName(String contactsName) {
		this.contactsName = contactsName;
	}
	public String getContactsSystem() {
		return contactsSystem;
	}
	public void setContactsSystem(String contactsSystem) {
		this.contactsSystem = contactsSystem;
	}
	public int getContactsStart() {
		return contactsStart;
	}
	public void setContactsStart(int contactsStart) {
		this.contactsStart = contactsStart;
	}
	public String getContactsPhone() {
		return contactsPhone;
	}
	public void setContactsPhone(String contactsPhone) {
		this.contactsPhone = contactsPhone;
	}
	public String getContactsMail() {
		return contactsMail;
	}
	public void setContactsMail(String contactsMail) {
		this.contactsMail = contactsMail;
	}
	public Date getContactsTime() {
		return contactsTime;
	}
	public void setContactsTime(Date contactsTime) {
		this.contactsTime = contactsTime;
	}
}

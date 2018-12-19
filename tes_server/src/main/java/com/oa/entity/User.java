package com.oa.entity;

import java.util.Date;

public class User {
	private String UUID;
	private String uName;
	private String sex;
	private String account;
	private String pwdMD5;
	private String emailAddr;
	private String aesKey;
	private Date joinDate;
	private String touchAddr;
	private String ipLast;
	private String ipLastIn;
	private Date timeLast;
	private String mobileTel;
	private Date creatTime;
	private Date editTime;
	private int flag;
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public String getuName() {
		return uName;
	}
	public void setuName(String uName) {
		this.uName = uName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPwdMD5() {
		return pwdMD5;
	}
	public void setPwdMD5(String pwdMD5) {
		this.pwdMD5 = pwdMD5;
	}
	public String getEmailAddr() {
		return emailAddr;
	}
	public void setEmailAddr(String emailAddr) {
		this.emailAddr = emailAddr;
	}
	public String getAesKey() {
		return aesKey;
	}
	public void setAesKey(String aesKey) {
		this.aesKey = aesKey;
	}
	public Date getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}
	public String getTouchAddr() {
		return touchAddr;
	}
	public void setTouchAddr(String touchAddr) {
		this.touchAddr = touchAddr;
	}
	public String getIpLast() {
		return ipLast;
	}
	public void setIpLast(String ipLast) {
		this.ipLast = ipLast;
	}
	public String getIpLastIn() {
		return ipLastIn;
	}
	public void setIpLastIn(String ipLastIn) {
		this.ipLastIn = ipLastIn;
	}
	public Date getTimeLast() {
		return timeLast;
	}
	public void setTimeLast(Date timeLast) {
		this.timeLast = timeLast;
	}
	public String getMobileTel() {
		return mobileTel;
	}
	public void setMobileTel(String mobileTel) {
		this.mobileTel = mobileTel;
	}
	public Date getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}
	public Date getEditTime() {
		return editTime;
	}
	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
}

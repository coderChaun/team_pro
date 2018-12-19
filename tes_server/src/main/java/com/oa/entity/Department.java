package com.oa.entity;

import java.util.Date;

public class Department {
	private String dId;
	private String dName;
	private String dAddr;
	private String upDid;
	private String levelCode;
	private Date creatTime;
	private Date edittTime;
	private int count;
	private int flag;
	public String getdId() {
		return dId;
	}
	public void setdId(String dId) {
		this.dId = dId;
	}
	public String getdName() {
		return dName;
	}
	public void setdName(String dName) {
		this.dName = dName;
	}
	public String getdAddr() {
		return dAddr;
	}
	public void setdAddr(String dAddr) {
		this.dAddr = dAddr;
	}
	public String getUpDid() {
		return upDid;
	}
	public void setUpDid(String upDid) {
		this.upDid = upDid;
	}
	public String getLevelCode() {
		return levelCode;
	}
	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}
	public Date getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}
	public Date getEdittTime() {
		return edittTime;
	}
	public void setEdittTime(Date edittTime) {
		this.edittTime = edittTime;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
}

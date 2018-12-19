package com.oa.entity;

import java.util.Date;

public class Position {
	private String UUID;
	private String positionName;
	private int position_level;
	private Date creatTime;
	private Date edittTime;
	private String depUUID;
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	public int getPosition_level() {
		return position_level;
	}
	public void setPosition_level(int position_level) {
		this.position_level = position_level;
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
	public String getDepUUID() {
		return depUUID;
	}
	public void setDepUUID(String depUUID) {
		this.depUUID = depUUID;
	}
	public String getPositionPid() {
		return positionPid;
	}
	public void setPositionPid(String positionPid) {
		this.positionPid = positionPid;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	private String positionPid;
	private int flag;
}

package com.oa.entity;

import java.util.Date;
import java.util.List;

public class Work {
	private String UUID;
	private String title;
	private String titleSecond;
	private String content;
	private int rangeHour;
	private Department depart;
	private List<Department> departId;
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitleSecond() {
		return titleSecond;
	}
	public void setTitleSecond(String titleSecond) {
		this.titleSecond = titleSecond;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getRangeHour() {
		return rangeHour;
	}
	public void setRangeHour(int rangeHour) {
		this.rangeHour = rangeHour;
	}
	public Department getDepart() {
		return depart;
	}
	public void setDepart(Department depart) {
		this.depart = depart;
	}
	public List<Department> getDepartId() {
		return departId;
	}
	public void setDepartId(List<Department> departId) {
		this.departId = departId;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
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
	public String getUserUUID() {
		return UserUUID;
	}
	public void setUserUUID(String userUUID) {
		UserUUID = userUUID;
	}
	private int flag;
	private Date creatTime;
	private Date edittTime;
	private String UserUUID;
}

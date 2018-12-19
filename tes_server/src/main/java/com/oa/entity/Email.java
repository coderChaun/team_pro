package com.oa.entity;

import java.util.Date;

public class Email {
	private String UUID;
	private String title;
	private String titleSecond;
	private String content;
	private String sendUserUUID;
	private String receiveUserUUID;
	private int mailFlag;
	private Date sendTime;
	private Date creatTime;
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
	public String getSendUserUUID() {
		return sendUserUUID;
	}
	public void setSendUserUUID(String sendUserUUID) {
		this.sendUserUUID = sendUserUUID;
	}
	public String getReceiveUserUUID() {
		return receiveUserUUID;
	}
	public void setReceiveUserUUID(String receiveUserUUID) {
		this.receiveUserUUID = receiveUserUUID;
	}
	public int getMailFlag() {
		return mailFlag;
	}
	public void setMailFlag(int mailFlag) {
		this.mailFlag = mailFlag;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
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
	private Date edittTime;
}


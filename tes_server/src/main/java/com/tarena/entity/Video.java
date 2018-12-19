package com.tarena.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class Video implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String title;
	private Integer special;
	private String forsale;
	private Long count;
	private String introduction;
	private String picture;
	private String picturecc;
	private String fileName;
	private String videoUrlcc;
	private int state;
	private Timestamp ontime;
	private Integer difficulty;
	private String md5;
	private String tag;
	private Long fileSize;
	private User user;
	private Course course;
	private String first;
	private String metaurl;
	private String chunkurl;
	private String ccvid;
	private String servicetype;
	
	private String categoryId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getSpecial() {
		return special;
	}

	public void setSpecial(Integer special) {
		this.special = special;
	}

	public String getForsale() {
		return forsale;
	}

	public void setForsale(String forsale) {
		this.forsale = forsale;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getPicturecc() {
		return picturecc;
	}

	public void setPicturecc(String picturecc) {
		this.picturecc = picturecc;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getVideoUrlcc() {
		return videoUrlcc;
	}

	public void setVideoUrlcc(String videoUrlcc) {
		this.videoUrlcc = videoUrlcc;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Timestamp getOntime() {
		return ontime;
	}

	public void setOntime(Timestamp ontime) {
		this.ontime = ontime;
	}

	public Integer getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Integer difficulty) {
		this.difficulty = difficulty;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getMetaurl() {
		return metaurl;
	}

	public void setMetaurl(String metaurl) {
		this.metaurl = metaurl;
	}

	public String getChunkurl() {
		return chunkurl;
	}

	public void setChunkurl(String chunkurl) {
		this.chunkurl = chunkurl;
	}

	public String getCcvid() {
		return ccvid;
	}

	public void setCcvid(String ccvid) {
		this.ccvid = ccvid;
	}

	public String getServicetype() {
		return servicetype;
	}

	public void setServicetype(String servicetype) {
		this.servicetype = servicetype;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public String toString() {
		return "Video [id=" + id + ", title=" + title + ", special=" + special + ", forsale=" + forsale + ", count="
				+ count + ", introduction=" + introduction + ", picture=" + picture + ", picturecc=" + picturecc
				+ ", fileName=" + fileName + ", videoUrlcc=" + videoUrlcc + ", state=" + state + ", ontime=" + ontime
				+ ", difficulty=" + difficulty + ", md5=" + md5 + ", tag=" + tag + ", fileSize=" + fileSize + ", user="
				+ user + ", course=" + course + ", first=" + first + ", metaurl=" + metaurl + ", chunkurl=" + chunkurl
				+ ", ccvid=" + ccvid + ", servicetype=" + servicetype + ", categoryId=" + categoryId + "]";
	}
	
}

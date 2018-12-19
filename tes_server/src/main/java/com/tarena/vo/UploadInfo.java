package com.tarena.vo;

import java.io.Serializable;

public class UploadInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String videoId;
	private String userId;
	private String metaurl;
	private String chunkurl;
	private String msgerror;
	public String getVideoId() {
		return videoId;
	}
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public String getMsgerror() {
		return msgerror;
	}
	public void setMsgerror(String msgerror) {
		this.msgerror = msgerror;
	}

}

package com.tarena.vo;

import java.io.Serializable;

public class VideoBlockMessage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private  String userId;
	private String videoId;
	private int length;
	private int blockIndex;
	private int blockNumber;
	private byte[] data;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getVideoId() {
		return videoId;
	}
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getBlockIndex() {
		return blockIndex;
	}
	public void setBlockIndex(int blockIndex) {
		this.blockIndex = blockIndex;
	}
	public int getBlockNumber() {
		return blockNumber;
	}
	public void setBlockNumber(int blockNumber) {
		this.blockNumber = blockNumber;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	
}

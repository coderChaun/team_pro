package com.tarena.vo;

import java.io.Serializable;

public class ReceiveInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private int result;
	private String msg;
	private int blockNumber;//总共的文件块数
	private int blockIndex;//已经上传完的最后的块的索引
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getBlockNumber() {
		return blockNumber;
	}
	public void setBlockNumber(int blockNumber) {
		this.blockNumber = blockNumber;
	}
	public int getBlockIndex() {
		return blockIndex;
	}
	public void setBlockIndex(int blockIndex) {
		this.blockIndex = blockIndex;
	}
	
}

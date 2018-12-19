package com.tarena.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.annotation.JsonAppend.Attr;

@Component("ccKeyUtil")
public class CCKeyUtil {
	@Value("#{props.appKey}")
	private String appKey;
	@Value("#{props.categoryId}")
	private String categoryId;
	@Value("#{props.blockSize}")
	private int blockSize;
	@Value("#{props.userId}")
	private String userId;
	@Value("#{props.notifyUrl}")
	private String notifyUrl;
	public String getAppKey() {
		return appKey;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public int getBlockSize() {
		return blockSize;
	}
	public String getUserId() {
		return userId;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	

}

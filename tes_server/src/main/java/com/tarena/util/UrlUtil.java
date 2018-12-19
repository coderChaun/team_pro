package com.tarena.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("urlUtil")
public class UrlUtil {
	@Value("#{props.host}")
	private String host;
	@Value("#{props.port}")
	private String port;
	@Value("#{props.serverName}")
	private String serverName;
	public String getMetaurl(){
		return "http://"+host+":"+port+"/"+serverName+"/video/uploadmeta";
	}
	public String getChunkurl(){
		return "http://"+host+":"+port+"/"+serverName+"/video/chunk";
	}
	
}

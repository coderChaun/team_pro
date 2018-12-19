package com.tarena.test;

import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSON;

public class TestFreeClass {
	private String host="http://localhost:8080/";
	private String basePath="tes_server/";
	@Test
	public void testLogin(){
		//用httpClient模拟http请求,来访问服务端
		String content=null;
		Map<String,Object> contentMap=null;
		try{
			//用httpClient模拟http请求,相当于用浏览器发送请求
			HttpClient httpClient=HttpClients.createDefault();
			HttpGet httpGet=new HttpGet(host+basePath+"/free/loginName/wt_zss@126.com/loginPassword/123");
			//发送一个get请求,返回的结果是一个HttpResponse响应对象
			HttpResponse response=httpClient.execute(httpGet);
			int resStatus=response.getStatusLine().getStatusCode();
			System.out.println("status="+resStatus);
			if(resStatus==HttpStatus.SC_OK){
				//sc_ok=200,说明响应状态码 是200
				//从响应对象中获取响应体内容
				HttpEntity entity=response.getEntity();
				content=EntityUtils.toString(entity);
			    //关闭httpClient的连接
				httpClient.getConnectionManager().shutdown();
			}
			System.out.println("content="+content);
			if(content!=null){
				contentMap=(Map<String,Object>)JSON.parse(content.trim());
				System.out.println("contentMap="+contentMap);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Test
	public void testAddUser(){
		//用httpClient模拟http请求,来访问服务端
		String content=null;
		Map<String,Object> contentMap=null;
		try{
			//用httpClient模拟http请求,相当于用浏览器发送请求
			HttpClient httpClient=HttpClients.createDefault();
			HttpPost httpPost=new HttpPost(host+basePath+"/free/registerUser");
			
			//给post提交方式添加提交的数据
			MultipartEntityBuilder multipartEntityBuilder=MultipartEntityBuilder.create();
			multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			
			//解决用post提交的中文乱码问题
			ContentType contentType=ContentType.create(HTTP.PLAIN_TEXT_TYPE,HTTP.UTF_8);
						
			//给post提交的请求体添加提交的数据
			multipartEntityBuilder.addTextBody("loginName", "测试111@126.com",contentType);
			multipartEntityBuilder.addTextBody("password", "123",contentType);
			
			httpPost.setEntity(multipartEntityBuilder.build());
			
			//发送一个post请求,返回的结果是一个HttpResponse响应对象
			HttpResponse response=httpClient.execute(httpPost);
			int resStatus=response.getStatusLine().getStatusCode();
			System.out.println("status="+resStatus);
			if(resStatus==HttpStatus.SC_OK){
				//sc_ok=200,说明响应状态码 是200
				//从响应对象中获取响应体内容
				HttpEntity entity=response.getEntity();
				content=EntityUtils.toString(entity);
			    //关闭httpClient的连接
				httpClient.getConnectionManager().shutdown();
			}
			System.out.println("content="+content);
			if(content!=null){
				contentMap=(Map<String,Object>)JSON.parse(content.trim());
				System.out.println("contentMap="+contentMap);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

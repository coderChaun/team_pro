package com.tarena.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.tarena.util.BlockStreamBody;
import com.tarena.util.Md5Util;
import com.tarena.util.UploadFileUtil;

public class TestUploadController {
	/**
	 * 专门用来给post方式添加提交的数据(仅限文本)
	 * @param params 提交的数据
	 * @param multipartEntityBuilder  把提交的数据存储到此对象中
	 */
	private void buildMultipartEntity(
			Map<String,String> params,
			MultipartEntityBuilder multipartEntityBuilder){
		//解决找中文乱码问题
		ContentType contentType=ContentType.create(HTTP.PLAIN_TEXT_TYPE,HTTP.UTF_8);
		
		//把map集合参数的key转换成list集合
		List<String> keys=new ArrayList<String>(params.keySet());
		Collections.sort(keys,String.CASE_INSENSITIVE_ORDER);
		for(Iterator<String> iterator=keys.iterator();iterator.hasNext();){
			String key=iterator.next();
			String value=params.get(key);
			if(StringUtils.isNoneBlank(value)){
				multipartEntityBuilder.addTextBody(key, value,contentType);
			}
		}
	}

	//创建视频的方法
	/**
	 * 1.创建视频
	 * @param params  服务器需要的参数
	 * @param serverUrlCreate  服务器的url地址
	 * @return  返回结果
	 *          vidoeId
	 *          userId
	 *          metaUrl
	 *          chunkUrl
	 *          msgerror
	 */
	public Map<String,String> videoCreate(
			Map<String,String> params,
			String serverUrlCreate){
		Map<String,String> returnMap=null;
		String content=null;
		try{
			HttpClient httpClient=HttpClients.createDefault();
			HttpPost httpPost=new HttpPost(serverUrlCreate);
			//给post方式添加提交的数据
			MultipartEntityBuilder multipartEntityBuilder=MultipartEntityBuilder.create();
			multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			//用buildMultipartEntity方法存储post中的数据					
			
			this.buildMultipartEntity(params,multipartEntityBuilder);
			
			httpPost.setEntity(multipartEntityBuilder.build());
			
			HttpResponse response=httpClient.execute(httpPost);
			System.out.println(response.getStatusLine().getStatusCode());
			if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
				HttpEntity entity=response.getEntity();
				content=EntityUtils.toString(entity);
				httpClient.getConnectionManager().shutdown();
			}
			//System.out.println("content-->"+content);
			if(content!=null){
				returnMap=(Map<String,String>)JSON.parse(content.trim());
				//System.out.println("conentMap-->"+returnMap);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return returnMap;
	}
	//上传元数据的方法
	/**
	 * 上传视频文件的元数据
	 * @param params  服务器需要的参数
	 * @param videoPicture  视频的截图
	 * @param serverUrl  服务器url地址
	 * @return  返回的数据
	 *          result
	 *          msg
	 *          blockNumber
	 *          blockIndex
	 */
	public Map<String,Object> uploadMetaData(
			Map<String,String> params,
			File videoPicture,
			String serverUrl){
		Map<String,Object> returnMap=null;
		String content=null;
		try{
			HttpClient httpClient=HttpClients.createDefault();
			HttpPost httpPost=new HttpPost(serverUrl);
			//给post方式添加提交的数据
			MultipartEntityBuilder multipartEntityBuilder=MultipartEntityBuilder.create();
			multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			//用buildMultipartEntity方法存储post中的数据					
			//添加文本
			this.buildMultipartEntity(params,multipartEntityBuilder);
			//添加文件
			ContentBody contentBody=new FileBody(videoPicture);
			multipartEntityBuilder.addPart("addPicture",contentBody);
						
			httpPost.setEntity(multipartEntityBuilder.build());
			
			HttpResponse response=httpClient.execute(httpPost);
			System.out.println(response.getStatusLine().getStatusCode());
			if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
				HttpEntity entity=response.getEntity();
				content=EntityUtils.toString(entity);
				httpClient.getConnectionManager().shutdown();
			}
			//System.out.println("content-->"+content);
			if(content!=null){
				returnMap=(Map<String,Object>)JSON.parse(content.trim());
				//System.out.println("conentMap-->"+returnMap);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return returnMap;
	}
	
	//上传文件块的方法
	/**
	 * 3.上传文件块
	 * @param params  服务器需要的参数
	 * @param filePath  文件实际路径,是用来从文件中定位某一个文件块
	 * @param serverUrl  服务器url地址
	 * @return 返回的数据
	 *       result
	 *       msg
	 *       blockNumber
	 *       blockIndex
	 */
	public Map<String,String> uploadToServer(
			Map<String,String> params,
			String filePath,
			String serverUrl){
		Map<String,String> returnMap=null;
		String content=null;
		try{
			HttpClient httpClient=HttpClients.createDefault();
			HttpPost httpPost=new HttpPost(serverUrl);
			//给post方式添加提交的数据
			MultipartEntityBuilder multipartEntityBuilder=MultipartEntityBuilder.create();
			multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			//用buildMultipartEntity方法存储post中的数据					
			//添加文本
			this.buildMultipartEntity(params,multipartEntityBuilder);
			//添加文件,是整个视频的文件的某一块(4M)
			//视频文件的总块数
			int blockNumber=Integer.parseInt(params.get("blockNumber"));
			//正在上传的那个文件的块的索引,在此处文件块还没上传190行才真正上传
			int blockIndex=Integer.parseInt(params.get("blockIndex"));
			//构建文件块(4M)
			ContentBody contentBody=new BlockStreamBody(blockNumber,blockIndex,new File(filePath));
			multipartEntityBuilder.addPart("file",contentBody);
			multipartEntityBuilder.setCharset(CharsetUtils.get("UTF-8"));
			
			httpPost.setEntity(multipartEntityBuilder.build());
			
			HttpResponse response=httpClient.execute(httpPost);
			System.out.println(response.getStatusLine().getStatusCode());
			if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
				HttpEntity entity=response.getEntity();
				content=EntityUtils.toString(entity);
				httpClient.getConnectionManager().shutdown();
			}
			//System.out.println("content-->"+content);
			if(content!=null){
				returnMap=(Map<String,String>)JSON.parse(content.trim());
				//System.out.println("conentMap-->"+returnMap);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return returnMap;
	}
	
	public static void main(String[] args) {
		try {
			//准备一些前提数据
			String host="http://localhost:8080/";
			String basePath="tes_server/";
			TestUploadController emulator=new TestUploadController();
			String fileName="c:/ks.mp4";
			File realFile=new File(fileName);
			long fileSize=0L;
			if(realFile.exists()){
				fileSize=realFile.length();
				//创建视频
				Map<String,String> createMap=new HashMap<String,String>();
				createMap.put("userId","7008ffa6-e01d-48ed-a460-dbf2a4908bfa");
				createMap.put("format", "json");
				String serverUrlCreate=host+basePath+"video/create";
				Map<String,String> returnCreateMap=emulator.videoCreate(createMap, serverUrlCreate);
				System.out.println("returnCreateMap-->"+returnCreateMap);
				String msgerror=returnCreateMap.get("msgerror");
				if(msgerror==null){
					//创建视频成功
					//可以上传视频的元数据
					Map<String,String> metadataMap=new HashMap<String,String>();
					metadataMap.put("videoId",returnCreateMap.get("videoId"));
					metadataMap.put("userId",returnCreateMap.get("userId"));
					metadataMap.put("videoTitle", "快闪视频wangtao");
					metadataMap.put("videoTag", "java娱乐");
					metadataMap.put("videoDescription", "很好玩的视频");
					metadataMap.put("videoFileName", fileName);
					metadataMap.put("videoFileSize", fileSize+"");
					metadataMap.put("courseId", "8c2ded0e-0455-4631-a3c4-b3c50aeda12f");
					metadataMap.put("md5",Md5Util.getMd5(realFile));
					metadataMap.put("format","json");
					metadataMap.put("first","1");
					File videoPicture=new File("c:/video.png");
					String serverUrl=returnCreateMap.get("metaurl");
					Map<String,Object> returnMetadataMap=emulator.uploadMetaData(metadataMap, videoPicture, serverUrl);
					System.out.println("returnMetadataMap-->"+returnMetadataMap);
					String msg=returnMetadataMap.get("msg").toString();
					if("success".equals(msg)){
						//说明元数据上传成功,且队列中已经有视频文件的head(Video对象)
						//准备数据
						//blockIndex代表的最后一个正确上传的文件块的索引
				    	//blockNumber代表文件分块的总块数
						//此值是从上传元数据的返回结果中得到的
				    	int blockIndex=(Integer)returnMetadataMap.get("blockIndex")+1;
				    	int blockNumber=(Integer)(returnMetadataMap.get("blockNumber"));
				    	if(blockNumber==0){
				    		blockNumber=UploadFileUtil.findBlockNumber(fileSize);
				    	}	
										
						Map<String,String> chunkMap=new HashMap<String,String>();
						chunkMap.put("userId", returnCreateMap.get("userId"));
						chunkMap.put("videoId", returnCreateMap.get("videoId"));
						//chunkMap.put("blockIndex", blockIndex+"");//?
						chunkMap.put("blockNumber", blockNumber+"");//?
						chunkMap.put("format", "json");
						//chunkMap.put("file", );//?在httpClient httpPOST对象中放上传的问价你块的信息
						serverUrl=returnCreateMap.get("chunkurl");
						
						for(int i=blockIndex;i<=blockNumber;i++){
							chunkMap.put("blockIndex", i+"");
				    		Map<String,String> returnChunkMap=emulator.uploadToServer(chunkMap, fileName, serverUrl);
				    		System.out.println("returnChunkMap-->"+returnChunkMap);
				    		if("error".equals(returnChunkMap.get("msg"))){
				    			//上传文件块的任意一块上传失败就终止循环
				    			break;
				    		}
				    	}
					}
				}
				
				
				
			}
			
			
			
			//循环上传文件块
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

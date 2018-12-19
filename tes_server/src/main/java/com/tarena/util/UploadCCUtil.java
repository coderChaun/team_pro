package com.tarena.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.tarena.bokecc.util.QueryStringUtil;

@Component("uploadCCUtil")
public class UploadCCUtil {
	@Resource(name="ccKeyUtil")
	private CCKeyUtil ccKeyUtil;
	
	//在cc视频平台创建视频
	public Map<String,Map<String,String>> ccVideoCreate(Map<String,String> paramsMap){
		Map<String,Map<String,String>> contentMap=null;
		String content="";//服务器返回的数据
		String createHost="http://spark.bokecc.com/api/video/create/v2";
		//cc的thqs加密,此thqs加密算法是由cc提供
		long time=System.currentTimeMillis();
		String requestURL=QueryStringUtil.createHashedQueryString(paramsMap,time,ccKeyUtil.getAppKey());
		String createurl=createHost+"?"+requestURL;
		System.out.println("createurl-->"+createurl);
		
		try{
			//发送创建视频的请求
			HttpClient httpClient=HttpClients.createDefault();
			HttpGet httpGet=new HttpGet(createurl);
			
			HttpResponse response=httpClient.execute(httpGet);
			
			int statusCode=response.getStatusLine().getStatusCode();
			if(statusCode==HttpStatus.SC_OK){
				HttpEntity entity=response.getEntity();
				content=EntityUtils.toString(entity);
				httpClient.getConnectionManager().shutdown();
			}
			//System.out.println("content-->"+content);
			if(content!=null){
				contentMap=(Map<String,Map<String,String>>)JSON.parse(content.trim());
			}
			//System.out.println("create contentMap-->"+contentMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return contentMap;
	}
	//cc视频平台视频元数据上传
	public Map<String,Object> ccUploadMetaData(Map<String,String> paramsMap){
		Map<String,Object> contentMap=null;
		String content="";
		
		String metaHost=paramsMap.get("metaurl");
		paramsMap.remove("metaurl");
		long time=System.currentTimeMillis();
		String requestUrl=QueryStringUtil.createQueryString(paramsMap);
		String metaurl=metaHost+"?"+requestUrl;
		try{
			HttpClient httpClient=HttpClients.createDefault();
			HttpGet httpGet=new HttpGet(metaurl);
			HttpResponse response=httpClient.execute(httpGet);
			int statusCode=response.getStatusLine().getStatusCode();
			if(statusCode==HttpStatus.SC_OK){
				HttpEntity entity=response.getEntity();
				content=EntityUtils.toString(entity);
				httpClient.getConnectionManager().shutdown();
			}
			if(content!=null){
				contentMap=(Map<String,Object>)JSON.parse(content.trim());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return contentMap;
	}
	//cc视频平台文件块上传
	public String ccVideoChunk(String chunkurl, 
			                   String ccvid, 
			                   int blockIndex, 
			                   int blockNumber, 
			                   byte[] data,
			                   long fileSize,
			                   String fileName) {
		String result=null;
		int chunkEnd=0;
		int chunkStart=1024*1024*ccKeyUtil.getBlockSize()*(blockIndex-1);
		if(blockIndex<blockNumber){
			chunkEnd=1024*1024*ccKeyUtil.getBlockSize()*blockIndex-1;
		}else if(blockIndex==blockNumber){
			chunkEnd=chunkStart+data.length-1;
		}
		result=uploachunk(chunkurl+"?ccvid="+ccvid+"&format=json",
				          chunkStart,chunkEnd,data,fileSize,fileName);
		
		return result;
	}
	/**
	 * 
	 * @param url  /servlet/uploadChunk?ccvid= &format=
	 * @param chunkStart chunk的起始位置
	 * @param chunkEnd   chunk的结束位置
	 * @param data    文件块的字节数组
	 * @param fileSize  文件的大小
	 * @param fileName  文件的名称
	 * @return  
	 */
	private String uploachunk(String url, 
			                  int chunkStart, 
			                  int chunkEnd, 
			                  byte[] bufferOut, 
			                  long fileSize,
			                  String fileName) {
		String message=null;
		if(bufferOut==null){
			message="no data";
			return message;
		}
		HttpURLConnection conn=null;
		try{
			String BOUNDARY = "---------CCHTTPAPIFormBoundaryEEXX" + new Random().nextInt(65536); // 定义数据分隔线
            URL openUrl = new URL(url);
            conn = (HttpURLConnection)openUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_4)");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            // content-range
            conn.setRequestProperty("Content-Range", "bytes " + chunkStart + "-" + chunkEnd + "/" + fileSize);
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            StringBuilder sb = new StringBuilder();
            sb.append("--").append(BOUNDARY).append("\r\n");
            sb.append("Content-Disposition: form-data;name=\"file" + fileName + "\";filename=\"" + fileName+ "\"\r\n");
            sb.append("Content-Type: application/octet-stream\r\n");
            sb.append("\r\n");
            byte[] data = sb.toString().getBytes();
            out.write(data);
            out.write(bufferOut);
            out.write("\r\n".getBytes());
            // 定义最后数据分隔线
            byte[] end_data = ("--" + BOUNDARY + "--\r\n").getBytes();
            out.write(end_data);
            out.flush();
            out.close();
            
            // 定义BufferedReader输入流来读取URL的响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer resultBuf = new StringBuffer("");
            String line = null;
            while ((line = reader.readLine()) != null)  {
            	resultBuf.append(line);
            }
            reader.close();
            conn.disconnect();
            return resultBuf.toString();

		}catch(Exception e){
			System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace(); 
        }finally {
            if (conn != null)
                    conn.disconnect();
        }
        return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}

package com.tarena.mq.listen;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSON;
import com.tarena.entity.Video;
import com.tarena.service.VideoService;
import com.tarena.util.CCKeyUtil;
import com.tarena.util.GlobalConstant;
import com.tarena.util.UploadCCUtil;
import com.tarena.vo.VideoBlockMessage;

/**
 * 消费videoSendCCQueue队列的消息
 * @author Administrator
 *
 */
public class VideoSendCC {
	@Resource(name="ccKeyUtil")
	private CCKeyUtil ccKeyUtil;
	@Resource(name="uploadCCUtil")
	private UploadCCUtil uploadCCUtil;
	@Resource(name="redisTemplage")
	private RedisTemplate<Serializable,Serializable> redisTemplate;
	@Resource(name="videoService")
	private VideoService videoService;
	/**
	 * 每调用一次方法,只消费一个消息
	 * @param obj
	 */
	public void videoSendCCListen(Object obj){
		System.out.println("VideoSendCC.videoSendCCListen()-->"+obj.getClass());
		//声明三个局部变量
		Map<String,Map<String,String>> createMap;
		Map<String,Object> metaMap;
		Map<String,Object> chunkMap;
		if(obj.getClass()==Video.class){
			System.out.println("VideoSendCC.videoSendCCListen()-->Video.class");
			Video video=(Video)obj;
			//构建创建cc视频的数据
			Map<String,String> paramsMap=new HashMap<String,String>();
			paramsMap.put("userid", ccKeyUtil.getUserId());
			paramsMap.put("categoryid", ccKeyUtil.getCategoryId());
			paramsMap.put("title", video.getTitle());
			paramsMap.put("tag", video.getTag());
			paramsMap.put("description", video.getIntroduction());
			paramsMap.put("filename", video.getFileName());
			paramsMap.put("filesize", video.getFileSize()+"");
			paramsMap.put("notify_url", ccKeyUtil.getNotifyUrl());
			paramsMap.put("format", "json");
			//在cc上创建视频
			createMap=uploadCCUtil.ccVideoCreate(paramsMap);
			System.out.println("bokecc createMap-->"+createMap);
			//在cc平台上上传元数据,前提是在cc平台上视频要创建成功
			Object errorMessage=createMap.get("error");
			if(errorMessage==null){
				//说明在创建cc视频是成功,是没有error信息的
				//获取创建视频成功的返回值
				String metaurl=createMap.get("uploadinfo").get("metaurl");
				String chunkurl=createMap.get("uploadinfo").get("chunkurl");
				String ccvid=createMap.get("uploadinfo").get("videoid");
				String servicetype=createMap.get("uploadinfo").get("servicetype");
				String userid=createMap.get("uploadinfo").get("userid");
				
				video.setMetaurl(metaurl);
				video.setChunkurl(chunkurl);
				video.setCcvid(ccvid);
				video.setServicetype(servicetype);
				//把创建视频的成功后的信息保存到数据库
				this.videoService.updateCCVideo(video);
				
				//开始构建视频的元数据,准备上传元数据
				paramsMap.clear();
				paramsMap.put("uid", ccKeyUtil.getUserId());
				paramsMap.put("ccvid", ccvid);
				paramsMap.put("filename", video.getFileName());
				paramsMap.put("md5", video.getMd5());
				paramsMap.put("filesize", video.getFileSize()+"");
				paramsMap.put("servicetype", servicetype);
				paramsMap.put("first", "1");
				paramsMap.put("format", "json");
				paramsMap.put("metaurl", metaurl);
				//数据准备完毕后,调用上传元数据的方法
				metaMap=uploadCCUtil.ccUploadMetaData(paramsMap);
				System.out.println("bokecc metaMap==>"+metaMap);
				int result=(Integer)metaMap.get("result");
				/*if(result==-1){
					// 上传失败，可以放弃“本次”上传，不要重试了；
					this.redisTemplate.boundHashOps("videoState").put(video.getId(), GlobalConstant.UPLOAD_FAILURE);
					video.setState(Integer.parseInt(GlobalConstant.UPLOAD_FAILURE));
					this.videoService.updateVideoState(video);
					return;
				}else if(result==-2){
					//-2  服务器内部错误，详见msg信息，可以续传重试；
					paramsMap.put("first", "2");
					metaMap=uploadCCUtil.ccUploadMetaData(paramsMap);
				}else if(result==-3){
					this.redisTemplate.boundHashOps("videoState").put(video.getId(), GlobalConstant.UPLOAD_FAILURE);
					video.setState(Integer.parseInt(GlobalConstant.UPLOAD_FAILURE));
					this.videoService.updateVideoState(video);
					return;
				}*/
			}else{
				/*//在cc平台上创建视频失败
				this.redisTemplate.boundHashOps("videoState").put(video.getId(), GlobalConstant.UPLOAD_FAILURE);
				video.setState(Integer.parseInt(GlobalConstant.UPLOAD_FAILURE));
				this.videoService.updateVideoState(video);
				return;*/
			}
			
		}else if(obj.getClass()==VideoBlockMessage.class){
			//处理文件块的部分
			System.out.println("VideoSendCC.videoSendCCListen()-->VideoBlockMessage.class");
			VideoBlockMessage videoBlockMessage=(VideoBlockMessage)obj;
			String state=(String)this.redisTemplate.boundHashOps("videoState").get(videoBlockMessage.getVideoId());
			if(GlobalConstant.UPLOAD_FAILURE.equals(state)){
				//在处理文件块之前,首先检查redis中的视频状态是否是是失败
				//如果失败就不cc队列中的所有的数据,逐个消费掉,不做任何处理
				return;
			}
			//能到此处说明可以处理文件块了
			//从数据库中取出chunkurl,ccvid,fileSize,fileName
			Video partcc=this.videoService.findPartCCInfoByVideoId(videoBlockMessage.getVideoId());
			String chunkurl=partcc.getChunkurl();
			String ccvid=partcc.getCcvid();
			long fileSize=partcc.getFileSize();
			String fileName=partcc.getFileName();
			//根据取出的数据,上传所有的文件块
		    String resultJson=uploadCCUtil.ccVideoChunk(chunkurl, ccvid, videoBlockMessage.getBlockIndex(),videoBlockMessage.getBlockNumber(), videoBlockMessage.getData(), fileSize, fileName);
			chunkMap=(Map<String,Object>)JSON.parse(resultJson);
			System.out.println("bokecc chunkMap-->"+chunkMap);
		    int result=(Integer)chunkMap.get("result");
			/*if(result==-1){
				//其中的某一个块上传失败
				boolean flag=false;
				for(int i=0;i<3;i++){
					resultJson=uploadCCUtil.ccVideoChunk(chunkurl, ccvid, videoBlockMessage.getBlockIndex(),videoBlockMessage.getBlockNumber(), videoBlockMessage.getData(), fileSize, fileName);
					chunkMap=(Map<String,Object>)JSON.parse(resultJson);
				    result=(Integer)chunkMap.get("result");
				    if(result==0){
				    	flag=true;
				    	break;
				    }
				}
				if(flag==false){
					//说明尝试三次都没有上传成功,我就认定,上传失败
					this.redisTemplate.boundHashOps("videoState").put(videoBlockMessage.getVideoId(), GlobalConstant.UPLOAD_FAILURE);
					Video v=new Video();
					v.setState(Integer.parseInt(GlobalConstant.UPLOAD_FAILURE));
					v.setId(videoBlockMessage.getVideoId());
					this.videoService.updateVideoState(v);
				}
			}*/
			//上传成功后,等待cc服务回调notify,在notify中更新数据库为视频上传完毕,
			//并可以播放
		}
	}
}

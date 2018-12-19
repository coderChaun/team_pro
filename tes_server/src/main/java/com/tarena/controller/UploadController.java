package com.tarena.controller;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.tarena.entity.Course;
import com.tarena.entity.User;
import com.tarena.entity.Video;
import com.tarena.util.UUIDUtil;
import com.tarena.util.UploadFileUtil;
import com.tarena.util.UrlUtil;
import com.tarena.vo.ReceiveInfo;
import com.tarena.vo.UploadInfo;
import com.tarena.vo.VideoBlockMessage;

@Controller
public class UploadController {
	@Resource(name="urlUtil")
	private UrlUtil urlUtil; 
	@Resource(name="amqpTemplate")
	private AmqpTemplate amqpTemplate;
	@Resource(name="redisTemplage")
	private RedisTemplate redisTemplate;
	/**
	 * 创建视频服务端
	 * @param userId
	 * @param format
	 * @return
	 */
    @RequestMapping(value="video/create",method=RequestMethod.POST)
    @ResponseBody
	public UploadInfo videoCreate(String userId,String format){
		UploadInfo uploadInfo=new UploadInfo();
		if(userId==null || "".equals(userId) || userId.length()!=36){
			uploadInfo.setMsgerror("INVALID_REQUEST");
			return uploadInfo;
		}
		uploadInfo.setVideoId(UUIDUtil.getUUID());
		uploadInfo.setUserId(userId);
		uploadInfo.setMetaurl(urlUtil.getMetaurl());
		uploadInfo.setChunkurl(urlUtil.getChunkurl());
		uploadInfo.setMsgerror(null);
		return uploadInfo;
	}
	@RequestMapping(value="video/uploadmeta")
	@ResponseBody
	public ReceiveInfo uploadMetadata(
			HttpServletRequest request,
            HttpServletResponse response,
            String userId,
            String videoId,
            String videoTitle,
            String videoTag,
            String videoDescription,
            String videoFileName,
            String videoFileSize,
            String courseId,
            String md5,
            String format,
            String first,
            MultipartFile addPicture){
		ReceiveInfo receiveInfo=new ReceiveInfo();
		try {
			//处理上传的视频截图文件,并做缩放和加水印
			String realPath=request.getServletContext().getRealPath("/videoimage");
			File file=new File(realPath);
			if(!file.exists())  file.mkdir();
			if(addPicture==null || addPicture.isEmpty()){
				//没有接收到客户端的上传视频截图图片
				receiveInfo.setResult(-1);
				receiveInfo.setMsg("error");
				return receiveInfo;
			}else{
				//接收到客户端上传的视频截图图片,缩放+水印
				boolean flag=UploadFileUtil.uploadImage(addPicture, videoId, true, 128, realPath);
				if(!flag){
					//图片没有上传成功
					receiveInfo.setResult(-1);
					receiveInfo.setMsg("error");
					return receiveInfo;			
				}			
			}
			
			Video video=new Video();
			video.setId(videoId);
			video.setTitle(videoTitle);
			video.setFileName(videoFileName);
			video.setFileSize(Long.parseLong(videoFileSize));
			video.setTag(videoTag);
			video.setIntroduction(videoDescription);
			video.setMd5(md5);
			video.setFirst(first);
			User user=new User();
			user.setId(userId);
			video.setUser(user);
			Course course=new Course();
			course.setId(courseId);
			video.setCourse(course);
			String oriFileName=addPicture.getOriginalFilename();
			String extName=oriFileName.substring(oriFileName.lastIndexOf(".")+1);
			video.setPicture(videoId+"."+extName);
			if("1".equals(first)){
				System.out.println("first-->"+first);
				//把视频的元数据封装后,转存给mq消息队列
				this.amqpTemplate.convertAndSend("videoCacheQueue",video);
				
			}else if("2".equals(first)){
			   	//非第一次,需要断点续传
				//从redis中获取最后上传完的那个快的信息
				//这个块可能是head(Video),也可能是VideoBlockMessage
				List<Serializable> currentVideoAllBlock=this.redisTemplate.boundListOps(videoId).range(0, -1);
				if(currentVideoAllBlock.size()==1){
					//只有head(Video)信息
					receiveInfo.setBlockIndex(0);
					receiveInfo.setBlockNumber(UploadFileUtil.findBlockNumber(Long.parseLong(videoFileSize)));
				}else if(currentVideoAllBlock.size()>1){
					//至少有一个文件块需要处理                                                                                        0(video) 1vbm   2vbm   3vbm 4vbm   5vbm
					VideoBlockMessage vbm=(VideoBlockMessage)currentVideoAllBlock.get(currentVideoAllBlock.size()-1);
					receiveInfo.setBlockIndex(vbm.getBlockIndex());
					receiveInfo.setBlockNumber(UploadFileUtil.findBlockNumber(Long.parseLong(videoFileSize)));
				}
			}
			receiveInfo.setResult(0);
			receiveInfo.setMsg("success");
		} catch (Exception e) {
			receiveInfo.setResult(-1);
			receiveInfo.setMsg("error");
			e.printStackTrace();
		}
		return receiveInfo;
		
	}
	//3.接收httpClient传送的文件块
	@RequestMapping(value="video/chunk",method=RequestMethod.POST)
	@ResponseBody
	public ReceiveInfo videoChunk(String userId,
			String videoId,
			String blockIndex,
			String blockNumber,
			String format,
			MultipartFile file){
		ReceiveInfo receiveInfo=new ReceiveInfo();
		
		try {
			//构建一个amq实体,并把amq实体转存到消息队列
			VideoBlockMessage videoBlockMessage=new VideoBlockMessage();
			videoBlockMessage.setUserId(userId);
			videoBlockMessage.setVideoId(videoId);
			videoBlockMessage.setLength((int)file.getSize());
			videoBlockMessage.setBlockIndex(Integer.parseInt(blockIndex));
			videoBlockMessage.setBlockNumber(Integer.parseInt(blockNumber));
			videoBlockMessage.setData(file.getBytes());
			//把文件块存储到消息队列
			this.amqpTemplate.convertAndSend("videoCacheQueue",videoBlockMessage);
					
			receiveInfo.setBlockIndex(Integer.parseInt(blockIndex));
			receiveInfo.setBlockNumber(Integer.parseInt(blockNumber));
			receiveInfo.setResult(0);
			receiveInfo.setMsg("success");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			receiveInfo.setResult(-1);
			receiveInfo.setMsg("error");
			receiveInfo.setBlockIndex(Integer.parseInt(blockIndex));
			receiveInfo.setBlockNumber(Integer.parseInt(blockNumber));
			e.printStackTrace();
		}
		return receiveInfo;
	}
}

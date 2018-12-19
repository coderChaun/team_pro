package com.tarena.mq.listen;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Hex;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.redis.core.RedisTemplate;

import com.tarena.entity.Video;
import com.tarena.service.VideoService;
import com.tarena.util.AssembledFileUtil;
import com.tarena.util.GlobalConstant;
import com.tarena.util.LuceneUtil;
import com.tarena.vo.VideoBlockMessage;

/**
 * 是rabbitmq的消费类,
 * 消费原则在videoCacheListen体现
 * 此监听只消费videoCacheQueue队列中的amq实体
 * @author Administrator
 *
 */
public class VideoCache {
	@Resource(name="amqpTemplate")
	private AmqpTemplate amqpTemplate;
	@Resource(name="videoService")
	private VideoService videoService;
	@Resource(name="redisTemplage")
	private RedisTemplate redisTemplate;
	@Resource(name="luceneUtil")
	private LuceneUtil luceneUtil;
	public void videoCacheListen(Object obj){
		System.out.print("进入消费方法,消费的是videoCacheQueue队列-->");
		System.out.println(obj.getClass().getName());
		try {
			if(obj.getClass()==Video.class){
				//消费视频的消息头(元数据)
				Video video=(Video)obj;
				//把实体数据存储到redis中,以videoId为key,以所有Vidoe和VideoBlockMessage为值
				this.redisTemplate.boundListOps(video.getId()).rightPush(video);
				//在redis服务器中对videoState状态设置为正在上传
				this.redisTemplate.boundHashOps("videoState").put(video.getId(), GlobalConstant.UPLOADING);
				//定时清除redis中指定的数据
				this.redisTemplate.expire(video.getId(), 1, TimeUnit.DAYS);
				this.redisTemplate.boundHashOps("videoState").expire(7, TimeUnit.DAYS);
				
			}else if(obj.getClass()==VideoBlockMessage.class){
				//消费具体文件的文件块
				VideoBlockMessage videoBlockMessage=(VideoBlockMessage)obj;
				
				//可以开始处理视频文件的相关消息
				//只要任何一个环节上传失败,则忽略队列中后面所有的数据块
				//此处可能出现的问题,就是数据库存储失败和md5不相等
				String state=(String)this.redisTemplate.boundHashOps("videoState").get(videoBlockMessage.getVideoId());
				//从redis的videostate中获取视频的状态是failure,则终止return此方法
				if(state.equals(GlobalConstant.UPLOAD_FAILURE)){
					return;
				}
				//不是failure,就把文件块的信息临时存储到redis服务器
				//把实体数据存储到redis中,以videoId为key,以所有Vidoe和VideoBlockMessage为值
				this.redisTemplate.boundListOps(videoBlockMessage.getVideoId()).rightPush(videoBlockMessage);
				//从redis中取出数据,计算md5,匹配md5,匹配成功就合并
				//并置视频状态为uploading
				this.redisTemplate.boundHashOps("videoState").put(videoBlockMessage.getVideoId(), GlobalConstant.UPLOADING);
				//判断blockIndex是否等于blockNumber
				if(videoBlockMessage.getBlockIndex()==videoBlockMessage.getBlockNumber()){
					//就到最后一块了,说明视频文件数据块已经全部存储到redis中
					//从redis中取出视频文件块的信息
					List<Serializable> currentVideoAllBlock=this.redisTemplate.boundListOps(videoBlockMessage.getVideoId()).range(0, -1);
					Video metaData=(Video)currentVideoAllBlock.get(0);
					//判断取出视频块的md5跟原始的md5是否相等
					MessageDigest md5=MessageDigest.getInstance("MD5");
					for(int i=1;i<currentVideoAllBlock.size();i++){
						VideoBlockMessage vbm=(VideoBlockMessage)currentVideoAllBlock.get(i);
						md5.update(vbm.getData());
					}
					byte[] digest=md5.digest();
					String newMd5=Hex.encodeHexString(digest);
					if(metaData.getMd5().equals(newMd5)){
						System.out.println("文件是完整的");
												
						//如果文件时完整的,要更新数据库把视频信息插入到数据库
						boolean flag=this.videoService.save(metaData);
						if(flag){
							System.out.println("视频信息数据库存储成功!");
							metaData.setState(Integer.parseInt(GlobalConstant.UPLOAD_OVER));
							//给redis的vidoeState置uploadOver
							this.redisTemplate.boundHashOps("videoState").put(videoBlockMessage.getVideoId(), GlobalConstant.UPLOAD_OVER);
							//可以合并所有的文件块(内网使用)
							AssembledFileUtil.assembledFile(currentVideoAllBlock);
							
							//查询数据库构建lucene的索引(注意每添加一个视频,都要重新建立索引)
							this.luceneUtil.createIndex(this.videoService.findAllVideosByLucene());
							
							//把文件块的数据循环送到videoSendCCQueue队列中,由videoSendCCQueue队列控制往cc服务器上上传
							for(Serializable serObject : currentVideoAllBlock){
								this.amqpTemplate.convertAndSend("videoSendCCQueue",serObject);
							}
							//清空redis,对应视频信息
							for(Serializable serObject : currentVideoAllBlock){
								this.redisTemplate.boundListOps(videoBlockMessage.getVideoId()).leftPop();
							}
						}else{
							System.out.println("视频信息数据库存储失败!");
							//更新redis状态为失败failure
						    this.redisTemplate.boundHashOps("videoState").put(videoBlockMessage.getVideoId(),GlobalConstant.UPLOAD_FAILURE);
						    return;
						}
						
					}else{
						System.out.println("md5不相等!");
						//更新redis状态为失败failure
					    this.redisTemplate.boundHashOps("videoState").put(videoBlockMessage.getVideoId(),GlobalConstant.UPLOAD_FAILURE);
					    return;
					}
					
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

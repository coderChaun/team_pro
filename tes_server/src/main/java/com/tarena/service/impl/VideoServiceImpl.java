package com.tarena.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tarena.dao.VideoMapper;
import com.tarena.entity.Video;
import com.tarena.service.VideoService;
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
@Service("videoService")
public class VideoServiceImpl implements VideoService {
    @Resource(name="videoMapper")
	private VideoMapper videoMapper;
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    @Override
	public boolean save(Video metaData) {
		boolean flag=false;
		this.videoMapper.save(metaData);
		flag=true;
		return flag;
	}
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	@Override
	public void updateCCVideo(Video video) {
		
		this.videoMapper.updateCCVideo(video);
		
	}
	@Override
	public Video findPartCCInfoByVideoId(String videoId) {
		// TODO Auto-generated method stub
		return this.videoMapper.findPartCCInfoByVideoId(videoId);
	}
	@Override
	public List<Video> findAllVideosByLucene() {
		
		return this.videoMapper.findAllVideosByLucene();
	}
	
}

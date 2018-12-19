package com.tarena.service;

import java.util.List;

import com.tarena.entity.Video;

public interface VideoService {
	//添加一个视频信息
	public boolean save(Video metaData);
    //更新数据库中的由关于cc的相关信息(ccvid,ccmetaurl,ccchunkurl,servicetype)
	public void updateCCVideo(Video video);
	//从数据库中获取必要视频信息
	public Video findPartCCInfoByVideoId(String videoId);
	//查询所有的视频信息,用于构建lucene的索引
	public List<Video> findAllVideosByLucene();

}

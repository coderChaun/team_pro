package com.tarena.dao;

import java.util.List;

import com.tarena.entity.Video;

public interface VideoMapper {
    //添加一个视频信息进入数据表
	public void save(Video metaData);
    //给数据库中的视频赋值cc相关信息
	public void updateCCVideo(Video video);
	public Video findPartCCInfoByVideoId(String videoId);
	public List<Video> findAllVideosByLucene();

}

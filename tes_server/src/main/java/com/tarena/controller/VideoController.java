package com.tarena.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tarena.entity.Video;
import com.tarena.util.LuceneUtil;
import com.tarena.vo.Result;

@Controller
@RequestMapping("video/")
public class VideoController {
	@Resource(name="luceneUtil")
	private LuceneUtil luceneUtil;
	@RequestMapping(value="{queryString}",method=RequestMethod.GET)
	@ResponseBody
	public Result findVideoByLucene(@PathVariable("queryString") String queryString){
		Result result=new Result();
		List<Video> videos=this.luceneUtil.search(queryString);
		result.setStatus(1);
		result.setData(videos);
		return result;
	}
}

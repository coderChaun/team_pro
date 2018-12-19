package com.tarena.controller;

import java.io.File;
import java.io.FileFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tarena.vo.Result;

@Controller
@RequestMapping("carousel/")
public class CarouselController {
	@RequestMapping(value="allImages",method=RequestMethod.GET)
	@ResponseBody
	public Result getCarousel(HttpServletRequest request,
			                  HttpServletResponse response){
		Result result=new Result();
		File[] files=null;
		//获取服务端轮播图片的路径
		String realPath=request.getServletContext().getRealPath("/carousel");
		File realFile=new File(realPath);
		if(realFile.exists()){
			//说明轮播图片的服务器路径是存在的
			files=realFile.listFiles(new FileFilter(){

				@Override
				public boolean accept(File file) {
					if(file.isFile()){
						//是文件为前提
						if(file.getName().endsWith(".png") ||
						   file.getName().endsWith(".jpg") ||
						   file.getName().endsWith(".jpeg") ||
						   file.getName().endsWith(".ico") ||
						   file.getName().endsWith(".bmp") ||
						   file.getName().endsWith(".gif")){
						   //是文件且图片
						   return true;
						}
					}
					return false;
				}				
			});
		}else{
			result.setStatus(0);
			result.setMessage("没有轮播图片");
			return result;
		}
		result.setStatus(1);
		result.setData(files);
		return result;
	}
}

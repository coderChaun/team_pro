package com.oa.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oa.service.OaDictService;
import com.oa.vo.OaDictDTO;
import com.oa.vo.Result;


@Controller
@RequestMapping("oa/user")
public class OaDictController {
	
	@Autowired
	private OaDictService  oaDictService;
	/**
	 * 用户（账号）新增一条记录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.GET)
	@ResponseBody
	public Result getCarousel(HttpServletRequest request,
			                  HttpServletResponse response){
		Result result=new Result();
		OaDictDTO paraDto = new OaDictDTO();
		int insertCount = oaDictService.add(paraDto);
		result.setStatus(1);
		result.setData(insertCount);
		return result;
	}
}

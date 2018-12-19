package com.oa.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oa.service.PositionService;



@Controller
@RequestMapping("positon/")
public class PositonController {
	@Resource(name="positionService")
	private PositionService poservice;
	

	@RequestMapping("add")
	public void addPosition(){
		
	}
	@RequestMapping("querry")
	public void querryPosition(){
		
	}
	@RequestMapping("update")
	public void updatePosition(){
		
	}
	@RequestMapping("delete")
	public void deletePosition(){
		
	}
}

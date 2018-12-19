package com.oa.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oa.service.DepartmentService;
import com.oa.vo.Result;





@Controller
@RequestMapping("department/")
public class DepartController {
	@Resource(name="departmentService")
	private DepartmentService depservice;
	
	@RequestMapping("add")
	public Result addDepart(String str){
		Result re=null;
		re=depservice.addDepart(str);
		return re;
	}
	@RequestMapping("querry")
	public void querryDepart(String str){
		Result re=null;
		re=depservice.querryDepart(str);
	
	}
	@RequestMapping("update")
	public void updateDepart(){
		
	}
	@RequestMapping("delete")
	public void deleteDepart(){
		
	}
}

package com.oa.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oa.service.OaUserService;
import com.oa.vo.Result;

/**
 * @author fuchun
 *
 */
/*@Controller
@RequestMapping("oa/user/")
public class OaUserController {

	@Resource(name=" oaUsertService")
	private OaUserService  oaUserService;
	@RequestMapping(value="login/name/{lName}/password/{pwd}",method=RequestMethod.GET)
	@ResponseBody
	public Result login(@PathVariable(value="1Name") String loginName,
			@PathVariable(value="pwd") String password){
		Result result =new Result();
		    result=this.oaUserService.login(loginName,password);
		return result;
	}
	
	public Result logout(){
		Result result =new Result();
		
		return result;
			
	}
	
}*/

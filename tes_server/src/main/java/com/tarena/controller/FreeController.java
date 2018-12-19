package com.tarena.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tarena.entity.User;
import com.tarena.service.UserService;
import com.tarena.vo.Result;

/**
 * 首页部分,不需要登录,属于免费
 * @author Administrator
 *
 */
@Controller
@RequestMapping("free/")
public class FreeController {
	@Resource(name="userService")
	private UserService userService;
	
	
	@RequestMapping(value="loginName/{loginName}/loginPassword/{password}",method=RequestMethod.GET)
	@ResponseBody
	public Result login(@PathVariable(value="loginName") String loginName,
		                @PathVariable(value="password") String password){
		Result result=null;
		result=this.userService.login(loginName,password);
		return result;
	}
	@RequestMapping(value="registerUser",method=RequestMethod.POST)
	@ResponseBody
	public Result addUser(User user){
		Result result=null;
		result=this.userService.addUser(user);
		return result;
	}
}

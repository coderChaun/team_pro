package com.tarena.service;

import com.tarena.entity.User;
import com.tarena.vo.Result;

public interface UserService {
    //登录操作
	public Result login(String loginName, String password);
	//添加用户
	public Result addUser(User user);

}

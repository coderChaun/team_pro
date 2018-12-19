package com.tarena.dao;

import com.tarena.entity.User;

public interface UserMapper {

	//登录操作
	public User login(User u);
	//根据登录名查询用户id  注册用
	public String findUserIdByLoginName(String loginName);
    //添加用户
	public void addUser(User user);
	
}

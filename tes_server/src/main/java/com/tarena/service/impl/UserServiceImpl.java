package com.tarena.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tarena.dao.UserMapper;
import com.tarena.entity.User;
import com.tarena.service.UserService;
import com.tarena.util.UUIDUtil;
import com.tarena.vo.Result;
@Service("userService")
public class UserServiceImpl implements UserService {
	@Resource(name="userMapper")
	private UserMapper userMapper;
	
	@Override
	public Result login(String loginName, String password) {
		Result result=new Result();
		User u=new User();
		u.setLoginName(loginName);
		u.setPassword(password);
		System.out.println(userMapper);
		User user=this.userMapper.login(u);
		if(user!=null){
			if("否".equals(user.getIsLock())){
				//能查询出结果,同时没有被锁
				result.setStatus(1);
				result.setData(user);
				return result;
			}else if("是".equals(user.getIsLock())){
				//有账号信息,但是被锁定
				result.setStatus(0);
				result.setMessage("您的账号被锁");
				return result;
			}
		}else{
			//没有账号信息
			result.setStatus(0);
			result.setMessage("没有账号和密码");
			return result;
		}
		
		return null;
	}
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	@Override
	public Result addUser(User user) {
		Result result=new Result();		
		try {
			//根据登录的名字查询是否有此登录名存在
			String userId=this.userMapper.findUserIdByLoginName(user.getLoginName());
			if(userId==null){
				//说明数据库中没有重名的
				user.setId(UUIDUtil.getUUID());
				this.userMapper.addUser(user);
				result.setStatus(1);
				result.setMessage("注册成功");
				return result;
			}else{
				//数据库中有同名的登录名
				result.setStatus(0);
				result.setMessage("用户名已经存在,请换其他的登录名");
				return result;
			}
		} catch (Exception e) {
			result.setStatus(0);
			result.setMessage("注册失败!");
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally{
			return result;
		}
		
	}

}

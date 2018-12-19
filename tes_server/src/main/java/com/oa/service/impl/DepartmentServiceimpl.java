package com.oa.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.oa.dao.DepartmentMapper;
import com.oa.service.DepartmentService;
import com.oa.vo.Result;



public class DepartmentServiceimpl implements DepartmentService {
	@Autowired
	private DepartmentMapper dmapper;
	
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public Result addDepart(String str) {
		Result re=new Result();
		
		return re;
	}

	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public Result querryDepart(String str) {
		
		return null;
	}

}

package com.oa.service;

import org.springframework.stereotype.Service;

import com.oa.vo.Result;



@Service("departmentService")
public interface DepartmentService {

	Result addDepart(String str);

	Result querryDepart(String str);

}

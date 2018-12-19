package com.oa.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.oa.dao.OaDictMapper;
import com.oa.service.OaDictService;
import com.oa.vo.OaDictDTO;
@Service("oaDictService")
public class OaDictServiceImpl implements OaDictService{
	
	@Resource(name="oaDictMapper")
	private OaDictMapper oaDictMapper;

	@Override
	public int add(OaDictDTO paraDto) {
		// TODO Auto-generated method stub
		return oaDictMapper.add(paraDto);
	}

}

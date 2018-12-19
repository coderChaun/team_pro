package com.oa.vo;

import java.io.Serializable;

/**
 * 数据字典表对象
 * @author tc
 *
 */
public class OaDictDTO implements Serializable{
	/**
	 * 
	 */
	private Integer id;
	/**
	 * varchar	 	字段名，比如性别字段，这里就存‘sex’
	 */
	private String field_name;	
	/**
	 * varchar	255	True	字段值，基于字段名的值
	 */
	private String val;		
	/**
	 * varchar	255	True	具体的业务数据
	 */
	private String msg;		
	/**
	 * varchar	30	False	标签
	 */
	private String tag;		
	/**
	 * int	3	True	排序值
	 */
	private Integer sort;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getField_name() {
		return field_name;
	}
	public void setField_name(String field_name) {
		this.field_name = field_name;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}		
	
	
	


}

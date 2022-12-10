package com.lp.server.shop.magento2;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MagTaxClass {
	private Integer classId;
	private String className;
	private String classType;
	
	public MagTaxClass() {
	}
	
	public MagTaxClass(String classId) {
		this.classId = Integer.valueOf(classId);
	}
	
	public Integer getClassId() {
		return classId;
	}
	public void setClassId(Integer classId) {
		this.classId = classId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getClassType() {
		return classType;
	}
	public void setClassType(String classType) {
		this.classType = classType;
	}
}

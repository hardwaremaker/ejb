package com.lp.server.system.service;

import java.io.Serializable;

public class VersandwegDto implements Serializable, IVersandwegDto {
	private static final long serialVersionUID = 102139474864967046L;
	
	private Integer iId ;
	private String cnr ;
	private String name ;
	
	public Integer getIId() {
		return iId;
	}
	public void setIId(Integer iId) {
		this.iId = iId;
	}
	public String getCnr() {
		return cnr;
	}
	public void setCnr(String cnr) {
		this.cnr = cnr;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}

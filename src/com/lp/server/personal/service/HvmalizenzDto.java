package com.lp.server.personal.service;

import java.io.Serializable;

public class HvmalizenzDto implements Serializable{
	private Integer iId;

	private String cNr;

	private Integer iMaxUser;
	
	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCNr() {
		return this.cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public Integer getIMaxUser() {
		return iMaxUser;
	}

	public void setIMaxUser(Integer iMaxUser) {
		this.iMaxUser = iMaxUser;
	}
}

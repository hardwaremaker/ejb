package com.lp.server.personal.service;

import java.io.Serializable;



public class HvmarechtDto implements Serializable{
	private static final long serialVersionUID = 8247748684354664512L;

	private Integer iId;

	private String cNr;

	private Integer hvmalizenzIId;
	private Short bAktiv;
	
	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCNr() {
		return this.cNr;
	}
	
	public HvmaRechtEnum getCnrAsEnum() {
		return HvmaRechtEnum.fromString(getCNr());
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public Integer getHvmalizenzIId() {
		return hvmalizenzIId;
	}

	public void setHvmalizenzIId(Integer hvmalizenzIId) {
		this.hvmalizenzIId = hvmalizenzIId;
	}

	public boolean isAktiv() {
		return this.bAktiv == 1;
	}
	
	public Short getbAktiv() {
		return bAktiv;
	}

	public void setbAktiv(Short bAktiv) {
		this.bAktiv = bAktiv;
	}
}

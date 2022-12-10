package com.lp.server.finanz.service;

import java.io.Serializable;

public class SepaBankTransactionCodeV2 implements Serializable {

	/**
	 * Diese Id darf NIE geaendert werden
	 */
	private static final long serialVersionUID = 2504136769171829870L;

	private String code;
	private String familie;
	private String subFamilie;
	
	public SepaBankTransactionCodeV2() {
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFamilie() {
		return familie;
	}

	public void setFamilie(String familie) {
		this.familie = familie;
	}
	
	public String getSubFamilie() {
		return subFamilie;
	}
	
	public void setSubFamilie(String subFamilie) {
		this.subFamilie = subFamilie;
	}
}

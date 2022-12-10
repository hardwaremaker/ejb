package com.lp.server.finanz.service;

import java.io.Serializable;

public class SepaBankTransactionCodeV3 implements Serializable {

	/**
	 * Diese Id darf NIE geaendert werden
	 */
	private static final long serialVersionUID = 2411461444453166872L;

	private String code;
	private String familie;
	private String subFamilie;
	private String proprietaererCode;

	public SepaBankTransactionCodeV3() {
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
	
	public String getProprietaererCode() {
		return proprietaererCode;
	}
	
	public void setProprietaererCode(String proprietaererCode) {
		this.proprietaererCode = proprietaererCode;
	}
}

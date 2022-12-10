package com.lp.server.personal.service;

import java.io.Serializable;
import java.sql.Timestamp;

public class HvmabenutzerDto implements Serializable {
	private Integer iId;
	private Integer benutzerIId;
	private Integer hvmalizenzIId;
	private String cToken;
	private Timestamp tAnlegen;

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	private static final long serialVersionUID = 1L;

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getBenutzerIId() {
		return benutzerIId;
	}

	public void setBenutzerIId(Integer benutzerIId) {
		this.benutzerIId = benutzerIId;
	}

	public String getCToken() {
		return cToken;
	}

	public void setCToken(String cToken) {
		this.cToken = cToken;
	}

	

	public Integer getHvmalizenzIId() {
		return hvmalizenzIId;
	}

	public void setHvmalizenzIId(Integer hvmalizenzIId) {
		this.hvmalizenzIId = hvmalizenzIId;
	}

}

package com.lp.server.angebotstkl.service;

import java.io.Serializable;

public class EkgruppeDto implements Serializable {
	private Integer iId;

	private String mandantCNr;

	private String cBez;

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}


	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String bez) {
		cBez = bez;
	}
}

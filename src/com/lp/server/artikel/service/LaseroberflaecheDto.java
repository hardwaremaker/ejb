package com.lp.server.artikel.service;

import java.io.Serializable;


public class LaseroberflaecheDto implements Serializable{
	private Integer iId;
	private String mandantCNr;

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	private String cNr;

	private String cBez;


	public String getBezeichnung() {
		String bezeichnung = getCNr();
		if (getCBez() != null) {
			bezeichnung += " " + getCBez();
		}
		return bezeichnung;
	}
	

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}
}

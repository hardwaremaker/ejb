package com.lp.server.artikel.service;

import java.io.Serializable;

public class DateiverweisDto implements Serializable {
	private Integer iId;

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	private String mandantCNr;
	private String cLaufwerk;

	public String getCLaufwerk() {
		return cLaufwerk;
	}

	public void setCLaufwerk(String cLaufwerk) {
		this.cLaufwerk = cLaufwerk;
	}

	public String getCUnc() {
		return cUnc;
	}

	public void setCUnc(String cUnc) {
		this.cUnc = cUnc;
	}

	private String cUnc;
}

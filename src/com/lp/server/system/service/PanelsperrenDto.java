package com.lp.server.system.service;

import java.io.Serializable;


public class PanelsperrenDto implements Serializable {
private Integer iId;
	
	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}
	
	private String belegartCNr;

	public String getBelegartCNr() {
		return belegartCNr;
	}

	public void setBelegartCNr(String belegartCNr) {
		this.belegartCNr = belegartCNr;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getCRessourceUnten() {
		return cRessourceUnten;
	}

	public void setCRessourceUnten(String cRessourceUnten) {
		this.cRessourceUnten = cRessourceUnten;
	}

	public String getCRessourceOben() {
		return cRessourceOben;
	}

	public void setCRessourceOben(String cRessourceOben) {
		this.cRessourceOben = cRessourceOben;
	}

	private String mandantCNr;
	
	private String cRessourceUnten;
	
	private String cRessourceOben;
}

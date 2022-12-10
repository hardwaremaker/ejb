package com.lp.server.forecast.service;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;

public class ForecastauftragDto implements Serializable {
	private Integer iId;

	private String cBemerkung;

	private Integer fclieferadresseIId;

	public Integer getFclieferadresseIId() {
		return fclieferadresseIId;
	}

	public void setFclieferadresseIId(Integer fclieferadresseIId) {
		this.fclieferadresseIId = fclieferadresseIId;
	}

	private java.sql.Timestamp tFreigabe;

	private Integer personalIIdFreigabe;

	public java.sql.Timestamp getTFreigabe() {
		return tFreigabe;
	}

	public void setTFreigabe(java.sql.Timestamp tFreigabe) {
		this.tFreigabe = tFreigabe;
	}

	public Integer getPersonalIIdFreigabe() {
		return personalIIdFreigabe;
	}

	public void setPersonalIIdFreigabe(Integer personalIIdFreigabe) {
		this.personalIIdFreigabe = personalIIdFreigabe;
	}

	private Timestamp tAnlegen;

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public String getCBemerkung() {
		return cBemerkung;
	}

	public void setCBemerkung(String cBemerkung) {
		this.cBemerkung = cBemerkung;
	}

	public String getStatusCNr() {
		return statusCNr;
	}

	public void setStatusCNr(String statusCNr) {
		this.statusCNr = statusCNr;
	}

	private String statusCNr;

	private static final long serialVersionUID = 1L;

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	private String xKommentar;

	public String getXKommentar() {
		return xKommentar;
	}

	public void setXKommentar(String xKommentar) {
		this.xKommentar = xKommentar;
	}
}

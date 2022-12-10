package com.lp.server.personal.service;

import java.io.Serializable;
import java.sql.Timestamp;

public class NachrichtenDto implements Serializable {
	private Integer iId;
	private Timestamp tAnlegen;
	private Timestamp tErledigt;
	private Integer personalIIdAbsender;
	private Integer personalIIdErledigt;
	private Integer nachrichtenartIId;
	private String cBetreff;
	private String xText;
	private String cBelegartnr;
	private Integer belegIId;
	private Integer belegpositionIId;

	private static final long serialVersionUID = 1L;

	public Timestamp gettAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Timestamp getTErledigt() {
		return tErledigt;
	}

	public void setTErledigt(Timestamp tErledigt) {
		this.tErledigt = tErledigt;
	}

	public Integer getPersonalIIdAbsender() {
		return personalIIdAbsender;
	}

	public void setPersonalIIdAbsender(Integer personalIIdAbsender) {
		this.personalIIdAbsender = personalIIdAbsender;
	}

	public Integer getPersonalIIdErledigt() {
		return personalIIdErledigt;
	}

	public void setPersonalIIdErledigt(Integer personalIIdErledigt) {
		this.personalIIdErledigt = personalIIdErledigt;
	}

	public Integer getNachrichtenartIId() {
		return nachrichtenartIId;
	}

	public void setNachrichtenartIId(Integer nachrichtenartIId) {
		this.nachrichtenartIId = nachrichtenartIId;
	}

	public String getCBetreff() {
		return cBetreff;
	}

	public void setCBetreff(String cBetreff) {
		this.cBetreff = cBetreff;
	}

	public String getXText() {
		return xText;
	}

	public void setXText(String xText) {
		this.xText = xText;
	}

	public String getCBelegartnr() {
		return cBelegartnr;
	}

	public void setCBelegartnr(String cBelegartnr) {
		this.cBelegartnr = cBelegartnr;
	}

	public Integer getBelegIId() {
		return belegIId;
	}

	public void setBelegIId(Integer belegIId) {
		this.belegIId = belegIId;
	}

	public Integer getBelegpositionIId() {
		return belegpositionIId;
	}

	public void setBelegpositionIId(Integer belegpositionIId) {
		this.belegpositionIId = belegpositionIId;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}
}

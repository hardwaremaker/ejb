package com.lp.server.personal.service;

import java.io.Serializable;


public class NachrichtengruppeteilnehmerDto implements Serializable{
	private Integer iId;

	private Integer nachrichtengruppeIId;

	private Integer personalIId;
	
	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getNachrichtengruppeIId() {
		return nachrichtengruppeIId;
	}

	public void setNachrichtengruppeIId(Integer nachrichtengruppeIId) {
		this.nachrichtengruppeIId = nachrichtengruppeIId;
	}

	public Integer getPersonalIId() {
		return personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}
}

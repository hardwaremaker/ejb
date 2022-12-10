package com.lp.server.personal.service;

import java.io.Serializable;

import com.lp.server.util.IIId;

public class NachrichtenaboDto  implements Serializable,IIId{

	
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



private Integer iId;

	

	private Integer personalIId;
	private Integer nachrichtenartIId;
	private Integer nachrichtengruppeIId;
	
	public Integer getIId() {
		return iId;
	}


	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getPersonalIId() {
		return personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

	public Integer getNachrichtenartIId() {
		return nachrichtenartIId;
	}

	public void setNachrichtenartIId(Integer nachrichtenartIId) {
		this.nachrichtenartIId = nachrichtenartIId;
	}

	public Integer getNachrichtengruppeIId() {
		return nachrichtengruppeIId;
	}

	public void setNachrichtengruppeIId(Integer nachrichtengruppeIId) {
		this.nachrichtengruppeIId = nachrichtengruppeIId;
	}
}

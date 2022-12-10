package com.lp.server.personal.service;

import java.io.Serializable;

import javax.persistence.Column;

public class ProjektzeitenDto implements Serializable {
private Integer zeitdatenIId;
	
	public Integer getZeitdatenIId() {
		return zeitdatenIId;
	}

	public void setZeitdatenIId(Integer zeitdatenIId) {
		this.zeitdatenIId = zeitdatenIId;
	}

	public Integer getTelefonzeitenIId() {
		return telefonzeitenIId;
	}

	public void setTelefonzeitenIId(Integer telefonzeitenIId) {
		this.telefonzeitenIId = telefonzeitenIId;
	}

	private Integer iId;
	
	public Integer getIId() {
		return iId;
	}

	public void setiId(Integer iId) {
		this.iId = iId;
	}


	private Integer telefonzeitenIId;
}

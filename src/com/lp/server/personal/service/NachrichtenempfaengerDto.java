package com.lp.server.personal.service;

import java.io.Serializable;
import java.sql.Timestamp;

public class NachrichtenempfaengerDto implements Serializable {
	private Integer iId;
	private Timestamp tEmpfangen;
	private Timestamp tGelesen;
	private Integer personalIIdEmpfaenger;
	private Integer nachrichtenIId;
	
	
	

	private static final long serialVersionUID = 1L;




	public Timestamp getTEmpfangen() {
		return tEmpfangen;
	}

	public void setTEmpfangen(Timestamp tEmpfangen) {
		this.tEmpfangen = tEmpfangen;
	}

	public Timestamp getTGelesen() {
		return tGelesen;
	}

	public void setTGelesen(Timestamp tGelesen) {
		this.tGelesen = tGelesen;
	}

	public Integer getPersonalIIdEmpfaenger() {
		return personalIIdEmpfaenger;
	}

	public void setPersonalIIdEmpfaenger(Integer personalIIdEmpfaenger) {
		this.personalIIdEmpfaenger = personalIIdEmpfaenger;
	}

	public Integer getNachrichtenIId() {
		return nachrichtenIId;
	}

	public void setNachrichtenIId(Integer nachrichtenIId) {
		this.nachrichtenIId = nachrichtenIId;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

}

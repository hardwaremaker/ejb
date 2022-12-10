package com.lp.server.personal.service;

import java.io.Serializable;


public class PersonalfahrzeugDto implements Serializable {
	private Integer iId;

	private Integer personalIId;
	private Integer fahrzeugIId;

	public Integer getFahrzeugIId() {
		return fahrzeugIId;
	}

	public void setFahrzeugIId(Integer fahrzeugIId) {
		this.fahrzeugIId = fahrzeugIId;
	}
	
	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}
	

	public Integer getPersonalIId() {
		return this.personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}
	
}

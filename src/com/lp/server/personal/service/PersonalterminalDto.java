package com.lp.server.personal.service;

import java.io.Serializable;

public class PersonalterminalDto implements Serializable {
	private Integer iId;
	private Integer personalIId;
	private Integer arbeitsplatzIId;

	private static final long serialVersionUID = 1L;



	public Integer getArbeitsplatzIId() {
		return arbeitsplatzIId;
	}

	public void setArbeitsplatzIId(Integer arbeitsplatzIId) {
		this.arbeitsplatzIId = arbeitsplatzIId;
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

package com.lp.server.personal.service;

import java.io.Serializable;

public class ReisespesenDto implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	private Integer iId;

	private Integer reiseIId;

	public Integer getReiseIId() {
		return reiseIId;
	}

	public void setReiseIId(Integer reiseIId) {
		this.reiseIId = reiseIId;
	}

	public Integer getEingangsrechnungIId() {
		return eingangsrechnungIId;
	}

	public void setEingangsrechnungIId(Integer eingangsrechnungIId) {
		this.eingangsrechnungIId = eingangsrechnungIId;
	}

	private Integer eingangsrechnungIId;

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}
}

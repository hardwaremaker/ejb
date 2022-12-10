package com.lp.server.angebotstkl.service;

import java.io.Serializable;

public class EkgruppelieferantDto implements Serializable {
	private Integer iId;
	private Integer ekgruppeIId;

	public Integer getEkgruppeIId() {
		return ekgruppeIId;
	}

	public void setEkgruppeIId(Integer ekgruppeIId) {
		this.ekgruppeIId = ekgruppeIId;
	}

	public Integer getLieferantIId() {
		return lieferantIId;
	}

	public void setLieferantIId(Integer lieferantIId) {
		this.lieferantIId = lieferantIId;
	}

	private Integer lieferantIId;

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	private Integer ansprechpartnerIId;

	public Integer getAnsprechpartnerIId() {
		return ansprechpartnerIId;
	}

	public void setAnsprechpartnerIId(Integer ansprechpartnerIId) {
		this.ansprechpartnerIId = ansprechpartnerIId;
	}

	private static final long serialVersionUID = 1L;

}

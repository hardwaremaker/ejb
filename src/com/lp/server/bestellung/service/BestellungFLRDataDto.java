package com.lp.server.bestellung.service;

import java.io.Serializable;

public class BestellungFLRDataDto implements IBestellungFLRData, Serializable {
	private static final long serialVersionUID = -1578322075669104156L;

	private String statusCnr;
	private Integer lieferantId;
	
	@Override
	public void setStatusCnr(String statusCnr) {
		this.statusCnr = statusCnr;
	}

	@Override
	public String getStatusCnr() {
		return statusCnr;
	}

	@Override
	public void setLieferantId(Integer lieferantId) {
		this.lieferantId = lieferantId;
	}

	@Override
	public Integer getLieferantId() {
		return lieferantId;
	}

}

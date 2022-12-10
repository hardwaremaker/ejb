package com.lp.server.bestellung.service;

import java.io.Serializable;

public class BestellvorschlagFLRDataDto implements IBestellvorschlagFLRData, Serializable {
	private static final long serialVersionUID = -5757907897990370985L;

	private Boolean noted;
	
	@Override
	public Boolean isNoted() {
		return noted;
	}

	@Override
	public void setNoted(Boolean noted) {
		this.noted = noted;
	}

}

package com.lp.server.angebotstkl.service;

import java.io.Serializable;

public class EkweblieferantDto extends WeblieferantDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer einkaufsangebotIId;

	public Integer getEinkaufsangebotIId() {
		return this.einkaufsangebotIId;
	}

	public void setEinkaufsangebotIId(Integer einkaufsangebot) {
		this.einkaufsangebotIId = einkaufsangebot;
	}

}

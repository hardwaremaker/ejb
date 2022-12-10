package com.lp.server.personal.service;

import java.io.Serializable;

public class DauerSondertaetigkeitenDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public double dStunden = 0;
	public double dTage = 0;

	public DauerSondertaetigkeitenDto(double dStunden, double dTage) {
		this.dStunden = dStunden;
		this.dTage = dTage;
	}
}

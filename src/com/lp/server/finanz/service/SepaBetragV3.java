package com.lp.server.finanz.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class SepaBetragV3 implements Serializable {

	/**
	 * Diese Id darf NIE geaendert werden
	 */
	private static final long serialVersionUID = -360634056555430280L;

	private BigDecimal wert;
	private Boolean isHaben;

	public SepaBetragV3() {
	}

	public BigDecimal getWert() {
		return wert;
	}
	
	public void setWert(BigDecimal wert) {
		this.wert = wert;
	}
	
	public Boolean getIsHaben() {
		return isHaben;
	}
	
	public void setIsHaben(Boolean isHaben) {
		this.isHaben = isHaben;
	}
}

package com.lp.server.finanz.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class SepaBetragV2 implements Serializable {

	/**
	 * Diese Id darf NIE geaendert werden
	 */
	private static final long serialVersionUID = 2034254964388520415L;

	private BigDecimal wert;
	private Boolean isHaben;
	
	public SepaBetragV2() {
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

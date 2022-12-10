package com.lp.server.forecast.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class LinienabrufArtikelDto implements Serializable {
	private static final long serialVersionUID = -88810132722486019L;

	private Integer artikelIId;
	private BigDecimal menge;
	private BigDecimal offeneMenge;
	private Integer linienabrufIId;
	private BigDecimal lagerstand;

	public LinienabrufArtikelDto() {
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	/**
	 * Sollsatzmenge
	 * 
	 * @return
	 */
	public BigDecimal getMenge() {
		return menge;
	}

	public void setMenge(BigDecimal menge) {
		this.menge = menge;
	}

	public BigDecimal getOffeneMenge() {
		return offeneMenge;
	}

	public void setOffeneMenge(BigDecimal offeneMenge) {
		this.offeneMenge = offeneMenge;
	}

	public Integer getLinienabrufIId() {
		return linienabrufIId;
	}

	public void setLinienabrufIId(Integer linienabrufIId) {
		this.linienabrufIId = linienabrufIId;
	}

	public BigDecimal getLagerstand() {
		return lagerstand;
	}
	
	public void setLagerstand(BigDecimal lagerstand) {
		this.lagerstand = lagerstand;
	}
}

package com.lp.server.rechnung.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class MmzDto implements Serializable {
	private Integer iId;
	private Integer artikelIId;
	private BigDecimal nBisWert;
	private BigDecimal nZuschlag;
	private String mandantCNr;
	
	private Integer landIId;

	public Integer getLandIId() {
		return landIId;
	}

	public void setLandIId(Integer landIId) {
		this.landIId = landIId;
	}
	
	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public BigDecimal getNBisWert() {
		return nBisWert;
	}

	public void setNBisWert(BigDecimal nBisWert) {
		this.nBisWert = nBisWert;
	}

	public BigDecimal getNZuschlag() {
		return nZuschlag;
	}

	public void setNZuschlag(BigDecimal nZuschlag) {
		this.nZuschlag = nZuschlag;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}
}

package com.lp.server.angebotstkl.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;

public class EkaglieferantDto implements Serializable {
	private Integer iId;
	private Integer einkaufsangebotIId;

	public Integer getEinkaufsangebotIId() {
		return einkaufsangebotIId;
	}

	public void setEinkaufsangebotIId(Integer einkaufsangebotIId) {
		this.einkaufsangebotIId = einkaufsangebotIId;
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

	private Timestamp tVersand;

	public Timestamp getTVersand() {
		return tVersand;
	}

	public void setTVersand(Timestamp tVersand) {
		this.tVersand = tVersand;
	}

	private Timestamp tImport;
	private String cAngebotsnummer;

	public Timestamp getTImport() {
		return tImport;
	}

	public void setTImport(Timestamp tImport) {
		this.tImport = tImport;
	}

	public String getCAngebotsnummer() {
		return cAngebotsnummer;
	}

	public void setCAngebotsnummer(String cAngebotsnummer) {
		this.cAngebotsnummer = cAngebotsnummer;
	}

private BigDecimal nAufschlag;
	
	public BigDecimal getNAufschlag() {
		return nAufschlag;
	}

	public void setNAufschlag(BigDecimal nAufschlag) {
		this.nAufschlag = nAufschlag;
	}
	
	private String waehrungCNr;

	public String getWaehrungCNr() {
		return waehrungCNr;
	}

	public void setWaehrungCNr(String waehrungCNr) {
		this.waehrungCNr = waehrungCNr;
	}

	private Integer ansprechpartnerIId;

	public Integer getAnsprechpartnerIId() {
		return ansprechpartnerIId;
	}

	public void setAnsprechpartnerIId(Integer ansprechpartnerIId) {
		this.ansprechpartnerIId = ansprechpartnerIId;
	}
}

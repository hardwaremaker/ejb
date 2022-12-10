package com.lp.server.bestellung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.lp.server.util.ArtikelId;

public class CreateBestellvorschlagDto implements Serializable {
	private static final long serialVersionUID = 4383741236313632027L;

	private ArtikelId artikelId;
	private BigDecimal menge;
	private boolean isVormerkung;
	private Timestamp liefertermin;
	
	public CreateBestellvorschlagDto(ArtikelId artikelId, BigDecimal menge) {
		setArtikelId(artikelId);
		setMenge(menge);
		setVormerkung(true);
	}

	public ArtikelId getArtikelId() {
		return artikelId;
	}
	public void setArtikelId(ArtikelId artikelId) {
		this.artikelId = artikelId;
	}
	
	public BigDecimal getMenge() {
		return menge;
	}
	public void setMenge(BigDecimal menge) {
		this.menge = menge;
	}
	
	public boolean isVormerkung() {
		return isVormerkung;
	}
	public void setVormerkung(boolean isVormerkung) {
		this.isVormerkung = isVormerkung;
	}
	
	public Timestamp getLiefertermin() {
		return liefertermin;
	}
	public void setLiefertermin(Timestamp liefertermin) {
		this.liefertermin = liefertermin;
	}
}

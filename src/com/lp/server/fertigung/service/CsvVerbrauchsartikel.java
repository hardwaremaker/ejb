package com.lp.server.fertigung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class CsvVerbrauchsartikel implements Serializable {
	private static final long serialVersionUID = 8813299603109210700L;

	private String rechnungsnummer;
	private String position;
	private Timestamp rechnungsdatum;
	private String artikelnummer;
	private BigDecimal menge;
	private String artikelbezeichnung;
	private Timestamp abschlussdatum;
	private BigDecimal einzelpreis;
	private BigDecimal gesamtpreis;
	private String zahlungsart;
	private String plu;
	private String artikelhauptgruppe;
	
	public void setRechnungsdatum(Timestamp rechnungsdatum) {
		this.rechnungsdatum = rechnungsdatum;
	}
	
	public Timestamp getRechnungsdatum() {
		return rechnungsdatum;
	}
	
	public void setRechnungsnummer(String rechnungsnummer) {
		this.rechnungsnummer = rechnungsnummer;
	}
	
	public String getRechnungsnummer() {
		return rechnungsnummer;
	}
	
	public void setPosition(String position) {
		this.position = position;
	}
	
	public String getPosition() {
		return position;
	}
	
	public String getArtikelnummer() {
		return artikelnummer;
	}
	
	public void setArtikelnummer(String artikelnummer) {
		this.artikelnummer = artikelnummer;
	}
	
	public BigDecimal getMenge() {
		return menge;
	}
	
	public void setMenge(BigDecimal menge) {
		this.menge = menge;
	}
	
	public String getArtikelbezeichnung() {
		return artikelbezeichnung;
	}
	
	public void setArtikelbezeichnung(String artikelbezeichnung) {
		this.artikelbezeichnung = artikelbezeichnung;
	}
	
	public Timestamp getAbschlussdatum() {
		return abschlussdatum;
	}
	
	public void setAbschlussdatum(Timestamp abschlussdatum) {
		this.abschlussdatum = abschlussdatum;
	}

	public BigDecimal getEinzelpreis() {
		return einzelpreis;
	}

	public void setEinzelpreis(BigDecimal einzelpreis) {
		this.einzelpreis = einzelpreis;
	}

	public BigDecimal getGesamtpreis() {
		return gesamtpreis;
	}

	public void setGesamtpreis(BigDecimal gesamtpreis) {
		this.gesamtpreis = gesamtpreis;
	}

	public String getZahlungsart() {
		return zahlungsart;
	}

	public void setZahlungsart(String zahlungsart) {
		this.zahlungsart = zahlungsart;
	}
	
	public String getPlu() {
		return plu;
	}
	
	public void setPlu(String plu) {
		this.plu = plu;
	}
	
	public String getArtikelhauptgruppe() {
		return artikelhauptgruppe;
	}
	
	public void setArtikelhauptgruppe(String artikelhauptgruppe) {
		this.artikelhauptgruppe = artikelhauptgruppe;
	}
}

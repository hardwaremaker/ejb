package com.lp.server.fertigung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;

public class BedarfsuebernahmeDto implements Serializable {
	private Integer iId;
	private Integer losIId;
	private BigDecimal nWunschmenge;
	private Short bZusaetzlich;
	private Short bAbgang;
	private String cArtikelnummer;
	private String cArtikelbezeichnung;
	private String cKommentar;
	private Integer personalIIdAnlegen;
	private Timestamp tAnlegen;
	private Integer personalIIdAendern;
	private Timestamp tAendern;
	private Integer artikelIId;
	private Integer lossollmaterialIId;
	public Integer getLossollmaterialIId() {
		return lossollmaterialIId;
	}

	private Integer personalIIdVerbuchtGedruckt;
	private Timestamp tVerbuchtGedruckt;
	private byte[] oMedia;

	private String cLosnummer;

	public String getCLosnummer() {
		return cLosnummer;
	}

	public void setCLosnummer(String cLosnummer) {
		this.cLosnummer = cLosnummer;
	}

	private Timestamp tWunschtermin;

	public Timestamp getTWunschtermin() {
		return tWunschtermin;
	}

	public void setTWunschtermin(Timestamp tWunschtermin) {
		this.tWunschtermin = tWunschtermin;
	}

	private String statusCNr;

	public String getStatusCNr() {
		return statusCNr;
	}

	public void setStatusCNr(String statusCNr) {
		this.statusCNr = statusCNr;
	}

	public Integer getLosIId() {
		return losIId;
	}

	public void setLosIId(Integer losIId) {
		this.losIId = losIId;
	}

	public BigDecimal getNWunschmenge() {
		return nWunschmenge;
	}

	public void setNWunschmenge(BigDecimal nWunschmenge) {
		this.nWunschmenge = nWunschmenge;
	}

	public Short getBZusaetzlich() {
		return bZusaetzlich;
	}

	public void setBzusaetzlich(Short bZusaetzlich) {
		this.bZusaetzlich = bZusaetzlich;
	}

	public Short getBAbgang() {
		return bAbgang;
	}

	public void setBAbgang(Short bAbgang) {
		this.bAbgang = bAbgang;
	}

	public String getCArtikelnummer() {
		return cArtikelnummer;
	}

	public void setCArtikelnummer(String cArtikelnummer) {
		this.cArtikelnummer = cArtikelnummer;
	}

	public String getCArtikelbezeichnung() {
		return cArtikelbezeichnung;
	}

	public void setCArtikelbezeichnung(String cArtikelbezeichnung) {
		this.cArtikelbezeichnung = cArtikelbezeichnung;
	}

	public String getcKommentar() {
		return cKommentar;
	}

	public void setCKommentar(String cKommentar) {
		this.cKommentar = cKommentar;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Integer integer() {
		return lossollmaterialIId;
	}

	public void setLossollmaterialIId(Integer lossollmaterialIId) {
		this.lossollmaterialIId = lossollmaterialIId;
	}

	public Integer getPersonalIIdVerbuchtGedruckt() {
		return personalIIdVerbuchtGedruckt;
	}

	public void setPersonalIIdVerbuchtGedruckt(
			Integer personalIIdVerbuchtGedruckt) {
		this.personalIIdVerbuchtGedruckt = personalIIdVerbuchtGedruckt;
	}

	public Timestamp getTVerbuchtGedruckt() {
		return tVerbuchtGedruckt;
	}

	public void setTVerbuchtGedruckt(Timestamp tVerbuchtGedruckt) {
		this.tVerbuchtGedruckt = tVerbuchtGedruckt;
	}

	public byte[] getOMedia() {
		return oMedia;
	}

	public void setOMedia(byte[] oMedia) {
		this.oMedia = oMedia;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

}

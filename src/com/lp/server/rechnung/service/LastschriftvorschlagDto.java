package com.lp.server.rechnung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.lp.server.util.IIId;

public class LastschriftvorschlagDto implements IIId, Serializable {
	private static final long serialVersionUID = 8074305621490410576L;

	private Integer iId;
	private Integer mahnlaufIId;
	private Integer rechnungIId;
	private Date tFaellig;
	private BigDecimal nRechnungsbetrag;
	private BigDecimal nBereitsBezahlt;
	private BigDecimal nZahlbetrag;
	private String cAuftraggeberreferenz;

	private Integer personalIIdAendern;
	private Timestamp tAendern;
	private Integer personalIIdGespeichert;
	private Timestamp tGespeichert;
	
	private String cVerwendungszweck;

	public LastschriftvorschlagDto() {
	}

	@Override
	public Integer getIId() {
		return iId;
	}

	@Override
	public void setIId(Integer newIId) {
		this.iId = newIId;
	}

	public Integer getMahnlaufIId() {
		return mahnlaufIId;
	}
	
	public void setMahnlaufIId(Integer mahnlaufIId) {
		this.mahnlaufIId = mahnlaufIId;
	}
	
	public Integer getRechnungIId() {
		return rechnungIId;
	}

	public void setRechnungIId(Integer rechnungIId) {
		this.rechnungIId = rechnungIId;
	}
	
	public Date getTFaellig() {
		return tFaellig;
	}

	public void setTFaellig(Date tFaellig) {
		this.tFaellig = tFaellig;
	}

	public BigDecimal getNRechnungsbetrag() {
		return nRechnungsbetrag;
	}

	public void setNRechnungsbetrag(BigDecimal nRechnungsbetrag) {
		this.nRechnungsbetrag = nRechnungsbetrag;
	}

	public BigDecimal getNBereitsBezahlt() {
		return nBereitsBezahlt;
	}

	public void setNBereitsBezahlt(BigDecimal nBereitsBezahlt) {
		this.nBereitsBezahlt = nBereitsBezahlt;
	}

	public BigDecimal getNZahlbetrag() {
		return nZahlbetrag;
	}

	public void setNZahlbetrag(BigDecimal nZahlbetrag) {
		this.nZahlbetrag = nZahlbetrag;
	}

	public String getCAuftraggeberreferenz() {
		return cAuftraggeberreferenz;
	}

	public void setCAuftraggeberreferenz(String cAuftraggeberreferenz) {
		this.cAuftraggeberreferenz = cAuftraggeberreferenz;
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

	public Integer getPersonalIIdGespeichert() {
		return personalIIdGespeichert;
	}

	public void setPersonalIIdGespeichert(Integer personalIIdGespeichert) {
		this.personalIIdGespeichert = personalIIdGespeichert;
	}

	public Timestamp getTGespeichert() {
		return tGespeichert;
	}

	public void setTGespeichert(Timestamp tGespeichert) {
		this.tGespeichert = tGespeichert;
	}

	public String getCVerwendungszweck() {
		return cVerwendungszweck;
	}
	
	public void setCVerwendungszweck(String cVerwendungszweck) {
		this.cVerwendungszweck = cVerwendungszweck;
	}
}

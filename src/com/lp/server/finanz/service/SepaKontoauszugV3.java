package com.lp.server.finanz.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class SepaKontoauszugV3 implements Serializable {

	/**
	 * Diese Id darf NIE geaendert werden
	 */
	private static final long serialVersionUID = -512622170235568577L;

	private SepaKontoauszugVersionEnum version;
	private String camtVersion;
	private Date erstellungsdatum;
	private String empfaenger;
	private String messageId;
	private String auszugId;
	private Integer seitennummer;
	private Boolean isLetzteSeite;
	private BigDecimal elektronischeAuszugsNr;
	private BigDecimal auszugsNr;
	private SepaKontoinformationV3 kontoinformation;
	private List<SepaSaldoV3> salden;
	private List<SepaBuchungV3> buchungen;
	private String zusatzinformation;

	public SepaKontoauszugV3() {
	}

	public SepaKontoauszugVersionEnum getVersion() {
		return version;
	}

	public void setVersion(SepaKontoauszugVersionEnum version) {
		this.version = version;
	}

	public String getCamtVersion() {
		return camtVersion;
	}

	public void setCamtVersion(String camtVersion) {
		this.camtVersion = camtVersion;
	}

	public Date getErstellungsdatum() {
		return erstellungsdatum;
	}

	public void setErstellungsdatum(Date erstellungsdatum) {
		this.erstellungsdatum = erstellungsdatum;
	}

	public String getEmpfaenger() {
		return empfaenger;
	}

	public void setEmpfaenger(String empfaenger) {
		this.empfaenger = empfaenger;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getAuszugId() {
		return auszugId;
	}

	public void setAuszugId(String auszugId) {
		this.auszugId = auszugId;
	}

	public Integer getSeitennummer() {
		return seitennummer;
	}

	public void setSeitennummer(Integer seitennummer) {
		this.seitennummer = seitennummer;
	}

	public Boolean getIsLetzteSeite() {
		return isLetzteSeite;
	}

	public void setIsLetzteSeite(Boolean isLetzteSeite) {
		this.isLetzteSeite = isLetzteSeite;
	}

	public BigDecimal getElektronischeAuszugsNr() {
		return elektronischeAuszugsNr;
	}

	public void setElektronischeAuszugsNr(BigDecimal elektronischeAuszugsNr) {
		this.elektronischeAuszugsNr = elektronischeAuszugsNr;
	}

	public BigDecimal getAuszugsNr() {
		return auszugsNr;
	}

	public void setAuszugsNr(BigDecimal auszugsNr) {
		this.auszugsNr = auszugsNr;
	}

	public SepaKontoinformationV3 getKontoinformation() {
		return kontoinformation;
	}

	public void setKontoinformation(SepaKontoinformationV3 kontoinformation) {
		this.kontoinformation = kontoinformation;
	}

	public List<SepaSaldoV3> getSalden() {
		return salden;
	}

	public void setSalden(List<SepaSaldoV3> salden) {
		this.salden = salden;
	}

	public List<SepaBuchungV3> getBuchungen() {
		return buchungen;
	}

	public void setBuchungen(List<SepaBuchungV3> buchungen) {
		this.buchungen = buchungen;
	}

	public String getZusatzinformation() {
		return zusatzinformation;
	}

	public void setZusatzinformation(String zusatzinformation) {
		this.zusatzinformation = zusatzinformation;
	}

}

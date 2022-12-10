package com.lp.server.finanz.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class SepaKontoauszugV2 implements Serializable {

	/**
	 * Diese Id darf NIE geaendert werden
	 */
	private static final long serialVersionUID = 2684744760599553115L;

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
	private SepaKontoinformationV2 kontoinformation;
	private List<SepaSaldoV2> salden;
	private List<SepaBuchungV2> buchungen;
	
	public SepaKontoauszugV2() {
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

	public SepaKontoinformationV2 getKontoinformation() {
		return kontoinformation;
	}

	public void setKontoinformation(SepaKontoinformationV2 kontoinformation) {
		this.kontoinformation = kontoinformation;
	}

	public List<SepaSaldoV2> getSalden() {
		return salden;
	}

	public void setSalden(List<SepaSaldoV2> salden) {
		this.salden = salden;
	}

	public List<SepaBuchungV2> getBuchungen() {
		return buchungen;
	}

	public void setBuchungen(List<SepaBuchungV2> buchungen) {
		this.buchungen = buchungen;
	}
	
}

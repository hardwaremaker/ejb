package com.lp.server.finanz.service;

import java.io.Serializable;

public class SepaZahlungV2 implements Serializable {

	/**
	 * Diese Id darf NIE geaendert werden
	 */
	private static final long serialVersionUID = 3151971887012861422L;

	private SepaBetragV2 betrag;
	private String verwendungszweck; 
	private String zahlungsreferenz;
	private String auftraggeberreferenz;
	private String bankreferenz;
	private SepaKontoinformationV2 kontoinformation;
	private SepaBankTransactionCodeV2 buchungscode;
	
	private String glaeubigerId;
	private Integer enthalteneBuchungen;
	private String mandatsreferenz;
	private String bestandsreferenzKunde;
	
	public SepaZahlungV2() {
	}

	public SepaBetragV2 getBetrag() {
		return betrag;
	}

	public void setBetrag(SepaBetragV2 betrag) {
		this.betrag = betrag;
	}

	public String getVerwendungszweck() {
		return verwendungszweck;
	}

	public void setVerwendungszweck(String verwendungszweck) {
		this.verwendungszweck = verwendungszweck;
	}

	public String getZahlungsreferenz() {
		return zahlungsreferenz;
	}

	public void setZahlungsreferenz(String zahlungsreferenz) {
		this.zahlungsreferenz = zahlungsreferenz;
	}

	public String getAuftraggeberreferenz() {
		return auftraggeberreferenz;
	}

	public void setAuftraggeberreferenz(String auftraggeberreferenz) {
		this.auftraggeberreferenz = auftraggeberreferenz;
	}

	public String getBankreferenz() {
		return bankreferenz;
	}

	public void setBankreferenz(String bankreferenz) {
		this.bankreferenz = bankreferenz;
	}

	public SepaKontoinformationV2 getKontoinformation() {
		return kontoinformation;
	}

	public void setKontoinformation(SepaKontoinformationV2 kontoinformation) {
		this.kontoinformation = kontoinformation;
	}

	public SepaBankTransactionCodeV2 getBuchungscode() {
		return buchungscode;
	}

	public void setBuchungscode(SepaBankTransactionCodeV2 buchungscode) {
		this.buchungscode = buchungscode;
	}

	public String getGlaeubigerId() {
		return glaeubigerId;
	}

	public void setGlaeubigerId(String glaeubigerId) {
		this.glaeubigerId = glaeubigerId;
	}

	public Integer getEnthalteneBuchungen() {
		return enthalteneBuchungen;
	}

	public void setEnthalteneBuchungen(Integer enthalteneBuchungen) {
		this.enthalteneBuchungen = enthalteneBuchungen;
	}

	public String getMandatsreferenz() {
		return mandatsreferenz;
	}

	public void setMandatsreferenz(String mandatsreferenz) {
		this.mandatsreferenz = mandatsreferenz;
	}

	public String getBestandsreferenzKunde() {
		return bestandsreferenzKunde;
	}

	public void setBestandsreferenzKunde(String bestandsreferenzKunde) {
		this.bestandsreferenzKunde = bestandsreferenzKunde;
	}
	
}

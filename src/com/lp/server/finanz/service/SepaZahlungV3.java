package com.lp.server.finanz.service;

import java.io.Serializable;
import java.util.List;

public class SepaZahlungV3 implements Serializable {

	/**
	 * Diese Id darf NIE geaendert werden
	 */
	private static final long serialVersionUID = 6174577780449980033L;

	private SepaBetragV3 betrag;
	private String verwendungszweck; 
	private String zahlungsreferenz;
	private String auftraggeberreferenz;
	private String bankreferenz;
	private SepaKontoinformationV3 kontoinformation;
	private SepaBankTransactionCodeV3 buchungscode;
	
	private String glaeubigerId;
	private Integer enthalteneBuchungen;
	private String mandatsreferenz;
	private String bestandsreferenzKunde;
	
	private String zusatzinformation;
	private List<SepaSpesenV3> spesen;

	public SepaZahlungV3() {
	}

	public SepaBetragV3 getBetrag() {
		return betrag;
	}

	public void setBetrag(SepaBetragV3 betrag) {
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

	public SepaKontoinformationV3 getKontoinformation() {
		return kontoinformation;
	}

	public void setKontoinformation(SepaKontoinformationV3 kontoinformation) {
		this.kontoinformation = kontoinformation;
	}

	public SepaBankTransactionCodeV3 getBuchungscode() {
		return buchungscode;
	}

	public void setBuchungscode(SepaBankTransactionCodeV3 buchungscode) {
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

	public String getZusatzinformation() {
		return zusatzinformation;
	}

	public void setZusatzinformation(String zusatzinformation) {
		this.zusatzinformation = zusatzinformation;
	}

	public List<SepaSpesenV3> getSpesen() {
		return spesen;
	}
	
	public void setSpesen(List<SepaSpesenV3> spesen) {
		this.spesen = spesen;
	}
}

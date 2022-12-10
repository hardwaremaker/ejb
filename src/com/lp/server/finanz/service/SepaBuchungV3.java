package com.lp.server.finanz.service;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class SepaBuchungV3 implements Serializable {

	/**
	 * Diese Id darf NIE geaendert werden
	 */
	private static final long serialVersionUID = -6876189023529057381L;

	private String waehrung;
	private String status; 
	private Date buchungsdatum; 
	private Date valutadatum; 
	private String bankreferenz; 
	private SepaBetragV3 betrag;
	private SepaBankTransactionCodeV3 buchungscode;
	private List<SepaZahlungV3> zahlungen;
	
	private String zusatzinformation;

	public SepaBuchungV3() {
	}

	public String getWaehrung() {
		return waehrung;
	}

	public void setWaehrung(String waehrung) {
		this.waehrung = waehrung;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getBuchungsdatum() {
		return buchungsdatum;
	}

	public void setBuchungsdatum(Date buchungsdatum) {
		this.buchungsdatum = buchungsdatum;
	}

	public Date getValutadatum() {
		return valutadatum;
	}

	public void setValutadatum(Date valutadatum) {
		this.valutadatum = valutadatum;
	}

	public String getBankreferenz() {
		return bankreferenz;
	}

	public void setBankreferenz(String bankreferenz) {
		this.bankreferenz = bankreferenz;
	}

	public SepaBetragV3 getBetrag() {
		return betrag;
	}

	public void setBetrag(SepaBetragV3 betrag) {
		this.betrag = betrag;
	}

	public SepaBankTransactionCodeV3 getBuchungscode() {
		return buchungscode;
	}

	public void setBuchungscode(SepaBankTransactionCodeV3 buchungscode) {
		this.buchungscode = buchungscode;
	}

	public List<SepaZahlungV3> getZahlungen() {
		return zahlungen;
	}

	public void setZahlungen(List<SepaZahlungV3> zahlungen) {
		this.zahlungen = zahlungen;
	}

	public String getZusatzinformation() {
		return zusatzinformation;
	}

	public void setZusatzinformation(String zusatzinformation) {
		this.zusatzinformation = zusatzinformation;
	}

}

package com.lp.server.finanz.service;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class SepaBuchungV2 implements Serializable {

	/**
	 * Diese Id darf NIE geaendert werden
	 */
	private static final long serialVersionUID = 7538032802599649398L;

	private String waehrung;
	private String status; 
	private Date buchungsdatum; 
	private Date valutadatum; 
	private String bankreferenz; 
	private SepaBetragV2 betrag;
	private SepaBankTransactionCodeV2 buchungscode;
	private List<SepaZahlungV2> zahlungen;
	
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
	public SepaBetragV2 getBetrag() {
		return betrag;
	}
	public void setBetrag(SepaBetragV2 betrag) {
		this.betrag = betrag;
	}
	public SepaBankTransactionCodeV2 getBuchungscode() {
		return buchungscode;
	}
	public void setBuchungscode(SepaBankTransactionCodeV2 buchungscode) {
		this.buchungscode = buchungscode;
	}
	public List<SepaZahlungV2> getZahlungen() {
		return zahlungen;
	}
	public void setZahlungen(List<SepaZahlungV2> zahlungen) {
		this.zahlungen = zahlungen;
	}
	
}

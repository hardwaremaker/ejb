package com.lp.server.finanz.service;

import java.io.Serializable;
import java.sql.Date;

public class SepaSaldoV2 implements Serializable {

	/**
	 * Diese Id darf NIE geaendert werden
	 */
	private static final long serialVersionUID = -4202439701187881840L;

	private String waehrung;
	private Date datum;
	private String saldocode;
	private SepaBetragV2 betrag;
	
	public SepaSaldoV2() {
	}

	public String getWaehrung() {
		return waehrung;
	}

	public void setWaehrung(String waehrung) {
		this.waehrung = waehrung;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public String getSaldocode() {
		return saldocode;
	}

	public void setSaldocode(String saldocode) {
		this.saldocode = saldocode;
	}

	public SepaBetragV2 getBetrag() {
		return betrag;
	}

	public void setBetrag(SepaBetragV2 betrag) {
		this.betrag = betrag;
	}
	
}

package com.lp.server.finanz.service;

import java.io.Serializable;
import java.sql.Date;

public class SepaSaldoV3 implements Serializable {

	/**
	 * Diese Id darf NIE geaendert werden
	 */
	private static final long serialVersionUID = 1351275171262110807L;

	private String waehrung;
	private Date datum;
	private String saldocode;
	private SepaBetragV3 betrag;

	public SepaSaldoV3() {
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

	public SepaBetragV3 getBetrag() {
		return betrag;
	}

	public void setBetrag(SepaBetragV3 betrag) {
		this.betrag = betrag;
	}

}

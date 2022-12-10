package com.lp.server.finanz.service;

import java.io.Serializable;

public class SepaSpesenV3 implements Serializable {
	/**
	 * Diese Id darf NIE geaendert werden
	 */
	private static final long serialVersionUID = -1830286460291496595L;

	private SepaBetragV3 betrag;
	private String info;
	
	public SepaSpesenV3() {
	}

	public SepaBetragV3 getBetrag() {
		return betrag;
	}
	public void setBetrag(SepaBetragV3 betrag) {
		this.betrag = betrag;
	}
	
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
}

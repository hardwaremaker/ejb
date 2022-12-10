package com.lp.server.finanz.service;

import java.io.Serializable;

public class SepaSpesen implements Serializable {
	private static final long serialVersionUID = -6226276382493254469L;

	private SepaBetrag betrag;
	private String info;
	
	public SepaSpesen() {
	}

	public SepaBetrag getBetrag() {
		return betrag;
	}
	public void setBetrag(SepaBetrag betrag) {
		this.betrag = betrag;
	}
	
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	@Override
	public String toString() {
		return "SepaSpesen [betrag = " + getBetrag() + ", info = " + getInfo() + "]";
	}
}

package com.lp.server.fertigung.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lp.util.EJBExceptionLP;

public class RueckgabeMehrereLoseAusgeben implements Serializable {
	private static final long serialVersionUID = -4212812649969180389L;

	String meldungZuAktualisieren;
	ArrayList<Integer> alAusgegeben;
	
	private List<EJBExceptionLP> losausgabeReturnedExc;
	
	public String getMeldungZuAktualisieren() {
		return meldungZuAktualisieren;
	}
	public void setMeldungZuAktualisieren(String meldungZuAktualisieren) {
		this.meldungZuAktualisieren = meldungZuAktualisieren;
	}
	public ArrayList<Integer> getAlAusgegeben() {
		return alAusgegeben;
	}
	public void setAlAusgegeben(ArrayList<Integer> alAusgegeben) {
		this.alAusgegeben = alAusgegeben;
	}
	
	public void setLosausgabeReturnedExc(List<EJBExceptionLP> losausgabeReturnedExc) {
		this.losausgabeReturnedExc = losausgabeReturnedExc;
	}
	
	public List<EJBExceptionLP> getLosausgabeReturnedExc() {
		if (losausgabeReturnedExc == null) {
			losausgabeReturnedExc = new ArrayList<EJBExceptionLP>();
		}
		return losausgabeReturnedExc;
	}
}

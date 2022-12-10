package com.lp.server.personal.service;

import java.io.Serializable;

import com.lp.server.util.report.JasperPrintLP;

public class MonatsabrechnungEmailVersand implements Serializable {
	public MonatsabrechnungEmailVersand(Integer personalIId, String privateEmailAdresse,
			String kennwort, Integer kostenstelleIIdAbteilung, JasperPrintLP print) {
		this.personalIId = personalIId;
		this.privateEmailAdresse = privateEmailAdresse;
		this.kennwort = kennwort;
		this.kostenstelleIIdAbteilung=kostenstelleIIdAbteilung;
		this.print=print;
	}

	private JasperPrintLP print =null;
	
	public JasperPrintLP getPrint() {
		return print;
	}

	private Integer kostenstelleIIdAbteilung=null;
	
	public Integer getKostenstelleIIdAbteilung() {
		return kostenstelleIIdAbteilung;
	}

	private Integer personalIId = null;

	public Integer getPersonalIId() {
		return personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

	public String getPrivateEmailAdresse() {
		return privateEmailAdresse;
	}

	public void setPrivateEmailAdresse(String privateEmailAdresse) {
		this.privateEmailAdresse = privateEmailAdresse;
	}

	public String getKennwort() {

		if (kennwort != null) {
			return kennwort.trim().replaceAll(" ", "");
		} else {
			return null;
		}

	}

	private String privateEmailAdresse = null;
	private String kennwort = null;

}

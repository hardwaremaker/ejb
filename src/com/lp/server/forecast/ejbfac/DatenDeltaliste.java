package com.lp.server.forecast.ejbfac;

import java.math.BigDecimal;


public class DatenDeltaliste {

	public BigDecimal bdMengeAlt = BigDecimal.ZERO;
	public BigDecimal bdMengeNeu = BigDecimal.ZERO;
	public Integer forecastpositionIId = null;
	
	public BigDecimal bdMengeLinienabrufeAlt = BigDecimal.ZERO;
	public BigDecimal bdMengeLinienabrufeNeu = BigDecimal.ZERO;
	public String kommentarAlt = "";
	public String kommentarNeu = "";

	private String statusAltCnr;
	private String statusNeuCnr;
	private Integer forecastpositionIIdNeu;
	
	public void addToKommentarAlt(String s) {
		if (s != null) {
			kommentarAlt = kommentarAlt + " " + s;
		}
	}

	public void addToKommentarNeu(String s) {
		if (s != null) {
			kommentarNeu = kommentarNeu + " " + s;
		}
	}

	public String getStatusCnrAlt() {
		return statusAltCnr;
	}

	public void setStatusCnrAlt(String statusCnr) {
		this.statusAltCnr = statusCnr;
	}

	public String getStatusCnrNeu() {
		return statusNeuCnr;
	}

	public void setStatusCnrNeu(String statusNeuCnr) {
		this.statusNeuCnr = statusNeuCnr;
	}

	public Integer getForecastpositionIIdNeu() {
		return forecastpositionIIdNeu;
	}

	public void setForecastpositionIIdNeu(Integer forecastpositionIIdNeu) {
		this.forecastpositionIIdNeu = forecastpositionIIdNeu;
	}
}

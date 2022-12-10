package com.lp.server.forecast.service;

import java.io.Serializable;
import java.util.ArrayList;

public class ForecastImportFehlerDto implements Serializable {
	private String error = null;
	private ArrayList<String> neueArtikel = new ArrayList<String>();

	public ForecastImportFehlerDto() {

	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void addNeueArtikel(String herstellernr) {
		neueArtikel.add(herstellernr);
	}

	public ArrayList getNeueArtikel() {
		return neueArtikel;
	}
}
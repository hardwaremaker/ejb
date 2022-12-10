package com.lp.server.artikel.service;

import java.io.Serializable;
import java.util.ArrayList;

public class ArtikelimportFehlerDto implements Serializable {
	private String error = null;
	private ArrayList<String> herstellernrNichtGefunden = new ArrayList<String>();

	public ArtikelimportFehlerDto() {

	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void addHerstellernrNichtGefunden(String herstellernr) {
		herstellernrNichtGefunden.add(herstellernr);
	}

	public ArrayList getHerstellernrNichtGefunden() {
		return herstellernrNichtGefunden;
	}
}

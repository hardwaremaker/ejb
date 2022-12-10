package com.lp.server.stueckliste.service;

import java.io.Serializable;

import com.lp.server.util.ArtikelId;

public class FlatPartlistPositionInfo implements Serializable {
	private static final long serialVersionUID = 6574877551260158126L;

	private ArtikelId artikelId;
	private String artikelCnr;
	private ArtikelId stklArtikelId;
	private String stklArtikelCnr;
	
	public FlatPartlistPositionInfo() {
	}

	public ArtikelId getArtikelId() {
		return artikelId;
	}

	public void setArtikelId(ArtikelId artikelId) {
		this.artikelId = artikelId;
	}

	public String getArtikelCnr() {
		return artikelCnr;
	}

	public void setArtikelCnr(String artikelCnr) {
		this.artikelCnr = artikelCnr;
	}

	public ArtikelId getStklArtikelId() {
		return stklArtikelId;
	}

	public void setStklArtikelId(ArtikelId stklArtikelId) {
		this.stklArtikelId = stklArtikelId;
	}

	public String getStklArtikelCnr() {
		return stklArtikelCnr;
	}

	public void setStklArtikelCnr(String stklArtikelCnr) {
		this.stklArtikelCnr = stklArtikelCnr;
	}

}

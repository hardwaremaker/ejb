package com.lp.server.artikel.service;

import java.io.Serializable;


public class ArtikelsnrchnrDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iId;

	private Integer artikelIId;

	private String cSeriennrchargennr;

	public Integer getiId() {
		return iId;
	}

	public void setiId(Integer iId) {
		this.iId = iId;
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public String getcSeriennrchargennr() {
		return cSeriennrchargennr;
	}

	public void setcSeriennrchargennr(String cSeriennrchargennr) {
		this.cSeriennrchargennr = cSeriennrchargennr;
	}
	
}

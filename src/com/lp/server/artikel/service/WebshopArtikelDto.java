package com.lp.server.artikel.service;

import java.io.Serializable;
import java.sql.Timestamp;

import com.lp.server.util.ArtikelId;
import com.lp.server.util.IIId;
import com.lp.server.util.WebshopId;

public class WebshopArtikelDto implements Serializable, IIId {
	private static final long serialVersionUID = 3233411664481912612L;
	
	private Integer iId;
	private WebshopId webshopId;
	private ArtikelId artikelId;
	private String externalId;
	private Timestamp tAendern;
	private Timestamp tAnlegen;

	
	@Override
	public Integer getIId() {
		return iId;
	}

	@Override
	public void setIId(Integer newIId) {
		this.iId = newIId;
	}

	public WebshopId getWebshopId() {
		return webshopId;
	}

	public void setWebshopId(WebshopId webshopId) {
		this.webshopId = webshopId;
	}

	public ArtikelId getArtikelId() {
		return artikelId;
	}

	public Integer getArtikelIId() {
		return artikelId == null ? null : artikelId.id();
	}
	
	public void setArtikelId(ArtikelId artikelId) {
		this.artikelId = artikelId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelId = new ArtikelId(artikelIId);
	}
	
	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}
}

package com.lp.server.artikel.service;

import java.io.Serializable;
import java.sql.Timestamp;

import com.lp.server.util.IIId;

public class WebshopShopgruppeDto implements Serializable, IIId {
	private static final long serialVersionUID = 4365190881022603449L;
	
	private Integer iId;
	private Integer webshopId;
	private Integer shopgruppeId;
	private String externalId;
	private String pfad;
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

	public Integer getWebshopId() {
		return webshopId;
	}

	public void setWebshopId(Integer webshopId) {
		this.webshopId = webshopId;
	}

	public Integer getShopgruppeId() {
		return shopgruppeId;
	}

	public void setShopgruppeId(Integer shopgruppeId) {
		this.shopgruppeId = shopgruppeId;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getPfad() {
		return pfad;
	}

	public void setPfad(String pfad) {
		this.pfad = pfad;
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

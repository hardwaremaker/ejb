package com.lp.server.artikel.service;

import java.io.Serializable;
import java.sql.Timestamp;

import com.lp.server.util.IIId;
import com.lp.server.util.PreislisteId;
import com.lp.server.util.WebshopId;

public class WebshopArtikelPreislisteDto implements Serializable, IIId {
	private static final long serialVersionUID = 3233411664481912612L;
	
	private Integer iId;
	private WebshopId webshopId;
	private PreislisteId preislisteId;
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

	public PreislisteId getPreislisteId() {
		return preislisteId;
	}

	public Integer getPreislisteIId() {
		return preislisteId == null ? null : preislisteId.id();
	}
	
	public void setPreislisteId(PreislisteId preislisteId) {
		this.preislisteId = preislisteId;
	}

	public void setPreislisteIId(Integer preislisteIId) {
		this.preislisteId = new PreislisteId(preislisteIId);
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

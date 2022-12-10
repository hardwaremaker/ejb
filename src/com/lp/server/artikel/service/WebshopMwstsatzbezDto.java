package com.lp.server.artikel.service;

import java.io.Serializable;
import java.sql.Timestamp;

import com.lp.server.util.IIId;
import com.lp.server.util.MwstsatzbezId;
import com.lp.server.util.WebshopId;

public class WebshopMwstsatzbezDto implements Serializable, IIId {
	private static final long serialVersionUID = 3233411664481912612L;
	
	private Integer iId;
	private WebshopId webshopId;
	private MwstsatzbezId mwstsatzbezId;
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

	public MwstsatzbezId getMwstsatzbezId() {
		return mwstsatzbezId;
	}

	public Integer getMwstsatzbezIId() {
		return mwstsatzbezId == null ? null : mwstsatzbezId.id();
	}
	
	public void setMwstsatzbezId(MwstsatzbezId mwstsatzbezId) {
		this.mwstsatzbezId = mwstsatzbezId;
	}

	public void setMwstsatzbezIId(Integer mwstsatzbezIId) {
		this.mwstsatzbezId = new MwstsatzbezId(mwstsatzbezIId);
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

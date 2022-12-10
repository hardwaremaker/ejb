package com.lp.server.shop.ejbfac;

import java.sql.Timestamp;

import com.lp.server.system.service.JobDetailsDto;
import com.lp.server.util.WebshopId;

public class JobDetailsSyncItemDto extends JobDetailsDto {
	private static final long serialVersionUID = -3786524305788907753L;

	private WebshopId shopId;
	private Timestamp tVollstaendig;
	private Timestamp tGeaendert;
	private String cEmailFehler;
	private String cEmailErfolgreich;

	public WebshopId getShopId() {
		return shopId;
	}

	public void setShopId(WebshopId shopId) {
		this.shopId = shopId;
	}
	
	public void setWebshopIId(Integer webshopIId) {
		this.shopId = new WebshopId(webshopIId);
	}
	
	public Integer getWebshopIId() {
		return shopId == null ? null : shopId.id();
	}

	public Timestamp getTVollstaendig() {
		return tVollstaendig;
	}

	public void setTVollstaendig(Timestamp tVollstaendig) {
		this.tVollstaendig = tVollstaendig;
	}

	public Timestamp getTGeaendert() {
		return tGeaendert;
	}

	public void setTGeaendert(Timestamp tGeaendert) {
		this.tGeaendert = tGeaendert;
	}

	public String getCEmailFehler() {
		return cEmailFehler;
	}

	public void setCEmailFehler(String cEmailFehler) {
		this.cEmailFehler = cEmailFehler;
	}

	public String getCEmailErfolgreich() {
		return cEmailErfolgreich;
	}

	public void setCEmailErfolgreich(String cEmailErfolgreich) {
		this.cEmailErfolgreich = cEmailErfolgreich;
	}
}

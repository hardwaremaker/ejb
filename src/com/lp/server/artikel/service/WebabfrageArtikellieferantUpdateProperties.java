package com.lp.server.artikel.service;

import java.io.Serializable;

import com.lp.server.util.ArtikelId;
import com.lp.server.util.LieferantId;

public class WebabfrageArtikellieferantUpdateProperties implements Serializable {
	private static final long serialVersionUID = -2181974069509871686L;

	private ArtikelId artikelId;
	private LieferantId lieferantId;
	private WebPart webPart;
	
	public WebabfrageArtikellieferantUpdateProperties(ArtikelId artikelId, LieferantId lieferantId, WebPart webPart) {
		setArtikelId(artikelId);
		setLieferantId(lieferantId);
		setWebPart(webPart);
	}

	public ArtikelId getArtikelId() {
		return artikelId;
	}
	
	public void setArtikelId(ArtikelId artikelId) {
		this.artikelId = artikelId;
	}
	
	public LieferantId getLieferantId() {
		return lieferantId;
	}
	
	public void setLieferantId(LieferantId lieferantId) {
		this.lieferantId = lieferantId;
	}
	
	public WebPart getWebPart() {
		return webPart;
	}
	
	public void setWebPart(WebPart webPart) {
		this.webPart = webPart;
	}
}

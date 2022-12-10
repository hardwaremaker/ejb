package com.lp.server.artikel.service;

import java.io.Serializable;

import com.lp.server.util.ArtikelId;
import com.lp.server.util.LieferantId;

public class WebabfrageArtikellieferantProperties implements Serializable {
	private static final long serialVersionUID = 4848181763722030497L;

	private ArtikelId artikelId;
	private LieferantId lieferantId;
	private boolean update;
	
	public WebabfrageArtikellieferantProperties(ArtikelId artikelId) {
		setArtikelId(artikelId);
		update = false;
	}

	public WebabfrageArtikellieferantProperties(ArtikelId artikelId, LieferantId lieferantId) {
		this(artikelId);
		setLieferantId(lieferantId);
	}
	
	public void setArtikelId(ArtikelId artikelId) {
		this.artikelId = artikelId;
	}
	
	public ArtikelId getArtikelId() {
		return artikelId;
	}
	
	public void setLieferantId(LieferantId lieferantId) {
		this.lieferantId = lieferantId;
	}
	
	public LieferantId getLieferantId() {
		return lieferantId;
	}
	
	public boolean isUpdate() {
		return update;
	}
	
	public void setUpdate(boolean update) {
		this.update = update;
	}
}

package com.lp.server.stueckliste.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lp.server.util.ArtikelId;
import com.lp.server.util.StuecklisteId;

public class FlatPartlistInfo implements Serializable {
	private static final long serialVersionUID = -3655482672547987941L;

	private StuecklisteId id;
	private ArtikelId artikelId;
	private String artikelCnr;
	private List<FlatPartlistPositionInfo> positions;
	
	public FlatPartlistInfo() {
	}

	public StuecklisteId getId() {
		return id;
	}

	public void setId(StuecklisteId id) {
		this.id = id;
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

	public List<FlatPartlistPositionInfo> getPositions() {
		return positions;
	}

	public void setPositions(List<FlatPartlistPositionInfo> positions) {
		if (positions == null) {
			positions = new ArrayList<FlatPartlistPositionInfo>();
		}
		this.positions = positions;
	}

}

package com.lp.server.personal.service;

import java.io.Serializable;

public class ZeiterfassungFavoritenDto implements Serializable {

	public static ZeiterfassungFavoritenDto clone(ZeiterfassungFavoritenDto orig) {
		ZeiterfassungFavoritenDto klon = new ZeiterfassungFavoritenDto(
				orig.getZeitdatenIId(), orig.getArtikelIId());
		return klon;
	}

	private static final long serialVersionUID = 1L;

	public ZeiterfassungFavoritenDto(Integer zeitdatenIId, Integer artikelIId) {

		this.zeitdatenIId = zeitdatenIId;
		this.artikelIId = artikelIId;
	}

	Integer zeitdatenIId = null;
	Integer artikelIId = null;

	public Integer getZeitdatenIId() {
		return zeitdatenIId;
	}

	public void setZeitdatenIId(Integer zeitdatenIId) {
		this.zeitdatenIId = zeitdatenIId;
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}
}

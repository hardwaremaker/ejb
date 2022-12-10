package com.lp.server.fertigung.bl;

import com.lp.server.fertigung.service.CsvVerbrauchsartikel;

public class VerbrauchsartikelExtended extends CsvVerbrauchsartikel {
	private static final long serialVersionUID = 1966980296729914359L;
	private String kommentar;
	
	public VerbrauchsartikelExtended(CsvVerbrauchsartikel verbrauchsartikel) {
		setAbschlussdatum(verbrauchsartikel.getAbschlussdatum());
		setArtikelbezeichnung(verbrauchsartikel.getArtikelbezeichnung());
		setArtikelnummer(verbrauchsartikel.getArtikelnummer());
		setMenge(verbrauchsartikel.getMenge());
		setPosition(verbrauchsartikel.getPosition());
		setRechnungsdatum(verbrauchsartikel.getRechnungsdatum());
		setRechnungsnummer(verbrauchsartikel.getRechnungsnummer());
		setEinzelpreis(verbrauchsartikel.getEinzelpreis());
		setGesamtpreis(verbrauchsartikel.getGesamtpreis());
		setZahlungsart(verbrauchsartikel.getZahlungsart());
		setPlu(verbrauchsartikel.getPlu());
	}
	
	public void setKommentar(String kommentar) {
		this.kommentar = kommentar;
	}
	
	public String getKommentar() {
		return kommentar;
	}
}


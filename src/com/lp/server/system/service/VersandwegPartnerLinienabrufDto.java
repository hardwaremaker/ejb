package com.lp.server.system.service;

import java.io.Serializable;

public class VersandwegPartnerLinienabrufDto extends VersandwegPartnerDto implements Serializable {
	private static final long serialVersionUID = -3273911146649769282L;

	private String cExportPfad;
	private String cKopfzeile;
	private String cDatenzeile;
	private String cBestellnummer;
	
	public String getCExportPfad() {
		return cExportPfad;
	}

	public void setCExportPfad(String cExportPfad) {
		this.cExportPfad = cExportPfad;
	}

	public String getCDatenzeile() {
		return cDatenzeile;
	}

	public void setCDatenzeile(String cDatenzeile) {
		this.cDatenzeile = cDatenzeile;
	}

	public String getCKopfzeile() {
		return cKopfzeile;
	}

	public void setCKopfzeile(String cKopfzeile) {
		this.cKopfzeile = cKopfzeile;
	}

	public String getcBestellnummer() {
		return cBestellnummer;
	}

	public void setcBestellnummer(String cBestellnummer) {
		this.cBestellnummer = cBestellnummer;
	}
}

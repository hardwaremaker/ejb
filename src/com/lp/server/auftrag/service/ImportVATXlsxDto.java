/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.server.auftrag.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class ImportVATXlsxDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	private int zeile;
	private String lieferadresse;

	public int getZeile() {
		return zeile;
	}
	public void setZeile(int zeile) {
		this.zeile = zeile;
	}
	public String getLieferadresse() {
		return lieferadresse;
	}
	public void setLieferadresse(String lieferadresse) {
		this.lieferadresse = lieferadresse;
	}
	private String artikelnummer = null;
	public String getArtikelnummer() {
		return artikelnummer;
	}
	public void setArtikelnummer(String artikelnummer) {
		this.artikelnummer = artikelnummer;
	}
	private String best_text = null;
	public String getBest_text() {
		return best_text;
	}
	public void setBest_text(String best_text) {
		this.best_text = best_text;
	}
	private String projektnummer = null;
	private java.sql.Timestamp liefertermin = null;
	
	
	public String getProjektnummer() {
		return projektnummer;
	}
	public void setProjektnummer(String projektnummer) {
		this.projektnummer = projektnummer;
	}
	public java.sql.Timestamp getLiefertermin() {
		return liefertermin;
	}
	public void setLiefertermin(java.sql.Timestamp liefertermin) {
		this.liefertermin = liefertermin;
	}
	public String getWaehrung() {
		return waehrung;
	}
	public void setWaehrung(String waehrung) {
		this.waehrung = waehrung;
	}
	public BigDecimal getMenge() {
		return menge;
	}
	public void setMenge(BigDecimal menge) {
		this.menge = menge;
	}
	public BigDecimal getPreis() {
		return preis;
	}
	public void setPreis(BigDecimal preis) {
		this.preis = preis;
	}
	private String waehrung = null;
	private BigDecimal menge = null;
	private BigDecimal preis = null;
	private String zusatzbezeichnung = null;
	private String bestellnummer = null;

	public String getBestellnummer() {
		return bestellnummer;
	}
	public void setBestellnummer(String bestellnummer) {
		this.bestellnummer = bestellnummer;
	}
	public String getZusatzbezeichnung() {
		return zusatzbezeichnung;
	}
	public void setZusatzbezeichnung(String zusatzbezeichnung) {
		this.zusatzbezeichnung = zusatzbezeichnung;
	}

}

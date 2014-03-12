/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
package com.lp.util.report;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class PositionRpt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Double fVerpackungsmenge;
	String sPositionsartCNr;
	String sIdent;
	String sBezeichnung;
	String sZusatzbezeichnung;
	String sZusatzbezeichnung2;

	public String getSZusatzbezeichnung2() {
		return sZusatzbezeichnung2;
	}

	public void setSZusatzbezeichnung2(String sZusatzbezeichnung2) {
		this.sZusatzbezeichnung2 = sZusatzbezeichnung2;
	}

	BigDecimal bdMenge;
	BigDecimal bdMaterialzuschlag;
	public BigDecimal getBdMaterialzuschlag() {
		return bdMaterialzuschlag;
	}

	public void setBdMaterialzuschlag(BigDecimal bdMaterialzuschlag) {
		this.bdMaterialzuschlag = bdMaterialzuschlag;
	}

	String sEinheit;
	Timestamp tTermin;
	Double dRabatt;
	BigDecimal bdPreis;
	String sLieferantenArtikelnummer;
	String sLieferantenArtikelbezeichnung;
	String sText;
	byte[] oImage;
	String sTypCNr;

	public String getSTypCNr() {
		return sTypCNr;
	}

	public void setSTypCNr(String sTypCNr) {
		this.sTypCNr = sTypCNr;
	}

	public Double getFVerpackungsmenge() {
		return fVerpackungsmenge;
	}

	public void setFVerpackungsmenge(Double fVerpackungsmenge) {
		this.fVerpackungsmenge = fVerpackungsmenge;
	}

	/**
	 * Setzt die Positionsart der Position
	 * 
	 * @param sPositionsartCNr
	 *            Die zu setzende Positionsart
	 */
	public void setSPositionsartCNr(String sPositionsartCNr) {
		this.sPositionsartCNr = sPositionsartCNr;
	}

	/**
	 * 
	 * @return liefert die Positionsart
	 */
	public String getSPositionsartCNr() {
		return sPositionsartCNr;
	}

	/**
	 * Setzt den Ident der Position
	 * 
	 * @param sIdent
	 *            Der zu setzende Ident
	 */
	public void setSIdent(String sIdent) {
		this.sIdent = sIdent;
	}

	/**
	 * 
	 * @return liefert den Ident der Position
	 */
	public String getSIdent() {
		return sIdent;
	}

	public void setSBezeichnung(String sBezeichnung) {
		this.sBezeichnung = sBezeichnung;
	}

	public String getSBezeichnung() {
		return sBezeichnung;
	}

	public void setSZusatzbezeichnung(String sZusatzbezeichnung) {
		this.sZusatzbezeichnung = sZusatzbezeichnung;
	}

	public String getSZusatzbezeichnung() {
		return sZusatzbezeichnung;
	}

	public void setBdMenge(BigDecimal bdMenge) {
		this.bdMenge = bdMenge;
	}

	public BigDecimal getBdMenge() {
		return bdMenge;
	}

	public void setSEinheit(String sEinheit) {
		this.sEinheit = sEinheit;
	}

	public String getSEinheit() {
		return sEinheit;
	}

	public void setTTermin(Timestamp tTermin) {
		this.tTermin = tTermin;
	}

	public Timestamp getTTermin() {
		return tTermin;
	}

	public void setDRabatt(Double dRabatt) {
		this.dRabatt = dRabatt;
	}

	public Double getDRabatt() {
		return dRabatt;
	}

	public void setBdPreis(BigDecimal bdPreis) {
		this.bdPreis = bdPreis;
	}

	public BigDecimal getBdPreis() {
		return bdPreis;
	}

	public void setSLieferantenArtikelnummer(String sLieferantenArtikelnummer) {
		this.sLieferantenArtikelnummer = sLieferantenArtikelnummer;
	}

	public String getSLieferantenArtikelnummer() {
		return sLieferantenArtikelnummer;
	}

	public void setSLieferantenArtikelbezeichnung(
			String sLieferantenArtikelbezeichnung) {
		this.sLieferantenArtikelbezeichnung = sLieferantenArtikelbezeichnung;
	}

	public String getSLieferantenArtikelbezeichnung() {
		return sLieferantenArtikelbezeichnung;
	}

	public void setSText(String sText) {
		this.sText = sText;
	}

	public String getSText() {
		return sText;
	}

	public void setOImage(byte[] oImage) {
		this.oImage = oImage;
	}

	public byte[] getOImage() {
		return oImage;
	}
}

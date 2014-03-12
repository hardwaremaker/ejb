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
package com.lp.server.fertigung.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class LosMonatsauswertungDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String artikelnummer;
	private String artikelbezeichnung;
	private BigDecimal nSollzeit;
	private BigDecimal nIstZeit;
	private BigDecimal nLosgroesse;
	private Double fBewertung;
	public Double getFBewertung() {
		return fBewertung;
	}

	public void setFBewertung(Double fBewertung) {
		this.fBewertung = fBewertung;
	}

	private String sKunde;
	private String sLosnummer;
	public String getSLosnummer() {
		return sLosnummer;
	}

	public void setSLosnummer(String losnummer) {
		sLosnummer = losnummer;
	}

	private Integer kundeIId;
	private BigDecimal nAbweichung;
	private boolean bMonatslos=false;
	private String sFertigungsgruppe;

	public String getSFertigungsgruppe() {
		return sFertigungsgruppe;
	}

	public void setSFertigungsgruppe(String fertigungsgruppe) {
		sFertigungsgruppe = fertigungsgruppe;
	}

	public boolean isBMonatslos() {
		return bMonatslos;
	}

	public void setBMonatslos(boolean monatslos) {
		bMonatslos = monatslos;
	}

	public String getArtikelnummer() {
		return artikelnummer;
	}

	public String getArtikelbezeichnung() {
		return artikelbezeichnung;
	}

	public BigDecimal getNSollzeit() {
		return nSollzeit;
	}

	public BigDecimal getNIstZeit() {
		return nIstZeit;
	}

	public BigDecimal getNLosgroesse() {
		return nLosgroesse;
	}

	public String getSKunde() {
		return sKunde;
	}

	public Integer getKundeIId() {
		return kundeIId;
	}

	public BigDecimal getNAbweichung() {
		return nAbweichung;
	}

	public void setArtikelnummer(String artikelnummer) {
		this.artikelnummer = artikelnummer;
	}

	public void setArtikelbezeichnung(String artikelbezeichnung) {
		this.artikelbezeichnung = artikelbezeichnung;
	}

	public void setNSollzeit(BigDecimal nSollzeit) {
		this.nSollzeit = nSollzeit;
	}

	public void setNIstZeit(BigDecimal nIstZeit) {
		this.nIstZeit = nIstZeit;
	}

	public void setNLosgroesse(BigDecimal nLosgroesse) {
		this.nLosgroesse = nLosgroesse;
	}

	public void setSKunde(String sKunde) {
		this.sKunde = sKunde;
	}

	public void setKundeIId(Integer kundeIId) {
		this.kundeIId = kundeIId;
	}

	public void setNAbweichung(BigDecimal nAbweichung) {
		this.nAbweichung = nAbweichung;
	}

}

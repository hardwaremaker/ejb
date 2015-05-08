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
package com.lp.server.fertigung.service;

import java.math.BigDecimal;

/**
 * <p>
 * Diese Klasse kuemmert sich um ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Vorname Nachname; dd.mm.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: sebastian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2008/11/29 21:51:36 $
 */
public class ReportAufloesbareFehlmengenDto {
	private String sBelegnummer = null;
	private String sProjekt = null;
	private String sStuecklisteNummer = null;
	private String sStuecklisteBezeichnung = null;
	private String sStuecklisteZusatzBezeichnung = null;
	private String sArtikelNummer = null;
	private String sArtikelBezeichnung = null;
	private String sArtikelZusatzBezeichnung = null;
	private BigDecimal bdFehlmenge = null;
	private BigDecimal bdLagerstand = null;
	private java.util.Date dTermin = null;
	private String sEinheit = null;
	private BigDecimal bdBestellt;
	private BigDecimal bdReserviert;
	private BigDecimal bdOffen;
	private String sABNummer;
	private Integer iMahnstufe;
	private boolean bLos = true;
	private Integer iArtikelIId;
	
	public Integer getIArtikelIId(){
		return this.iArtikelIId;
	}
	
	public void setIArtikelIId(Integer iArtikelIId){
		this.iArtikelIId = iArtikelIId;
	}

	public BigDecimal getBdFehlmenge() {
		return bdFehlmenge;
	}

	public BigDecimal getBdLagerstand() {
		return bdLagerstand;
	}

	public java.util.Date getDTermin() {
		return dTermin;
	}

	public String getSArtikelBezeichnung() {
		return sArtikelBezeichnung;
	}

	public String getSArtikelZusatzBezeichnung() {
		return sArtikelZusatzBezeichnung;
	}

	public String getSArtikelNummer() {
		return sArtikelNummer;
	}

	public String getSEinheit() {
		return sEinheit;
	}

	public String getSBelegnummer() {
		return sBelegnummer;
	}

	public String getSProjekt() {
		return sProjekt;
	}

	public String getSStuecklisteBezeichnung() {
		return sStuecklisteBezeichnung;
	}

	public String getSStuecklisteZusatzBezeichnung() {
		return sStuecklisteZusatzBezeichnung;
	}

	public String getSStuecklisteNummer() {
		return sStuecklisteNummer;
	}

	public BigDecimal getBdBestellt() {
		return bdBestellt;
	}

	public BigDecimal getBdReserviert() {
		return bdReserviert;
	}

	public BigDecimal getBdOffen() {
		return bdOffen;
	}

	public String getSABNummer() {
		return sABNummer;
	}

	public Integer getIMahnstufe() {
		return iMahnstufe;
	}

	public boolean isBLos() {
		return bLos;
	}

	public void setBdFehlmenge(BigDecimal bdFehlmenge) {
		this.bdFehlmenge = bdFehlmenge;
	}

	public void setBdLagerstand(BigDecimal bdLagerstand) {
		this.bdLagerstand = bdLagerstand;
	}

	public void setDTermin(java.util.Date dTermin) {
		this.dTermin = dTermin;
	}

	public void setSArtikelBezeichnung(String sArtikelBezeichnung) {
		this.sArtikelBezeichnung = sArtikelBezeichnung;
	}

	public void setSArtikelZusatzBezeichnung(String sArtikelZusatzBezeichnung) {
		this.sArtikelZusatzBezeichnung = sArtikelZusatzBezeichnung;
	}

	public void setSArtikelNummer(String sArtikelNummer) {
		this.sArtikelNummer = sArtikelNummer;
	}

	public void setSBelegnummer(String sBelegnummer) {
		this.sBelegnummer = sBelegnummer;
	}

	public void setSEinheit(String sEinheit) {
		this.sEinheit = sEinheit;
	}

	public void setSProjekt(String sProjekt) {
		this.sProjekt = sProjekt;
	}

	public void setSStuecklisteBezeichnung(String sStuecklisteBezeichnung) {
		this.sStuecklisteBezeichnung = sStuecklisteBezeichnung;
	}

	public void setSStuecklisteZusatzBezeichnung(
			String sStuecklisteZusatzBezeichnung) {
		this.sStuecklisteZusatzBezeichnung = sStuecklisteZusatzBezeichnung;
	}

	public void setSStuecklisteNummer(String sStuecklisteNummer) {
		this.sStuecklisteNummer = sStuecklisteNummer;
	}

	public void setBdBestellt(BigDecimal bdBestellt) {
		this.bdBestellt = bdBestellt;
	}

	public void setBdReserviert(BigDecimal bdReserviert) {
		this.bdReserviert = bdReserviert;
	}

	public void setBdOffen(BigDecimal bdOffen) {
		this.bdOffen = bdOffen;
	}

	public void setSABNummer(String sABNummer) {
		this.sABNummer = sABNummer;
	}

	public void setIMahnstufe(Integer iMahnstufe) {
		this.iMahnstufe = iMahnstufe;
	}

	public void setBLos(boolean bLos) {
		this.bLos = bLos;
	}

}

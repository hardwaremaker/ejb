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

import java.math.BigDecimal;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;

/**
 * <p>
 * Diese Klasse kuemmert sich um die Zwischenspeicherung der
 * Verfuegbarkeitsdaten eines Auftrags.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur GmbH (c) 2004 - 2007
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 24.07.07
 * </p>
 * 
 * <p>
 * 
 * @author $Author: Gerold $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/09/07 12:51:40 $
 */
public class ReportAuftragVerfuegbarkeitDto {
	private ArtikelDto artikelDto = null;
	private String einheitCNr = null;
	private String mandantCNr = null;
	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	private BigDecimal bdMenge = null;
	
	private BigDecimal bdMenge2 = null;
	private BigDecimal bdMenge3 = null;
	private BigDecimal bdMenge4 = null;
	private BigDecimal bdMenge5 = null;
	private BigDecimal bdMenge6 = null;
	private BigDecimal bdMenge7 = null;
	private BigDecimal bdMenge8 = null;
	private BigDecimal bdMenge9 = null;
	private BigDecimal bdMenge10 = null;
	private BigDecimal bdMenge11 = null;
	private BigDecimal bdMenge12 = null;
	private BigDecimal bdMenge13 = null;
	
	
	private BigDecimal bdNachfolgendeProduktionsdauer = null;
	private BigDecimal bdMaximaleWBZDerEbene = null;
	private BigDecimal bdDauerBisFertigstellungDerEbene = null;
	private java.util.Date fruestmoeglicherLiefertermin =null;
	
	public BigDecimal getBdDauerBisFertigstellungDerEbene() {
		return bdDauerBisFertigstellungDerEbene;
	}

	public void setBdDauerBisFertigstellungDerEbene(
			BigDecimal bdDauerBisFertigstellungDerEbene) {
		this.bdDauerBisFertigstellungDerEbene = bdDauerBisFertigstellungDerEbene;
	}

	public BigDecimal getBdMaximaleWBZDerEbene() {
		return bdMaximaleWBZDerEbene;
	}

	public void setBdMaximaleWBZDerEbene(BigDecimal bdMaximaleWBZDerEbene) {
		this.bdMaximaleWBZDerEbene = bdMaximaleWBZDerEbene;
	}

	public BigDecimal getBdNachfolgendeProduktionsdauer() {
		return bdNachfolgendeProduktionsdauer;
	}

	public void setBdNachfolgendeProduktionsdauer(
			BigDecimal bdNachfolgendeProduktionsdauer) {
		this.bdNachfolgendeProduktionsdauer = bdNachfolgendeProduktionsdauer;
	}


	private String artikelsetType = null ;
	
	public BigDecimal getBdMenge2() {
		return bdMenge2;
	}

	public void setBdMenge2(BigDecimal bdMenge2) {
		this.bdMenge2 = bdMenge2;
	}

	public BigDecimal getBdMenge3() {
		return bdMenge3;
	}

	public void setBdMenge3(BigDecimal bdMenge3) {
		this.bdMenge3 = bdMenge3;
	}

	public BigDecimal getBdMenge4() {
		return bdMenge4;
	}

	public void setBdMenge4(BigDecimal bdMenge4) {
		this.bdMenge4 = bdMenge4;
	}

	public BigDecimal getBdMenge5() {
		return bdMenge5;
	}

	public void setBdMenge5(BigDecimal bdMenge5) {
		this.bdMenge5 = bdMenge5;
	}

	public BigDecimal getBdMenge6() {
		return bdMenge6;
	}

	public void setBdMenge6(BigDecimal bdMenge6) {
		this.bdMenge6 = bdMenge6;
	}

	public BigDecimal getBdMenge7() {
		return bdMenge7;
	}

	public void setBdMenge7(BigDecimal bdMenge7) {
		this.bdMenge7 = bdMenge7;
	}

	public BigDecimal getBdMenge8() {
		return bdMenge8;
	}

	public void setBdMenge8(BigDecimal bdMenge8) {
		this.bdMenge8 = bdMenge8;
	}

	public BigDecimal getBdMenge9() {
		return bdMenge9;
	}

	public void setBdMenge9(BigDecimal bdMenge9) {
		this.bdMenge9 = bdMenge9;
	}

	public BigDecimal getBdMenge10() {
		return bdMenge10;
	}

	public void setBdMenge10(BigDecimal bdMenge10) {
		this.bdMenge10 = bdMenge10;
	}

	public BigDecimal getBdMenge11() {
		return bdMenge11;
	}

	public void setBdMenge11(BigDecimal bdMenge11) {
		this.bdMenge11 = bdMenge11;
	}

	public BigDecimal getBdMenge12() {
		return bdMenge12;
	}

	public void setBdMenge12(BigDecimal bdMenge12) {
		this.bdMenge12 = bdMenge12;
	}

	public BigDecimal getBdMenge13() {
		return bdMenge13;
	}

	public void setBdMenge13(BigDecimal bdMenge13) {
		this.bdMenge13 = bdMenge13;
	}

	private BigDecimal bdBedarf = null;
	private ArtikellieferantDto artikellieferantDto = null;;
	private BigDecimal bdDefaultdurchlaufzeit = null;
	private boolean bLagernd = false;
	private Integer belegpositionIId = null;
	private Integer iEbene=null;
	private BigDecimal bdWBZInTagenAusArtikellieferant = null;
	private BigDecimal bdLagerstand = null;
	

	public BigDecimal getBdLagerstand() {
		return bdLagerstand;
	}

	public void setBdLagerstand(BigDecimal bdLagerstand) {
		this.bdLagerstand = bdLagerstand;
	}

	public BigDecimal getBdWBZInTagenAusArtikellieferant() {
		return bdWBZInTagenAusArtikellieferant;
	}

	public void setBdWBZInTagenAusArtikellieferant(
			BigDecimal bdWBZInTagenAusArtikellieferant) {
		this.bdWBZInTagenAusArtikellieferant = bdWBZInTagenAusArtikellieferant;
	}

	public Integer getIEbene() {
		return iEbene;
	}

	public void setIEbene(Integer iEbene) {
		this.iEbene = iEbene;
	}

	public BigDecimal getBdMenge() {
		return bdMenge;
	}

	public BigDecimal getBdBedarf() {
		return bdBedarf;
	}

	public ArtikelDto getArtikelDto() {
		return artikelDto;
	}

	public ArtikellieferantDto getArtikellieferantDto() {
		return artikellieferantDto;
	}

	public void setBdMenge(BigDecimal bdMenge) {
		this.bdMenge = bdMenge;
	}

	public void setBdBedarf(BigDecimal bdBedarf) {
		this.bdBedarf = bdBedarf;
	}

	public void setArtikelDto(ArtikelDto artikelDto) {
		this.artikelDto = artikelDto;
	}

	public void setArtikellieferantDto(ArtikellieferantDto artikellieferantDto) {
		this.artikellieferantDto = artikellieferantDto;
	}

	public String getEinheitCNr() {
		return einheitCNr;
	}

	public void setEinheitCNr(String einheitCNr) {
		this.einheitCNr = einheitCNr;
	}

	public BigDecimal getBdDefaultdurchlaufzeit() {
		return bdDefaultdurchlaufzeit;
	}

	public void setBdDefaultdurchlaufzeit(BigDecimal bdDefaultdurchlaufzeit) {
		this.bdDefaultdurchlaufzeit = bdDefaultdurchlaufzeit;
	}

	public void setBLagernd(boolean bLagernd) {
		this.bLagernd = bLagernd;
	}

	public boolean isBLagernd() {
		return bLagernd;
	}

	public void setBelegpositionIId(Integer belegpositionIId) {
		this.belegpositionIId = belegpositionIId;
	}

	public Integer isBelegpositionIId() {
		return belegpositionIId;
	}

	public String getArtikelsetType() {
		return artikelsetType;
	}

	public void setArtikelsetType(String artikelsetType) {
		this.artikelsetType = artikelsetType;
	}

	public java.util.Date getDFruestmoeglicherLiefertermin() {
		return fruestmoeglicherLiefertermin;
	}

	public void setDFruestmoeglicherLiefertermin(
			java.util.Date fruestmoeglicherLiefertermin) {
		this.fruestmoeglicherLiefertermin = fruestmoeglicherLiefertermin;
	}
}

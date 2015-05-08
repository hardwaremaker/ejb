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

import java.awt.Image;
import java.io.Serializable;
import java.math.BigDecimal;

public class ReportLosAusgabelisteDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getSSnrChnr() {
		return sSnrChnr;
	}

	public void setSSnrChnr(String snrChnr) {
		sSnrChnr = snrChnr;
	}
	private BigDecimal nInFertigung = null;
	
	public BigDecimal getNInFertigung() {
		return nInFertigung;
	}

	public void setNInFertigung(BigDecimal nInFertigung) {
		this.nInFertigung = nInFertigung;
	}
	private BigDecimal nLagerstandSperrlager = null;
	public BigDecimal getNLagerstandSperrlager() {
		return nLagerstandSperrlager;
	}

	public void setNLagerstandSperrlager(BigDecimal lagerstandSperrlager) {
		nLagerstandSperrlager = lagerstandSperrlager;
	}
	private BigDecimal nLagerstand = null;
	public BigDecimal getNLagerstand() {
		return nLagerstand;
	}

	public void setNLagerstand(BigDecimal lagerstand) {
		nLagerstand = lagerstand;
	}

	private Boolean bNurZurInfo = null;
	
	public Boolean getBNurZurInfo() {
		return bNurZurInfo;
	}

	public void setBNurZurInfo(Boolean bNurZurInfo) {
		this.bNurZurInfo = bNurZurInfo;
	}
	private String sIdent = null;
	private BigDecimal nMenge = null;
	private String sEinheit = null;
	private BigDecimal nAusgabe = null;
	private String sBezeichnung = null;
	private String sZusatzBezeichnung = null;
	private String sLager = null;
	private String sZusatzBezeichnung2;
	private String sFarbcode="";
	private String sLagerort = null;
	private String sMontageart = null;
	private Integer iSchale = null;
	private String sArtikelklasse = "";
	private String sMaterial = "";
	private Double dHoehe;
	private Double dBreite;
	private Double dTiefe;
	private String sBauform;
	private String sVerpackungsart;
	private Double dGewichtkg;
	private Double dRasterstehend;
	private BigDecimal bdAnzahlBestellt;
	private String sKommentarStueckliste = null;
	private String sPositionStueckliste = null;
	public String getSPositionStueckliste() {
		return sPositionStueckliste;
	}

	public void setSPositionStueckliste(String sPositionStueckliste) {
		this.sPositionStueckliste = sPositionStueckliste;
	}
	private String sSnrChnr = null;
	private String sKommentar = null;
	private Image artikelbild=null;
	
	private String sIndex = null;
	public String getSIndex() {
		return sIndex;
	}

	public void setSIndex(String index) {
		sIndex = index;
	}

	public String getSRevision() {
		return sRevision;
	}

	public void setSRevision(String revision) {
		sRevision = revision;
	}
	private String sRevision = null;

	public String getSKommentar() {
		return sKommentar;
	}

	public void setSKommentar(String kommentar) {
		sKommentar = kommentar;
	}

	public Image getArtikelbild() {
		return artikelbild;
	}

	public void setArtikelbild(Image artikelbild) {
		this.artikelbild = artikelbild;
	}

	public Integer getISchale() {
		return iSchale;
	}

	public BigDecimal getNAusgabe() {
		return nAusgabe;
	}

	public BigDecimal getNMenge() {
		return nMenge;
	}

	public String getSArtikelklasse() {
		return sArtikelklasse;
	}

	public String getSBezeichnung() {
		return sBezeichnung;
	}

	public String getSZusatzBezeichnung() {
		return sZusatzBezeichnung;
	}

	public String getSEinheit() {
		return sEinheit;
	}

	public String getSIdent() {
		return sIdent;
	}

	public String getSLager() {
		return sLager;
	}

	public String getSLagerort() {
		return sLagerort;
	}

	public String getSMontageart() {
		return sMontageart;
	}

	public String getSFarbcode() {
		return sFarbcode;
	}

	public String getSZusatzBezeichnung2() {
		return sZusatzBezeichnung2;
	}

	public String getSMaterial() {
		return sMaterial;
	}

	public Double getDHoehe() {
		return dHoehe;
	}

	public Double getDTiefe() {
		return dTiefe;
	}

	public Double getDBreite() {
		return dBreite;
	}

	public String getSVerpackungsart() {
		return sVerpackungsart;
	}

	public String getSBauform() {
		return sBauform;
	}

	public Double getDGewichtkg() {
		return dGewichtkg;
	}

	public Double getDRasterstehend() {
		return dRasterstehend;
	}

	public BigDecimal getBdAnzahlBestellt() {
		return bdAnzahlBestellt;
	}

	public String getSKommentarStueckliste() {
		return sKommentarStueckliste;
	}

	public void setISchale(Integer iSchale) {
		this.iSchale = iSchale;
	}

	public void setNAusgabe(BigDecimal nAusgabe) {
		this.nAusgabe = nAusgabe;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public void setSArtikelklasse(String sArtikelklasse) {
		this.sArtikelklasse = sArtikelklasse;
	}

	public void setSBezeichnung(String sBezeichnung) {
		this.sBezeichnung = sBezeichnung;
	}

	public void setSZusatzBezeichnung(String sZusatzBezeichnung) {
		this.sZusatzBezeichnung = sZusatzBezeichnung;
	}

	public void setSEinheit(String sEinheit) {
		this.sEinheit = sEinheit;
	}

	public void setSIdent(String sIdent) {
		this.sIdent = sIdent;
	}

	public void setSLager(String sLager) {
		this.sLager = sLager;
	}

	public void setSLagerort(String sLagerort) {
		this.sLagerort = sLagerort;
	}

	public void setSMontageart(String sMontageart) {
		this.sMontageart = sMontageart;
	}

	public void setSFarbcode(String sFarbcode) {
		this.sFarbcode = sFarbcode;
	}

	public void setSZusatzBezeichnung2(String sZusatzBezeichnung2) {
		this.sZusatzBezeichnung2 = sZusatzBezeichnung2;
	}

	public void setSMaterial(String sMaterial) {
		this.sMaterial = sMaterial;
	}

	public void setDHoehe(Double dHoehe) {
		this.dHoehe = dHoehe;
	}

	public void setDTiefe(Double dTiefe) {
		this.dTiefe = dTiefe;
	}

	public void setDBreite(Double dBreite) {
		this.dBreite = dBreite;
	}

	public void setSVerpackungsart(String cVerpackungsart) {
		this.sVerpackungsart = cVerpackungsart;
	}

	public void setSBauform(String cBauform) {
		this.sBauform = cBauform;
	}

	public void setDGewichtkg(Double dGewichtkg) {
		this.dGewichtkg = dGewichtkg;
	}

	public void setDRasterstehend(Double dRasterstehend) {
		this.dRasterstehend = dRasterstehend;
	}

	public void setBdAnzahlBestellt(BigDecimal bdAnzahlBestellt) {
		this.bdAnzahlBestellt = bdAnzahlBestellt;
	}

	public void setSKommentarStueckliste(String sKommentarStueckliste) {
		this.sKommentarStueckliste = sKommentarStueckliste;
	}
}

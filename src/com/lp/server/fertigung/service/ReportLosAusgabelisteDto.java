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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ReportLosAusgabelisteDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sKommentarMaterial;
	private Double fDimension1Material;
	private Double fDimension2Material;
	private Double fDimension3Material;
	private String sPositionMaterial;
	
	private String kurzbezeichnung;
	private String referenznummer;
	
	// cNr des zugehoerigen Loses, wenn verdichtet nach Ident, dann eventuell
	// mehrere Losnummern, getrennt mit ,
	private String losnummer;
	private List<Integer> losSollmaterialIIds;
	
	public String getKurzbezeichnung() {
		return kurzbezeichnung;
	}

	public void setKurzbezeichnung(String kurzbezeichnung) {
		this.kurzbezeichnung = kurzbezeichnung;
	}

	public String getReferenznummer() {
		return referenznummer;
	}

	public void setReferenznummer(String referenznummer) {
		this.referenznummer = referenznummer;
	}

	
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
	private String sStandort = null;
	public String getSStandort() {
		return sStandort;
	}

	public void setSStandort(String sStandort) {
		this.sStandort = sStandort;
	}
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
	private String sSortAusStueckliste = null;
	public String getSSortAusStueckliste() {
		return sSortAusStueckliste;
	}

	private BigDecimal bdFehlmenge=BigDecimal.ZERO;
	
	public BigDecimal getBdFehlmenge() {
		return bdFehlmenge;
	}

	public void setBdFehlmenge(BigDecimal bdFehlmenge) {
		this.bdFehlmenge = bdFehlmenge;
	}

	public void setSSortAusStueckliste(String sSortAusStueckliste) {
		this.sSortAusStueckliste = sSortAusStueckliste;
	}
	
	
	private String einheitStueckliste = null;
	private BigDecimal sollmengeStueckliste = null;
	
	public String getEinheitStueckliste() {
		return einheitStueckliste;
	}

	public void setEinheitStueckliste(String einheitStueckliste) {
		this.einheitStueckliste = einheitStueckliste;
	}

	public BigDecimal getSollmengeStueckliste() {
		return sollmengeStueckliste;
	}

	public void setSollmengeStueckliste(BigDecimal sollmengeStueckliste) {
		this.sollmengeStueckliste = sollmengeStueckliste;
	}
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
	
	public String getSKommentarMaterial() {
		return sKommentarMaterial;
	}
	
	public void setSKommentarMaterial(String sKommentarMaterial) {
		this.sKommentarMaterial = sKommentarMaterial;
	}
	
	public String getSPositionMaterial() {
		return sPositionMaterial;
	}
	
	public void setSPositionMaterial(String sPositionMaterial) {
		this.sPositionMaterial = sPositionMaterial;
	}
	
	public Double getFDimension1Material() {
		return fDimension1Material;
	}
	
	public void setFDimension1Material(Double fDimension1Material) {
		this.fDimension1Material = fDimension1Material;
	}

	public Double getFDimension2Material() {
		return fDimension2Material;
	}
	
	public void setFDimension2Material(Double fDimension2Material) {
		this.fDimension2Material = fDimension2Material;
	}

	public Double getFDimension3Material() {
		return fDimension3Material;
	}
	
	public void setFDimension3Material(Double fDimension3Material) {
		this.fDimension3Material = fDimension3Material;
	}
	
	public void setLosnummer(String losnummer) {
		this.losnummer = losnummer;
	}
	
	public void addLosnummer(String losnummer) {
		if(this.losnummer == null) {
			this.losnummer = losnummer;
		} else if(losnummer != null) {
			this.losnummer += ", " + losnummer;
		}
	}
	
	public String getLosnummer() {
		return losnummer;
	}
	
	public void addLosSollmaterialIId(Integer sollMatIId) {
		if(this.losSollmaterialIIds == null) {
			this.losSollmaterialIIds = new ArrayList<Integer>();
		}
		this.losSollmaterialIIds.add(sollMatIId);
	}

	public void addLosSollmaterialIIds(Collection<Integer> sollMatIIds) {
		this.losSollmaterialIIds.addAll(sollMatIIds);
	}
	
	public List<Integer> getLosSollmaterialIIds() {
		return losSollmaterialIIds;
	}
	
	
	public BigDecimal getBdLosLosgroesse() {
		return bdLosLosgroesse;
	}

	public void setBdLosLosgroesse(BigDecimal bdLosLosgroesse) {
		this.bdLosLosgroesse = bdLosLosgroesse;
	}

	public BigDecimal getBdLosStuecklisteErfassungsfaktor() {
		return bdLosStuecklisteErfassungsfaktor;
	}

	public void setBdLosStuecklisteErfassungsfaktor(BigDecimal bdLosStuecklisteErfassungsfaktor) {
		this.bdLosStuecklisteErfassungsfaktor = bdLosStuecklisteErfassungsfaktor;
	}

	public Timestamp gettLosAngelegt() {
		return tLosAngelegt;
	}

	public void settLosAngelegt(Timestamp tLosAngelegt) {
		this.tLosAngelegt = tLosAngelegt;
	}

	public java.sql.Date getdLosProduktionsbeginn() {
		return dLosProduktionsbeginn;
	}

	public void setdLosProduktionsbeginn(java.sql.Date dLosProduktionsbeginn) {
		this.dLosProduktionsbeginn = dLosProduktionsbeginn;
	}

	public java.sql.Date getdLosProduktionsende() {
		return dLosProduktionsende;
	}

	public void setdLosProduktionsende(java.sql.Date dLosProduktionsende) {
		this.dLosProduktionsende = dLosProduktionsende;
	}

	public String getsLosLosnummer() {
		return sLosLosnummer;
	}

	public void setsLosLosnummer(String sLosLosnummer) {
		this.sLosLosnummer = sLosLosnummer;
	}

	public String getsLosAuftragsnummer() {
		return sLosAuftragsnummer;
	}

	public void setsLosAuftragsnummer(String sLosAuftragsnummer) {
		this.sLosAuftragsnummer = sLosAuftragsnummer;
	}

	public String getsLosKostenstellenummer() {
		return sLosKostenstellenummer;
	}

	public void setsLosKostenstellenummer(String sLosKostenstellenummer) {
		this.sLosKostenstellenummer = sLosKostenstellenummer;
	}

	public String getsLosKunde() {
		return sLosKunde;
	}

	public void setsLosKunde(String sLosKunde) {
		this.sLosKunde = sLosKunde;
	}

	public String getsLosStuecklistenummer() {
		return sLosStuecklistenummer;
	}

	public void setsLosStuecklistenummer(String sLosStuecklistenummer) {
		this.sLosStuecklistenummer = sLosStuecklistenummer;
	}

	public String getsLosStuecklistebezeichnung() {
		return sLosStuecklistebezeichnung;
	}

	public void setsLosStuecklistebezeichnung(String sLosStuecklistebezeichnung) {
		this.sLosStuecklistebezeichnung = sLosStuecklistebezeichnung;
	}

	public String getsLosStuecklistezusatzbezeichnung() {
		return sLosStuecklistezusatzbezeichnung;
	}

	public void setsLosStuecklistezusatzbezeichnung(String sLosStuecklistezusatzbezeichnung) {
		this.sLosStuecklistezusatzbezeichnung = sLosStuecklistezusatzbezeichnung;
	}
	private BigDecimal bdLosLosgroesse;
	private BigDecimal bdLosStuecklisteErfassungsfaktor;
	
	
	private Timestamp tLosAngelegt;
	private java.sql.Date dLosProduktionsbeginn;
	private java.sql.Date dLosProduktionsende;
	
	private String sLosLosnummer;
	private String sLosAuftragsnummer;
	private String sLosKostenstellenummer;
	private String sLosKunde;
	private String sLosStuecklistenummer;
	private String sLosStuecklistebezeichnung;
	private String sLosStuecklistezusatzbezeichnung;
	
	
	
	
	
	
	
}

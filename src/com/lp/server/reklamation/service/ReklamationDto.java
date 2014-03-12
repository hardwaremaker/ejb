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
package com.lp.server.reklamation.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;

public class ReklamationDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String mandantCNr;
	private String cNr;
	private String reklamationartCNr;
	private Timestamp tBelegdatum;
	private Integer kostenstelleIId;
	private Integer fehlerangabeIId;
	private Integer aufnahmeartIId;
	private Integer personalIIdAufnehmer;
	private Short bArtikel;
	private Integer artikelIId;
	private String cHandartikel;
	private BigDecimal nMenge;
	private Short bFremdprodukt;
	private String cGrund;
	private Integer losIId;
	private Integer bestellungIId;
	private Integer lieferscheinIId;
	private Integer rechnungIId;
	private Integer kundeIId;
	private Integer lieferantIId;
	private Integer ansprechpartnerIId;
	private String cProjekt;
	private String cTelansprechpartner;
	private String xAnalyse;
	private String xKommentar;
	private Integer fehlerIId;
	private Short bBerechtigt;
	private Integer personalIIdRuecksprache;
	private Timestamp tRuecksprache;
	private String cRuecksprachemit;
	private BigDecimal nKostenmaterial;
	private BigDecimal nKostenarbeitszeit;
	private Integer massnahmeIIdKurz;
	private Timestamp tMassnahmebiskurz;
	private Timestamp tEingefuehrtkurz;
	private Integer personalIIdEingefuehrtkurz;
	private Integer massnahmeIIdMittel;
	private Timestamp tMassnahmebismittel;
	private Timestamp tEingefuehrtmittel;
	private Integer personalIIdEingefuehrtmittel;
	private Integer massnahmeIIdLang;
	private Timestamp tMassnahmebislang;
	private Timestamp tEingefuehrtlang;
	private Integer personalIIdEingefuehrtlang;
	private Integer personalIIdAnlegen;
	private Timestamp tAnlegen;
	private Integer personalIIdAendern;
	private Timestamp tAendern;
	private Integer personalIIdErledigt;
	private Timestamp tErledigt;
	private Integer schwereIId;
	private Integer behandlungIId;
	private Integer wareneingangIId;
	private String statusCNr;
	private Integer personalIIdVerursacher;
	private Integer lossollarbeitsplanIId;
	private Integer maschineIId;
	private String cKdreklanr;
	private String cKdlsnr;

	private Integer ansprechpartnerIIdLieferant;

	public Integer getAnsprechpartnerIIdLieferant() {
		return ansprechpartnerIIdLieferant;
	}

	public void setAnsprechpartnerIIdLieferant(
			Integer ansprechpartnerIIdLieferant) {
		this.ansprechpartnerIIdLieferant = ansprechpartnerIIdLieferant;
	}

	private String cTelansprechpartnerLieferant;

	public String getCTelansprechpartnerLieferant() {
		return cTelansprechpartnerLieferant;
	}

	public void setCTelansprechpartnerLieferant(
			String cTelansprechpartnerLieferant) {
		this.cTelansprechpartnerLieferant = cTelansprechpartnerLieferant;
	}

	private Integer iKundeunterart;

	public Integer getIKundeunterart() {
		return iKundeunterart;
	}

	public void setIKundeunterart(Integer iKundeunterart) {
		this.iKundeunterart = iKundeunterart;
	}

	private String cSeriennrchargennr;

	public String getCSeriennrchargennr() {
		return this.cSeriennrchargennr;
	}

	public void setCSeriennrchargennr(String cSeriennrchargennr) {
		this.cSeriennrchargennr = cSeriennrchargennr;
	}

	public Integer getLossollarbeitsplanIId() {
		return lossollarbeitsplanIId;
	}

	public void setLossollarbeitsplanIId(Integer lossollarbeitsplanIId) {
		this.lossollarbeitsplanIId = lossollarbeitsplanIId;
	}

	public Integer getPersonalIIdVerursacher() {
		return personalIIdVerursacher;
	}

	public void setPersonalIIdVerursacher(Integer personalIIdVerursacher) {
		this.personalIIdVerursacher = personalIIdVerursacher;
	}

	public Integer getMaschineIId() {
		return maschineIId;
	}

	public void setMaschineIId(Integer maschineIId) {
		this.maschineIId = maschineIId;
	}

	public String getCKdreklanr() {
		return cKdreklanr;
	}

	public void setCKdreklanr(String cKdreklanr) {
		this.cKdreklanr = cKdreklanr;
	}

	public String getCKdlsnr() {
		return cKdlsnr;
	}

	public void setCKdlsnr(String cKdlsnr) {
		this.cKdlsnr = cKdlsnr;
	}

	public String getStatusCNr() {
		return statusCNr;
	}

	public void setStatusCNr(String statusCNr) {
		this.statusCNr = statusCNr;
	}

	private Integer personalIIdWirksamkeit;
	private Timestamp tWirksamkeitbis;
	private Timestamp tWirksamkeiteingefuehrt;

	public Integer getPersonalIIdWirksamkeit() {
		return personalIIdWirksamkeit;
	}

	public void setPersonalIIdWirksamkeit(Integer personalIIdWirksamkeit) {
		this.personalIIdWirksamkeit = personalIIdWirksamkeit;
	}

	public Timestamp getTWirksamkeitbis() {
		return tWirksamkeitbis;
	}

	public void setTWirksamkeitbis(Timestamp wirksamkeitbis) {
		tWirksamkeitbis = wirksamkeitbis;
	}

	public Timestamp getTWirksamkeiteingefuehrt() {
		return tWirksamkeiteingefuehrt;
	}

	public void setTWirksamkeiteingefuehrt(Timestamp wirksamkeiteingefuehrt) {
		tWirksamkeiteingefuehrt = wirksamkeiteingefuehrt;
	}

	public String getXGrundLang() {
		return xGrundLang;
	}

	public void setXGrundLang(String grundLang) {
		xGrundLang = grundLang;
	}

	private String xGrundLang;

	public Integer getWirksamkeitIId() {
		return wirksamkeitIId;
	}

	public void setWirksamkeitIId(Integer wirksamkeitIId) {
		this.wirksamkeitIId = wirksamkeitIId;
	}

	public String getXWirksamkeit() {
		return xWirksamkeit;
	}

	public void setXWirksamkeit(String wirksamkeit) {
		xWirksamkeit = wirksamkeit;
	}

	public String getXMassnahmeLang() {
		return xMassnahmeLang;
	}

	public void setXMassnahmeLang(String massnahmeLang) {
		xMassnahmeLang = massnahmeLang;
	}

	public String getXMassnahmeKurz() {
		return xMassnahmeKurz;
	}

	public void setXMassnahmeKurz(String massnahmeKurz) {
		xMassnahmeKurz = massnahmeKurz;
	}

	public String getXMassnahmeMittel() {
		return xMassnahmeMittel;
	}

	public void setXMassnahmeMittel(String massnahmeMittel) {
		xMassnahmeMittel = massnahmeMittel;
	}

	public Short getBBetrifftlagerstand() {
		return bBetrifftlagerstand;
	}

	public void setBBetrifftlagerstand(Short betrifftlagerstand) {
		bBetrifftlagerstand = betrifftlagerstand;
	}

	public Short getBBetrifftgelieferte() {
		return bBetrifftgelieferte;
	}

	public void setBBetrifftgelieferte(Short betrifftgelieferte) {
		bBetrifftgelieferte = betrifftgelieferte;
	}

	public BigDecimal getNStuecklagerstand() {
		return nStuecklagerstand;
	}

	public void setNStuecklagerstand(BigDecimal stuecklagerstand) {
		nStuecklagerstand = stuecklagerstand;
	}

	public BigDecimal getNStueckgelieferte() {
		return nStueckgelieferte;
	}

	public void setNStueckgelieferte(BigDecimal stueckgelieferte) {
		nStueckgelieferte = stueckgelieferte;
	}

	private Integer wirksamkeitIId;
	private String xWirksamkeit;
	private String xMassnahmeLang;
	private String xMassnahmeKurz;
	private String xMassnahmeMittel;
	private Short bBetrifftlagerstand;
	private Short bBetrifftgelieferte;
	private BigDecimal nStuecklagerstand;
	private BigDecimal nStueckgelieferte;

	public Integer getWareneingangIId() {
		return wareneingangIId;
	}

	public void setWareneingangIId(Integer wareneingangIId) {
		this.wareneingangIId = wareneingangIId;
	}

	public Integer getBehandlungIId() {
		return behandlungIId;
	}

	public void setBehandlungIId(Integer behandlungIId) {
		this.behandlungIId = behandlungIId;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public String getReklamationartCNr() {
		return reklamationartCNr;
	}

	public void setReklamationartCNr(String reklamationartCNr) {
		this.reklamationartCNr = reklamationartCNr;
	}

	public Timestamp getTBelegdatum() {
		return tBelegdatum;
	}

	public void setTBelegdatum(Timestamp tBelegdatum) {
		this.tBelegdatum = tBelegdatum;
	}

	public Integer getKostenstelleIId() {
		return kostenstelleIId;
	}

	public void setKostenstelleIId(Integer kostenstelleIId) {
		this.kostenstelleIId = kostenstelleIId;
	}

	public Integer getFehlerangabeIId() {
		return fehlerangabeIId;
	}

	public void setFehlerangabeIId(Integer fehlerangabeIId) {
		this.fehlerangabeIId = fehlerangabeIId;
	}

	public Integer getAufnahmeartIId() {
		return aufnahmeartIId;
	}

	public void setAufnahmeartIId(Integer aufnahmeartIId) {
		this.aufnahmeartIId = aufnahmeartIId;
	}

	public Integer getPersonalIIdAufnehmer() {
		return personalIIdAufnehmer;
	}

	public void setPersonalIIdAufnehmer(Integer personalIIdAufnehmer) {
		this.personalIIdAufnehmer = personalIIdAufnehmer;
	}

	public Short getBArtikel() {
		return bArtikel;
	}

	public void setBArtikel(Short bArtikel) {
		this.bArtikel = bArtikel;
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public String getCHandartikel() {
		return cHandartikel;
	}

	public void setCHandartikel(String cHandartikel) {
		this.cHandartikel = cHandartikel;
	}

	public BigDecimal getNMenge() {
		return nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public Short getBFremdprodukt() {
		return bFremdprodukt;
	}

	public void setBFremdprodukt(Short bFremdprodukt) {
		this.bFremdprodukt = bFremdprodukt;
	}

	public String getCGrund() {
		return cGrund;
	}

	public void setCGrund(String cGrund) {
		this.cGrund = cGrund;
	}

	public Integer getLosIId() {
		return losIId;
	}

	public void setLosIId(Integer losIId) {
		this.losIId = losIId;
	}

	public Integer getBestellungIId() {
		return bestellungIId;
	}

	public void setBestellungIId(Integer bestellungIId) {
		this.bestellungIId = bestellungIId;
	}

	public Integer getLieferscheinIId() {
		return lieferscheinIId;
	}

	public void setLieferscheinIId(Integer lieferscheinIId) {
		this.lieferscheinIId = lieferscheinIId;
	}

	public Integer getRechnungIId() {
		return rechnungIId;
	}

	public void setRechnungIId(Integer rechnungIId) {
		this.rechnungIId = rechnungIId;
	}

	public Integer getKundeIId() {
		return kundeIId;
	}

	public void setKundeIId(Integer kundeIId) {
		this.kundeIId = kundeIId;
	}

	public Integer getLieferantIId() {
		return lieferantIId;
	}

	public void setLieferantIId(Integer lieferantIId) {
		this.lieferantIId = lieferantIId;
	}

	public Integer getAnsprechpartnerIId() {
		return ansprechpartnerIId;
	}

	public void setAnsprechpartnerIId(Integer ansprechpartnerIId) {
		this.ansprechpartnerIId = ansprechpartnerIId;
	}

	public String getCProjekt() {
		return cProjekt;
	}

	public void setCProjekt(String cProjekt) {
		this.cProjekt = cProjekt;
	}

	public String getCTelansprechpartner() {
		return cTelansprechpartner;
	}

	public void setCTelansprechpartner(String cTelansprechpartner) {
		this.cTelansprechpartner = cTelansprechpartner;
	}

	public String getXAnalyse() {
		return xAnalyse;
	}

	public void setXAnalyse(String xAnalyse) {
		this.xAnalyse = xAnalyse;
	}

	public String getXKommentar() {
		return xKommentar;
	}

	public void setXKommentar(String xKommentar) {
		this.xKommentar = xKommentar;
	}

	public Integer getFehlerIId() {
		return fehlerIId;
	}

	public void setFehlerIId(Integer fehlerIId) {
		this.fehlerIId = fehlerIId;
	}

	public Short getBBerechtigt() {
		return bBerechtigt;
	}

	public void setBBerechtigt(Short bBerechtigt) {
		this.bBerechtigt = bBerechtigt;
	}

	public Integer getPersonalIIdRuecksprache() {
		return personalIIdRuecksprache;
	}

	public void setPersonalIIdRuecksprache(Integer personalIIdRuecksprache) {
		this.personalIIdRuecksprache = personalIIdRuecksprache;
	}

	public Timestamp getTRuecksprache() {
		return tRuecksprache;
	}

	public void setTRuecksprache(Timestamp tRuecksprache) {
		this.tRuecksprache = tRuecksprache;
	}

	public String getCRuecksprachemit() {
		return cRuecksprachemit;
	}

	public void setCRuecksprachemit(String cRuecksprachemit) {
		this.cRuecksprachemit = cRuecksprachemit;
	}

	public BigDecimal getNKostenmaterial() {
		return nKostenmaterial;
	}

	public void setNKostenmaterial(BigDecimal nKostenmaterial) {
		this.nKostenmaterial = nKostenmaterial;
	}

	public BigDecimal getNKostenarbeitszeit() {
		return nKostenarbeitszeit;
	}

	public void setNKostenarbeitszeit(BigDecimal nKostenarbeitszeit) {
		this.nKostenarbeitszeit = nKostenarbeitszeit;
	}

	public Integer getMassnahmeIIdKurz() {
		return massnahmeIIdKurz;
	}

	public void setMassnahmeIIdKurz(Integer massnahmeIIdKurz) {
		this.massnahmeIIdKurz = massnahmeIIdKurz;
	}

	public Timestamp getTMassnahmebiskurz() {
		return tMassnahmebiskurz;
	}

	public void setTMassnahmebiskurz(Timestamp tMassnahmebiskurz) {
		this.tMassnahmebiskurz = tMassnahmebiskurz;
	}

	public Timestamp getTEingefuehrtkurz() {
		return tEingefuehrtkurz;
	}

	public void setTEingefuehrtkurz(Timestamp tEingefuehrtkurz) {
		this.tEingefuehrtkurz = tEingefuehrtkurz;
	}

	public Integer getPersonalIIdEingefuehrtkurz() {
		return personalIIdEingefuehrtkurz;
	}

	public void setPersonalIIdEingefuehrtkurz(Integer personalIIdEingefuehrtkurz) {
		this.personalIIdEingefuehrtkurz = personalIIdEingefuehrtkurz;
	}

	public Integer getMassnahmeIIdMittel() {
		return massnahmeIIdMittel;
	}

	public void setMassnahmeIIdMittel(Integer massnahmeIIdMittel) {
		this.massnahmeIIdMittel = massnahmeIIdMittel;
	}

	public Timestamp getTMassnahmebismittel() {
		return tMassnahmebismittel;
	}

	public void setTMassnahmebismittel(Timestamp tMassnahmebismittel) {
		this.tMassnahmebismittel = tMassnahmebismittel;
	}

	public Timestamp getTEingefuehrtmittel() {
		return tEingefuehrtmittel;
	}

	public void setTEingefuehrtmittel(Timestamp tEingefuehrtmittel) {
		this.tEingefuehrtmittel = tEingefuehrtmittel;
	}

	public Integer getPersonalIIdEingefuehrtmittel() {
		return personalIIdEingefuehrtmittel;
	}

	public void setPersonalIIdEingefuehrtmittel(
			Integer personalIIdEingefuehrtmittel) {
		this.personalIIdEingefuehrtmittel = personalIIdEingefuehrtmittel;
	}

	public Integer getMassnahmeIIdLang() {
		return massnahmeIIdLang;
	}

	public void setMassnahmeIIdLang(Integer massnahmeIIdLang) {
		this.massnahmeIIdLang = massnahmeIIdLang;
	}

	public Timestamp getTMassnahmebislang() {
		return tMassnahmebislang;
	}

	public void setTMassnahmebislang(Timestamp tMassnahmebislang) {
		this.tMassnahmebislang = tMassnahmebislang;
	}

	public Timestamp getTEingefuehrtlang() {
		return tEingefuehrtlang;
	}

	public void setTEingefuehrtlang(Timestamp tEingefuehrtlang) {
		this.tEingefuehrtlang = tEingefuehrtlang;
	}

	public Integer getPersonalIIdEingefuehrtlang() {
		return personalIIdEingefuehrtlang;
	}

	public void setPersonalIIdEingefuehrtlang(Integer personalIIdEingefuehrtlang) {
		this.personalIIdEingefuehrtlang = personalIIdEingefuehrtlang;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public Integer getPersonalIIdErledigt() {
		return personalIIdErledigt;
	}

	public Timestamp getTErledigt() {
		return tErledigt;
	}

	public Integer getSchwereIId() {
		return schwereIId;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public void setPersonalIIdErledigt(Integer personalIIdErledigt) {
		this.personalIIdErledigt = personalIIdErledigt;
	}

	public void setTErledigt(Timestamp tErledigt) {
		this.tErledigt = tErledigt;
	}

	public void setSchwereIId(Integer schwereIId) {
		this.schwereIId = schwereIId;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ReklamationDto))
			return false;
		ReklamationDto that = (ReklamationDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.mandantCNr == null ? this.mandantCNr == null
				: that.mandantCNr.equals(this.mandantCNr)))
			return false;
		if (!(that.cNr == null ? this.cNr == null : that.cNr.equals(this.cNr)))
			return false;
		if (!(that.reklamationartCNr == null ? this.reklamationartCNr == null
				: that.reklamationartCNr.equals(this.reklamationartCNr)))
			return false;
		if (!(that.tBelegdatum == null ? this.tBelegdatum == null
				: that.tBelegdatum.equals(this.tBelegdatum)))
			return false;
		if (!(that.kostenstelleIId == null ? this.kostenstelleIId == null
				: that.kostenstelleIId.equals(this.kostenstelleIId)))
			return false;
		if (!(that.fehlerangabeIId == null ? this.fehlerangabeIId == null
				: that.fehlerangabeIId.equals(this.fehlerangabeIId)))
			return false;
		if (!(that.aufnahmeartIId == null ? this.aufnahmeartIId == null
				: that.aufnahmeartIId.equals(this.aufnahmeartIId)))
			return false;
		if (!(that.personalIIdAufnehmer == null ? this.personalIIdAufnehmer == null
				: that.personalIIdAufnehmer.equals(this.personalIIdAufnehmer)))
			return false;
		if (!(that.bArtikel == null ? this.bArtikel == null : that.bArtikel
				.equals(this.bArtikel)))
			return false;
		if (!(that.artikelIId == null ? this.artikelIId == null
				: that.artikelIId.equals(this.artikelIId)))
			return false;
		if (!(that.cHandartikel == null ? this.cHandartikel == null
				: that.cHandartikel.equals(this.cHandartikel)))
			return false;
		if (!(that.nMenge == null ? this.nMenge == null : that.nMenge
				.equals(this.nMenge)))
			return false;
		if (!(that.bFremdprodukt == null ? this.bFremdprodukt == null
				: that.bFremdprodukt.equals(this.bFremdprodukt)))
			return false;
		if (!(that.cGrund == null ? this.cGrund == null : that.cGrund
				.equals(this.cGrund)))
			return false;
		if (!(that.losIId == null ? this.losIId == null : that.losIId
				.equals(this.losIId)))
			return false;
		if (!(that.bestellungIId == null ? this.bestellungIId == null
				: that.bestellungIId.equals(this.bestellungIId)))
			return false;
		if (!(that.lieferscheinIId == null ? this.lieferscheinIId == null
				: that.lieferscheinIId.equals(this.lieferscheinIId)))
			return false;
		if (!(that.rechnungIId == null ? this.rechnungIId == null
				: that.rechnungIId.equals(this.rechnungIId)))
			return false;
		if (!(that.kundeIId == null ? this.kundeIId == null : that.kundeIId
				.equals(this.kundeIId)))
			return false;
		if (!(that.lieferantIId == null ? this.lieferantIId == null
				: that.lieferantIId.equals(this.lieferantIId)))
			return false;
		if (!(that.ansprechpartnerIId == null ? this.ansprechpartnerIId == null
				: that.ansprechpartnerIId.equals(this.ansprechpartnerIId)))
			return false;
		if (!(that.cProjekt == null ? this.cProjekt == null : that.cProjekt
				.equals(this.cProjekt)))
			return false;
		if (!(that.cTelansprechpartner == null ? this.cTelansprechpartner == null
				: that.cTelansprechpartner.equals(this.cTelansprechpartner)))
			return false;
		if (!(that.xAnalyse == null ? this.xAnalyse == null : that.xAnalyse
				.equals(this.xAnalyse)))
			return false;
		if (!(that.xKommentar == null ? this.xKommentar == null
				: that.xKommentar.equals(this.xKommentar)))
			return false;
		if (!(that.fehlerIId == null ? this.fehlerIId == null : that.fehlerIId
				.equals(this.fehlerIId)))
			return false;
		if (!(that.bBerechtigt == null ? this.bBerechtigt == null
				: that.bBerechtigt.equals(this.bBerechtigt)))
			return false;
		if (!(that.personalIIdRuecksprache == null ? this.personalIIdRuecksprache == null
				: that.personalIIdRuecksprache
						.equals(this.personalIIdRuecksprache)))
			return false;
		if (!(that.tRuecksprache == null ? this.tRuecksprache == null
				: that.tRuecksprache.equals(this.tRuecksprache)))
			return false;
		if (!(that.cRuecksprachemit == null ? this.cRuecksprachemit == null
				: that.cRuecksprachemit.equals(this.cRuecksprachemit)))
			return false;
		if (!(that.nKostenmaterial == null ? this.nKostenmaterial == null
				: that.nKostenmaterial.equals(this.nKostenmaterial)))
			return false;
		if (!(that.nKostenarbeitszeit == null ? this.nKostenarbeitszeit == null
				: that.nKostenarbeitszeit.equals(this.nKostenarbeitszeit)))
			return false;
		if (!(that.massnahmeIIdKurz == null ? this.massnahmeIIdKurz == null
				: that.massnahmeIIdKurz.equals(this.massnahmeIIdKurz)))
			return false;
		if (!(that.tMassnahmebiskurz == null ? this.tMassnahmebiskurz == null
				: that.tMassnahmebiskurz.equals(this.tMassnahmebiskurz)))
			return false;
		if (!(that.tEingefuehrtkurz == null ? this.tEingefuehrtkurz == null
				: that.tEingefuehrtkurz.equals(this.tEingefuehrtkurz)))
			return false;
		if (!(that.personalIIdEingefuehrtkurz == null ? this.personalIIdEingefuehrtkurz == null
				: that.personalIIdEingefuehrtkurz
						.equals(this.personalIIdEingefuehrtkurz)))
			return false;
		if (!(that.massnahmeIIdMittel == null ? this.massnahmeIIdMittel == null
				: that.massnahmeIIdMittel.equals(this.massnahmeIIdMittel)))
			return false;
		if (!(that.tMassnahmebismittel == null ? this.tMassnahmebismittel == null
				: that.tMassnahmebismittel.equals(this.tMassnahmebismittel)))
			return false;
		if (!(that.tEingefuehrtmittel == null ? this.tEingefuehrtmittel == null
				: that.tEingefuehrtmittel.equals(this.tEingefuehrtmittel)))
			return false;
		if (!(that.personalIIdEingefuehrtmittel == null ? this.personalIIdEingefuehrtmittel == null
				: that.personalIIdEingefuehrtmittel
						.equals(this.personalIIdEingefuehrtmittel)))
			return false;
		if (!(that.massnahmeIIdLang == null ? this.massnahmeIIdLang == null
				: that.massnahmeIIdLang.equals(this.massnahmeIIdLang)))
			return false;
		if (!(that.tMassnahmebislang == null ? this.tMassnahmebislang == null
				: that.tMassnahmebislang.equals(this.tMassnahmebislang)))
			return false;
		if (!(that.tEingefuehrtlang == null ? this.tEingefuehrtlang == null
				: that.tEingefuehrtlang.equals(this.tEingefuehrtlang)))
			return false;
		if (!(that.personalIIdEingefuehrtlang == null ? this.personalIIdEingefuehrtlang == null
				: that.personalIIdEingefuehrtlang
						.equals(this.personalIIdEingefuehrtlang)))
			return false;
		if (!(that.personalIIdAnlegen == null ? this.personalIIdAnlegen == null
				: that.personalIIdAnlegen.equals(this.personalIIdAnlegen)))
			return false;
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen
				.equals(this.tAnlegen)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern)))
			return false;
		if (!(that.tErledigt == null ? this.tErledigt == null : that.tErledigt
				.equals(this.tErledigt)))
			return false;
		if (!(that.personalIIdErledigt == null ? this.personalIIdErledigt == null
				: that.personalIIdErledigt.equals(this.personalIIdErledigt)))
			return false;
		if (!(that.schwereIId == null ? this.schwereIId == null
				: that.schwereIId.equals(this.schwereIId)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.cNr.hashCode();
		result = 37 * result + this.reklamationartCNr.hashCode();
		result = 37 * result + this.tBelegdatum.hashCode();
		result = 37 * result + this.kostenstelleIId.hashCode();
		result = 37 * result + this.fehlerangabeIId.hashCode();
		result = 37 * result + this.aufnahmeartIId.hashCode();
		result = 37 * result + this.personalIIdAufnehmer.hashCode();
		result = 37 * result + this.bArtikel.hashCode();
		result = 37 * result + this.artikelIId.hashCode();
		result = 37 * result + this.cHandartikel.hashCode();
		result = 37 * result + this.nMenge.hashCode();
		result = 37 * result + this.bFremdprodukt.hashCode();
		result = 37 * result + this.cGrund.hashCode();
		result = 37 * result + this.losIId.hashCode();
		result = 37 * result + this.bestellungIId.hashCode();
		result = 37 * result + this.lieferscheinIId.hashCode();
		result = 37 * result + this.rechnungIId.hashCode();
		result = 37 * result + this.kundeIId.hashCode();
		result = 37 * result + this.lieferantIId.hashCode();
		result = 37 * result + this.ansprechpartnerIId.hashCode();
		result = 37 * result + this.cProjekt.hashCode();
		result = 37 * result + this.cTelansprechpartner.hashCode();
		result = 37 * result + this.xAnalyse.hashCode();
		result = 37 * result + this.xKommentar.hashCode();
		result = 37 * result + this.fehlerIId.hashCode();
		result = 37 * result + this.bBerechtigt.hashCode();
		result = 37 * result + this.personalIIdRuecksprache.hashCode();
		result = 37 * result + this.tRuecksprache.hashCode();
		result = 37 * result + this.cRuecksprachemit.hashCode();
		result = 37 * result + this.nKostenmaterial.hashCode();
		result = 37 * result + this.nKostenarbeitszeit.hashCode();
		result = 37 * result + this.massnahmeIIdKurz.hashCode();
		result = 37 * result + this.tMassnahmebiskurz.hashCode();
		result = 37 * result + this.tEingefuehrtkurz.hashCode();
		result = 37 * result + this.personalIIdEingefuehrtkurz.hashCode();
		result = 37 * result + this.massnahmeIIdMittel.hashCode();
		result = 37 * result + this.tMassnahmebismittel.hashCode();
		result = 37 * result + this.tEingefuehrtmittel.hashCode();
		result = 37 * result + this.personalIIdEingefuehrtmittel.hashCode();
		result = 37 * result + this.massnahmeIIdLang.hashCode();
		result = 37 * result + this.tMassnahmebislang.hashCode();
		result = 37 * result + this.tEingefuehrtlang.hashCode();
		result = 37 * result + this.personalIIdEingefuehrtlang.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.tErledigt.hashCode();
		result = 37 * result + this.personalIIdErledigt.hashCode();
		result = 37 * result + this.schwereIId.hashCode();
		return result;
	}

	public String toString() {
		StringBuffer returnStringBuffer = new StringBuffer(1664);
		returnStringBuffer.append("[");
		returnStringBuffer.append("iId:").append(iId);
		returnStringBuffer.append("mandantCNr:").append(mandantCNr);
		returnStringBuffer.append("cNr:").append(cNr);
		returnStringBuffer.append("reklamationartCNr:").append(
				reklamationartCNr);
		returnStringBuffer.append("tBelegdatum:").append(tBelegdatum);
		returnStringBuffer.append("kostenstelleIId:").append(kostenstelleIId);
		returnStringBuffer.append("fehlerangabeIId:").append(fehlerangabeIId);
		returnStringBuffer.append("aufnahmeartIId:").append(aufnahmeartIId);
		returnStringBuffer.append("personalIIdAufnehmer:").append(
				personalIIdAufnehmer);
		returnStringBuffer.append("bArtikel:").append(bArtikel);
		returnStringBuffer.append("artikelIId:").append(artikelIId);
		returnStringBuffer.append("cHandartikel:").append(cHandartikel);
		returnStringBuffer.append("nMenge:").append(nMenge);
		returnStringBuffer.append("bFremdprodukt:").append(bFremdprodukt);
		returnStringBuffer.append("cGrund:").append(cGrund);
		returnStringBuffer.append("losIId:").append(losIId);
		returnStringBuffer.append("bestellungIId:").append(bestellungIId);
		returnStringBuffer.append("lieferscheinIId:").append(lieferscheinIId);
		returnStringBuffer.append("rechnungIId:").append(rechnungIId);
		returnStringBuffer.append("kundeIId:").append(kundeIId);
		returnStringBuffer.append("lieferantIId:").append(lieferantIId);
		returnStringBuffer.append("ansprechpartnerIId:").append(
				ansprechpartnerIId);
		returnStringBuffer.append("cProjekt:").append(cProjekt);
		returnStringBuffer.append("cTelansprechpartner:").append(
				cTelansprechpartner);
		returnStringBuffer.append("xAnalyse:").append(xAnalyse);
		returnStringBuffer.append("xKommentar:").append(xKommentar);
		returnStringBuffer.append("fehlerIId:").append(fehlerIId);
		returnStringBuffer.append("bBerechtigt:").append(bBerechtigt);
		returnStringBuffer.append("personalIIdRuecksprache:").append(
				personalIIdRuecksprache);
		returnStringBuffer.append("tRuecksprache:").append(tRuecksprache);
		returnStringBuffer.append("cRuecksprachemit:").append(cRuecksprachemit);
		returnStringBuffer.append("nKostenmaterial:").append(nKostenmaterial);
		returnStringBuffer.append("nKostenarbeitszeit:").append(
				nKostenarbeitszeit);
		returnStringBuffer.append("massnahmeIIdKurz:").append(massnahmeIIdKurz);
		returnStringBuffer.append("tMassnahmebiskurz:").append(
				tMassnahmebiskurz);
		returnStringBuffer.append("tEingefuehrtkurz:").append(tEingefuehrtkurz);
		returnStringBuffer.append("personalIIdEingefuehrtkurz:").append(
				personalIIdEingefuehrtkurz);
		returnStringBuffer.append("massnahmeIIdMittel:").append(
				massnahmeIIdMittel);
		returnStringBuffer.append("tMassnahmebismittel:").append(
				tMassnahmebismittel);
		returnStringBuffer.append("tEingefuehrtmittel:").append(
				tEingefuehrtmittel);
		returnStringBuffer.append("personalIIdEingefuehrtmittel:").append(
				personalIIdEingefuehrtmittel);
		returnStringBuffer.append("massnahmeIIdLang:").append(massnahmeIIdLang);
		returnStringBuffer.append("tMassnahmebislang:").append(
				tMassnahmebislang);
		returnStringBuffer.append("tEingefuehrtlang:").append(tEingefuehrtlang);
		returnStringBuffer.append("personalIIdEingefuehrtlang:").append(
				personalIIdEingefuehrtlang);
		returnStringBuffer.append("personalIIdAnlegen:").append(
				personalIIdAnlegen);
		returnStringBuffer.append("tAnlegen:").append(tAnlegen);
		returnStringBuffer.append("personalIIdAendern:").append(
				personalIIdAendern);
		returnStringBuffer.append("tAendern:").append(tAendern);
		returnStringBuffer.append("tErledigt:").append(tErledigt);
		returnStringBuffer.append("personalIIdErledigt:").append(
				personalIIdErledigt);
		returnStringBuffer.append("schwereIId:").append(schwereIId);
		returnStringBuffer.append("]");
		return returnStringBuffer.toString();
	}
}

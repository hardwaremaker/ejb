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
package com.lp.server.artikel.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.util.ICNr;

@NamedQueries({
		@NamedQuery(name = "ArtikelfindByCNrMandantCNr", query = "SELECT OBJECT(C) FROM Artikel c WHERE c.cNr = ?1 AND c.mandantCNr=?2"),
		@NamedQuery(name = "ArtikelfindByCVerkaufseannrMandantCNr", query = "SELECT OBJECT(C) FROM Artikel c WHERE c.cVerkaufseannr = ?1 AND c.mandantCNr=?2"),
		@NamedQuery(name = "ArtikelfindByCVerpackungseannrMandantCNr", query = "SELECT OBJECT(C) FROM Artikel c WHERE c.cVerpackungseannr = ?1 AND c.mandantCNr=?2"),
		@NamedQuery(name = "ArtikelfindByCNr", query = "SELECT OBJECT(C) FROM Artikel c WHERE c.cNr = ?1"),
		@NamedQuery(name = "ArtikelfindByArtgruIIdMandantCNr", query = "SELECT OBJECT(C) FROM Artikel c WHERE c.mandantCNr = ?1 AND c.artgruIId = ?2"),
		@NamedQuery(name = "ArtikelfindByArtgruIIdMandantCNrWithDate", 
			query = "SELECT OBJECT(C) FROM Artikel c WHERE c.mandantCNr = ?1 AND c.artgruIId = ?2 AND c.tAendern >= ?3"),
		@NamedQuery(name = ArtikelQuery.ByMandantCNrShopgruppeIId,
			query = "SELECT OBJECT(C) FROM Artikel c WHERE c.mandantCNr = :mandant AND c.shopgruppeIId = :id"),
		@NamedQuery(name = ArtikelQuery.ByMandantCNrShopgruppeIIdWithDate,
			query = "SELECT OBJECT(C) FROM Artikel c WHERE c.mandantCNr = :mandant AND c.shopgruppeIId = :id AND c.tAendern >= :tChanged"),	
		@NamedQuery(name = ArtikelQuery.ByMandantCNrWithDate,
			query = "SELECT OBJECT(C) FROM Artikel c WHERE c.mandantCNr = :mandant AND c.shopgruppeIId IS NOT NULL AND c.bVersteckt = 0 AND c.tAendern >= :tChanged")	
		})
@Entity
@Table(name = "WW_ARTIKEL")
public class Artikel implements Serializable, ICNr {
	
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "C_ARTIKELBEZHERSTELLER")
	private String cArtikelbezhersteller;

	@Column(name = "C_ARTIKELNRHERSTELLER")
	private String cArtikelnrhersteller;

	@Column(name = "B_SERIENNRTRAGEND")
	private Short bSeriennrtragend;

	@Column(name = "B_CHARGENNRTRAGEND")
	private Short bChargennrtragend;

	@Column(name = "B_BESTELLMENGENEINHEIT_INVERS")
	private Short bBestellmengeneinheitInvers;

	
	public Short getbBestellmengeneinheitInvers() {
		return bBestellmengeneinheitInvers;
	}

	public void setbBestellmengeneinheitInvers(Short bBestellmengeneinheitInvers) {
		this.bBestellmengeneinheitInvers = bBestellmengeneinheitInvers;
	}

	@Column(name = "B_LAGERBEWIRTSCHAFTET")
	private Short bLagerbewirtschaftet;

	@Column(name = "B_DOKUMENTENPFLICHT")
	private Short bDokumentenpflicht;

	@Column(name = "B_VERLEIH")
	private Short bVerleih;
	
	@Column(name = "B_KALKULATORISCH")
	private Short bKalkulatorisch;

	public Short getBKalkulatorisch() {
		return bKalkulatorisch;
	}

	public void setBKalkulatorisch(Short bKalkulatorisch) {
		this.bKalkulatorisch = bKalkulatorisch;
	}

	@Column(name = "LFLIEFERGRUPPE_I_ID")
	private Integer lfliefergruppeIId;

	public Integer getLfliefergruppeIId() {
		return lfliefergruppeIId;
	}

	public void setLfliefergruppeIId(Integer lfliefergruppeIId) {
		this.lfliefergruppeIId = lfliefergruppeIId;
	}

	public Short getBVerleih() {
		return bVerleih;
	}

	public void setBVerleih(Short bVerleih) {
		this.bVerleih = bVerleih;
	}

	@Column(name = "B_REINEMANNZEIT")
	private Short bReinemannzeit;
	@Column(name = "B_NURZURINFO")
	private Short bNurzurinfo;

	public Short getbReinemannzeit() {
		return bReinemannzeit;
	}

	public void setbReinemannzeit(Short bReinemannzeit) {
		this.bReinemannzeit = bReinemannzeit;
	}

	public Short getbNurzurinfo() {
		return bNurzurinfo;
	}

	public void setbNurzurinfo(Short bNurzurinfo) {
		this.bNurzurinfo = bNurzurinfo;
	}

	@Column(name = "B_LAGERBEWERTET")
	private Short bLagerbewertet;

	public Short getBDokumentenpflicht() {
		return bDokumentenpflicht;
	}

	public void setBDokumentenpflicht(Short dokumentenpflicht) {
		bDokumentenpflicht = dokumentenpflicht;
	}

	@Column(name = "C_REFERENZNR")
	private String cReferenznr;

	@Column(name = "F_LAGERMINDEST")
	private Double fLagermindest;

	@Column(name = "F_LAGERSOLL")
	private Double fLagersoll;

	@Column(name = "F_FERTIGUNGSSATZGROESSE")
	private Double fFertigungssatzgroesse;

	@Column(name = "F_VERPACKUNGSMENGE")
	private Double fVerpackungsmenge;

	@Column(name = "F_VERSCHNITTFAKTOR")
	private Double fVerschnittfaktor;

	@Column(name = "F_DETAILPROZENTMINDESTSTAND")
	private Double fDetailprozentmindeststand;

	public Double getFDetailprozentmindeststand() {
		return fDetailprozentmindeststand;
	}

	public void setFDetailprozentmindeststand(Double fDetailprozentmindeststand) {
		this.fDetailprozentmindeststand = fDetailprozentmindeststand;
	}

	@Column(name = "F_VERSCHNITTBASIS")
	private Double fVerschnittbasis;

	@Column(name = "F_JAHRESMENGE")
	private Double fJahresmenge;

	@Column(name = "F_GEWICHTKG")
	private Double fGewichtkg;

	@Column(name = "F_MATERIALGEWICHT")
	private Double fMaterialgewicht;

	@Column(name = "B_ANTISTATIC")
	private Short bAntistatic;

	@Column(name = "F_VERTRETERPROVISIONMAX")
	private Double fVertreterprovisionmax;

	@Column(name = "F_MINUTENFAKTOR1")
	private Double fMinutenfaktor1;

	@Column(name = "F_MINUTENFAKTOR2")
	private Double fMinutenfaktor2;

	@Column(name = "F_MINDESTDECKUNGSBEITRAG")
	private Double fMindestdeckungsbeitrag;

	@Column(name = "C_VERKAUFSEANNR")
	private String cVerkaufseannr;

	@Column(name = "C_WARENVERKEHRSNUMMER")
	private String cWarenverkehrsnummer;

	@Column(name = "B_RABATTIERBAR")
	private Short bRabattierbar;

	@Column(name = "I_GARANTIEZEIT")
	private Integer iGarantiezeit;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "N_UMRECHNUGSFAKTOR")
	private BigDecimal nUmrechnugsfaktor;

	@Column(name = "B_VERSTECKT")
	private Short bVersteckt;

	@Column(name = "B_WERBEABGABEPFLICHTIG")
	private Short bWerbeabgabepflichtig;

	
	public Short getBWerbeabgabepflichtig() {
		return bWerbeabgabepflichtig;
	}

	public void setBWerbeabgabepflichtig(Short bWerbeabgabepflichtig) {
		this.bWerbeabgabepflichtig = bWerbeabgabepflichtig;
	}

	@Column(name = "C_VERPACKUNGSEANNR")
	private String cVerpackungseannr;

	@Column(name = "EINHEIT_C_NR")
	private String einheitCNr;

	@Column(name = "EINHEIT_C_NR_BESTELLUNG")
	private String einheitCNrBestellung;

	@Column(name = "LAND_I_ID_URSPRUNGSLAND")
	private Integer landIIdUrsprungsland;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "MWSTSATZ_I_ID")
	private Integer mwstsatzIId;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "ARTGRU_I_ID")
	private Integer artgruIId;

	@Column(name = "ARTIKEL_I_ID_ZUGEHOERIG")
	private Integer artikelIIdZugehoerig;

	@Column(name = "ARTIKEL_I_ID_ERSATZ")
	private Integer artikelIIdErsatz;

	@Column(name = "SHOPGRUPPE_I_ID")
	private Integer shopgruppeIId;
	
	public Integer getShopgruppeIId() {
		return shopgruppeIId;
	}

	public void setShopgruppeIId(Integer shopgruppeIId) {
		this.shopgruppeIId = shopgruppeIId;
	}

	@Column(name = "ARTIKELART_C_NR")
	private String artikelartCNr;

	@Column(name = "ARTKLA_I_ID")
	private Integer artklaIId;

	@Column(name = "FARBCODE_I_ID")
	private Integer farbcodeIId;

	@Column(name = "HERSTELLER_I_ID")
	private Integer herstellerIId;

	@Column(name = "MATERIAL_I_ID")
	private Integer materialIId;

	@Column(name = "I_WARTUNGSINTERVALL")
	private Integer iWartungsintervall;

	@Column(name = "I_SOFORTVERBRAUCH")
	private Integer iSofortverbrauch;

	@Column(name = "C_REVISION")
	private String cRevision;
	@Column(name = "C_INDEX")
	private String cIndex;

	@Column(name = "F_STROMVERBRAUCHTYP")
	private Double fStromverbrauchtyp;

	public Double getFStromverbrauchtyp() {
		return fStromverbrauchtyp;
	}

	public void setFStromverbrauchtyp(Double fStromverbrauchtyp) {
		this.fStromverbrauchtyp = fStromverbrauchtyp;
	}

	public Double getFStromverbrauchmax() {
		return fStromverbrauchmax;
	}

	public void setFStromverbrauchmax(Double fStromverbrauchmax) {
		this.fStromverbrauchmax = fStromverbrauchmax;
	}

	@Column(name = "F_STROMVERBRAUCHMAX")
	private Double fStromverbrauchmax;

	public String getCRevision() {
		return cRevision;
	}

	public void setCRevision(String revision) {
		cRevision = revision;
	}

	public String getCIndex() {
		return cIndex;
	}

	public void setCIndex(String index) {
		cIndex = index;
	}

	public Integer getISofortverbrauch() {
		return iSofortverbrauch;
	}

	public void setISofortverbrauch(Integer sofortverbrauch) {
		iSofortverbrauch = sofortverbrauch;
	}

	public Integer getIWartungsintervall() {
		return iWartungsintervall;
	}

	public void setIWartungsintervall(Integer wartungsintervall) {
		iWartungsintervall = wartungsintervall;
	}

	
	@Column(name = "PERSONAL_I_ID_LETZTEWARTUNG")
	private Integer personalIIdLetztewartung;
	@Column(name = "T_LETZTEWARTUNG")
	private Timestamp tLetztewartung;
	
	
	public Timestamp getTLetztewartung() {
		return tLetztewartung;
	}

	public void setTLetztewartung(Timestamp tLetztewartung) {
		this.tLetztewartung = tLetztewartung;
	}

	public Integer getPersonalIIdLetztewartung() {
		return personalIIdLetztewartung;
	}

	public void setPersonalIIdLetztewartung(Integer personalIIdLetztewartung) {
		this.personalIIdLetztewartung = personalIIdLetztewartung;
	}

	
	@Column(name = "F_AUFSCHLAG_PROZENT")
	private Double fAufschlagProzent;

	@Column(name = "N_AUFSCHLAG_BETRAG")
	private BigDecimal nAufschlagBetrag;
	
	public Double getFAufschlagProzent() {
		return fAufschlagProzent;
	}

	public void setFAufschlagProzent(Double fAufschlagProzent) {
		this.fAufschlagProzent = fAufschlagProzent;
	}

	public BigDecimal getNAufschlagBetrag() {
		return nAufschlagBetrag;
	}

	public void setNAufschlagBetrag(BigDecimal nAufschlagBetrag) {
		this.nAufschlagBetrag = nAufschlagBetrag;
	}

	private static final long serialVersionUID = 1L;

	public Artikel() {

	}

	public Artikel(Integer id, String cNr, String artikelartCNr,
			String einheitCNr, Double mindestdeckungsbeitrag,
			Integer personalIIdAnlegen, Integer personalIIdAendern,
			Short versteckt, String mandantCNr) {
		super();
		setIId(id);
		setTAnlegen(new Timestamp(System.currentTimeMillis()));
		setTAendern(new Timestamp(System.currentTimeMillis()));
		setCNr(cNr);
		setBAntistatic(new Short((short) 0));
		setBChargennrtragend(new Short((short) 0));
		setBVerleih(new Short((short) 0));
		setBRabattierbar(new Short((short) 0));
		setBSeriennrtragend(new Short((short) 0));
		setBLagerbewertet(new Short((short) 1));
		setBLagerbewirtschaftet(new Short((short) 1));
		setBDokumentenpflicht(new Short((short) 0));
		setbNurzurinfo(new Short((short) 0));
		setbReinemannzeit(new Short((short) 0));
		setbBestellmengeneinheitInvers(new Short((short) 0));
		setBWerbeabgabepflichtig(new Short((short) 0));
		setBKalkulatorisch(new Short((short) 0));
		setArtikelartCNr(artikelartCNr);
		setEinheitCNr(einheitCNr);
		setFMindestdeckungsbeitrag(mindestdeckungsbeitrag);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setPersonalIIdAendern(personalIIdAendern);
		setBVersteckt(versteckt);
		setMandantCNr(mandantCNr);

	}

	public Artikel(Integer id, String cNr, String artikelartCNr,
			String einheitCNr, Short seriennrtragend, Short chargennrtragend,
			Short lagerbewirtschaftet, Short lagerbewertet, Short antistatic,
			Double mindestdeckungsbeitrag, Short rabattierbar,
			Integer personalIIdAnlegen, Integer personalIIdAendern,
			String mandantCNr, Short versteckt, Short verleih, Short bReineMannzeit, Short bNurzurinfo, Short bBestellmengeneinheitInvers,Short bKalkulatorisch) {
		super();
		setIId(id);
		setTAnlegen(new Timestamp(System.currentTimeMillis()));
		setTAendern(new Timestamp(System.currentTimeMillis()));
		setCNr(cNr);
		setBVerleih(verleih);
		setBAntistatic(antistatic);
		setBChargennrtragend(chargennrtragend);
		setBRabattierbar(rabattierbar);
		setBSeriennrtragend(seriennrtragend);
		setBLagerbewertet(lagerbewertet);
		setBLagerbewirtschaftet(lagerbewirtschaftet);
		setArtikelartCNr(artikelartCNr);
		setEinheitCNr(einheitCNr);
		setFMindestdeckungsbeitrag(mindestdeckungsbeitrag);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setPersonalIIdAendern(personalIIdAendern);
		setBVersteckt(versteckt);
		setMandantCNr(mandantCNr);
		setbReinemannzeit(bReineMannzeit);
		setbNurzurinfo(bNurzurinfo);
		setBKalkulatorisch(bKalkulatorisch);

	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCNr() {
		return this.cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public String getCArtikelbezhersteller() {
		return this.cArtikelbezhersteller;
	}

	public void setCArtikelbezhersteller(String cArtikelbezhersteller) {
		this.cArtikelbezhersteller = cArtikelbezhersteller;
	}

	public String getCArtikelnrhersteller() {
		return this.cArtikelnrhersteller;
	}

	public void setCArtikelnrhersteller(String cArtikelnrhersteller) {
		this.cArtikelnrhersteller = cArtikelnrhersteller;
	}

	public Short getBSeriennrtragend() {
		return this.bSeriennrtragend;
	}

	public void setBSeriennrtragend(Short bSeriennrtragend) {
		this.bSeriennrtragend = bSeriennrtragend;
	}

	public Short getBChargennrtragend() {
		return this.bChargennrtragend;
	}

	public void setBChargennrtragend(Short bChargennrtragend) {
		this.bChargennrtragend = bChargennrtragend;
	}

	public Short getBLagerbewirtschaftet() {
		return this.bLagerbewirtschaftet;
	}

	public void setBLagerbewirtschaftet(Short bLagerbewirtschaftet) {
		this.bLagerbewirtschaftet = bLagerbewirtschaftet;
	}

	public Short getBLagerbewertet() {
		return this.bLagerbewertet;
	}

	public void setBLagerbewertet(Short bLagerbewertet) {
		this.bLagerbewertet = bLagerbewertet;
	}

	public String getCReferenznr() {
		return this.cReferenznr;
	}

	public void setCReferenznr(String cReferenznr) {
		this.cReferenznr = cReferenznr;
	}

	public Double getFLagermindest() {
		return this.fLagermindest;
	}

	public void setFLagermindest(Double fLagermindest) {
		this.fLagermindest = fLagermindest;
	}

	public Double getFLagersoll() {
		return this.fLagersoll;
	}

	public void setFLagersoll(Double fLagersoll) {
		this.fLagersoll = fLagersoll;
	}

	public Double getFFertigungssatzgroesse() {
		return this.fFertigungssatzgroesse;
	}

	public void setFFertigungssatzgroesse(Double fFertigungssatzgroesse) {
		this.fFertigungssatzgroesse = fFertigungssatzgroesse;
	}

	public Double getFVerpackungsmenge() {
		return this.fVerpackungsmenge;
	}

	public void setFVerpackungsmenge(Double fVerpackungsmenge) {
		this.fVerpackungsmenge = fVerpackungsmenge;
	}

	public Double getFVerschnittfaktor() {
		return this.fVerschnittfaktor;
	}

	public void setFVerschnittfaktor(Double fVerschnittfaktor) {
		this.fVerschnittfaktor = fVerschnittfaktor;
	}

	public Double getFVerschnittbasis() {
		return this.fVerschnittbasis;
	}

	public void setFVerschnittbasis(Double fVerschnittbasis) {
		this.fVerschnittbasis = fVerschnittbasis;
	}

	public Double getFJahresmenge() {
		return this.fJahresmenge;
	}

	public void setFJahresmenge(Double fJahresmenge) {
		this.fJahresmenge = fJahresmenge;
	}

	public Double getFGewichtkg() {
		return this.fGewichtkg;
	}

	public void setFGewichtkg(Double fGewichtkg) {
		this.fGewichtkg = fGewichtkg;
	}

	public Double getFMaterialgewicht() {
		return this.fMaterialgewicht;
	}

	public void setFMaterialgewicht(Double fMaterialgewicht) {
		this.fMaterialgewicht = fMaterialgewicht;
	}

	public Short getBAntistatic() {
		return this.bAntistatic;
	}

	public void setBAntistatic(Short bAntistatic) {
		this.bAntistatic = bAntistatic;
	}

	public Double getFVertreterprovisionmax() {
		return this.fVertreterprovisionmax;
	}

	public void setFVertreterprovisionmax(Double fVertreterprovisionmax) {
		this.fVertreterprovisionmax = fVertreterprovisionmax;
	}

	public Double getFMinutenfaktor1() {
		return this.fMinutenfaktor1;
	}

	public void setFMinutenfaktor1(Double fMinutenfaktor1) {
		this.fMinutenfaktor1 = fMinutenfaktor1;
	}

	public Double getFMinutenfaktor2() {
		return this.fMinutenfaktor2;
	}

	public void setFMinutenfaktor2(Double fMinutenfaktor2) {
		this.fMinutenfaktor2 = fMinutenfaktor2;
	}

	public Double getFMindestdeckungsbeitrag() {
		return this.fMindestdeckungsbeitrag;
	}

	public void setFMindestdeckungsbeitrag(Double fMindestdeckungsbeitrag) {
		this.fMindestdeckungsbeitrag = fMindestdeckungsbeitrag;
	}

	public String getCVerkaufseannr() {
		return this.cVerkaufseannr;
	}

	public void setCVerkaufseannr(String cVerkaufseannr) {
		this.cVerkaufseannr = cVerkaufseannr;
	}

	public String getCWarenverkehrsnummer() {
		return this.cWarenverkehrsnummer;
	}

	public void setCWarenverkehrsnummer(String cWarenverkehrsnummer) {
		this.cWarenverkehrsnummer = cWarenverkehrsnummer;
	}

	public Short getBRabattierbar() {
		return this.bRabattierbar;
	}

	public void setBRabattierbar(Short bRabattierbar) {
		this.bRabattierbar = bRabattierbar;
	}

	public Integer getIGarantiezeit() {
		return this.iGarantiezeit;
	}

	public void setIGarantiezeit(Integer iGarantiezeit) {
		this.iGarantiezeit = iGarantiezeit;
	}

	public Timestamp getTAnlegen() {
		return this.tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public BigDecimal getNUmrechnungsfaktor() {
		return this.nUmrechnugsfaktor;
	}

	public void setNUmrechnungsfaktor(BigDecimal nUmrechnugsfaktor) {
		this.nUmrechnugsfaktor = nUmrechnugsfaktor;
	}

	public Short getBVersteckt() {
		return this.bVersteckt;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	public String getCVerpackungseannr() {
		return this.cVerpackungseannr;
	}

	public void setCVerpackungseannr(String cVerpackungseannr) {
		this.cVerpackungseannr = cVerpackungseannr;
	}

	public String getEinheitCNr() {
		return this.einheitCNr;
	}

	public void setEinheitCNr(String einheitCNr) {
		this.einheitCNr = einheitCNr;
	}

	public String getEinheitCNrBestellung() {
		return this.einheitCNrBestellung;
	}

	public void setEinheitCNrBestellung(String einheitCNrBestellung) {
		this.einheitCNrBestellung = einheitCNrBestellung;
	}

	public Integer getLandIIdUrsprungsland() {
		return this.landIIdUrsprungsland;
	}

	public void setLandIIdUrsprungsland(Integer landIIdUrsprungsland) {
		this.landIIdUrsprungsland = landIIdUrsprungsland;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Integer getMwstsatzIId() {
		return this.mwstsatzIId;
	}

	public void setMwstsatzIId(Integer mwstsatzIId) {
		this.mwstsatzIId = mwstsatzIId;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Integer getArtgruIId() {
		return this.artgruIId;
	}

	public void setArtgruIId(Integer artgruIId) {
		this.artgruIId = artgruIId;
	}

	public Integer getArtikelIIdZugehoerig() {
		return this.artikelIIdZugehoerig;
	}

	public void setArtikelIIdZugehoerig(Integer artikelIIdZugehoerig) {
		this.artikelIIdZugehoerig = artikelIIdZugehoerig;
	}

	public Integer getArtikelIIdErsatz() {
		return this.artikelIIdErsatz;
	}

	public void setArtikelIIdErsatz(Integer artikelIIdErsatz) {
		this.artikelIIdErsatz = artikelIIdErsatz;
	}

	public String getArtikelartCNr() {
		return this.artikelartCNr;
	}

	public void setArtikelartCNr(String artikelartCNr) {
		this.artikelartCNr = artikelartCNr;
	}

	public Integer getArtklaIId() {
		return this.artklaIId;
	}

	public void setArtklaIId(Integer artklaIId) {
		this.artklaIId = artklaIId;
	}

	public Integer getFarbcodeIId() {
		return this.farbcodeIId;
	}

	public void setFarbcodeIId(Integer farbcodeIId) {
		this.farbcodeIId = farbcodeIId;
	}

	public Integer getHerstellerIId() {
		return this.herstellerIId;
	}

	public void setHerstellerIId(Integer herstellerIId) {
		this.herstellerIId = herstellerIId;
	}

	public Integer getMaterialIId() {
		return this.materialIId;
	}

	public void setMaterialIId(Integer materialIId) {
		this.materialIId = materialIId;
	}

}

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
package com.lp.server.artikel.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.lp.server.artikel.ejb.Artgru;
import com.lp.server.artikel.ejb.Artkla;
import com.lp.server.artikel.ejb.Material;
import com.lp.server.artikel.ejb.Shopgruppe;
import com.lp.server.partner.ejb.Lfliefergruppe;
import com.lp.server.system.service.HvDtoLogClass;
import com.lp.server.system.service.HvDtoLogComplex;
import com.lp.server.system.service.HvDtoLogIdCnr;
import com.lp.server.system.service.HvDtoLogIgnore;
import com.lp.server.util.IIId;
import com.lp.util.Helper;

@HvDtoLogClass(name = HvDtoLogClass.ARTIKEL)
public class ArtikelDto implements Serializable, IIId {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String cNr;
	private Integer herstellerIId;
	private Integer artgruIId;
	private Integer artklaIId;

	private Double fFertigungsVpe;

	public Double getFFertigungsVpe() {
		return fFertigungsVpe;
	}

	public void setFFertigungsVpe(Double fFertigungsVpe) {
		this.fFertigungsVpe = fFertigungsVpe;
	}

	private String cEccn;

	public String getCEccn() {
		return cEccn;
	}

	public void setCEccn(String cEccn) {
		this.cEccn = cEccn;
	}

	private Double fUeberproduktion;

	public Double getFUeberproduktion() {
		return fUeberproduktion;
	}

	public void setFUeberproduktion(Double fUeberproduktion) {
		this.fUeberproduktion = fUeberproduktion;
	}

	private Short bKalkulatorisch;

	public Short getBKalkulatorisch() {
		return bKalkulatorisch;
	}

	public void setBKalkulatorisch(Short bKalkulatorisch) {
		this.bKalkulatorisch = bKalkulatorisch;
	}

	private Integer personalIIdLetztewartung;
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

	private Integer lfliefergruppeIId;

	@HvDtoLogIdCnr(entityClass = Lfliefergruppe.class)
	public Integer getLfliefergruppeIId() {
		return lfliefergruppeIId;
	}

	public void setLfliefergruppeIId(Integer lfliefergruppeIId) {
		this.lfliefergruppeIId = lfliefergruppeIId;
	}

	private Double fDetailprozentmindeststand;

	public Double getFDetailprozentmindeststand() {
		return fDetailprozentmindeststand;
	}

	public void setFDetailprozentmindeststand(Double fDetailprozentmindeststand) {
		this.fDetailprozentmindeststand = fDetailprozentmindeststand;
	}

	private Short bBestellmengeneinheitInvers;

	public Short getbBestellmengeneinheitInvers() {
		return bBestellmengeneinheitInvers;
	}

	public void setbBestellmengeneinheitInvers(Short bBestellmengeneinheitInvers) {
		this.bBestellmengeneinheitInvers = bBestellmengeneinheitInvers;
	}

	private Integer shopgruppeIId;

	@HvDtoLogIdCnr(entityClass = Shopgruppe.class)
	public Integer getShopgruppeIId() {
		return shopgruppeIId;
	}

	public void setShopgruppeIId(Integer shopgruppeIId) {
		this.shopgruppeIId = shopgruppeIId;
	}

	private Short bReinemannzeit;
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

	private Short bWerbeabgabepflichtig;

	public Short getBWerbeabgabepflichtig() {
		return bWerbeabgabepflichtig;
	}

	public void setBWerbeabgabepflichtig(Short bWerbeabgabepflichtig) {
		this.bWerbeabgabepflichtig = bWerbeabgabepflichtig;
	}

	private Short bVerleih;

	public Short getBVerleih() {
		return bVerleih;
	}

	public void setBVerleih(Short bVerleih) {
		this.bVerleih = bVerleih;
	}

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

	private String artikelartCNr;
	private String mandantCNr;
	private Short bDokumentenpflicht;

	public Short getBDokumentenpflicht() {
		return bDokumentenpflicht;
	}

	private String cRevision;
	private String cIndex;

	public void setBDokumentenpflicht(Short dokumentenpflicht) {
		bDokumentenpflicht = dokumentenpflicht;
	}

	private Short bSeriennrtragend;
	private Short bLagerbewertet;
	private Timestamp tAnlegen;
	private Integer farbcodeIId;
	private BigDecimal nUmrechnungsfaktor;
	private Integer personalIIdAnlegen;
	private Timestamp tAendern;
	private Integer personalIIdAendern;
	private String einheitCNrBestellung;
	private String cArtikelbezhersteller;
	private String cArtikelnrhersteller;
	private String einheitCNr;
	private String cReferenznr;
	private Double fLagermindest;
	private Double fLagersoll;
	private Double fFertigungssatzgroesse;
	private Short bLagerbewirtschaftet;
	private Double fVerpackungsmenge;
	private Double fVerschnittfaktor;
	private Double fVerschnittbasis;
	private Double fJahresmenge;
	private Integer mwstsatzbezIId;
	private Integer materialIId;
	private Double fGewichtkg;
	private Double fMaterialgewicht;
	private Short bAntistatic;
	private Integer artikelIIdZugehoerig;
	private Double fVertreterprovisionmax;
	private Double fMinutenfaktor1;
	private Double fMinutenfaktor2;
	private Double fMindestdeckungsbeitrag;
	private String cVerkaufseannr;
	private String cWarenverkehrsnummer;
	private Short bRabattierbar;
	private Integer iGarantiezeit;
	private SollverkaufDto sollverkaufDto;
	private GeometrieDto geometrieDto;
	private VerpackungDto verpackungDto;
	private MontageDto montageDto;
	private ArtikelsprDto artikelsprDto;
	private ArtgruDto artgruDto;
	private ArtklaDto artklaDto;
	private Short bChargennrtragend;
	private HerstellerDto herstellerDto;
	private Short bVersteckt;
	private String cVerpackungseannr;
	private Integer artikelIIdErsatz;
	private Integer landIIdUrsprungsland;
	private Integer iWartungsintervall;
	private Integer iSofortverbrauch;

	private Double fAufschlagProzent;
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

	private Double fStromverbrauchtyp;
	private Double fStromverbrauchmax;

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

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public String getArtikelartCNr() {
		return artikelartCNr;
	}

	public void setArtikelartCNr(String artikelartCNr) {
		this.artikelartCNr = artikelartCNr;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	@HvDtoLogIgnore
	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public String getCArtikelbezhersteller() {
		return cArtikelbezhersteller;
	}

	public void setCArtikelbezhersteller(String cArtikelbezhersteller) {
		this.cArtikelbezhersteller = cArtikelbezhersteller;
	}

	public String getCArtikelnrhersteller() {
		return cArtikelnrhersteller;
	}

	public void setCArtikelnrhersteller(String cArtikelnrhersteller) {
		this.cArtikelnrhersteller = cArtikelnrhersteller;
	}

	public String getEinheitCNr() {
		return einheitCNr;
	}

	public void setEinheitCNr(String einheitCNr) {
		this.einheitCNr = einheitCNr;
	}

	public String getCReferenznr() {
		return cReferenznr;
	}

	public void setCReferenznr(String cReferenznr) {
		this.cReferenznr = cReferenznr;
	}

	public Double getFLagermindest() {
		return fLagermindest;
	}

	public void setFLagermindest(Double fLagermindest) {
		this.fLagermindest = fLagermindest;
	}

	public Double getFLagersoll() {
		return fLagersoll;
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
		return fVerpackungsmenge;
	}

	public void setFVerpackungsmenge(Double fVerpackungsmenge) {
		this.fVerpackungsmenge = fVerpackungsmenge;
	}

	public Double getFVerschnittfaktor() {
		return fVerschnittfaktor;
	}

	public void setFVerschnittfaktor(Double fVerschnittfaktor) {
		this.fVerschnittfaktor = fVerschnittfaktor;
	}

	public Double getFVerschnittbasis() {
		return fVerschnittbasis;
	}

	public void setFVerschnittbasis(Double fVerschnittbasis) {
		this.fVerschnittbasis = fVerschnittbasis;
	}

	public Double getFJahresmenge() {
		return fJahresmenge;
	}

	public void setFJahresmenge(Double fJahresmenge) {
		this.fJahresmenge = fJahresmenge;
	}

	public Integer getMwstsatzbezIId() {
		return mwstsatzbezIId;
	}

	public void setMwstsatzbezIId(Integer mwstsatzbezIId) {
		this.mwstsatzbezIId = mwstsatzbezIId;
	}

	public Double getFGewichtkg() {
		return fGewichtkg;
	}

	public void setFGewichtkg(Double fGewichtkg) {
		this.fGewichtkg = fGewichtkg;
	}

	public Double getFMaterialgewicht() {
		return fMaterialgewicht;
	}

	public void setFMaterialgewicht(Double fMaterialgewicht) {
		this.fMaterialgewicht = fMaterialgewicht;
	}

	public Short getBAntistatic() {
		return bAntistatic;
	}

	public void setBAntistatic(Short bAntistatic) {
		this.bAntistatic = bAntistatic;
	}

	public Integer getArtikelIIdZugehoerig() {
		return artikelIIdZugehoerig;
	}

	public void setArtikelIIdZugehoerig(Integer artikelIIdZugehoerig) {
		this.artikelIIdZugehoerig = artikelIIdZugehoerig;
	}

	public Double getFVertreterprovisionmax() {
		return fVertreterprovisionmax;
	}

	public void setFVertreterprovisionmax(Double fVertreterprovisionmax) {
		this.fVertreterprovisionmax = fVertreterprovisionmax;
	}

	public Double getFMinutenfaktor1() {
		return fMinutenfaktor1;
	}

	public void setFMinutenfaktor1(Double fMinutenfaktor1) {
		this.fMinutenfaktor1 = fMinutenfaktor1;
	}

	public Double getFMinutenfaktor2() {
		return fMinutenfaktor2;
	}

	public void setFMinutenfaktor2(Double fMinutenfaktor2) {
		this.fMinutenfaktor2 = fMinutenfaktor2;
	}

	public Double getFMindestdeckungsbeitrag() {
		return fMindestdeckungsbeitrag;
	}

	public void setFMindestdeckungsbeitrag(Double fMindestdeckungsbeitrag) {
		this.fMindestdeckungsbeitrag = fMindestdeckungsbeitrag;
	}

	public String getCVerkaufseannr() {
		return cVerkaufseannr;
	}

	public void setCVerkaufseannr(String cVerkaufseannr) {
		this.cVerkaufseannr = cVerkaufseannr;
	}

	public String getCWarenverkehrsnummer() {
		return cWarenverkehrsnummer;
	}

	public void setCWarenverkehrsnummer(String cWarenverkehrsnummer) {
		this.cWarenverkehrsnummer = cWarenverkehrsnummer;
	}

	public Short getBRabattierbar() {
		return bRabattierbar;
	}

	public void setBRabattierbar(Short bRabattierbar) {
		this.bRabattierbar = bRabattierbar;
	}

	public Integer getIGarantiezeit() {
		return iGarantiezeit;
	}

	@HvDtoLogComplex
	public ArtikelsprDto getArtikelsprDto() {
		return artikelsprDto;
	}

	@HvDtoLogIgnore
	public MontageDto getMontageDto() {
		return montageDto;
	}

	@HvDtoLogIgnore
	public VerpackungDto getVerpackungDto() {
		return verpackungDto;
	}

	@HvDtoLogIgnore
	public GeometrieDto getGeometrieDto() {
		return geometrieDto;
	}

	@HvDtoLogIgnore
	public SollverkaufDto getSollverkaufDto() {
		return sollverkaufDto;
	}

	@HvDtoLogIgnore
	public ArtgruDto getArtgruDto() {
		return artgruDto;
	}

	@HvDtoLogIgnore
	public ArtklaDto getArtklaDto() {
		return artklaDto;
	}

	public Short getBChargennrtragend() {
		return bChargennrtragend;
	}

	public boolean isChargennrtragend() {
		if (null == bChargennrtragend)
			return false;
		return bChargennrtragend > 0;
	}

	public Short getBSeriennrtragend() {
		return bSeriennrtragend;
	}

	public boolean isSeriennrtragend() {
		if (null == bSeriennrtragend)
			return false;
		return bSeriennrtragend > 0;
	}

	public Integer getHerstellerIId() {
		return herstellerIId;
	}

	@HvDtoLogIdCnr(entityClass = Artkla.class)
	public Integer getArtklaIId() {
		return artklaIId;
	}

	@HvDtoLogIdCnr(entityClass = Artgru.class)
	public Integer getArtgruIId() {
		return artgruIId;
	}

	@HvDtoLogIdCnr(entityClass = Material.class)
	public Integer getMaterialIId() {
		return materialIId;
	}

	@HvDtoLogIgnore
	public HerstellerDto getHerstellerDto() {
		return herstellerDto;
	}

	public Short getBLagerbewertet() {
		return bLagerbewertet;
	}

	public Short getBLagerbewirtschaftet() {
		return bLagerbewirtschaftet;
	}

	public boolean isLagerbewirtschaftet() {
		if (null == bLagerbewirtschaftet)
			return false;
		return bLagerbewirtschaftet > 0;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public Integer getFarbcodeIId() {
		return farbcodeIId;
	}

	public String getEinheitCNrBestellung() {
		return einheitCNrBestellung;
	}

	public BigDecimal getNUmrechnungsfaktor() {
		return nUmrechnungsfaktor;
	}

	public Short getBVersteckt() {
		return bVersteckt;
	}

	public String getCVerpackungseannr() {
		return cVerpackungseannr;
	}

	public Integer getArtikelIIdErsatz() {
		return artikelIIdErsatz;
	}

	public Integer getLandIIdUrsprungsland() {
		return landIIdUrsprungsland;
	}

	public void setIGarantiezeit(Integer iGarantiezeit) {
		this.iGarantiezeit = iGarantiezeit;
	}

	public void setArtikelsprDto(ArtikelsprDto artikelsprDto) {
		this.artikelsprDto = artikelsprDto;
	}

	public void setMontageDto(MontageDto montageDto) {
		this.montageDto = montageDto;
	}

	public void setVerpackungDto(VerpackungDto verpackungDto) {
		this.verpackungDto = verpackungDto;
	}

	public void setGeometrieDto(GeometrieDto geometrieDto) {
		this.geometrieDto = geometrieDto;
	}

	public void setSollverkaufDto(SollverkaufDto sollverkaufDto) {
		this.sollverkaufDto = sollverkaufDto;
	}

	public void setArtgruDto(ArtgruDto artgruDto) {
		this.artgruDto = artgruDto;
	}

	public void setArtklaDto(ArtklaDto artklaDto) {
		this.artklaDto = artklaDto;
	}

	public void setBChargennrtragend(Short bChargennrtragend) {
		this.bChargennrtragend = bChargennrtragend;
	}

	public void setBSeriennrtragend(Short bSeriennrtragend) {
		this.bSeriennrtragend = bSeriennrtragend;
	}

	public void setHerstellerIId(Integer herstellerIId) {
		this.herstellerIId = herstellerIId;
	}

	public void setArtklaIId(Integer artklaIId) {
		this.artklaIId = artklaIId;
	}

	public void setArtgruIId(Integer artgruIId) {
		this.artgruIId = artgruIId;
	}

	public void setMaterialIId(Integer materialIId) {
		this.materialIId = materialIId;
	}

	public void setHerstellerDto(HerstellerDto herstellerDto) {
		this.herstellerDto = herstellerDto;
	}

	public void setBLagerbewertet(Short bLagerbewertet) {
		this.bLagerbewertet = bLagerbewertet;
	}

	public void setBLagerbewirtschaftet(Short bLagerbewirtschaftet) {
		this.bLagerbewirtschaftet = bLagerbewirtschaftet;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public void setFarbcodeIId(Integer farbcodeIId) {
		this.farbcodeIId = farbcodeIId;
	}

	private Integer vorzugIId;

	public Integer getVorzugIId() {
		return vorzugIId;
	}

	public void setVorzugIId(Integer vorzugIId) {
		this.vorzugIId = vorzugIId;
	}

	public void setEinheitCNrBestellung(String einheitCNrBestellung) {
		this.einheitCNrBestellung = einheitCNrBestellung;
	}

	public void setNUmrechnungsfaktor(BigDecimal nUmrechnungsfaktor) {
		this.nUmrechnungsfaktor = nUmrechnungsfaktor;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	public void setCVerpackungseannr(String cVerpackungseannr) {
		this.cVerpackungseannr = cVerpackungseannr;
	}

	public void setArtikelIIdErsatz(Integer artikelIIdErsatz) {
		this.artikelIIdErsatz = artikelIIdErsatz;
	}

	public void setLandIIdUrsprungsland(Integer landIIdUrsprungsland) {
		this.landIIdUrsprungsland = landIIdUrsprungsland;
	}

	private String cUL;
	private Integer reachIId;
	private Integer rohsIId;
	private Integer automotiveIId;
	private Integer medicalIId;

	public String getCUL() {
		return cUL;
	}

	public void setCUL(String cUL) {
		this.cUL = cUL;
	}

	public Integer getReachIId() {
		return reachIId;
	}

	public void setReachIId(Integer reachIId) {
		this.reachIId = reachIId;
	}

	public Integer getRohsIId() {
		return rohsIId;
	}

	public void setRohsIId(Integer rohsIId) {
		this.rohsIId = rohsIId;
	}

	public Integer getAutomotiveIId() {
		return automotiveIId;
	}

	public void setAutomotiveIId(Integer automotiveIId) {
		this.automotiveIId = automotiveIId;
	}

	public Integer getMedicalIId() {
		return medicalIId;
	}

	public void setMedicalIId(Integer medicalIId) {
		this.medicalIId = medicalIId;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ArtikelDto)) {
			return false;
		}
		ArtikelDto that = (ArtikelDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.cNr == null ? this.cNr == null : that.cNr.equals(this.cNr))) {
			return false;
		}
		if (!(that.herstellerIId == null ? this.herstellerIId == null
				: that.herstellerIId.equals(this.herstellerIId))) {
			return false;
		}
		if (!(that.cArtikelbezhersteller == null ? this.cArtikelbezhersteller == null
				: that.cArtikelbezhersteller.equals(this.cArtikelbezhersteller))) {
			return false;
		}
		if (!(that.cArtikelnrhersteller == null ? this.cArtikelnrhersteller == null
				: that.cArtikelnrhersteller.equals(this.cArtikelnrhersteller))) {
			return false;
		}
		if (!(that.artgruIId == null ? this.artgruIId == null : that.artgruIId
				.equals(this.artgruIId))) {
			return false;
		}
		if (!(that.artklaIId == null ? this.artklaIId == null : that.artklaIId
				.equals(this.artklaIId))) {
			return false;
		}
		if (!(that.artikelartCNr == null ? this.artikelartCNr == null
				: that.artikelartCNr.equals(this.artikelartCNr))) {
			return false;
		}
		if (!(that.einheitCNr == null ? this.einheitCNr == null
				: that.einheitCNr.equals(this.einheitCNr))) {
			return false;
		}
		if (!(that.bSeriennrtragend == null ? this.bSeriennrtragend == null
				: that.bSeriennrtragend.equals(this.bSeriennrtragend))) {
			return false;
		}
		if (!(that.cReferenznr == null ? this.cReferenznr == null
				: that.cReferenznr.equals(this.cReferenznr))) {
			return false;
		}
		if (!(that.fLagermindest == null ? this.fLagermindest == null
				: that.fLagermindest.equals(this.fLagermindest))) {
			return false;
		}
		if (!(that.fLagersoll == null ? this.fLagersoll == null
				: that.fLagersoll.equals(this.fLagersoll))) {
			return false;
		}
		if (!(that.fVerpackungsmenge == null ? this.fVerpackungsmenge == null
				: that.fVerpackungsmenge.equals(this.fVerpackungsmenge))) {
			return false;
		}
		if (!(that.fVerschnittfaktor == null ? this.fVerschnittfaktor == null
				: that.fVerschnittfaktor.equals(this.fVerschnittfaktor))) {
			return false;
		}
		if (!(that.fVerschnittbasis == null ? this.fVerschnittbasis == null
				: that.fVerschnittbasis.equals(this.fVerschnittbasis))) {
			return false;
		}
		if (!(that.fJahresmenge == null ? this.fJahresmenge == null
				: that.fJahresmenge.equals(this.fJahresmenge))) {
			return false;
		}
		if (!(that.mwstsatzbezIId == null ? this.mwstsatzbezIId == null
				: that.mwstsatzbezIId.equals(this.mwstsatzbezIId))) {
			return false;
		}
		if (!(that.materialIId == null ? this.materialIId == null
				: that.materialIId.equals(this.materialIId))) {
			return false;
		}
		if (!(that.fGewichtkg == null ? this.fGewichtkg == null
				: that.fGewichtkg.equals(this.fGewichtkg))) {
			return false;
		}
		if (!(that.fMaterialgewicht == null ? this.fMaterialgewicht == null
				: that.fMaterialgewicht.equals(this.fMaterialgewicht))) {
			return false;
		}
		if (!(that.bAntistatic == null ? this.bAntistatic == null
				: that.bAntistatic.equals(this.bAntistatic))) {
			return false;
		}
		if (!(that.artikelIIdZugehoerig == null ? this.artikelIIdZugehoerig == null
				: that.artikelIIdZugehoerig.equals(this.artikelIIdZugehoerig))) {
			return false;
		}
		if (!(that.fVertreterprovisionmax == null ? this.fVertreterprovisionmax == null
				: that.fVertreterprovisionmax
						.equals(this.fVertreterprovisionmax))) {
			return false;
		}
		if (!(that.fMinutenfaktor1 == null ? this.fMinutenfaktor1 == null
				: that.fMinutenfaktor1.equals(this.fMinutenfaktor1))) {
			return false;
		}
		if (!(that.fMinutenfaktor2 == null ? this.fMinutenfaktor2 == null
				: that.fMinutenfaktor2.equals(this.fMinutenfaktor2))) {
			return false;
		}
		if (!(that.fMindestdeckungsbeitrag == null ? this.fMindestdeckungsbeitrag == null
				: that.fMindestdeckungsbeitrag
						.equals(this.fMindestdeckungsbeitrag))) {
			return false;
		}
		if (!(that.cVerkaufseannr == null ? this.cVerkaufseannr == null
				: that.cVerkaufseannr.equals(this.cVerkaufseannr))) {
			return false;
		}
		if (!(that.cWarenverkehrsnummer == null ? this.cWarenverkehrsnummer == null
				: that.cWarenverkehrsnummer.equals(this.cWarenverkehrsnummer))) {
			return false;
		}
		if (!(that.bRabattierbar == null ? this.bRabattierbar == null
				: that.bRabattierbar.equals(this.bRabattierbar))) {
			return false;
		}
		if (!(that.iGarantiezeit == null ? this.iGarantiezeit == null
				: that.iGarantiezeit.equals(this.iGarantiezeit))) {
			return false;
		}
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen
				.equals(this.tAnlegen))) {
			return false;
		}
		if (!(that.personalIIdAnlegen == null ? this.personalIIdAnlegen == null
				: that.personalIIdAnlegen.equals(this.personalIIdAnlegen))) {
			return false;
		}
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern))) {
			return false;
		}
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern))) {
			return false;
		}
		if (!(that.fFertigungssatzgroesse == null ? this.fFertigungssatzgroesse == null
				: that.fFertigungssatzgroesse
						.equals(this.fFertigungssatzgroesse))) {
			return false;
		}
		return true;
	}

	public boolean istArtikelSnrOderchargentragend() {
		if (Helper.short2boolean(getBSeriennrtragend())
				|| Helper.short2boolean(getBChargennrtragend())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Einzeilige Artikelbezeichnung fuer die UI Darstellung zusammenbauen. <br>
	 * Wird beispielsweise in FLR Listen verwendet.
	 * 
	 * @return String
	 */
	public String formatArtikelbezeichnung() {
		StringBuffer sbBez = new StringBuffer();
		if (getCNr() != null && getArtikelartCNr() != null) {
			if (!getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
				sbBez.append(getCNr());
			}
			if (getArtikelsprDto() != null) {

				if (getArtikelsprDto().getCBez() != null
						&& getArtikelsprDto().getCBez().length() > 0) {
					sbBez.append(" " + getArtikelsprDto().getCBez());
				} else if (getArtikelsprDto().getCZbez() != null
						&& getArtikelsprDto().getCZbez().length() > 0) {
					sbBez.append(" " + getArtikelsprDto().getCZbez());
				}
			}

		}

		return sbBez.toString();
	}

	public String formatArtikelbezeichnungMitZusatzbezeichnung() {
		StringBuffer sbBez = new StringBuffer();
		if (getCNr() != null && getArtikelartCNr() != null) {
			if (!getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
				sbBez.append(getCNr());
			}
			if (getArtikelsprDto() != null) {

				if (getArtikelsprDto().getCBez() != null
						&& getArtikelsprDto().getCBez().length() > 0) {
					sbBez.append(" " + getArtikelsprDto().getCBez());
				}
				if (getArtikelsprDto().getCZbez() != null
						&& getArtikelsprDto().getCZbez().length() > 0) {
					sbBez.append(" " + getArtikelsprDto().getCZbez());
				}
			}

		}

		return sbBez.toString();
	}

	/**
	 * Nur Bezeichnung ohne Artikelnummer zusammenbauen. <br>
	 * Wird beispielsweise in FLR Listen verwendet.
	 * 
	 * @return String
	 */
	public String formatBezeichnung() {
		StringBuffer sbBez = new StringBuffer();
		if (getArtikelsprDto() != null) {
			if (getArtikelsprDto().getCBez() != null
					&& getArtikelsprDto().getCBez().length() > 0) {
				sbBez.append(getArtikelsprDto().getCBez());
				// MR 20080109: ZBez an CBez anhaengen, nur wenn CBez vorhanden
				if (getArtikelsprDto().getCZbez() != null
						&& getArtikelsprDto().getCZbez().length() > 0) {
					sbBez.append(" " + getArtikelsprDto().getCZbez());
				}
			}
		}
		return sbBez.toString();
	}

	@HvDtoLogIgnore
	public String getKbezAusSpr() {

		if (getArtikelsprDto() != null) {
			return getArtikelsprDto().getCKbez();
		} else {
			return null;
		}

	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.cNr.hashCode();
		result = 37 * result + this.herstellerIId.hashCode();
		result = 37 * result + this.cArtikelbezhersteller.hashCode();
		result = 37 * result + this.cArtikelnrhersteller.hashCode();
		result = 37 * result + this.artgruIId.hashCode();
		result = 37 * result + this.artklaIId.hashCode();
		result = 37 * result + this.artikelartCNr.hashCode();
		result = 37 * result + this.einheitCNr.hashCode();
		result = 37 * result + this.bSeriennrtragend.hashCode();
		result = 37 * result + this.cReferenznr.hashCode();
		result = 37 * result + this.fLagermindest.hashCode();
		result = 37 * result + this.fLagersoll.hashCode();
		result = 37 * result + this.fFertigungssatzgroesse.hashCode();
		result = 37 * result + this.fVerpackungsmenge.hashCode();
		result = 37 * result + this.fVerschnittfaktor.hashCode();
		result = 37 * result + this.fVerschnittbasis.hashCode();
		result = 37 * result + this.fJahresmenge.hashCode();
		result = 37 * result + this.mwstsatzbezIId.hashCode();
		result = 37 * result + this.materialIId.hashCode();
		result = 37 * result + this.fGewichtkg.hashCode();
		result = 37 * result + this.fMaterialgewicht.hashCode();
		result = 37 * result + this.bAntistatic.hashCode();
		result = 37 * result + this.artikelIIdZugehoerig.hashCode();
		result = 37 * result + this.fVertreterprovisionmax.hashCode();
		result = 37 * result + this.fMinutenfaktor1.hashCode();
		result = 37 * result + this.fMinutenfaktor2.hashCode();
		result = 37 * result + this.fMindestdeckungsbeitrag.hashCode();
		result = 37 * result + this.cVerkaufseannr.hashCode();
		result = 37 * result + this.cWarenverkehrsnummer.hashCode();
		result = 37 * result + this.bRabattierbar.hashCode();
		result = 37 * result + this.iGarantiezeit.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + cNr;
		returnString += ", " + herstellerIId;
		returnString += ", " + cArtikelbezhersteller;
		returnString += ", " + cArtikelnrhersteller;
		returnString += ", " + artgruIId;
		returnString += ", " + artklaIId;
		returnString += ", " + artikelartCNr;
		returnString += ", " + einheitCNr;
		returnString += ", " + bSeriennrtragend;
		returnString += ", " + cReferenznr;
		returnString += ", " + fLagermindest;
		returnString += ", " + fLagersoll;
		returnString += ", " + fFertigungssatzgroesse;
		returnString += ", " + fVerpackungsmenge;
		returnString += ", " + fVerschnittfaktor;
		returnString += ", " + fVerschnittbasis;
		returnString += ", " + fJahresmenge;
		returnString += ", " + mwstsatzbezIId;
		returnString += ", " + materialIId;
		returnString += ", " + fGewichtkg;
		returnString += ", " + fMaterialgewicht;
		returnString += ", " + bAntistatic;
		returnString += ", " + artikelIIdZugehoerig;
		returnString += ", " + fVertreterprovisionmax;
		returnString += ", " + fMinutenfaktor1;
		returnString += ", " + fMinutenfaktor2;
		returnString += ", " + fMindestdeckungsbeitrag;
		returnString += ", " + cVerkaufseannr;
		returnString += ", " + cWarenverkehrsnummer;
		returnString += ", " + bRabattierbar;
		returnString += ", " + iGarantiezeit;
		returnString += ", " + tAnlegen;
		returnString += ", " + personalIIdAnlegen;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdAendern;
		return returnString;
	}
}

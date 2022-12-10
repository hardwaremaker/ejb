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
import java.sql.Timestamp;

import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.IVersionable;
import com.lp.service.BelegVerkaufDto;

public class AuftragDto extends BelegVerkaufDto implements Serializable, Cloneable, IVersionable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer auftragIIdRahmenauftrag;
	private Integer angebotIId;
	private String cNr;
	private String mandantCNr;
	private String auftragartCNr;
	private String belegartCNr;
	private Integer kundeIIdAuftragsadresse;
	private Integer ansprechpartnerIId;
	private Integer personalIIdVertreter;
	private Integer kundeIIdLieferadresse;
	private Integer kundeIIdRechnungsadresse;
	private String cBezProjektbezeichnung;
	private String cBestellnummer;
	private Timestamp dBestelldatum;
	private String cAuftragswaehrung;
	private Integer verrechenbarIId;
	private Integer iAenderungsauftragVersion;

	private String cKommission;

	public String getCKommission() {
		return cKommission;
	}

	public void setCKommission(String kommission) {
		cKommission = kommission;
	}

	private Integer verrechnungsmodellIId;

	public Integer getVerrechnungsmodellIId() {
		return verrechnungsmodellIId;
	}

	public void setVerrechnungsmodellIId(Integer verrechnungsmodellIId) {
		this.verrechnungsmodellIId = verrechnungsmodellIId;
	}

	public Integer getVerrechenbarIId() {
		return verrechenbarIId;
	}

	public void setVerrechenbarIId(Integer verrechenbarIId) {
		this.verrechenbarIId = verrechenbarIId;
	}

	private BigDecimal nIndexanpassung;

	public BigDecimal getNIndexanpassung() {
		return nIndexanpassung;
	}

	public void setNIndexanpassung(BigDecimal nIndexanpassung) {
		this.nIndexanpassung = nIndexanpassung;
	}

	private java.sql.Timestamp tAuftragsfreigabe;

	private Integer personalIIdAuftragsfreigabe;

	public java.sql.Timestamp getTAuftragsfreigabe() {
		return tAuftragsfreigabe;
	}

	public void setTAuftragsfreigabe(java.sql.Timestamp tAuftragsfreigabe) {
		this.tAuftragsfreigabe = tAuftragsfreigabe;
	}

	public Integer getPersonalIIdAuftragsfreigabe() {
		return personalIIdAuftragsfreigabe;
	}

	public void setPersonalIIdAuftragsfreigabe(Integer personalIIdAuftragsfreigabe) {
		this.personalIIdAuftragsfreigabe = personalIIdAuftragsfreigabe;
	}

	private Integer personalIIdVerrechenbar;
	private Timestamp tVerrechenbar;

	private Timestamp tResponse;
	private Integer personalIIdResponse;

	public Timestamp getTVerrechenbar() {
		return tVerrechenbar;
	}

	public void setTVerrechenbar(Timestamp tVerrechenbar) {
		this.tVerrechenbar = tVerrechenbar;
	}

	public Integer getPersonalIIdVerrechenbar() {
		return personalIIdVerrechenbar;
	}

	public void setPersonalIIdVerrechenbar(Integer personalIIdVerrechenbar) {
		this.personalIIdVerrechenbar = personalIIdVerrechenbar;
	}

	private Integer bestellungIIdAndererMandant;

	public Integer getBestellungIIdAndererMandant() {
		return bestellungIIdAndererMandant;
	}

	public void setBestellungIIdAndererMandant(Integer bestellungIIdAndererMandant) {
		this.bestellungIIdAndererMandant = bestellungIIdAndererMandant;
	}

	private Timestamp tAenderungsauftrag;

	public Timestamp getTAenderungsauftrag() {
		return tAenderungsauftrag;
	}

	public void setTAenderungsauftrag(Timestamp tAenderungsauftrag) {
		this.tAenderungsauftrag = tAenderungsauftrag;
	}

	private BigDecimal nPraemie;

	public BigDecimal getNPraemie() {
		return nPraemie;
	}

	public void setNPraemie(BigDecimal nPraemie) {
		this.nPraemie = nPraemie;
	}

	private Timestamp tLaufterminBis;

	public Timestamp getTLaufterminBis() {
		return tLaufterminBis;
	}

	public void setTLaufterminBis(Timestamp tLaufterminBis) {
		this.tLaufterminBis = tLaufterminBis;
	}

	// wir zur zeit nicht verwendet
	// private Double fSonderrabattsatz;
	// private Double dWechselkursMandantWaehrungZuAuftragswaehrung;
	private Timestamp dLiefertermin;
	private Short bLieferterminUnverbindlich;
	private Timestamp dFinaltermin;
	private Integer kostIId;
	private Short bTeillieferungMoeglich;
	private Short bPoenale;
	private Short bRoHs;
	private Integer iLeihtage;

	private Short bMindermengenzuschlag;

	public Short getBMindermengenzuschlag() {
		return this.bMindermengenzuschlag;
	}

	public void setBMindermengenzuschlag(Short bMindermengenzuschlag) {
		this.bMindermengenzuschlag = bMindermengenzuschlag;
	}

	private Integer iGarantie;
	private BigDecimal nGesamtauftragswertInAuftragswaehrung;
	private BigDecimal nMaterialwertInMandantenwaehrung;
	private BigDecimal nRohdeckungInMandantenwaehrung;
	private BigDecimal nRohdeckungaltInMandantenwaehrung;

	private Timestamp tGedruckt;
	private Integer personalIIdStorniert;
	private Timestamp tStorniert;
	private Integer personalIIdAnlegen;
	private Timestamp tAnlegen;
	private Integer personalIIdAendern;
	private Timestamp tAendern;
	private Integer personalIIdManuellerledigt;
	private Timestamp tManuellerledigt;
	private Integer personalIIdErledigt;
	private Timestamp tErledigt;
	private Integer auftragtextIIdKopftext;
	private String cKopftextUebersteuert;
	private Integer auftragtextIIdFusstext;
	private String cFusstextUebersteuert;
	private String xExternerkommentar;
	private String xInternerkommentar;
	private Timestamp tLauftermin;
	private String wiederholungsintervallCNr;

	private Integer personalIIdVertreter2;

	public Integer getPersonalIIdVertreter2() {
		return personalIIdVertreter2;
	}

	public void setPersonalIIdVertreter2(Integer personalIIdVertreter2) {
		this.personalIIdVertreter2 = personalIIdVertreter2;
	}

	private Timestamp tWunschtermin;
	private String laenderartCnr;

	public Timestamp getTWunschtermin() {
		return tWunschtermin;
	}

	public void setTWunschtermin(Timestamp tWunschtermin) {
		this.tWunschtermin = tWunschtermin;
	}

	private Double fErfuellungsgrad;

	private Integer ansprechpartnerIIdRechnungsadresse;

	public Integer getAnsprechpartnerIIdRechnungsadresse() {
		return ansprechpartnerIIdRechnungsadresse;
	}

	public void setAnsprechpartnerIIdRechnungsadresse(Integer ansprechpartnerIIdRechnungsadresse) {
		this.ansprechpartnerIIdRechnungsadresse = ansprechpartnerIIdRechnungsadresse;
	}

	private Integer lagerIIdAbbuchungslager;

	public Integer getLagerIIdAbbuchungslager() {
		return this.lagerIIdAbbuchungslager;
	}

	public void setLagerIIdAbbuchungslager(Integer lagerIIdAbbuchungslager) {
		this.lagerIIdAbbuchungslager = lagerIIdAbbuchungslager;
	}

	private Timestamp tBegruendung;
	private Integer personalIIdBegruendung;
	private Integer auftragbegruendungIId;

	private Integer ansprechpartnerIIdLieferadresse;

	public Integer getAnsprechpartnerIIdLieferadresse() {
		return ansprechpartnerIIdLieferadresse;
	}

	public void setAnsprechpartnerIIdLieferadresse(Integer ansprechpartnerIIdLieferadresse) {
		this.ansprechpartnerIIdLieferadresse = ansprechpartnerIIdLieferadresse;
	}

	private Short bVersteckt;

	public Short getBVersteckt() {
		return bVersteckt;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	public Timestamp getTBegruendung() {
		return tBegruendung;
	}

	public void setTBegruendung(Timestamp tBegruendung) {
		this.tBegruendung = tBegruendung;
	}

	public Integer getPersonalIIdBegruendung() {
		return personalIIdBegruendung;
	}

	public void setPersonalIIdBegruendung(Integer personalIIdBegruendung) {
		this.personalIIdBegruendung = personalIIdBegruendung;
	}

	public Integer getAuftragbegruendungIId() {
		return auftragbegruendungIId;
	}

	public void setAuftragbegruendungIId(Integer auftragbegruendungIId) {
		this.auftragbegruendungIId = auftragbegruendungIId;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getAuftragIIdRahmenauftrag() {
		return this.auftragIIdRahmenauftrag;
	}

	public void setAuftragIIdRahmenauftrag(Integer auftragIIdRahmenauftragI) {
		this.auftragIIdRahmenauftrag = auftragIIdRahmenauftragI;
	}

	public Integer getAngebotIId() {
		return this.angebotIId;
	}

	public void setAngebotIId(Integer angebotIIdI) {
		this.angebotIId = angebotIIdI;
	}

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getAuftragartCNr() {
		return auftragartCNr;
	}

	public void setAuftragartCNr(String auftragartCNr) {
		this.auftragartCNr = auftragartCNr;
	}

	public String getBelegartCNr() {
		return this.belegartCNr;
	}

	public void setBelegartCNr(String belegartCNr) {
		this.belegartCNr = belegartCNr;
	}

	public Integer getKundeIIdAuftragsadresse() {
		return kundeIIdAuftragsadresse;
	}

	public void setKundeIIdAuftragsadresse(Integer kundeIIdAuftragsadresse) {
		this.kundeIIdAuftragsadresse = kundeIIdAuftragsadresse;
	}

	public Integer getAnsprechparnterIId() {
		return ansprechpartnerIId;
	}

	public void setAnsprechpartnerIId(Integer ansprechpartnerIId) {
		this.ansprechpartnerIId = ansprechpartnerIId;
	}

	public Integer getPersonalIIdVertreter() {
		return personalIIdVertreter;
	}

	public void setPersonalIIdVertreter(Integer personalIIdVertreter) {
		this.personalIIdVertreter = personalIIdVertreter;
	}

	public Integer getKundeIIdLieferadresse() {
		return kundeIIdLieferadresse;
	}

	public void setKundeIIdLieferadresse(Integer kundeIIdLieferadresse) {
		this.kundeIIdLieferadresse = kundeIIdLieferadresse;
	}

	public Integer getKundeIIdRechnungsadresse() {
		return kundeIIdRechnungsadresse;
	}

	public void setKundeIIdRechnungsadresse(Integer kundeIIdRechnungsadresse) {
		this.kundeIIdRechnungsadresse = kundeIIdRechnungsadresse;
	}

	public String getCBezProjektbezeichnung() {
		return cBezProjektbezeichnung;
	}

	public void setCBezProjektbezeichnung(String cBezProjektbezeichnung) {
		this.cBezProjektbezeichnung = cBezProjektbezeichnung;
	}

	public String getCBestellnummer() {
		return cBestellnummer;
	}

	public void setCBestellnummer(String cBestellnummer) {
		this.cBestellnummer = cBestellnummer;
	}

	public Timestamp getDBestelldatum() {
		return dBestelldatum;
	}

	public void setDBestelldatum(Timestamp dBestelldatum) {
		this.dBestelldatum = dBestelldatum;
	}

	public String getCAuftragswaehrung() {
		return cAuftragswaehrung;
	}

	public void setCAuftragswaehrung(String cAuftragswaehrung) {
		this.cAuftragswaehrung = cAuftragswaehrung;
	}

	/*
	 * public Double getDWechselkursMandantWaehrungZuAuftragswaehrung() { return
	 * dWechselkursMandantWaehrungZuAuftragswaehrung; }
	 * 
	 * public void setDWechselkursMandantWaehrungZuAuftragswaehrung(Double kurs) {
	 * dWechselkursMandantWaehrungZuAuftragswaehrung = kurs; }
	 * 
	 * public Double getFSonderrabattsatz() { return fSonderrabattsatz; }
	 * 
	 * public void setFSonderrabattsatz(Double fSonderrabattsatz) {
	 * this.fSonderrabattsatz = fSonderrabattsatz; }
	 */
	public Timestamp getDLiefertermin() {
		return dLiefertermin;
	}

	public void setDLiefertermin(Timestamp dLiefertermin) {
		this.dLiefertermin = dLiefertermin;
	}

	public Integer getKostIId() {
		return this.kostIId;
	}

	public void setKostIId(Integer pKost) {
		this.kostIId = pKost;
	}

	public Short getBLieferterminUnverbindlich() {
		return bLieferterminUnverbindlich;
	}

	public void setBLieferterminUnverbindlich(Short bLieferterminUnverbindlich) {
		this.bLieferterminUnverbindlich = bLieferterminUnverbindlich;
	}

	public Timestamp getDFinaltermin() {
		return dFinaltermin;
	}

	public void setDFinaltermin(Timestamp dFinaltermin) {
		this.dFinaltermin = dFinaltermin;
	}

	public Short getBTeillieferungMoeglich() {
		return bTeillieferungMoeglich;
	}

	public void setBTeillieferungMoeglich(Short bTeillieferungMoeglich) {
		this.bTeillieferungMoeglich = bTeillieferungMoeglich;
	}

	public Short getBPoenale() {
		return bPoenale;
	}

	public void setBPoenale(Short bPoenale) {
		this.bPoenale = bPoenale;
	}

	public Short getBRoHs() {
		return this.bRoHs;
	}

	public void setBRoHs(Short bRoHs) {
		this.bRoHs = bRoHs;
	}

	public Integer getILeihtage() {
		return iLeihtage;
	}

	public void setILeihtage(Integer iLeihtage) {
		this.iLeihtage = iLeihtage;
	}

	private java.sql.Timestamp tFreigabe;

	private Integer personalIIdFreigabe;

	public java.sql.Timestamp getTFreigabe() {
		return tFreigabe;
	}

	public void setTFreigabe(java.sql.Timestamp tFreigabe) {
		this.tFreigabe = tFreigabe;
	}

	public Integer getPersonalIIdFreigabe() {
		return personalIIdFreigabe;
	}

	public void setPersonalIIdFreigabe(Integer personalIIdFreigabe) {
		this.personalIIdFreigabe = personalIIdFreigabe;
	}

	public Double getFVersteckterAufschlag() {
		return fVersteckterAufschlag;
	}

	public void setFVersteckterAufschlag(Double fVersteckterAufschlag) {
		this.fVersteckterAufschlag = fVersteckterAufschlag;
	}

	public Integer getIGarantie() {
		return iGarantie;
	}

	public void setIGarantie(Integer iGarantie) {
		this.iGarantie = iGarantie;
	}

	public BigDecimal getNRohdeckungInMandantenwaehrung() {
		return nRohdeckungInMandantenwaehrung;
	}

	public void setNRohdeckungInMandantenwaehrung(BigDecimal nRohdeckungInMandantenwaehrung) {
		this.nRohdeckungInMandantenwaehrung = nRohdeckungInMandantenwaehrung;
	}

	public BigDecimal getNGesamtauftragswertInAuftragswaehrung() {
		return nGesamtauftragswertInAuftragswaehrung;
	}

	public void setNGesamtauftragswertInAuftragswaehrung(BigDecimal nGesamtauftragswertInAuftragswaehrung) {
		this.nGesamtauftragswertInAuftragswaehrung = nGesamtauftragswertInAuftragswaehrung;
	}

	public BigDecimal getNMaterialwertInMandantenwaehrung() {
		return nMaterialwertInMandantenwaehrung;
	}

	public void setNMaterialwertInMandantenwaehrung(BigDecimal nMaterialwertInMandantenwaehrung) {
		this.nMaterialwertInMandantenwaehrung = nMaterialwertInMandantenwaehrung;
	}

	public Timestamp getTGedruckt() {
		return tGedruckt;
	}

	public void setTGedruckt(Timestamp t) {
		this.tGedruckt = t;
	}

	public Integer getPersonalIIdStorniert() {
		return personalIIdStorniert;
	}

	public void setPersonalIIdStorniert(Integer pIId) {
		this.personalIIdStorniert = pIId;
	}

	public Timestamp getTStorniert() {
		return tStorniert;
	}

	public void setTStorniert(Timestamp t) {
		this.tStorniert = t;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer partnerIIdAnlegen) {
		this.personalIIdAnlegen = partnerIIdAnlegen;
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

	public void setPersonalIIdAendern(Integer partnerIIdAendern) {
		this.personalIIdAendern = partnerIIdAendern;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getPersonalIIdManuellerledigt() {
		return personalIIdManuellerledigt;
	}

	public void setPersonalIIdManuellerledigt(Integer personalIIdManuellerledigt) {
		this.personalIIdManuellerledigt = personalIIdManuellerledigt;
	}

	public Timestamp getTManuellerledigt() {
		return tManuellerledigt;
	}

	public void setTManuellerledigt(Timestamp tManuellerledigt) {
		this.tManuellerledigt = tManuellerledigt;
	}

	public Integer getPersonalIIdErledigt() {
		return personalIIdErledigt;
	}

	public void setPersonalIIdErledigt(Integer personalIIdErledigt) {
		this.personalIIdErledigt = personalIIdErledigt;
	}

	public Timestamp getTErledigt() {
		return tErledigt;
	}

	public void setTErledigt(Timestamp tErledigt) {
		this.tErledigt = tErledigt;
	}

	public String getCKopftextUebersteuert() {
		return cKopftextUebersteuert;
	}

	public void setCKopftextUebersteuert(String cKopftextUebersteuert) {
		this.cKopftextUebersteuert = cKopftextUebersteuert;
	}

	public String getCFusstextUebersteuert() {
		return cFusstextUebersteuert;
	}

	public void setCFusstextUebersteuert(String cFusstextUebersteuert) {
		this.cFusstextUebersteuert = cFusstextUebersteuert;
	}

	public Integer getAuftragIIdKopftext() {
		return auftragtextIIdKopftext;
	}

	public void setAuftragtextIIdKopftext(Integer auftragtextIIdKopftext) {
		this.auftragtextIIdKopftext = auftragtextIIdKopftext;
	}

	public Integer getAuftragIIdFusstext() {
		return auftragtextIIdFusstext;
	}

	public void setAuftragtextIIdFusstext(Integer auftragtextIIdFusstext) {
		this.auftragtextIIdFusstext = auftragtextIIdFusstext;
	}

	public String getXExternerkommentar() {
		return xExternerkommentar;
	}

	public void setXExternerkommentar(String xExternerkommentar) {
		this.xExternerkommentar = xExternerkommentar;
	}

	public String getXInternerkommentar() {
		return this.xInternerkommentar;
	}

	public Timestamp getTLauftermin() {
		return tLauftermin;
	}

	public String getWiederholungsintervallCNr() {
		return wiederholungsintervallCNr;
	}

	public Double getFErfuellungsgrad() {
		return fErfuellungsgrad;
	}

	public void setXInternerkommentar(String xInternerkommentar) {
		this.xInternerkommentar = xInternerkommentar;
	}

	public void setTLauftermin(Timestamp tLauftermin) {
		this.tLauftermin = tLauftermin;
	}

	public void setWiederholungsintervallCNr(String wiederholungsintervallCNr) {
		this.wiederholungsintervallCNr = wiederholungsintervallCNr;
	}

	public void setFErfuellungsgrad(Double fErfuellungsgrad) {
		this.fErfuellungsgrad = fErfuellungsgrad;
	}

	public void setNRohdeckungaltInMandantenwaehrung(BigDecimal nRohdeckungaltInMandantenwaehrung) {
		this.nRohdeckungaltInMandantenwaehrung = nRohdeckungaltInMandantenwaehrung;
	}

	public BigDecimal getNRohdeckungaltInMandantenwaehrung() {
		return nRohdeckungaltInMandantenwaehrung;
	}

	private String cLieferartort;

	public String getCLieferartort() {
		return cLieferartort;
	}

	public void setCLieferartort(String cLieferartort) {
		this.cLieferartort = cLieferartort;
	}

	public Timestamp getTResponse() {
		return tResponse;
	}

	public void setTResponse(Timestamp tResponse) {
		this.tResponse = tResponse;
	}

	public Integer getPersonalIIdResponse() {
		return personalIIdResponse;
	}

	public void setPersonalIIdResponse(Integer personalIIdResponse) {
		this.personalIIdResponse = personalIIdResponse;
	}

	public Integer getIVersion() {
		return iAenderungsauftragVersion;
	}

	public void setIVersion(Integer iVersion) {
		this.iAenderungsauftragVersion = iVersion;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AuftragDto)) {
			return false;
		}
		AuftragDto that = (AuftragDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.cNr == null ? this.cNr == null : that.cNr.equals(this.cNr))) {
			return false;
		}
		if (!(that.mandantCNr == null ? this.mandantCNr == null : that.mandantCNr.equals(this.mandantCNr))) {
			return false;
		}
		if (!(that.auftragartCNr == null ? this.auftragartCNr == null
				: that.auftragartCNr.equals(this.auftragartCNr))) {
			return false;
		}
		if (!(that.belegartCNr == null ? this.belegartCNr == null : that.belegartCNr.equals(this.belegartCNr))) {
			return false;
		}
		if (!(that.kundeIIdAuftragsadresse == null ? this.kundeIIdAuftragsadresse == null
				: that.kundeIIdAuftragsadresse.equals(this.kundeIIdAuftragsadresse))) {
			return false;
		}
		if (!(that.ansprechpartnerIId == null ? this.ansprechpartnerIId == null
				: that.ansprechpartnerIId.equals(this.ansprechpartnerIId))) {
			return false;
		}
		if (!(that.personalIIdVertreter == null ? this.personalIIdVertreter == null
				: that.personalIIdVertreter.equals(this.personalIIdVertreter))) {
			return false;
		}
		if (!(that.kundeIIdLieferadresse == null ? this.kundeIIdLieferadresse == null
				: that.kundeIIdLieferadresse.equals(this.kundeIIdLieferadresse))) {
			return false;
		}
		if (!(that.kundeIIdRechnungsadresse == null ? this.kundeIIdRechnungsadresse == null
				: that.kundeIIdRechnungsadresse.equals(this.kundeIIdRechnungsadresse))) {
			return false;
		}
		if (!(that.cBezProjektbezeichnung == null ? this.cBezProjektbezeichnung == null
				: that.cBezProjektbezeichnung.equals(this.cBezProjektbezeichnung))) {
			return false;
		}
		if (!(that.cBestellnummer == null ? this.cBestellnummer == null
				: that.cBestellnummer.equals(this.cBestellnummer))) {
			return false;
		}
		if (!(that.dBestelldatum == null ? this.dBestelldatum == null
				: that.dBestelldatum.equals(this.dBestelldatum))) {
			return false;
		}
		if (!(that.cAuftragswaehrung == null ? this.cAuftragswaehrung == null
				: that.cAuftragswaehrung.equals(this.cAuftragswaehrung))) {
			return false;
		}
		if (!(that.dLiefertermin == null ? this.dLiefertermin == null
				: that.dLiefertermin.equals(this.dLiefertermin))) {
			return false;
		}
		if (!(that.bLieferterminUnverbindlich == null ? this.bLieferterminUnverbindlich == null
				: that.bLieferterminUnverbindlich.equals(this.bLieferterminUnverbindlich))) {
			return false;
		}
		if (!(that.dFinaltermin == null ? this.dFinaltermin == null : that.dFinaltermin.equals(this.dFinaltermin))) {
			return false;
		}
		if (!(that.kostIId == null ? this.kostIId == null : that.kostIId.equals(this.kostIId))) {
			return false;
		}
		if (!(that.bTeillieferungMoeglich == null ? this.bTeillieferungMoeglich == null
				: that.bTeillieferungMoeglich.equals(this.bTeillieferungMoeglich))) {
			return false;
		}
		if (!(that.bPoenale == null ? this.bPoenale == null : that.bPoenale.equals(this.bPoenale))) {
			return false;
		}
		if (!(that.iLeihtage == null ? this.iLeihtage == null : that.iLeihtage.equals(this.iLeihtage))) {
			return false;
		}
		if (!(that.fVersteckterAufschlag == null ? this.fVersteckterAufschlag == null
				: that.fVersteckterAufschlag.equals(this.fVersteckterAufschlag))) {
			return false;
		}
		if (!(that.fAllgemeinerRabattsatz == null ? this.fAllgemeinerRabattsatz == null
				: that.fAllgemeinerRabattsatz.equals(this.fAllgemeinerRabattsatz))) {
			return false;
		}
		if (!(that.fProjektierungsRabattsatz == null ? this.fProjektierungsRabattsatz == null
				: that.fProjektierungsRabattsatz.equals(this.fProjektierungsRabattsatz))) {
			return false;
		}
		if (!(that.lieferartIId == null ? this.lieferartIId == null : that.lieferartIId.equals(this.lieferartIId))) {
			return false;
		}
		if (!(that.zahlungszielIId == null ? this.zahlungszielIId == null
				: that.zahlungszielIId.equals(this.zahlungszielIId))) {
			return false;
		}
		if (!(that.spediteurIId == null ? this.spediteurIId == null : that.spediteurIId.equals(this.spediteurIId))) {
			return false;
		}
		if (!(that.iGarantie == null ? this.iGarantie == null : that.iGarantie.equals(this.iGarantie))) {
			return false;
		}
		if (!(that.nRohdeckungInMandantenwaehrung == null ? this.nRohdeckungInMandantenwaehrung == null
				: that.nRohdeckungInMandantenwaehrung.equals(this.nRohdeckungInMandantenwaehrung))) {
			return false;
		}
		if (!(that.nGesamtauftragswertInAuftragswaehrung == null ? this.nGesamtauftragswertInAuftragswaehrung == null
				: that.nGesamtauftragswertInAuftragswaehrung.equals(this.nGesamtauftragswertInAuftragswaehrung))) {
			return false;
		}
		if (!(that.nMaterialwertInMandantenwaehrung == null ? this.nMaterialwertInMandantenwaehrung == null
				: that.nMaterialwertInMandantenwaehrung.equals(this.nMaterialwertInMandantenwaehrung))) {
			return false;
		}
		if (!(that.statusCNr == null ? this.statusCNr == null : that.statusCNr.equals(this.statusCNr))) {
			return false;
		}
		if (!(that.tBelegdatum == null ? this.tBelegdatum == null : that.tBelegdatum.equals(this.tBelegdatum))) {
			return false;
		}
		if (!(that.tGedruckt == null ? this.tGedruckt == null : that.tGedruckt.equals(this.tGedruckt))) {
			return false;
		}
		if (!(that.tErledigt == null ? this.tErledigt == null : that.tErledigt.equals(this.tErledigt))) {
			return false;
		}
		if (!(that.personalIIdErledigt == null ? this.personalIIdErledigt == null
				: that.personalIIdErledigt.equals(this.personalIIdErledigt))) {
			return false;
		}
		if (!(that.personalIIdStorniert == null ? this.personalIIdStorniert == null
				: that.personalIIdStorniert.equals(this.personalIIdStorniert))) {
			return false;
		}
		if (!(that.tStorniert == null ? this.tStorniert == null : that.tStorniert.equals(this.tStorniert))) {
			return false;
		}
		if (!(that.personalIIdAnlegen == null ? this.personalIIdAnlegen == null
				: that.personalIIdAnlegen.equals(this.personalIIdAnlegen))) {
			return false;
		}
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen.equals(this.tAnlegen))) {
			return false;
		}
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern))) {
			return false;
		}

		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern.equals(this.tAendern))) {
			return false;
		}
		if (!(that.cKopftextUebersteuert == null ? this.cKopftextUebersteuert == null
				: that.cKopftextUebersteuert.equals(this.cKopftextUebersteuert))) {
			return false;
		}
		if (!(that.cFusstextUebersteuert == null ? this.cFusstextUebersteuert == null
				: that.cFusstextUebersteuert.equals(this.cFusstextUebersteuert))) {
			return false;
		}
		if (!(that.tLauftermin == null ? this.tLauftermin == null : that.tLauftermin.equals(this.tLauftermin))) {
			return false;
		}
		if (!(that.wiederholungsintervallCNr == null ? this.wiederholungsintervallCNr == null
				: that.wiederholungsintervallCNr.equals(this.wiederholungsintervallCNr))) {
			return false;
		}
		if (!(that.fErfuellungsgrad == null ? this.fErfuellungsgrad == null
				: that.fErfuellungsgrad.equals(this.fErfuellungsgrad))) {
			return false;
		}
		if (!(that.getIVersion() == null ? getIVersion() == null : that.getIVersion().equals(getIVersion()))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.cNr.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.auftragartCNr.hashCode();
		result = 37 * result + this.belegartCNr.hashCode();
		result = 37 * result + this.kundeIIdAuftragsadresse.hashCode();
		result = 37 * result + this.ansprechpartnerIId.hashCode();
		result = 37 * result + this.personalIIdVertreter.hashCode();
		result = 37 * result + this.kundeIIdLieferadresse.hashCode();
		result = 37 * result + this.kundeIIdRechnungsadresse.hashCode();
		result = 37 * result + this.cBezProjektbezeichnung.hashCode();
		result = 37 * result + this.cBestellnummer.hashCode();
		result = 37 * result + this.dBestelldatum.hashCode();
		result = 37 * result + this.cAuftragswaehrung.hashCode();
		result = 37 * result + this.fWechselkursmandantwaehrungzubelegwaehrung.hashCode();
		// result = 37 * result + this.fSonderrabattsatz.hashCode();
		result = 37 * result + this.dLiefertermin.hashCode();
		result = 37 * result + this.bLieferterminUnverbindlich.hashCode();
		result = 37 * result + this.dFinaltermin.hashCode();
		result = 37 * result + this.kostIId.hashCode();
		result = 37 * result + this.bTeillieferungMoeglich.hashCode();
		result = 37 * result + this.bPoenale.hashCode();
		result = 37 * result + this.iLeihtage.hashCode();
		result = 37 * result + this.fVersteckterAufschlag.hashCode();
		result = 37 * result + this.fAllgemeinerRabattsatz.hashCode();
		result = 37 * result + this.fProjektierungsRabattsatz.hashCode();
		result = 37 * result + this.lieferartIId.hashCode();
		result = 37 * result + this.zahlungszielIId.hashCode();
		result = 37 * result + this.spediteurIId.hashCode();
		result = 37 * result + this.iGarantie.hashCode();
		result = 37 * result + this.nGesamtauftragswertInAuftragswaehrung.hashCode();
		result = 37 * result + this.nRohdeckungInMandantenwaehrung.hashCode();
		result = 37 * result + this.nMaterialwertInMandantenwaehrung.hashCode();
		result = 37 * result + this.statusCNr.hashCode();
		result = 37 * result + this.tBelegdatum.hashCode();
		result = 37 * result + this.tGedruckt.hashCode();
		result = 37 * result + this.personalIIdStorniert.hashCode();
		result = 37 * result + this.tStorniert.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.cKopftextUebersteuert.hashCode();
		result = 37 * result + this.cFusstextUebersteuert.hashCode();
		result = 37 * result + this.tErledigt.hashCode();
		result = 37 * result + this.personalIIdErledigt.hashCode();
		result = 37 * result + this.tLauftermin.hashCode();
		result = 37 * result + this.wiederholungsintervallCNr.hashCode();
		result = 37 * result + this.fErfuellungsgrad.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + cNr;
		returnString += ", " + mandantCNr;
		returnString += ", " + auftragartCNr;
		returnString += ", " + belegartCNr;
		returnString += ", " + kundeIIdAuftragsadresse;
		returnString += ", " + ansprechpartnerIId;
		returnString += ", " + personalIIdVertreter;
		returnString += ", " + kundeIIdLieferadresse;
		returnString += ", " + kundeIIdRechnungsadresse;
		returnString += ", " + cBezProjektbezeichnung;
		returnString += ", " + cBestellnummer;
		returnString += ", " + dBestelldatum;
		returnString += ", " + cAuftragswaehrung;
		returnString += ", " + fWechselkursmandantwaehrungzubelegwaehrung;
		// returnString += ", " + fSonderrabattsatz;
		returnString += ", " + dLiefertermin;
		returnString += ", " + bLieferterminUnverbindlich;
		returnString += ", " + dFinaltermin;
		returnString += ", " + kostIId;
		returnString += ", " + bTeillieferungMoeglich;
		returnString += ", " + bPoenale;
		returnString += ", " + iLeihtage;
		returnString += ", " + fVersteckterAufschlag;
		returnString += ", " + fAllgemeinerRabattsatz;
		returnString += ", " + fProjektierungsRabattsatz;
		returnString += ", " + lieferartIId;
		returnString += ", " + zahlungszielIId;
		returnString += ", " + spediteurIId;
		returnString += ", " + iGarantie;
		returnString += ", " + nGesamtauftragswertInAuftragswaehrung;
		returnString += ", " + nRohdeckungInMandantenwaehrung;
		returnString += ", " + nMaterialwertInMandantenwaehrung;
		returnString += ", " + statusCNr;
		returnString += ", " + tBelegdatum;
		returnString += ", " + tGedruckt;
		returnString += ", " + personalIIdStorniert;
		returnString += ", " + tStorniert;
		returnString += ", " + personalIIdAnlegen;
		returnString += ", " + tAnlegen;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + tAendern;
		returnString += ", " + cKopftextUebersteuert;
		returnString += ", " + cFusstextUebersteuert;
		returnString += ", " + tErledigt;
		returnString += ", " + personalIIdErledigt;
		returnString += ", " + tLauftermin;
		returnString += ", " + wiederholungsintervallCNr;
		returnString += ", " + fErfuellungsgrad;
		return returnString;
	}

	public Object clone() {
		AuftragDto auftragDto = new AuftragDto();

		// iId, cNr, angebotIId null
		auftragDto.auftragIIdRahmenauftrag = this.auftragIIdRahmenauftrag;
		auftragDto.mandantCNr = this.mandantCNr;
		auftragDto.auftragartCNr = this.auftragartCNr;
		auftragDto.belegartCNr = this.belegartCNr;
		auftragDto.kundeIIdAuftragsadresse = this.kundeIIdAuftragsadresse;
		auftragDto.ansprechpartnerIId = this.ansprechpartnerIId;
		auftragDto.personalIIdVertreter = this.personalIIdVertreter;
		auftragDto.kundeIIdLieferadresse = this.kundeIIdLieferadresse;
		auftragDto.kundeIIdRechnungsadresse = this.kundeIIdRechnungsadresse;
		auftragDto.cBezProjektbezeichnung = this.cBezProjektbezeichnung;
		auftragDto.cBestellnummer = this.cBestellnummer;
		auftragDto.dBestelldatum = this.dBestelldatum;
		auftragDto.cAuftragswaehrung = this.cAuftragswaehrung;
		auftragDto.fWechselkursmandantwaehrungzubelegwaehrung = this.fWechselkursmandantwaehrungzubelegwaehrung;
		// auftragDto.fSonderrabattsatz = this.fSonderrabattsatz;
		auftragDto.dLiefertermin = this.dLiefertermin;
		auftragDto.bLieferterminUnverbindlich = this.bLieferterminUnverbindlich;
		auftragDto.dFinaltermin = this.dFinaltermin;
		auftragDto.kostIId = this.kostIId;
		auftragDto.projektIId = this.projektIId;
		auftragDto.bTeillieferungMoeglich = this.bTeillieferungMoeglich;
		auftragDto.bPoenale = this.bPoenale;
		auftragDto.bRoHs = this.bRoHs;
		auftragDto.bVersteckt = this.bVersteckt;
		auftragDto.iLeihtage = this.iLeihtage;
		auftragDto.fVersteckterAufschlag = this.fVersteckterAufschlag;
		auftragDto.fAllgemeinerRabattsatz = this.fAllgemeinerRabattsatz;
		auftragDto.fProjektierungsRabattsatz = this.fProjektierungsRabattsatz;
		auftragDto.lieferartIId = this.lieferartIId;
		auftragDto.cLieferartort = this.cLieferartort;
		auftragDto.zahlungszielIId = this.zahlungszielIId;
		auftragDto.spediteurIId = this.spediteurIId;
		auftragDto.iGarantie = this.iGarantie;
		auftragDto.statusCNr = AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT; // initial
		// ist
		// der
		// Auftrag
		// immer
		// angelegt
		auftragDto.tErledigt = null;
		auftragDto.personalIIdErledigt = null;
		auftragDto.tAuftragsfreigabe = null;
		auftragDto.personalIIdAuftragsfreigabe = null;
		auftragDto.tBelegdatum = new Timestamp(System.currentTimeMillis()); // jetzt

		// !
		// Auftragswerte null
		// tGedruckt, Anlegen, Aendern, Stornieren, Manuell erledigen null
		// Kopftext, Fusstext null
		// IMS 1775 spezieller Kopf- und Fusstext sollen uebernommen werden
		auftragDto.setCKopftextUebersteuert(getCKopftextUebersteuert());
		auftragDto.setCFusstextUebersteuert(getCFusstextUebersteuert());
		auftragDto.setXExternerkommentar(getXExternerkommentar());
		auftragDto.setXInternerkommentar(getXInternerkommentar());
		auftragDto.setBMitzusammenfassung(getBMitzusammenfassung());
		auftragDto.setBMindermengenzuschlag(getBMindermengenzuschlag());
		auftragDto.setLagerIIdAbbuchungslager(getLagerIIdAbbuchungslager());
		auftragDto.setVerrechenbarIId(getVerrechenbarIId());
		auftragDto.setVerrechnungsmodellIId(getVerrechnungsmodellIId());

		// SP8608
		auftragDto.tLauftermin = new Timestamp(System.currentTimeMillis());
		auftragDto.wiederholungsintervallCNr = this.getWiederholungsintervallCNr();

		return auftragDto;
	}

	public LieferscheinDto cloneAsLieferscheinDto() {
		LieferscheinDto lieferscheinDto = new LieferscheinDto();

		// iId, rechnungIId, cNr, angebotIId null
		lieferscheinDto.setMandantCNr(getMandantCNr());
		lieferscheinDto.setLieferscheinartCNr(LieferscheinFac.LSART_AUFTRAG);
		lieferscheinDto.setStatusCNr(LieferscheinFac.LSSTATUS_ANGELEGT);
		lieferscheinDto.setBelegartCNr(LocaleFac.BELEGART_LIEFERSCHEIN);
		lieferscheinDto.setBVerrechenbar(new Short((short) 1));
		lieferscheinDto.setTBelegdatum(new Timestamp(System.currentTimeMillis())); // jetzt
		// !
		lieferscheinDto.setKundeIIdLieferadresse(kundeIIdLieferadresse);

		if (getKundeIIdAuftragsadresse().equals(getKundeIIdLieferadresse())) {
			// nur dann kann der Ansprechpartner uebernommen werden
			lieferscheinDto.setAnsprechpartnerIId(getAnsprechparnterIId());
		}

		if (getAnsprechpartnerIIdLieferadresse() != null) {
			lieferscheinDto.setAnsprechpartnerIId(getAnsprechpartnerIIdLieferadresse());
		}

		lieferscheinDto.setKundeIIdRechnungsadresse(getKundeIIdRechnungsadresse());
		lieferscheinDto.setAnsprechpartnerIIdRechnungsadresse(getAnsprechpartnerIIdRechnungsadresse());
		lieferscheinDto.setPersonalIIdVertreter(personalIIdVertreter);
		lieferscheinDto.setCBestellnummer(getCBestellnummer());
		lieferscheinDto.setCKommission(getCKommission());
		lieferscheinDto.setCBezProjektbezeichnung(getCBezProjektbezeichnung());
		lieferscheinDto.setProjektIId(getProjektIId());
		// lagerIID am Client setzen
		lieferscheinDto.setKostenstelleIId(getKostIId());
		lieferscheinDto.setTLiefertermin(getDLiefertermin());
		// rueckgabetermin null
		lieferscheinDto.setFVersteckterAufschlag(getFVersteckterAufschlag());
		// MB IMS 1532 da der LS keinen Projektrabatt hat, werden fuer den LS
		// allgemeiner Rabatt und Projektrabatt zusammengefasst
		Double dRabattsatz = new Double(100.0 - ((1.0 - (getFAllgemeinerRabattsatz().doubleValue() / 100.0))
				* (1.0 - (getFProjektierungsrabattsatz().doubleValue() / 100.0)) * 100.0));
		// lieferscheinDto.setFAllgemeinerRabatt(getFAllgemeinerRabattsatz());
		lieferscheinDto.setFAllgemeinerRabattsatz(dRabattsatz);
		lieferscheinDto.setIAnzahlPakete(new Integer(0));
		lieferscheinDto.setFGewichtLieferung(new Double(0));

		lieferscheinDto.setLieferartIId(getLieferartIId());
		lieferscheinDto.setCLieferartort(getCLieferartort());
		lieferscheinDto.setZahlungszielIId(getZahlungszielIId());
		lieferscheinDto.setSpediteurIId(getSpediteurIId());

		lieferscheinDto.setWaehrungCNr(getCAuftragswaehrung());
		lieferscheinDto.setLaenderartCnr(getLaenderartCnr());
		// wechselkurs, kopftext, fusstext am Client bestimmen
		// der Rest null

		return lieferscheinDto;
	}

	public AngebotDto cloneAsAngebotDto() {
		AngebotDto angebotDto = new AngebotDto();

		// iId, rechnungIId, cNr, angebotIId null
		angebotDto.setMandantCNr(getMandantCNr());
		angebotDto.setStatusCNr(AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT);
		angebotDto.setArtCNr(AngebotServiceFac.ANGEBOTART_FREI);
		angebotDto.setAngeboteinheitCNr(SystemFac.EINHEIT_WOCHE);
		angebotDto.setILieferzeitinstunden(0);
		angebotDto.setBelegartCNr(LocaleFac.BELEGART_ANGEBOT);

		angebotDto.setTBelegdatum(new Timestamp(System.currentTimeMillis())); // jetzt
		// !
		angebotDto.setKundeIIdAngebotsadresse(kundeIIdAuftragsadresse);
		angebotDto.setKundeIIdLieferadresse(kundeIIdLieferadresse);
		angebotDto.setKundeIIdRechnungsadresse(kundeIIdRechnungsadresse);

		// nun dann kann der Ansprechpartner uebernommen werden
		angebotDto.setAnsprechpartnerIIdKunde(getAnsprechparnterIId());
		angebotDto.setAnsprechpartnerIIdLieferadresse(getAnsprechpartnerIIdLieferadresse());
		angebotDto.setAnsprechpartnerIIdRechnungsadresse(getAnsprechpartnerIIdRechnungsadresse());

		angebotDto.setPersonalIIdVertreter(personalIIdVertreter);
		angebotDto.setPersonalIIdVertreter2(personalIIdVertreter2);

		angebotDto.setFAuftragswahrscheinlichkeit(0D);
		angebotDto.setIGarantie(0);

		angebotDto.setCBez(getCBezProjektbezeichnung());
		angebotDto.setProjektIId(getProjektIId());
		// lagerIID am Client setzen
		angebotDto.setKostenstelleIId(getKostIId());

		// rueckgabetermin null
		angebotDto.setFVersteckterAufschlag(getFVersteckterAufschlag());
		// MB IMS 1532 da der LS keinen Projektrabatt hat, werden fuer den LS
		// allgemeiner Rabatt und Projektrabatt zusammengefasst
		Double dRabattsatz = new Double(100.0 - ((1.0 - (getFAllgemeinerRabattsatz().doubleValue() / 100.0))
				* (1.0 - (getFProjektierungsrabattsatz().doubleValue() / 100.0)) * 100.0));
		// lieferscheinDto.setFAllgemeinerRabatt(getFAllgemeinerRabattsatz());
		angebotDto.setFAllgemeinerRabattsatz(dRabattsatz);
		// nGesamtwertInLieferscheinwaehrung, nGestehungswertInMandantenwaehrung
		// null

		angebotDto.setLieferartIId(getLieferartIId());
		angebotDto.setZahlungszielIId(getZahlungszielIId());
		angebotDto.setSpediteurIId(getSpediteurIId());

		angebotDto.setBMindermengenzuschlag(getBMindermengenzuschlag());

		angebotDto.setWaehrungCNr(getCAuftragswaehrung());
		// wechselkurs, kopftext, fusstext am Client bestimmen
		// der Rest null

		return angebotDto;
	}

	@Override
	public boolean hasVersion() {
		return getIVersion() != null;
	}

	@Override
	public Timestamp getTVersion() {
		return getTAenderungsauftrag();
	}

	@Override
	public void setTVersion(Timestamp tVersion) {
		setTAenderungsauftrag(tVersion);
	}

	public String getLaenderartCnr() {
		return laenderartCnr;
	}

	public void setLaenderartCnr(String laenderartCnr) {
		this.laenderartCnr = laenderartCnr;
	}
}

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

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;

import com.lp.server.system.service.HvDtoLogClass;
import com.lp.server.util.IIId;

@HvDtoLogClass(name = HvDtoLogClass.LOS, filtername = HvDtoLogClass.LOS)
public class LosDto implements Serializable, IIId {
	/**
	 * 
	 */

	public Integer artikelIIdFuerInterneZwecke = null;

	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String mandantCNr;
	private String cNr;
	private Integer losIIdElternlos;
	private Integer kostenstelleIId;
	private Integer auftragpositionIId;
	private String cKommentar;
	private String cProjekt;
	private Integer stuecklisteIId;
	private BigDecimal nLosgroesse;
	private Integer partnerIIdFertigungsort;
	private Integer personalIIdTechniker;
	private Date tProduktionsende;
	private Date tProduktionsbeginn;
	private Timestamp tAusgabe;
	private Integer personalIIdAusgabe;
	private Timestamp tErledigt;
	private Integer personalIIdErledigt;
	private Timestamp tProduktionsstop;
	private Integer personalIIdProduktionsstop;
	private Timestamp tLeitstandstop;
	private Integer personalIIdLeitstandstop;
	private Integer lagerIIdZiel;
	private String statusCNr;
	private Timestamp tAktualisierungstueckliste;
	private Timestamp tAktualisierungarbeitszeit;
	private Integer personalIIdAnlegen;
	private Timestamp tAnlegen;
	private Integer personalIIdAendern;
	private Timestamp tAendern;
	private Integer personalIIdManuellerledigt;
	private Timestamp tManuellerledigt;
	private String xText;
	private String cZusatznummer;
	private Integer auftragIId;
	private Integer fertigungsgruppeIId;
	private Integer wiederholendeloseIId;
	private Double fBewertung;

	private String cAbposnr;

	public String getCAbposnr() {
		return cAbposnr;
	}

	public void setCAbposnr(String cAbposnr) {
		this.cAbposnr = cAbposnr;
	}

	private String losUnternummerReihenfolgenplanung = null;

	public String getLosUnternummerReihenfolgenplanung() {
		return losUnternummerReihenfolgenplanung;
	}

	public void setLosUnternummerReihenfolgenplanung(String losUnternummerReihenfolgenplanung) {
		this.losUnternummerReihenfolgenplanung = losUnternummerReihenfolgenplanung;
	}

	private Integer forecastpositionIId;
	private Date tNachtraeglichGeoeffnet;
	private Integer personalIIdNachtraeglichGeoeffnet;

	private Integer personalIIdMaterialvollstaendig;

	public Integer getPersonalIIdMaterialvollstaendig() {
		return personalIIdMaterialvollstaendig;
	}

	public void setPersonalIIdMaterialvollstaendig(Integer personalIIdMaterialvollstaendig) {
		this.personalIIdMaterialvollstaendig = personalIIdMaterialvollstaendig;
	}

	public Timestamp getTMaterialvollstaendig() {
		return tMaterialvollstaendig;
	}

	public void setTMaterialvollstaendig(Timestamp tMaterialvollstaendig) {
		this.tMaterialvollstaendig = tMaterialvollstaendig;
	}

	private String cSchachtelplan;

	public String getCSchachtelplan() {
		return cSchachtelplan;
	}

	public void setCSchachtelplan(String cSchachtelplan) {
		this.cSchachtelplan = cSchachtelplan;
	}

	private Integer personalIIdVpEtikettengedruckt;

	public Integer getPersonalIIdVpEtikettengedruckt() {
		return personalIIdVpEtikettengedruckt;
	}

	public void setPersonalIIdVpEtikettengedruckt(Integer personalIIdVpEtikettengedruckt) {
		this.personalIIdVpEtikettengedruckt = personalIIdVpEtikettengedruckt;
	}

	private Timestamp tVpEtikettengedruckt;

	public Timestamp getTVpEtikettengedruckt() {
		return tVpEtikettengedruckt;
	}

	public void setTVpEtikettengedruckt(Timestamp tVpEtikettengedruckt) {
		this.tVpEtikettengedruckt = tVpEtikettengedruckt;
	}

	private Timestamp tMaterialvollstaendig;

	private Integer losbereichIId;

	public Integer getLosbereichIId() {
		return losbereichIId;
	}

	public void setLosbereichIId(Integer losbereichIId) {
		this.losbereichIId = losbereichIId;
	}

	private Integer projektIId;

	public Integer getProjektIId() {
		return projektIId;
	}

	public void setProjektIId(Integer projektIId) {
		this.projektIId = projektIId;
	}

	public Integer getKundeIId() {
		return kundeIId;
	}

	public void setKundeIId(Integer kundeIId) {
		this.kundeIId = kundeIId;
	}

	private Integer kundeIId;

	public String getXProduktionsinformation() {
		return xProduktionsinformation;
	}

	public void setXProduktionsinformation(String produktionsinformation) {
		xProduktionsinformation = produktionsinformation;
	}

	private String xProduktionsinformation;

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

	
	private Integer lagerplatzIId;
	public Integer getLagerplatzIId() {
		return this.lagerplatzIId;
	}


	public void setLagerplatzIId(Integer lagerplatzIId) {
		this.lagerplatzIId = lagerplatzIId;
	}

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public Integer getLosIIdElternlos() {
		return losIIdElternlos;
	}

	public void setLosIIdElternlos(Integer losIIdElternlos) {
		this.losIIdElternlos = losIIdElternlos;
	}

	public Integer getKostenstelleIId() {
		return kostenstelleIId;
	}

	public void setKostenstelleIId(Integer kostenstelleIId) {
		this.kostenstelleIId = kostenstelleIId;
	}

	public Integer getAuftragpositionIId() {
		return auftragpositionIId;
	}

	public void setAuftragpositionIId(Integer auftragpositionIId) {
		this.auftragpositionIId = auftragpositionIId;
	}

	public String getCKommentar() {
		return cKommentar;
	}

	public void setCKommentar(String cKommentar) {
		this.cKommentar = cKommentar;
	}

	public String getCProjekt() {
		return cProjekt;
	}

	public void setCProjekt(String cProjekt) {
		this.cProjekt = cProjekt;
	}

	public Integer getStuecklisteIId() {
		return stuecklisteIId;
	}

	public void setStuecklisteIId(Integer artikelIId) {
		this.stuecklisteIId = artikelIId;
	}

	public BigDecimal getNLosgroesse() {
		return nLosgroesse;
	}

	public void setNLosgroesse(BigDecimal nLosgroesse) {
		this.nLosgroesse = nLosgroesse;
	}

	public Integer getPartnerIIdFertigungsort() {
		return partnerIIdFertigungsort;
	}

	public void setPartnerIIdFertigungsort(Integer partnerIIdFertigungsort) {
		this.partnerIIdFertigungsort = partnerIIdFertigungsort;
	}

	public Integer getPersonalIIdTechniker() {
		return personalIIdTechniker;
	}

	public void setPersonalIIdTechniker(Integer personalIIdTechniker) {
		this.personalIIdTechniker = personalIIdTechniker;
	}

	public Date getTProduktionsende() {
		return tProduktionsende;
	}

	public void setTProduktionsende(Date tProduktionsende) {
		this.tProduktionsende = tProduktionsende;
	}

	public Date getTProduktionsbeginn() {
		return tProduktionsbeginn;
	}

	public void setTProduktionsbeginn(Date tProduktionsbeginn) {
		this.tProduktionsbeginn = tProduktionsbeginn;
	}

	public Timestamp getTAusgabe() {
		return tAusgabe;
	}

	public void setTAusgabe(Timestamp tAusgabe) {
		this.tAusgabe = tAusgabe;
	}

	public Integer getPersonalIIdAusgabe() {
		return personalIIdAusgabe;
	}

	public void setPersonalIIdAusgabe(Integer personalIIdAusgabe) {
		this.personalIIdAusgabe = personalIIdAusgabe;
	}

	public Timestamp getTErledigt() {
		return tErledigt;
	}

	public void setTErledigt(Timestamp tErledigt) {
		this.tErledigt = tErledigt;
	}

	public Integer getPersonalIIdErledigt() {
		return personalIIdErledigt;
	}

	public void setPersonalIIdErledigt(Integer personalIIdErledigt) {
		this.personalIIdErledigt = personalIIdErledigt;
	}

	public Timestamp getTProduktionsstop() {
		return tProduktionsstop;
	}

	public void setTProduktionsstop(Timestamp tProduktionsstop) {
		this.tProduktionsstop = tProduktionsstop;
	}

	public Integer getPersonalIIdProduktionsstop() {
		return personalIIdProduktionsstop;
	}

	public void setPersonalIIdProduktionsstop(Integer personalIIdProduktionsstop) {
		this.personalIIdProduktionsstop = personalIIdProduktionsstop;
	}

	public Timestamp getTLeitstandstop() {
		return tLeitstandstop;
	}

	public void setTLeitstandstop(Timestamp tLeitstandstop) {
		this.tLeitstandstop = tLeitstandstop;
	}

	public Integer getPersonalIIdLeitstandstop() {
		return personalIIdLeitstandstop;
	}

	public void setPersonalIIdLeitstandstop(Integer personalIIdLeitstandstop) {
		this.personalIIdLeitstandstop = personalIIdLeitstandstop;
	}

	public Integer getLagerIIdZiel() {
		return lagerIIdZiel;
	}

	public void setLagerIIdZiel(Integer lagerIIdZiel) {
		this.lagerIIdZiel = lagerIIdZiel;
	}

	public String getStatusCNr() {
		return statusCNr;
	}

	public void setStatusCNr(String statusCNr) {
		this.statusCNr = statusCNr;
	}

	public Timestamp getTAktualisierungstueckliste() {
		return tAktualisierungstueckliste;
	}

	public void setTAktualisierungstueckliste(Timestamp tAktualisierungstueckliste) {
		this.tAktualisierungstueckliste = tAktualisierungstueckliste;
	}

	public Timestamp getTAktualisierungarbeitszeit() {
		return tAktualisierungarbeitszeit;
	}

	public void setTAktualisierungarbeitszeit(Timestamp tAktualisierungarbeitszeit) {
		this.tAktualisierungarbeitszeit = tAktualisierungarbeitszeit;
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

	public String getXText() {
		return xText;
	}

	public void setXText(String xText) {
		this.xText = xText;
	}

	public String getCZusatznummer() {
		return cZusatznummer;
	}

	public void setCZusatznummer(String cZusatznummer) {
		this.cZusatznummer = cZusatznummer;
	}

	public Integer getAuftragIId() {
		return auftragIId;
	}

	public Integer getFertigungsgruppeIId() {
		return fertigungsgruppeIId;
	}

	public Integer getWiederholendeloseIId() {
		return wiederholendeloseIId;
	}

	public Double getFBewertung() {
		return fBewertung;
	}

	public void setAuftragIId(Integer auftragIId) {
		this.auftragIId = auftragIId;
	}

	public void setFertigungsgruppeIId(Integer fertigungsgruppeIId) {
		this.fertigungsgruppeIId = fertigungsgruppeIId;
	}

	public void setWiederholendeloseIId(Integer wiederholendeloseIId) {
		this.wiederholendeloseIId = wiederholendeloseIId;
	}

	public void setFBewertung(Double fBewertung) {
		this.fBewertung = fBewertung;
	}

	private BigDecimal nSollmaterial;

	public BigDecimal getNSollmaterial() {
		return nSollmaterial;
	}

	public void setNSollmaterial(BigDecimal nSollmaterial) {
		this.nSollmaterial = nSollmaterial;
	}

	public Date getTNachtraeglichGeoeffnet() {
		return tNachtraeglichGeoeffnet;
	}

	public void setTNachtraeglichGeoeffnet(Date tNachtraeglichGeoeffnet) {
		this.tNachtraeglichGeoeffnet = tNachtraeglichGeoeffnet;
	}

	public Integer getPersonalIIdNachtraeglichGeoeffnet() {
		return personalIIdNachtraeglichGeoeffnet;
	}

	public void setPersonalIIdNachtraeglichGeoeffnet(Integer personalIIdNachtraeglichGeoeffnet) {
		this.personalIIdNachtraeglichGeoeffnet = personalIIdNachtraeglichGeoeffnet;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof LosDto))
			return false;
		LosDto that = (LosDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.mandantCNr == null ? this.mandantCNr == null : that.mandantCNr.equals(this.mandantCNr)))
			return false;
		if (!(that.cNr == null ? this.cNr == null : that.cNr.equals(this.cNr)))
			return false;
		if (!(that.losIIdElternlos == null ? this.losIIdElternlos == null
				: that.losIIdElternlos.equals(this.losIIdElternlos)))
			return false;
		if (!(that.kostenstelleIId == null ? this.kostenstelleIId == null
				: that.kostenstelleIId.equals(this.kostenstelleIId)))
			return false;
		if (!(that.auftragpositionIId == null ? this.auftragpositionIId == null
				: that.auftragpositionIId.equals(this.auftragpositionIId)))
			return false;
		if (!(that.cKommentar == null ? this.cKommentar == null : that.cKommentar.equals(this.cKommentar)))
			return false;
		if (!(that.cProjekt == null ? this.cProjekt == null : that.cProjekt.equals(this.cProjekt)))
			return false;
		if (!(that.stuecklisteIId == null ? this.stuecklisteIId == null
				: that.stuecklisteIId.equals(this.stuecklisteIId)))
			return false;
		if (!(that.nLosgroesse == null ? this.nLosgroesse == null : that.nLosgroesse.equals(this.nLosgroesse)))
			return false;
		if (!(that.partnerIIdFertigungsort == null ? this.partnerIIdFertigungsort == null
				: that.partnerIIdFertigungsort.equals(this.partnerIIdFertigungsort)))
			return false;
		if (!(that.personalIIdTechniker == null ? this.personalIIdTechniker == null
				: that.personalIIdTechniker.equals(this.personalIIdTechniker)))
			return false;
		if (!(that.tProduktionsende == null ? this.tProduktionsende == null
				: that.tProduktionsende.equals(this.tProduktionsende)))
			return false;
		if (!(that.tProduktionsbeginn == null ? this.tProduktionsbeginn == null
				: that.tProduktionsbeginn.equals(this.tProduktionsbeginn)))
			return false;
		if (!(that.tAusgabe == null ? this.tAusgabe == null : that.tAusgabe.equals(this.tAusgabe)))
			return false;
		if (!(that.personalIIdAusgabe == null ? this.personalIIdAusgabe == null
				: that.personalIIdAusgabe.equals(this.personalIIdAusgabe)))
			return false;
		if (!(that.tErledigt == null ? this.tErledigt == null : that.tErledigt.equals(this.tErledigt)))
			return false;
		if (!(that.personalIIdErledigt == null ? this.personalIIdErledigt == null
				: that.personalIIdErledigt.equals(this.personalIIdErledigt)))
			return false;
		if (!(that.tProduktionsstop == null ? this.tProduktionsstop == null
				: that.tProduktionsstop.equals(this.tProduktionsstop)))
			return false;
		if (!(that.personalIIdProduktionsstop == null ? this.personalIIdProduktionsstop == null
				: that.personalIIdProduktionsstop.equals(this.personalIIdProduktionsstop)))
			return false;
		if (!(that.tLeitstandstop == null ? this.tLeitstandstop == null
				: that.tLeitstandstop.equals(this.tLeitstandstop)))
			return false;
		if (!(that.personalIIdLeitstandstop == null ? this.personalIIdLeitstandstop == null
				: that.personalIIdLeitstandstop.equals(this.personalIIdLeitstandstop)))
			return false;
		if (!(that.lagerIIdZiel == null ? this.lagerIIdZiel == null : that.lagerIIdZiel.equals(this.lagerIIdZiel)))
			return false;
		if (!(that.statusCNr == null ? this.statusCNr == null : that.statusCNr.equals(this.statusCNr)))
			return false;
		if (!(that.tAktualisierungstueckliste == null ? this.tAktualisierungstueckliste == null
				: that.tAktualisierungstueckliste.equals(this.tAktualisierungstueckliste)))
			return false;
		if (!(that.tAktualisierungarbeitszeit == null ? this.tAktualisierungarbeitszeit == null
				: that.tAktualisierungarbeitszeit.equals(this.tAktualisierungarbeitszeit)))
			return false;
		if (!(that.personalIIdAnlegen == null ? this.personalIIdAnlegen == null
				: that.personalIIdAnlegen.equals(this.personalIIdAnlegen)))
			return false;
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen.equals(this.tAnlegen)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern.equals(this.tAendern)))
			return false;
		if (!(that.personalIIdManuellerledigt == null ? this.personalIIdManuellerledigt == null
				: that.personalIIdManuellerledigt.equals(this.personalIIdManuellerledigt)))
			return false;
		if (!(that.tManuellerledigt == null ? this.tManuellerledigt == null
				: that.tManuellerledigt.equals(this.tManuellerledigt)))
			return false;
		if (!(that.xText == null ? this.xText == null : that.xText.equals(this.xText)))
			return false;
		if (!(that.cZusatznummer == null ? this.cZusatznummer == null : that.cZusatznummer.equals(this.cZusatznummer)))
			return false;
		if (!(that.auftragIId == null ? this.auftragIId == null : that.auftragIId.equals(this.auftragIId)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.cNr.hashCode();
		result = 37 * result + this.losIIdElternlos.hashCode();
		result = 37 * result + this.kostenstelleIId.hashCode();
		result = 37 * result + this.auftragpositionIId.hashCode();
		result = 37 * result + this.cKommentar.hashCode();
		result = 37 * result + this.cProjekt.hashCode();
		result = 37 * result + this.stuecklisteIId.hashCode();
		result = 37 * result + this.nLosgroesse.hashCode();
		result = 37 * result + this.partnerIIdFertigungsort.hashCode();
		result = 37 * result + this.personalIIdTechniker.hashCode();
		result = 37 * result + this.tProduktionsende.hashCode();
		result = 37 * result + this.tProduktionsbeginn.hashCode();
		result = 37 * result + this.tAusgabe.hashCode();
		result = 37 * result + this.personalIIdAusgabe.hashCode();
		result = 37 * result + this.tErledigt.hashCode();
		result = 37 * result + this.personalIIdErledigt.hashCode();
		result = 37 * result + this.tProduktionsstop.hashCode();
		result = 37 * result + this.personalIIdProduktionsstop.hashCode();
		result = 37 * result + this.tLeitstandstop.hashCode();
		result = 37 * result + this.personalIIdLeitstandstop.hashCode();
		result = 37 * result + this.lagerIIdZiel.hashCode();
		result = 37 * result + this.statusCNr.hashCode();
		result = 37 * result + this.tAktualisierungstueckliste.hashCode();
		result = 37 * result + this.tAktualisierungarbeitszeit.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdManuellerledigt.hashCode();
		result = 37 * result + this.tManuellerledigt.hashCode();
		result = 37 * result + this.xText.hashCode();
		result = 37 * result + this.cZusatznummer.hashCode();
		result = 37 * result + this.auftragIId.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + mandantCNr;
		returnString += ", " + cNr;
		returnString += ", " + losIIdElternlos;
		returnString += ", " + kostenstelleIId;
		returnString += ", " + auftragpositionIId;
		returnString += ", " + cKommentar;
		returnString += ", " + cProjekt;
		returnString += ", " + stuecklisteIId;
		returnString += ", " + nLosgroesse;
		returnString += ", " + partnerIIdFertigungsort;
		returnString += ", " + personalIIdTechniker;
		returnString += ", " + tProduktionsende;
		returnString += ", " + tProduktionsbeginn;
		returnString += ", " + tAusgabe;
		returnString += ", " + personalIIdAusgabe;
		returnString += ", " + tErledigt;
		returnString += ", " + personalIIdErledigt;
		returnString += ", " + tProduktionsstop;
		returnString += ", " + personalIIdProduktionsstop;
		returnString += ", " + tLeitstandstop;
		returnString += ", " + personalIIdLeitstandstop;
		returnString += ", " + lagerIIdZiel;
		returnString += ", " + statusCNr;
		returnString += ", " + tAktualisierungstueckliste;
		returnString += ", " + tAktualisierungarbeitszeit;
		returnString += ", " + personalIIdAnlegen;
		returnString += ", " + tAnlegen;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdManuellerledigt;
		returnString += ", " + tManuellerledigt;
		returnString += ", " + xText;
		returnString += ", " + cZusatznummer;
		returnString += ", " + auftragIId;
		return returnString;
	}

	public Integer getForecastpositionIId() {
		return forecastpositionIId;
	}

	public void setForecastpositionIId(Integer forecastpositionIId) {
		this.forecastpositionIId = forecastpositionIId;
	}
}

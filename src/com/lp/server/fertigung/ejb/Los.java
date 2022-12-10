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
package com.lp.server.fertigung.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.system.service.ITablenames;
import com.lp.server.util.ICNr;

@NamedQueries( {
		@NamedQuery(name = "LosfindByCNrMandantCNr", query = "SELECT OBJECT (o) FROM Los o WHERE o.cNr=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "LosfindByCProjektMandantCNr", query = "SELECT OBJECT (o) FROM Los o WHERE o.cProjekt=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "LosfindByAuftragIId", query = "SELECT OBJECT (o) FROM Los o WHERE o.auftragIId=?1"),
		@NamedQuery(name = "LosfindByAuftragIIdStuecklisteIId", query = "SELECT OBJECT (o) FROM Los o WHERE o.auftragIId=?1 ANd o.stuecklisteIId=?2"),
		@NamedQuery(name = "LosfindByStuecklisteIId", query = "SELECT OBJECT (o) FROM Los o WHERE o.stuecklisteIId=?1"),
		@NamedQuery(name = "LosfindByAuftragpositionIId", query = "SELECT OBJECT (o) FROM Los o WHERE o.auftragpositionIId=?1"),
		@NamedQuery(name = "LosfindByFertigungsortpartnerIIdMandantCNr", query = "SELECT OBJECT (o) FROM Los o WHERE o.partnerIIdFertigungsort=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "LosfindWiederholendeloseIIdTProduktionsbeginnMandantCNr", query = "SELECT OBJECT (o) FROM Los o WHERE o.wiederholendeloseIId=?1 AND o.tProduktionsbeginn=?2 AND o.mandantCNr=?3"),
		@NamedQuery(name = LosQuery.ByForecastpositionIId, query = "SELECT OBJECT (o) FROM Los o WHERE o.forecastpositionIId=:id"),
		@NamedQuery(name = LosQuery.ByStuecklisteIIdFertigungsgruppeIIdStatusCnr, query = "SELECT OBJECT (o) FROM Los o WHERE o.stuecklisteIId=:id AND o.fertigungsgruppeIId=:fertigungsgruppeId AND o.statusCNr IN (:stati)") })
@Entity
@Table(name = ITablenames.FERT_LOS)
public class Los implements Serializable, ICNr {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "C_KOMMENTAR")
	private String cKommentar;

	@Column(name = "C_ABPOSNR")
	private String cAbposnr;
	
	public String getCAbposnr() {
		return cAbposnr;
	}

	public void setCAbposnr(String cAbposnr) {
		this.cAbposnr = cAbposnr;
	}

	@Column(name = "C_SCHACHTELPLAN")
	private String cSchachtelplan;

	
	public String getCSchachtelplan() {
		return cSchachtelplan;
	}

	public void setCSchachtelplan(String cSchachtelplan) {
		this.cSchachtelplan = cSchachtelplan;
	}

	@Column(name = "LAGERPLATZ_I_ID")
	private Integer lagerplatzIId;
	public Integer getLagerplatzIId() {
		return this.lagerplatzIId;
	}

	public void setLagerplatzIId(Integer lagerplatzIId) {
		this.lagerplatzIId = lagerplatzIId;
	}

	
	
	@Column(name = "C_PROJEKT")
	private String cProjekt;

	@Column(name = "N_LOSGROESSE")
	private BigDecimal nLosgroesse;

	@Column(name = "T_PRODUKTIONSENDE")
	private Date tProduktionsende;
	
	@Column(name = "T_NACHTRAEGLICH_GEOEFFNET")
	private Date tNachtraeglichGeoeffnet;

	@Column(name = "T_PRODUKTIONSBEGINN")
	private Date tProduktionsbeginn;

	@Column(name = "T_AUSGABE")
	private Timestamp tAusgabe;

	@Column(name = "T_ERLEDIGT")
	private Timestamp tErledigt;

	@Column(name = "T_PRODUKTIONSSTOP")
	private Timestamp tProduktionsstop;

	@Column(name = "T_AKTUALISIERUNGSTUECKLISTE")
	private Timestamp tAktualisierungstueckliste;

	@Column(name = "T_AKTUALISIERUNGARBEITSZEIT")
	private Timestamp tAktualisierungarbeitszeit;

	@Column(name = "T_VP_ETIKETTENGEDRUCKT")
	private Timestamp tVpEtikettengedruckt;

	
	public Timestamp getTVpEtikettengedruckt() {
		return tVpEtikettengedruckt;
	}

	public void setTVpEtikettengedruckt(Timestamp tVpEtikettengedruckt) {
		this.tVpEtikettengedruckt = tVpEtikettengedruckt;
	}

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "T_MANUELLERLEDIGT")
	private Timestamp tManuellerledigt;

	@Column(name = "X_TEXT")
	private String xText;

	public String getXProduktionsinformation() {
		return xProduktionsinformation;
	}

	public void setXProduktionsinformation(String produktionsinformation) {
		xProduktionsinformation = produktionsinformation;
	}

	@Column(name = "X_PRODUKTIONSINFORMATION")
	private String xProduktionsinformation;
	
	@Column(name = "C_ZUSATZNUMMER")
	private String cZusatznummer;

	@Column(name = "T_LEITSTANDSTOP")
	private Timestamp tLeitstandstop;

	@Column(name = "F_BEWERTUNG")
	private Double fBewertung;

	@Column(name = "AUFTRAG_I_ID")
	private Integer auftragIId;
	
	@Column(name = "PROJEKT_I_ID")
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

	@Column(name = "KUNDE_I_ID")
	private Integer kundeIId;

	@Column(name = "AUFTRAGPOSITION_I_ID")
	private Integer auftragpositionIId;

	@Column(name = "LOS_I_ID_ELTERNLOS")
	private Integer losIIdElternlos;

	@Column(name = "STATUS_C_NR")
	private String statusCNr;

	@Column(name = "WIEDERHOLENDELOSE_I_ID")
	private Integer wiederholendeloseIId;

	@Column(name = "KOSTENSTELLE_I_ID")
	private Integer kostenstelleIId;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "PARTNER_I_ID_FERTIGUNGSORT")
	private Integer partnerIIdFertigungsort;
	
	@Column(name = "PERSONAL_I_ID_NACHTRAEGLICH_GEOEFFNET")
	private Integer personalIIdNachtraeglichGeoeffnet;

	public Date getTNachtraeglichGeoeffnet() {
		return tNachtraeglichGeoeffnet;
	}

	public void setTNachtraeglichGeoeffnet(Date tNachtraeglichGeoeffnet) {
		this.tNachtraeglichGeoeffnet = tNachtraeglichGeoeffnet;
	}

	public Integer getPersonalIIdNachtraeglichGeoeffnet() {
		return personalIIdNachtraeglichGeoeffnet;
	}

	public void setPersonalIIdNachtraeglichGeoeffnet(
			Integer personalIIdNachtraeglichGeoeffnet) {
		this.personalIIdNachtraeglichGeoeffnet = personalIIdNachtraeglichGeoeffnet;
	}

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "PERSONAL_I_ID_AUSGABE")
	private Integer personalIIdAusgabe;

	@Column(name = "PERSONAL_I_ID_ERLEDIGT")
	private Integer personalIIdErledigt;

	@Column(name = "PERSONAL_I_ID_LEITSTANDSTOP")
	private Integer personalIIdLeitstandstop;

	@Column(name = "PERSONAL_I_ID_MANUELLERLEDIGT")
	private Integer personalIIdManuellerledigt;

	@Column(name = "PERSONAL_I_ID_PRODUKTIONSSTOP")
	private Integer personalIIdProduktionsstop;
	
	@Column(name = "PERSONAL_I_ID_VP_ETIKETTENGEDRUCKT")
	private Integer personalIIdVpEtikettengedruckt;

	public Integer getPersonalIIdVpEtikettengedruckt() {
		return personalIIdVpEtikettengedruckt;
	}

	public void setPersonalIIdVpEtikettengedruckt(
			Integer personalIIdVpEtikettengedruckt) {
		this.personalIIdVpEtikettengedruckt = personalIIdVpEtikettengedruckt;
	}

	@Column(name = "PERSONAL_I_ID_TECHNIKER")
	private Integer personalIIdTechniker;

	@Column(name = "FERTIGUNGSGRUPPE_I_ID")
	private Integer fertigungsgruppeIId;

	@Column(name = "STUECKLISTE_I_ID")
	private Integer stuecklisteIId;

	@Column(name = "FORECASTPOSITION_I_ID")
	private Integer forecastpositionIId;

	public Integer getForecastpositionIId() {
		return forecastpositionIId;
	}

	public void setForecastpositionIId(Integer forecastpositionIId) {
		this.forecastpositionIId = forecastpositionIId;
	}

	
	@Column(name = "PERSONAL_I_ID_MATERIAL_VOLLSTAENDIG")
	private Integer personalIIdMaterialvollstaendig;
	
	public Integer getPersonalIIdMaterialvollstaendig() {
		return personalIIdMaterialvollstaendig;
	}

	public void setPersonalIIdMaterialvollstaendig(
			Integer personalIIdMaterialvollstaendig) {
		this.personalIIdMaterialvollstaendig = personalIIdMaterialvollstaendig;
	}

	public Timestamp getTMaterialvollstaendig() {
		return tMaterialvollstaendig;
	}

	public void setTMaterialvollstaendig(Timestamp tMaterialvollstaendig) {
		this.tMaterialvollstaendig = tMaterialvollstaendig;
	}

	@Column(name = "T_MATERIAL_VOLLSTAENDIG")
	private Timestamp tMaterialvollstaendig;
	
	
	@Column(name = "LOSBEREICH_I_ID")
	private Integer losbereichIId;

	public Integer getLosbereichIId() {
		return losbereichIId;
	}

	public void setLosbereichIId(Integer losbereichIId) {
		this.losbereichIId = losbereichIId;
	}

	@Column(name = "LAGER_I_ID_ZIEL")
	private Integer lagerIIdZiel;

	@Column(name = "N_SOLLMATERIAL")
	private BigDecimal nSollmaterial;
	
	public BigDecimal getNSollmaterial() {
		return nSollmaterial;
	}

	public void setNSollmaterial(BigDecimal nSollmaterial) {
		this.nSollmaterial = nSollmaterial;
	}

	private static final long serialVersionUID = 1L;

	public Los() {
		super();
	}

	public Los(Integer id, String mandantCNr2, String nr,
			Integer kostenstelleIId2, BigDecimal losgroesse,
			Date produktionsende, Date produktionsbeginn,
			Integer lagerIIdZiel2, String statusCNr2,
			Integer personalIIdAnlegen2, Integer personalIIdAendern2,
			Integer fertigungsgruppeIId2) {
		setIId(id);
		setMandantCNr(mandantCNr2);
		setCNr(nr);
		setKostenstelleIId(kostenstelleIId2);
		setNLosgroesse(losgroesse);
		setTProduktionsende(produktionsende);
		setTProduktionsbeginn(produktionsbeginn);
		setLagerIIdZiel(lagerIIdZiel2);
		setStatusCNr(statusCNr2);
		setPersonalIIdAnlegen(personalIIdAnlegen2);
		setPersonalIIdAendern(personalIIdAendern2);
		// Setzen der NOT NULL felder
	    Timestamp now = new Timestamp(System.currentTimeMillis());
	    this.setTAendern(now);
	    this.setTAnlegen(now);
		setFertigungsgruppeIId(fertigungsgruppeIId2);
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

	public String getCKommentar() {
		return this.cKommentar;
	}

	public void setCKommentar(String cKommentar) {
		this.cKommentar = cKommentar;
	}

	public String getCProjekt() {
		return this.cProjekt;
	}

	public void setCProjekt(String cProjekt) {
		this.cProjekt = cProjekt;
	}

	public BigDecimal getNLosgroesse() {
		return this.nLosgroesse;
	}

	public void setNLosgroesse(BigDecimal nLosgroesse) {
		this.nLosgroesse = nLosgroesse;
	}

	public Date getTProduktionsende() {
		return this.tProduktionsende;
	}

	public void setTProduktionsende(Date produktionsende) {
		this.tProduktionsende = produktionsende;
	}

	public Date getTProduktionsbeginn() {
		return this.tProduktionsbeginn;
	}

	public void setTProduktionsbeginn(Date tProduktionsbeginn) {
		this.tProduktionsbeginn = tProduktionsbeginn;
	}

	public Timestamp getTAusgabe() {
		return this.tAusgabe;
	}

	public void setTAusgabe(Timestamp tAusgabe) {
		this.tAusgabe = tAusgabe;
	}

	public Timestamp getTErledigt() {
		return this.tErledigt;
	}

	public void setTErledigt(Timestamp tErledigt) {
		this.tErledigt = tErledigt;
	}

	public Timestamp getTProduktionsstop() {
		return this.tProduktionsstop;
	}

	public void setTProduktionsstop(Timestamp tProduktionsstop) {
		this.tProduktionsstop = tProduktionsstop;
	}

	public Timestamp getTAktualisierungstueckliste() {
		return this.tAktualisierungstueckliste;
	}

	public void setTAktualisierungstueckliste(
			Timestamp tAktualisierungstueckliste) {
		this.tAktualisierungstueckliste = tAktualisierungstueckliste;
	}

	public Timestamp getTAktualisierungarbeitszeit() {
		return this.tAktualisierungarbeitszeit;
	}

	public void setTAktualisierungarbeitszeit(
			Timestamp tAktualisierungarbeitszeit) {
		this.tAktualisierungarbeitszeit = tAktualisierungarbeitszeit;
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

	public Timestamp getTManuellerledigt() {
		return this.tManuellerledigt;
	}

	public void setTManuellerledigt(Timestamp tManuellerledigt) {
		this.tManuellerledigt = tManuellerledigt;
	}

	public String getXText() {
		return this.xText;
	}

	public void setXText(String xText) {
		this.xText = xText;
	}

	public String getCZusatznummer() {
		return this.cZusatznummer;
	}

	public void setCZusatznummer(String cZusatznummer) {
		this.cZusatznummer = cZusatznummer;
	}

	public Timestamp getTLeitstandstop() {
		return this.tLeitstandstop;
	}

	public void setTLeitstandstop(Timestamp tLeitstandstop) {
		this.tLeitstandstop = tLeitstandstop;
	}

	public Double getFBewertung() {
		return this.fBewertung;
	}

	public void setFBewertung(Double fBewertung) {
		this.fBewertung = fBewertung;
	}

	public Integer getAuftragIId() {
		return this.auftragIId;
	}

	public void setAuftragIId(Integer auftragIId) {
		this.auftragIId = auftragIId;
	}

	public Integer getAuftragpositionIId() {
		return this.auftragpositionIId;
	}

	public void setAuftragpositionIId(Integer auftragpositionIId) {
		this.auftragpositionIId = auftragpositionIId;
	}

	public Integer getLosIIdElternlos() {
		return this.losIIdElternlos;
	}

	public void setLosIIdElternlos(Integer losIIdElternlos) {
		this.losIIdElternlos = losIIdElternlos;
	}

	public String getStatusCNr() {
		return this.statusCNr;
	}

	public void setStatusCNr(String statusCNr) {
		this.statusCNr = statusCNr;
	}

	public Integer getWiederholendeloseIId() {
		return this.wiederholendeloseIId;
	}

	public void setWiederholendeloseIId(Integer wiederholendeloseIId) {
		this.wiederholendeloseIId = wiederholendeloseIId;
	}

	public Integer getKostenstelleIId() {
		return this.kostenstelleIId;
	}

	public void setKostenstelleIId(Integer kostenstelleIId) {
		this.kostenstelleIId = kostenstelleIId;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Integer getPartnerIIdFertigungsort() {
		return this.partnerIIdFertigungsort;
	}

	public void setPartnerIIdFertigungsort(Integer partnerIIdFertigungsort) {
		this.partnerIIdFertigungsort = partnerIIdFertigungsort;
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

	public Integer getPersonalIIdAusgabe() {
		return this.personalIIdAusgabe;
	}

	public void setPersonalIIdAusgabe(Integer personalIIdAusgabe) {
		this.personalIIdAusgabe = personalIIdAusgabe;
	}

	public Integer getPersonalIIdErledigt() {
		return this.personalIIdErledigt;
	}

	public void setPersonalIIdErledigt(Integer personalIIdErledigt) {
		this.personalIIdErledigt = personalIIdErledigt;
	}

	public Integer getPersonalIIdLeitstandstop() {
		return this.personalIIdLeitstandstop;
	}

	public void setPersonalIIdLeitstandstop(Integer personalIIdLeitstandstop) {
		this.personalIIdLeitstandstop = personalIIdLeitstandstop;
	}

	public Integer getPersonalIIdManuellerledigt() {
		return this.personalIIdManuellerledigt;
	}

	public void setPersonalIIdManuellerledigt(Integer personalIIdManuellerledigt) {
		this.personalIIdManuellerledigt = personalIIdManuellerledigt;
	}

	public Integer getPersonalIIdProduktionsstop() {
		return this.personalIIdProduktionsstop;
	}

	public void setPersonalIIdProduktionsstop(Integer personalIIdProduktionsstop) {
		this.personalIIdProduktionsstop = personalIIdProduktionsstop;
	}

	public Integer getPersonalIIdTechniker() {
		return this.personalIIdTechniker;
	}

	public void setPersonalIIdTechniker(Integer personalIIdTechniker) {
		this.personalIIdTechniker = personalIIdTechniker;
	}

	public Integer getFertigungsgruppeIId() {
		return this.fertigungsgruppeIId;
	}

	public void setFertigungsgruppeIId(Integer fertigungsgruppeIId) {
		this.fertigungsgruppeIId = fertigungsgruppeIId;
	}

	public Integer getStuecklisteIId() {
		return this.stuecklisteIId;
	}

	public void setStuecklisteIId(Integer stuecklisteIId) {
		this.stuecklisteIId = stuecklisteIId;
	}

	public Integer getLagerIIdZiel() {
		return this.lagerIIdZiel;
	}

	public void setLagerIIdZiel(Integer lagerIIdZiel) {
		this.lagerIIdZiel = lagerIIdZiel;
	}

}

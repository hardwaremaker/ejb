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
package com.lp.server.angebot.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.system.service.ITablenames;
import com.lp.server.util.IVersionable;

@NamedQueries({
		@NamedQuery(name = "AngebotfindByKundeIIdAngebotsadresseMandantCNr", query = "SELECT OBJECT (O) FROM Angebot o WHERE o.kundeIIdAngebotsadresse=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "AngebotfindByAnsprechpartnerIIdKundeMandantCNr", query = "SELECT OBJECT (O) FROM Angebot o WHERE o.ansprechpartnerIIdKunde=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "AngebotfindByKundeIIdLieferadresseMandantCNr", query = "SELECT OBJECT (O) FROM Angebot o WHERE o.kundeIIdLieferadresse=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "AngebotfindByProjektIId", query = "SELECT OBJECT (O) FROM Angebot o WHERE o.projektIId=?1"),
		@NamedQuery(name = "AngebotfindByKundeIIdRechnungsadresseMandantCNr", query = "SELECT OBJECT (O) FROM Angebot o WHERE o.kundeIIdRechnungsadresse=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "AngebotfindByAnsprechpartnerIIdRechnungsadresseMandantCNr", query = "SELECT OBJECT (O) FROM Angebot o WHERE o.ansprechpartnerIIdRechnungsadresse=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "AngebotfindByAnsprechpartnerIIdLieferadresseMandantCNr", query = "SELECT OBJECT (O) FROM Angebot o WHERE o.ansprechpartnerIIdLieferadresse=?1 AND o.mandantCNr=?2"),


		@NamedQuery(name = Angebot.QueryFindByCnrMandantCnr, query = "SELECT OBJECT (O) FROM Angebot o WHERE o.cNr = :cnr AND o.mandantCNr= :mandant")})
@Entity
@Table(name = ITablenames.ANGB_ANGEBOT)
public class Angebot implements Serializable, IVersionable {
	public final static String QueryFindByCnrMandantCnr = "AngebotfindByCnrMandantCNr" ;
	
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "T_BELEGDATUM")
	private Timestamp tBelegdatum;

	@Column(name = "T_ANFRAGEDATUM")
	private Timestamp tAnfragedatum;

	@Column(name = "T_ANGEBOTSGUELTIGKEITBIS")
	private Timestamp tAngebotsgueltigkeitbis;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "F_WECHSELKURSMANDANTWAEHRUNGZUANGEBOTSWAEHRUNG")
	private Double fWechselkursmandantwaehrungzuangebotswaehrung;

	@Column(name = "I_LIEFERZEITINSTUNDEN")
	private Integer iLieferzeitinstunden;

	@Column(name = "T_NACHFASSTERMIN")
	private Timestamp tNachfasstermin;

	@Column(name = "T_REALISIERUNGSTERMIN")
	private Timestamp tRealisierungstermin;

	@Column(name = "F_AUFTRAGSWAHRSCHEINLICHKEIT")
	private Double fAuftragswahrscheinlichkeit;

	@Column(name = "X_ABLAGEORT")
	private String xAblageort;

	@Column(name = "F_VERSTECKTERAUFSCHLAG")
	private Double fVersteckteraufschlag;

	@Column(name = "F_ALLGEMEINERRABATTSATZ")
	private Double fAllgemeinerrabattsatz;

	@Column(name = "F_PROJEKTIERUNGSRABATTSATZ")
	private Double fProjektierungsrabattsatz;

	@Column(name = "I_GARANTIE")
	private Integer iGarantie;
	
	@Column(name = "C_KOMMISSION")
	private String cKommission;
	
	public String getCKommission() {
		return cKommission;
	}

	public void setCKommission(String kommission) {
		cKommission = kommission;
	}
	
	@Column(name = "AKQUISESTATUS_I_ID")
	private Integer akquisestatusIId;

	public Integer getAkquisestatusIId() {
		return akquisestatusIId;
	}

	public void setAkquisestatusIId(Integer akquisestatusIId) {
		this.akquisestatusIId = akquisestatusIId;
	}

	@Column(name = "B_MINDERMENGENZUSCHLAG")
	private Short bMindermengenzuschlag;
	public Short getBMindermengenzuschlag() {
		return this.bMindermengenzuschlag;
	}

	public void setBMindermengenzuschlag(Short bMindermengenzuschlag) {
		this.bMindermengenzuschlag = bMindermengenzuschlag;
	}
	
	@Column(name = "N_GESAMTANGEBOTSWERTINANGEBOTSWAEHRUNG")
	private BigDecimal nGesamtangebotswertinangebotswaehrung;

	@Column(name = "N_KORREKTURBETRAG")
	private BigDecimal nKorrekturbetrag;

	
	public BigDecimal getNKorrekturbetrag() {
		return nKorrekturbetrag;
	}

	public void setNKorrekturbetrag(BigDecimal nKorrekturbetrag) {
		this.nKorrekturbetrag = nKorrekturbetrag;
	}

	@Column(name = "X_KOPFTEXTUEBERSTEUERT")
	private String xKopftextuebersteuert;

	@Column(name = "X_FUSSTEXTUEBERSTEUERT")
	private String xFusstextuebersteuert;

	@Column(name = "T_GEDRUCKT")
	private Timestamp tGedruckt;

	@Column(name = "T_STORNIERT")
	private Timestamp tStorniert;

	@Column(name = "T_MANUELLERLEDIGT")
	private Timestamp tManuellerledigt;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "X_EXTERNERKOMMENTAR")
	private String xExternerkommentar;

	@Column(name = "X_INTERNERKOMMENTAR")
	private String xInternerkommentar;

	@Column(name = "ANGEBOTART_C_NR")
	private String angebotartCNr;

	@Column(name = "ANGEBOTEINHEIT_C_NR")
	private String angeboteinheitCNr;

	@Column(name = "ANGEBOTERLEDIGUNGSGRUND_C_NR")
	private String angeboterledigungsgrundCNr;

	@Column(name = "ANGEBOTSTATUS_C_NR")
	private String angebotstatusCNr;
	
	@Column(name = "C_LIEFERARTORT")
	private String cLieferartort;

	@Column(name = "PROJEKT_I_ID")
	private Integer projektIId;
	
	public Integer getProjektIId() {
		return projektIId;
	}

	public void setProjektIId(Integer projektIId) {
		this.projektIId = projektIId;
	}

	public String getCLieferartort() {
		return cLieferartort;
	}

	public void setCLieferartort(String cLieferartort) {
		this.cLieferartort = cLieferartort;
	}

	@Column(name = "B_MITZUSAMMENFASSUNG")
	private Short bMitzusammenfassung;

	public Short getBMitzusammenfassung() {
		return bMitzusammenfassung;
	}

	public void setBMitzusammenfassung(Short bMitzusammenfassung) {
		this.bMitzusammenfassung = bMitzusammenfassung;
	}

	@Column(name = "ANGEBOTTEXT_I_ID_KOPFTEXT")
	private Integer angebottextIIdKopftext;

	@Column(name = "ANGEBOTTEXT_I_ID_FUSSTEXT")
	private Integer angebottextIIdFusstext;

	@Column(name = "BELEGART_C_NR")
	private String belegartCNr;

	@Column(name = "KOSTENSTELLE_I_ID")
	private Integer kostenstelleIId;

	@Column(name = "LIEFERART_I_ID")
	private Integer lieferartIId;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "SPEDITEUR_I_ID")
	private Integer spediteurIId;

	@Column(name = "WAEHRUNG_C_NR_ANGEBOTSWAEHRUNG")
	private String waehrungCNrAngebotswaehrung;

	@Column(name = "ZAHLUNGSZIEL_I_ID")
	private Integer zahlungszielIId;

	@Column(name = "ANSPRECHPARTNER_I_ID_KUNDE")
	private Integer ansprechpartnerIIdKunde;

	@Column(name = "KUNDE_I_ID_ANGEBOTSADRESSE")
	private Integer kundeIIdAngebotsadresse;

	@Column(name = "KUNDE_I_ID_RECHNUNGSADRESSE")
	private Integer kundeIIdRechnungsadresse;

	public Integer getKundeIIdRechnungsadresse() {
		return kundeIIdRechnungsadresse;
	}

	public void setKundeIIdRechnungsadresse(Integer kundeIIdRechnungsadresse) {
		this.kundeIIdRechnungsadresse = kundeIIdRechnungsadresse;
	}

	public Integer getKundeIIdLieferadresse() {
		return kundeIIdLieferadresse;
	}

	public void setKundeIIdLieferadresse(Integer kundeIIdLieferadresse) {
		this.kundeIIdLieferadresse = kundeIIdLieferadresse;
	}

	public Integer getAnsprechpartnerIIdRechnungsadresse() {
		return ansprechpartnerIIdRechnungsadresse;
	}

	public void setAnsprechpartnerIIdRechnungsadresse(Integer ansprechpartnerIIdRechnungsadresse) {
		this.ansprechpartnerIIdRechnungsadresse = ansprechpartnerIIdRechnungsadresse;
	}

	public Integer getAnsprechpartnerIIdLieferadresse() {
		return ansprechpartnerIIdLieferadresse;
	}

	public void setAnsprechpartnerIIdLieferadresse(Integer ansprechpartnerIIdLieferadresse) {
		this.ansprechpartnerIIdLieferadresse = ansprechpartnerIIdLieferadresse;
	}

	@Column(name = "KUNDE_I_ID_LIEFERADRESSE")
	private Integer kundeIIdLieferadresse;

	@Column(name = "ANSPRECHPARTNER_I_ID_RECHNUNGSADRESSE")
	private Integer ansprechpartnerIIdRechnungsadresse;
	@Column(name = "ANSPRECHPARTNER_I_ID_LIEFERADRESSE")
	private Integer ansprechpartnerIIdLieferadresse;
	
	@Column(name = "PERSONAL_I_ID_STORNIERT")
	private Integer personalIIdStorniert;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_MANUELLERLEDIGT")
	private Integer personalIIdManuellerledigt;

	@Column(name = "PERSONAL_I_ID_VERTRETER")
	private Integer personalIIdVertreter;

	@Column(name = "PERSONAL_I_ID_VERTRETER2")
	private Integer personalIIdVertreter2;

	public Integer getPersonalIIdVertreter2() {
		return personalIIdVertreter2;
	}

	public void setPersonalIIdVertreter2(Integer personalIIdVertreter2) {
		this.personalIIdVertreter2 = personalIIdVertreter2;
	}

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "T_VERSANDZEITPUNKT")
	private Timestamp tVersandzeitpunkt;

	@Column(name = "C_VERSANDTYPE")
	private String cVersandtype;

	public String getCKundenanfrage() {
		return cKundenanfrage;
	}

	public void setCKundenanfrage(String cKundenanfrage) {
		this.cKundenanfrage = cKundenanfrage;
	}

	@Column(name = "C_KUNDENANFRAGE")
	private String cKundenanfrage;
	
	@Column(name = "T_AENDERUNGSANGEBOT")
	private Timestamp tAenderungsangebot;
	
	@Column(name = "I_AENDERUNGSANGEBOT_VERSION")
	private Integer iAenderungsangebotVersion;

	private static final long serialVersionUID = 1L;

	public Angebot(Integer iId, String cNr, String mandantCNr, String artCNr,
			String statusCNr, String belegartCNr, Timestamp tBelegdatum,
			Timestamp tAnfragedatum, Timestamp tAngebotsgueltigkeitbis,
			Integer kundeIIdAngebotsadresse, Integer kundeIIdRechnungsadresse, Integer kundeIIdLieferadresse, Integer personalIIdVertreter,
			String waehrungCNrAngebotswaehrung,
			Double fWechselkursmandantwaehrungzuangebotswaehrung,
			String angeboteinheitCNr, Integer kostenstelleIId,
			Timestamp tNachfasstermin, Double fAuftragswahrscheinlichkeit,
			Double fVersteckteraufschlag, Double fAllgemeinerrabattsatz,
			Double fProjektierungsrabattsatz, Integer lieferartIId,
			Integer zahlungszielIId, Integer spediteurIId, Integer iGarantie,
			Integer personalIIdAnlegen, Integer personalIIdAendern,
			Short bMitzusammenfassung,Short bMindermengenzuschlag) {

		Timestamp t = new Timestamp(System.currentTimeMillis());
		this.setTAnlegen(t);
		this.setTAendern(t);
		setIId(iId);
		setCNr(cNr);
		setMandantCNr(mandantCNr);
		setAngebotartCNr(artCNr);
		setAngebotstatusCNr(statusCNr);
		setBelegartCNr(belegartCNr);
		setTBelegdatum(tBelegdatum);
		setTAnfragedatum(tAnfragedatum);
		setTAngebotsgueltigkeitbis(tAngebotsgueltigkeitbis);
		setKundeIIdAngebotsadresse(kundeIIdAngebotsadresse);
		setKundeIIdLieferadresse(kundeIIdLieferadresse);
		setKundeIIdRechnungsadresse(kundeIIdRechnungsadresse);
		setPersonalIIdVertreter(personalIIdVertreter);
		setWaehrungCNrAngebotswaehrung(waehrungCNrAngebotswaehrung);
		setFWechselkursmandantwaehrungzuangebotswaehrung(fWechselkursmandantwaehrungzuangebotswaehrung);
		setAngeboteinheitCNr(angeboteinheitCNr);
		setKostenstelleIId(kostenstelleIId);
		setTNachfasstermin(tNachfasstermin);
		setFAuftragswahrscheinlichkeit(fAuftragswahrscheinlichkeit);
		setFVersteckteraufschlag(fVersteckteraufschlag);
		setFAllgemeinerrabattsatz(fAllgemeinerrabattsatz);
		setFProjektierungsrabattsatz(fProjektierungsrabattsatz);
		setLieferartIId(lieferartIId);
		setZahlungszielIId(zahlungszielIId);
		setSpediteurIId(spediteurIId);
		setIGarantie(iGarantie);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setPersonalIIdAendern(personalIIdAendern);
		setBMitzusammenfassung(bMitzusammenfassung);
		setBMindermengenzuschlag(bMindermengenzuschlag);
	}

	public Angebot() {

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

	public Timestamp getTBelegdatum() {
		return this.tBelegdatum;
	}

	public void setTBelegdatum(Timestamp tBelegdatum) {
		this.tBelegdatum = tBelegdatum;
	}

	public Timestamp getTAnfragedatum() {
		return this.tAnfragedatum;
	}

	public void setTAnfragedatum(Timestamp tAnfragedatum) {
		this.tAnfragedatum = tAnfragedatum;
	}

	public Timestamp getTAngebotsgueltigkeitbis() {
		return this.tAngebotsgueltigkeitbis;
	}

	public void setTAngebotsgueltigkeitbis(Timestamp tAngebotsgueltigkeitbis) {
		this.tAngebotsgueltigkeitbis = tAngebotsgueltigkeitbis;
	}

	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public Double getFWechselkursmandantwaehrungzuangebotswaehrung() {
		return this.fWechselkursmandantwaehrungzuangebotswaehrung;
	}

	public void setFWechselkursmandantwaehrungzuangebotswaehrung(
			Double fWechselkursmandantwaehrungzuangebotswaehrung) {
		this.fWechselkursmandantwaehrungzuangebotswaehrung = fWechselkursmandantwaehrungzuangebotswaehrung;
	}

	public Integer getILieferzeitinstunden() {
		return this.iLieferzeitinstunden;
	}

	public void setILieferzeitinstunden(Integer iLieferzeitinstunden) {
		this.iLieferzeitinstunden = iLieferzeitinstunden;
	}

	public Timestamp getTNachfasstermin() {
		return this.tNachfasstermin;
	}

	public void setTNachfasstermin(Timestamp tNachfasstermin) {
		this.tNachfasstermin = tNachfasstermin;
	}

	public Timestamp getTRealisierungstermin() {
		return this.tRealisierungstermin;
	}

	public void setTRealisierungstermin(Timestamp tRealisierungstermin) {
		this.tRealisierungstermin = tRealisierungstermin;
	}

	public Double getFAuftragswahrscheinlichkeit() {
		return this.fAuftragswahrscheinlichkeit;
	}

	public void setFAuftragswahrscheinlichkeit(
			Double fAuftragswahrscheinlichkeit) {
		this.fAuftragswahrscheinlichkeit = fAuftragswahrscheinlichkeit;
	}

	public String getXAblageort() {
		return this.xAblageort;
	}

	public void setXAblageort(String xAblageort) {
		this.xAblageort = xAblageort;
	}

	public Double getFVersteckteraufschlag() {
		return this.fVersteckteraufschlag;
	}

	public void setFVersteckteraufschlag(Double fVersteckteraufschlag) {
		this.fVersteckteraufschlag = fVersteckteraufschlag;
	}

	public Double getFAllgemeinerrabattsatz() {
		return this.fAllgemeinerrabattsatz;
	}

	public void setFAllgemeinerrabattsatz(Double fAllgemeinerrabattsatz) {
		this.fAllgemeinerrabattsatz = fAllgemeinerrabattsatz;
	}

	public Double getFProjektierungsrabattsatz() {
		return this.fProjektierungsrabattsatz;
	}

	public void setFProjektierungsrabattsatz(Double fProjektierungsrabattsatz) {
		this.fProjektierungsrabattsatz = fProjektierungsrabattsatz;
	}

	public Integer getIGarantie() {
		return this.iGarantie;
	}

	public void setIGarantie(Integer iGarantie) {
		this.iGarantie = iGarantie;
	}

	public BigDecimal getNGesamtangebotswertinangebotswaehrung() {
		return this.nGesamtangebotswertinangebotswaehrung;
	}

	public void setNGesamtangebotswertinangebotswaehrung(
			BigDecimal nGesamtangebotswertinangebotswaehrung) {
		this.nGesamtangebotswertinangebotswaehrung = nGesamtangebotswertinangebotswaehrung;
	}

	public String getXKopftextuebersteuert() {
		return this.xKopftextuebersteuert;
	}

	public void setXKopftextuebersteuert(String xKopftextuebersteuert) {
		this.xKopftextuebersteuert = xKopftextuebersteuert;
	}

	public String getXFusstextuebersteuert() {
		return this.xFusstextuebersteuert;
	}

	public void setXFusstextuebersteuert(String xFusstextuebersteuert) {
		this.xFusstextuebersteuert = xFusstextuebersteuert;
	}

	public Timestamp getTGedruckt() {
		return this.tGedruckt;
	}

	public void setTGedruckt(Timestamp tGedruckt) {
		this.tGedruckt = tGedruckt;
	}

	public Timestamp getTStorniert() {
		return this.tStorniert;
	}

	public void setTStorniert(Timestamp tStorniert) {
		this.tStorniert = tStorniert;
	}

	public Timestamp getTManuellerledigt() {
		return this.tManuellerledigt;
	}

	public void setTManuellerledigt(Timestamp tManuellerledigt) {
		this.tManuellerledigt = tManuellerledigt;
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

	public String getXExternerkommentar() {
		return this.xExternerkommentar;
	}

	public void setXExternerkommentar(String xExternerkommentar) {
		this.xExternerkommentar = xExternerkommentar;
	}

	public String getXInternerkommentar() {
		return this.xInternerkommentar;
	}

	public void setXInternerkommentar(String xInternerkommentar) {
		this.xInternerkommentar = xInternerkommentar;
	}

	public String getAngebotartCNr() {
		return this.angebotartCNr;
	}

	public void setAngebotartCNr(String angebotartCNr) {
		this.angebotartCNr = angebotartCNr;
	}

	public String getAngeboteinheitCNr() {
		return this.angeboteinheitCNr;
	}

	public void setAngeboteinheitCNr(String angeboteinheitCNr) {
		this.angeboteinheitCNr = angeboteinheitCNr;
	}

	public String getAngeboterledigungsgrundCNr() {
		return this.angeboterledigungsgrundCNr;
	}

	public void setAngeboterledigungsgrundCNr(String angeboterledigungsgrundCNr) {
		this.angeboterledigungsgrundCNr = angeboterledigungsgrundCNr;
	}

	public String getAngebotstatusCNr() {
		return this.angebotstatusCNr;
	}

	public void setAngebotstatusCNr(String angebotstatusCNr) {
		this.angebotstatusCNr = angebotstatusCNr;
	}

	public Integer getAngebottextIIdKopftext() {
		return this.angebottextIIdKopftext;
	}

	public void setAngebottextIIdKopftext(Integer angebottextIIdKopftext) {
		this.angebottextIIdKopftext = angebottextIIdKopftext;
	}

	public Integer getAngebottextIIdFusstext() {
		return this.angebottextIIdFusstext;
	}

	public void setAngebottextIIdFusstext(Integer angebottextIIdFusstext) {
		this.angebottextIIdFusstext = angebottextIIdFusstext;
	}

	public String getBelegartCNr() {
		return this.belegartCNr;
	}

	public void setBelegartCNr(String belegartCNr) {
		this.belegartCNr = belegartCNr;
	}

	public Integer getKostenstelleIId() {
		return this.kostenstelleIId;
	}

	public void setKostenstelleIId(Integer kostenstelleIId) {
		this.kostenstelleIId = kostenstelleIId;
	}

	public Integer getLieferartIId() {
		return this.lieferartIId;
	}

	public void setLieferartIId(Integer lieferartIId) {
		this.lieferartIId = lieferartIId;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Integer getSpediteurIId() {
		return this.spediteurIId;
	}

	public void setSpediteurIId(Integer spediteurIId) {
		this.spediteurIId = spediteurIId;
	}

	public String getWaehrungCNrAngebotswaehrung() {
		return this.waehrungCNrAngebotswaehrung;
	}

	public void setWaehrungCNrAngebotswaehrung(
			String waehrungCNrAngebotswaehrung) {
		this.waehrungCNrAngebotswaehrung = waehrungCNrAngebotswaehrung;
	}

	public Integer getZahlungszielIId() {
		return this.zahlungszielIId;
	}

	public void setZahlungszielIId(Integer zahlungszielIId) {
		this.zahlungszielIId = zahlungszielIId;
	}

	public Integer getAnsprechpartnerIIdKunde() {
		return this.ansprechpartnerIIdKunde;
	}

	public void setAnsprechpartnerIIdKunde(Integer ansprechpartnerIIdKunde) {
		this.ansprechpartnerIIdKunde = ansprechpartnerIIdKunde;
	}

	public Integer getKundeIIdAngebotsadresse() {
		return this.kundeIIdAngebotsadresse;
	}

	public void setKundeIIdAngebotsadresse(Integer kundeIIdAngebotsadresse) {
		this.kundeIIdAngebotsadresse = kundeIIdAngebotsadresse;
	}

	public Integer getPersonalIIdStorniert() {
		return this.personalIIdStorniert;
	}

	public void setPersonalIIdStorniert(Integer personalIIdStorniert) {
		this.personalIIdStorniert = personalIIdStorniert;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getPersonalIIdManuellerledigt() {
		return this.personalIIdManuellerledigt;
	}

	public void setPersonalIIdManuellerledigt(Integer personalIIdManuellerledigt) {
		this.personalIIdManuellerledigt = personalIIdManuellerledigt;
	}

	public Integer getPersonalIIdVertreter() {
		return this.personalIIdVertreter;
	}

	public void setPersonalIIdVertreter(Integer personalIIdVertreter) {
		this.personalIIdVertreter = personalIIdVertreter;
	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public void setTVersandzeitpunkt(Timestamp tVersandzeitpunkt) {
		this.tVersandzeitpunkt = tVersandzeitpunkt;
	}

	public Timestamp getTVersandzeitpunkt() {
		return tVersandzeitpunkt;
	}

	public void setCVersandtype(String cVersandtype) {
		this.cVersandtype = cVersandtype;
	}

	public String getCVersandtype() {
		return cVersandtype;
	}

	public Timestamp getTAenderungsangebot() {
		return tAenderungsangebot;
	}
	
	public void setTAenderungsangebot(Timestamp tAenderungsangebot) {
		this.tAenderungsangebot = tAenderungsangebot;
	}
	
	public Integer getIVersion() {
		return iAenderungsangebotVersion;
	}
	
	public void setIVersion(Integer iVersion) {
		this.iAenderungsangebotVersion = iVersion;
	}

	@Override
	public boolean hasVersion() {
		return getIVersion() != null;
	}

	@Override
	public Timestamp getTVersion() {
		return getTAenderungsangebot();
	}

	@Override
	public void setTVersion(Timestamp tVersion) {
		setTAenderungsangebot(tVersion);
	}
}

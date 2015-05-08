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
package com.lp.server.auftrag.ejb;

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
		@NamedQuery(name = "AuftragfindByMandantAndStatus", query = "SELECT OBJECT (o) FROM Auftrag o WHERE o.mandantCNr=?1 AND o.auftragstatusCNr=?2"),
		@NamedQuery(name = "AuftragfindByKundeBelegdatumVonBis", query = "SELECT OBJECT (o) FROM Auftrag o WHERE o.kundeIIdAuftragsadresse=?1 AND (o.tBelegdatum>?2 OR o.tBelegdatum=?2) AND (o.tBelegdatum<?3 OR o.tBelegdatum=?3)"),
		@NamedQuery(name = "AuftragfindByStatusKundeBelegdatumVonBis", query = "SELECT OBJECT (o) FROM Auftrag o WHERE o.auftragstatusCNr=?1 AND o.kundeIIdAuftragsadresse=?2 AND (o.tBelegdatum>?3 OR o.tBelegdatum=?3) AND (o.tBelegdatum<?4 OR o.tBelegdatum=?4)"),
		@NamedQuery(name = "AuftragfindByAngebotIId", query = "SELECT OBJECT (o) FROM Auftrag o WHERE o.angebotIId=?1"),
		@NamedQuery(name = "AuftragfindByAuftragIIdRahmenauftrag", query = "SELECT OBJECT (o) FROM Auftrag o WHERE o.auftragIIdRahmenauftrag=?1 ORDER BY o.cNr"),
		@NamedQuery(name = "AuftragfindByMandantCNrCNr", query = "SELECT OBJECT (o) FROM Auftrag o WHERE o.mandantCNr=?1 AND o.cNr=?2"),
		@NamedQuery(name = "AuftragfindByMandantCNrAuftragartCNr", query = "SELECT OBJECT (o) FROM Auftrag o WHERE o.mandantCNr=?1 AND o.auftragartCNr=?2"),
		@NamedQuery(name = "AuftragfindByMandantCNrAuftragartCNrStatusCNr", query = "SELECT OBJECT (o) FROM Auftrag o WHERE o.mandantCNr=?1 AND o.auftragartCNr=?2 AND (o.auftragstatusCNr=?3 OR o.auftragstatusCNr=?4)"),
		@NamedQuery(name = "AuftragfindByKundeIIdAuftragsadresseMandantCNr", query = "SELECT OBJECT (O) FROM Auftrag o WHERE o.kundeIIdAuftragsadresse=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "AuftragfindByKundeIIdLieferadresseMandantCNr", query = "SELECT OBJECT (O) FROM Auftrag o WHERE o.kundeIIdLieferadresse=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "AuftragfindByKundeIIdRechnungsadresseMandantCNr", query = "SELECT OBJECT (O) FROM Auftrag o WHERE o.kundeIIdRechnungsadresse=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "AuftragfindByAnsprechpartnerIIdMandantCNr", query = "SELECT OBJECT (O) FROM Auftrag o WHERE o.ansprechpartnerIIdKunde=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "AuftragfindByAnsprechpartnerIIdRechnungsadresseMandantCNr", query = "SELECT OBJECT (O) FROM Auftrag o WHERE o.ansprechpartnerIIdRechnungsadresse=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "AuftragfindByAnsprechpartnerIIdLieferadresseMandantCNr", query = "SELECT OBJECT (O) FROM Auftrag o WHERE o.ansprechpartnerIIdLieferadresse=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "AuftragfindByMandantCNrKundeIIdCBestellnummer", query = "SELECT OBJECT (O) FROM Auftrag o WHERE o.mandantCNr=?1 AND o.kundeIIdAuftragsadresse=?2 AND REPLACE(o.cBestellnummer,' ','')=?3"),
		@NamedQuery(name = "AuftragfindAll", query = "SELECT OBJECT (O) FROM Auftrag o") })
@Entity
@Table(name = "AUFT_AUFTRAG")
public class Auftrag implements Serializable, ICNr {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "T_BELEGDATUM")
	private Timestamp tBelegdatum;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "C_BESTELLNUMMER")
	private String cBestellnummer;

	@Column(name = "T_BESTELLDATUM")
	private Timestamp tBestelldatum;

	@Column(name = "F_WECHSELKURSMANDANTWAEHRUNGZUAUFTRAGSWAEHRUNG")
	private Double fWechselkursmandantwaehrungzuauftragswaehrung;

	@Column(name = "F_SONDERRABATTSATZ")
	private Double fSonderrabattsatz;

	@Column(name = "T_LIEFERTERMIN")
	private Timestamp tLiefertermin;

	@Column(name = "B_LIEFERTERMINUNVERBINDLICH")
	private Short bLieferterminunverbindlich;

	
	@Column(name = "N_KORREKTURBETRAG")
	private BigDecimal nKorrekturbetrag;

	
	public BigDecimal getNKorrekturbetrag() {
		return nKorrekturbetrag;
	}

	public void setNKorrekturbetrag(BigDecimal nKorrekturbetrag) {
		this.nKorrekturbetrag = nKorrekturbetrag;
	}

	
	@Column(name = "B_MITZUSAMMENFASSUNG")
	private Short bMitzusammenfassung;

	public Short getBMitzusammenfassung() {
		return bMitzusammenfassung;
	}

	public void setBMitzusammenfassung(Short bMitzusammenfassung) {
		this.bMitzusammenfassung = bMitzusammenfassung;
	}
	@Column(name = "PROJEKT_I_ID")
	private Integer projektIId;
	
	public Integer getProjektIId() {
		return projektIId;
	}

	public void setProjektIId(Integer projektIId) {
		this.projektIId = projektIId;
	}
	@Column(name = "B_VERSTECKT")
	private Short bVersteckt;

	public Short getBVersteckt() {
		return bVersteckt;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	@Column(name = "T_FINALTERMIN")
	private Timestamp tFinaltermin;

	@Column(name = "B_TEILLIEFERUNGMOEGLICH")
	private Short bTeillieferungmoeglich;

	@Column(name = "B_POENALE")
	private Short bPoenale;

	@Column(name = "B_ROHS")
	private Short bRoHs;

	@Column(name = "I_LEIHTAGE")
	private Integer iLeihtage;

	@Column(name = "F_VERSTECKTERAUFSCHLAG")
	private Double fVersteckteraufschlag;

	@Column(name = "F_ALLGEMEINERRABATTSATZ")
	private Double fAllgemeinerrabattsatz;

	@Column(name = "F_PROJEKTIERUNGSRABATTSATZ")
	private Double fProjektierungsrabattsatz;

	@Column(name = "I_GARANTIE")
	private Integer iGarantie;

	@Column(name = "N_GESAMTAUFTRAGSWERTINAUFTRAGSWAEHRUNG")
	private BigDecimal nGesamtauftragswertinauftragswaehrung;

	@Column(name = "N_MATERIALWERTINMANDANTENWAEHRUNG")
	private BigDecimal nMaterialwertinmandantenwaehrung;

	@Column(name = "N_ROHDECKUNGINMANDANTENWAEHRUNG")
	private BigDecimal nRohdeckunginmandantenwaehrung;

	@Column(name = "N_ROHDECKUNGALTINMANDANTENWAEHRUNG")
	private BigDecimal nRohdeckungaltinmandantenwaehrung;

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

	@Column(name = "AUFTRAG_I_ID_RAHMENAUFTRAG")
	private Integer auftragIIdRahmenauftrag;

	@Column(name = "X_EXTERNERKOMMENTAR")
	private String xExternerkommentar;

	@Column(name = "X_INTERNERKOMMENTAR")
	private String xInternerkommentar;

	@Column(name = "T_VERRECHENBAR")
	private Timestamp tVerrechenbar;

	
	@Column(name = "T_ERLEDIGT")
	private Timestamp tErledigt;

	@Column(name = "T_LAUFTERMIN")
	private Timestamp tLauftermin;

	@Column(name = "F_ERFUELLUNGSGRAD")
	private Double fErfuellungsgrad;

	@Column(name = "ANGEBOT_I_ID")
	private Integer angebotIId;

	@Column(name = "AUFTRAGART_C_NR")
	private String auftragartCNr;

	@Column(name = "AUFTRAGSTATUS_C_NR")
	private String auftragstatusCNr;

	@Column(name = "AUFTRAGTEXT_I_ID_KOPFTEXT")
	private Integer auftragtextIIdKopftext;

	@Column(name = "AUFTRAGTEXT_I_ID_FUSSTEXT")
	private Integer auftragtextIIdFusstext;

	@Column(name = "AUFTRAGWIEDERHOLUNGSINTERVALL_C_NR")
	private String auftragwiederholungsintervallCNr;

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

	@Column(name = "WAEHRUNG_C_NR_AUFTRAGSWAEHRUNG")
	private String waehrungCNrAuftragswaehrung;

	@Column(name = "ZAHLUNGSZIEL_I_ID")
	private Integer zahlungszielIId;

	@Column(name = "ANSPRECHPARTNER_I_ID_KUNDE")
	private Integer ansprechpartnerIIdKunde;

	@Column(name = "ANSPRECHPARTNER_I_ID_RECHNUNGSADRESSE")
	private Integer ansprechpartnerIIdRechnungsadresse;

	
	public Integer getAnsprechpartnerIIdRechnungsadresse() {
		return ansprechpartnerIIdRechnungsadresse;
	}

	public void setAnsprechpartnerIIdRechnungsadresse(
			Integer ansprechpartnerIIdRechnungsadresse) {
		this.ansprechpartnerIIdRechnungsadresse = ansprechpartnerIIdRechnungsadresse;
	}

	@Column(name = "ANSPRECHPARTNER_I_ID_LIEFERADRESSE")
	private Integer ansprechpartnerIIdLieferadresse;

	public Integer getAnsprechpartnerIIdLieferadresse() {
		return ansprechpartnerIIdLieferadresse;
	}

	public void setAnsprechpartnerIIdLieferadresse(
			Integer ansprechpartnerIIdLieferadresse) {
		this.ansprechpartnerIIdLieferadresse = ansprechpartnerIIdLieferadresse;
	}

	@Column(name = "KUNDE_I_ID_LIEFERADRESSE")
	private Integer kundeIIdLieferadresse;

	@Column(name = "KUNDE_I_ID_AUFTRAGSADRESSE")
	private Integer kundeIIdAuftragsadresse;

	@Column(name = "KUNDE_I_ID_RECHNUNGSADRESSE")
	private Integer kundeIIdRechnungsadresse;

	@Column(name = "PERSONAL_I_ID_ERLEDIGT")
	private Integer personalIIdErledigt;
	
	@Column(name = "PERSONAL_I_ID_VERRECHENBAR")
	private Integer personalIIdVerrechenbar;

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

	@Column(name = "PERSONAL_I_ID_STORNIERT")
	private Integer personalIIdStorniert;

	@Column(name = "PERSONAL_I_ID_VERTRETER")
	private Integer personalIIdVertreter;

	@Column(name = "PERSONAL_I_ID_MANUELLERLEDIGT")
	private Integer personalIIdManuellerledigt;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "T_VERSANDZEITPUNKT")
	private Timestamp tVersandzeitpunkt;

	@Column(name = "C_VERSANDTYPE")
	private String cVersandtype;

	@Column(name = "T_BEGRUENDUNG")
	private Timestamp tBegruendung;

	@Column(name = "PERSONAL_I_ID_BEGRUENDUNG")
	private Integer personalIIdBegruendung;

	@Column(name = "AUFTRAGBEGRUENDUNG_I_ID")
	private Integer auftragbegruendungIId;

	@Column(name = "LAGER_I_ID_ABBUCHUNGSLAGER")
	private Integer lagerIIdAbbuchungslager;
	

	public Integer getLagerIIdAbbuchungslager() {
		return this.lagerIIdAbbuchungslager;
	}
	public void setLagerIIdAbbuchungslager(Integer lagerIIdAbbuchungslager) {
		this.lagerIIdAbbuchungslager = lagerIIdAbbuchungslager;
	}
	@Column(name = "C_LIEFERARTORT")
	private String cLieferartort;
	
	
	@Column(name = "T_RESPONSE")
	private Timestamp tResponse ;
	
	@Column(name = "PERSONAL_I_ID_RESPONSE")
	private Integer personalIIdResponse ;
	
	public String getCLieferartort() {
		return cLieferartort;
	}

	public void setCLieferartort(String cLieferartort) {
		this.cLieferartort = cLieferartort;
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

	private static final long serialVersionUID = 1L;

	public Auftrag(Integer id, String nr, String mandantCNr,
			String auftragartCNr, Integer kundeIIdAuftragsadresse,
			Integer kundeIIdLieferadresse, Integer kundeIIdRechnungsadresse,
			String auftragswaehrung, Timestamp tBelegdatum,
			Timestamp tLiefertermin, Timestamp tFinaltermin,
			Integer lieferartIId, Integer zahlungszielIId,
			Integer spediteurIId, String auftragstatusCNr,
			Integer personalIIdAnlegen, Integer personalIIdAendern,
			Integer kostenstelleIId, Integer personalIIdVertreter,
			String belegartCNr,
			Double fWechselkursmandantwaehrungzuauftragswaehrung, Integer lagerIIdAbbuchungslager ) {
		Timestamp t = new Timestamp(System.currentTimeMillis());
		this.setTAnlegen(t);
		this.setTAendern(t);

		setIId(id);
		setCNr(nr);
		setMandantCNr(mandantCNr);
		setAuftragartCNr(auftragartCNr);
		setKundeIIdAuftragsadresse(kundeIIdAuftragsadresse);
		setKundeIIdLieferadresse(kundeIIdLieferadresse);
		setKundeIIdRechnungsadresse(kundeIIdRechnungsadresse);
		setWaehrungCNrAuftragswaehrung(auftragswaehrung);
		setTBelegdatum(tBelegdatum);
		setTLiefertermin(tLiefertermin);
		setLieferartIId(lieferartIId);
		setZahlungszielIId(zahlungszielIId);
		setSpediteurIId(spediteurIId);
		setAuftragstatusCNr(auftragstatusCNr);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setPersonalIIdAendern(personalIIdAendern);
		setKostenstelleIId(kostenstelleIId);
		setPersonalIIdVertreter(personalIIdVertreter);
		setBelegartCNr(belegartCNr);
		setFWechselkursmandantwaehrungzuauftragswaehrung(fWechselkursmandantwaehrungzuauftragswaehrung);
		setTFinaltermin(tFinaltermin);

		this.setBLieferterminunverbindlich(new Short((short) 0));
		this.setBTeillieferungmoeglich(new Short((short) 1));
		this.setBPoenale(new Short((short) 0));
		this.setBRoHs(new Short((short) 0));
		this.setILeihtage(new Integer(0));
		this.setFVersteckteraufschlag(new Double(0));
		this.setFAllgemeinerrabattsatz(new Double(0));
		this.setFProjektierungsrabattsatz(new Double(0));
		this.setIGarantie(new Integer(0));
		this.setBVersteckt(new Short((short) 0));
		this.setBMitzusammenfassung(new Short((short) 0));

		setFWechselkursmandantwaehrungzuauftragswaehrung(fWechselkursmandantwaehrungzuauftragswaehrung);
		setZahlungszielIId(zahlungszielIId);

		setPersonalIIdVertreter(personalIIdVertreter);
		setLagerIIdAbbuchungslager(lagerIIdAbbuchungslager);
	}

	public Auftrag(Integer id, String nr, String mandantCNr,
			String auftragartCNr, Integer kundeIIdAuftragsadresse,
			Integer kundeIIdLieferadresse, Integer kundeIIdRechnungsadresse,
			String auftragswaehrung, Timestamp tBelegdatum,
			Timestamp tLiefertermin, Integer lieferartIId,
			Integer zahlungszielIId, Integer spediteurIId,
			String auftragstatusCNr, Integer personalIIdAnlegen,
			Integer personalIIdAendern, Integer kostenstelleIId,
			Integer personalIIdVertreter, String belegartCNr,
			Double fWechselkursmandantwaehrungzuauftragswaehrung, Integer lagerIIdAbbuchungslager ) {
		Timestamp t = new Timestamp(System.currentTimeMillis());
		this.setTAnlegen(t);
		this.setTAendern(t);

		setIId(id);
		setCNr(nr);
		setMandantCNr(mandantCNr);
		setAuftragartCNr(auftragartCNr);
		setKundeIIdAuftragsadresse(kundeIIdAuftragsadresse);
		setKundeIIdLieferadresse(kundeIIdLieferadresse);
		setKundeIIdRechnungsadresse(kundeIIdRechnungsadresse);
		setWaehrungCNrAuftragswaehrung(auftragswaehrung);
		setTBelegdatum(tBelegdatum);
		setTLiefertermin(tLiefertermin);
		setTFinaltermin(tLiefertermin);
		setLieferartIId(lieferartIId);
		setZahlungszielIId(zahlungszielIId);
		setSpediteurIId(spediteurIId);
		setAuftragstatusCNr(auftragstatusCNr);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setPersonalIIdAendern(personalIIdAendern);
		setKostenstelleIId(kostenstelleIId);
		setPersonalIIdVertreter(personalIIdVertreter);
		setBelegartCNr(belegartCNr);
		setFWechselkursmandantwaehrungzuauftragswaehrung(fWechselkursmandantwaehrungzuauftragswaehrung);
		setLagerIIdAbbuchungslager(lagerIIdAbbuchungslager);

		this.setBLieferterminunverbindlich(new Short((short) 0));
		this.setBTeillieferungmoeglich(new Short((short) 1));
		this.setBPoenale(new Short((short) 0));
		this.setBRoHs(new Short((short) 0));
		this.setBVersteckt(new Short((short) 0));
		this.setILeihtage(new Integer(0));
		this.setFVersteckteraufschlag(new Double(0));
		this.setFAllgemeinerrabattsatz(new Double(0));
		this.setFProjektierungsrabattsatz(new Double(0));
		this.setIGarantie(new Integer(0));
		this.setBMitzusammenfassung(new Short((short) 0));
	}

	public Auftrag(Integer id, String nr, String mandantCNr,
			String auftragartCNr, Integer kundeIIdAuftragsadresse,
			Integer kundeIIdLieferadresse, Integer kundeIIdRechnungsadresse,
			String auftragswaehrung, Integer lagerIIdAbbuchungslager ) {
		Timestamp t = new Timestamp(System.currentTimeMillis());
		this.setTAnlegen(t);
		this.setTAendern(t);

		setIId(id);
		setCNr(nr);
		setMandantCNr(mandantCNr);
		setAuftragartCNr(auftragartCNr);
		setKundeIIdAuftragsadresse(kundeIIdAuftragsadresse);
		setKundeIIdLieferadresse(kundeIIdLieferadresse);
		setKundeIIdRechnungsadresse(kundeIIdRechnungsadresse);
		setWaehrungCNrAuftragswaehrung(auftragswaehrung);

		this.setBLieferterminunverbindlich(new Short((short) 0));
		this.setBTeillieferungmoeglich(new Short((short) 1));
		this.setBPoenale(new Short((short) 0));
		this.setBRoHs(new Short((short) 0));
		this.setILeihtage(new Integer(0));
		this.setFVersteckteraufschlag(new Double(0));
		this.setFAllgemeinerrabattsatz(new Double(0));
		this.setFProjektierungsrabattsatz(new Double(0));
		this.setIGarantie(new Integer(0));
		this.setBVersteckt(new Short((short) 0));
		this.setBMitzusammenfassung(new Short((short) 0));
		setLagerIIdAbbuchungslager(lagerIIdAbbuchungslager);
	}

	public Auftrag(Integer id, String nr, String mandantCNr,
			String auftragartCNr, String auftragstatusCNr, String belegartCNr,
			Timestamp tBelegdatum, Integer kundeIIdAuftragsadresse,
			Integer personalIIdVertreter, Integer kundeIIdLieferadresse,
			Integer kundeIIdRechnungsadresse, String auftragswaehrung,
			Double fWechselkursmandantwaehrungzuauftragswaehrung,
			Timestamp tLiefertermin, Short lieferterminunverbindlich,
			Timestamp tFinaltermin, Integer kostenstelleIId,
			Short teillieferungmoeglich, Short poenale, Short bRoHs,
			Integer leihtage, Double versteckteraufschlag,
			Double allgemeinerrabattsatz, Double projektierungsrabattsatz,
			Integer lieferartIId, Integer zahlungszielIId,
			Integer spediteurIId, Integer garantie, Integer personalIIdAnlegen,
			Integer personalIIdAendern, Short bVersteckt,
			Short bMitzusammenfassung, Integer lagerIIdAbbuchungslager ) {
		Timestamp t = new Timestamp(System.currentTimeMillis());
		this.setTAnlegen(t);
		this.setTAendern(t);

		setIId(id);
		setCNr(nr);
		setMandantCNr(mandantCNr);
		setAuftragartCNr(auftragartCNr);
		setKundeIIdAuftragsadresse(kundeIIdAuftragsadresse);
		setKundeIIdLieferadresse(kundeIIdLieferadresse);
		setKundeIIdRechnungsadresse(kundeIIdRechnungsadresse);
		setWaehrungCNrAuftragswaehrung(auftragswaehrung);
		setTBelegdatum(tBelegdatum);
		setTLiefertermin(tLiefertermin);
		setLieferartIId(lieferartIId);
		setZahlungszielIId(zahlungszielIId);
		setSpediteurIId(spediteurIId);
		setAuftragstatusCNr(auftragstatusCNr);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setPersonalIIdAendern(personalIIdAendern);
		setKostenstelleIId(kostenstelleIId);
		setPersonalIIdVertreter(personalIIdVertreter);
		setBelegartCNr(belegartCNr);
		setFWechselkursmandantwaehrungzuauftragswaehrung(fWechselkursmandantwaehrungzuauftragswaehrung);
		setTFinaltermin(tFinaltermin);
		setBLieferterminunverbindlich(lieferterminunverbindlich);
		setBTeillieferungmoeglich(teillieferungmoeglich);
		setBPoenale(poenale);
		setBRoHs(bRoHs);
		setILeihtage(leihtage);
		setFVersteckteraufschlag(versteckteraufschlag);
		setFAllgemeinerrabattsatz(allgemeinerrabattsatz);
		setFProjektierungsrabattsatz(projektierungsrabattsatz);
		setIGarantie(garantie);
		setFWechselkursmandantwaehrungzuauftragswaehrung(fWechselkursmandantwaehrungzuauftragswaehrung);
		setZahlungszielIId(zahlungszielIId);
		setPersonalIIdVertreter(personalIIdVertreter);
		setBVersteckt(bVersteckt);
		setBMitzusammenfassung(bMitzusammenfassung);
		setLagerIIdAbbuchungslager(lagerIIdAbbuchungslager);

	}

	public Auftrag() {

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

	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public String getCBestellnummer() {
		return this.cBestellnummer;
	}

	public void setCBestellnummer(String cBestellnummer) {
		this.cBestellnummer = cBestellnummer;
	}

	public Timestamp getTBestelldatum() {
		return this.tBestelldatum;
	}

	public void setTBestelldatum(Timestamp tBestelldatum) {
		this.tBestelldatum = tBestelldatum;
	}

	public Double getFWechselkursmandantwaehrungzuauftragswaehrung() {
		return this.fWechselkursmandantwaehrungzuauftragswaehrung;
	}

	public void setFWechselkursmandantwaehrungzuauftragswaehrung(
			Double fWechselkursmandantwaehrungzuauftragswaehrung) {
		this.fWechselkursmandantwaehrungzuauftragswaehrung = fWechselkursmandantwaehrungzuauftragswaehrung;
	}

	public Double getFSonderrabattsatz() {
		return this.fSonderrabattsatz;
	}

	public void setFSonderrabattsatz(Double fSonderrabattsatz) {
		this.fSonderrabattsatz = fSonderrabattsatz;
	}

	public Timestamp getTLiefertermin() {
		return this.tLiefertermin;
	}

	public void setTLiefertermin(Timestamp tLiefertermin) {
		this.tLiefertermin = tLiefertermin;
	}

	public Short getBLieferterminunverbindlich() {
		return this.bLieferterminunverbindlich;
	}

	public void setBLieferterminunverbindlich(Short bLieferterminunverbindlich) {
		this.bLieferterminunverbindlich = bLieferterminunverbindlich;
	}

	public Timestamp getTFinaltermin() {
		return this.tFinaltermin;
	}

	public void setTFinaltermin(Timestamp tFinaltermin) {
		this.tFinaltermin = tFinaltermin;
	}

	public Short getBTeillieferungmoeglich() {
		return this.bTeillieferungmoeglich;
	}

	public void setBTeillieferungmoeglich(Short bTeillieferungmoeglich) {
		this.bTeillieferungmoeglich = bTeillieferungmoeglich;
	}

	public Short getBPoenale() {
		return this.bPoenale;
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
		return this.iLeihtage;
	}

	public void setILeihtage(Integer iLeihtage) {
		this.iLeihtage = iLeihtage;
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

	public BigDecimal getNGesamtauftragswertinauftragswaehrung() {
		return this.nGesamtauftragswertinauftragswaehrung;
	}

	public void setNGesamtauftragswertinauftragswaehrung(
			BigDecimal nGesamtauftragswertinauftragswaehrung) {
		this.nGesamtauftragswertinauftragswaehrung = nGesamtauftragswertinauftragswaehrung;
	}

	public BigDecimal getNMaterialwertinmandantenwaehrung() {
		return this.nMaterialwertinmandantenwaehrung;
	}

	public void setNMaterialwertinmandantenwaehrung(
			BigDecimal nMaterialwertinmandantenwaehrung) {
		this.nMaterialwertinmandantenwaehrung = nMaterialwertinmandantenwaehrung;
	}

	public BigDecimal getNRohdeckunginmandantenwaehrung() {
		return this.nRohdeckunginmandantenwaehrung;
	}

	public void setNRohdeckunginmandantenwaehrung(
			BigDecimal nRohdeckunginmandantenwaehrung) {
		this.nRohdeckunginmandantenwaehrung = nRohdeckunginmandantenwaehrung;
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

	public Integer getAuftragIIdRahmenauftrag() {
		return this.auftragIIdRahmenauftrag;
	}

	public void setAuftragIIdRahmenauftrag(Integer auftragIIdRahmenauftrag) {
		this.auftragIIdRahmenauftrag = auftragIIdRahmenauftrag;
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

	public Timestamp getTErledigt() {
		return this.tErledigt;
	}

	public void setTErledigt(Timestamp tErledigt) {
		this.tErledigt = tErledigt;
	}

	public Timestamp getTLauftermin() {
		return this.tLauftermin;
	}

	public void setTLauftermin(Timestamp tLauftermin) {
		this.tLauftermin = tLauftermin;
	}

	public Double getFErfuellungsgrad() {
		return this.fErfuellungsgrad;
	}

	public void setFErfuellungsgrad(Double fErfuellungsgrad) {
		this.fErfuellungsgrad = fErfuellungsgrad;
	}

	public Integer getAngebotIId() {
		return this.angebotIId;
	}

	public void setAngebotIId(Integer angebotIId) {
		this.angebotIId = angebotIId;
	}

	public String getAuftragartCNr() {
		return this.auftragartCNr;
	}

	public void setAuftragartCNr(String auftragartCNr) {
		this.auftragartCNr = auftragartCNr;
	}

	public String getAuftragstatusCNr() {
		return this.auftragstatusCNr;
	}

	public void setAuftragstatusCNr(String auftragstatusCNr) {
		this.auftragstatusCNr = auftragstatusCNr;
	}

	public Integer getAuftragtextIIdKopftext() {
		return this.auftragtextIIdKopftext;
	}

	public void setAuftragtextIIdKopftext(Integer auftragtextIIdKopftext) {
		this.auftragtextIIdKopftext = auftragtextIIdKopftext;
	}

	public Integer getAuftragtextIIdFusstext() {
		return this.auftragtextIIdFusstext;
	}

	public void setAuftragtextIIdFusstext(Integer auftragtextIIdFusstext) {
		this.auftragtextIIdFusstext = auftragtextIIdFusstext;
	}

	public String getAuftragwiederholungsintervallCNr() {
		return this.auftragwiederholungsintervallCNr;
	}

	public void setAuftragwiederholungsintervallCNr(
			String auftragwiederholungsintervallCNr) {
		this.auftragwiederholungsintervallCNr = auftragwiederholungsintervallCNr;
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

	public String getWaehrungCNrAuftragswaehrung() {
		return this.waehrungCNrAuftragswaehrung;
	}

	public void setWaehrungCNrAuftragswaehrung(
			String waehrungCNrAuftragswaehrung) {
		this.waehrungCNrAuftragswaehrung = waehrungCNrAuftragswaehrung;
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

	public Integer getKundeIIdLieferadresse() {
		return this.kundeIIdLieferadresse;
	}

	public void setKundeIIdLieferadresse(Integer kundeIIdLieferadresse) {
		this.kundeIIdLieferadresse = kundeIIdLieferadresse;
	}

	public Integer getKundeIIdAuftragsadresse() {
		return this.kundeIIdAuftragsadresse;
	}

	public void setKundeIIdAuftragsadresse(Integer kundeIIdAuftragsadresse) {
		this.kundeIIdAuftragsadresse = kundeIIdAuftragsadresse;
	}

	public Integer getKundeIIdRechnungsadresse() {
		return this.kundeIIdRechnungsadresse;
	}

	public void setKundeIIdRechnungsadresse(Integer kundeIIdRechnungsadresse) {
		this.kundeIIdRechnungsadresse = kundeIIdRechnungsadresse;
	}

	public Integer getPersonalIIdErledigt() {
		return this.personalIIdErledigt;
	}

	public void setPersonalIIdErledigt(Integer personalIIdErledigt) {
		this.personalIIdErledigt = personalIIdErledigt;
	}

	public Integer getPersonalIIdStorniert() {
		return this.personalIIdStorniert;
	}

	public void setPersonalIIdStorniert(Integer personalIIdStorniert) {
		this.personalIIdStorniert = personalIIdStorniert;
	}

	public Integer getPersonalIIdVertreter() {
		return this.personalIIdVertreter;
	}

	public void setPersonalIIdVertreter(Integer personalIIdVertreter) {
		this.personalIIdVertreter = personalIIdVertreter;
	}

	public Integer getPersonalIIdManuellerledigt() {
		return this.personalIIdManuellerledigt;
	}

	public void setPersonalIIdManuellerledigt(Integer personalIIdManuellerledigt) {
		this.personalIIdManuellerledigt = personalIIdManuellerledigt;
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

	public void setNRohdeckungaltinmandantenwaehrung(
			BigDecimal nRohdeckungaltinmandantenwaehrung) {
		this.nRohdeckungaltinmandantenwaehrung = nRohdeckungaltinmandantenwaehrung;
	}

	public BigDecimal getNRohdeckungaltinmandantenwaehrung() {
		return nRohdeckungaltinmandantenwaehrung;
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
}

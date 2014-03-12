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
package com.lp.server.anfrage.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "AnfragefindByMandant", query = "SELECT OBJECT (o) FROM Anfrage o WHERE o.mandantCNr=?1 ORDER BY o.cNr"),
		@NamedQuery(name = "AnfragefindByAnfrageIIdLiefergruppenanfrage", query = "SELECT OBJECT (o) FROM Anfrage o WHERE o.anfrageIIdLiefergruppenanfrage=?1"),
		@NamedQuery(name = "AnfragefindByLieferantIIdAnfrageadresseMandantCNr", query = "SELECT OBJECT (O) FROM Anfrage o WHERE o.lieferantIIdAnfrageadresse=?1 AND  o.mandantCNr=?2"),
		@NamedQuery(name = "AnfragefindByAnsprechpartnerlieferantIIdMandantCNr", query = "SELECT OBJECT (O) FROM Anfrage o WHERE o.ansprechpartnerIIdLieferant=?1 AND  o.mandantCNr=?2"), 
		@NamedQuery(name = Anfrage.QueryFindByCnrMandantCnr, query = "SELECT OBJECT (O) FROM Anfrage o WHERE o.cNr = :cnr AND o.mandantCNr= :mandant")})
@Entity
@Table(name = "ANF_ANFRAGE")
public class Anfrage implements Serializable {
	public final static String QueryFindByCnrMandantCnr = "AnfrageFindByCnrMandantCNr";
	
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "T_BELEGDATUM")
	private Timestamp tBelegdatum;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "C_ANGEBOTNUMMER")
	private String cAngebotnummer;

	@Column(name = "F_WECHSELKURSMANDANTWAEHRUNGZUANFRAGEWAEHRUNG")
	private Double fWechselkursmandantwaehrungzuanfragewaehrung;

	@Column(name = "T_ANLIEFERTERMIN")
	private Timestamp tAnliefertermin;

	@Column(name = "F_ALLGEMEINERRABATTSATZ")
	private Double fAllgemeinerrabattsatz;

	@Column(name = "N_GESAMTANFRAGEWERTINANFRAGEWAEHRUNG")
	private BigDecimal nGesamtanfragewertinanfragewaehrung;

	@Column(name = "N_TRANSPORTKOSTENINANFRAGEWAEHRUNG")
	private BigDecimal nTransportkosteninanfragewaehrung;

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

	@Column(name = "ANFRAGE_I_ID_LIEFERGRUPPENANFRAGE")
	private Integer anfrageIIdLiefergruppenanfrage;

	@Column(name = "ANFRAGEART_C_NR")
	private String anfrageartCNr;

	@Column(name = "ANFRAGESTATUS_C_NR")
	private String anfragestatusCNr;

	@Column(name = "ANFRAGETEXT_I_ID_FUSSTEXT")
	private Integer anfragetextIIdFusstext;

	@Column(name = "ANFRAGETEXT_I_ID_KOPFTEXT")
	private Integer anfragetextIIdKopftext;

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

	@Column(name = "WAEHRUNG_C_NR_ANFRAGEWAEHRUNG")
	private String waehrungCNrAnfragewaehrung;

	@Column(name = "ZAHLUNGSZIEL_I_ID")
	private Integer zahlungszielIId;

	@Column(name = "ANSPRECHPARTNER_I_ID_LIEFERANT")
	private Integer ansprechpartnerIIdLieferant;

	@Column(name = "LFLIEFERGRUPPE_I_ID")
	private Integer lfliefergruppe;

	@Column(name = "LIEFERANT_I_ID_ANFRAGEADRESSE")
	private Integer lieferantIIdAnfrageadresse;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_STORNIERT")
	private Integer personalIIdStorniert;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "PERSONAL_I_ID_MANUELLERLEDIGT")
	private Integer personalIIdManuellerledigt;

	@Column(name = "T_VERSANDZEITPUNKT")
	private Timestamp tVersandzeitpunkt;

	@Column(name = "C_VERSANDTYPE")
	private String cVersandtype;

	@Column(name = "T_ANGEBOTDATUM")
	private Timestamp tAngebotdatum;
	
	@Column(name = "T_ANGEBOTGUELTIGBIS")
	private Timestamp tAngebotgueltigbis;

	public Timestamp getTAngebotdatum() {
		return tAngebotdatum;
	}

	public void setTAngebotdatum(Timestamp angebotdatum) {
		tAngebotdatum = angebotdatum;
	}

	public Timestamp getTAngebotgueltigbis() {
		return tAngebotgueltigbis;
	}

	public void setTAngebotgueltigbis(Timestamp angebotgueltigbis) {
		tAngebotgueltigbis = angebotgueltigbis;
	}
	@Column(name = "PROJEKT_I_ID")
	private Integer projektIId;
	
	public Integer getProjektIId() {
		return projektIId;
	}

	public void setProjektIId(Integer projektIId) {
		this.projektIId = projektIId;
	}

	@Column(name = "C_LIEFERARTORT")
	private String cLieferartort;
	
	
	public String getCLieferartort() {
		return cLieferartort;
	}

	public void setCLieferartort(String cLieferartort) {
		this.cLieferartort = cLieferartort;
	}

	private static final long serialVersionUID = 1L;

	public Anfrage() {
		super();
	}

	public Anfrage(Integer id, String nr, String mandantCNr, String statusCNr,
			String artCNr, String belegartCNr, Timestamp belegdatum,
			String waehrungCNr,
			Double wechselkursmandantwaehrungzubelegwaehrung,
			Integer kostenstelleIId, Double allgemeinerrabattsatz,
			BigDecimal transportkosteninanfragewaehrung,
			Integer belegtextIIdKopftext, Integer belegtextIIdFusstext,
			Integer personalIIdAnlegen2, Integer personalIIdAendern2) {
		setIId(id);
		Timestamp t = new Timestamp(System.currentTimeMillis());
		this.setTAnlegen(t);
		this.setTAendern(t);
		setCNr(nr);
		setMandantCNr(mandantCNr);
		setAnfrageartCNr(artCNr);
		setAnfragestatusCNr(statusCNr);
		setBelegartCNr(belegartCNr);
		setTBelegdatum(belegdatum);
		setWaehrungCNrAnfragewaehrung(waehrungCNr);
		setFWechselkursmandantwaehrungzuanfragewaehrung(wechselkursmandantwaehrungzubelegwaehrung);
		setKostenstelleIId(kostenstelleIId);
		setFAllgemeinerrabattsatz(allgemeinerrabattsatz);
		setNTransportkosteninanfragewaehrung(transportkosteninanfragewaehrung);
		setAnfragetextIIdKopftext(belegtextIIdKopftext);
		setAnfragetextIIdFusstext(belegtextIIdFusstext);
		setPersonalIIdAnlegen(personalIIdAnlegen2);
		setPersonalIIdAendern(personalIIdAendern2);
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

	public String getCAngebotnummer() {
		return this.cAngebotnummer;
	}

	public void setCAngebotnummer(String cAngebotnummer) {
		this.cAngebotnummer = cAngebotnummer;
	}

	public Double getFWechselkursmandantwaehrungzuanfragewaehrung() {
		return this.fWechselkursmandantwaehrungzuanfragewaehrung;
	}

	public void setFWechselkursmandantwaehrungzuanfragewaehrung(
			Double fWechselkursmandantwaehrungzuanfragewaehrung) {
		this.fWechselkursmandantwaehrungzuanfragewaehrung = fWechselkursmandantwaehrungzuanfragewaehrung;
	}

	public Timestamp getTAnliefertermin() {
		return this.tAnliefertermin;
	}

	public void setTAnliefertermin(Timestamp tAnliefertermin) {
		this.tAnliefertermin = tAnliefertermin;
	}

	public Double getFAllgemeinerrabattsatz() {
		return this.fAllgemeinerrabattsatz;
	}

	public void setFAllgemeinerrabattsatz(Double fAllgemeinerrabattsatz) {
		this.fAllgemeinerrabattsatz = fAllgemeinerrabattsatz;
	}

	public BigDecimal getNGesamtanfragewertinanfragewaehrung() {
		return this.nGesamtanfragewertinanfragewaehrung;
	}

	public void setNGesamtanfragewertinanfragewaehrung(
			BigDecimal nGesamtanfragewertinanfragewaehrung) {
		this.nGesamtanfragewertinanfragewaehrung = nGesamtanfragewertinanfragewaehrung;
	}

	public BigDecimal getNTransportkosteninanfragewaehrung() {
		return this.nTransportkosteninanfragewaehrung;
	}

	public void setNTransportkosteninanfragewaehrung(
			BigDecimal nTransportkosteninanfragewaehrung) {
		this.nTransportkosteninanfragewaehrung = nTransportkosteninanfragewaehrung;
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

	public Integer getAnfrageIIdLiefergruppenanfrage() {
		return this.anfrageIIdLiefergruppenanfrage;
	}

	public void setAnfrageIIdLiefergruppenanfrage(
			Integer anfrageIIdLiefergruppenanfrage) {
		this.anfrageIIdLiefergruppenanfrage = anfrageIIdLiefergruppenanfrage;
	}

	public String getAnfrageartCNr() {
		return this.anfrageartCNr;
	}

	public void setAnfrageartCNr(String anfrageart) {
		this.anfrageartCNr = anfrageart;
	}

	public String getAnfragestatusCNr() {
		return this.anfragestatusCNr;
	}

	public void setAnfragestatusCNr(String anfragestatusCNr) {
		this.anfragestatusCNr = anfragestatusCNr;
	}

	public Integer getAnfragetextIIdFusstext() {
		return this.anfragetextIIdFusstext;
	}

	public void setAnfragetextIIdFusstext(Integer anfragetextIIdFusstext) {
		this.anfragetextIIdFusstext = anfragetextIIdFusstext;
	}

	public Integer getAnfragetextIIdKopftext() {
		return this.anfragetextIIdKopftext;
	}

	public void setAnfragetextIIdKopftext(Integer anfragetextIIdKopftext) {
		this.anfragetextIIdKopftext = anfragetextIIdKopftext;
	}

	public String getBelegartCNr() {
		return this.belegartCNr;
	}

	public void setBelegartCNr(String belegart) {
		this.belegartCNr = belegart;
	}

	public Integer getKostenstelleIId() {
		return this.kostenstelleIId;
	}

	public void setKostenstelleIId(Integer kostenstelle) {
		this.kostenstelleIId = kostenstelle;
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

	public String getWaehrungCNrAnfragewaehrung() {
		return this.waehrungCNrAnfragewaehrung;
	}

	public void setWaehrungCNrAnfragewaehrung(String waehrungCNrAnfragewaehrung) {
		this.waehrungCNrAnfragewaehrung = waehrungCNrAnfragewaehrung;
	}

	public Integer getZahlungszielIId() {
		return this.zahlungszielIId;
	}

	public void setZahlungszielIId(Integer zahlungsziel) {
		this.zahlungszielIId = zahlungsziel;
	}

	public Integer getAnsprechpartnerIIdLieferant() {
		return this.ansprechpartnerIIdLieferant;
	}

	public void setAnsprechpartnerIIdLieferant(
			Integer ansprechpartnerIIdLieferant) {
		this.ansprechpartnerIIdLieferant = ansprechpartnerIIdLieferant;
	}

	public Integer getLfliefergruppeIId() {
		return this.lfliefergruppe;
	}

	public void setLieferguppeIId(Integer lfliefergruppe) {
		this.lfliefergruppe = lfliefergruppe;
	}

	public Integer getLieferantIIdAnfrageadresse() {
		return this.lieferantIIdAnfrageadresse;
	}

	public void setLieferantIIdAnfrageadresse(Integer lieferantIIdAnfrageadresse) {
		this.lieferantIIdAnfrageadresse = lieferantIIdAnfrageadresse;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getPersonalIIdStorniert() {
		return this.personalIIdStorniert;
	}

	public void setPersonalIIdStorniert(Integer personalIIdStorniert) {
		this.personalIIdStorniert = personalIIdStorniert;
	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Integer getPersonalIIdManuellerledigt() {
		return this.personalIIdManuellerledigt;
	}

	public void setPersonalIIdManuellerledigt(Integer personalIIdManuellerledigt) {
		this.personalIIdManuellerledigt = personalIIdManuellerledigt;
	}

	public void setLieferartIId(Integer lieferartIId) {
		this.lieferartIId = lieferartIId;
	}

	public Integer getLieferartIId() {
		return lieferartIId;
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

}

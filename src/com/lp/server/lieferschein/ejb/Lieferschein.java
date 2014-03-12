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
package com.lp.server.lieferschein.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "LieferscheinfindByBelegdatumVonBelegdatumBis", query = "SELECT OBJECT (o) FROM Lieferschein o WHERE o.tBelegdatum>=?1 AND o.tBelegdatum<=?2"),
		@NamedQuery(name = "LieferscheinfindByStatus1Status2", query = "SELECT OBJECT (o) FROM Lieferschein o WHERE o.lieferscheinstatusCNr=?1 OR o.lieferscheinstatusCNr=?2"),
		@NamedQuery(name = "LieferscheinejbSelectCountLieferschein", query = "SELECT COUNT (o) FROM Lieferschein AS o WHERE o.tAnlegen>=?1 AND o.tAnlegen<=?2"),
		@NamedQuery(name = "LieferscheinfindByMandantVonBisStatus1Status2Status3Status4", query = "SELECT OBJECT (o) FROM Lieferschein o WHERE o.mandantCNr=?1 AND o.tBelegdatum>=?2 AND o.tBelegdatum<=?3 AND (o.lieferscheinstatusCNr=?4 OR o.lieferscheinstatusCNr=?5 OR o.lieferscheinstatusCNr=?6 OR o.lieferscheinstatusCNr=?7)"),
		@NamedQuery(name = "LieferscheinfindByMandantAndStatus", query = "SELECT OBJECT (o) FROM Lieferschein o WHERE o.mandantCNr=?1 AND o.lieferscheinstatusCNr=?2"),
		@NamedQuery(name = "LieferscheinfindByKundeIIdLieferadresseMandantCNr", query = "SELECT OBJECT (O) FROM Lieferschein o WHERE o.kundeIIdLieferadresse=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "LieferscheinfindByKundeIIdRechnungsadresseMandantCNr", query = "SELECT OBJECT (O) FROM Lieferschein o WHERE o.kundeIIdRechnungsadresse=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "LieferscheinfindByAnsprechpartnerIIdMandantCNr", query = "SELECT OBJECT (O) FROM Lieferschein o WHERE o.ansprechpartnerIIdKunde=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "LieferscheinfindByCNrMandantCNr", query = "SELECT OBJECT (O) FROM Lieferschein o WHERE o.cNr=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "LieferscheinfindByAnsprechpartnerIIdRechnungsadresseMandantCNr", query = "SELECT OBJECT (O) FROM Lieferschein o WHERE o.ansprechpartnerIIdRechnungsadresse=?1 AND o.mandantCNr=?2") })
@Entity
@Table(name = "LS_LIEFERSCHEIN")
public class Lieferschein implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "T_BELEGDATUM")
	private Timestamp tBelegdatum;

	@Column(name = "B_VERRECHENBAR")
	private Short bVerrechenbar;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "C_BESTELLNUMMER")
	private String cBestellnummer;

	@Column(name = "F_WECHSELKURSMANDANTWAEHRUNGZULIEFERSCHEINWAEHRUNG")
	private Double fWechselkursmandantwaehrungzulieferscheinwaehrung;

	@Column(name = "F_VERSTECKTERAUFSCHLAG")
	private Double fVersteckteraufschlag;

	@Column(name = "F_ALLGEMEINERRABATT")
	private Double fAllgemeinerrabatt;

	@Column(name = "N_GESAMTWERTINLIEFERSCHEINWAEHRUNG")
	private BigDecimal nGesamtwertinlieferscheinwaehrung;

	@Column(name = "N_GESTEHUNGSWERTINMANDANTENWAEHRUNG")
	private BigDecimal nGestehungswertinmandantenwaehrung;

	@Column(name = "T_LIEFERTERMIN")
	private Timestamp tLiefertermin;

	@Column(name = "T_RUECKGABETERMIN")
	private Timestamp tRueckgabetermin;

	@Column(name = "B_MINDERMENGENZUSCHLAG")
	private Short bMindermengenzuschlag;

	@Column(name = "I_ANZAHLPAKETE")
	private Integer iAnzahlpakete;

	@Column(name = "F_GEWICHTLIEFERUNG")
	private Double fGewichtlieferung;

	@Column(name = "C_VERSANDNUMMER")
	private String cVersandnummer;

	@Column(name = "X_KOPFTEXTUEBERSTEUERT")
	private String xKopftextuebersteuert;

	@Column(name = "X_FUSSTEXTUEBERSTEUERT")
	private String xFusstextuebersteuert;

	@Column(name = "T_GEDRUCKT")
	private Timestamp tGedruckt;

	@Column(name = "T_MANUELLERLEDIGT")
	private Timestamp tManuellerledigt;

	@Column(name = "T_STORNIERT")
	private Timestamp tStorniert;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "ZIELLAGER_I_ID")
	private Integer ziellagerIId;

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

	@Column(name = "ANSPRECHPARTNER_I_ID_RECHNUNGSADRESSE")
	private Integer ansprechpartnerIIdRechnungsadresse;

	public Integer getAnsprechpartnerIIdRechnungsadresse() {
		return ansprechpartnerIIdRechnungsadresse;
	}

	public void setAnsprechpartnerIIdRechnungsadresse(
			Integer ansprechpartnerIIdRechnungsadresse) {
		this.ansprechpartnerIIdRechnungsadresse = ansprechpartnerIIdRechnungsadresse;
	}

	@Column(name = "BEGRUENDUNG_I_ID")
	private Integer begruendungIId;

	public Integer getBegruendungIId() {
		return begruendungIId;
	}

	public void setBegruendungIId(Integer begruendungIId) {
		this.begruendungIId = begruendungIId;
	}

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

	@Column(name = "WAEHRUNG_C_NR_LIEFERSCHEINWAEHRUNG")
	private String waehrungCNrLieferscheinwaehrung;

	@Column(name = "ZAHLUNGSZIEL_I_ID")
	private Integer zahlungszielIId;

	@Column(name = "LIEFERSCHEINART_C_NR")
	private String lieferscheinartCNr;

	@Column(name = "LIEFERSCHEINSTATUS_C_NR")
	private String lieferscheinstatusCNr;

	@Column(name = "LIEFERSCHEINTEXT_I_ID_KOPFTEXT")
	private Integer lieferscheintextIIdKopftext;

	@Column(name = "LIEFERSCHEINTEXT_I_ID_FUSSTEXT")
	private Integer lieferscheintextIIdFusstext;

	@Column(name = "ANSPRECHPARTNER_I_ID_KUNDE")
	private Integer ansprechpartnerIIdKunde;

	@Column(name = "KUNDE_I_ID_LIEFERADRESSE")
	private Integer kundeIIdLieferadresse;

	@Column(name = "KUNDE_I_ID_RECHNUNGSADRESSE")
	private Integer kundeIIdRechnungsadresse;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "PERSONAL_I_ID_VERTRETER")
	private Integer personalIIdVertreter;

	@Column(name = "PERSONAL_I_ID_STORNIERT")
	private Integer personalIIdStorniert;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_MANUELLERLEDIGT")
	private Integer personalIIdManuellerledigt;

	@Column(name = "RECHNUNG_I_ID")
	private Integer rechnungIId;

	@Column(name = "LAGER_I_ID")
	private Integer lagerIId;

	@Column(name = "C_KOMMISSION")
	private String cKommission;

	@Column(name = "T_VERSANDZEITPUNKT")
	private Timestamp tVersandzeitpunkt;

	@Column(name = "C_VERSANDTYPE")
	private String cVersandtype;

	public String getCKommission() {
		return cKommission;
	}

	public void setCKommission(String kommission) {
		cKommission = kommission;
	}

	@Column(name = "C_LIEFERARTORT")
	private String cLieferartort;

	@Column(name = "T_LIEFERAVISO")
	private Timestamp tLieferaviso ;
	
	@Column(name = "PERSONAL_I_ID_LIEFERAVISO")
	private Integer personalIIdLieferaviso;

	public String getCLieferartort() {
		return cLieferartort;
	}

	public void setCLieferartort(String cLieferartort) {
		this.cLieferartort = cLieferartort;
	}

	@Column(name = "EINGANGSRECHNUNG_I_ID_ZOLLEXPORT")
	private Integer eingangsrechnungIdZollexport;

	public Integer getEingangsrechnungIdZollexport() {
		return eingangsrechnungIdZollexport;
	}

	public void setEingangsrechnungIdZollexport(
			Integer eingangsrechnungIdZollexport) {
		this.eingangsrechnungIdZollexport = eingangsrechnungIdZollexport;
	}

	@Column(name = "T_ZOLLEXPORTPAPIER")
	private Timestamp tZollexportpapier;
	@Column(name = "PERSONAL_I_ID_ZOLLEXPORTPAPIER")
	private Integer personalIIdZollexportpapier;

	@Column(name = "C_ZOLLEXPORTPAPIER")
	private String cZollexportpapier;

	public String getCZollexportpapier() {
		return cZollexportpapier;
	}

	public void setCZollexportpapier(String cZollexportpapier) {
		this.cZollexportpapier = cZollexportpapier;
	}

	public Timestamp getTZollexportpapier() {
		return tZollexportpapier;
	}

	public void setTZollexportpapier(Timestamp tZollexportpapier) {
		this.tZollexportpapier = tZollexportpapier;
	}

	public Integer getPersonalIIdZollexportpapier() {
		return personalIIdZollexportpapier;
	}

	public void setPersonalIIdZollexportpapier(
			Integer personalIIdZollexportpapier) {
		this.personalIIdZollexportpapier = personalIIdZollexportpapier;
	}

	private static final long serialVersionUID = 1L;

	public Lieferschein() {
		super();
	}

	public Lieferschein(Integer id, String nr, String mandantCNr,
			String lieferscheinartCNr, String lieferscheinstatus,
			String belegartLieferschein, Timestamp belegdatum, Short verrechenbar,
			Integer kundeIIdLieferadresse, Integer personalIIdVertreter,
			Integer kundeIIdRechnungsadresse, String bezProjektbezeichnung,
			String bestellnummer, String lieferscheinwaehrung,
			Double wechselkursMandantenwaehrungZuLieferscheinwaehrung,
			Double versteckteraufschlag, Double allgemeinerrabatt,
			Integer kostenstelleIId, Integer lagerIId, Timestamp liefertermin,
			Timestamp rueckgabetermin, Integer lieferartIId,
			Integer zahlungszielIId, Integer spediteurIId,
			Short mindermengenzuschlag, Integer anzahlpakete,
			Double gewichtlieferung,
			Integer lieferscheintextIIdDefaultKopftext,
			Integer lieferscheintextIIdDefaultFusstext,
			Integer personalIIdAnlegen, Integer personalIIdAendern) {
		setIId(id);
		setCNr(nr);
		setLieferscheinartCNr(lieferscheinartCNr);
		setTBelegdatum(belegdatum);
		setKundeIIdLieferadresse(kundeIIdLieferadresse);
		setPersonalIIdVertreter(personalIIdVertreter);
		setKundeIIdRechnungsadresse(kundeIIdRechnungsadresse);
		setCBez(bezProjektbezeichnung);
		setCBestellnummer(bestellnummer);
		setKostenstelleIId(kostenstelleIId);
		setTLiefertermin(liefertermin);
		setTRueckgabetermin(rueckgabetermin);
		setLieferartIId(lieferartIId);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setPersonalIIdAendern(personalIIdAendern);
		setMandantCNr(mandantCNr);
		setLieferscheinstatusCNr(lieferscheinstatus);
		setLagerIId(lagerIId);
		setWaehrungCNrLieferscheinwaehrung(lieferscheinwaehrung);
		setFWechselkursmandantwaehrungzulieferscheinwaehrung(wechselkursMandantenwaehrungZuLieferscheinwaehrung);
		setZahlungszielIId(zahlungszielIId);
		setSpediteurIId(spediteurIId);
		setLieferscheintextIIdKopftext(lieferscheintextIIdDefaultKopftext);
		setLieferscheintextIIdFusstext(lieferscheintextIIdDefaultFusstext);
		setBVerrechenbar(verrechenbar);
		setFAllgemeinerrabatt(allgemeinerrabatt);
		setFVersteckteraufschlag(versteckteraufschlag);
		setBMindermengenzuschlag(mindermengenzuschlag);
		setIAnzahlpakete(anzahlpakete);
		setFGewichtlieferung(gewichtlieferung);
		setBelegartCNr(belegartLieferschein);
		Timestamp t = new java.sql.Timestamp(System.currentTimeMillis());
		this.setTAendern(t);
		this.setTAnlegen(t);
	}

	public Lieferschein(Integer id, String nr, String lieferscheinartCNr,
			Timestamp belegdatum, Integer kundeIIdLieferadresse2,
			Integer personalIIdVertreter2, Integer kundeIIdRechnungsadresse2,
			String bezProjektbezeichnung, String bestellnummer,
			Integer kostenstelleIId2, Timestamp liefertermin,
			Timestamp rueckgabetermin, Integer lieferartIId2,
			Integer personalIIdAnlegen2, Integer personalIIdAendern2,
			String mandantCNr2, String lieferscheinstatus, Integer lagerIId2,
			String belegartLieferschein, String lieferscheinwaehrung,
			Double wechselkursMandantenwaehrungZuLieferscheinwaehrung,
			Integer zahlungszielIId2, Integer spediteurIId2,
			Integer lieferscheintextIIdDefaultKopftext,
			Integer lieferscheintextIIdDefaultFusstext) {
		setIId(id);
		setCNr(nr);
		setLieferscheinartCNr(lieferscheinartCNr);
		setTBelegdatum(belegdatum);
		setKundeIIdLieferadresse(kundeIIdLieferadresse2);
		setPersonalIIdVertreter(personalIIdVertreter2);
		setKundeIIdRechnungsadresse(kundeIIdRechnungsadresse2);
		setCBez(bezProjektbezeichnung);
		setCBestellnummer(bestellnummer);
		setKostenstelleIId(kostenstelleIId2);
		setTLiefertermin(liefertermin);
		setTRueckgabetermin(rueckgabetermin);
		setLieferartIId(lieferartIId2);
		setPersonalIIdAnlegen(personalIIdAnlegen2);
		setPersonalIIdAendern(personalIIdAendern2);
		setMandantCNr(mandantCNr2);
		setLieferscheinstatusCNr(lieferscheinstatus);
		setLagerIId(lagerIId2);
		setWaehrungCNrLieferscheinwaehrung(lieferscheinwaehrung);
		setFWechselkursmandantwaehrungzulieferscheinwaehrung(wechselkursMandantenwaehrungZuLieferscheinwaehrung);
		setZahlungszielIId(zahlungszielIId2);
		setSpediteurIId(spediteurIId2);
		setLieferscheintextIIdKopftext(lieferscheintextIIdDefaultKopftext);
		setLieferscheintextIIdFusstext(lieferscheintextIIdDefaultFusstext);
		setBelegartCNr(belegartLieferschein);
		// NOT_NULL default
		this.setBVerrechenbar(new Short((short) 1));
		this.setFAllgemeinerrabatt(new Double(0));
		this.setFVersteckteraufschlag(new Double(0));
		this.setBMindermengenzuschlag(new Short((short) 0));
		this.setIAnzahlpakete(new Integer(0));
		this.setFGewichtlieferung(new Double(0));

		Timestamp t = new java.sql.Timestamp(System.currentTimeMillis());
		this.setTAendern(t);
		this.setTAnlegen(t);
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

	public Short getBVerrechenbar() {
		return this.bVerrechenbar;
	}

	public void setBVerrechenbar(Short bVerrechenbar) {
		this.bVerrechenbar = bVerrechenbar;
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

	public Double getFWechselkursmandantwaehrungzulieferscheinwaehrung() {
		return this.fWechselkursmandantwaehrungzulieferscheinwaehrung;
	}

	public void setFWechselkursmandantwaehrungzulieferscheinwaehrung(
			Double fWechselkursmandantwaehrungzulieferscheinwaehrung) {
		this.fWechselkursmandantwaehrungzulieferscheinwaehrung = fWechselkursmandantwaehrungzulieferscheinwaehrung;
	}

	public Double getFVersteckteraufschlag() {
		return this.fVersteckteraufschlag;
	}

	public void setFVersteckteraufschlag(Double fVersteckteraufschlag) {
		this.fVersteckteraufschlag = fVersteckteraufschlag;
	}

	public Double getFAllgemeinerrabatt() {
		return this.fAllgemeinerrabatt;
	}

	public void setFAllgemeinerrabatt(Double fAllgemeinerrabatt) {
		this.fAllgemeinerrabatt = fAllgemeinerrabatt;
	}

	public BigDecimal getNGesamtwertinlieferscheinwaehrung() {
		return this.nGesamtwertinlieferscheinwaehrung;
	}

	public void setNGesamtwertinlieferscheinwaehrung(
			BigDecimal nGesamtwertinlieferscheinwaehrung) {
		this.nGesamtwertinlieferscheinwaehrung = nGesamtwertinlieferscheinwaehrung;
	}

	public BigDecimal getNGestehungswertinmandantenwaehrung() {
		return this.nGestehungswertinmandantenwaehrung;
	}

	public void setNGestehungswertinmandantenwaehrung(
			BigDecimal nGestehungswertinmandantenwaehrung) {
		this.nGestehungswertinmandantenwaehrung = nGestehungswertinmandantenwaehrung;
	}

	public Timestamp getTLiefertermin() {
		return this.tLiefertermin;
	}

	public void setTLiefertermin(Timestamp tLiefertermin) {
		this.tLiefertermin = tLiefertermin;
	}

	public Timestamp getTRueckgabetermin() {
		return this.tRueckgabetermin;
	}

	public void setTRueckgabetermin(Timestamp tRueckgabetermin) {
		this.tRueckgabetermin = tRueckgabetermin;
	}

	public Short getBMindermengenzuschlag() {
		return this.bMindermengenzuschlag;
	}

	public void setBMindermengenzuschlag(Short bMindermengenzuschlag) {
		this.bMindermengenzuschlag = bMindermengenzuschlag;
	}

	public Integer getIAnzahlpakete() {
		return this.iAnzahlpakete;
	}

	public void setIAnzahlpakete(Integer iAnzahlpakete) {
		this.iAnzahlpakete = iAnzahlpakete;
	}

	public Double getFGewichtlieferung() {
		return this.fGewichtlieferung;
	}

	public void setFGewichtlieferung(Double fGewichtlieferung) {
		this.fGewichtlieferung = fGewichtlieferung;
	}

	public String getCVersandnummer() {
		return this.cVersandnummer;
	}

	public void setCVersandnummer(String cVersandnummer) {
		this.cVersandnummer = cVersandnummer;
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

	public Timestamp getTManuellerledigt() {
		return this.tManuellerledigt;
	}

	public void setTManuellerledigt(Timestamp tManuellerledigt) {
		this.tManuellerledigt = tManuellerledigt;
	}

	public Timestamp getTStorniert() {
		return this.tStorniert;
	}

	public void setTStorniert(Timestamp tStorniert) {
		this.tStorniert = tStorniert;
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

	public Integer getZiellagerIId() {
		return this.ziellagerIId;
	}

	public void setZiellagerIId(Integer ziellagerIId) {
		this.ziellagerIId = ziellagerIId;
	}

	public Integer getAuftragIId() {
		return this.auftragIId;
	}

	public void setAuftragIId(Integer auftragIId) {
		this.auftragIId = auftragIId;
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

	public String getWaehrungCNrLieferscheinwaehrung() {
		return this.waehrungCNrLieferscheinwaehrung;
	}

	public void setWaehrungCNrLieferscheinwaehrung(
			String waehrungCNrLieferscheinwaehrung) {
		this.waehrungCNrLieferscheinwaehrung = waehrungCNrLieferscheinwaehrung;
	}

	public Integer getZahlungszielIId() {
		return this.zahlungszielIId;
	}

	public void setZahlungszielIId(Integer zahlungszielIId) {
		this.zahlungszielIId = zahlungszielIId;
	}

	public String getLieferscheinartCNr() {
		return this.lieferscheinartCNr;
	}

	public void setLieferscheinartCNr(String lieferscheinartCNr) {
		this.lieferscheinartCNr = lieferscheinartCNr;
	}

	public String getLieferscheinstatusCNr() {
		return this.lieferscheinstatusCNr;
	}

	public void setLieferscheinstatusCNr(String lieferscheinstatusCNr) {
		this.lieferscheinstatusCNr = lieferscheinstatusCNr;
	}

	public Integer getLieferscheintextIIdKopftext() {
		return this.lieferscheintextIIdKopftext;
	}

	public void setLieferscheintextIIdKopftext(
			Integer lieferscheintextIIdKopftext) {
		this.lieferscheintextIIdKopftext = lieferscheintextIIdKopftext;
	}

	public Integer getLieferscheintextIIdFusstext() {
		return this.lieferscheintextIIdFusstext;
	}

	public void setLieferscheintextIIdFusstext(
			Integer lieferscheintextIIdFusstext) {
		this.lieferscheintextIIdFusstext = lieferscheintextIIdFusstext;
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

	public Integer getKundeIIdRechnungsadresse() {
		return this.kundeIIdRechnungsadresse;
	}

	public void setKundeIIdRechnungsadresse(Integer kundeIIdRechnungsadresse) {
		this.kundeIIdRechnungsadresse = kundeIIdRechnungsadresse;
	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Integer getPersonalIIdVertreter() {
		return this.personalIIdVertreter;
	}

	public void setPersonalIIdVertreter(Integer personalIIdVertreter) {
		this.personalIIdVertreter = personalIIdVertreter;
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

	public Integer getRechnungIId() {
		return this.rechnungIId;
	}

	public void setRechnungIId(Integer rechnungIId) {
		this.rechnungIId = rechnungIId;
	}

	public Integer getLagerIId() {
		return this.lagerIId;
	}

	public void setLagerIId(Integer lagerIId) {
		this.lagerIId = lagerIId;
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

	public Timestamp getTLieferaviso() {
		return tLieferaviso;
	}

	public void setTLieferaviso(Timestamp tLieferaviso) {
		this.tLieferaviso = tLieferaviso;
	}

	public Integer getPersonalIIdLieferaviso() {
		return personalIIdLieferaviso;
	}

	public void setPersonalIIdLieferaviso(Integer personalIIdLieferaviso) {
		this.personalIIdLieferaviso = personalIIdLieferaviso;
	}

}

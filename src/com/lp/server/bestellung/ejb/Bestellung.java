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
package com.lp.server.bestellung.ejb;

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
import com.lp.server.util.IVersionable;

@NamedQueries({
		@NamedQuery(name = "BestellungfindByAnfrage", query = "SELECT OBJECT (o) FROM Bestellung o WHERE o.anfrageIId=?1"),
		@NamedQuery(name = "BestellungfindByRahmenbestellung", query = "SELECT OBJECT (o) FROM Bestellung o WHERE o.bestellungIIdRahmenbestellung=?1 ORDER BY o.cNr"),
		@NamedQuery(name = "BestellungfindAll", query = "SELECT OBJECT(o) FROM Bestellung o ORDER BY o.iId"),
		@NamedQuery(name = "BestellungfindByMandantCNr", query = "SELECT OBJECT (o) FROM Bestellung o WHERE o.mandantCNr=?1 AND o.bestellungstatusCNr=?2"),
		@NamedQuery(name = "BestellungfindByLieferadressepartnerIIdMandantCNr", query = "SELECT OBJECT (o) FROM Bestellung o WHERE o.mandantCNr=?2 AND o.partnerIIdLieferadresse=?1"),
		@NamedQuery(name = "BestellungfindByAbholadressepartnerIIdMandantCNr", query = "SELECT OBJECT (o) FROM Bestellung o WHERE o.mandantCNr=?2 AND o.partnerIIdAbholadresse=?1"),
		@NamedQuery(name = "BestellungfindByLieferantIIdBestelladresseMandantCNr", query = "SELECT OBJECT (O) FROM Bestellung o WHERE o.mandantCNr=?2 AND o.lieferantIIdBestelladresse=?1"),
		@NamedQuery(name = "BestellungfindByLieferantIIdRechnungsadresseMandantCNr", query = "SELECT OBJECT (O) FROM Bestellung o WHERE o.mandantCNr=?2 AND o.lieferantIIdRechnungsadresse=?1"),
		@NamedQuery(name = "BestellungfindByAnsprechpartnerIIdMandantCNr", query = "SELECT OBJECT (O) FROM Bestellung o WHERE o.mandantCNr=?2 AND o.ansprechpartnerIId=?1"),
		@NamedQuery(name = "BestellungfindByAnsprechpartnerIIdLieferadresseMandantCNr", query = "SELECT OBJECT (O) FROM Bestellung o WHERE o.mandantCNr=?2 AND o.ansprechpartnerIIdLieferadresse=?1"),
		@NamedQuery(name = "BestellungfindByAnsprechpartnerIIdAbholadresseMandantCNr", query = "SELECT OBJECT (O) FROM Bestellung o WHERE o.mandantCNr=?2 AND o.ansprechpartnerIIdAbholadresse=?1"),
		@NamedQuery(name = Bestellung.BestellungFindByAuftragIId, query = "SELECT OBJECT (O) FROM Bestellung o WHERE o.auftragIId=?1"),
		@NamedQuery(name = "BestellungfindByCNrMandantCNr", query = "SELECT OBJECT (O) FROM Bestellung o WHERE o.cNr=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = BestellungQuery.ByLieferantIdBestelladresseFilter,
			query = "SELECT OBJECT (O) FROM Bestellung o WHERE o.mandantCNr= :mandant AND o.lieferantIIdBestelladresse= :lieferantId AND o.bestellungstatusCNr IN (:filter)")})
@Entity
@Table(name = ITablenames.BES_BESTELLUNG)
public class Bestellung implements Serializable, IVersionable {

	public static final String BestellungFindByAuftragIId = "BestellungFindByAuftragIId";
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	
	
	@Column(name = "T_BELEGDATUM")
	private Date tBelegdatum;
	
	@Column(name = "T_VOLLSTAENDIG_GELIEFERT")
	private Timestamp tVollstaendigGeliefert;

	public Timestamp getTVollstaendigGeliefert() {
		return tVollstaendigGeliefert;
	}

	public void setTTVollstaendigGeliefert(Timestamp tVollstaendigGeliefert) {
		this.tVollstaendigGeliefert = tVollstaendigGeliefert;
	}

	@Column(name = "C_BEZPROJEKTBEZEICHNUNG")
	private String cBezprojektbezeichnung;

	@Column(name = "C_LIEFERANTENANGEBOT")
	private String cLieferantenangebot;

	public String getCLieferantenangebot() {
		return cLieferantenangebot;
	}

	public void setCLieferantenangebot(String cLieferantenangebot) {
		this.cLieferantenangebot = cLieferantenangebot;
	}

	@Column(name = "F_WECHSELKURSMANDANTWAEHRUNGBESTELLUNGSWAEHRUNG")
	private Double fWechselkursmandantwaehrungbestellungswaehrung;

	@Column(name = "T_LIEFERTERMIN")
	private Timestamp tLiefertermin;

	@Column(name = "B_TEILLIEFERUNGMOEGLICH")
	private Short bTeillieferungmoeglich;

	@Column(name = "C_LIEFERARTORT")
	private String cLieferartort;

	public String getCLieferartort() {
		return cLieferartort;
	}

	public void setCLieferartort(String cLieferartort) {
		this.cLieferartort = cLieferartort;
	}

	public Short getBPoenale() {
		return bPoenale;
	}

	public void setBPoenale(Short bPoenale) {
		this.bPoenale = bPoenale;
	}

	@Column(name = "B_POENALE")
	private Short bPoenale;

	@Column(name = "I_LEIHTAGE")
	private Integer iLeihtage;

	@Column(name = "F_ALLGEMEINERRABATTSATZ")
	private Double fAllgemeinerrabattsatz;

	@Column(name = "N_BESTELLWERT")
	private BigDecimal nBestellwert;

	@Column(name = "N_TRANSPORTKOSTEN")
	private BigDecimal nTransportkosten;

	
	public BigDecimal getNTransportkosten() {
		return nTransportkosten;
	}

	public void setNTransportkosten(BigDecimal nTransportkosten) {
		this.nTransportkosten = nTransportkosten;
	}

	@Column(name = "N_KORREKTURBETRAG")
	private BigDecimal nKorrekturbetrag;

	public BigDecimal getNKorrekturbetrag() {
		return nKorrekturbetrag;
	}

	public void setNKorrekturbetrag(BigDecimal nKorrekturbetrag) {
		this.nKorrekturbetrag = nKorrekturbetrag;
	}

	@Column(name = "C_KOPFTEXTUEBERSTEUERT")
	private String cKopftextuebersteuert;

	@Column(name = "C_FUSSTEXTUEBERSTEUERT")
	private String cFusstextuebersteuert;

	@Column(name = "T_GEDRUCKT")
	private Timestamp tGedruckt;

	@Column(name = "T_STORNIERT")
	private Timestamp tStorniert;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "T_MANUELLGELIEFERT")
	private Timestamp tManuellgeliefert;

	@Column(name = "T_MAHNSPERREBIS")
	private Date tMahnsperrebis;

	@Column(name = "X_EXTERNERKOMMENTAR")
	private String xExternerkommentar;

	@Column(name = "X_INTERNERKOMMENTAR")
	private String xInternerkommentar;

	@Column(name = "ANFRAGE_I_ID")
	private Integer anfrageIId;

	@Column(name = "AUFTRAG_I_ID")
	private Integer auftragIId;

	@Column(name = "BESTELLUNG_I_ID_RAHMENBESTELLUNG")
	private Integer bestellungIIdRahmenbestellung;

	@Column(name = "BESTELLUNGART_C_NR")
	private String bestellungartCNr;

	@Column(name = "BESTELLUNGSTATUS_C_NR")
	private String bestellungstatusCNr;

	@Column(name = "BESTELLUNGTEXT_I_ID_KOPFTEXT")
	private Integer bestellungtextIIdKopftext;

	@Column(name = "BESTELLUNGTEXT_I_ID_FUSSTEXT")
	private Integer bestellungtextIIdFusstext;

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

	@Column(name = "WAEHRUNG_C_NR_BESTELLUNGSWAEHRUNG")
	private String waehrungCNrBestellungswaehrung;

	@Column(name = "ZAHLUNGSZIEL_I_ID")
	private Integer zahlungszielIId;

	@Column(name = "ANSPRECHPARTNER_I_ID")
	private Integer ansprechpartnerIId;

	@Column(name = "LIEFERANT_I_ID_BESTELLADRESSE")
	private Integer lieferantIIdBestelladresse;

	@Column(name = "LIEFERANT_I_ID_RECHNUNGSADRESSE")
	private Integer lieferantIIdRechnungsadresse;

	@Column(name = "PARTNER_I_ID_LIEFERADRESSE")
	private Integer partnerIIdLieferadresse;

	@Column(name = "PERSONAL_I_ID_MANUELLGELIEFERT")
	private Integer personalIIdManuellgeliefert;

	@Column(name = "PERSONAL_I_ID_STORNIERT")
	private Integer personalIIdStorniert;

	@Column(name = "PERSONAL_I_ID_ANFORDERER")
	private Integer personalIIdAnforderer;

	@Column(name = "PERSONAL_I_ID_INTERNERANFORDERER")
	private Integer personalIIdInterneranforderer;

	
	public Integer getPersonalIIdInterneranforderer() {
		return personalIIdInterneranforderer;
	}

	public void setPersonalIIdInterneranforderer(Integer personalIIdInterneranforderer) {
		this.personalIIdInterneranforderer = personalIIdInterneranforderer;
	}

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "MAHNSTUFE_I_ID")
	private Integer mahnstufeIId;

	@Column(name = "T_AENDERUNGSBESTELLUNG")
	private Timestamp tAenderungsbestellung;

	@Column(name = "T_VERSANDZEITPUNKT")
	private Timestamp tVersandzeitpunkt;

	@Column(name = "ANSPRECHPARTNER_I_ID_LIEFERADRESSE")
	private Integer ansprechpartnerIIdLieferadresse;

	@Column(name = "C_VERSANDTYPE")
	private String cVersandtype;

	public Integer getPartnerIIdAbholadresse() {
		return partnerIIdAbholadresse;
	}

	public void setPartnerIIdAbholadresse(Integer partnerIIdAbholadresse) {
		this.partnerIIdAbholadresse = partnerIIdAbholadresse;
	}

	public Integer getAnsprechpartnerIIdAbholadresse() {
		return ansprechpartnerIIdAbholadresse;
	}

	public void setAnsprechpartnerIIdAbholadresse(
			Integer ansprechpartnerIIdAbholadresse) {
		this.ansprechpartnerIIdAbholadresse = ansprechpartnerIIdAbholadresse;
	}

	@Column(name = "PROJEKT_I_ID")
	private Integer projektIId;

	public Integer getProjektIId() {
		return projektIId;
	}

	public void setProjektIId(Integer projektIId) {
		this.projektIId = projektIId;
	}

	@Column(name = "PARTNER_I_ID_ABHOLADRESSE")
	private Integer partnerIIdAbholadresse;

	@Column(name = "ANSPRECHPARTNER_I_ID_ABHOLADRESSE")
	private Integer ansprechpartnerIIdAbholadresse;

	@Column(name = "T_KOMISSIONIERUNG_GEPLANT")
	private Timestamp tKommissionierungGeplant;
	@Column(name = "T_KOMISSIONIERUNG_DURCHGEFUEHT")
	private Timestamp tKommissionierungDurchgefuehrt;
	@Column(name = "T_UEBERGABE_TECHNIK")
	private Timestamp tUebergabeTechnik;
	@Column(name = "I_AENDERUNGSBESTELLUNG_VERSION")
	private Integer iAenderungsbestellungVersion;

	public Timestamp getTKommissionierungGeplant() {
		return tKommissionierungGeplant;
	}

	public void setTKommissionierungGeplant(Timestamp tKommissionierungGeplant) {
		this.tKommissionierungGeplant = tKommissionierungGeplant;
	}

	public Timestamp getTKommissionierungDurchgefuehrt() {
		return tKommissionierungDurchgefuehrt;
	}

	public void setTKommissionierungDurchgefuehrt(
			Timestamp tKommissionierungDurchgefuehrt) {
		this.tKommissionierungDurchgefuehrt = tKommissionierungDurchgefuehrt;
	}

	public Timestamp getTUebergabeTechnik() {
		return tUebergabeTechnik;
	}

	public void setTUebergabeTechnik(Timestamp tUebergabeTechnik) {
		this.tUebergabeTechnik = tUebergabeTechnik;
	}

	private static final long serialVersionUID = 1L;

	public Bestellung() {
		super();
	}

	public Bestellung(Integer id, String bestellungsnummer, String mandantCNr,
			String belegartCNr, Date belegdatum, Timestamp liefertermin,
			Short teillieferungMoeglich, Integer personalIIdAnlegen,
			Integer personalIIdAendern, Integer lieferartIId,
			Integer zahlungszielIId, Integer spediteurIId, Short bPoenale) {
		setIId(id);
		Timestamp t = new Timestamp(System.currentTimeMillis());
		setTAendern(t);
		setTAnlegen(t);
		// beim anlegen sind die gleich.
		setPersonalIIdAendern(personalIIdAnlegen);
		setPersonalIIdAnlegen(personalIIdAendern);
		setCNr(bestellungsnummer);
		setMandantCNr(mandantCNr);
		setBelegartCNr(belegartCNr);
		setTBelegdatum(belegdatum);
		setTLiefertermin(liefertermin);
		setBTeillieferungmoeglich(teillieferungMoeglich);
		setLieferartIId(lieferartIId);
		setZahlungszielIId(zahlungszielIId);
		setSpediteurIId(spediteurIId);
		setBPoenale(bPoenale);
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

	public Date getTBelegdatum() {
		return this.tBelegdatum;
	}

	public void setTBelegdatum(Date tBelegdatum) {
		this.tBelegdatum = tBelegdatum;
	}

	public String getCBezprojektbezeichnung() {
		return this.cBezprojektbezeichnung;
	}

	public void setCBezprojektbezeichnung(String cBezprojektbezeichnung) {
		this.cBezprojektbezeichnung = cBezprojektbezeichnung;
	}

	public Double getFWechselkursmandantwaehrungbestellungswaehrung() {
		return this.fWechselkursmandantwaehrungbestellungswaehrung;
	}

	public void setFWechselkursmandantwaehrungbestellungswaehrung(
			Double fWechselkursmandantwaehrungbestellungswaehrung) {
		this.fWechselkursmandantwaehrungbestellungswaehrung = fWechselkursmandantwaehrungbestellungswaehrung;
	}

	public Timestamp getTLiefertermin() {
		return this.tLiefertermin;
	}

	public void setTLiefertermin(Timestamp tLiefertermin) {
		this.tLiefertermin = tLiefertermin;
	}

	public Short getBTeillieferungmoeglich() {
		return this.bTeillieferungmoeglich;
	}

	public void setBTeillieferungmoeglich(Short bTeillieferungmoeglich) {
		this.bTeillieferungmoeglich = bTeillieferungmoeglich;
	}

	public Integer getILeihtage() {
		return this.iLeihtage;
	}

	public void setILeihtage(Integer iLeihtage) {
		this.iLeihtage = iLeihtage;
	}

	public Double getFAllgemeinerrabattsatz() {
		return this.fAllgemeinerrabattsatz;
	}

	public void setFAllgemeinerrabattsatz(Double fAllgemeinerrabattsatz) {
		this.fAllgemeinerrabattsatz = fAllgemeinerrabattsatz;
	}

	public BigDecimal getNBestellwert() {
		return this.nBestellwert;
	}

	public void setNBestellwert(BigDecimal nBestellwert) {
		this.nBestellwert = nBestellwert;
	}

	public String getCKopftextuebersteuert() {
		return this.cKopftextuebersteuert;
	}

	public void setCKopftextuebersteuert(String cKopftextuebersteuert) {
		this.cKopftextuebersteuert = cKopftextuebersteuert;
	}

	public String getCFusstextuebersteuert() {
		return this.cFusstextuebersteuert;
	}

	public void setCFusstextuebersteuert(String cFusstextuebersteuert) {
		this.cFusstextuebersteuert = cFusstextuebersteuert;
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

	public Timestamp getTManuellgeliefert() {
		return this.tManuellgeliefert;
	}

	public void setTManuellgeliefert(Timestamp tManuellgeliefert) {
		this.tManuellgeliefert = tManuellgeliefert;
	}

	public Date getTMahnsperrebis() {
		return this.tMahnsperrebis;
	}

	public void setTMahnsperrebis(Date tMahnsperrebis) {
		this.tMahnsperrebis = tMahnsperrebis;
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

	public Integer getAnfrageIId() {
		return this.anfrageIId;
	}

	public void setAnfrageIId(Integer anfrageIId) {
		this.anfrageIId = anfrageIId;
	}

	public Integer getAuftragIId() {
		return this.auftragIId;
	}

	public void setAuftragIId(Integer auftragIId) {
		this.auftragIId = auftragIId;
	}

	public Integer getBestellungIIdRahmenbestellung() {
		return this.bestellungIIdRahmenbestellung;
	}

	public void setBestellungIIdRahmenbestellung(
			Integer bestellungIIdRahmenbestellung) {
		this.bestellungIIdRahmenbestellung = bestellungIIdRahmenbestellung;
	}

	public String getBestellungartCNr() {
		return this.bestellungartCNr;
	}

	public void setBestellungartCNr(String bestellungartCNr) {
		this.bestellungartCNr = bestellungartCNr;
	}

	public String getBestellungstatusCNr() {
		return this.bestellungstatusCNr;
	}

	public void setBestellungstatusCNr(String bestellungstatusCNr) {
		this.bestellungstatusCNr = bestellungstatusCNr;
	}

	public Integer getBestellungtextIIdKopftext() {
		return this.bestellungtextIIdKopftext;
	}

	public void setBestellungtextIIdKopftext(Integer bestellungtextIIdKopftext) {
		this.bestellungtextIIdKopftext = bestellungtextIIdKopftext;
	}

	public Integer getBestellungtextIIdFusstext() {
		return this.bestellungtextIIdFusstext;
	}

	public void setBestellungtextIIdFusstext(Integer bestellungtextIIdFusstext) {
		this.bestellungtextIIdFusstext = bestellungtextIIdFusstext;
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

	public String getWaehrungCNrBestellungswaehrung() {
		return this.waehrungCNrBestellungswaehrung;
	}

	public void setWaehrungCNrBestellungswaehrung(
			String waehrungCNrBestellungswaehrung) {
		this.waehrungCNrBestellungswaehrung = waehrungCNrBestellungswaehrung;
	}

	public Integer getZahlungszielIId() {
		return this.zahlungszielIId;
	}

	public void setZahlungszielIId(Integer zahlungszielIId) {
		this.zahlungszielIId = zahlungszielIId;
	}

	public Integer getAnsprechpartnerIId() {
		return this.ansprechpartnerIId;
	}

	public void setAnsprechpartnerIId(Integer ansprechpartnerIId) {
		this.ansprechpartnerIId = ansprechpartnerIId;
	}

	public Integer getLieferantIIdBestelladresse() {
		return this.lieferantIIdBestelladresse;
	}

	public void setLieferantIIdBestelladresse(Integer lieferantIIdBestelladresse) {
		this.lieferantIIdBestelladresse = lieferantIIdBestelladresse;
	}

	public Integer getLieferantIIdRechnungsadresse() {
		return this.lieferantIIdRechnungsadresse;
	}

	public void setLieferantIIdRechnungsadresse(
			Integer lieferantIIdRechnungsadresse) {
		this.lieferantIIdRechnungsadresse = lieferantIIdRechnungsadresse;
	}

	public Integer getPartnerIIdLieferadresse() {
		return this.partnerIIdLieferadresse;
	}

	public void setPartnerIIdLieferadresse(Integer partnerIIdLieferadresse) {
		this.partnerIIdLieferadresse = partnerIIdLieferadresse;
	}

	public Integer getPersonalIIdManuellgeliefert() {
		return this.personalIIdManuellgeliefert;
	}

	public void setPersonalIIdManuellgeliefert(
			Integer personalIIdManuellgeliefert) {
		this.personalIIdManuellgeliefert = personalIIdManuellgeliefert;
	}

	public Integer getPersonalIIdStorniert() {
		return this.personalIIdStorniert;
	}

	public void setPersonalIIdStorniert(Integer personalIIdStorniert) {
		this.personalIIdStorniert = personalIIdStorniert;
	}

	public Integer getPersonalIIdAnforderer() {
		return this.personalIIdAnforderer;
	}

	public void setPersonalIIdAnforderer(Integer personalIIdAnforderer) {
		this.personalIIdAnforderer = personalIIdAnforderer;
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

	public Integer getMahnstufeIId() {
		return this.mahnstufeIId;
	}

	public void setMahnstufeIId(Integer mahnstufeIId) {
		this.mahnstufeIId = mahnstufeIId;
	}

	public Timestamp getTAenderungsbestellung() {
		return this.tAenderungsbestellung;
	}

	public void setTAenderungsbestellung(Timestamp aenderungsbestellung) {
		tAenderungsbestellung = aenderungsbestellung;
	}

	public Timestamp getTVersandzeitpunkt() {
		return this.tVersandzeitpunkt;
	}

	public void setTVersandzeitpunkt(Timestamp versandzeitpunkt) {
		tVersandzeitpunkt = versandzeitpunkt;
	}

	public Integer getAnsprechpartnerIIdLieferadresse() {
		return this.ansprechpartnerIIdLieferadresse;
	}

	public void setAnsprechpartnerIIdLieferadresse(
			Integer ansprechpartnerIIdLieferadresse) {
		this.ansprechpartnerIIdLieferadresse = ansprechpartnerIIdLieferadresse;
	}

	public void setCVersandtype(String cVersandtype) {
		this.cVersandtype = cVersandtype;
	}

	public String getCVersandtype() {
		return cVersandtype;
	}

	public Integer getIVersion() {
		return iAenderungsbestellungVersion;
	}
	
	public void setIVersion(Integer iVersion) {
		this.iAenderungsbestellungVersion = iVersion;
	}

	@Override
	public boolean hasVersion() {
		return getIVersion() != null;
	}

	@Override
	public Timestamp getTVersion() {
		return getTAenderungsbestellung();
	}

	@Override
	public void setTVersion(Timestamp tVersion) {
		setTAenderungsbestellung(tVersion);
	}
}

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
package com.lp.server.eingangsrechnung.ejb;

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
import com.lp.util.Helper;

@NamedQueries({
		@NamedQuery(name = "EingangsrechnungfindByBestellungIId", query = "SELECT OBJECT(o) FROM Eingangsrechnung o WHERE o.bestellungIId=?1"),
		@NamedQuery(name = EingangsrechnungQuery.ByMandantBelegdatumBisStatusCNr, query = "SELECT OBJECT(o) FROM Eingangsrechnung o WHERE o.mandantCNr=:mandant AND o.tBelegdatum<:belegdatum AND o.statusCNr IN (:stati)"),
		@NamedQuery(name = "EingangsrechnungfindByMandantBelegdatumVonBis", query = "SELECT OBJECT(o) FROM Eingangsrechnung o WHERE o.mandantCNr=?1 AND o.tBelegdatum>=?2 AND o.tBelegdatum<?3"),
		@NamedQuery(name = "EingangsrechnungfindByMandantFreigabedatumVonBis", query = "SELECT OBJECT(o) FROM Eingangsrechnung o WHERE o.mandantCNr=?1 AND o.tFreigabedatum>=?2 AND o.tFreigabedatum<?3"),
		@NamedQuery(name = "EingangsrechnungfindByMandantLieferantBelegdatumVonBis", query = "SELECT OBJECT(o) FROM Eingangsrechnung o WHERE o.mandantCNr=?1 AND o.tBelegdatum>=?2 AND o.tBelegdatum<?3 AND o.lieferantIId = ?4"),
		@NamedQuery(name = "EingangsrechnungfindByMandantLieferantFreigabedatumVonBis", query = "SELECT OBJECT(o) FROM Eingangsrechnung o WHERE o.mandantCNr=?1 AND o.tFreigabedatum>=?2 AND o.tFreigabedatum<?3 AND o.lieferantIId = ?4"),
		@NamedQuery(name = "EingangsrechnungfindByMandantLieferantIId", query = "SELECT OBJECT(o) FROM Eingangsrechnung o WHERE o.mandantCNr=?1 AND o.lieferantIId=?2"),
		@NamedQuery(name = EingangsrechnungQuery.ByMandantLieferantIIdStatusCNr, query = "SELECT OBJECT(o) FROM Eingangsrechnung o WHERE o.mandantCNr=:mandant AND o.lieferantIId=:lieferant AND o.statusCNr IN (:stati)"),
		@NamedQuery(name = "EingangsrechnungfindByMandantCNr", query = "SELECT OBJECT(o) FROM Eingangsrechnung o WHERE o.mandantCNr=?1"),
		@NamedQuery(name = "EingangsrechnungfindByLieferantIIdCLieferantenrechnungsnummer", query = "SELECT OBJECT(o) FROM Eingangsrechnung o WHERE o.lieferantIId=?1 AND o.cLieferantenrechnungsnummer=?2"),
		@NamedQuery(name = EingangsrechnungQuery.ByCNrMandantCNr, query = "SELECT OBJECT(o) FROM Eingangsrechnung o WHERE o.cNr=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "EingangsrechnungfindByLieferantIIdStatusCNrEingangsrechnungartCNr", query = "SELECT OBJECT(o) FROM Eingangsrechnung o WHERE o.lieferantIId=?1 AND o.statusCNr=?2 AND o.eingangsrechnungartCNr=?3"),
		@NamedQuery(name = EingangsrechnungQuery.ByMandantStatusCNr, query = "SELECT OBJECT(o) FROM Eingangsrechnung o WHERE o.mandantCNr=:mandant AND o.statusCNr IN (:stati)"),
		@NamedQuery(name = EingangsrechnungQuery.ByMandantKundendatenStatusCNr, query = "SELECT OBJECT(o) FROM Eingangsrechnung o WHERE o.mandantCNr=:mandant AND o.cKundendaten=:kundendaten AND o.statusCNr IN (:stati)"),
		@NamedQuery(name = EingangsrechnungQuery.MaxCNr, query = "SELECT MAX(o.cNr) FROM Eingangsrechnung o WHERE o.mandantCNr=:mandant"),
		@NamedQuery(name = EingangsrechnungQuery.MaxCNrByGeschaeftsjahr, query = "SELECT MAX(o.cNr) FROM Eingangsrechnung o WHERE o.mandantCNr=?1 AND o.iGeschaeftsjahr=?2"),
		@NamedQuery(name = EingangsrechnungQuery.ByIIds, query = "SELECT OBJECT(o) FROM Eingangsrechnung o WHERE o.iId IN (?1)")
})
@Entity
@Table(name = ITablenames.ER_EINGANGSRECHNUNG)
public class Eingangsrechnung implements Serializable, ICNr {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "T_BELEGDATUM")
	private Date tBelegdatum;

	@Column(name = "T_FREIGABEDATUM")
	private Date tFreigabedatum;

	
	@Column(name = "C_KOPFTEXTUEBERSTEUERT")
	private String cKopftextuebersteuert;

	@Column(name = "C_FUSSTEXTUEBERSTEUERT")
	private String cFusstextuebersteuert;
	
	@Column(name = "C_TEXT")
	private String cText;

	@Column(name = "C_WEARTIKEL")
	private String cWeartikel;

	public String getCWeartikel() {
		return cWeartikel;
	}

	public void setCWeartikel(String cWeartikel) {
		this.cWeartikel = cWeartikel;
	}

	@Column(name = "T_GEPRUEFT")
	private java.sql.Timestamp tGeprueft;

	public java.sql.Timestamp getTGeprueft() {
		return tGeprueft;
	}

	public void setTGeprueft(java.sql.Timestamp tGeprueft) {
		this.tGeprueft = tGeprueft;
	}

	public Integer getPersonalIIdGeprueft() {
		return personalIIdGeprueft;
	}

	public void setPersonalIIdGeprueft(Integer personalIIdGeprueft) {
		this.personalIIdGeprueft = personalIIdGeprueft;
	}

	@Column(name = "PERSONAL_I_ID_GEPRUEFT")
	private Integer personalIIdGeprueft;

	
	
	@Column(name = "N_BETRAG")
	private BigDecimal nBetrag;

	@Column(name = "N_BETRAGFW")
	private BigDecimal nBetragfw;

	@Column(name = "N_USTBETRAG")
	private BigDecimal nUstbetrag;

	@Column(name = "N_USTBETRAGFW")
	private BigDecimal nUstbetragfw;

	@Column(name = "N_KURS")
	private BigDecimal nKurs;

	@Column(name = "T_BEZAHLTDATUM")
	private Date tBezahltdatum;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "T_MANUELLERLEDIGT")
	private Timestamp tManuellerledigt;

	@Column(name = "C_LIEFERANTENRECHNUNGSNUMMER")
	private String cLieferantenrechnungsnummer;

	@Column(name = "T_FIBUUEBERNAHME")
	private Timestamp tFibuuebernahme;

	@Column(name = "C_KUNDENDATEN")
	private String cKundendaten;

	@Column(name = "BESTELLUNG_I_ID")
	private Integer bestellungIId;
	
	@Column(name = "PERSONAL_I_ID_ABW_BANKVERBINDUNG")
	private Integer personalIIdAbwBankverbindung;

	public Integer getPersonalIIdAbwBankverbindung() {
		return personalIIdAbwBankverbindung;
	}

	public void setPersonalIIdAbwBankverbindung(Integer personalIIdAbwBankverbindung) {
		this.personalIIdAbwBankverbindung = personalIIdAbwBankverbindung;
	}

	@Column(name = "EINGANGSRECHNUNGART_C_NR")
	private String eingangsrechnungartCNr;

	@Column(name = "STATUS_C_NR")
	private String statusCNr;

	@Column(name = "KONTO_I_ID")
	private Integer kontoIId;

	@Column(name = "EINGANGSRECHNUNG_I_ID_NACHFOLGER")
	private Integer eingangsrechnungIIdNachfolger;

	@Column(name = "T_GEDRUCKT")
	private Timestamp tGedruckt;

	@Column(name = "I_GESCHAEFTSJAHR")
	private Integer iGeschaeftsjahr;

	@Column(name = "KOSTENSTELLE_I_ID")
	private Integer kostenstelleIId;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "MWSTSATZ_I_ID")
	private Integer mwstsatzIId;

	@Column(name = "WAEHRUNG_C_NR")
	private String waehrungCNr;

	@Column(name = "ZAHLUNGSZIEL_I_ID")
	private Integer zahlungszielIId;

	@Column(name = "LIEFERANT_I_ID")
	private Integer lieferantIId;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_MANUELLERLEDIGT")
	private Integer personalIIdManuellerledigt;

	@Column(name = "AUFTRAGWIEDERHOLUNGSINTERVALL_C_NR")
	private String auftragwiederholungsintervallCNr;

	@Column(name = "B_REVERSECHARGE")
	private Short bReversecharge;

	@Column(name = "B_MITPOSITIONEN")
	private Short bMitpositionen;

	public Short getBMitpositionen() {
		return bMitpositionen;
	}

	public void setBMitpositionen(Short bMitpositionen) {
		this.bMitpositionen = bMitpositionen;
	}

	@Column(name = "B_IGERWERB")
	private Short bIgErwerb;

	@Column(name = "T_MAHNDATUM")
	private Timestamp tMahndatum;

	@Column(name = "MAHNSTUFE_I_ID")
	private Integer mahnstufeIId;

	@Column(name = "T_WIEDERHOLENDERLEDIGT")
	private Timestamp tWiederholenderledigt;
	@Column(name = "PERSONAL_I_ID_WIEDERHOLENDERLEDIGT")
	private Integer personalIIdWiederholenderledigt;

	
	@Column(name = "EINGANGSRECHNUNG_I_ID_ZOLLIMPORT")
	private Integer eingangsrechnungIdZollimport;
	
	public Integer getEingangsrechnungIdZollimport() {
		return eingangsrechnungIdZollimport;
	}

	public void setEingangsrechnungIdZollimport(Integer eingangsrechnungIdZollimport) {
		this.eingangsrechnungIdZollimport = eingangsrechnungIdZollimport;
	}

	@Column(name = "T_ZOLLIMPORTPAPIER")
	private Timestamp tZollimportpapier;
	@Column(name = "PERSONAL_I_ID_ZOLLIMPORTPAPIER")
	private Integer personalIIdZollimportpapier;

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
	
	@Column(name = "C_ZOLLIMPORTPAPIER")
	private String cZollimportpapier;

	@Column(name = "REVERSECHARGEART_I_ID")
	private Integer reversechargeartId ;

	public String getCZollimportpapier() {
		return cZollimportpapier;
	}

	public void setCZollimportpapier(String cZollimportpapier) {
		this.cZollimportpapier = cZollimportpapier;
	}

	public Timestamp getTZollimportpapier() {
		return tZollimportpapier;
	}

	public void setTZollimportpapier(Timestamp tZollimportpapier) {
		this.tZollimportpapier = tZollimportpapier;
	}

	public Integer getPersonalIIdZollimportpapier() {
		return personalIIdZollimportpapier;
	}

	public void setPersonalIIdZollimportpapier(
			Integer personalIIdZollimportpapier) {
		this.personalIIdZollimportpapier = personalIIdZollimportpapier;
	}

	public Timestamp getTWiederholenderledigt() {
		return tWiederholenderledigt;
	}

	public void setTWiederholenderledigt(Timestamp tWiederholenderledigt) {
		this.tWiederholenderledigt = tWiederholenderledigt;
	}

	public Integer getPersonalIIdWiederholenderledigt() {
		return personalIIdWiederholenderledigt;
	}

	public void setPersonalIIdWiederholenderledigt(
			Integer personalIIdWiederholenderledigt) {
		this.personalIIdWiederholenderledigt = personalIIdWiederholenderledigt;
	}

	private static final long serialVersionUID = 1L;

	public Eingangsrechnung() {
		super();
	}

	public Eingangsrechnung(Integer id, String nr, Integer geschaeftsjahr,
			String mandantCNr, String eingangsrechnungartCNr, Date belegdatum,
			Date freigabedatum, Integer lieferantIId, BigDecimal betrag,
			BigDecimal betragfw, BigDecimal ustBetrag, BigDecimal ustBetragfw,
			Integer mwstsatzIId, BigDecimal kurs, String waehrungCNr,
			String statusCNr, Integer personalIIdAnlegen,
			Integer personalIIdAendern, Short bIgErwerb, Short bMitpositionen, Integer reversechargeartId) {
		setIId(id);
		setCNr(nr);
		setIGeschaeftsjahr(geschaeftsjahr);
		setMandantCNr(mandantCNr);
		setEingangsrechnungartCNr(eingangsrechnungartCNr);
		setTBelegdatum(belegdatum);
		setTFreigabedatum(freigabedatum);
		setLieferantIId(lieferantIId);
		setNBetrag(betrag);
		setNBetragfw(betragfw);
		setNUstbetrag(ustBetrag);
		setNUstbetragfw(ustBetragfw);
		setMwstsatzIId(mwstsatzIId);
		setNKurs(kurs);
		setWaehrungCNr(waehrungCNr);
		setStatusCNr(statusCNr);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setPersonalIIdAendern(personalIIdAendern);
		setBReversecharge(Helper.getShortFalse());
		setBIgErwerb(bIgErwerb);
		setBMitpositionen(bMitpositionen);
		setReversechargeartIId(reversechargeartId);
		
		// Setzen der NOT NULL Felder
		java.sql.Timestamp timestamp = new java.sql.Timestamp(
				System.currentTimeMillis());
		this.setTAendern(timestamp);
		this.setTAnlegen(timestamp);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Timestamp getTMahndatum() {
		return this.tMahndatum;
	}

	public void setTMahndatum(Timestamp tMahndatum) {
		this.tMahndatum = tMahndatum;
	}

	public Integer getMahnstufeIId() {
		return this.mahnstufeIId;
	}

	public void setMahnstufeIId(Integer mahnstufeIId) {
		this.mahnstufeIId = mahnstufeIId;
	}

	public String getAuftragwiederholungsintervallCNr() {
		return this.auftragwiederholungsintervallCNr;
	}

	public void setAuftragwiederholungsintervallCNr(
			String auftragwiederholungsintervallCNr) {
		this.auftragwiederholungsintervallCNr = auftragwiederholungsintervallCNr;
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

	public Date getTFreigabedatum() {
		return this.tFreigabedatum;
	}

	public void setTFreigabedatum(Date tFreigabedatum) {
		this.tFreigabedatum = tFreigabedatum;
	}

	public String getCText() {
		return this.cText;
	}

	public void setCText(String cText) {
		this.cText = cText;
	}

	public BigDecimal getNBetrag() {
		return this.nBetrag;
	}

	public void setNBetrag(BigDecimal nBetrag) {
		this.nBetrag = nBetrag;
	}

	public BigDecimal getNBetragfw() {
		return this.nBetragfw;
	}

	public void setNBetragfw(BigDecimal nBetragfw) {
		this.nBetragfw = nBetragfw;
	}

	public BigDecimal getNUstbetrag() {
		return this.nUstbetrag;
	}

	public void setNUstbetrag(BigDecimal nUstbetrag) {
		this.nUstbetrag = nUstbetrag;
	}

	public BigDecimal getNUstbetragfw() {
		return this.nUstbetragfw;
	}

	public void setNUstbetragfw(BigDecimal nUstbetragfw) {
		this.nUstbetragfw = nUstbetragfw;
	}

	public BigDecimal getNKurs() {
		return this.nKurs;
	}

	public void setNKurs(BigDecimal nKurs) {
		this.nKurs = nKurs;
	}

	public Date getTBezahltdatum() {
		return this.tBezahltdatum;
	}

	public void setTBezahltdatum(Date tBezahltdatum) {
		this.tBezahltdatum = tBezahltdatum;
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

	public String getCLieferantenrechnungsnummer() {
		return this.cLieferantenrechnungsnummer;
	}

	public void setCLieferantenrechnungsnummer(
			String cLieferantenrechnungsnummer) {
		this.cLieferantenrechnungsnummer = cLieferantenrechnungsnummer;
	}

	public Timestamp getTFibuuebernahme() {
		return this.tFibuuebernahme;
	}

	public void setTFibuuebernahme(Timestamp tFibuuebernahme) {
		this.tFibuuebernahme = tFibuuebernahme;
	}

	public String getCKundendaten() {
		return this.cKundendaten;
	}

	public void setCKundendaten(String cKundendaten) {
		this.cKundendaten = cKundendaten;
	}

	public Integer getBestellungIId() {
		return this.bestellungIId;
	}

	public void setBestellungIId(Integer bestellungIId) {
		this.bestellungIId = bestellungIId;
	}

	public String getEingangsrechnungartCNr() {
		return this.eingangsrechnungartCNr;
	}

	public void setEingangsrechnungartCNr(String eingangsrechnungartCNr) {
		this.eingangsrechnungartCNr = eingangsrechnungartCNr;
	}

	public String getStatusCNr() {
		return this.statusCNr;
	}

	public void setStatusCNr(String statusCNr) {
		this.statusCNr = statusCNr;
	}

	public Integer getKontoIId() {
		return this.kontoIId;
	}

	public void setKontoIId(Integer kontoIId) {
		this.kontoIId = kontoIId;
	}

	public Integer getIGeschaeftsjahr() {
		return this.iGeschaeftsjahr;
	}

	public void setIGeschaeftsjahr(Integer iGeschaeftsjahr) {
		this.iGeschaeftsjahr = iGeschaeftsjahr;
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

	public Integer getMwstsatzIId() {
		return this.mwstsatzIId;
	}

	public void setMwstsatzIId(Integer mwstsatzIId) {
		this.mwstsatzIId = mwstsatzIId;
	}

	public String getWaehrungCNr() {
		return this.waehrungCNr;
	}

	public void setWaehrungCNr(String waehrungCNr) {
		this.waehrungCNr = waehrungCNr;
	}

	public Integer getZahlungszielIId() {
		return this.zahlungszielIId;
	}

	public void setZahlungszielIId(Integer zahlungszielIId) {
		this.zahlungszielIId = zahlungszielIId;
	}

	public Integer getLieferantIId() {
		return this.lieferantIId;
	}

	public void setLieferantIId(Integer lieferantIId) {
		this.lieferantIId = lieferantIId;
	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
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

	public Integer getEingangsrechnungIIdNachfolger() {
		return eingangsrechnungIIdNachfolger;
	}

	public void setEingangsrechnungIIdNachfolger(
			Integer eingangsrechnungIIdNachfolger) {
		this.eingangsrechnungIIdNachfolger = eingangsrechnungIIdNachfolger;
	}

	public Timestamp getTGedruckt() {
		return tGedruckt;
	}

	public void setTGedruckt(Timestamp tGedruckt) {
		this.tGedruckt = tGedruckt;
	}

	public Short getBReversecharge() {
		return this.bReversecharge;
	}

	public void setBReversecharge(Short bReversecharge) {
		this.bReversecharge = bReversecharge;
	}

	public void setBIgErwerb(Short bIgErwerb) {
		this.bIgErwerb = bIgErwerb;
	}

	public Short getBIgErwerb() {
		return bIgErwerb;
	}

	public Integer getReversechargeartIId() {
		return reversechargeartId ;
	}
	
	public void setReversechargeartIId(Integer reversechargeartId) {
		this.reversechargeartId = reversechargeartId ;
	}
}

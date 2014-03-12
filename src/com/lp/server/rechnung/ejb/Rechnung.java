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
package com.lp.server.rechnung.ejb;

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

import com.lp.util.Helper;

@NamedQueries( {
		@NamedQuery(name = "RechnungfindByMandantBelegdatumVonBis", query = "SELECT OBJECT(o) FROM Rechnung o WHERE o.mandantCNr=?1 AND o.tBelegdatum>=?2 AND o.tBelegdatum<?3"),
		@NamedQuery(name = "RechnungfindByCNrMandant", query = "SELECT OBJECT(o) FROM Rechnung o WHERE o.cNr=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "RechnungfindByCNrRechnungartCNrMandant", query = "SELECT OBJECT(o) FROM Rechnung o WHERE o.cNr=?1 AND o.rechnungartCNr=?2 AND o.mandantCNr=?3"),
		@NamedQuery(name = "RechnungfindByRechnungartRechnungCNrMandant", 
				query = "SELECT OBJECT(o) FROM Rechnung o WHERE o.cNr=?1 AND "
					+ "(o.rechnungartCNr='Anzahlungsrechnung' OR o.rechnungartCNr='Rechnung' OR o.rechnungartCNr='Schlussrechnung') AND "
					+ "o.mandantCNr=?2"),
		@NamedQuery(name = "RechnungfindAll", query = "SELECT OBJECT(o) FROM Rechnung o"),
		@NamedQuery(name = "RechnungfindByAuftragIId", query = "SELECT OBJECT(o) FROM Rechnung o WHERE o.auftragIId=?1"),
		@NamedQuery(name = "RechnungfindByLieferscheinIId", query = "SELECT OBJECT(o) FROM Rechnung o WHERE o.lieferscheinIId=?1"),
		@NamedQuery(name = "RechnungfindByAuftragIIdRechnungArtCNr", query = "SELECT OBJECT(o) FROM Rechnung o WHERE o.auftragIId=?1 AND o.rechnungartCNr=?2"),
		@NamedQuery(name = "RechnungfindByAuftragIIdTBelegdatum", query = "SELECT OBJECT(o) FROM Rechnung o WHERE o.auftragIId=?1 AND o.tBelegdatum>=?2 ORDER by o.tBelegdatum"),
		@NamedQuery(name = "RechnungfindByAuftragIIdNotInStatusCNr", query = "SELECT OBJECT(o) FROM Rechnung o WHERE o.auftragIId=?1 AND o.statusCNr<>?2"),
//		@NamedQuery(name = "RechnungfindByPartnerIIdRechnungsadresseMandantCNr", query = "SELECT OBJECT(O) FROM Rechnung o WHERE o.partnerIIdRechnungsadresse=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "RechnungfindByKundeIIdMandantCNr", query = "SELECT OBJECT(O) FROM Rechnung o WHERE o.kundeIId=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "RechnungfindByRechnungIIdZuRechnung", query = "SELECT OBJECT(O) FROM Rechnung o WHERE o.rechnungIIdZurechnung=?1 AND (o.rechnungartCNr='Gutschrift' OR o.rechnungartCNr='Wertgutschrift')"),
		@NamedQuery(name = "RechnungfindByKundeIIdStatistikadresseMandantCNr", query = "SELECT OBJECT(O) FROM Rechnung o WHERE o.kundeIIdStatistikadresse=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "RechnungfindByAnsprechpartnerIId", query = "SELECT OBJECT(o) FROM Rechnung o WHERE o.ansprechpartnerIId=?1") })
@Entity
@Table(name = "RECH_RECHNUNG")
public class Rechnung implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "C_BEZ")
	private String cBez;
	
	@Column(name = "T_BELEGDATUM")
	private Timestamp tBelegdatum;

	@Column(name = "N_KURS")
	private BigDecimal nKurs;

	@Column(name = "B_MWSTALLEPOSITIONEN")
	private Short bMwstallepositionen;

	@Column(name = "N_WERT")
	private BigDecimal nWert;

	@Column(name = "N_WERTFW")
	private BigDecimal nWertfw;

	@Column(name = "N_WERTUST")
	private BigDecimal nWertust;

	@Column(name = "N_WERTUSTFW")
	private BigDecimal nWertustfw;

	@Column(name = "F_VERSTECKTERAUFSCHLAG")
	private Double fVersteckteraufschlag;

	@Column(name = "F_ALLGEMEINERRABATTSATZ")
	private Double fAllgemeinerrabattsatz;

	@Column(name = "B_MINDERMENGENZUSCHLAG")
	private Short bMindermengenzuschlag;

	@Column(name = "N_PROVISION")
	private BigDecimal nProvision;

	@Column(name = "C_PROVISIONTEXT")
	private String cProvisiontext;

	@Column(name = "T_GEDRUCKT")
	private Timestamp tGedruckt;

	@Column(name = "T_FIBUUEBERNAHME")
	private Timestamp tFibuuebernahme;

	@Column(name = "C_KOPFTEXTUEBERSTEUERT")
	private String cKopftextuebersteuert;

	@Column(name = "C_FUSSTEXTUEBERSTEUERT")
	private String cFusstextuebersteuert;

	@Column(name = "T_STORNIERT")
	private Timestamp tStorniert;

	@Column(name = "T_BEZAHLTDATUM")
	private Date tBezahltdatum;

	@Column(name = "T_MAHNSPERREBIS")
	private Date tMahnsperrebis;


	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "T_MANUELLERLEDIGT")
	private Timestamp tManuellerledigt;

	@Column(name = "C_BESTELLNUMMER")
	private String cBestellnummer;

	@Column(name = "B_REVERSECHARGE")
	private Short bReversecharge;

	@Column(name = "AUFTRAG_I_ID")
	private Integer auftragIId;

	@Column(name = "KONTO_I_ID")
	private Integer kontoIId;

	@Column(name = "I_GESCHAEFTSJAHR")
	private Integer iGeschaeftsjahr;

	@Column(name = "KOSTENSTELLE_I_ID")
	private Integer kostenstelleIId;

	@Column(name = "LIEFERART_I_ID")
	private Integer lieferartIId;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "MWSTSATZ_I_ID")
	private Integer mwstsatzIId;

	@Column(name = "SPEDITEUR_I_ID")
	private Integer spediteurIId;

	@Column(name = "WAEHRUNG_C_NR")
	private String waehrungCNr;

	@Column(name = "ZAHLUNGSZIEL_I_ID")
	private Integer zahlungszielIId;

	@Column(name = "LIEFERSCHEIN_I_ID")
	private Integer lieferscheinIId;

	@Column(name = "ANSPRECHPARTNER_I_ID")
	private Integer ansprechpartnerIId;

	@Column(name = "KUNDE_I_ID_STATISTIKADRESSE")
	private Integer kundeIIdStatistikadresse;

	@Column(name = "KUNDE_I_ID")
	private Integer kundeIId;

	@Column(name = "PERSONAL_I_ID_MANUELLERLEDIGT")
	private Integer personalIIdManuellerledigt;

	@Column(name = "PERSONAL_I_ID_STORNIERT")
	private Integer personalIIdStorniert;

	@Column(name = "PERSONAL_I_ID_VERTRETER")
	private Integer personalIIdVertreter;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "RECHNUNG_I_ID_ZURECHNUNG")
	private Integer rechnungIIdZurechnung;

	@Column(name = "RECHNUNGART_C_NR")
	private String rechnungartCNr;

	@Column(name = "STATUS_C_NR")
	private String statusCNr;

	@Column(name = "LAGER_I_ID")
	private Integer lagerIId;
	
	
	@Column(name="GUTSCHRIFTSGRUND_I_ID")
	private Integer gutschriftsgrundIId;

	@Column(name="T_VERSANDZEITPUNKT")
	private Timestamp tVersandzeitpunkt;
	
	@Column(name="C_VERSANDTYPE")
	private String cVersandtype;

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
	
	
	@Column(name = "T_ZOLLPAPIER")
	private Timestamp  tZollpapier;
	@Column(name = "PERSONAL_I_ID_ZOLLPAPIER")
	private Integer personalIIdZollpapier;
	public Timestamp getTZollpapier() {
		return tZollpapier;
	}

	public void setTZollpapier(Timestamp tZollpapier) {
		this.tZollpapier = tZollpapier;
	}

	public Integer getPersonalIIdZollpapier() {
		return personalIIdZollpapier;
	}

	public void setPersonalIIdZollpapier(Integer personalIIdZollpapier) {
		this.personalIIdZollpapier = personalIIdZollpapier;
	}

	public String getCZollpapier() {
		return cZollpapier;
	}

	public void setCZollpapier(String cZollpapier) {
		this.cZollpapier = cZollpapier;
	}
	@Column(name = "C_ZOLLPAPIER")
	private String   cZollpapier;
	
	
	public String getCLieferartort() {
		return cLieferartort;
	}

	public void setCLieferartort(String cLieferartort) {
		this.cLieferartort = cLieferartort;
	}

	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}
	@Column(name = "N_MTL_ZAHLBETRAG")
	private BigDecimal nMtlZahlbetrag;
	
	@Column(name = "I_ZAHLTAG_MTL_ZAHLBETRAG")
	private Integer iZahltagMtlZahlbetrag;
	
	public BigDecimal getNMtlZahlbetrag() {
		return nMtlZahlbetrag;
	}

	public void setNMtlZahlbetrag(BigDecimal nMtlZahlbetrag) {
		this.nMtlZahlbetrag = nMtlZahlbetrag;
	}

	public Integer getIZahltagMtlZahlbetrag() {
		return iZahltagMtlZahlbetrag;
	}

	public void setIZahltagMtlZahlbetrag(Integer iZahltagMtlZahlbetrag) {
		this.iZahltagMtlZahlbetrag = iZahltagMtlZahlbetrag;
	}

	private static final long serialVersionUID = 1L;

	public Rechnung() {
		super();
	}

	public Rechnung(Integer id, String mandantCNr, Integer geschaeftsjahr,
			String nr, Integer kundeIId, Timestamp belegdatum,
			String statusCNr, String rechnungartCNr,Integer kostenstelleIId, String waehrungCNr,
			 Short mwstsatzallepositionen,
			Integer personalIIdAnlegen, Integer personalIIdAendern,
			 Short reversecharge,Integer kundeIIdStatistikadresse, Integer personalIIdVertreter,
			Integer lieferartIId, 
			Integer zahlungszielIId,
			Integer spediteurIId 
	) {
		setIId(id);
		setMandantCNr(mandantCNr);
		setIGeschaeftsjahr(geschaeftsjahr);
		setCNr(nr);
		setKundeIId(kundeIId);
		// vorlaeufig gleich setzen - wird spaeter eh ueberschrieben.
	    setKundeIIdStatistikadresse(kundeIId);
		setTBelegdatum(Helper.cutTimestamp(belegdatum));
		setStatusCNr(statusCNr);
		setRechnungartCNr(rechnungartCNr);
		setKostenstelleIId(kostenstelleIId);
		setWaehrungCNr(waehrungCNr);
		setBMwstallepositionen(mwstsatzallepositionen);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setPersonalIIdAendern(personalIIdAendern);
		setPersonalIIdVertreter(personalIIdVertreter);
		setBReversecharge(reversecharge);
		setLieferartIId(lieferartIId);
		setZahlungszielIId(zahlungszielIId);
		setSpediteurIId(spediteurIId);
		// Setzen der NOT NULL felder
	    Timestamp now = new Timestamp(System.currentTimeMillis());
	    this.setFAllgemeinerrabattsatz(new Double(0));
	    this.setFVersteckteraufschlag(new Double(0));
	    this.setTAendern(now);
	    this.setTAnlegen(now);
	}
	
	
	public void setGutschriftsgrundIId(Integer gutschriftsgrundIId){
		this.gutschriftsgrundIId = gutschriftsgrundIId;
	}
	
	public Integer getGutschriftsgrundIId(){
		return this.gutschriftsgrundIId;
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

	public BigDecimal getNKurs() {
		return this.nKurs;
	}

	public void setNKurs(BigDecimal nKurs) {
		this.nKurs = nKurs;
	}

	public Short getBMwstallepositionen() {
		return this.bMwstallepositionen;
	}

	public void setBMwstallepositionen(Short bMwstallepositionen) {
		this.bMwstallepositionen = bMwstallepositionen;
	}

	public BigDecimal getNWert() {
		return this.nWert;
	}

	public void setNWert(BigDecimal nWert) {
		this.nWert = nWert;
	}

	public BigDecimal getNWertfw() {
		return this.nWertfw;
	}

	public void setNWertfw(BigDecimal nWertfw) {
		this.nWertfw = nWertfw;
	}

	public BigDecimal getNWertust() {
		return this.nWertust;
	}

	public void setNWertust(BigDecimal nWertust) {
		this.nWertust = nWertust;
	}

	public BigDecimal getNWertustfw() {
		return this.nWertustfw;
	}

	public void setNWertustfw(BigDecimal nWertustfw) {
		this.nWertustfw = nWertustfw;
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

	public Short getBMindermengenzuschlag() {
		return this.bMindermengenzuschlag;
	}

	public void setBMindermengenzuschlag(Short bMindermengenzuschlag) {
		this.bMindermengenzuschlag = bMindermengenzuschlag;
	}

	public BigDecimal getNProvision() {
		return this.nProvision;
	}

	public void setNProvision(BigDecimal nProvision) {
		this.nProvision = nProvision;
	}

	public String getCProvisiontext() {
		return this.cProvisiontext;
	}

	public void setCProvisiontext(String cProvisiontext) {
		this.cProvisiontext = cProvisiontext;
	}

	public Timestamp getTGedruckt() {
		return this.tGedruckt;
	}

	public void setTGedruckt(Timestamp tGedruckt) {
		this.tGedruckt = tGedruckt;
	}

	public Timestamp getTFibuuebernahme() {
		return this.tFibuuebernahme;
	}

	public void setTFibuuebernahme(Timestamp tFibuuebernahme) {
		this.tFibuuebernahme = tFibuuebernahme;
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

	public Timestamp getTStorniert() {
		return this.tStorniert;
	}

	public void setTStorniert(Timestamp tStorniert) {
		this.tStorniert = tStorniert;
	}

	public Date getTBezahltdatum() {
		return this.tBezahltdatum;
	}

	public void setTBezahltdatum(Date tBezahltdatum) {
		this.tBezahltdatum = tBezahltdatum;
	}

	public Date getTMahnsperrebis() {
		return this.tMahnsperrebis;
	}

	public void setTMahnsperrebis(Date tMahnsperrebis) {
		this.tMahnsperrebis = tMahnsperrebis;
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

	public String getCBestellnummer() {
		return this.cBestellnummer;
	}

	public void setCBestellnummer(String cBestellnummer) {
		this.cBestellnummer = cBestellnummer;
	}

	public Short getBReversecharge() {
		return this.bReversecharge;
	}

	public void setBReversecharge(Short bReversecharge) {
		this.bReversecharge = bReversecharge;
	}

	public Integer getAuftragIId() {
		return this.auftragIId;
	}

	public void setAuftragIId(Integer auftragIId) {
		this.auftragIId = auftragIId;
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

	public Integer getMwstsatzIId() {
		return this.mwstsatzIId;
	}

	public void setMwstsatzIId(Integer mwstsatzIId) {
		this.mwstsatzIId = mwstsatzIId;
	}

	public Integer getSpediteurIId() {
		return this.spediteurIId;
	}

	public void setSpediteurIId(Integer spediteurIId) {
		this.spediteurIId = spediteurIId;
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

	public Integer getLieferscheinIId() {
		return this.lieferscheinIId;
	}

	public void setLieferscheinIId(Integer lieferscheinIId) {
		this.lieferscheinIId = lieferscheinIId;
	}

	public Integer getAnsprechpartnerIId() {
		return this.ansprechpartnerIId;
	}

	public void setAnsprechpartnerIId(Integer ansprechpartnerIId) {
		this.ansprechpartnerIId = ansprechpartnerIId;
	}

	public Integer getKundeIIdStatistikadresse() {
		return this.kundeIIdStatistikadresse;
	}

	public void setKundeIIdStatistikadresse(Integer kundeIIdStatistikadresse) {
		this.kundeIIdStatistikadresse = kundeIIdStatistikadresse;
	}

	public Integer getKundeIId() {
		return this.kundeIId;
	}

	public void setKundeIId(Integer kundeIId) {
		this.kundeIId = kundeIId;
	}

	public Integer getPersonalIIdManuellerledigt() {
		return this.personalIIdManuellerledigt;
	}

	public void setPersonalIIdManuellerledigt(Integer personalIIdManuellerledigt) {
		this.personalIIdManuellerledigt = personalIIdManuellerledigt;
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

	public Integer getRechnungIIdZurechnung() {
		return this.rechnungIIdZurechnung;
	}

	public void setRechnungIIdZurechnung(Integer rechnungIIdZurechnung) {
		this.rechnungIIdZurechnung = rechnungIIdZurechnung;
	}

	public String getRechnungartCNr() {
		return this.rechnungartCNr;
	}

	public void setRechnungartCNr(String rechnungartCNr) {
		this.rechnungartCNr = rechnungartCNr;
	}

	public String getStatusCNr() {
		return this.statusCNr;
	}

	public void setStatusCNr(String statusCNr) {
		this.statusCNr = statusCNr;
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

}

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
package com.lp.server.reklamation.ejb;

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
		@NamedQuery(name = "ReklamationfindByMandantCNrCNr", query = "SELECT OBJECT(o) FROM Reklamation o WHERE o.mandantCNr = ?1 AND o.cNr = ?2"),
		@NamedQuery(name = "ReklamationfindByKundeIIdMandantCNr", query = "SELECT OBJECT(O) FROM Reklamation o WHERE o.kundeIId = ?1 AND o.mandantCNr = ?2"),
		@NamedQuery(name = "ReklamationfindByArtikelIIdCSeriennrchargennrMandantCNr", query = "SELECT OBJECT(O) FROM Reklamation o WHERE o.artikelIId = ?1 AND o.cSeriennrchargennr = ?2 AND o.mandantCNr = ?3"),
		@NamedQuery(name = "ReklamationfindByLieferantIIdMandantCNr", query = "SELECT OBJECT(O) FROM Reklamation o WHERE o.lieferantIId = ?1 AND o.mandantCNr = ?2"),
		@NamedQuery(name = "ReklamationfindOffeneReklamationenEinesArtikels", query = "SELECT OBJECT(O) FROM Reklamation o WHERE o.artikelIId = ?1 AND o.tErledigt IS NULL ORDER BY o.cNr ASC"),
		@NamedQuery(name = "ReklamationfindByWareneingangIIdMandantCNr", query = "SELECT OBJECT(O) FROM Reklamation o WHERE o.wareneingangIId = ?1 AND o.mandantCNr = ?2"),
		@NamedQuery(name = "ReklamationfindByAnsprechpartnerIId", query = "SELECT OBJECT(O) FROM Reklamation o WHERE o.ansprechpartnerIId = ?1"),
		@NamedQuery(name = "ReklamationfindByAnsprechpartnerIIdLieferant", query = "SELECT OBJECT(O) FROM Reklamation o WHERE o.ansprechpartnerIIdLieferant = ?1") })
@Entity
@Table(name = "REKLA_REKLAMATION")
public class Reklamation implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "T_BELEGDATUM")
	private Timestamp tBelegdatum;

	@Column(name = "B_ARTIKEL")
	private Short bArtikel;

	@Column(name = "C_HANDARTIKEL")
	private String cHandartikel;

	@Column(name = "N_MENGE")
	private BigDecimal nMenge;

	@Column(name = "B_FREMDPRODUKT")
	private Short bFremdprodukt;

	@Column(name = "C_SERIENNRCHARGENNR")
	private String cSeriennrchargennr;

	@Column(name = "C_GRUND")
	private String cGrund;

	@Column(name = "C_PROJEKT")
	private String cProjekt;

	@Column(name = "C_TELANSPRECHPARTNER")
	private String cTelansprechpartner;

	@Column(name = "C_TELANSPRECHPARTNER_LIEFERANT")
	private String cTelansprechpartnerLieferant;

	public String getCTelansprechpartnerLieferant() {
		return cTelansprechpartnerLieferant;
	}

	public void setCTelansprechpartnerLieferant(
			String cTelansprechpartnerLieferant) {
		this.cTelansprechpartnerLieferant = cTelansprechpartnerLieferant;
	}

	@Column(name = "STATUS_C_NR")
	private String statusCNr;

	public String getStatusCNr() {
		return statusCNr;
	}

	public void setStatusCNr(String statusCNr) {
		this.statusCNr = statusCNr;
	}

	@Column(name = "X_ANALYSE")
	private String xAnalyse;

	@Column(name = "X_KOMMENTAR")
	private String xKommentar;

	@Column(name = "B_BERECHTIGT")
	private Short bBerechtigt;

	@Column(name = "T_RUECKSPRACHE")
	private Timestamp tRuecksprache;

	@Column(name = "C_RUECKSPRACHEMIT")
	private String cRuecksprachemit;

	@Column(name = "N_KOSTENMATERIAL")
	private BigDecimal nKostenmaterial;

	@Column(name = "N_KOSTENARBEITSZEIT")
	private BigDecimal nKostenarbeitszeit;

	@Column(name = "T_MASSNAHMEBISKURZ")
	private Timestamp tMassnahmebiskurz;

	@Column(name = "T_EINGEFUEHRTKURZ")
	private Timestamp tEingefuehrtkurz;

	@Column(name = "T_MASSNAHMEBISMITTEL")
	private Timestamp tMassnahmebismittel;

	@Column(name = "T_EINGEFUEHRTMITTEL")
	private Timestamp tEingefuehrtmittel;

	@Column(name = "T_MASSNAHMEBISLANG")
	private Timestamp tMassnahmebislang;

	@Column(name = "T_EINGEFUEHRTLANG")
	private Timestamp tEingefuehrtlang;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "T_ERLEDIGT")
	private Timestamp tErledigt;

	@Column(name = "BESTELLUNG_I_ID")
	private Integer bestellungIId;

	@Column(name = "LOS_I_ID")
	private Integer losIId;

	@Column(name = "I_KUNDEUNTERART")
	private Integer iKundeunterart;

	public Integer getIKundeunterart() {
		return iKundeunterart;
	}

	public void setIKundeunterart(Integer iKundeunterart) {
		this.iKundeunterart = iKundeunterart;
	}

	@Column(name = "KOSTENSTELLE_I_ID")
	private Integer kostenstelleIId;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "LIEFERSCHEIN_I_ID")
	private Integer lieferscheinIId;

	@Column(name = "ANSPRECHPARTNER_I_ID")
	private Integer ansprechpartnerIId;

	@Column(name = "ANSPRECHPARTNER_I_ID_LIEFERANT")
	private Integer ansprechpartnerIIdLieferant;

	public Integer getAnsprechpartnerIIdLieferant() {
		return ansprechpartnerIIdLieferant;
	}

	public void setAnsprechpartnerIIdLieferant(
			Integer ansprechpartnerIIdLieferant) {
		this.ansprechpartnerIIdLieferant = ansprechpartnerIIdLieferant;
	}

	@Column(name = "LOSSOLLARBEITSPLAN_I_ID")
	private Integer lossollarbeitsplanIId;

	public Integer getLossollarbeitsplanIId() {
		return lossollarbeitsplanIId;
	}

	public void setLossollarbeitsplanIId(Integer lossollarbeitsplanIId) {
		this.lossollarbeitsplanIId = lossollarbeitsplanIId;
	}

	public Integer getPersonalIIdVerursacher() {
		return personalIIdVerursacher;
	}

	public void setPersonalIIdVerursacher(Integer personalIIdVerursacher) {
		this.personalIIdVerursacher = personalIIdVerursacher;
	}

	@Column(name = "KUNDE_I_ID")
	private Integer kundeIId;

	@Column(name = "LIEFERANT_I_ID")
	private Integer lieferantIId;

	@Column(name = "PERSONAL_I_ID_EINGEFUEHRTLANG")
	private Integer personalIIdEingefuehrtlang;

	@Column(name = "PERSONAL_I_ID_ERLEDIGT")
	private Integer personalIIdErledigt;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_EINGEFUEHRTKURZ")
	private Integer personalIIdEingefuehrtkurz;

	@Column(name = "PERSONAL_I_ID_AUFNEHMER")
	private Integer personalIIdAufnehmer;

	@Column(name = "PERSONAL_I_ID_RUECKSPRACHE")
	private Integer personalIIdRuecksprache;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "PERSONAL_I_ID_EINGEFUEHRTMITTEL")
	private Integer personalIIdEingefuehrtmittel;

	@Column(name = "PERSONAL_I_ID_WIRKSAMKEIT")
	private Integer personalIIdWirksamkeit;

	@Column(name = "PERSONAL_I_ID_VERURSACHER")
	private Integer personalIIdVerursacher;

	@Column(name = "T_WIRKSAMKEITBIS")
	private Timestamp tWirksamkeitbis;

	@Column(name = "T_WIRKSAMKEITEINGEFUEHRT")
	private Timestamp tWirksamkeiteingefuehrt;

	@Column(name = "X_GRUND_LANG")
	private String xGrundLang;

	@Column(name = "MASCHINE_I_ID")
	private Integer maschineIId;

	@Column(name = "C_KDREKLANR")
	private String cKdreklanr;

	@Column(name = "C_KDLSNR")
	private String cKdlsnr;

	public String getCSeriennrchargennr() {
		return this.cSeriennrchargennr;
	}

	public void setCSeriennrchargennr(String cSeriennrchargennr) {
		this.cSeriennrchargennr = cSeriennrchargennr;
	}

	public Integer getMaschineIId() {
		return maschineIId;
	}

	public void setMaschineIId(Integer maschineIId) {
		this.maschineIId = maschineIId;
	}

	public String getCKdreklanr() {
		return cKdreklanr;
	}

	public void setCKdreklanr(String cKdreklanr) {
		this.cKdreklanr = cKdreklanr;
	}

	public String getCKdlsnr() {
		return cKdlsnr;
	}

	public void setCKdlsnr(String cKdlsnr) {
		this.cKdlsnr = cKdlsnr;
	}

	public Integer getPersonalIIdWirksamkeit() {
		return personalIIdWirksamkeit;
	}

	public void setPersonalIIdWirksamkeit(Integer personalIIdWirksamkeit) {
		this.personalIIdWirksamkeit = personalIIdWirksamkeit;
	}

	public Timestamp getTWirksamkeitbis() {
		return tWirksamkeitbis;
	}

	public void setTWirksamkeitbis(Timestamp wirksamkeitbis) {
		tWirksamkeitbis = wirksamkeitbis;
	}

	public Timestamp getTWirksamkeiteingefuehrt() {
		return tWirksamkeiteingefuehrt;
	}

	public void setTWirksamkeiteingefuehrt(Timestamp wirksamkeiteingefuehrt) {
		tWirksamkeiteingefuehrt = wirksamkeiteingefuehrt;
	}

	public String getXGrundLang() {
		return xGrundLang;
	}

	public void setXGrundLang(String grundLang) {
		xGrundLang = grundLang;
	}

	@Column(name = "RECHNUNG_I_ID")
	private Integer rechnungIId;

	@Column(name = "AUFNAHMEART_I_ID")
	private Integer aufnahmeartIId;

	@Column(name = "FEHLER_I_ID")
	private Integer fehlerIId;

	@Column(name = "FEHLERANGABE_I_ID")
	private Integer fehlerangabeIId;

	@Column(name = "MASSNAHME_I_ID_LANG")
	private Integer massnahmeIIdLang;

	@Column(name = "MASSNAHME_I_ID_MITTEL")
	private Integer massnahmeIIdMittel;

	@Column(name = "MASSNAHME_I_ID_KURZ")
	private Integer massnahmeIIdKurz;

	@Column(name = "REKLAMATIONART_C_NR")
	private String reklamationartCNr;

	@Column(name = "SCHWERE_I_ID")
	private Integer schwereIId;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@Column(name = "BEHANDLUNG_I_ID")
	private Integer behandlungIId;

	@Column(name = "WARENEINGANG_I_ID")
	private Integer wareneingangIId;

	@Column(name = "WIRKSAMKEIT_I_ID")
	private Integer wirksamkeitIId;

	public Integer getWirksamkeitIId() {
		return wirksamkeitIId;
	}

	public void setWirksamkeitIId(Integer wirksamkeitIId) {
		this.wirksamkeitIId = wirksamkeitIId;
	}

	public String getXWirksamkeit() {
		return xWirksamkeit;
	}

	public void setXWirksamkeit(String wirksamkeit) {
		xWirksamkeit = wirksamkeit;
	}

	public String getXMassnahmeLang() {
		return xMassnahmeLang;
	}

	public void setXMassnahmeLang(String massnahmeLang) {
		xMassnahmeLang = massnahmeLang;
	}

	public String getXMassnahmeKurz() {
		return xMassnahmeKurz;
	}

	public void setXMassnahmeKurz(String massnahmeKurz) {
		xMassnahmeKurz = massnahmeKurz;
	}

	public String getXMassnahmeMittel() {
		return xMassnahmeMittel;
	}

	public void setXMassnahmeMittel(String massnahmeMittel) {
		xMassnahmeMittel = massnahmeMittel;
	}

	public Short getBBetrifftlagerstand() {
		return bBetrifftlagerstand;
	}

	public void setBBetrifftlagerstand(Short betrifftlagerstand) {
		bBetrifftlagerstand = betrifftlagerstand;
	}

	public Short getBBetrifftgelieferte() {
		return bBetrifftgelieferte;
	}

	public void setBBetrifftgelieferte(Short betrifftgelieferte) {
		bBetrifftgelieferte = betrifftgelieferte;
	}

	public BigDecimal getNStuecklagerstand() {
		return nStuecklagerstand;
	}

	public void setNStuecklagerstand(BigDecimal stuecklagerstand) {
		nStuecklagerstand = stuecklagerstand;
	}

	public BigDecimal getNStueckgelieferte() {
		return nStueckgelieferte;
	}

	public void setNStueckgelieferte(BigDecimal stueckgelieferte) {
		nStueckgelieferte = stueckgelieferte;
	}

	@Column(name = "X_WIRKSAMKEIT")
	private String xWirksamkeit;
	@Column(name = "X_MASSNAHME_LANG")
	private String xMassnahmeLang;
	@Column(name = "X_MASSNAHME_KURZ")
	private String xMassnahmeKurz;
	@Column(name = "X_MASSNAHME_MITTEL")
	private String xMassnahmeMittel;
	@Column(name = "B_BETRIFFTLAGERSTAND")
	private Short bBetrifftlagerstand;
	@Column(name = "B_BETRIFFTGELIFERTE")
	private Short bBetrifftgelieferte;
	@Column(name = "N_STUECKLAGERSTAND")
	private BigDecimal nStuecklagerstand;
	@Column(name = "N_STUECKGELIEFERTE")
	private BigDecimal nStueckgelieferte;

	public Integer getWareneingangIId() {
		return wareneingangIId;
	}

	public void setWareneingangIId(Integer wareneingangIId) {
		this.wareneingangIId = wareneingangIId;
	}

	public Integer getBehandlungIId() {
		return behandlungIId;
	}

	public void setBehandlungIId(Integer behandlungIId) {
		this.behandlungIId = behandlungIId;
	}

	private static final long serialVersionUID = 1L;

	public Reklamation() {
		super();
	}

	public Reklamation(Integer id, String mandantCNr, String nr,
			String reklamationartCNr, Timestamp belegdatum,
			Integer kostenstelleIId, Integer fehlerangabeIId,
			Integer aufnahmeartIId, Integer personalIIdAufnehmer2,
			Short artikel2, BigDecimal menge, Short fremdprodukt,
			Short berechtigt, Short betrifftgelieferte,
			Short betrifftlagerstand, Integer personalIIdAnlegen2,
			Integer personalIIdAendern2, String statusCNr) {
		setIId(id);
		setMandantCNr(mandantCNr);
		setCNr(nr);
		setReklamationartCNr(reklamationartCNr);
		setTBelegdatum(belegdatum);
		setKostenstelleIId(kostenstelleIId);
		setFehlerangabeIId(fehlerangabeIId);
		setAufnahmeartIId(aufnahmeartIId);
		setPersonalIIdAufnehmer(personalIIdAufnehmer2);
		setBArtikel(artikel2);
		setNMenge(menge);
		setBFremdprodukt(fremdprodukt);
		setBBerechtigt(berechtigt);
		setBBetrifftgelieferte(betrifftgelieferte);
		setBBetrifftlagerstand(betrifftlagerstand);
		setPersonalIIdAnlegen(personalIIdAnlegen2);
		Timestamp t = new Timestamp(System.currentTimeMillis());
		setTAnlegen(t);
		setPersonalIIdAendern(personalIIdAendern2);
		setTAendern(t);
		setStatusCNr(statusCNr);
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

	public Short getBArtikel() {
		return this.bArtikel;
	}

	public void setBArtikel(Short bArtikel) {
		this.bArtikel = bArtikel;
	}

	public String getCHandartikel() {
		return this.cHandartikel;
	}

	public void setCHandartikel(String cHandartikel) {
		this.cHandartikel = cHandartikel;
	}

	public BigDecimal getNMenge() {
		return this.nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public Short getBFremdprodukt() {
		return this.bFremdprodukt;
	}

	public void setBFremdprodukt(Short bFremdprodukt) {
		this.bFremdprodukt = bFremdprodukt;
	}

	public String getCGrund() {
		return this.cGrund;
	}

	public void setCGrund(String cGrund) {
		this.cGrund = cGrund;
	}

	public String getCProjekt() {
		return this.cProjekt;
	}

	public void setCProjekt(String cProjekt) {
		this.cProjekt = cProjekt;
	}

	public String getCTelansprechpartner() {
		return this.cTelansprechpartner;
	}

	public void setCTelansprechpartner(String cTelansprechpartner) {
		this.cTelansprechpartner = cTelansprechpartner;
	}

	public String getXAnalyse() {
		return this.xAnalyse;
	}

	public void setXAnalyse(String xAnalyse) {
		this.xAnalyse = xAnalyse;
	}

	public String getXKommentar() {
		return this.xKommentar;
	}

	public void setXKommentar(String xKommentar) {
		this.xKommentar = xKommentar;
	}

	public Short getBBerechtigt() {
		return this.bBerechtigt;
	}

	public void setBBerechtigt(Short bBerechtigt) {
		this.bBerechtigt = bBerechtigt;
	}

	public Timestamp getTRuecksprache() {
		return this.tRuecksprache;
	}

	public void setTRuecksprache(Timestamp tRuecksprache) {
		this.tRuecksprache = tRuecksprache;
	}

	public String getCRuecksprachemit() {
		return this.cRuecksprachemit;
	}

	public void setCRuecksprachemit(String cRuecksprachemit) {
		this.cRuecksprachemit = cRuecksprachemit;
	}

	public BigDecimal getNKostenmaterial() {
		return this.nKostenmaterial;
	}

	public void setNKostenmaterial(BigDecimal nKostenmaterial) {
		this.nKostenmaterial = nKostenmaterial;
	}

	public BigDecimal getNKostenarbeitszeit() {
		return this.nKostenarbeitszeit;
	}

	public void setNKostenarbeitszeit(BigDecimal nKostenarbeitszeit) {
		this.nKostenarbeitszeit = nKostenarbeitszeit;
	}

	public Timestamp getTMassnahmebiskurz() {
		return this.tMassnahmebiskurz;
	}

	public void setTMassnahmebiskurz(Timestamp tMassnahmebiskurz) {
		this.tMassnahmebiskurz = tMassnahmebiskurz;
	}

	public Timestamp getTEingefuehrtkurz() {
		return this.tEingefuehrtkurz;
	}

	public void setTEingefuehrtkurz(Timestamp tEingefuehrtkurz) {
		this.tEingefuehrtkurz = tEingefuehrtkurz;
	}

	public Timestamp getTMassnahmebismittel() {
		return this.tMassnahmebismittel;
	}

	public void setTMassnahmebismittel(Timestamp tMassnahmebismittel) {
		this.tMassnahmebismittel = tMassnahmebismittel;
	}

	public Timestamp getTEingefuehrtmittel() {
		return this.tEingefuehrtmittel;
	}

	public void setTEingefuehrtmittel(Timestamp tEingefuehrtmittel) {
		this.tEingefuehrtmittel = tEingefuehrtmittel;
	}

	public Timestamp getTMassnahmebislang() {
		return this.tMassnahmebislang;
	}

	public void setTMassnahmebislang(Timestamp tMassnahmebislang) {
		this.tMassnahmebislang = tMassnahmebislang;
	}

	public Timestamp getTEingefuehrtlang() {
		return this.tEingefuehrtlang;
	}

	public void setTEingefuehrtlang(Timestamp tEingefuehrtlang) {
		this.tEingefuehrtlang = tEingefuehrtlang;
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

	public Timestamp getTErledigt() {
		return this.tErledigt;
	}

	public void setTErledigt(Timestamp tErledigt) {
		this.tErledigt = tErledigt;
	}

	public Integer getBestellungIId() {
		return this.bestellungIId;
	}

	public void setBestellungIId(Integer bestellungIId) {
		this.bestellungIId = bestellungIId;
	}

	public Integer getLosIId() {
		return this.losIId;
	}

	public void setLosIId(Integer losIId) {
		this.losIId = losIId;
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

	public Integer getKundeIId() {
		return this.kundeIId;
	}

	public void setKundeIId(Integer kundeIId) {
		this.kundeIId = kundeIId;
	}

	public Integer getLieferantIId() {
		return this.lieferantIId;
	}

	public void setLieferantIId(Integer lieferantIId) {
		this.lieferantIId = lieferantIId;
	}

	public Integer getPersonalIIdEingefuehrtlang() {
		return this.personalIIdEingefuehrtlang;
	}

	public void setPersonalIIdEingefuehrtlang(Integer personalIIdEingefuehrtlang) {
		this.personalIIdEingefuehrtlang = personalIIdEingefuehrtlang;
	}

	public Integer getPersonalIIdErledigt() {
		return this.personalIIdErledigt;
	}

	public void setPersonalIIdErledigt(Integer personalIIdErledigt) {
		this.personalIIdErledigt = personalIIdErledigt;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getPersonalIIdEingefuehrtkurz() {
		return this.personalIIdEingefuehrtkurz;
	}

	public void setPersonalIIdEingefuehrtkurz(Integer personalIIdEingefuehrtkurz) {
		this.personalIIdEingefuehrtkurz = personalIIdEingefuehrtkurz;
	}

	public Integer getPersonalIIdAufnehmer() {
		return this.personalIIdAufnehmer;
	}

	public void setPersonalIIdAufnehmer(Integer personalIIdAufnehmer) {
		this.personalIIdAufnehmer = personalIIdAufnehmer;
	}

	public Integer getPersonalIIdRuecksprache() {
		return this.personalIIdRuecksprache;
	}

	public void setPersonalIIdRuecksprache(Integer personalIIdRuecksprache) {
		this.personalIIdRuecksprache = personalIIdRuecksprache;
	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Integer getPersonalIIdEingefuehrtmittel() {
		return this.personalIIdEingefuehrtmittel;
	}

	public void setPersonalIIdEingefuehrtmittel(
			Integer personalIIdEingefuehrtmittel) {
		this.personalIIdEingefuehrtmittel = personalIIdEingefuehrtmittel;
	}

	public Integer getRechnungIId() {
		return this.rechnungIId;
	}

	public void setRechnungIId(Integer rechnungIId) {
		this.rechnungIId = rechnungIId;
	}

	public Integer getAufnahmeartIId() {
		return this.aufnahmeartIId;
	}

	public void setAufnahmeartIId(Integer aufnahmeartIId) {
		this.aufnahmeartIId = aufnahmeartIId;
	}

	public Integer getFehlerIId() {
		return this.fehlerIId;
	}

	public void setFehlerIId(Integer fehlerIId) {
		this.fehlerIId = fehlerIId;
	}

	public Integer getFehlerangabeIId() {
		return this.fehlerangabeIId;
	}

	public void setFehlerangabeIId(Integer fehlerangabeIId) {
		this.fehlerangabeIId = fehlerangabeIId;
	}

	public Integer getMassnahmeIIdLang() {
		return this.massnahmeIIdLang;
	}

	public void setMassnahmeIIdLang(Integer massnahmeIIdLang) {
		this.massnahmeIIdLang = massnahmeIIdLang;
	}

	public Integer getMassnahmeIIdMittel() {
		return this.massnahmeIIdMittel;
	}

	public void setMassnahmeIIdMittel(Integer massnahmeIIdMittel) {
		this.massnahmeIIdMittel = massnahmeIIdMittel;
	}

	public Integer getMassnahmeIIdKurz() {
		return this.massnahmeIIdKurz;
	}

	public void setMassnahmeIIdKurz(Integer massnahmeIIdKurz) {
		this.massnahmeIIdKurz = massnahmeIIdKurz;
	}

	public String getReklamationartCNr() {
		return this.reklamationartCNr;
	}

	public void setReklamationartCNr(String reklamationartCNr) {
		this.reklamationartCNr = reklamationartCNr;
	}

	public Integer getSchwereIId() {
		return this.schwereIId;
	}

	public void setSchwereIId(Integer schwereIId) {
		this.schwereIId = schwereIId;
	}

	public Integer getArtikelIId() {
		return this.artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

}

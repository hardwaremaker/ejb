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
package com.lp.server.partner.ejb;

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
import com.lp.util.Helper;

@NamedQueries({
		@NamedQuery(name = "KundefindAllKurznr", query = "SELECT OBJECT(C) FROM Kunde c WHERE SUBSTRING(c.cKurznr, 1,1) =?1"),
		@NamedQuery(name = "KundefindByCKurznr", query = "SELECT OBJECT(C) FROM Kunde c WHERE c.cKurznr=?1"),
		@NamedQuery(name = "KundefindByVkpfArtikelpreislisteIIdStdpreisliste", query = "SELECT OBJECT (o) FROM Kunde o WHERE o.vkpfartikelpreislisteIIdstdpreisliste=?1"),
		@NamedQuery(name = "KundefindByiIdPartnercNrMandant", query = "SELECT OBJECT (o) FROM Kunde o WHERE o.partnerIId=?1 and o.mandantCNr=?2"),
		@NamedQuery(name = KundeQuery.BycFremdsystemnrcNrMandant, query = "SELECT OBJECT (o) FROM Kunde o WHERE o.cFremdsystemnr=?1 and o.mandantCNr=?2"),
		@NamedQuery(name = "KundefindByKontoIIdDebitorenkonto", query = "SELECT OBJECT (o) FROM Kunde o WHERE o.kontoIIdDebitorenkonto=?1"),
		@NamedQuery(name = "KundeejbSelectNextPersonalnummer", query = "SELECT MAX (o.iKundennummer) FROM Kunde o WHERE o.mandantCNr = ?1"),
		@NamedQuery(name = KundeQuery.ByLieferantCnrMandantCnr, query = "SELECT OBJECT (o) FROM Kunde o WHERE o.cLieferantennr = :lieferantCnr AND o.mandantCNr = :mandantCnr"),
		@NamedQuery(name = KundeQuery.ByMandantCnr, query = "SELECT OBJECT (o) FROM Kunde o WHERE o.mandantCNr = :mandantCnr"),
		@NamedQuery(name = KundeQuery.ByPartnerId, query = "SELECT OBJECT (o) FROM Kunde o WHERE o.partnerIId = :partnerId"),
		@NamedQuery(name = KundeQuery.ByCountPartnerId, query = "SELECT COUNT(o) FROM Kunde o WHERE o.partnerIId = :partnerId"),
		@NamedQuery(name = KundeQuery.ByKundenummer, query = "SELECT OBJECT(o) FROM Kunde o WHERE o.iKundennummer = :kundenummer"),
		@NamedQuery(name = KundeQuery.MaxFremdsystemnr, query = "SELECT MAX(CAST(o.cFremdsystemnr AS integer)) FROM Kunde o"),
		@NamedQuery(name = KundeQuery.IIdsByPartnerIds, query = "SELECT o.iId FROM Kunde o WHERE o.partnerIId IN (:partnerIds)"),
		@NamedQuery(name = KundeQuery.ByKennung, query = "SELECT OBJECT(o) FROM Kunde o LEFT JOIN KundeKennung kk ON o.iId = kk.kundeIId WHERE kk.kennungIId = :kennungid AND kk.cWert = :value"),
		@NamedQuery(name = "KundefindByCEmailRechnungsempfangMandantCNr", query = "SELECT OBJECT (o) FROM Kunde o WHERE o.cEmailRechnungsempfang=?1 and o.mandantCNr=?2")})

@Entity
@Table(name = ITablenames.PART_KUNDE)
public class Kunde implements Serializable {

	public Kunde() {
	}

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "N_MINDESTBESTELLWERT")
	private BigDecimal nMindestbestellwert;

	public BigDecimal getNMindestbestellwert() {
		return nMindestbestellwert;
	}

	public void setNMindestbestellwert(BigDecimal nMindestbestellwert) {
		this.nMindestbestellwert = nMindestbestellwert;
	}

	@Column(name = "N_KUPFERZAHL")
	private BigDecimal nKupferzahl;

	public BigDecimal getNKupferzahl() {
		return nKupferzahl;
	}

	public void setNKupferzahl(BigDecimal nKupferzahl) {
		this.nKupferzahl = nKupferzahl;
	}

	@Column(name = "B_RECHNUNG_JE_LIEFERADRESSE")
	private Short bRechnungJeLieferadresse;

	@Column(name = "B_VKPREIS_ANHAND_LS_DATUM")
	private Short bVkpreisAnhandLSDatum;

	public Short getBVkpreisAnhandLSDatum() {
		return bVkpreisAnhandLSDatum;
	}

	public void setBVkpreisAnhandLSDatum(Short bVkpreisAnhandLSDatum) {
		this.bVkpreisAnhandLSDatum = bVkpreisAnhandLSDatum;
	}

	public Short getBRechnungJeLieferadresse() {
		return bRechnungJeLieferadresse;
	}

	public void setBRechnungJeLieferadresse(Short bRechnungJeLieferadresse) {
		this.bRechnungJeLieferadresse = bRechnungJeLieferadresse;
	}

	@Column(name = "B_MINDERMENGENZUSCHLAG")
	private Short bMindermengenzuschlag;

	@Column(name = "B_ZUSCHLAG_INKLUSIVE")
	private Short bZuschlagInklusive;

	public Short getBZuschlagInklusive() {
		return bZuschlagInklusive;
	}

	public void setBZuschlagInklusive(Short bZuschlagInklusive) {
		this.bZuschlagInklusive = bZuschlagInklusive;
	}

	@Column(name = "B_MONATSRECHNUNG")
	private Short bMonatsrechnung;

	@Column(name = "B_SAMMELRECHNUNG")
	private Short bSammelrechnung;

	@Column(name = "B_ISTREEMPFAENGER")
	private Short bIstreempfaenger;

	@Column(name = "B_PREISEANLSANDRUCKEN")
	private Short bPreiseanlsandrucken;

	@Column(name = "B_RECHNUNGSDRUCKMITRABATT")
	private Short bRechnungsdruckmitrabatt;

	@Column(name = "B_DISTRIBUTOR")
	private Short bDistributor;

	@Column(name = "B_AKZEPTIERTTEILLIEFERUNG")
	private Short bAkzeptiertteillieferung;

	@Column(name = "B_LSGEWICHTANGEBEN")
	private Short bLsgewichtangeben;

	@Column(name = "B_ISTINTERESSENT")
	private Short bIstinteressent;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "F_RABATTSATZ")
	private Double fRabattsatz;

	@Column(name = "I_GARANTIEINMONATEN")
	private Integer iGarantieinmonaten;

	@Column(name = "I_LIEFERDAUER")
	private Integer iLieferdauer;

	@Column(name = "I_MAX_REPOS")
	private Integer iMaxRepos;

	public Integer getIMaxRepos() {
		return iMaxRepos;
	}

	public void setIMaxRepos(Integer iMaxRepos) {
		this.iMaxRepos = iMaxRepos;
	}

	public Integer getILieferdauer() {
		return iLieferdauer;
	}

	@Column(name = "VERRECHNUNGSMODELL_I_ID")
	private Integer verrechnungsmodellIId;

	public Integer getVerrechnungsmodellIId() {
		return verrechnungsmodellIId;
	}

	public void setVerrechnungsmodellIId(Integer verrechnungsmodellIId) {
		this.verrechnungsmodellIId = verrechnungsmodellIId;
	}

	public void setILieferdauer(Integer lieferdauer) {
		iLieferdauer = lieferdauer;
	}

	@Column(name = "X_KOMMENTAR")
	private String xKommentar;

	@Column(name = "C_KURZNR")
	private String cKurznr;

	@Column(name = "N_KREDITLIMIT")
	private BigDecimal nKreditlimit;

	@Column(name = "T_BONITAET")
	private Date tBonitaet;

	@Column(name = "T_LIEFERSPERREAM")
	private Date tLiefersperream;

	@Column(name = "I_DEFAULTREKOPIENDRUCKEN")
	private Integer iDefaultrekopiendrucken;

	@Column(name = "I_DEFAULTLSKOPIENDRUCKEN")
	private Integer iDefaultlskopiendrucken;

	@Column(name = "I_MITARBEITERANZAHL")
	private Integer iMitarbeiteranzahl;

	@Column(name = "C_TOUR")
	private String cTour;

	@Column(name = "C_LIEFERANTENNR")
	private String cLieferantennr;
	

	@Column(name = "C_ABC")
	private String cAbc;

	@Column(name = "C_EMAIL_RECHNUNGSEMPFANG")
	private String cEmailRechnungsempfang;

	public String getCEmailRechnungsempfang() {
		return cEmailRechnungsempfang;
	}

	public void setCEmailRechnungsempfang(String cEmailRechnungsempfang) {
		this.cEmailRechnungsempfang = cEmailRechnungsempfang;
	}

	@Column(name = "T_AGBUEBERMITTELUNG")
	private Date tAgbuebermittelung;

	@Column(name = "C_FREMDSYSTEMNR")
	private String cFremdsystemnr;

	@Column(name = "C_HINWEISINTERN")
	private String cHinweisintern;

	@Column(name = "C_HINWEISEXTERN")
	private String cHinweisextern;

	@Column(name = "F_ZESSIONSFAKTOR")
	private Double fZessionsfaktor;

	@Column(name = "F_VERPACKUNGSKOSTEN_IN_PROZENT")
	private Double fVerpackungskostenInProzent;

	public Double getfVerpackungskostenInProzent() {
		return fVerpackungskostenInProzent;
	}

	public void setfVerpackungskostenInProzent(Double fVerpackungskostenInProzent) {
		this.fVerpackungskostenInProzent = fVerpackungskostenInProzent;
	}

	@Column(name = "B_VERSTECKTERLIEFERANT")
	private Short bVersteckterlieferant;

	@Column(name = "B_REVERSECHARGE")
	private Short bReversecharge;

	@Column(name = "I_KUNDENNUMMER")
	private Integer iKundennummer;

	@Column(name = "KONTO_I_ID_DEBITORENKONTO")
	private Integer kontoIIdDebitorenkonto;

	@Column(name = "KONTO_I_ID_ERLOESEKONTO")
	private Integer kontoIIdErloesekonto;

	@Column(name = "KOSTENSTELLE_I_ID")
	private Integer kostenstelleIId;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "SPEDITEUR_I_ID")
	private Integer spediteurIId;

	@Column(name = "WAEHRUNG_C_NR")
	private String waehrungCNr;

	@Column(name = "ZAHLUNGSZIEL_I_ID")
	private Integer zahlungszielIId;

	@Column(name = "PARTNER_I_ID_RECHNUNGSADRESSE")
	private Integer partnerIIdRechnungsadresse;

	@Column(name = "PARTNER_I_ID")
	private Integer partnerIId;

	@Column(name = "PARTNERBANK_I_ID")
	private Integer partnerbankIId;

	@Column(name = "PERSONAL_I_ID_BEKOMMEPROVISION")
	private Integer personalIIdBekommeprovision;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "VKPFARTIKELPREISLISTE_I_ID_STDPREISLISTE")
	private Integer vkpfartikelpreislisteIIdstdpreisliste;

	@Column(name = "LIEFERART_I_ID")
	private Integer lieferartIId;

	@Column(name = "MWSTSATZ_I_ID")
	private Integer mwstsatzIId;

	@Column(name = "T_ERWERBSBERECHTIGUNG")
	private Timestamp tErwerbsberechtigung;

	@Column(name = "C_ERWERBSBERECHTIGUNGSBEGRUENDUNG")
	private String cErwerbsberechtigungsbegruendung;

	@Column(name = "LAGER_I_ID_ABBUCHUNGSLAGER")
	private Integer lagerIIdAbbuchungslager;

	@Column(name = "C_ID_EXTERN")
	private String cIdExtern;

	@Column(name = "B_ZOLLPAPIER")
	private Short bZollpapier;

	@Column(name = "REVERSECHARGEART_I_ID")
	private Integer reversechargeartId;

	@Column(name = "LAENDERART_C_NR")
	private String laenderartCnr;

	public Short getBZollpapier() {
		return bZollpapier;
	}

	public void setBZollpapier(Short bZollpapier) {
		this.bZollpapier = bZollpapier;
	}

	private static final long serialVersionUID = 1L;

	public Kunde(Integer iId, Integer partnerIId, String mandantCNr, String cWaehrung, Integer lieferartIId,
			Integer spediteurIId, Integer zahlungszielIId, Integer kostenstelleIId, Integer mwstsatzbezIId,
			Integer vkpfArtikelpreislisteIIdStdpreisliste, Integer personalAnlegenIId, Integer personalAendernIId,
			Short bAkzeptiertteillieferung, Integer personalIIdProvisionsempfanger, Short bReversecharge,
			Short bVersteckterlieferant, Integer lagerIIdAbbuchungslager, Integer iLieferdauer, Short bZollpapier,
			Integer reversechargeartId, Short bMindermengenzuschlag, Short bRechnungJeLieferadresse,
			Short bVkpreisAnhandLSDatum) {

		setIId(iId);
		setPartnerIId(partnerIId);
		setMandantCNr(mandantCNr);
		setWaehrungCNr(cWaehrung);
		setLieferartIId(lieferartIId);
		setSpediteurIId(spediteurIId);
		setZahlungszielIId(zahlungszielIId);
		setKostenstelleIId(kostenstelleIId);
		setMwstsatzIId(mwstsatzbezIId);
		setVkpfartikelpreislisteIIdstdpreisliste(vkpfArtikelpreislisteIIdStdpreisliste);
		setPersonalIIdAnlegen(personalAnlegenIId);
		setPersonalIIdAendern(personalAendernIId);
		setBAkzeptiertteillieferung(bAkzeptiertteillieferung);
		setPersonalIIdBekommeprovision(personalIIdProvisionsempfanger);
		setBReversecharge(bReversecharge);
		setBVersteckterlieferant(bVersteckterlieferant);
		// die ts anlegen, aendern nur am server
		setTAnlegen(new Timestamp(System.currentTimeMillis()));
		setTAendern(new Timestamp(System.currentTimeMillis()));
		// not null Felder
		setBDistributor(Helper.boolean2Short(false));
		setBIstreempfaenger(Helper.boolean2Short(false));
		setBIstinteressent(Helper.boolean2Short(false));
		setBLsgewichtangeben(Helper.boolean2Short(false));
		setBMonatsrechnung(Helper.boolean2Short(false));
		setBPreiseanlsandrucken(Helper.boolean2Short(false));
		setBRechnungsdruckmitrabatt(Helper.boolean2Short(false));
		setBSammelrechnung(Helper.boolean2Short(false));
		setBZuschlagInklusive(Helper.boolean2Short(false));
		setLagerIIdAbbuchungslager(lagerIIdAbbuchungslager);
		setILieferdauer(iLieferdauer);
		setBZollpapier(bZollpapier);
		setReversechargeartId(reversechargeartId);
		setBMindermengenzuschlag(bMindermengenzuschlag);
		setBRechnungJeLieferadresse(bRechnungJeLieferadresse);
		setBVkpreisAnhandLSDatum(bVkpreisAnhandLSDatum);
	}

	public void setMwstsatzIId(Integer mwstsatzIId) {
		this.mwstsatzIId = mwstsatzIId;
	}

	public Integer getMwstsatzIId() {
		return this.mwstsatzIId;
	}

	public Integer getLieferartIId() {
		return this.lieferartIId;
	}

	public void setLieferartIId(Integer lieferartIId) {
		this.lieferartIId = lieferartIId;
	}

	public void setVkpfartikelpreislisteIIdstdpreisliste(Integer vkpfartikelpreislisteIIdstdpreisliste) {
		this.vkpfartikelpreislisteIIdstdpreisliste = vkpfartikelpreislisteIIdstdpreisliste;
	}

	public Integer getVkpfartikelpreislisteIIdstdpreisliste() {
		return this.vkpfartikelpreislisteIIdstdpreisliste;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getLagerIIdAbbuchungslager() {
		return this.lagerIIdAbbuchungslager;
	}

	public void setLagerIIdAbbuchungslager(Integer lagerIIdAbbuchungslager) {
		this.lagerIIdAbbuchungslager = lagerIIdAbbuchungslager;
	}

	public Short getBMindermengenzuschlag() {
		return this.bMindermengenzuschlag;
	}

	public void setBMindermengenzuschlag(Short bMindermengenzuschlag) {
		this.bMindermengenzuschlag = bMindermengenzuschlag;
	}

	public Short getBMonatsrechnung() {
		return this.bMonatsrechnung;
	}

	public void setBMonatsrechnung(Short bMonatsrechnung) {
		this.bMonatsrechnung = bMonatsrechnung;
	}

	public Short getBSammelrechnung() {
		return this.bSammelrechnung;
	}

	public void setBSammelrechnung(Short bSammelrechnung) {
		this.bSammelrechnung = bSammelrechnung;
	}

	public Short getBIstreempfaenger() {
		return this.bIstreempfaenger;
	}

	public void setBIstreempfaenger(Short bIstreempfaenger) {
		this.bIstreempfaenger = bIstreempfaenger;
	}

	public Short getBPreiseanlsandrucken() {
		return this.bPreiseanlsandrucken;
	}

	public void setBPreiseanlsandrucken(Short bPreiseanlsandrucken) {
		this.bPreiseanlsandrucken = bPreiseanlsandrucken;
	}

	public Short getBRechnungsdruckmitrabatt() {
		return this.bRechnungsdruckmitrabatt;
	}

	public void setBRechnungsdruckmitrabatt(Short bRechnungsdruckmitrabatt) {
		this.bRechnungsdruckmitrabatt = bRechnungsdruckmitrabatt;
	}

	public Short getBDistributor() {
		return this.bDistributor;
	}

	public void setBDistributor(Short bDistributor) {
		this.bDistributor = bDistributor;
	}

	public Short getBAkzeptiertteillieferung() {
		return this.bAkzeptiertteillieferung;
	}

	public void setBAkzeptiertteillieferung(Short bAkzeptiertteillieferung) {
		this.bAkzeptiertteillieferung = bAkzeptiertteillieferung;
	}

	public Short getBLsgewichtangeben() {
		return this.bLsgewichtangeben;
	}

	public void setBLsgewichtangeben(Short bLsgewichtangeben) {
		this.bLsgewichtangeben = bLsgewichtangeben;
	}

	public Short getBIstinteressent() {
		return this.bIstinteressent;
	}

	public void setBIstinteressent(Short bIstinteressent) {
		this.bIstinteressent = bIstinteressent;
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

	public Double getFRabattsatz() {
		return this.fRabattsatz;
	}

	public void setFRabattsatz(Double fRabattsatz) {
		this.fRabattsatz = fRabattsatz;
	}

	public Integer getIGarantieinmonaten() {
		return this.iGarantieinmonaten;
	}

	public void setIGarantieinmonaten(Integer iGarantieinmonaten) {
		this.iGarantieinmonaten = iGarantieinmonaten;
	}

	public String getXKommentar() {
		return this.xKommentar;
	}

	public void setXKommentar(String xKommentar) {
		this.xKommentar = xKommentar;
	}

	public String getCKurznr() {
		return this.cKurznr;
	}

	public void setCKurznr(String cKurznr) {
		this.cKurznr = cKurznr;
	}

	public BigDecimal getNKreditlimit() {
		return this.nKreditlimit;
	}

	public void setNKreditlimit(BigDecimal nKreditlimit) {
		this.nKreditlimit = nKreditlimit;
	}

	public Date getTBonitaet() {
		return this.tBonitaet;
	}

	public void setTBonitaet(Date tBonitaet) {
		this.tBonitaet = tBonitaet;
	}

	public Date getTLiefersperream() {
		return this.tLiefersperream;
	}

	public void setTLiefersperream(Date tLiefersperream) {
		this.tLiefersperream = tLiefersperream;
	}

	public Integer getIDefaultrekopiendrucken() {
		return this.iDefaultrekopiendrucken;
	}

	public void setIDefaultrekopiendrucken(Integer iDefaultrekopiendrucken) {
		this.iDefaultrekopiendrucken = iDefaultrekopiendrucken;
	}

	public Integer getIDefaultlskopiendrucken() {
		return this.iDefaultlskopiendrucken;
	}

	public void setIDefaultlskopiendrucken(Integer iDefaultlskopiendrucken) {
		this.iDefaultlskopiendrucken = iDefaultlskopiendrucken;
	}

	public Integer getIMitarbeiteranzahl() {
		return this.iMitarbeiteranzahl;
	}

	public void setIMitarbeiteranzahl(Integer iMitarbeiteranzahl) {
		this.iMitarbeiteranzahl = iMitarbeiteranzahl;
	}

	public String getCTour() {
		return this.cTour;
	}

	public void setCTour(String cTour) {
		this.cTour = cTour;
	}

	public String getCLieferantennr() {
		return this.cLieferantennr;
	}

	public void setCLieferantennr(String cLieferantennr) {
		this.cLieferantennr = cLieferantennr;
	}

	public String getCAbc() {
		return this.cAbc;
	}

	public void setCAbc(String cAbc) {
		this.cAbc = cAbc;
	}

	public Date getTAgbuebermittelung() {
		return this.tAgbuebermittelung;
	}

	public void setTAgbuebermittelung(Date tAgbuebermittelung) {
		this.tAgbuebermittelung = tAgbuebermittelung;
	}

	public String getCFremdsystemnr() {
		return this.cFremdsystemnr;
	}

	public void setCFremdsystemnr(String cFremdsystemnr) {
		this.cFremdsystemnr = cFremdsystemnr;
	}

	public String getCHinweisintern() {
		return this.cHinweisintern;
	}

	public void setCHinweisintern(String cHinweisintern) {
		this.cHinweisintern = cHinweisintern;
	}

	public String getCHinweisextern() {
		return this.cHinweisextern;
	}

	public void setCHinweisextern(String cHinweisextern) {
		this.cHinweisextern = cHinweisextern;
	}

	public Double getFZessionsfaktor() {
		return this.fZessionsfaktor;
	}

	public void setFZessionsfaktor(Double fZessionsfaktor) {
		this.fZessionsfaktor = fZessionsfaktor;
	}

	public Short getBVersteckterlieferant() {
		return this.bVersteckterlieferant;
	}

	public void setBVersteckterlieferant(Short bVersteckterlieferant) {
		this.bVersteckterlieferant = bVersteckterlieferant;
	}

	public Short getBReversecharge() {
		return this.bReversecharge;
	}

	public void setBReversecharge(Short bReversecharge) {
		this.bReversecharge = bReversecharge;
	}

	public Integer getIKundennummer() {
		return this.iKundennummer;
	}

	public void setIKundennummer(Integer iKundennummer) {
		this.iKundennummer = iKundennummer;
	}

	public Integer getKontoIIdDebitorenkonto() {
		return this.kontoIIdDebitorenkonto;
	}

	public void setKontoIIdDebitorenkonto(Integer kontoIIdDebitorenkonto) {
		this.kontoIIdDebitorenkonto = kontoIIdDebitorenkonto;
	}

	public Integer getKontoIIdErloesekonto() {
		return this.kontoIIdErloesekonto;
	}

	public void setKontoIIdErloesekonto(Integer kontoIIdErloesekonto) {
		this.kontoIIdErloesekonto = kontoIIdErloesekonto;
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

	public Integer getPartnerIIdRechnungsadresse() {
		return this.partnerIIdRechnungsadresse;
	}

	public void setPartnerIIdRechnungsadresse(Integer partnerIIdRechnungsadresse) {
		this.partnerIIdRechnungsadresse = partnerIIdRechnungsadresse;
	}

	public Integer getPartnerIId() {
		return this.partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	public Integer getPartnerbankIId() {
		return this.partnerbankIId;
	}

	public void setPartnerbankIId(Integer partnerbankIId) {
		this.partnerbankIId = partnerbankIId;
	}

	public Integer getPersonalIIdBekommeprovision() {
		return this.personalIIdBekommeprovision;
	}

	public void setPersonalIIdBekommeprovision(Integer personalIIdBekommeprovision) {
		this.personalIIdBekommeprovision = personalIIdBekommeprovision;
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

	public void setCErwerbsberechtigungsbegruendung(String cErwerbsberechtigungsbegruendung) {
		this.cErwerbsberechtigungsbegruendung = cErwerbsberechtigungsbegruendung;
	}

	public String getCErwerbsberechtigungsbegruendung() {
		return this.cErwerbsberechtigungsbegruendung;
	}

	public Timestamp getTErwerbsberechtigung() {
		return this.tErwerbsberechtigung;
	}

	public void setTErwerbsberechtigung(Timestamp tErwerbsberechtigung) {
		this.tErwerbsberechtigung = tErwerbsberechtigung;
	}

	/**
	 * Die externe ID (Magento-Id)
	 * 
	 * @return die Id des externen Systems
	 */
	public String getCIdExtern() {
		return cIdExtern;
	}

	public void setCIdExtern(String cIdExtern) {
		this.cIdExtern = cIdExtern;
	}

	public Integer getReversechargeartId() {
		return reversechargeartId;
	}

	public void setReversechargeartId(Integer reversechargeartId) {
		this.reversechargeartId = reversechargeartId;
	}

	public void setLaenderartCnr(String laenderartCnr) {
		this.laenderartCnr = laenderartCnr;
	}

	public String getLaenderartCnr() {
		return laenderartCnr;
	}
}

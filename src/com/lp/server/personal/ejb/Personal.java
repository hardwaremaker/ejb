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
package com.lp.server.personal.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.util.Helper;

@NamedQueries({
		@NamedQuery(name = "PersonalfindByPartnerIIdMandantCNr", query = "SELECT OBJECT(C) FROM Personal c WHERE c.partnerIId = ?1 AND c.mandantCNr = ?2"),
		@NamedQuery(name = "PersonalfindByCPersonalnrMandantCNr", query = "SELECT OBJECT(C) FROM Personal c WHERE c.cPersonalnr = ?1 AND c.mandantCNr = ?2"),
		@NamedQuery(name = "PersonalfindByMandantCNrPersonalfunktionCNr", query = "SELECT OBJECT(C) FROM Personal c WHERE c.mandantCNr = ?1 AND c.personalfunktionCNr = ?2"),
		@NamedQuery(name = "PersonalejbSelectNextPersonalnummer", query = "SELECT MAX (o.cPersonalnr) FROM Personal o WHERE o.mandantCNr = ?1"),
		@NamedQuery(name = "PersonalfindByMandantCNr", query = "SELECT OBJECT(C) FROM Personal c WHERE c.mandantCNr = ?1 ORDER BY c.cPersonalnr ASC"),
		@NamedQuery(name = "PersonalfindByMandantCNrPersonalartCNr", query = "SELECT OBJECT(C) FROM Personal c WHERE c.mandantCNr = ?1 AND c.personalartCNr = ?2 ORDER BY c.cPersonalnr ASC"),
		@NamedQuery(name = "PersonalfindByMandantCNrKostenstelleIIdAbteilung", query = "SELECT OBJECT(C) FROM Personal c WHERE c.mandantCNr = ?1 AND c.kostenstelleIIdAbteilung = ?2 ORDER BY c.cPersonalnr ASC"),
		@NamedQuery(name = "PersonalfindByCAusweisSortiertNachIPersonalnr", query = "SELECT OBJECT(C) FROM Personal c WHERE c.cAusweis IS NOT NULL ORDER BY c.cPersonalnr ASC"),
		@NamedQuery(name = "PersonalfindByCAusweisMandantCNrSortiertNachIPersonalnr", query = "SELECT OBJECT(C) FROM Personal c WHERE c.cAusweis IS NOT NULL AND c.mandantCNr = ?1 ORDER BY c.cPersonalnr ASC"),
		@NamedQuery(name = "PersonalfindByCAusweisSortiertNachCAusweis", query = "SELECT OBJECT(C) FROM Personal c WHERE c.cAusweis IS NOT NULL ORDER BY c.cAusweis ASC"),
		@NamedQuery(name = "PersonalfindByCAusweis", query = "SELECT OBJECT(C) FROM Personal c WHERE c.cAusweis = ?1"),
		@NamedQuery(name = "PersonalfindByPersonalgruppeIIdMandantCNr", query = "SELECT OBJECT(C) FROM Personal c WHERE c.personalgruppeIId = ?1 AND c.mandantCNr = ?2  ORDER BY c.cPersonalnr ASC"),
		@NamedQuery(name = "PersonalfindBySozialversichererPartnerIIdMandantCNr", query = "SELECT OBJECT(C) FROM Personal c WHERE c.partnerIIdSozialversicherer = ?1 AND c.mandantCNr = ?2"),
		@NamedQuery(name = "PersonalfindByFirmaPartnerIIdMandantCNr", query = "SELECT OBJECT(C) FROM Personal c WHERE c.partnerIIdFirma = ?1 AND c.mandantCNr = ?2"),
		@NamedQuery(name = Personal.QUERY_ALL_PERSONAL_AUSWEISMANDANTOHNEVERSTECKT, query = "SELECT OBJECT(C) FROM Personal c WHERE c.cAusweis IS NOT NULL AND c.mandantCNr = ?1 and bVersteckt = 0 ORDER BY c.cPersonalnr ASC")
})
@Entity
@Table(name = "PERS_PERSONAL")
public class Personal implements Serializable {

	public static final String QUERY_ALL_PERSONAL_AUSWEISMANDANTOHNEVERSTECKT = "PersonalfindByCAusweisMandantCNrOhneVerstecktSortiertNachIPersonalnr";

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_PERSONALNR")
	private String cPersonalnr;

	@Column(name = "C_AUSWEIS")
	private String cAusweis;

	@Column(name = "B_MAENNLICH")
	private Short bMaennlich;

	@Column(name = "B_UEBERSTUNDENAUSBEZAHLT")
	private Short bUeberstundenausbezahlt;

	@Column(name = "T_GEBURTSDATUM")
	private Timestamp tGeburtsdatum;

	@Column(name = "C_SOZIALVERSNR")
	private String cSozialversnr;

	@Column(name = "B_ANWESENHEITSLISTE")
	private Short bAnwesenheitsliste;

	@Column(name = "C_KURZZEICHEN")
	private String cKurzzeichen;

	@Column(name = "X_KOMMENTAR")
	private String xKommentar;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "C_UNTERSCHRIFTSFUNKTION")
	private String cUnterschriftsfunktion;

	@Column(name = "C_UNTERSCHRIFTSTEXT")
	private String cUnterschriftstext;

	@Column(name = "B_VERSTECKT")
	private Short bVersteckt;

	@Column(name = "KOSTENSTELLE_I_ID_STAMM")
	private Integer kostenstelleIIdStamm;

	@Column(name = "KOSTENSTELLE_I_ID_ABTEILUNG")
	private Integer kostenstelleIIdAbteilung;

	@Column(name = "LAND_I_ID_STAATSANGEHOERIGKEIT")
	private Integer landIIdStaatsangehoerigkeit;

	@Column(name = "LANDPLZORT_I_ID_GEBURT")
	private Integer landplzortIIdGeburt;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "PARTNER_I_ID")
	private Integer partnerIId;

	@Column(name = "PARTNER_I_ID_SOZIALVERSICHERER")
	private Integer partnerIIdSozialversicherer;

	@Column(name = "PARTNER_I_ID_FIRMA")
	private Integer partnerIIdFirma;

	@Column(name = "C_FAX")
	private String cFax;

	@Column(name = "C_TELEFON")
	private String cTelefon;

	@Column(name = "C_HANDY")
	private String cHandy;

	@Column(name = "C_DIREKTFAX")
	private String cDirektfax;

	@Column(name = "C_EMAIL")
	private String cEmail;

	public String getCFax() {
		return cFax;
	}

	public void setCFax(String cFax) {
		this.cFax = cFax;
	}

	public String getCTelefon() {
		return cTelefon;
	}

	public void setCTelefon(String cTelefon) {
		this.cTelefon = cTelefon;
	}

	public String getCDirektfax() {
		return cDirektfax;
	}

	public void setCDirektfax(String cDirektfax) {
		this.cDirektfax = cDirektfax;
	}

	public String getCEmail() {
		return cEmail;
	}

	public void setCEmail(String cEmail) {
		this.cEmail = cEmail;
	}

	public String getCHandy() {
		return cHandy;
	}

	public void setCHandy(String cHandy) {
		this.cHandy = cHandy;
	}

	@Column(name = "BERUF_I_ID")
	private Integer berufIId;

	@Column(name = "FAMILIENSTAND_C_NR")
	private String familienstandCNr;

	@Column(name = "KOLLEKTIV_I_ID")
	private Integer kollektivIId;

	@Column(name = "LOHNGRUPPE_I_ID")
	private Integer lohngruppeIId;

	@Column(name = "PENDLERPAUSCHALE_I_ID")
	private Integer pendlerpauschaleIId;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "PERSONALART_C_NR")
	private String personalartCNr;

	@Column(name = "PERSONALFUNKTION_C_NR")
	private String personalfunktionCNr;

	@Column(name = "RELIGION_I_ID")
	private Integer religionIId;

	@Column(name = "B_ANWESENHEITTERMINAL")
	private Short bAnwesenheitTerminal;

	@Column(name = "B_ANWESENHEITALLETERMINAL")
	private Short bAnwesenheitalleterminal;

	public Short getBAnwesenheitalleterminal() {
		return bAnwesenheitalleterminal;
	}

	public void setBAnwesenheitalleterminal(Short bAnwesenheitalleterminal) {
		this.bAnwesenheitalleterminal = bAnwesenheitalleterminal;
	}

	public String getCImapbenutzer() {
		return cImapbenutzer;
	}

	public void setCImapbenutzer(String cImapbenutzer) {
		this.cImapbenutzer = cImapbenutzer;
	}

	public String getCImapkennwort() {
		return Helper.decode(cImapkennwort);
	}

	public void setCImapkennwort(String cImapkennwort) {
		this.cImapkennwort = Helper.encode(cImapkennwort);
	}

	@Column(name = "PERSONALGRUPPE_I_ID")
	private Integer personalgruppeIId;

	@Column(name = "C_IMAPBENUTZER")
	private String cImapbenutzer;

	@Column(name = "C_IMAPKENNWORT")
	private String cImapkennwort;

	public Integer getPersonalgruppeIId() {
		return personalgruppeIId;
	}

	public void setPersonalgruppeIId(Integer personalgruppeIId) {
		this.personalgruppeIId = personalgruppeIId;
	}

	private static final long serialVersionUID = 1L;


	public Personal() {
		super();
	}

	public Personal(Integer id, Integer partnerIId2, String mandantCNr2,
			String personalnr, String personalartCNr2,
			Integer kostenstelleIIdStamm2) {
		setIId(id);
		setPartnerIId(partnerIId2);
		setMandantCNr(mandantCNr2);
		setCPersonalnr(personalnr);
		setTAendern(new Timestamp(System.currentTimeMillis()));
		setTAnlegen(new Timestamp(System.currentTimeMillis()));
		setBAnwesenheitsliste(new Short((short) 1));
		setBMaennlich(new Short((short) 0));
		setBUeberstundenausbezahlt(new Short((short) 0));
		setBVersteckt(new Short((short) 0));
		setPersonalartCNr(personalartCNr2);
		setKostenstelleIIdStamm(kostenstelleIIdStamm2);
		setBAnwesenheitTerminal(new Short((short) 0));
		setBAnwesenheitalleterminal(new Short((short) 0));
	}

	public Personal(Integer id, Integer partnerIId, String mandantCNr,
			String personalnr, String personalartCNr, Short maennlich,
			Short ueberstundenausbezahlt, Integer kostenstelleIIdStamm,
			Short anwesenheitsliste, Short versteckt,
			Short anwesenheitTerminal, Short anwesenheitAlleTerminal) {
		setIId(id);
		setPartnerIId(partnerIId);
		setMandantCNr(mandantCNr);
		setCPersonalnr(personalnr);
		setTAendern(new Timestamp(System.currentTimeMillis()));
		setTAnlegen(new Timestamp(System.currentTimeMillis()));
		setBAnwesenheitsliste(anwesenheitsliste);
		setBMaennlich(maennlich);
		setBUeberstundenausbezahlt(ueberstundenausbezahlt);
		setBVersteckt(versteckt);
		setPersonalartCNr(personalartCNr);
		setKostenstelleIIdStamm(kostenstelleIIdStamm);
		setBAnwesenheitTerminal(anwesenheitTerminal);
		setBAnwesenheitalleterminal(anwesenheitAlleTerminal);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCPersonalnr() {
		return this.cPersonalnr;
	}

	public void setCPersonalnr(String cPersonalnr) {
		this.cPersonalnr = cPersonalnr;
	}

	public String getCAusweis() {
		return this.cAusweis;
	}

	public void setCAusweis(String cAusweis) {
		this.cAusweis = cAusweis;
	}

	public Short getBMaennlich() {
		return this.bMaennlich;
	}

	public void setBMaennlich(Short bMaennlich) {
		this.bMaennlich = bMaennlich;
	}

	public Short getBUeberstundenausbezahlt() {
		return this.bUeberstundenausbezahlt;
	}

	public void setBUeberstundenausbezahlt(Short bUeberstundenausbezahlt) {
		this.bUeberstundenausbezahlt = bUeberstundenausbezahlt;
	}

	public Timestamp getTGeburtsdatum() {
		return this.tGeburtsdatum;
	}

	public void setTGeburtsdatum(Timestamp tGeburtsdatum) {
		this.tGeburtsdatum = tGeburtsdatum;
	}

	public String getCSozialversnr() {
		return this.cSozialversnr;
	}

	public void setCSozialversnr(String cSozialversnr) {
		this.cSozialversnr = cSozialversnr;
	}

	public Short getBAnwesenheitsliste() {
		return this.bAnwesenheitsliste;
	}

	public void setBAnwesenheitsliste(Short bAnwesenheitsliste) {
		this.bAnwesenheitsliste = bAnwesenheitsliste;
	}

	public String getCKurzzeichen() {
		return this.cKurzzeichen;
	}

	public void setCKurzzeichen(String cKurzzeichen) {
		this.cKurzzeichen = cKurzzeichen;
	}

	public String getXKommentar() {
		return this.xKommentar;
	}

	public void setXKommentar(String xKommentar) {
		this.xKommentar = xKommentar;
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

	public String getCUnterschriftsfunktion() {
		return this.cUnterschriftsfunktion;
	}

	public void setCUnterschriftsfunktion(String cUnterschriftsfunktion) {
		this.cUnterschriftsfunktion = cUnterschriftsfunktion;
	}

	public String getCUnterschriftstext() {
		return this.cUnterschriftstext;
	}

	public void setCUnterschriftstext(String cUnterschriftstext) {
		this.cUnterschriftstext = cUnterschriftstext;
	}

	public Short getBVersteckt() {
		return this.bVersteckt;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	public Integer getKostenstelleIIdStamm() {
		return this.kostenstelleIIdStamm;
	}

	public void setKostenstelleIIdStamm(Integer kostenstelleIIdStamm) {
		this.kostenstelleIIdStamm = kostenstelleIIdStamm;
	}

	public Integer getKostenstelleIIdAbteilung() {
		return this.kostenstelleIIdAbteilung;
	}

	public void setKostenstelleIIdAbteilung(Integer kostenstelleIIdAbteilung) {
		this.kostenstelleIIdAbteilung = kostenstelleIIdAbteilung;
	}

	public Integer getLandIIdStaatsangehoerigkeit() {
		return this.landIIdStaatsangehoerigkeit;
	}

	public void setLandIIdStaatsangehoerigkeit(
			Integer landIIdStaatsangehoerigkeit) {
		this.landIIdStaatsangehoerigkeit = landIIdStaatsangehoerigkeit;
	}

	public Integer getLandplzortIIdGeburt() {
		return this.landplzortIIdGeburt;
	}

	public void setLandplzortIIdGeburt(Integer landplzortIIdGeburt) {
		this.landplzortIIdGeburt = landplzortIIdGeburt;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Integer getPartnerIId() {
		return this.partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	public Integer getPartnerIIdSozialversicherer() {
		return this.partnerIIdSozialversicherer;
	}

	public void setPartnerIIdSozialversicherer(
			Integer partnerIIdSozialversicherer) {
		this.partnerIIdSozialversicherer = partnerIIdSozialversicherer;
	}

	public Integer getPartnerIIdFirma() {
		return this.partnerIIdFirma;
	}

	public void setPartnerIIdFirma(Integer partnerIIdFirma) {
		this.partnerIIdFirma = partnerIIdFirma;
	}

	public Integer getBerufIId() {
		return this.berufIId;
	}

	public void setBerufIId(Integer berufIId) {
		this.berufIId = berufIId;
	}

	public String getFamilienstandCNr() {
		return this.familienstandCNr;
	}

	public void setFamilienstandCNr(String familienstandCNr) {
		this.familienstandCNr = familienstandCNr;
	}

	public Integer getKollektivIId() {
		return this.kollektivIId;
	}

	public void setKollektivIId(Integer kollektivIId) {
		this.kollektivIId = kollektivIId;
	}

	public Integer getLohngruppeIId() {
		return this.lohngruppeIId;
	}

	public void setLohngruppeIId(Integer lohngruppeIId) {
		this.lohngruppeIId = lohngruppeIId;
	}

	public Integer getPendlerpauschaleIId() {
		return this.pendlerpauschaleIId;
	}

	public void setPendlerpauschaleIId(Integer pendlerpauschaleIId) {
		this.pendlerpauschaleIId = pendlerpauschaleIId;
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

	public String getPersonalartCNr() {
		return this.personalartCNr;
	}

	public void setPersonalartCNr(String personalartCNr) {
		this.personalartCNr = personalartCNr;
	}

	public String getPersonalfunktionCNr() {
		return this.personalfunktionCNr;
	}

	public void setPersonalfunktionCNr(String personalfunktionCNr) {
		this.personalfunktionCNr = personalfunktionCNr;
	}

	public Integer getReligionIId() {
		return this.religionIId;
	}

	public void setReligionIId(Integer religionIId) {
		this.religionIId = religionIId;
	}

	public void setBAnwesenheitTerminal(Short bAnwesenheitTerminal) {
		this.bAnwesenheitTerminal = bAnwesenheitTerminal;
	}

	public Short getBAnwesenheitTerminal() {
		return bAnwesenheitTerminal;
	}

}

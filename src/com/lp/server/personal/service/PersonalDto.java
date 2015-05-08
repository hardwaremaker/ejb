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
package com.lp.server.personal.service;

import java.io.Serializable;
import java.sql.Timestamp;

import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerkommunikationDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LandplzortDto;

public class PersonalDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iId;
	private Integer partnerIId;
	private String mandantCNr;
	private KostenstelleDto kostenstelleDto_Abteilung;
	private BerufDto berufDto;
	private KostenstelleDto koststelleDto_Stamm;
	private KollektivDto kollektivDto;
	private Short bVersteckt;
	private PartnerDto partnerDto_Firma;
	private LohngruppeDto lohngruppeDto;
	private PartnerDto partnerDto_Sozialversicherer;
	private String cPersonalnr;
	private ReligionDto religionDto;
	private LandplzortDto landplzortDto_Geburtsort;
	private String personalartCNr;
	private LandDto landDto;
	private PendlerpauschaleDto pendlerpauschaleDto;
	private String personalfunktionCNr;
	private String cAusweis;
	private Short bMaennlich;
	private String familienstandCNr;
	private Integer kollektivIId;
	private Integer berufIId;
	private Integer lohngruppeIId;
	private Integer landIIdStaatsangehoerigkeit;
	private Short bUeberstundenausbezahlt;
	private Integer religionIId;
	private Integer landplzortIIdGeburt;
	private Timestamp tGeburtsdatum;
	private String cSozialversnr;
	private Integer partnerIIdSozialversicherer;
	private Integer partnerIIdFirma;
	private Integer kostenstelleIIdAbteilung;
	private Integer kostenstelleIIdStamm;
	private Float fVerfuegbar;
	private Short bAnwesenheitsliste;
	private String cKurzzeichen;
	private Integer pendlerpauschaleIId;
	private String xKommentar;
	private Timestamp tAnlegen;
	private Integer personalIIdAnlegen;
	private Timestamp tAendern;
	private Integer personalIIdAendern;
	private String cUnterschriftstext;
	private String cUnterschriftsfunktion;
	private Integer personalgruppeIId;
	private String cImapbenutzer;
	private String cImapkennwort;
	private Short bAnwesenheitTerminal;
	private Short bAnwesenheitalleterminal;
	private String cImapInboxFolder ;
	
	public Short getBAnwesenheitalleterminal() {
		return bAnwesenheitalleterminal;
	}

	public void setBAnwesenheitalleterminal(Short bAnwesenheitalleterminal) {
		this.bAnwesenheitalleterminal = bAnwesenheitalleterminal;
	}

	public Integer getPersonalgruppeIId() {
		return personalgruppeIId;
	}

	public void setPersonalgruppeIId(Integer personalgruppeIId) {
		this.personalgruppeIId = personalgruppeIId;
	}

	private Short bTelefonzeitstarten;

	public Short getBTelefonzeitstarten() {
		return bTelefonzeitstarten;
	}

	public void setBTelefonzeitstarten(Short bTelefonzeitstarten) {
		this.bTelefonzeitstarten = bTelefonzeitstarten;
	}

	private PartnerDto partnerDto;
	private String cFax;
	private String cTelefon;
	private String cHandy;
	private String cDirektfax;
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

	public Integer getIId() {
		return iId;
	}

	/**
	 * Formatiere Anrede, Vorname, Nachname
	 * 
	 * @return getAnredeCNr getCName2vornamefirmazeile2
	 *         getCName1nachnamefirmazeile1
	 */
	public String formatAnrede() {
		String ret = "";
		if (getPartnerDto() != null) {
			if (getPartnerDto().getAnredeCNr() != null) {
				ret += getPartnerDto().getAnredeCNr().trim();
			}
			if (getPartnerDto().getCName2vornamefirmazeile2() != null) {
				ret += " "
						+ getPartnerDto().getCName2vornamefirmazeile2().trim();
			}
			if (getPartnerDto().getCName1nachnamefirmazeile1() != null) {
				ret += " "
						+ getPartnerDto().getCName1nachnamefirmazeile1().trim();
			}
		}
		return ret.trim();
	}

	/**
	 * Fuer die Unterschriften in Belegdrucken. Beispiel:
	 * "i.A. Ing. Werner Hehenwarter" - ohne Anrede mit Unterschriftsfunktion
	 * "Einkaufsleiter" - Unterschriftstext
	 * 
	 * @return String
	 */
	public String formatFixUFTitelName2Name1() {
		String ret = "";
		if (getPartnerDto() != null) {
			if (getCUnterschriftsfunktion() != null) {
				ret += getCUnterschriftsfunktion().trim() + " ";
			}
			// if (getPartnerDto().getAnredeCNr() != null) {
			// ret += getPartnerDto().getAnredeCNr().trim();
			// }
			if (getPartnerDto().getCTitel() != null) {
				ret += " " + getPartnerDto().getCTitel().trim();
			}
			if (getPartnerDto().getCName2vornamefirmazeile2() != null) {
				ret += " "
						+ getPartnerDto().getCName2vornamefirmazeile2().trim();
			}
			if (getPartnerDto().getCName1nachnamefirmazeile1() != null) {
				ret += " "
						+ getPartnerDto().getCName1nachnamefirmazeile1().trim();
			}
		}
		return ret.trim();
	}

	public String formatFixName1Name2() {
		String ret = "";
		if (getPartnerDto() != null) {
			if (getPartnerDto().getCName1nachnamefirmazeile1() != null) {
				ret += " "
						+ getPartnerDto().getCName1nachnamefirmazeile1().trim();
			}
			if (getPartnerDto().getCName2vornamefirmazeile2() != null) {
				ret += " "
						+ getPartnerDto().getCName2vornamefirmazeile2().trim();
			}

		}
		return ret.trim();
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getPartnerIId() {
		return partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getCPersonalnr() {
		return cPersonalnr;
	}

	public void setCPersonalnr(String cPersonalnr) {
		this.cPersonalnr = cPersonalnr;
	}

	public String getPersonalartCNr() {
		return personalartCNr;
	}

	public void setPersonalartCNr(String personalartCNr) {
		this.personalartCNr = personalartCNr;
	}

	public String getPersonalfunktionCNr() {
		return personalfunktionCNr;
	}

	public void setPersonalfunktionCNr(String personalfunktionCNr) {
		this.personalfunktionCNr = personalfunktionCNr;
	}

	public String getCAusweis() {
		return cAusweis;
	}

	public void setCAusweis(String cAusweis) {
		this.cAusweis = cAusweis;
	}

	public String getCImapbenutzer() {
		return cImapbenutzer;
	}

	public void setCImapbenutzer(String cImapbenutzer) {
		this.cImapbenutzer = cImapbenutzer;
	}

	public String getCImapkennwort() {
		return cImapkennwort;
	}

	public void setCImapkennwort(String cImapkennwort) {
		this.cImapkennwort = cImapkennwort;
	}

	public Short getBMaennlich() {
		return bMaennlich;
	}

	public void setBMaennlich(Short bMaennlich) {
		this.bMaennlich = bMaennlich;
	}

	public String getFamilienstandCNr() {
		return familienstandCNr;
	}

	public void setFamilienstandCNr(String familienstandCNr) {
		this.familienstandCNr = familienstandCNr;
	}

	public Integer getKollektivIId() {
		return kollektivIId;
	}

	public void setKollektivIId(Integer kollektivIId) {
		this.kollektivIId = kollektivIId;
	}

	public Integer getBerufIId() {
		return berufIId;
	}

	public void setBerufIId(Integer berufIId) {
		this.berufIId = berufIId;
	}

	public Integer getLohngruppeIId() {
		return lohngruppeIId;
	}

	public void setLohngruppeIId(Integer lohngruppeIId) {
		this.lohngruppeIId = lohngruppeIId;
	}

	public Integer getLandIIdStaatsangehoerigkeit() {
		return landIIdStaatsangehoerigkeit;
	}

	public void setLandIIdStaatsangehoerigkeit(
			Integer landIIdStaatsangehoerigkeit) {
		this.landIIdStaatsangehoerigkeit = landIIdStaatsangehoerigkeit;
	}

	public Short getBUeberstundenausbezahlt() {
		return bUeberstundenausbezahlt;
	}

	public void setBUeberstundenausbezahlt(Short bUeberstundenausbezahlt) {
		this.bUeberstundenausbezahlt = bUeberstundenausbezahlt;
	}

	public Integer getReligionIId() {
		return religionIId;
	}

	public void setReligionIId(Integer religionIId) {
		this.religionIId = religionIId;
	}

	public Integer getLandplzortIIdGeburt() {
		return landplzortIIdGeburt;
	}

	public void setLandplzortIIdGeburt(Integer landplzortIIdGeburt) {
		this.landplzortIIdGeburt = landplzortIIdGeburt;
	}

	public Timestamp getTGeburtsdatum() {
		return tGeburtsdatum;
	}

	public void setTGeburtsdatum(Timestamp tGeburtsdatum) {
		this.tGeburtsdatum = tGeburtsdatum;
	}

	public String getCSozialversnr() {
		return cSozialversnr;
	}

	public void setCSozialversnr(String cSozialversnr) {
		this.cSozialversnr = cSozialversnr;
	}

	public Integer getPartnerIIdSozialversicherer() {
		return partnerIIdSozialversicherer;
	}

	public void setPartnerIIdSozialversicherer(
			Integer partnerIIdSozialversicherer) {
		this.partnerIIdSozialversicherer = partnerIIdSozialversicherer;
	}

	public Integer getPartnerIIdFirma() {
		return partnerIIdFirma;
	}

	public void setPartnerIIdFirma(Integer partnerIIdFirma) {
		this.partnerIIdFirma = partnerIIdFirma;
	}

	public Integer getKostenstelleIIdAbteilung() {
		return kostenstelleIIdAbteilung;
	}

	public void setKostenstelleIIdAbteilung(Integer kostenstelleIIdAbteilung) {
		this.kostenstelleIIdAbteilung = kostenstelleIIdAbteilung;
	}

	public Integer getKostenstelleIIdStamm() {
		return kostenstelleIIdStamm;
	}

	public void setKostenstelleIIdStamm(Integer kostenstelleIIdStamm) {
		this.kostenstelleIIdStamm = kostenstelleIIdStamm;
	}

	public Float getFVerfuegbar() {
		return fVerfuegbar;
	}

	public void setFVerfuegbar(Float fVerfuegbar) {
		this.fVerfuegbar = fVerfuegbar;
	}

	public Short getBAnwesenheitsliste() {
		return bAnwesenheitsliste;
	}

	public void setBAnwesenheitsliste(Short bAnwesenheitsliste) {
		this.bAnwesenheitsliste = bAnwesenheitsliste;
	}

	public String getCKurzzeichen() {
		return cKurzzeichen;
	}

	public void setCKurzzeichen(String cKurzzeichen) {
		this.cKurzzeichen = cKurzzeichen;
	}

	public Integer getPendlerpauschaleIId() {
		return pendlerpauschaleIId;
	}

	public void setPendlerpauschaleIId(Integer pendlerpauschaleIId) {
		this.pendlerpauschaleIId = pendlerpauschaleIId;
	}

	public String getXKommentar() {
		return xKommentar;
	}

	public void setXKommentar(String xKommentar) {
		this.xKommentar = xKommentar;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public PartnerDto getPartnerDto() {
		return partnerDto;
	}

	public BerufDto getBerufDto() {
		return berufDto;
	}

	public ReligionDto getReligionDto() {
		return religionDto;
	}

	public LohngruppeDto getLohngruppeDto() {
		return lohngruppeDto;
	}

	public PendlerpauschaleDto getPendlerpauschaleDto() {
		return pendlerpauschaleDto;
	}

	public LandDto getLandDto() {
		return landDto;
	}

	public KollektivDto getKollektivDto() {
		return kollektivDto;
	}

	public PartnerDto getPartnerDto_Sozialversicherer() {
		return partnerDto_Sozialversicherer;
	}

	public PartnerDto getPartnerDto_Firma() {
		return partnerDto_Firma;
	}

	public LandplzortDto getLandplzortDto_Geburtsort() {
		return landplzortDto_Geburtsort;
	}

	public KostenstelleDto getKostenstelleDto_Stamm() {
		return koststelleDto_Stamm;
	}

	public KostenstelleDto getKostenstelleDto_Abteilung() {
		return kostenstelleDto_Abteilung;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public void setPartnerDto(PartnerDto partnerDto) {
		this.partnerDto = partnerDto;
	}

	public void setBerufDto(BerufDto berufDto) {
		this.berufDto = berufDto;
	}

	public void setReligionDto(ReligionDto religionDto) {
		this.religionDto = religionDto;
	}

	public void setLohngruppeDto(LohngruppeDto lohngruppeDto) {
		this.lohngruppeDto = lohngruppeDto;
	}

	public void setPendlerpauschaleDto(PendlerpauschaleDto pendlerpauschaleDto) {
		this.pendlerpauschaleDto = pendlerpauschaleDto;
	}

	public void setLandDto(LandDto landDto) {
		this.landDto = landDto;
	}

	public void setKollektivDto(KollektivDto kollektivDto) {
		this.kollektivDto = kollektivDto;
	}

	public void setPartnerDto_Sozialversicherer(
			PartnerDto partnerDto_Sozialversicherer) {
		this.partnerDto_Sozialversicherer = partnerDto_Sozialversicherer;
	}

	public void setPartnerDto_Firma(PartnerDto partnerDto_Firma) {
		this.partnerDto_Firma = partnerDto_Firma;
	}

	public void setLandplzortDto_Geburtsort(
			LandplzortDto landplzortDto_Geburtsort) {
		this.landplzortDto_Geburtsort = landplzortDto_Geburtsort;
	}

	public void setKostenstelleDto_Stamm(KostenstelleDto kostenstelleDto_Stamm) {
		this.koststelleDto_Stamm = kostenstelleDto_Stamm;
	}

	public void setKostenstelleDto_Abteilung(
			KostenstelleDto kostenstelleDto_Abteilung) {
		this.kostenstelleDto_Abteilung = kostenstelleDto_Abteilung;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PersonalDto)) {
			return false;
		}
		PersonalDto that = (PersonalDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.partnerIId == null ? this.partnerIId == null
				: that.partnerIId.equals(this.partnerIId))) {
			return false;
		}
		if (!(that.mandantCNr == null ? this.mandantCNr == null
				: that.mandantCNr.equals(this.mandantCNr))) {
			return false;
		}
		if (!(that.cPersonalnr == null ? this.cPersonalnr == null
				: that.cPersonalnr.equals(this.cPersonalnr))) {
			return false;
		}
		if (!(that.personalartCNr == null ? this.personalartCNr == null
				: that.personalartCNr.equals(this.personalartCNr))) {
			return false;
		}
		if (!(that.personalfunktionCNr == null ? this.personalfunktionCNr == null
				: that.personalfunktionCNr.equals(this.personalfunktionCNr))) {
			return false;
		}
		if (!(that.cAusweis == null ? this.cAusweis == null : that.cAusweis
				.equals(this.cAusweis))) {
			return false;
		}
		if (!(that.bMaennlich == null ? this.bMaennlich == null
				: that.bMaennlich.equals(this.bMaennlich))) {
			return false;
		}
		if (!(that.familienstandCNr == null ? this.familienstandCNr == null
				: that.familienstandCNr.equals(this.familienstandCNr))) {
			return false;
		}
		if (!(that.kollektivIId == null ? this.kollektivIId == null
				: that.kollektivIId.equals(this.kollektivIId))) {
			return false;
		}
		if (!(that.berufIId == null ? this.berufIId == null : that.berufIId
				.equals(this.berufIId))) {
			return false;
		}
		if (!(that.lohngruppeIId == null ? this.lohngruppeIId == null
				: that.lohngruppeIId.equals(this.lohngruppeIId))) {
			return false;
		}
		if (!(that.landIIdStaatsangehoerigkeit == null ? this.landIIdStaatsangehoerigkeit == null
				: that.landIIdStaatsangehoerigkeit
						.equals(this.landIIdStaatsangehoerigkeit))) {
			return false;
		}
		if (!(that.bUeberstundenausbezahlt == null ? this.bUeberstundenausbezahlt == null
				: that.bUeberstundenausbezahlt
						.equals(this.bUeberstundenausbezahlt))) {
			return false;
		}
		if (!(that.religionIId == null ? this.religionIId == null
				: that.religionIId.equals(this.religionIId))) {
			return false;
		}
		if (!(that.landplzortIIdGeburt == null ? this.landplzortIIdGeburt == null
				: that.landplzortIIdGeburt.equals(this.landplzortIIdGeburt))) {
			return false;
		}
		if (!(that.tGeburtsdatum == null ? this.tGeburtsdatum == null
				: that.tGeburtsdatum.equals(this.tGeburtsdatum))) {
			return false;
		}
		if (!(that.cSozialversnr == null ? this.cSozialversnr == null
				: that.cSozialversnr.equals(this.cSozialversnr))) {
			return false;
		}
		if (!(that.partnerIIdSozialversicherer == null ? this.partnerIIdSozialversicherer == null
				: that.partnerIIdSozialversicherer
						.equals(this.partnerIIdSozialversicherer))) {
			return false;
		}
		if (!(that.partnerIIdFirma == null ? this.partnerIIdFirma == null
				: that.partnerIIdFirma.equals(this.partnerIIdFirma))) {
			return false;
		}
		if (!(that.kostenstelleIIdAbteilung == null ? this.kostenstelleIIdAbteilung == null
				: that.kostenstelleIIdAbteilung
						.equals(this.kostenstelleIIdAbteilung))) {
			return false;
		}
		if (!(that.kostenstelleIIdStamm == null ? this.kostenstelleIIdStamm == null
				: that.kostenstelleIIdStamm.equals(this.kostenstelleIIdStamm))) {
			return false;
		}
		if (!(that.fVerfuegbar == null ? this.fVerfuegbar == null
				: that.fVerfuegbar.equals(this.fVerfuegbar))) {
			return false;
		}
		if (!(that.bAnwesenheitsliste == null ? this.bAnwesenheitsliste == null
				: that.bAnwesenheitsliste.equals(this.bAnwesenheitsliste))) {
			return false;
		}
		if (!(that.cKurzzeichen == null ? this.cKurzzeichen == null
				: that.cKurzzeichen.equals(this.cKurzzeichen))) {
			return false;
		}
		if (!(that.pendlerpauschaleIId == null ? this.pendlerpauschaleIId == null
				: that.pendlerpauschaleIId.equals(this.pendlerpauschaleIId))) {
			return false;
		}
		if (!(that.xKommentar == null ? this.xKommentar == null
				: that.xKommentar.equals(this.xKommentar))) {
			return false;
		}
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen
				.equals(this.tAnlegen))) {
			return false;
		}
		if (!(that.personalIIdAnlegen == null ? this.personalIIdAnlegen == null
				: that.personalIIdAnlegen.equals(this.personalIIdAnlegen))) {
			return false;
		}
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern))) {
			return false;
		}
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern))) {
			return false;
		}
		if (!(that.bAnwesenheitTerminal == null ? this.bAnwesenheitTerminal == null
				: that.bAnwesenheitTerminal.equals(this.bAnwesenheitTerminal))) {
			return false;
		}
		return true;
	}

	public void setCUnterschriftsfunktion(String cUnterschriftsfunktion) {
		this.cUnterschriftsfunktion = cUnterschriftsfunktion;
	}

	public void setCUnterschriftstext(String cUnterschriftstext) {
		this.cUnterschriftstext = cUnterschriftstext;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	public String getCUnterschriftsfunktion() {
		return cUnterschriftsfunktion;
	}

	public String getCUnterschriftstext() {
		return cUnterschriftstext;
	}

	public Short getBVersteckt() {
		return bVersteckt;
	}

	public void setBAnwesenheitTerminal(Short bAnwesenheitTerminal) {
		this.bAnwesenheitTerminal = bAnwesenheitTerminal;
	}

	public Short getBAnwesenheitTerminal() {
		return bAnwesenheitTerminal;
	}

	/**
	 * Der Name des IMap Ordners der als "inbox" fungiert</br>
	 * <p>Kann auch leer sein, dann wird in weiterer Folge der
	 * Default "INBOX" verwendet.</p>
	 * 
	 * @return null oder der Name des Ordners der als Inbox verwendet werden soll
	 */
	public String getCImapInboxFolder() {
		return cImapInboxFolder;
	}

	public void setCImapInboxFolder(String cImapInboxFolder) {
		this.cImapInboxFolder = cImapInboxFolder;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.partnerIId.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.cPersonalnr.hashCode();
		result = 37 * result + this.personalartCNr.hashCode();
		result = 37 * result + this.personalfunktionCNr.hashCode();
		result = 37 * result + this.cAusweis.hashCode();
		result = 37 * result + this.bMaennlich.hashCode();
		result = 37 * result + this.familienstandCNr.hashCode();
		result = 37 * result + this.kollektivIId.hashCode();
		result = 37 * result + this.berufIId.hashCode();
		result = 37 * result + this.lohngruppeIId.hashCode();
		result = 37 * result + this.landIIdStaatsangehoerigkeit.hashCode();
		result = 37 * result + this.bUeberstundenausbezahlt.hashCode();
		result = 37 * result + this.religionIId.hashCode();
		result = 37 * result + this.landplzortIIdGeburt.hashCode();
		result = 37 * result + this.tGeburtsdatum.hashCode();
		result = 37 * result + this.cSozialversnr.hashCode();
		result = 37 * result + this.partnerIIdSozialversicherer.hashCode();
		result = 37 * result + this.partnerIIdFirma.hashCode();
		result = 37 * result + this.kostenstelleIIdAbteilung.hashCode();
		result = 37 * result + this.kostenstelleIIdStamm.hashCode();
		result = 37 * result + this.fVerfuegbar.hashCode();
		result = 37 * result + this.bAnwesenheitsliste.hashCode();
		result = 37 * result + this.cKurzzeichen.hashCode();
		result = 37 * result + this.pendlerpauschaleIId.hashCode();
		result = 37 * result + this.xKommentar.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.bAnwesenheitTerminal.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + partnerIId;
		returnString += ", " + mandantCNr;
		returnString += ", " + cPersonalnr;
		returnString += ", " + personalartCNr;
		returnString += ", " + personalfunktionCNr;
		returnString += ", " + cAusweis;
		returnString += ", " + bMaennlich;
		returnString += ", " + familienstandCNr;
		returnString += ", " + kollektivIId;
		returnString += ", " + berufIId;
		returnString += ", " + lohngruppeIId;
		returnString += ", " + landIIdStaatsangehoerigkeit;
		returnString += ", " + bUeberstundenausbezahlt;
		returnString += ", " + religionIId;
		returnString += ", " + landplzortIIdGeburt;
		returnString += ", " + tGeburtsdatum;
		returnString += ", " + cSozialversnr;
		returnString += ", " + partnerIIdSozialversicherer;
		returnString += ", " + partnerIIdFirma;
		returnString += ", " + kostenstelleIIdAbteilung;
		returnString += ", " + kostenstelleIIdStamm;
		returnString += ", " + fVerfuegbar;
		returnString += ", " + bAnwesenheitsliste;
		returnString += ", " + cKurzzeichen;
		returnString += ", " + pendlerpauschaleIId;
		returnString += ", " + xKommentar;
		returnString += ", " + tAnlegen;
		returnString += ", " + personalIIdAnlegen;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + bAnwesenheitTerminal;
		return returnString;
	}

}

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
package com.lp.server.system.service;

import java.io.Serializable;

import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;

public class ReportMandantDto implements Serializable {

	private static final long serialVersionUID = 4222380484150741478L;

	public String getAktuellenBenutzer() {
		String sAktuellerBenutzer = "";
		if(aktuellerBenutzerAnredeInLocale != null) {
			return aktuellerBenutzerAnredeInLocale;
		}
		if (personalDto_AktuellerBenutzer != null && personalDto_AktuellerBenutzer.getPartnerDto() != null) {
			sAktuellerBenutzer = personalDto_AktuellerBenutzer.formatAnrede();
		}
		return sAktuellerBenutzer;
	}

	private String hauptmandant;

	public String getHauptmandant() {
		return hauptmandant;
	}

	public void setHauptmandant(String hauptmandant) {
		this.hauptmandant = hauptmandant;
	}

	private TheClientDto clientDto;
	private MandantDto mandantDto;
	private PersonalDto personalDto_AktuellerBenutzer;
	private String signatur_AktuellerBenutzer;
	private String zessionstext;
	private boolean bParametermandantArtikelgewichtGrammStattKilo = false;
	// SP9089 Anrede von Benutzer muss uebersetzt werden. Das Dto hat aber keinen
	// Zugriff auf Sprachinfo und die benoetigte Server-Methode, also wird die
	// Anrede zusaetzlich uebergeben, wenn Sprache bekannt
	private String aktuellerBenutzerAnredeInLocale;

	public boolean isBParametermandantArtikelgewichtGrammStattKilo() {
		return bParametermandantArtikelgewichtGrammStattKilo;
	}

	public String getZessionstext() {
		return zessionstext;
	}

	public final static String EMPTY_FIELD_VALUE = "";

	protected ReportMandantDto() {
		// TODO Auto-generated constructor stub
	}

	public ReportMandantDto(TheClientDto theClientDto, MandantDto mandantDto, PersonalDto personalDto_AktuellerBenutzer,
			String signatur_AktuellerBenutzer, String sZessionstext,
			boolean bParametermandantArtikelgewichtGrammStattKilo, String hauptmandant,
			String aktBenutzerAnredeInLocale) {
		if (null == theClientDto)
			throw new IllegalArgumentException("theClientDto == null");
		if (null == mandantDto)
			throw new IllegalArgumentException("mandantDto == null");

		this.clientDto = theClientDto;
		this.mandantDto = mandantDto;
		this.personalDto_AktuellerBenutzer = personalDto_AktuellerBenutzer;
		this.signatur_AktuellerBenutzer = signatur_AktuellerBenutzer;
		this.zessionstext = sZessionstext;
		this.bParametermandantArtikelgewichtGrammStattKilo = bParametermandantArtikelgewichtGrammStattKilo;
		this.hauptmandant = hauptmandant;
		this.aktuellerBenutzerAnredeInLocale = aktBenutzerAnredeInLocale;
	}

	public TheClientDto getTheClientDto() {
		return clientDto;
	}

	public MandantDto getMandantDto() {
		return mandantDto;
	}

	public PartnerDto getPartnerDto() {
		return mandantDto.getPartnerDto();
	}

	public String getMandantCNr() {
		return getReportString(clientDto.getMandant());
	}

	public String getKurzbezeichnung() {
		return getReportString(getMandantDto().getCKbez());
	}

	private String getReportString(String value) {
		return value == null ? EMPTY_FIELD_VALUE : value;
	}

	public String getStrasse() {
		PartnerDto partnerDto = getPartnerDto();
		if (partnerDto == null)
			return EMPTY_FIELD_VALUE;

		return getReportString(partnerDto.getCStrasse());
	}

	public String getOrt() {
		PartnerDto partnerDto = getPartnerDto();
		if (partnerDto == null)
			return "";

		LandplzortDto lpzoDto = partnerDto.getLandplzortDto();
		if (lpzoDto == null)
			return "";

		OrtDto ortDto = lpzoDto.getOrtDto();
		if (ortDto == null)
			return "";

		return getReportString(ortDto.getCName());
	}

	public String getLkz() {
		return isLandDefined() ? getReportString(getPartnerDto().getLandplzortDto().getLandDto().getCLkz())
				: EMPTY_FIELD_VALUE;
	}

	private boolean isPartnerDefined() {
		return getPartnerDto() != null;
	}

	public String getAktuellerBenutzerVorname() {
		if (personalDto_AktuellerBenutzer != null && personalDto_AktuellerBenutzer.getPartnerDto() != null) {
			return personalDto_AktuellerBenutzer.getPartnerDto().getCName2vornamefirmazeile2();
		} else {
			return EMPTY_FIELD_VALUE;
		}
	}

	public String getAktuellerBenutzerNachname() {
		if (personalDto_AktuellerBenutzer != null && personalDto_AktuellerBenutzer.getPartnerDto() != null) {
			return personalDto_AktuellerBenutzer.getPartnerDto().getCName1nachnamefirmazeile1();
		} else {
			return EMPTY_FIELD_VALUE;
		}
	}

	public String getAktuellerBenutzerVorname2() {
		if (personalDto_AktuellerBenutzer != null && personalDto_AktuellerBenutzer.getPartnerDto() != null) {
			return personalDto_AktuellerBenutzer.getPartnerDto().getCName3vorname2abteilung();
		} else {
			return EMPTY_FIELD_VALUE;
		}
	}

	public String getAktuellerBenutzerTitel() {
		if (personalDto_AktuellerBenutzer != null && personalDto_AktuellerBenutzer.getPartnerDto() != null) {
			return personalDto_AktuellerBenutzer.getPartnerDto().getCTitel();
		} else {
			return EMPTY_FIELD_VALUE;
		}
	}

	public String getAktuellerBenutzerNTitel() {
		if (personalDto_AktuellerBenutzer != null && personalDto_AktuellerBenutzer.getPartnerDto() != null) {
			return personalDto_AktuellerBenutzer.getPartnerDto().getCNtitel();
		} else {
			return EMPTY_FIELD_VALUE;
		}
	}

	public String getAktuellerBenutzerUnterschriftsfunktion() {
		if (personalDto_AktuellerBenutzer != null) {
			return personalDto_AktuellerBenutzer.getCUnterschriftsfunktion();
		} else {
			return EMPTY_FIELD_VALUE;
		}
	}

	public String getAktuellerBenutzerUnterschriftstext() {
		if (personalDto_AktuellerBenutzer != null) {
			return personalDto_AktuellerBenutzer.getCUnterschriftstext();
		} else {
			return EMPTY_FIELD_VALUE;
		}
	}

	public String getAktuellerBenutzerEmail() {
		if (personalDto_AktuellerBenutzer != null) {
			return personalDto_AktuellerBenutzer.getCEmail();
		} else {
			return EMPTY_FIELD_VALUE;
		}
	}

	public String getAktuellerBenutzerTelDW() {
		if (personalDto_AktuellerBenutzer != null) {
			return personalDto_AktuellerBenutzer.getCTelefon();
		} else {
			return EMPTY_FIELD_VALUE;
		}
	}

	public String getAktuellerBenutzerHandy() {
		if (personalDto_AktuellerBenutzer != null) {
			return personalDto_AktuellerBenutzer.getCHandy();
		} else {
			return EMPTY_FIELD_VALUE;
		}
	}

	public String getAktuellerBenutzerFaxDW() {
		if (personalDto_AktuellerBenutzer != null) {
			return personalDto_AktuellerBenutzer.getCFax();
		} else {
			return EMPTY_FIELD_VALUE;
		}
	}

	public String getAktuellerBenutzerDirektfax() {
		if (personalDto_AktuellerBenutzer != null) {
			return personalDto_AktuellerBenutzer.getCDirektfax();
		} else {
			return EMPTY_FIELD_VALUE;
		}
	}

	public String getAktuellerBenutzerSignatur() {
		if (signatur_AktuellerBenutzer != null) {
			return signatur_AktuellerBenutzer;
		} else {
			return EMPTY_FIELD_VALUE;
		}
	}

	private boolean isLandplzortDefined() {
		return isPartnerDefined() && getPartnerDto().getLandplzortDto() != null;
	}

	private boolean isLandDefined() {
		return isLandplzortDefined() && getPartnerDto().getLandplzortDto().getLandDto() != null;
	}

	public String getUID() {
		return isPartnerDefined() ? getReportString(getPartnerDto().getCUid()) : EMPTY_FIELD_VALUE;
	}

	public String getName1() {
		return isPartnerDefined() ? getReportString(getPartnerDto().getCName1nachnamefirmazeile1()) : EMPTY_FIELD_VALUE;
	}

	public String getName2() {
		return isPartnerDefined() ? getReportString(getPartnerDto().getCName2vornamefirmazeile2()) : EMPTY_FIELD_VALUE;
	}

	public String getName3() {
		return isPartnerDefined() ? getReportString(getPartnerDto().getCName3vorname2abteilung()) : EMPTY_FIELD_VALUE;
	}

	public String getPLZ() {
		return isLandplzortDefined() ? getReportString(getPartnerDto().getLandplzortDto().getCPlz())
				: EMPTY_FIELD_VALUE;
	}

	public String getTelefon() {
		return isPartnerDefined() ? getReportString(getPartnerDto().getCTelefon()) : EMPTY_FIELD_VALUE;
	}

	public String getFax() {
		return isPartnerDefined() ? getReportString(getPartnerDto().getCFax()) : EMPTY_FIELD_VALUE;
	}

	public String getEmail() {
		return isPartnerDefined() ? getReportString(getPartnerDto().getCEmail()) : EMPTY_FIELD_VALUE;
	}

	public String getHomepage() {
		return isPartnerDefined() ? getReportString(getPartnerDto().getCHomepage()) : EMPTY_FIELD_VALUE;
	}
}

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

import java.sql.Date;
import java.util.Locale;

import com.lp.server.personal.service.PersonalDto;

/**
 * <p>
 * Diese Klasse kuemmert sich um ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 24.08.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/05/23 12:08:39 $
 */
public class MailtextDto implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer mailAnprechpartnerIId = null;
	private Integer mailPartnerIId = null;
	private String mailBelegnummer = null;
	private String mailBezeichnung = null;
	private java.sql.Date mailBelegdatum = null;
	private String mailProjekt = null;
	private String mailBetreff = null;
	private PersonalDto mailVertreter = null;
	private String mailFusstext = null;
	private String mailText = null;
	private String paramModul = null;
	private String paramMandantCNr = null;

	private String rekla_kndreklanr = null;
	private String rekla_kndlsnr = null;

	private Integer projektIId = null;
	private String angebotAnfragenummer = null;
	private Integer belegVersion = null;
	private String rechnungsart = null;
	private String mailKopftext = null;

	public String getRekla_kndreklanr() {
		return rekla_kndreklanr;
	}

	public void setRekla_kndreklanr(String rekla_kndreklanr) {
		this.rekla_kndreklanr = rekla_kndreklanr;
	}

	public String getRekla_kndlsnr() {
		return rekla_kndlsnr;
	}

	public void setRekla_kndlsnr(String rekla_kndlsnr) {
		this.rekla_kndlsnr = rekla_kndlsnr;
	}

	private String rekla_lieferschein = null;
	private String rekla_rechnung = null;
	private String rekla_we_lsnr = null;
	private String rekla_we_datum = null;
	private String rekla_we_lsdatum = null;

	
	private java.sql.Date bisDatum=null;
	
	public java.sql.Date getBisDatum() {
		return bisDatum;
	}

	public void setBisDatum(java.sql.Date bisDatum) {
		this.bisDatum = bisDatum;
	}

	public String getRekla_lieferschein() {
		return rekla_lieferschein;
	}

	public void setRekla_lieferschein(String rekla_lieferschein) {
		this.rekla_lieferschein = rekla_lieferschein;
	}

	public String getRekla_rechnung() {
		return rekla_rechnung;
	}

	public void setRekla_rechnung(String rekla_rechnung) {
		this.rekla_rechnung = rekla_rechnung;
	}

	public String getRekla_we_lsnr() {
		return rekla_we_lsnr;
	}

	public void setRekla_we_lsnr(String rekla_we_lsnr) {
		this.rekla_we_lsnr = rekla_we_lsnr;
	}

	public String getRekla_we_datum() {
		return rekla_we_datum;
	}

	public void setRekla_we_datum(String rekla_we_datum) {
		this.rekla_we_datum = rekla_we_datum;
	}

	public String getRekla_we_lsdatum() {
		return rekla_we_lsdatum;
	}

	public void setRekla_we_lsdatum(String rekla_we_lsdatum) {
		this.rekla_we_lsdatum = rekla_we_lsdatum;
	}

	public String getKundenbestellnummer() {
		return kundenbestellnummer;
	}

	public void setKundenbestellnummer(String kundenbestellnummer) {
		this.kundenbestellnummer = kundenbestellnummer;
	}

	private String person_anrede = null;

	public String getPerson_anrede() {
		return person_anrede;
	}

	public void setPerson_anrede(String person_anrede) {
		this.person_anrede = person_anrede;
	}

	public String getPerson_personalnummer() {
		return person_personalnummer;
	}

	public void setPerson_personalnummer(String person_personalnummer) {
		this.person_personalnummer = person_personalnummer;
	}

	public String getPerson_titel() {
		return person_titel;
	}

	public void setPerson_titel(String person_titel) {
		this.person_titel = person_titel;
	}

	public String getPerson_vorname1() {
		return person_vorname1;
	}

	public void setPerson_vorname1(String person_vorname1) {
		this.person_vorname1 = person_vorname1;
	}

	public String getPerson_vorname2() {
		return person_vorname2;
	}

	public void setPerson_vorname2(String person_vorname2) {
		this.person_vorname2 = person_vorname2;
	}

	public String getPerson_nachname() {
		return person_nachname;
	}

	public void setPerson_nachname(String person_nachname) {
		this.person_nachname = person_nachname;
	}

	public String getPerson_ntitel() {
		return person_ntitel;
	}

	public void setPerson_ntitel(String person_ntitel) {
		this.person_ntitel = person_ntitel;
	}

	private String person_personalnummer = null;
	private String person_titel = null;
	private String person_vorname1 = null;
	private String person_vorname2 = null;
	private String person_nachname = null;
	private String person_ntitel = null;

	private String xlsmailversand_projekt = null;
	public String getXlsmailversand_projekt() {
		return xlsmailversand_projekt;
	}

	public void setXlsmailversand_projekt(String xlsmailversand_projekt) {
		this.xlsmailversand_projekt = xlsmailversand_projekt;
	}

	public String getXlsmailversand_endkunde() {
		return xlsmailversand_endkunde;
	}

	public void setXlsmailversand_endkunde(String xlsmailversand_endkunde) {
		this.xlsmailversand_endkunde = xlsmailversand_endkunde;
	}

	public java.util.Date getXlsmailversand_abgabetermin() {
		return xlsmailversand_abgabetermin;
	}

	public void setXlsmailversand_abgabetermin(java.util.Date xlsmailversand_abgabetermin) {
		this.xlsmailversand_abgabetermin = xlsmailversand_abgabetermin;
	}

	private String xlsmailversand_endkunde = null;
	private java.util.Date xlsmailversand_abgabetermin = null;
	private java.util.Date xlsmailversand_geplanterfertigungstermin = null;

	public java.util.Date getXlsmailversand_geplanterfertigungstermin() {
		return xlsmailversand_geplanterfertigungstermin;
	}

	public void setXlsmailversand_geplanterfertigungstermin(java.util.Date xlsmailversand_geplanterfertigungstermin) {
		this.xlsmailversand_geplanterfertigungstermin = xlsmailversand_geplanterfertigungstermin;
	}

	private String paramXslFile = null;
	private Locale paramLocale = null;
	private String kundenbestellnummer = null;
	private String abnummer = null;
	private Integer anwesenheitsLfdnr;
	
	private String ls_spediteur_name = null;
		public String getLs_spediteur_name() {
		return ls_spediteur_name;
	}

	public void setLs_spediteur_name(String ls_spediteur_name) {
		this.ls_spediteur_name = ls_spediteur_name;
	}

	public String getLs_spediteur_website() {
		return ls_spediteur_website;
	}

	public void setLs_spediteur_website(String ls_spediteur_website) {
		this.ls_spediteur_website = ls_spediteur_website;
	}

		private String ls_spediteur_website = null;
	
	private String ls_versandnummer = null;
	public String getLs_versandnummer() {
		return ls_versandnummer;
	}

	public void setLs_versandnummer(String ls_versandnummer) {
		this.ls_versandnummer = ls_versandnummer;
	}

	public String getLs_versandnummer2() {
		return ls_versandnummer2;
	}

	public void setLs_versandnummer2(String ls_versandnummer2) {
		this.ls_versandnummer2 = ls_versandnummer2;
	}

	private String ls_versandnummer2 = null;
	

	public String getAbnummer() {
		return abnummer;
	}

	public void setAbnummer(String abnummer) {
		this.abnummer = abnummer;
	}

	public PersonalDto getMailVertreter() {
		return mailVertreter;
	}

	public String getMailVertreterAlsString() {
		if (mailVertreter != null && mailVertreter.getPartnerDto() != null) {
			return mailVertreter.formatFixUFTitelName2Name1();
		} else {
			return null;
		}
	}

	public Date getMailBelegdatum() {
		return mailBelegdatum;
	}

	public String getMailBelegnummer() {
		return mailBelegnummer;
	}

	public String getMailBezeichnung() {
		return mailBezeichnung;
	}

	public String getMailFusstext() {
		return mailFusstext;
	}

	public String getMailProjekt() {
		return mailProjekt;
	}

	public String getMailBetreff() {
		return mailBetreff;
	}

	public String getMailText() {
		return mailText;
	}

	public Locale getParamLocale() {
		return paramLocale;
	}

	public String getParamMandantCNr() {
		return paramMandantCNr;
	}

	public String getParamModul() {
		return paramModul;
	}

	public String getParamXslFile() {
		return paramXslFile;
	}

	public Integer getMailAnprechpartnerIId() {
		return mailAnprechpartnerIId;
	}

	public Integer getMailPartnerIId() {
		return mailPartnerIId;
	}

	public void setMailPartnerIId(Integer mailPartnerIId) {
		this.mailPartnerIId = mailPartnerIId;
	}

	public void setMailAnprechpartnerIId(Integer mailAnprechpartnerIId) {
		this.mailAnprechpartnerIId = mailAnprechpartnerIId;
	}

	public void setMailVertreter(PersonalDto mailBearbeiter) {
		this.mailVertreter = mailBearbeiter;
	}

	public void setMailBelegdatum(Date mailBelegdatum) {
		this.mailBelegdatum = mailBelegdatum;
	}

	public void setMailBelegnummer(String mailBelegnummer) {
		this.mailBelegnummer = mailBelegnummer;
	}

	public void setMailBezeichnung(String mailBezeichnung) {
		this.mailBezeichnung = mailBezeichnung;
	}

	public void setMailFusstext(String mailFusstext) {
		this.mailFusstext = mailFusstext;
	}

	public void setMailProjekt(String mailProjekt) {
		this.mailProjekt = mailProjekt;
	}

	public void setMailBetreff(String mailBetreff) {
		this.mailBetreff = mailBetreff;
	}

	public void setMailText(String mailText) {
		this.mailText = mailText;
	}

	public void setParamLocale(Locale paramLocale) {
		this.paramLocale = paramLocale;
	}

	public void setParamMandantCNr(String paramMandantCNr) {
		this.paramMandantCNr = paramMandantCNr;
	}

	public void setParamModul(String paramModul) {
		this.paramModul = paramModul;
	}

	public void setParamXslFile(String paramXslFile) {
		this.paramXslFile = paramXslFile;
	}

	public Integer getProjektIId() {
		return projektIId;
	}

	public void setProjektIId(Integer projektIId) {
		this.projektIId = projektIId;
	}

	public void setAngebotAnfragenummer(String angebotAnfragenummer) {
		this.angebotAnfragenummer = angebotAnfragenummer;
	}

	public String getAngebotAnfragenummer() {
		return angebotAnfragenummer;
	}

	public void setAnwesenheitsLfdnr(Integer iLfdnr) {
		this.anwesenheitsLfdnr = iLfdnr;
	}

	public Integer getAnwesenheitsLfdnr() {
		return anwesenheitsLfdnr;
	}
	
	public Integer getBelegVersion() {
		return belegVersion;
	}
	
	public void setBelegVersion(Integer belegVersion) {
		this.belegVersion = belegVersion;
	}
	
	public void setRechnungsart(String rechnungsart) {
		this.rechnungsart = rechnungsart;
	}
	
	public String getRechnungsart() {
		return rechnungsart;
	}
	
	public void setMailKopftext(String mailKopftext) {
		this.mailKopftext = mailKopftext;
	}
	
	public String getMailKopftext() {
		return mailKopftext;
	}
}

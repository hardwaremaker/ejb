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
package com.lp.server.system.service;

import java.sql.Date;
import java.util.Locale;

import com.lp.server.personal.service.PersonalDto;
import com.lp.util.report.PersonRpt;

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
	private String rekla_we_lsnr= null;
	private String rekla_we_datum= null;
	private String rekla_we_lsdatum= null;

	

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

	private String paramXslFile = null;
	private Locale paramLocale = null;
	private String kundenbestellnummer = null;
	private String abnummer = null;

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
}

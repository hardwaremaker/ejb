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
package com.lp.util.report;

public class PersonRpt  implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sTitel="";
	private String sVorname="";
	private String sNachname="";
	private String sTelefonFirma="";
	private String sTelefonDWFirma="";
	private String sFaxDWFirma="";
	private Integer personalIId=null;
	public Integer getPersonalIId() {
		return personalIId;
	}
	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}
	public String getSFaxDWFirma() {
		return sFaxDWFirma;
	}
	public void setSFaxDWFirma(String faxDWFirma) {
		sFaxDWFirma = faxDWFirma;
	}
	private String sEmail="";
	private String sMobil="";
	private String sTelefonFirmaMitDurchwahlBearbeiter=""; 
	private String sFaxFirmaMitDurchwahlBearbeiter="";
	private String sDirektfax="";
	
	
	public String getSFaxFirmaMitDurchwahlBearbeiter() {
		return sFaxFirmaMitDurchwahlBearbeiter;
	}
	public void setSFaxFirmaMitDurchwahlBearbeiter(String sFaxFirmaMitDurchwahlBearbeiter) {
		this.sFaxFirmaMitDurchwahlBearbeiter = sFaxFirmaMitDurchwahlBearbeiter;
	}
	public String getSDirektfax() {
		return sDirektfax;
	}
	public void setSDirektfax(String direktfax) {
		sDirektfax = direktfax;
	}
	public String getSTelefonFirmaMitDurchwahlBearbeiter() {
		return sTelefonFirmaMitDurchwahlBearbeiter;
	}
	public void setSTelefonFirmaMitDurchwahlBearbeiter(String sTelefonFirmaMitDurchwahlBearbeiter) {
		this.sTelefonFirmaMitDurchwahlBearbeiter = sTelefonFirmaMitDurchwahlBearbeiter;
	}
	public String getSTitel() {
		return sTitel;
	}
	public void setSTitel(String titel) {
		sTitel = titel;
	}
	/**
	 * getSVorname()
	 * @return String Vorname
	 */
	public String getSVorname() {
		return sVorname;
	}
	public void setSVorname(String vorname) {
		sVorname = vorname;
	}
	public String getSNachname() {
		return sNachname;
	}
	public void setSNachname(String nachname) {
		sNachname = nachname;
	}
	public String getSTelefonFirma() {
		return sTelefonFirma;
	}
	public void setSTelefonFirma(String telefonFirma) {
		sTelefonFirma = telefonFirma;
	}
	public String getSTelefonDWFirma() {
		return sTelefonDWFirma;
	}
	public void setSTelefonDWFirma(String telefonDWFirma) {
		sTelefonDWFirma = telefonDWFirma;
	}
	public String getSEmail() {
		return sEmail;
	}
	public void setSEmail(String email) {
		sEmail = email;
	}
	public String getSMobil() {
		return sMobil;
	}
	public void setSMobil(String mobil) {
		sMobil = mobil;
	}
}

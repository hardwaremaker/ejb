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
package com.lp.server.partner.service;

import java.io.Serializable;

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
 * Erstellung: Vorname Nachname; dd.mm.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: valentin $
 *         </p>
 * 
 * @version not attributable Date $Date: 2008/08/07 14:03:07 $
 */
public class PartnerDtoSmall implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String anredeCNr;
	private String cName1nachnamefirmazeile1;
	private String cName2vornamefirmazeile2;
	private String cTitel;
	private Integer iId;

	public PartnerDtoSmall() {
	}

	public String getAnredeCNr() {
		return anredeCNr;
	}

	public String getCName1nachnamefirmazeile1() {
		return cName1nachnamefirmazeile1;
	}

	public String getCName2vornamefirmazeile2() {
		return cName2vornamefirmazeile2;
	}

	public String getCTitel() {
		return cTitel;
	}

	public Integer getIId() {
		return iId;
	}

	public String getCPersonalnummer() {
		return cPersonalnummer;
	}

	public void setcTitel(String cTitel) {
		this.cTitel = cTitel;
	}

	public void setcName2vornamefirmazeile2(String cName2vornamefirmazeile2) {
		this.cName2vornamefirmazeile2 = cName2vornamefirmazeile2;
	}

	public void setcName1nachnamefirmazeile1(String cName1nachnamefirmazeile1) {
		this.cName1nachnamefirmazeile1 = cName1nachnamefirmazeile1;
	}

	public void setAnredeCNr(String anredeCNr) {
		this.anredeCNr = anredeCNr;
	}

	public void setiId(Integer iId) {
		this.iId = iId;
	}

	public void setCPersonalnummer(String cPersonalnummer) {
		this.cPersonalnummer = cPersonalnummer;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.anredeCNr.hashCode();
		result = 37 * result + this.cName1nachnamefirmazeile1.hashCode();
		result = 37 * result + this.cName2vornamefirmazeile2.hashCode();
		result = 37 * result + this.cTitel.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += anredeCNr;
		returnString += ", " + cName1nachnamefirmazeile1;
		returnString += ", " + cName2vornamefirmazeile2;
		returnString += ", " + cTitel;
		return returnString;
	}

	private String cPersonalnummer;

	public String formatFixAnredeTitelName2Name1() {
		String ret = "";
		if (getAnredeCNr() != null) {
			ret += getAnredeCNr().trim();
		}
		if (getCTitel() != null) {
			ret += " " + getCTitel().trim();
		}
		if (getCName2vornamefirmazeile2() != null) {
			ret += " " + getCName2vornamefirmazeile2().trim();
		}
		if (getCName1nachnamefirmazeile1() != null) {
			ret += " " + getCName1nachnamefirmazeile1().trim();
		}
		return ret.trim();
	}

}

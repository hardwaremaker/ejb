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

import java.io.Serializable;
import java.sql.Timestamp;

import com.lp.util.Helper;

public class ParameteranwenderDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cNr;
	private String cWert;
	private String cKategorie;
	private Timestamp tAendern;
	private Integer personalIIdAendern;
	private String cBemerkungsmall;
	private String cBemerkunglarge;
	private String cDatentyp;

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public Object getCWertAsObject() throws Exception {
		Object oRet = null;
		if (cDatentyp.equals("java.util.Locale")) {
			// String muss genau 10 Zeichen haben, damit ihn der Helper zu einem
			// Locale machen kann
			oRet = Helper.string2Locale((cWert + "               ").substring(
					0, 10));
		} else if (cDatentyp.equals("java.lang.Integer")) {
			oRet = new Integer(cWert);
		}
		return oRet;
	}

	public String getCWert() {
		return cWert;
	}

	public void setCWert(String cWert) {
		this.cWert = cWert;
	}

	public String getCKategorie() {
		return cKategorie;
	}

	public void setCKategorie(String cKategorie) {
		this.cKategorie = cKategorie;
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

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public String getCBemerkungsmall() {
		return cBemerkungsmall;
	}

	public void setCBemerkungsmall(String cBemerkungsmall) {
		this.cBemerkungsmall = cBemerkungsmall;
	}

	public String getCBemerkunglarge() {
		return cBemerkunglarge;
	}

	public String getCDatentyp() {
		return cDatentyp;
	}

	public void setCBemerkunglarge(String cBemerkunglarge) {
		this.cBemerkunglarge = cBemerkunglarge;
	}

	public void setCDatentyp(String cDatentyp) {
		this.cDatentyp = cDatentyp;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ParameteranwenderDto)) {
			return false;
		}
		ParameteranwenderDto that = (ParameteranwenderDto) obj;
		if (!(that.cNr == null ? this.cNr == null : that.cNr.equals(this.cNr))) {
			return false;
		}
		if (!(that.cWert == null ? this.cWert == null : that.cWert
				.equals(this.cWert))) {
			return false;
		}
		if (!(that.cKategorie == null ? this.cKategorie == null
				: that.cKategorie.equals(this.cKategorie))) {
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
		if (!(that.cBemerkungsmall == null ? this.cBemerkungsmall == null
				: that.cBemerkungsmall.equals(this.cBemerkungsmall))) {
			return false;
		}
		if (!(that.cBemerkunglarge == null ? this.cBemerkunglarge == null
				: that.cBemerkunglarge.equals(this.cBemerkunglarge))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.cNr.hashCode();
		result = 37 * result + this.cWert.hashCode();
		result = 37 * result + this.cKategorie.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.cBemerkungsmall.hashCode();
		result = 37 * result + this.cBemerkunglarge.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += cNr;
		returnString += ", " + cWert;
		returnString += ", " + cKategorie;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + cBemerkungsmall;
		returnString += ", " + cBemerkunglarge;
		return returnString;
	}
}

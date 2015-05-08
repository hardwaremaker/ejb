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
package com.lp.server.finanz.service;

import java.io.Serializable;
import java.sql.Timestamp;

public class ErgebnisgruppeDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String mandantCNr;
	private String cBez;
	private Integer ergebnisgruppeIIdSumme;
	private Integer iReihung;
	private Short bSummeNegativ;
	private Short bInvertiert;
	private Timestamp tAnlegen;
	private Integer personalIIdAnlegen;
	private Timestamp tAendern;
	private Integer personalIIdAendern;
	private Short bProzentbasis;
	private Integer iTyp;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public Integer getErgebnisgruppeIIdSumme() {
		return ergebnisgruppeIIdSumme;
	}

	public void setErgebnisgruppeIIdSumme(Integer ergebnisgruppeIIdSumme) {
		this.ergebnisgruppeIIdSumme = ergebnisgruppeIIdSumme;
	}

	public Integer getIReihung() {
		return iReihung;
	}

	public void setIReihung(Integer iReihung) {
		this.iReihung = iReihung;
	}

	public Short getBSummeNegativ() {
		return bSummeNegativ;
	}

	public void setBSummeNegativ(Short bSummeNegativ) {
		this.bSummeNegativ = bSummeNegativ;
	}

	public Short getBInvertiert() {
		return bInvertiert;
	}

	public void setBInvertiert(Short bInvertiert) {
		this.bInvertiert = bInvertiert;
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

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Short getBProzentbasis() {
		return bProzentbasis;
	}

	public void setBProzentbasis(Short bProzentbasis) {
		this.bProzentbasis = bProzentbasis;
	}

	public Integer getITyp() {
		return iTyp;
	}

	public void setITyp(Integer iTyp) {
		this.iTyp = iTyp;
	}

	private Short bBilanzgruppe;

	public Short getBBilanzgruppe() {
		return bBilanzgruppe;
	}

	public void setBBilanzgruppe(Short bBilanzgruppe) {
		this.bBilanzgruppe = bBilanzgruppe;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ErgebnisgruppeDto)) {
			return false;
		}
		ErgebnisgruppeDto that = (ErgebnisgruppeDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.mandantCNr == null ? this.mandantCNr == null
				: that.mandantCNr.equals(this.mandantCNr))) {
			return false;
		}
		if (!(that.cBez == null ? this.cBez == null : that.cBez
				.equals(this.cBez))) {
			return false;
		}
		if (!(that.ergebnisgruppeIIdSumme == null ? this.ergebnisgruppeIIdSumme == null
				: that.ergebnisgruppeIIdSumme
						.equals(this.ergebnisgruppeIIdSumme))) {
			return false;
		}
		if (!(that.iReihung == null ? this.iReihung == null : that.iReihung
				.equals(this.iReihung))) {
			return false;
		}
		if (!(that.bSummeNegativ == null ? this.bSummeNegativ == null
				: that.bSummeNegativ.equals(this.bSummeNegativ))) {
			return false;
		}
		if (!(that.bInvertiert == null ? this.bInvertiert == null
				: that.bInvertiert.equals(this.bInvertiert))) {
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
		if (!(that.bProzentbasis == null ? this.bProzentbasis == null
				: that.bProzentbasis.equals(this.bProzentbasis))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.cBez.hashCode();
		result = 37 * result + this.ergebnisgruppeIIdSumme.hashCode();
		result = 37 * result + this.iReihung.hashCode();
		result = 37 * result + this.bSummeNegativ.hashCode();
		result = 37 * result + this.bInvertiert.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.bProzentbasis.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + mandantCNr;
		returnString += ", " + cBez;
		returnString += ", " + ergebnisgruppeIIdSumme;
		returnString += ", " + iReihung;
		returnString += ", " + bSummeNegativ;
		returnString += ", " + bInvertiert;
		returnString += ", " + tAnlegen;
		returnString += ", " + personalIIdAnlegen;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + bProzentbasis;
		return returnString;
	}
}

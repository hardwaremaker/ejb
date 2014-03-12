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
package com.lp.server.benutzer.service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;

import com.lp.util.Helper;

public class BenutzerDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String cBenutzerkennung;
	private char[] cKennwort;
	private Short bAendern;
	private Short bGesperrt;
	private Timestamp tGueltigbis;
	private Integer iFehlversuchegemacht;
	private String mandantCNrDefault;
	private Timestamp tAnlegen;
	private Integer personalIIdAnlegen;
	private Timestamp tAendern;
	private Integer personalIIdAendern;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCBenutzerkennung() {
		return cBenutzerkennung;
	}

	public void setCBenutzerkennung(String cBenutzerkennung) {
		this.cBenutzerkennung = cBenutzerkennung;
	}

	public char[] getCKennwort() {
		return cKennwort;
	}

	public void setCKennwort(char[] cKennwort) {
		this.cKennwort = cKennwort;
	}

	public Short getBAendern() {
		return bAendern;
	}

	public void setBAendern(Short bAendern) {
		this.bAendern = bAendern;
	}

	public Short getBGesperrt() {
		return bGesperrt;
	}

	public void setBGesperrt(Short bGesperrt) {
		this.bGesperrt = bGesperrt;
	}

	public Timestamp getTGueltigbis() {
		return tGueltigbis;
	}

	public void setTGueltigbis(Timestamp tGueltigbis) {
		this.tGueltigbis = Helper.cutTimestamp(tGueltigbis);
	}

	public Integer getIFehlversuchegemacht() {
		return iFehlversuchegemacht;
	}

	public void setIFehlversuchegemacht(Integer iFehlversuchegemacht) {
		this.iFehlversuchegemacht = iFehlversuchegemacht;
	}

	public String getMandantCNrDefault() {
		return mandantCNrDefault;
	}

	public void setMandantCNrDefault(String mandantCNrDefault) {
		this.mandantCNrDefault = mandantCNrDefault;
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

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BenutzerDto)) {
			return false;
		}
		BenutzerDto that = (BenutzerDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.cBenutzerkennung == null ? this.cBenutzerkennung == null
				: that.cBenutzerkennung.equals(this.cBenutzerkennung))) {
			return false;
		}
		if (!(that.cKennwort == null ? this.cKennwort == null :
			Arrays.equals(that.cKennwort, this.cKennwort))) {
			return false;
		}
		if (!(that.bAendern == null ? this.bAendern == null : that.bAendern
				.equals(this.bAendern))) {
			return false;
		}
		if (!(that.bGesperrt == null ? this.bGesperrt == null : that.bGesperrt
				.equals(this.bGesperrt))) {
			return false;
		}
		if (!(that.tGueltigbis == null ? this.tGueltigbis == null
				: that.tGueltigbis.equals(this.tGueltigbis))) {
			return false;
		}
		if (!(that.iFehlversuchegemacht == null ? this.iFehlversuchegemacht == null
				: that.iFehlversuchegemacht.equals(this.iFehlversuchegemacht))) {
			return false;
		}
		if (!(that.mandantCNrDefault == null ? this.mandantCNrDefault == null
				: that.mandantCNrDefault.equals(this.mandantCNrDefault))) {
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
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.cBenutzerkennung.hashCode();
		result = 37 * result + Arrays.hashCode(this.cKennwort);
		result = 37 * result + this.bAendern.hashCode();
		result = 37 * result + this.bGesperrt.hashCode();
		result = 37 * result + this.tGueltigbis.hashCode();
		result = 37 * result + this.iFehlversuchegemacht.hashCode();
		result = 37 * result + this.mandantCNrDefault.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + cBenutzerkennung;
		returnString += ", " + new String(cKennwort);
		returnString += ", " + bAendern;
		returnString += ", " + bGesperrt;
		returnString += ", " + tGueltigbis;
		returnString += ", " + iFehlversuchegemacht;
		returnString += ", " + mandantCNrDefault;
		returnString += ", " + tAnlegen;
		returnString += ", " + personalIIdAnlegen;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdAendern;
		return returnString;
	}
}

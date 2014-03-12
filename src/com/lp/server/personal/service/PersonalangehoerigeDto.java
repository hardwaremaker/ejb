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
package com.lp.server.personal.service;

import java.io.Serializable;
import java.sql.Timestamp;

public class PersonalangehoerigeDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iId;
	private Integer personalIId;
	private String angehoerigenartCNr;
	private String cVorname;
	private String cName;
	private Timestamp tGeburtsdatum;
	private String cSozialversnr;

	public Integer getPersonalIId() {
		return personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

	public String getAngehoerigenartCNr() {
		return angehoerigenartCNr;
	}

	public void setAngehoerigenartCNr(String angehoerigenartCNr) {
		this.angehoerigenartCNr = angehoerigenartCNr;
	}

	public String getCVorname() {
		return cVorname;
	}

	public void setCVorname(String cVorname) {
		this.cVorname = cVorname;
	}

	public String getCName() {
		return cName;
	}

	public void setCName(String cName) {
		this.cName = cName;
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

	public Integer getIId() {
		return iId;
	}

	public void setCSozialversnr(String cSozialversnr) {
		this.cSozialversnr = cSozialversnr;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PersonalangehoerigeDto)) {
			return false;
		}
		PersonalangehoerigeDto that = (PersonalangehoerigeDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.personalIId == null ? this.personalIId == null
				: that.personalIId.equals(this.personalIId))) {
			return false;
		}
		if (!(that.angehoerigenartCNr == null ? this.angehoerigenartCNr == null
				: that.angehoerigenartCNr.equals(this.angehoerigenartCNr))) {
			return false;
		}
		if (!(that.cVorname == null ? this.cVorname == null : that.cVorname
				.equals(this.cVorname))) {
			return false;
		}
		if (!(that.cName == null ? this.cName == null : that.cName
				.equals(this.cName))) {
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
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.personalIId.hashCode();
		result = 37 * result + this.angehoerigenartCNr.hashCode();
		result = 37 * result + this.cVorname.hashCode();
		result = 37 * result + this.cName.hashCode();
		result = 37 * result + this.tGeburtsdatum.hashCode();
		result = 37 * result + this.cSozialversnr.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + personalIId;
		returnString += ", " + angehoerigenartCNr;
		returnString += ", " + cVorname;
		returnString += ", " + cName;
		returnString += ", " + tGeburtsdatum;
		returnString += ", " + cSozialversnr;
		return returnString;
	}
}

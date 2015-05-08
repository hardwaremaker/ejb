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
import java.sql.Time;
import java.sql.Timestamp;

public class ZeitmodelltagpauseDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer zeitmodelltagIId;
	private Time uBeginn;
	private Time uEnde;
	private Integer personalIIdAendern;
	private Timestamp tAendern;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getZeitmodelltagIId() {
		return zeitmodelltagIId;
	}

	public void setZeitmodelltagIId(Integer zeitmodelltagIId) {
		this.zeitmodelltagIId = zeitmodelltagIId;
	}

	public Time getUBeginn() {
		return uBeginn;
	}

	public void setUBeginn(Time uBeginn) {
		this.uBeginn = uBeginn;
	}

	public Time getUEnde() {
		return uEnde;
	}

	public void setUEnde(Time uEnde) {
		this.uEnde = uEnde;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ZeitmodelltagpauseDto))
			return false;
		ZeitmodelltagpauseDto that = (ZeitmodelltagpauseDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.zeitmodelltagIId == null ? this.zeitmodelltagIId == null
				: that.zeitmodelltagIId.equals(this.zeitmodelltagIId)))
			return false;
		if (!(that.uBeginn == null ? this.uBeginn == null : that.uBeginn
				.equals(this.uBeginn)))
			return false;
		if (!(that.uEnde == null ? this.uEnde == null : that.uEnde
				.equals(this.uEnde)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.zeitmodelltagIId.hashCode();
		result = 37 * result + this.uBeginn.hashCode();
		result = 37 * result + this.uEnde.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + zeitmodelltagIId;
		returnString += ", " + uBeginn;
		returnString += ", " + uEnde;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + tAendern;
		return returnString;
	}
}

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
package com.lp.server.artikel.service;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;

public class LagerplatzDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String cLagerplatz;
	private Integer iMaxmenge;
	private Integer lagerIId;
	private Timestamp tAendern;
	private Integer personalIIdAendern;
	private Integer paternosterIId;
	

	public Integer getPaternosterIId() {
		return paternosterIId;
	}

	public void setPaternosterIId(Integer paternosterIId) {
		this.paternosterIId = paternosterIId;
	}

	public Integer getIId() {
		return iId;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp aendern) {
		tAendern = aendern;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCLagerplatz() {
		return cLagerplatz;
	}

	public void setCLagerplatz(String cLagerplatz) {
		this.cLagerplatz = cLagerplatz;
	}

	public Integer getIMaxmenge() {
		return iMaxmenge;
	}

	public void setIMaxmenge(Integer iMaxmenge) {
		this.iMaxmenge = iMaxmenge;
	}

	public Integer getLagerIId() {
		return lagerIId;
	}

	public void setLagerIId(Integer lagerIId) {
		this.lagerIId = lagerIId;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof LagerplatzDto))
			return false;
		LagerplatzDto that = (LagerplatzDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.cLagerplatz == null ? this.cLagerplatz == null
				: that.cLagerplatz.equals(this.cLagerplatz)))
			return false;
		if (!(that.iMaxmenge == null ? this.iMaxmenge == null : that.iMaxmenge
				.equals(this.iMaxmenge)))
			return false;
		if (!(that.lagerIId == null ? this.lagerIId == null : that.lagerIId
				.equals(this.lagerIId)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.cLagerplatz.hashCode();
		result = 37 * result + this.iMaxmenge.hashCode();
		result = 37 * result + this.lagerIId.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + cLagerplatz;
		returnString += ", " + iMaxmenge;
		returnString += ", " + lagerIId;
		return returnString;
	}
}

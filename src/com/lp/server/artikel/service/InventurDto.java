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

public class InventurDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Timestamp tInventurdatum;
	private String cBez;
	private Timestamp tAendern;
	private Integer personalIIdAendern;

	public Integer getIId() {
		return iId;
	}

	private Timestamp tAbwertungdurchgefuehrt;
	private Timestamp tInventurdurchgefuehrt;
	private Short bInventurdurchgefuehrt;
	private Short bAbwertungdurchgefuehrt;
	private Integer personalIIdAbwertungdurchgefuehrt;
	private Integer personalIIdInventurdurchgefuehrt;
	private String mandantCNr;
	private Integer lagerIId;

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Timestamp getTInventurdatum() {
		return com.lp.util.Helper.cutTimestamp(tInventurdatum);
	}

	public void setTInventurdatum(Timestamp tInventurdatum) {
		this.tInventurdatum = tInventurdatum;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
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

	public Integer getPersonalIIdAbwertungdurchgefuehrt() {
		return personalIIdAbwertungdurchgefuehrt;
	}

	public Integer getPersonalIIdInventurdurchgefuehrt() {
		return personalIIdInventurdurchgefuehrt;
	}

	public Short getBInventurdurchgefuehrt() {
		return bInventurdurchgefuehrt;
	}

	public Short getBAbwertungdurchgefuehrt() {
		return bAbwertungdurchgefuehrt;
	}

	public Timestamp getTInventurdurchgefuehrt() {
		return tInventurdurchgefuehrt;
	}

	public Timestamp getTAbwertungdurchgefuehrt() {
		return tAbwertungdurchgefuehrt;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public Integer getLagerIId() {
		return lagerIId;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public void setPersonalIIdInventurdurchgefuehrt(
			Integer personalIIdInventurdurchgefuehrt) {
		this.personalIIdInventurdurchgefuehrt = personalIIdInventurdurchgefuehrt;
	}

	public void setPersonalIIdAbwertungdurchgefuehrt(
			Integer personalIIdAbwertungdurchgefuehrt) {
		this.personalIIdAbwertungdurchgefuehrt = personalIIdAbwertungdurchgefuehrt;
	}

	public void setBInventurdurchgefuehrt(Short bInventurdurchgefuehrt) {
		this.bInventurdurchgefuehrt = bInventurdurchgefuehrt;
	}

	public void setBAbwertungdurchgefuehrt(Short bAbwertungdurchgefuehrt) {
		this.bAbwertungdurchgefuehrt = bAbwertungdurchgefuehrt;
	}

	public void setTInventurdurchgefuehrt(Timestamp tInventurdurchgefuehrt) {
		this.tInventurdurchgefuehrt = tInventurdurchgefuehrt;
	}

	public void setTAbwertungdurchgefuehrt(Timestamp tAbwertungdurchgefuehrt) {
		this.tAbwertungdurchgefuehrt = tAbwertungdurchgefuehrt;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public void setLagerIId(Integer lagerIId) {
		this.lagerIId = lagerIId;
	}

	private Short bNichtinventierteartikelabbuchen;

	public Short getBNichtinventierteartikelabbuchen() {
		return bNichtinventierteartikelabbuchen;
	}

	public void setBNichtinventierteartikelabbuchen(
			Short bNichtinventierteartikelabbuchen) {
		this.bNichtinventierteartikelabbuchen = bNichtinventierteartikelabbuchen;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof InventurDto))
			return false;
		InventurDto that = (InventurDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.tInventurdatum == null ? this.tInventurdatum == null
				: that.tInventurdatum.equals(this.tInventurdatum)))
			return false;
		if (!(that.cBez == null ? this.cBez == null : that.cBez
				.equals(this.cBez)))
			return false;
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.tInventurdatum.hashCode();
		result = 37 * result + this.cBez.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + tInventurdatum;
		returnString += ", " + cBez;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdAendern;
		return returnString;
	}
}

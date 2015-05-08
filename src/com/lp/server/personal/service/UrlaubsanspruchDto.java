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
import java.sql.Timestamp;

public class UrlaubsanspruchDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iId;
	private Integer personalIId;
	private Integer iJahr;
	private Double fTage;
	private Double fStunden;
	private Integer personalIIdAendern;
	private Timestamp tAendern;
	private Double fTagezusaetzlich;

	public Double getFJahresurlaubinwochen() {
		return fJahresurlaubinwochen;
	}

	public void setFJahresurlaubinwochen(Double fJahresurlaubinwochen) {
		this.fJahresurlaubinwochen = fJahresurlaubinwochen;
	}
	private Double fJahresurlaubinwochen;
	
	
	public Double getFResturlaubjahresendestunden() {
		return fResturlaubjahresendestunden;
	}

	public void setFResturlaubjahresendestunden(Double resturlaubjahresendestunden) {
		fResturlaubjahresendestunden = resturlaubjahresendestunden;
	}

	public Double getFResturlaubjahresendetage() {
		return fResturlaubjahresendetage;
	}

	public void setFResturlaubjahresendetage(Double resturlaubjahresendetage) {
		fResturlaubjahresendetage = resturlaubjahresendetage;
	}

	public Short getBGesperrt() {
		return bGesperrt;
	}

	public void setBGesperrt(Short gesperrt) {
		bGesperrt = gesperrt;
	}

	private Double fResturlaubjahresendestunden;
	private Double fResturlaubjahresendetage;
	private Short bGesperrt;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	private Double fStundenzusaetzlich;

	public Integer getPersonalIId() {
		return personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

	public Integer getIJahr() {
		return iJahr;
	}

	public void setIJahr(Integer iJahr) {
		this.iJahr = iJahr;
	}

	public Double getFTage() {
		return fTage;
	}

	public void setFTage(Double fTage) {
		this.fTage = fTage;
	}

	public Double getFStunden() {
		return fStunden;
	}

	public void setFStunden(Double fStunden) {
		this.fStunden = fStunden;
	}

	public Integer getPersonaIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public Double getFTagezusaetzlich() {
		return fTagezusaetzlich;
	}

	public Double getFStundenzusaetzlich() {
		return fStundenzusaetzlich;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public void setFTagezusaetzlich(Double fTagezusaetzlich) {
		this.fTagezusaetzlich = fTagezusaetzlich;
	}

	public void setFStundenzusaetzlich(Double fStundenzusaetzlich) {
		this.fStundenzusaetzlich = fStundenzusaetzlich;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof UrlaubsanspruchDto))
			return false;
		UrlaubsanspruchDto that = (UrlaubsanspruchDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.personalIId == null ? this.personalIId == null
				: that.personalIId.equals(this.personalIId)))
			return false;
		if (!(that.iJahr == null ? this.iJahr == null : that.iJahr
				.equals(this.iJahr)))
			return false;
		if (!(that.fTage == null ? this.fTage == null : that.fTage
				.equals(this.fTage)))
			return false;
		if (!(that.fStunden == null ? this.fStunden == null : that.fStunden
				.equals(this.fStunden)))
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
		result = 37 * result + this.personalIId.hashCode();
		result = 37 * result + this.iJahr.hashCode();
		result = 37 * result + this.fTage.hashCode();
		result = 37 * result + this.fStunden.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + personalIId;
		returnString += ", " + iJahr;
		returnString += ", " + fTage;
		returnString += ", " + fStunden;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + tAendern;
		return returnString;
	}
}

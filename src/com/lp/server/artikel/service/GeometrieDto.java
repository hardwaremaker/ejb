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
package com.lp.server.artikel.service;

import java.io.Serializable;

public class GeometrieDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer artikelIId;
	private Double fBreite;
	private String cBreitetext;
	private Double fHoehe;
	private Double fTiefe;

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Double getFBreite() {
		return fBreite;
	}

	public void setFBreite(Double fBreite) {
		this.fBreite = fBreite;
	}

	public String getCBreitetext() {
		return cBreitetext;
	}

	public void setCBreitetext(String cBreitetext) {
		this.cBreitetext = cBreitetext;
	}

	public Double getFHoehe() {
		return fHoehe;
	}

	public void setFHoehe(Double fHoehe) {
		this.fHoehe = fHoehe;
	}

	public Double getFTiefe() {
		return fTiefe;
	}

	public void setFTiefe(Double fTiefe) {
		this.fTiefe = fTiefe;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof GeometrieDto)) {
			return false;
		}
		GeometrieDto that = (GeometrieDto) obj;
		if (!(that.artikelIId == null ? this.artikelIId == null
				: that.artikelIId.equals(this.artikelIId))) {
			return false;
		}
		if (!(that.fBreite == null ? this.fBreite == null : that.fBreite
				.equals(this.fBreite))) {
			return false;
		}
		if (!(that.cBreitetext == null ? this.cBreitetext == null
				: that.cBreitetext.equals(this.cBreitetext))) {
			return false;
		}
		if (!(that.fHoehe == null ? this.fHoehe == null : that.fHoehe
				.equals(this.fHoehe))) {
			return false;
		}
		if (!(that.fTiefe == null ? this.fTiefe == null : that.fTiefe
				.equals(this.fTiefe))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.artikelIId.hashCode();
		result = 37 * result + this.fBreite.hashCode();
		result = 37 * result + this.cBreitetext.hashCode();
		result = 37 * result + this.fHoehe.hashCode();
		result = 37 * result + this.fTiefe.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += artikelIId;
		returnString += ", " + fBreite;
		returnString += ", " + cBreitetext;
		returnString += ", " + fHoehe;
		returnString += ", " + fTiefe;
		return returnString;
	}
}

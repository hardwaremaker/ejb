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

public class SollverkaufDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer artikelIId;
	private Double fAufschlag;
	private Double fSollverkauf;

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Double getFAufschlag() {
		return fAufschlag;
	}

	public void setFAufschlag(Double fAufschlag) {
		this.fAufschlag = fAufschlag;
	}

	public Double getFSollverkauf() {
		return fSollverkauf;
	}

	public void setFSollverkauf(Double fSollverkauf) {
		this.fSollverkauf = fSollverkauf;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof SollverkaufDto)) {
			return false;
		}
		SollverkaufDto that = (SollverkaufDto) obj;
		if (!(that.artikelIId == null ? this.artikelIId == null
				: that.artikelIId.equals(this.artikelIId))) {
			return false;
		}
		if (!(that.fAufschlag == null ? this.fAufschlag == null
				: that.fAufschlag.equals(this.fAufschlag))) {
			return false;
		}
		if (!(that.fSollverkauf == null ? this.fSollverkauf == null
				: that.fSollverkauf.equals(this.fSollverkauf))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.artikelIId.hashCode();
		result = 37 * result + this.fAufschlag.hashCode();
		result = 37 * result + this.fSollverkauf.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += artikelIId;
		returnString += ", " + fAufschlag;
		returnString += ", " + fSollverkauf;
		return returnString;
	}
}

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

public class MontageDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer artikelIId;
	private Double fRasterliegend;
	private Double fRasterstehend;
	private Short bHochstellen;
	private Short bHochsetzen;
	private Short bPolarisiert;

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Double getFRasterliegend() {
		return fRasterliegend;
	}

	public void setFRasterliegend(Double fRasterliegend) {
		this.fRasterliegend = fRasterliegend;
	}

	public Double getFRasterstehend() {
		return fRasterstehend;
	}

	public void setFRasterstehend(Double fRasterstehend) {
		this.fRasterstehend = fRasterstehend;
	}

	public Short getBHochstellen() {
		return bHochstellen;
	}

	public void setBHochstellen(Short bHochstellen) {
		this.bHochstellen = bHochstellen;
	}

	public Short getBHochsetzen() {
		return bHochsetzen;
	}

	public void setBHochsetzen(Short bHochsetzen) {
		this.bHochsetzen = bHochsetzen;
	}

	public Short getBPolarisiert() {
		return bPolarisiert;
	}

	public void setBPolarisiert(Short bPolarisiert) {
		this.bPolarisiert = bPolarisiert;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof MontageDto)) {
			return false;
		}
		MontageDto that = (MontageDto) obj;
		if (!(that.artikelIId == null ? this.artikelIId == null
				: that.artikelIId.equals(this.artikelIId))) {
			return false;
		}
		if (!(that.fRasterliegend == null ? this.fRasterliegend == null
				: that.fRasterliegend.equals(this.fRasterliegend))) {
			return false;
		}
		if (!(that.fRasterstehend == null ? this.fRasterstehend == null
				: that.fRasterstehend.equals(this.fRasterstehend))) {
			return false;
		}
		if (!(that.bHochstellen == null ? this.bHochstellen == null
				: that.bHochstellen.equals(this.bHochstellen))) {
			return false;
		}
		if (!(that.bHochsetzen == null ? this.bHochsetzen == null
				: that.bHochsetzen.equals(this.bHochsetzen))) {
			return false;
		}
		if (!(that.bPolarisiert == null ? this.bPolarisiert == null
				: that.bPolarisiert.equals(this.bPolarisiert))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.artikelIId.hashCode();
		result = 37 * result + this.fRasterliegend.hashCode();
		result = 37 * result + this.fRasterstehend.hashCode();
		result = 37 * result + this.bHochstellen.hashCode();
		result = 37 * result + this.bHochsetzen.hashCode();
		result = 37 * result + this.bPolarisiert.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += artikelIId;
		returnString += ", " + fRasterliegend;
		returnString += ", " + fRasterstehend;
		returnString += ", " + bHochstellen;
		returnString += ", " + bHochsetzen;
		returnString += ", " + bPolarisiert;
		return returnString;
	}
}

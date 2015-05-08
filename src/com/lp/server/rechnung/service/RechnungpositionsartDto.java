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
package com.lp.server.rechnung.service;

import java.io.Serializable;

import com.lp.service.DatenspracheIf;

public class RechnungpositionsartDto implements Serializable, DatenspracheIf {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String positionsartCNr;
	private Integer iSort;
	private Short bVersteckt;

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	public Short getBVersteckt() {
		return bVersteckt;
	}

	public String getPositionsartCNr() {
		return positionsartCNr;
	}

	public void setPositionsartCNr(String positionsartCNr) {
		this.positionsartCNr = positionsartCNr;
	}

	public Integer getISort() {
		return iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof RechnungpositionsartDto))
			return false;
		RechnungpositionsartDto that = (RechnungpositionsartDto) obj;
		if (!(that.positionsartCNr == null ? this.positionsartCNr == null
				: that.positionsartCNr.equals(this.positionsartCNr)))
			return false;
		if (!(that.iSort == null ? this.iSort == null : that.iSort
				.equals(this.iSort)))
			return false;
		if (!(that.bVersteckt == null ? this.bVersteckt == null
				: that.bVersteckt.equals(this.bVersteckt)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.positionsartCNr.hashCode();
		result = 37 * result + this.iSort.hashCode();
		result = 37 * result + this.bVersteckt.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += positionsartCNr;
		returnString += ", " + iSort;
		returnString += ", " + bVersteckt;
		return returnString;
	}

	public String getCNr() {
		return positionsartCNr;
	}
}

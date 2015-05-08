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

/**
 * <p>
 * <I>Data Object fuer Rechnungart</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum <I>6. 8. 2004</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version 1.0
 */
public class RechnungartDto implements Serializable, DatenspracheIf {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String cNr;
	private String rechnungtypCNr;
	private Integer iSort;

	private RechnungartsprDto rechnungartsprDto = new RechnungartsprDto();

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public String getRechnungtypCNr() {
		return rechnungtypCNr;
	}

	public void setRechnungtypCNr(String rechnungtypCNr) {
		this.rechnungtypCNr = rechnungtypCNr;
	}

	public Integer getISort() {
		return iSort;
	}

	public RechnungartsprDto getRechnungartsprDto() {
		return rechnungartsprDto;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public void setRechnungartsprDto(RechnungartsprDto rechnungartsprDto) {
		this.rechnungartsprDto = rechnungartsprDto;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof RechnungartDto)) {
			return false;
		}
		RechnungartDto that = (RechnungartDto) obj;
		if (!(that.cNr == null ? this.cNr == null : that.cNr.equals(this.cNr))) {
			return false;
		}
		if (!(that.rechnungtypCNr == null ? this.rechnungtypCNr == null
				: that.rechnungtypCNr.equals(this.rechnungtypCNr))) {
			return false;
		}
		if (!(that.iSort == null ? this.iSort == null : that.iSort
				.equals(this.iSort))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.cNr.hashCode();
		result = 37 * result + this.rechnungtypCNr.hashCode();
		result = 37 * result + this.iSort.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += cNr;
		returnString += ", " + rechnungtypCNr;
		returnString += ", " + iSort;
		return returnString;
	}
}

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
package com.lp.server.partner.service;

import java.io.Serializable;

public class AnsprechpartnerfunktionDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cNr;
	private Integer iId;
	public String getCReportname() {
		return cReportname;
	}

	public void setCReportname(String reportname) {
		cReportname = reportname;
	}

	private String cReportname;
	private AnsprechpartnerfunktionsprDto ansprechpartnerfunktionsprDto = null;

	public String getCNr() {
		return cNr;
	}

	public String getBezeichnung() {
		if (getAnsprechpartnerfunktionsprDto() != null) {
			if (getAnsprechpartnerfunktionsprDto().getCBez() != null) {
				return getAnsprechpartnerfunktionsprDto().getCBez();
			} else {
				return getCNr();
			}
		} else {
			return getCNr();
		}
	}

	public Integer getIId() {
		return iId;
	}

	public AnsprechpartnerfunktionsprDto getAnsprechpartnerfunktionsprDto() {
		return ansprechpartnerfunktionsprDto;
	}

	public void setCNr(String cNrI) {
		this.cNr = cNrI;
	}

	public void setAnsprechpartnerfunktionsprDto(
			AnsprechpartnerfunktionsprDto ansprechpartnerfunktionsprDto) {
		this.ansprechpartnerfunktionsprDto = ansprechpartnerfunktionsprDto;
	}

	public void setIId(Integer iIdI) {
		this.iId = iIdI;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AnsprechpartnerfunktionDto)) {
			return false;
		}
		AnsprechpartnerfunktionDto that = (AnsprechpartnerfunktionDto) obj;
		if (!(that.cNr == null ? this.cNr == null : that.cNr.equals(this.cNr))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.cNr.hashCode();
		result = 37 * result + this.iId.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += cNr;
		returnString += ", " + iId;
		return returnString;
	}
}

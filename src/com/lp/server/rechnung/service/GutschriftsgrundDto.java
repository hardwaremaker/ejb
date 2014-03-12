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
package com.lp.server.rechnung.service;

import java.io.Serializable;

public class GutschriftsgrundDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cNr;
	private GutschriftsgrundsprDto gutschriftsgrundsprDto;
	private Integer iId;

	public String getCNr() {
		return cNr;
	}

	public String getBezeichnung() {
		if (getGutschriftsgrundsprDto() != null) {
			if (getGutschriftsgrundsprDto().getCBez() != null) {
				return getGutschriftsgrundsprDto().getCBez();
			} else {
				return getCNr().trim();
			}
		} else {
			return getCNr().trim();
		}
	}

	public GutschriftsgrundsprDto getGutschriftsgrundsprDto() {
		return gutschriftsgrundsprDto;
	}

	public Integer getIId() {
		return iId;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public void setGutschriftsgrundsprDto(GutschriftsgrundsprDto gutschriftsgrundsprDto) {
		this.gutschriftsgrundsprDto = gutschriftsgrundsprDto;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof GutschriftsgrundDto)) {
			return false;
		}
		GutschriftsgrundDto that = (GutschriftsgrundDto) obj;
		if (!(that.cNr == null ? this.cNr == null : that.cNr.equals(this.cNr))) {
			return false;
		}
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
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

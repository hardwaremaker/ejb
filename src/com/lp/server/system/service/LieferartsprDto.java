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
package com.lp.server.system.service;

import java.io.Serializable;

public class LieferartsprDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private Integer lieferartIId;
	private String localeCNr;
	private String cBezeichnung;

	public Integer getLieferartIId() {
		return lieferartIId;
	}

	public void setLieferartIId(Integer lieferartIId) {
		this.lieferartIId = lieferartIId;
	}

	public String getLocaleCNr() {
		return localeCNr;
	}

	public void setLocaleCNr(String localeCNr) {
		this.localeCNr = localeCNr;
	}

	public String getCBezeichnung() {
		return cBezeichnung;
	}

	public void setCBezeichnung(String cBezeichnung) {
		this.cBezeichnung = cBezeichnung;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof LieferartsprDto)) {
			return false;
		}
		LieferartsprDto that = (LieferartsprDto) obj;
		if (!(that.lieferartIId == null ? this.lieferartIId == null
				: that.lieferartIId.equals(this.lieferartIId))) {
			return false;
		}
		if (!(that.localeCNr == null ? this.localeCNr == null : that.localeCNr
				.equals(this.localeCNr))) {
			return false;
		}
		if (!(that.cBezeichnung == null ? this.cBezeichnung == null
				: that.cBezeichnung.equals(this.cBezeichnung))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.lieferartIId.hashCode();
		result = 37 * result + this.localeCNr.hashCode();
		result = 37 * result + this.cBezeichnung.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += lieferartIId;
		returnString += ", " + localeCNr;
		returnString += ", " + cBezeichnung;
		return returnString;
	}
}

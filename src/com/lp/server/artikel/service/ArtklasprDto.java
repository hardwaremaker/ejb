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

public class ArtklasprDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String localeCNr;
	private Integer artklaIId;
	private String cBez;

	public String getLocaleCNr() {
		return localeCNr;
	}

	public void setLocaleCNr(String localeCNr) {
		this.localeCNr = localeCNr;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public Integer getArtklaIId() {
		return artklaIId;
	}

	public void setArtklaIId(Integer artklaIId) {
		this.artklaIId = artklaIId;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ArtklasprDto)) {
			return false;
		}
		ArtklasprDto that = (ArtklasprDto) obj;
		if (!(that.localeCNr == null ? this.localeCNr == null : that.localeCNr
				.equals(this.localeCNr))) {
			return false;
		}
		if (!(that.artklaIId == null ? this.artklaIId == null : that.artklaIId
				.equals(this.artklaIId))) {
			return false;
		}
		if (!(that.cBez == null ? this.cBez == null : that.cBez
				.equals(this.cBez))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.localeCNr.hashCode();
		result = 37 * result + this.artklaIId.hashCode();
		result = 37 * result + this.cBez.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += localeCNr;
		returnString += ", " + artklaIId;
		returnString += ", " + cBez;
		return returnString;
	}
}

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
package com.lp.server.finanz.service;

import java.io.Serializable;

public class ExportdatenDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer exportlaufIId;
	private String belegartCNr;
	private Integer iBelegiid;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getExportlaufIId() {
		return exportlaufIId;
	}

	public void setExportlaufIId(Integer exportlaufIId) {
		this.exportlaufIId = exportlaufIId;
	}

	public String getBelegartCNr() {
		return belegartCNr;
	}

	public void setBelegartCNr(String belegartCNr) {
		this.belegartCNr = belegartCNr;
	}

	public Integer getIBelegiid() {
		return iBelegiid;
	}

	public void setIBelegiid(Integer iBelegiid) {
		this.iBelegiid = iBelegiid;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ExportdatenDto))
			return false;
		ExportdatenDto that = (ExportdatenDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.exportlaufIId == null ? this.exportlaufIId == null
				: that.exportlaufIId.equals(this.exportlaufIId)))
			return false;
		if (!(that.belegartCNr == null ? this.belegartCNr == null
				: that.belegartCNr.equals(this.belegartCNr)))
			return false;
		if (!(that.iBelegiid == null ? this.iBelegiid == null : that.iBelegiid
				.equals(this.iBelegiid)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.exportlaufIId.hashCode();
		result = 37 * result + this.belegartCNr.hashCode();
		result = 37 * result + this.iBelegiid.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + exportlaufIId;
		returnString += ", " + belegartCNr;
		returnString += ", " + iBelegiid;
		return returnString;
	}
}

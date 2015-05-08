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
package com.lp.server.finanz.service;

import java.io.Serializable;

public class BelegbuchungDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String belegartCNr;
	private Integer iBelegiid;
	private Integer buchungIId;
	private Integer buchungIIdZahlung;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
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

	public Integer getBuchungIId() {
		return buchungIId;
	}

	public void setBuchungIId(Integer buchungIId) {
		this.buchungIId = buchungIId;
	}

	public Integer getBuchungIIdZahlung() {
		return buchungIIdZahlung;
	}

	public void setBuchungIIdZahlung(Integer buchungIIdZahlung) {
		this.buchungIIdZahlung = buchungIIdZahlung;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof BelegbuchungDto))
			return false;
		BelegbuchungDto that = (BelegbuchungDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.belegartCNr == null ? this.belegartCNr == null
				: that.belegartCNr.equals(this.belegartCNr)))
			return false;
		if (!(that.iBelegiid == null ? this.iBelegiid == null : that.iBelegiid
				.equals(this.iBelegiid)))
			return false;
		if (!(that.buchungIId == null ? this.buchungIId == null
				: that.buchungIId.equals(this.buchungIId)))
			return false;
		if (!(that.buchungIIdZahlung == null ? this.buchungIIdZahlung == null
				: that.buchungIIdZahlung.equals(this.buchungIIdZahlung)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.belegartCNr.hashCode();
		result = 37 * result + this.iBelegiid.hashCode();
		result = 37 * result + this.buchungIId.hashCode();
		result = 37 * result + this.buchungIIdZahlung.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + belegartCNr;
		returnString += ", " + iBelegiid;
		returnString += ", " + buchungIId;
		returnString += ", " + buchungIIdZahlung;
		return returnString;
	}
}

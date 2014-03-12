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

public class PartnerbankDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer partnerIId;
	private Integer bankPartnerIId;
	private String cKtonr;
	private String cIban;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getPartnerIId() {
		return partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	public Integer getBankPartnerIId() {
		return bankPartnerIId;
	}

	public void setBankPartnerIId(Integer bankPartnerIId) {
		this.bankPartnerIId = bankPartnerIId;
	}

	public String getCKtonr() {
		return cKtonr;
	}

	public void setCKtonr(String cKtonr) {
		this.cKtonr = cKtonr;
	}

	public String getCIban() {
		return cIban;
	}

	public Integer getISort() {
		return iSort;
	}

	public void setCIban(String cIban) {
		this.cIban = cIban;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PartnerbankDto)) {
			return false;
		}
		PartnerbankDto that = (PartnerbankDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.partnerIId == null ? this.partnerIId == null
				: that.partnerIId.equals(this.partnerIId))) {
			return false;
		}
		if (!(that.bankPartnerIId == null ? this.bankPartnerIId == null
				: that.bankPartnerIId.equals(this.bankPartnerIId))) {
			return false;
		}
		if (!(that.cKtonr == null ? this.cKtonr == null : that.cKtonr
				.equals(this.cKtonr))) {
			return false;
		}
		if (!(that.cIban == null ? this.cIban == null : that.cIban
				.equals(this.cIban))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.partnerIId.hashCode();
		result = 37 * result + this.bankPartnerIId.hashCode();
		result = 37 * result + this.cKtonr.hashCode();
		result = 37 * result + this.cIban.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + partnerIId;
		returnString += ", " + bankPartnerIId;
		returnString += ", " + cKtonr;
		returnString += ", " + cIban;
		return returnString;
	}

	private Integer iSort;
}

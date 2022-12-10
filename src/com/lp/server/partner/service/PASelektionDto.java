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
package com.lp.server.partner.service;

import java.io.Serializable;

public class PASelektionDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer partnerIId;
	private Integer selektionIId;
	private String cBemerkung;
	private Integer iId;

	public Integer getIId() {
		return this.iId;
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

	public Integer getSelektionIId() {
		return selektionIId;
	}

	public void setSelektionIId(Integer selektionIId) {
		this.selektionIId = selektionIId;
	}

	public String getCBemerkung() {
		return cBemerkung;
	}


	public void setCBemerkung(String cBemerkung) {
		this.cBemerkung = cBemerkung;
	}


	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof PASelektionDto))
			return false;
		PASelektionDto that = (PASelektionDto) obj;
		if (!(that.partnerIId == null ? this.partnerIId == null
				: that.partnerIId.equals(this.partnerIId)))
			return false;
		if (!(that.selektionIId == null ? this.selektionIId == null
				: that.selektionIId.equals(this.selektionIId)))
			return false;
		if (!(that.cBemerkung == null ? this.cBemerkung == null
				: that.cBemerkung.equals(this.cBemerkung)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.partnerIId.hashCode();
		result = 37 * result + this.selektionIId.hashCode();
		result = 37 * result + this.cBemerkung.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += ", " + partnerIId;
		returnString += ", " + selektionIId;
		returnString += ", " + cBemerkung;
		return returnString;
	}
}

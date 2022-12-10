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
package com.lp.server.artikel.service;

import java.io.Serializable;

import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.HvDtoLogIgnore;

public class HerstellerDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cNr;
	private Integer partnerIId;
	private Integer iId;
	private PartnerDto partnerDto;
	private String cLeadIn;

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public Integer getPartnerIId() {
		return partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	public Integer getIId() {
		return iId;
	}

	public PartnerDto getPartnerDto() {
		return partnerDto;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public void setPartnerDto(PartnerDto partnerDto) {
		this.partnerDto = partnerDto;
	}

	@HvDtoLogIgnore
	public String getBezeichnung() {

		String bezeichnung = getCNr();

		if (getPartnerDto() != null) {
			return bezeichnung += " " + getPartnerDto().getCName1nachnamefirmazeile1();
		}
		return bezeichnung;

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HerstellerDto other = (HerstellerDto) obj;
		if (cLeadIn == null) {
			if (other.cLeadIn != null)
				return false;
		} else if (!cLeadIn.equals(other.cLeadIn))
			return false;
		if (cNr == null) {
			if (other.cNr != null)
				return false;
		} else if (!cNr.equals(other.cNr))
			return false;
		if (iId == null) {
			if (other.iId != null)
				return false;
		} else if (!iId.equals(other.iId))
			return false;
		if (partnerIId == null) {
			if (other.partnerIId != null)
				return false;
		} else if (!partnerIId.equals(other.partnerIId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cLeadIn == null) ? 0 : cLeadIn.hashCode());
		result = prime * result + ((cNr == null) ? 0 : cNr.hashCode());
		result = prime * result + ((iId == null) ? 0 : iId.hashCode());
		result = prime * result + ((partnerIId == null) ? 0 : partnerIId.hashCode());
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += cNr;
		returnString += ", " + partnerIId;
		returnString += ", " + iId;
		return returnString;
	}

	/**
	 * Der Barcode-LeadIn dieses Herstellers
	 * 
	 * @return Barcode-LeadIn des Herstellers
	 */
	public String getCLeadIn() {
		return cLeadIn;
	}

	public void setCLeadIn(String cLeadIn) {
		this.cLeadIn = cLeadIn;
	}
}

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

import com.lp.server.system.service.HvDtoLogClass;
import com.lp.server.system.service.HvDtoLogIgnore;
import com.lp.server.util.IIId;

@HvDtoLogClass(name = HvDtoLogClass.BANK)
public class BankDto implements Serializable, IIId {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer partnerIId;
	private String cBlz;
	private String cBic;
	private Integer partnerIIdNeuAus = null;

	
	public Integer getIId() {
		return partnerIId;
	}

	public void setIId(Integer iId) {
		this.partnerIId = iId;
	}

	// Manuell hinzugefuegt.
	private PartnerDto partnerDto = new PartnerDto();

	public Integer getPartnerIId() {
		return partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	public String getCBlz() {
		return cBlz;
	}

	public void setCBlz(String cBlz) {
		this.cBlz = cBlz;
	}

	public String getCBic() {
		return cBic;
	}

	public void setCBic(String cBic) {
		this.cBic = cBic;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BankDto)) {
			return false;
		}
		BankDto that = (BankDto) obj;
		if (!(that.partnerIId == null ? this.partnerIId == null : that.partnerIId.equals(this.partnerIId))) {
			return false;
		}
		if (!(that.cBlz == null ? this.cBlz == null : that.cBlz.equals(this.cBlz))) {
			return false;
		}
		if (!(that.cBic == null ? this.cBic == null : that.cBic.equals(this.cBic))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.partnerIId.hashCode();
		result = 37 * result + this.cBlz.hashCode();
		result = 37 * result + this.cBic.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += partnerIId;
		returnString += ", " + cBlz;
		returnString += ", " + cBic;
		return returnString;
	}

	@HvDtoLogIgnore
	public PartnerDto getPartnerDto() {
		return partnerDto;
	}

	public Integer getPartnerIIdNeuAus() {
		return partnerIIdNeuAus;
	}

	public void setPartnerDto(PartnerDto partnerDto) {
		this.partnerDto = partnerDto;
	}

	public void setPartnerIIdNeuAus(Integer partnerIIdNeuAus) {
		this.partnerIIdNeuAus = partnerIIdNeuAus;
	}
}

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
import java.util.BitSet;

public class PartnerkommunikationDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int iF = 0;
	public int iFiId = iF++;
	private Integer iId;

	public int iFpartnerIId = iF++;
	private Integer partnerIId;

	public int iFkommunikationsartCNr = iF++;
	private String kommunikationsartCNr;

	public int iFcBez = iF++;
	private String cBez;

	public int iFcInhalt = iF++;
	private String cInhalt;

	public int iFcNrMandant = iF++;
	private String cNrMandant = null;

	public BitSet bsIndikator = new BitSet(iF);

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		bsIndikator.set(iFiId, true);
		this.iId = iId;
	}

	public Integer getPartnerIId() {
		return partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		bsIndikator.set(iFpartnerIId, true);
		this.partnerIId = partnerIId;
	}

	public String getKommunikationsartCNr() {
		return kommunikationsartCNr;
	}

	public void setKommunikationsartCNr(String kommunikationsartCNr) {
		bsIndikator.set(iFkommunikationsartCNr, true);
		this.kommunikationsartCNr = kommunikationsartCNr;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		bsIndikator.set(iFcBez, true);
		this.cBez = cBez;
	}

	public String getCInhalt() {
		return cInhalt;
	}

	public void setCInhalt(String cInhalt) {
		bsIndikator.set(iFcInhalt, true);
		this.cInhalt = cInhalt;
	}

	public String getCNrMandant() {
		return cNrMandant;
	}

	public void setCNrMandant(String cNrMandant) {
		bsIndikator.set(iFcNrMandant, true);
		this.cNrMandant = cNrMandant;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PartnerkommunikationDto)) {
			return false;
		}
		PartnerkommunikationDto that = (PartnerkommunikationDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.partnerIId == null ? this.partnerIId == null
				: that.partnerIId.equals(this.partnerIId))) {
			return false;
		}
		if (!(that.kommunikationsartCNr == null ? this.kommunikationsartCNr == null
				: that.kommunikationsartCNr.equals(this.kommunikationsartCNr))) {
			return false;
		}
		if (!(that.cBez == null ? this.cBez == null : that.cBez
				.equals(this.cBez))) {
			return false;
		}
		if (!(that.cInhalt == null ? this.cInhalt == null : that.cInhalt
				.equals(this.cInhalt))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.partnerIId.hashCode();
		result = 37 * result + this.kommunikationsartCNr.hashCode();
		result = 37 * result + this.cBez.hashCode();
		result = 37 * result + this.cInhalt.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + partnerIId;
		returnString += ", " + kommunikationsartCNr;
		returnString += ", " + cBez;
		returnString += ", " + cInhalt;
		return returnString;
	}
}

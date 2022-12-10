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
import java.util.BitSet;

import com.lp.util.Helper;

public class LandplzortDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	int iF = 0;

	public int iFiId = iF++;
	private Integer iId;

	public int iFilandID = iF++;
	private Integer ilandID;

	// public int iFlandCLkz = iF++;
	// private String landCLkz;

	public int iFcPlz = iF++;
	private String cPlz;

	public int iFortIId = iF++;
	private Integer ortIId;

	public int iFortDto = iF++;
	private OrtDto ortDto = null;

	public int iFlandDto = iF++;
	private LandDto landDto = null;

	public BitSet bsIndikator = new BitSet(iF);

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		bsIndikator.set(iFiId, true);
		this.iId = iId;
	}

	// public String getLandCLkz() {
	// return landCLkz;
	// }
	//
	//
	// public void setLandCLkz(String landCLkz) {
	// bsIndikator.set(iFlandCLkz, true);
	// this.landCLkz = landCLkz;
	// }

	public String getCPlz() {
		return cPlz;
	}

	public void setCPlz(String cPlz) {
		bsIndikator.set(iFcPlz, true);
		this.cPlz = cPlz;
	}

	public Integer getOrtIId() {
		return ortIId;
	}

	public LandDto getLandDto() {
		return landDto;
	}

	public void setLandDto(LandDto landDto) {
		bsIndikator.set(iFlandDto, true);
		this.landDto = landDto;
	}

	public OrtDto getOrtDto() {
		return ortDto;
	}

	public Integer getIlandID() {
		return ilandID;
	}

	public void setOrtIId(Integer ortIId) {
		bsIndikator.set(iFortIId, true);
		this.ortIId = ortIId;
	}

	public void setOrtDto(OrtDto ortDto) {
		bsIndikator.set(iFortDto, true);
		this.ortDto = ortDto;
	}

	public void setIlandID(Integer ilandID) {
		bsIndikator.set(iFilandID, true);
		this.ilandID = ilandID;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof LandplzortDto)) {
			return false;
		}
		LandplzortDto that = (LandplzortDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		// if (! (that.landCLkz == null ? this.landCLkz == null :
		// that.landCLkz.equals(this.landCLkz))) {
		// return false;
		// }
		if (!(that.cPlz == null ? this.cPlz == null : that.cPlz.equals(this.cPlz))) {
			return false;
		}
		if (!(that.ortIId == null ? this.ortIId == null : that.ortIId.equals(this.ortIId))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		// result = 37 * result + this.landCLkz.hashCode();
		result = 37 * result + this.cPlz.hashCode();
		result = 37 * result + this.ortIId.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		// returnString += ", " + landCLkz;
		returnString += ", " + cPlz;
		returnString += ", " + ortIId;
		return returnString;
	}

	// Formater
	// ******************************************************************
	public String formatLandPlzOrt() {
		return landDto.getCLkz() + " " + getCPlz() + " " + getOrtDto().getCName();
	}

	public String formatPlzOrt() {
		return formatPlzOrt(false);
	}

	public String formatPlzOrt(boolean bAusland) {

		String ort = getOrtDto().getCName();
		if (ort != null && bAusland) {
			ort = ort.toUpperCase();
		}

		if (getLandDto() != null && Helper.short2boolean(getLandDto().getBPlznachort())) {
			return ort + " " + getCPlz();
		} else {
			return getCPlz() + " " + ort;
		}

	}
}

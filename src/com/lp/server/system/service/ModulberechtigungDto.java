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
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class ModulberechtigungDto extends CryptDto implements Serializable, ICryptDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String belegartCNr;
	private String mandantCNr;
	transient private byte[] oCode;
	transient private byte[] oHash;
	
	public String getBelegartCNr() {
		return belegartCNr;
	}

	public void setBelegartCNr(String belegartCNr) {
		this.belegartCNr = belegartCNr;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public void setOCode(byte[] oCode) {
		this.oCode = oCode;
	}

	public void setOHash(byte[] oHash) {
		this.oHash = oHash;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((belegartCNr == null) ? 0 : belegartCNr.hashCode());
		result = prime * result
				+ ((mandantCNr == null) ? 0 : mandantCNr.hashCode());
		result = prime * result + Arrays.hashCode(oCode);
		result = prime * result + Arrays.hashCode(oHash);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModulberechtigungDto other = (ModulberechtigungDto) obj;
		if (belegartCNr == null) {
			if (other.belegartCNr != null)
				return false;
		} else if (!belegartCNr.equals(other.belegartCNr))
			return false;
		if (mandantCNr == null) {
			if (other.mandantCNr != null)
				return false;
		} else if (!mandantCNr.equals(other.mandantCNr))
			return false;
		if (!Arrays.equals(oCode, other.oCode))
			return false;
		if (!Arrays.equals(oHash, other.oHash))
			return false;
		return true;
	}

	public String toString() {
		String returnString = "";
		returnString += belegartCNr;
		returnString += ", " + mandantCNr;
		return returnString;
	}

	@Override
	public String toValidateString() {
		String returnString = "";
		returnString += belegartCNr;
		returnString += mandantCNr;
		returnString += Arrays.hashCode(oCode);
		return returnString;
	}

	@Override
	public boolean validate() {
		if (this.oHash == null) return false;
		byte[] baDecode = super.decodeRSA(this.oHash, this.oCode);
		if (baDecode == null) return false;
		String sDecode;
		try {
			sDecode = new String(baDecode, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			sDecode = new String(baDecode);
		}
		if (sDecode.compareTo(this.toValidateString())==0) return true;
		return false;
	}
}

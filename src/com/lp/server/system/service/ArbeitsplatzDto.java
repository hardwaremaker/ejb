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

public class ArbeitsplatzDto extends CryptDto implements Serializable, ICryptDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String cStandort;
	private String cBemerkung;
	private String cPcname;
	private String cTyp;
	private String cGeraetecode;
	transient private byte[] oCode;
	transient private byte[] oHash;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCStandort() {
		return cStandort;
	}

	public void setCStandort(String cStandort) {
		this.cStandort = cStandort;
	}

	public String getCBemerkung() {
		return cBemerkung;
	}

	public void setCBemerkung(String cBemerkung) {
		this.cBemerkung = cBemerkung;
	}

	public String getCPcname() {
		return cPcname;
	}

	public void setCPcname(String cPcname) {
		this.cPcname = cPcname;
	}

	public void setCTyp(String cTyp) {
		this.cTyp = cTyp;
	}

	public String getCTyp() {
		return cTyp;
	}

	public void setCGeraetecode(String cGeraetecode) {
		this.cGeraetecode = cGeraetecode;
	}

	public String getCGeraetecode() {
		return cGeraetecode;
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
				+ ((cBemerkung == null) ? 0 : cBemerkung.hashCode());
		result = prime * result
				+ ((cGeraetecode == null) ? 0 : cGeraetecode.hashCode());
		result = prime * result + ((cPcname == null) ? 0 : cPcname.hashCode());
		result = prime * result
				+ ((cStandort == null) ? 0 : cStandort.hashCode());
		result = prime * result + ((cTyp == null) ? 0 : cTyp.hashCode());
		result = prime * result + ((iId == null) ? 0 : iId.hashCode());
		result = prime * result + Arrays.hashCode(oCode);
		result = prime * result + Arrays.hashCode(oHash);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)	return true;
		if (obj == null)	return false;
		if (getClass() != obj.getClass())	return false;
		ArbeitsplatzDto other = (ArbeitsplatzDto) obj;
		if (cBemerkung == null) {
			if (other.cBemerkung != null)	return false;
		} else if (!cBemerkung.equals(other.cBemerkung))	return false;
		if (cGeraetecode == null) {
			if (other.cGeraetecode != null)	return false;
		} else if (!cGeraetecode.equals(other.cGeraetecode))	return false;
		if (cPcname == null) {
			if (other.cPcname != null)	return false;
		} else if (!cPcname.equals(other.cPcname))	return false;
		if (cStandort == null) {
			if (other.cStandort != null)	return false;
		} else if (!cStandort.equals(other.cStandort))	return false;
		if (cTyp == null) {
			if (other.cTyp != null)	return false;
		} else if (!cTyp.equals(other.cTyp))	return false;
		if (iId == null) {
			if (other.iId != null)	return false;
		} else if (!iId.equals(other.iId))	return false;
		if (!Arrays.equals(oCode, other.oCode))	return false;
		if (!Arrays.equals(oHash, other.oHash))	return false;
		return true;
	}

	public String toString() {
		StringBuffer returnStringBuffer = new StringBuffer(128);
		returnStringBuffer.append("[");
		returnStringBuffer.append("iId:").append(iId);
		returnStringBuffer.append("cStandort:").append(cStandort);
		returnStringBuffer.append("cBemerkung:").append(cBemerkung);
		returnStringBuffer.append("cPcname:").append(cPcname);
		returnStringBuffer.append("cTyp:").append(cTyp);
		returnStringBuffer.append("cGeraetecode:").append(cGeraetecode);
		returnStringBuffer.append("]");
		return returnStringBuffer.toString();
	}

	@Override
	public String toValidateString() {
		StringBuffer returnStringBuffer = new StringBuffer(128);
		returnStringBuffer.append(iId);
		returnStringBuffer.append(cStandort);
		returnStringBuffer.append(cPcname);
		returnStringBuffer.append(cTyp);
		returnStringBuffer.append(cGeraetecode);
		returnStringBuffer.append(Arrays.hashCode(oCode));
		return returnStringBuffer.toString();
	}

	@Override
	public boolean validate() {
		// nur Terminals und aehnl. (= Arbeitsplaetze mit Typ != null) pruefen
		// alle anderen sind PCs und koennen vom Anwender gepflegt werden!
		//
		if (cTyp == null && cGeraetecode == null) return true;
		if (this.oHash == null) return false;
		byte[] baDecode = super.decodeRSA(this.oHash, oCode);
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

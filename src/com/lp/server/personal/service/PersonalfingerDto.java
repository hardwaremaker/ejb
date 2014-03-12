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
package com.lp.server.personal.service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;

public class PersonalfingerDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iId;
	private Integer personalIId;
	private Integer iFingerid;
	private byte[] oTemplate1;
	private byte[] oTemplate2;
	private Timestamp tAendern;
	private Integer fingerartIId;
	private Integer personalIIdAendern;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getPersonalIId() {
		return personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

	public Integer getIFingerid() {
		return iFingerid;
	}

	public void setIFingerid(Integer iFingerid) {
		this.iFingerid = iFingerid;
	}

	public byte[] getOTemplate1() {
		return oTemplate1;
	}

	public void setOTemplate1(byte[] oTemplate1) {
		this.oTemplate1 = oTemplate1;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public Integer getFingerartIId() {
		return fingerartIId;
	}

	public byte[] getOTemplate2() {
		return oTemplate2;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public void setFingerartIId(Integer fingerartIId) {
		this.fingerartIId = fingerartIId;
	}

	public void setOTemplate2(byte[] oTemplate2) {
		this.oTemplate2 = oTemplate2;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof PersonalfingerDto))
			return false;
		PersonalfingerDto that = (PersonalfingerDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.personalIId == null ? this.personalIId == null
				: that.personalIId.equals(this.personalIId)))
			return false;
		if (!(that.iFingerid == null ? this.iFingerid == null : that.iFingerid
				.equals(this.iFingerid)))
			return false;
		if (!(that.oTemplate1 == null ? this.oTemplate1 == null
				: Arrays.equals(that.oTemplate1, this.oTemplate1)))
			return false;
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.personalIId.hashCode();
		result = 37 * result + this.iFingerid.hashCode();
		result = 37 * result + Arrays.hashCode(this.oTemplate1);
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		return result;
	}

	public String toString() {
		StringBuffer returnStringBuffer = new StringBuffer(224);
		returnStringBuffer.append("[");
		returnStringBuffer.append("iId:").append(iId);
		returnStringBuffer.append("personalIId:").append(personalIId);
		returnStringBuffer.append("iFingerid:").append(iFingerid);
		returnStringBuffer.append("oTemplate:").append(oTemplate1);
		returnStringBuffer.append("tAendern:").append(tAendern);
		returnStringBuffer.append("personalIIdAendern:").append(
				personalIIdAendern);
		returnStringBuffer.append("]");
		return returnStringBuffer.toString();
	}
}

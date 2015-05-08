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
package com.lp.server.personal.service;

import java.io.Serializable;
import java.sql.Timestamp;

public class PersonalzutrittsklasseDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iId;
	private Integer personalIId;
	private Integer zutrittsklasseIId;
	private ZutrittsklasseDto ZutrittsklasseDto;
	private Timestamp tGueltigab;
	private PersonalDto personalDto;

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

	public Integer getZutrittsklasseIId() {
		return zutrittsklasseIId;
	}

	public void setZutrittsklasseIId(Integer zutrittsklasseIId) {
		this.zutrittsklasseIId = zutrittsklasseIId;
	}

	public Timestamp getTGueltigab() {
		return tGueltigab;
	}

	public PersonalDto getPersonalDto() {
		return personalDto;
	}

	public ZutrittsklasseDto getZutrittsklasseDto() {
		return ZutrittsklasseDto;
	}

	public void setTGueltigab(Timestamp tGueltigab) {
		this.tGueltigab = tGueltigab;
	}

	public void setPersonalDto(PersonalDto personalDto) {
		this.personalDto = personalDto;
	}

	public void setZutrittsklasseDto(ZutrittsklasseDto ZutrittsklasseDto) {
		this.ZutrittsklasseDto = ZutrittsklasseDto;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof PersonalzutrittsklasseDto))
			return false;
		PersonalzutrittsklasseDto that = (PersonalzutrittsklasseDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.personalIId == null ? this.personalIId == null
				: that.personalIId.equals(this.personalIId)))
			return false;
		if (!(that.zutrittsklasseIId == null ? this.zutrittsklasseIId == null
				: that.zutrittsklasseIId.equals(this.zutrittsklasseIId)))
			return false;
		if (!(that.tGueltigab == null ? this.tGueltigab == null
				: that.tGueltigab.equals(this.tGueltigab)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.personalIId.hashCode();
		result = 37 * result + this.zutrittsklasseIId.hashCode();
		result = 37 * result + this.tGueltigab.hashCode();
		return result;
	}

	public String toString() {
		StringBuffer returnStringBuffer = new StringBuffer(128);
		returnStringBuffer.append("[");
		returnStringBuffer.append("iId:").append(iId);
		returnStringBuffer.append("personalIId:").append(personalIId);
		returnStringBuffer.append("zutrittsklasseIId:").append(
				zutrittsklasseIId);
		returnStringBuffer.append("tGueltigab:").append(tGueltigab);
		returnStringBuffer.append("]");
		return returnStringBuffer.toString();
	}
}

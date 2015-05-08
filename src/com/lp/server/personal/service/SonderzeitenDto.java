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
import java.sql.Time;
import java.sql.Timestamp;

import com.lp.util.Helper;

public class SonderzeitenDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iId;
	private Integer personalIId;
	private Timestamp tDatum;
	private Integer taetigkeitIId;
	private Short bTag;
	private Time uStunden;
	private Short bHalbtag;
	private Integer personalIIdAendern;
	private Timestamp tAendern;
	private TaetigkeitDto taetigkeitDto;

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

	public Timestamp getTDatum() {
		return tDatum;
	}

	public void setTDatum(Timestamp tDatum) {
		this.tDatum = Helper.cutTimestamp(tDatum);
	}

	public Integer getTaetigkeitIId() {
		return taetigkeitIId;
	}

	public void setTaetigkeitIId(Integer taetigkeitIId) {
		this.taetigkeitIId = taetigkeitIId;
	}

	public Short getBTag() {
		return bTag;
	}

	public void setBTag(Short bTag) {
		this.bTag = bTag;
	}

	public Time getUStunden() {
		return uStunden;
	}

	public void setUStunden(Time uStunden) {
		this.uStunden = uStunden;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public TaetigkeitDto getTaetigkeitDto() {
		return taetigkeitDto;
	}

	public Short getBHalbtag() {
		return bHalbtag;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public void setTaetigkeitDto(TaetigkeitDto taetigkeitDto) {
		this.taetigkeitDto = taetigkeitDto;
	}

	public void setBHalbtag(Short bHalbtag) {
		this.bHalbtag = bHalbtag;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof SonderzeitenDto))
			return false;
		SonderzeitenDto that = (SonderzeitenDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.personalIId == null ? this.personalIId == null
				: that.personalIId.equals(this.personalIId)))
			return false;
		if (!(that.tDatum == null ? this.tDatum == null : that.tDatum
				.equals(this.tDatum)))
			return false;
		if (!(that.taetigkeitIId == null ? this.taetigkeitIId == null
				: that.taetigkeitIId.equals(this.taetigkeitIId)))
			return false;
		if (!(that.bTag == null ? this.bTag == null : that.bTag
				.equals(this.bTag)))
			return false;
		if (!(that.uStunden == null ? this.uStunden == null : that.uStunden
				.equals(this.uStunden)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.personalIId.hashCode();
		result = 37 * result + this.tDatum.hashCode();
		result = 37 * result + this.taetigkeitIId.hashCode();
		result = 37 * result + this.bTag.hashCode();
		result = 37 * result + this.uStunden.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + personalIId;
		returnString += ", " + tDatum;
		returnString += ", " + taetigkeitIId;
		returnString += ", " + bTag;
		returnString += ", " + uStunden;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + tAendern;
		return returnString;
	}
}

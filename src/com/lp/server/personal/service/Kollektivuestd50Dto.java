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

public class Kollektivuestd50Dto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private Short bUnterignorieren;

	public Short getBUnterignorieren() {
		return bUnterignorieren;
	}

	public void setBUnterignorieren(Short bUnterignorieren) {
		this.bUnterignorieren = bUnterignorieren;
	}

	private Integer iId;
	private Integer kollektivIId;
	private Time uVon;
	private Time uBis;
	private Short bRestdestages;
	private Integer tagesartIId;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getKollektivIId() {
		return kollektivIId;
	}

	public void setKollektivIId(Integer kollektivIId) {
		this.kollektivIId = kollektivIId;
	}

	public Time getUVon() {
		return uVon;
	}

	public void setUVon(Time uVon) {
		this.uVon = uVon;
	}

	public Time getUBis() {
		return uBis;
	}

	public void setUBis(Time uBis) {
		this.uBis = uBis;
	}

	public Short getBRestdestages() {
		return bRestdestages;
	}

	public void setBRestdestages(Short bRestdestages) {
		this.bRestdestages = bRestdestages;
	}

	public Integer getTagesartIId() {
		return tagesartIId;
	}

	public void setTagesartIId(Integer tagesartIId) {
		this.tagesartIId = tagesartIId;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Kollektivuestd50Dto))
			return false;
		Kollektivuestd50Dto that = (Kollektivuestd50Dto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.kollektivIId == null ? this.kollektivIId == null
				: that.kollektivIId.equals(this.kollektivIId)))
			return false;
		if (!(that.uVon == null ? this.uVon == null : that.uVon
				.equals(this.uVon)))
			return false;
		if (!(that.uBis == null ? this.uBis == null : that.uBis
				.equals(this.uBis)))
			return false;
		if (!(that.bRestdestages == null ? this.bRestdestages == null
				: that.bRestdestages.equals(this.bRestdestages)))
			return false;
		if (!(that.tagesartIId == null ? this.tagesartIId == null
				: that.tagesartIId.equals(this.tagesartIId)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.kollektivIId.hashCode();
		result = 37 * result + this.uVon.hashCode();
		result = 37 * result + this.uBis.hashCode();
		result = 37 * result + this.bRestdestages.hashCode();
		result = 37 * result + this.tagesartIId.hashCode();
		return result;
	}

	public String toString() {
		StringBuffer returnStringBuffer = new StringBuffer(192);
		returnStringBuffer.append("[");
		returnStringBuffer.append("iId:").append(iId);
		returnStringBuffer.append("kollektivIId:").append(kollektivIId);
		returnStringBuffer.append("uVon:").append(uVon);
		returnStringBuffer.append("uBis:").append(uBis);
		returnStringBuffer.append("bRestdestages:").append(bRestdestages);
		returnStringBuffer.append("tagesartIId:").append(tagesartIId);
		returnStringBuffer.append("]");
		return returnStringBuffer.toString();
	}
}

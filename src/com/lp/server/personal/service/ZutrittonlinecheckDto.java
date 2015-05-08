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

public class ZutrittonlinecheckDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mandantCNr;
	private Integer iId;
	private Integer zutrittsklasseIId;
	private String cPincode;
	private String cAusweis;
	private Timestamp tGueltigab;
	private Timestamp tGueltigbis;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getZutrittsklasseIId() {
		return zutrittsklasseIId;
	}

	public void setZutrittsklasseIId(Integer zutrittsklasseIId) {
		this.zutrittsklasseIId = zutrittsklasseIId;
	}

	public String getCPincode() {
		return cPincode;
	}

	public void setCPincode(String cPincode) {
		this.cPincode = cPincode;
	}

	public String getCAusweis() {
		return cAusweis;
	}

	public void setCAusweis(String cAusweis) {
		this.cAusweis = cAusweis;
	}

	public Timestamp getTGueltigab() {
		return tGueltigab;
	}

	public void setTGueltigab(Timestamp tGueltigab) {
		this.tGueltigab = tGueltigab;
	}

	public Timestamp getTGueltigbis() {
		return tGueltigbis;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setTGueltigbis(Timestamp tGueltigbis) {
		this.tGueltigbis = tGueltigbis;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ZutrittonlinecheckDto))
			return false;
		ZutrittonlinecheckDto that = (ZutrittonlinecheckDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.zutrittsklasseIId == null ? this.zutrittsklasseIId == null
				: that.zutrittsklasseIId.equals(this.zutrittsklasseIId)))
			return false;
		if (!(that.cPincode == null ? this.cPincode == null : that.cPincode
				.equals(this.cPincode)))
			return false;
		if (!(that.cAusweis == null ? this.cAusweis == null : that.cAusweis
				.equals(this.cAusweis)))
			return false;
		if (!(that.tGueltigab == null ? this.tGueltigab == null
				: that.tGueltigab.equals(this.tGueltigab)))
			return false;
		if (!(that.tGueltigbis == null ? this.tGueltigbis == null
				: that.tGueltigbis.equals(this.tGueltigbis)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.zutrittsklasseIId.hashCode();
		result = 37 * result + this.cPincode.hashCode();
		result = 37 * result + this.cAusweis.hashCode();
		result = 37 * result + this.tGueltigab.hashCode();
		result = 37 * result + this.tGueltigbis.hashCode();
		return result;
	}

	public String toString() {
		StringBuffer returnStringBuffer = new StringBuffer(192);
		returnStringBuffer.append("[");
		returnStringBuffer.append("iId:").append(iId);
		returnStringBuffer.append("zutrittsklasseIId:").append(
				zutrittsklasseIId);
		returnStringBuffer.append("cPincode:").append(cPincode);
		returnStringBuffer.append("cAusweis:").append(cAusweis);
		returnStringBuffer.append("tGueltigab:").append(tGueltigab);
		returnStringBuffer.append("tGueltigbis:").append(tGueltigbis);
		returnStringBuffer.append("]");
		return returnStringBuffer.toString();
	}
}

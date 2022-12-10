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

public class ArbeitsplatzparameterDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer arbeitsplatzIId;
	private String parameterCNr;
	private String cWert;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getArbeitsplatzIId() {
		return arbeitsplatzIId;
	}

	public void setArbeitsplatzIId(Integer arbeitsplatzIId) {
		this.arbeitsplatzIId = arbeitsplatzIId;
	}

	public String getParameterCNr() {
		return parameterCNr;
	}

	public void setParameterCNr(String parameterCNr) {
		this.parameterCNr = parameterCNr;
	}

	public String getCWert() {
		return cWert;
	}

	public void setCWert(String cWert) {
		this.cWert = cWert;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ArbeitsplatzparameterDto))
			return false;
		ArbeitsplatzparameterDto that = (ArbeitsplatzparameterDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.arbeitsplatzIId == null ? this.arbeitsplatzIId == null
				: that.arbeitsplatzIId.equals(this.arbeitsplatzIId)))
			return false;
		if (!(that.parameterCNr == null ? this.parameterCNr == null
				: that.parameterCNr.equals(this.parameterCNr)))
			return false;
		if (!(that.cWert == null ? this.cWert == null : that.cWert
				.equals(this.cWert)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.arbeitsplatzIId.hashCode();
		result = 37 * result + this.parameterCNr.hashCode();
		result = 37 * result + this.cWert.hashCode();
		return result;
	}

	public String toString() {
		StringBuffer returnStringBuffer = new StringBuffer(128);
		returnStringBuffer.append("[");
		returnStringBuffer.append("iId:").append(iId);
		returnStringBuffer.append("arbeitsplatzIId:").append(arbeitsplatzIId);
		returnStringBuffer.append("parameterCNr:").append(parameterCNr);
		returnStringBuffer.append("cWert:").append(cWert);
		returnStringBuffer.append("]");
		return returnStringBuffer.toString();
	}
	
	public Boolean asBoolean() {
		return "1".equals(getCWert());
	}
}

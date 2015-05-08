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

public class ZutrittsmodelltagDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer zutrittsmodellIId;
	private Integer tagesartIId;
	private String xBem;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getZutrittsmodellIId() {
		return zutrittsmodellIId;
	}

	public void setZutrittsmodellIId(Integer zutrittsmodellIId) {
		this.zutrittsmodellIId = zutrittsmodellIId;
	}

	public Integer getTagesartIId() {
		return tagesartIId;
	}

	public void setTagesartIId(Integer tagesartIId) {
		this.tagesartIId = tagesartIId;
	}

	public String getXBem() {
		return xBem;
	}

	public void setXBem(String xBem) {
		this.xBem = xBem;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ZutrittsmodelltagDto))
			return false;
		ZutrittsmodelltagDto that = (ZutrittsmodelltagDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.zutrittsmodellIId == null ? this.zutrittsmodellIId == null
				: that.zutrittsmodellIId.equals(this.zutrittsmodellIId)))
			return false;
		if (!(that.tagesartIId == null ? this.tagesartIId == null
				: that.tagesartIId.equals(this.tagesartIId)))
			return false;
		if (!(that.xBem == null ? this.xBem == null : that.xBem
				.equals(this.xBem)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.zutrittsmodellIId.hashCode();
		result = 37 * result + this.tagesartIId.hashCode();
		result = 37 * result + this.xBem.hashCode();
		return result;
	}

	public String toString() {
		StringBuffer returnStringBuffer = new StringBuffer(128);
		returnStringBuffer.append("[");
		returnStringBuffer.append("iId:").append(iId);
		returnStringBuffer.append("zutrittsmodellIId:").append(
				zutrittsmodellIId);
		returnStringBuffer.append("tagesartIId:").append(tagesartIId);
		returnStringBuffer.append("xBem:").append(xBem);
		returnStringBuffer.append("]");
		return returnStringBuffer.toString();
	}
}

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

import com.lp.util.Helper;

public class EintrittaustrittDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iId;
	private Integer personalIId;
	private Timestamp tEintritt;
	private Timestamp tAustritt;
	private String cAustrittsgrund;

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

	public Timestamp getTEintritt() {
		return tEintritt;
	}

	public void setTEintritt(Timestamp tEintritt) {
		this.tEintritt = Helper.cutTimestamp(tEintritt);
	}

	public Timestamp getTAustritt() {
		return tAustritt;
	}

	public void setTAustritt(Timestamp tAustritt) {
		this.tAustritt = Helper.cutTimestamp(tAustritt);
	}

	public String getCAustrittsgrund() {
		return cAustrittsgrund;
	}

	public void setCAustrittsgrund(String cAustrittsgrund) {
		this.cAustrittsgrund = cAustrittsgrund;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof EintrittaustrittDto)) {
			return false;
		}
		EintrittaustrittDto that = (EintrittaustrittDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.personalIId == null ? this.personalIId == null
				: that.personalIId.equals(this.personalIId))) {
			return false;
		}
		if (!(that.tEintritt == null ? this.tEintritt == null : that.tEintritt
				.equals(this.tEintritt))) {
			return false;
		}
		if (!(that.tAustritt == null ? this.tAustritt == null : that.tAustritt
				.equals(this.tAustritt))) {
			return false;
		}
		if (!(that.cAustrittsgrund == null ? this.cAustrittsgrund == null
				: that.cAustrittsgrund.equals(this.cAustrittsgrund))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.personalIId.hashCode();
		result = 37 * result + this.tEintritt.hashCode();
		result = 37 * result + this.tAustritt.hashCode();
		result = 37 * result + this.cAustrittsgrund.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + personalIId;
		returnString += ", " + tEintritt;
		returnString += ", " + tAustritt;
		returnString += ", " + cAustrittsgrund;
		return returnString;
	}
}

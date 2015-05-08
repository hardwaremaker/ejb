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
package com.lp.server.benutzer.service;

import java.io.Serializable;

public class SystemrolleDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String cBez;
	private Integer iMaxUsers;
	private Integer aliasRolleIId;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((aliasRolleIId == null) ? 0 : aliasRolleIId.hashCode());
		result = prime * result + ((cBez == null) ? 0 : cBez.hashCode());
		result = prime * result + ((iId == null) ? 0 : iId.hashCode());
		result = prime * result
				+ ((iMaxUsers == null) ? 0 : iMaxUsers.hashCode());
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
		SystemrolleDto other = (SystemrolleDto) obj;
		if (aliasRolleIId == null) {
			if (other.aliasRolleIId != null)
				return false;
		} else if (!aliasRolleIId.equals(other.aliasRolleIId))
			return false;
		if (cBez == null) {
			if (other.cBez != null)
				return false;
		} else if (!cBez.equals(other.cBez))
			return false;
		if (iId == null) {
			if (other.iId != null)
				return false;
		} else if (!iId.equals(other.iId))
			return false;
		if (iMaxUsers == null) {
			if (other.iMaxUsers != null)
				return false;
		} else if (!iMaxUsers.equals(other.iMaxUsers))
			return false;
		return true;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + cBez;
		returnString += ", " + iMaxUsers;
		return returnString;
	}

	public Integer getIMaxUsers() {
		return iMaxUsers;
	}

	public void setiMaxUsers(Integer iMaxUsers) {
		this.iMaxUsers = iMaxUsers;
	}

	public Integer getAliasRolleIId() {
		return aliasRolleIId;
	}

	public void setAliasRolleIId(Integer aliasRolleIId) {
		this.aliasRolleIId = aliasRolleIId;
	}
}

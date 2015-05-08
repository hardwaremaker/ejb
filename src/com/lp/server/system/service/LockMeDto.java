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
import java.sql.Timestamp;

public class LockMeDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cWer;
	private String cWas;
	private Timestamp tWann;
	private Integer personalIIdLocker;
	private String cUsernr = null;

	public LockMeDto(String cWerI, String cWasI, String cUsernrI) {
		cWer = cWerI;
		cWas = cWasI;
		cUsernr = cUsernrI;
	}

	public LockMeDto() {
		// nothing here
	}

	public String getCWer() {
		return cWer;
	}

	public void setCWer(String cWer) {
		this.cWer = cWer;
	}

	public String getCWas() {
		return cWas;
	}

	public void setCWas(String cWas) {
		this.cWas = cWas;
	}

	public Timestamp getTWann() {
		return tWann;
	}

	public void setTWann(Timestamp tWann) {
		this.tWann = tWann;
	}

	public Integer getPersonalIIdLocker() {
		return personalIIdLocker;
	}

	public String getCUsernr() {
		return cUsernr;
	}

	public void setPersonalIIdLocker(Integer personalIIdLockerI) {
		personalIIdLocker = personalIIdLockerI;
	}

	public void setCUsernr(String cUsernr) {
		this.cUsernr = cUsernr;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof LockMeDto)) {
			return false;
		}
		LockMeDto that = (LockMeDto) obj;
		if (!(that.cWer == null ? this.cWer == null : that.cWer
				.equals(this.cWer))) {
			return false;
		}
		if (!(that.cWas == null ? this.cWas == null : that.cWas
				.equals(this.cWas))) {
			return false;
		}
		if (!(that.tWann == null ? this.tWann == null : that.tWann
				.equals(this.tWann))) {
			return false;
		}
		if (!(that.personalIIdLocker == null ? this.personalIIdLocker == null
				: that.personalIIdLocker.equals(this.personalIIdLocker))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.cWer.hashCode();
		result = 37 * result + this.cWas.hashCode();
		result = 37 * result + this.tWann.hashCode();
		result = 37 * result + this.personalIIdLocker.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += cWer;
		returnString += ", " + cWas;
		returnString += ", " + tWann;
		returnString += ", " + personalIIdLocker;
		return returnString;
	}
}

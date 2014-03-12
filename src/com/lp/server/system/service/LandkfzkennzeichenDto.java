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
package com.lp.server.system.service;

import java.io.Serializable;

public class LandkfzkennzeichenDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String cLkz;
	private String cKfzkennzeichen;

	public String getCLkz() {
		return cLkz;
	}

	public void setCLkz(String cLkz) {
		this.cLkz = cLkz;
	}

	public String getCKfzkennzeichen() {
		return cKfzkennzeichen;
	}

	public void setCKfzkennzeichen(String cKfzkennzeichen) {
		this.cKfzkennzeichen = cKfzkennzeichen;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof LandkfzkennzeichenDto))
			return false;
		LandkfzkennzeichenDto that = (LandkfzkennzeichenDto) obj;
		if (!(that.cLkz == null ? this.cLkz == null : that.cLkz
				.equals(this.cLkz)))
			return false;
		if (!(that.cKfzkennzeichen == null ? this.cKfzkennzeichen == null
				: that.cKfzkennzeichen.equals(this.cKfzkennzeichen)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.cLkz.hashCode();
		result = 37 * result + this.cKfzkennzeichen.hashCode();
		return result;
	}

	public String toString() {
		StringBuffer returnStringBuffer = new StringBuffer(64);
		returnStringBuffer.append("[");
		returnStringBuffer.append("cLkz:").append(cLkz);
		returnStringBuffer.append("cKfzkennzeichen:").append(cKfzkennzeichen);
		returnStringBuffer.append("]");
		return returnStringBuffer.toString();
	}
}

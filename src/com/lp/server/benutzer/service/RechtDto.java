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

public class RechtDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cNr;
	private String xKommentar;

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public String getXKommentar() {
		return xKommentar;
	}

	public void setXKommentar(String xKommentar) {
		this.xKommentar = xKommentar;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof RechtDto))
			return false;
		RechtDto that = (RechtDto) obj;
		if (!(that.cNr == null ? this.cNr == null : that.cNr.equals(this.cNr)))
			return false;
		if (!(that.xKommentar == null ? this.xKommentar == null
				: that.xKommentar.equals(this.xKommentar)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.cNr.hashCode();
		result = 37 * result + this.xKommentar.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += cNr;
		returnString += ", " + xKommentar;
		return returnString;
	}
}

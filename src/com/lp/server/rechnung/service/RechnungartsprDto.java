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
package com.lp.server.rechnung.service;

import java.io.Serializable;

/**
 * <p>
 * <I></I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum <I>6. 8. 2004</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version 1.0
 */
public class RechnungartsprDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String rechnungartCNr;
	private String spracheCNr;
	private String cBez;

	public String getRechnungartCNr() {
		return rechnungartCNr;
	}

	public void setRechnungartCNr(String rechnungartCNr) {
		this.rechnungartCNr = rechnungartCNr;
	}

	public String getSpracheCNr() {
		return spracheCNr;
	}

	public void setSpracheCNr(String spracheCNr) {
		this.spracheCNr = spracheCNr;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof RechnungartsprDto)) {
			return false;
		}
		RechnungartsprDto that = (RechnungartsprDto) obj;
		if (!(that.rechnungartCNr == null ? this.rechnungartCNr == null
				: that.rechnungartCNr.equals(this.rechnungartCNr))) {
			return false;
		}
		if (!(that.spracheCNr == null ? this.spracheCNr == null
				: that.spracheCNr.equals(this.spracheCNr))) {
			return false;
		}
		if (!(that.cBez == null ? this.cBez == null : that.cBez
				.equals(this.cBez))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.rechnungartCNr.hashCode();
		result = 37 * result + this.spracheCNr.hashCode();
		result = 37 * result + this.cBez.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += rechnungartCNr;
		returnString += ", " + spracheCNr;
		returnString += ", " + cBez;
		return returnString;
	}
}

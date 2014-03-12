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
package com.lp.server.bestellung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class BestellvorschlagUeberleitungDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Timestamp tLiefertermin;
	private BigDecimal nMenge;
	private String cBelegartKurzbez;
	private Integer iBelegartId;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getIBelegartId() {
		return iBelegartId;
	}

	public void setIBelegartId(Integer iBelegartId) {
		this.iBelegartId = iBelegartId;
	}

	public String getCBelegartBez() {
		return cBelegartKurzbez;
	}

	public void setCBelegartBez(String cBelegartKurzbezeichnung) {
		this.cBelegartKurzbez = cBelegartKurzbezeichnung;
	}

	public Timestamp getTLiefertermin() {
		return tLiefertermin;
	}

	public void setTLiefertermin(Timestamp tLiefertermin) {
		this.tLiefertermin = tLiefertermin;
	}

	public BigDecimal getNMenge() {
		return nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BestellvorschlagUeberleitungDto)) {
			return false;
		}
		BestellvorschlagUeberleitungDto that = (BestellvorschlagUeberleitungDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.tLiefertermin == null ? this.tLiefertermin == null
				: that.tLiefertermin.equals(this.tLiefertermin))) {
			return false;
		}
		if (!(that.nMenge == null ? this.nMenge == null : that.nMenge
				.equals(this.nMenge))) {
			return false;
		}
		if (!(that.cBelegartKurzbez == null ? this.cBelegartKurzbez == null
				: that.cBelegartKurzbez.equals(this.cBelegartKurzbez))) {
			return false;
		}
		if (!(that.iBelegartId == null ? this.iBelegartId == null
				: that.iBelegartId.equals(this.iBelegartId))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.tLiefertermin.hashCode();
		result = 37 * result + this.nMenge.hashCode();
		result = 37 * result + this.cBelegartKurzbez.hashCode();
		result = 37 * result + this.iBelegartId.hashCode();

		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + tLiefertermin;
		returnString += ", " + tLiefertermin;
		returnString += ", " + nMenge;
		returnString += ", " + cBelegartKurzbez;
		returnString += ", " + iBelegartId;
		return returnString;
	}
}

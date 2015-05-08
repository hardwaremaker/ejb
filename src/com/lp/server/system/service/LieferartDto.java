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

import com.lp.service.DatenspracheIf;

public class LieferartDto implements Serializable, DatenspracheIf {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private Integer iId;
	private String cNr;
	private Short bFrachtkostenalserledigtverbuchen;
	private String cVersandort;
	private String mandantCNr;

	public Integer getIId() {
		return iId;
	}

	private LieferartsprDto lieferartsprDto = null;
	private Short bVersteckt;

	public String formatBez() {
		if (getLieferartsprDto() != null) {
			if (getLieferartsprDto().getCBezeichnung() != null) {
				return getLieferartsprDto().getCBezeichnung();
			} else {
				return getCNr().trim();
			}
		} else {
			return getCNr().trim();
		}
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public Short getBFrachtkostenalserledigtverbuchen() {
		return bFrachtkostenalserledigtverbuchen;
	}

	public void setBFrachtkostenalserledigtverbuchen(
			Short bFrachtkostenalserledigtverbuchen) {
		this.bFrachtkostenalserledigtverbuchen = bFrachtkostenalserledigtverbuchen;
	}

	public String getCVersandort() {
		return cVersandort;
	}

	public LieferartsprDto getLieferartsprDto() {
		return lieferartsprDto;
	}

	public Short getBVersteckt() {
		return bVersteckt;
	}

	public void setCVersandort(String cVersandort) {
		this.cVersandort = cVersandort;
	}

	public void setLieferartsprDto(LieferartsprDto lieferartsprDto) {
		this.lieferartsprDto = lieferartsprDto;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof LieferartDto)) {
			return false;
		}
		LieferartDto that = (LieferartDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.cNr == null ? this.cNr == null : that.cNr.equals(this.cNr))) {
			return false;
		}
		if (!(that.bFrachtkostenalserledigtverbuchen == null ? this.bFrachtkostenalserledigtverbuchen == null
				: that.bFrachtkostenalserledigtverbuchen
						.equals(this.bFrachtkostenalserledigtverbuchen))) {
			return false;
		}
		if (!(that.cVersandort == null ? this.cVersandort == null
				: that.cVersandort.equals(this.cVersandort))) {
			return false;
		}
		if (!(that.mandantCNr == null ? this.mandantCNr == null
				: that.mandantCNr.equals(this.mandantCNr))) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.cNr.hashCode();
		result = 37 * result
				+ this.bFrachtkostenalserledigtverbuchen.hashCode();
		result = 37 * result + this.cVersandort.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + cNr;
		returnString += ", " + bFrachtkostenalserledigtverbuchen;
		returnString += ", " + cVersandort;
		returnString += ", " + mandantCNr;
		return returnString;
	}
}

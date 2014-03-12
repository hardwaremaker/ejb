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
package com.lp.server.artikel.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.lp.util.Helper;

public class ArtikelreservierungDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iBelegartpositionid;
	private Integer artikelIId;
	private Timestamp tLiefertermin;
	private Integer iId;
	private BigDecimal nMenge;

	public Integer getIBelegartpositionid() {
		return iBelegartpositionid;
	}

	private String cBelegartnr;

	public void setIBelegartpositionid(Integer iBelegartpositionid) {
		this.iBelegartpositionid = iBelegartpositionid;
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
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

	public Integer getIId() {
		return iId;
	}

	public String getCBelegartnr() {
		return cBelegartnr;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = Helper.rundeKaufmaennisch(nMenge, 4);
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public void setCBelegartnr(String cBelegartnr) {
		this.cBelegartnr = cBelegartnr;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ArtikelreservierungDto)) {
			return false;
		}
		ArtikelreservierungDto that = (ArtikelreservierungDto) obj;
		if (!(that.iBelegartpositionid == null ? this.iBelegartpositionid == null
				: that.iBelegartpositionid.equals(this.iBelegartpositionid))) {
			return false;
		}
		if (!(that.artikelIId == null ? this.artikelIId == null
				: that.artikelIId.equals(this.artikelIId))) {
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
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iBelegartpositionid.hashCode();
		result = 37 * result + this.artikelIId.hashCode();
		result = 37 * result + this.tLiefertermin.hashCode();
		result = 37 * result + this.nMenge.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iBelegartpositionid;
		returnString += ", " + artikelIId;
		returnString += ", " + tLiefertermin;
		returnString += ", " + nMenge;
		return returnString;
	}
}

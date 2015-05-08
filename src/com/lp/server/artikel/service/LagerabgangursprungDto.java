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
package com.lp.server.artikel.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class LagerabgangursprungDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iLagerbewegungid;
	private Integer iLagerbewegungidursprung;
	private BigDecimal nVerbrauchtemenge;
	private BigDecimal nGestehungspreis;
	private String versionAusUrsprung;

	public String getVersionAusUrsprung() {
		return versionAusUrsprung;
	}

	public void setVersionAusUrsprung(String versionAusUrsprung) {
		this.versionAusUrsprung = versionAusUrsprung;
	}

	public Integer getILagerbewegungid() {
		return iLagerbewegungid;
	}

	public void setILagerbewegungid(Integer iLagerbewegungid) {
		this.iLagerbewegungid = iLagerbewegungid;
	}

	public Integer getILagerbewegungidursprung() {
		return iLagerbewegungidursprung;
	}

	public void setILagerbewegungidursprung(Integer iLagerbewegungidursprung) {
		this.iLagerbewegungidursprung = iLagerbewegungidursprung;
	}

	public BigDecimal getNVerbrauchtemenge() {
		return nVerbrauchtemenge;
	}

	public BigDecimal getNGestehungspreis() {
		return nGestehungspreis;
	}

	public void setNVerbrauchtemenge(BigDecimal nVerbrauchtemenge) {
		/**
		 * @todo MB->CK runden!!!! Mandantenparameter kommt PJ 4370
		 */
		this.nVerbrauchtemenge = nVerbrauchtemenge.setScale(4, BigDecimal.ROUND_HALF_EVEN);
	}

	public void setNGestehungspreis(BigDecimal nGestehungspreis) {
		this.nGestehungspreis = nGestehungspreis;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof LagerabgangursprungDto)) {
			return false;
		}
		LagerabgangursprungDto that = (LagerabgangursprungDto) obj;
		if (!(that.iLagerbewegungid == null ? this.iLagerbewegungid == null
				: that.iLagerbewegungid.equals(this.iLagerbewegungid))) {
			return false;
		}
		if (!(that.iLagerbewegungidursprung == null ? this.iLagerbewegungidursprung == null
				: that.iLagerbewegungidursprung
						.equals(this.iLagerbewegungidursprung))) {
			return false;
		}
		if (!(that.nVerbrauchtemenge == null ? this.nVerbrauchtemenge == null
				: that.nVerbrauchtemenge.equals(this.nVerbrauchtemenge))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iLagerbewegungid.hashCode();
		result = 37 * result + this.iLagerbewegungidursprung.hashCode();
		result = 37 * result + this.nVerbrauchtemenge.hashCode();
		result = 37 * result + this.nGestehungspreis.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += "iLagerbewegungid: " + iLagerbewegungid;
		returnString += ",iLagerbewegungidursprung: "
				+ iLagerbewegungidursprung;
		returnString += ",nVerbrauchtemenge: " + nVerbrauchtemenge;
		returnString += ",nGestehungspreis: " + nGestehungspreis;
		return returnString;
	}
}

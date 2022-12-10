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

import com.lp.util.Helper;

public class MwstsatzDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Double fMwstsatz;
	private Integer iMwstsatzbezId;
	private Timestamp dGueltigab;
	private MwstsatzbezDto mwstsatzbezDto;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getIIMwstsatzbezId() {
		return iMwstsatzbezId;
	}

	public void setIIMwstsatzbezId(Integer iMwstsatzbezId) {
		this.iMwstsatzbezId = iMwstsatzbezId;
	}

	public Timestamp getDGueltigab() {
		return dGueltigab;
	}

	public void setDGueltigab(Timestamp dGueltigab) {
		this.dGueltigab = dGueltigab;
	}

	public Double getFMwstsatz() {
		return fMwstsatz;
	}

	public MwstsatzbezDto getMwstsatzbezDto() {
		return mwstsatzbezDto;
	}

	public void setFMwstsatz(Double fMwstsatz) {
		this.fMwstsatz = fMwstsatz;
	}

	public void setMwstsatzbezDto(MwstsatzbezDto mwstsatzbezDto) {
		this.mwstsatzbezDto = mwstsatzbezDto;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof MwstsatzDto)) {
			return false;
		}
		MwstsatzDto that = (MwstsatzDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.dGueltigab == null ? this.dGueltigab == null : that.dGueltigab.equals(this.dGueltigab))) {
			return false;
		}
		if (!(that.iMwstsatzbezId == null ? this.iMwstsatzbezId == null
				: that.iMwstsatzbezId.equals(this.iMwstsatzbezId))) {
			return false;
		}
		if (!(that.fMwstsatz == null ? this.fMwstsatz == null : that.fMwstsatz.equals(this.fMwstsatz))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.iMwstsatzbezId.hashCode();
		result = 37 * result + this.dGueltigab.hashCode();
		result = 37 * result + this.fMwstsatz.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + iMwstsatzbezId;
		returnString += ", " + dGueltigab;
		returnString += ", " + fMwstsatz;
		return returnString;
	}

	public String formatMwstsatz(TheClientDto theClientDto) {

		StringBuffer s = new StringBuffer();

		if (getMwstsatzbezDto() != null) {
			s.append(getMwstsatzbezDto().getCBezeichnung());
		}
		s.append(" " + Helper.formatDatum(dGueltigab, theClientDto.getLocUi()));
		s.append(" " + Helper.formatZahl(getFMwstsatz(), 1, theClientDto.getLocUi()));
		s.append("%");
		return s.toString();
	}

}

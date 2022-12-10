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
package com.lp.server.auftrag.service;

import java.io.Serializable;
import java.sql.Timestamp;

public class AuftragseriennrnDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer artikelIId;
	private Integer auftragpositionIId;
	private String cSeriennr;
	private Integer iSort;
	private String cKommentar;
	private Integer iVersionNr;
	private Timestamp tAnlegen;
	private Integer personalIIdAnlegen;
	
	private String auftragCNr_NOT_IN_DB;

	public String getAuftragCNr_NOT_IN_DB() {
		return auftragCNr_NOT_IN_DB;
	}

	public void setAuftragCNr_NOT_IN_DB(String auftragCNr_NOT_IN_DB) {
		this.auftragCNr_NOT_IN_DB = auftragCNr_NOT_IN_DB;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Integer getAuftragpositionIId() {
		return auftragpositionIId;
	}

	public void setAuftragpositionIId(Integer auftragpositionIId) {
		this.auftragpositionIId = auftragpositionIId;
	}

	public String getCSeriennr() {
		return cSeriennr;
	}

	public Integer getISort() {
		return iSort;
	}

	public String getCKommentar() {
		return cKommentar;
	}

	public void setCSeriennr(String cSeriennr) {
		this.cSeriennr = cSeriennr;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public void setCKommentar(String cKommentar) {
		this.cKommentar = cKommentar;
	}
	
	public Timestamp getTAnlegen() {
		return this.tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}
	
	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}
	
	public Integer getIVersionNr() {
		return this.iVersionNr;
	}

	public void setIVersionNr(Integer iVersionNr) {
		this.iVersionNr = iVersionNr;
	}


	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof AuftragseriennrnDto))
			return false;
		AuftragseriennrnDto that = (AuftragseriennrnDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.artikelIId == null ? this.artikelIId == null
				: that.artikelIId.equals(this.artikelIId)))
			return false;
		if (!(that.auftragpositionIId == null ? this.auftragpositionIId == null
				: that.auftragpositionIId.equals(this.auftragpositionIId)))
			return false;
		if (!(that.cSeriennr == null ? this.cSeriennr == null : that.cSeriennr
				.equals(this.cSeriennr)))
			return false;
		if (!(that.cKommentar == null ? this.cKommentar == null
				: that.cKommentar.equals(this.cKommentar)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.artikelIId.hashCode();
		result = 37 * result + this.auftragpositionIId.hashCode();
		result = 37 * result + this.cSeriennr.hashCode();
		result = 37 * result + this.cKommentar.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += getIId();
		returnString += ", " + getArtikelIId();
		returnString += ", " + getAuftragpositionIId();
		returnString += ", " + getCSeriennr();
		returnString += ", " + getCKommentar();
		return returnString;
	}
}

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

public class ArtikelkommentarDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer artikelIId;
	private Integer artikelkommentarartIId;
	private ArtikelkommentarsprDto artikelkommentarsprDto;
	private ArtikelkommentardruckDto[] artikelkommentardruckDto;
	private Integer iSort;
	
	
	public Integer getIId() {
		return iId;
	}

	private String datenformatCNr;
	private Short bDefaultbild;
	private Integer iArt;

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Integer getArtikelkommentarartIId() {
		return artikelkommentarartIId;
	}

	public ArtikelkommentarsprDto getArtikelkommentarsprDto() {
		return artikelkommentarsprDto;
	}

	public ArtikelkommentardruckDto[] getArtikelkommentardruckDto() {
		return artikelkommentardruckDto;
	}

	public String getDatenformatCNr() {
		return datenformatCNr;
	}

	public Short getBDefaultbild() {
		return bDefaultbild;
	}

	public Integer getIArt() {
		return this.iArt;
	}

	public void setIArt(Integer iArt) {
		this.iArt = iArt;
	}


	public void setArtikelkommentarartIId(Integer artikelkommentarartIId) {
		this.artikelkommentarartIId = artikelkommentarartIId;
	}

	public void setArtikelkommentarsprDto(
			ArtikelkommentarsprDto artikelkommentarsprDto) {
		this.artikelkommentarsprDto = artikelkommentarsprDto;
	}

	public void setArtikelkommentardruckDto(
			ArtikelkommentardruckDto[] artikelkommentardruckDto) {
		this.artikelkommentardruckDto = artikelkommentardruckDto;
	}

	public void setDatenformatCNr(String datenformatCNr) {
		this.datenformatCNr = datenformatCNr;
	}

	public void setBDefaultbild(Short bDefaultbild) {
		this.bDefaultbild = bDefaultbild;
	}

	public Integer getISort() {
		return iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ArtikelkommentarDto))
			return false;
		ArtikelkommentarDto that = (ArtikelkommentarDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.artikelIId == null ? this.artikelIId == null
				: that.artikelIId.equals(this.artikelIId)))
			return false;
		if (!(that.artikelkommentarartIId == null ? this.artikelkommentarartIId == null
				: that.artikelkommentarartIId
						.equals(this.artikelkommentarartIId)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.artikelIId.hashCode();
		result = 37 * result + this.artikelkommentarartIId.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + artikelIId;
		returnString += ", " + artikelkommentarartIId;
		return returnString;
	}
}

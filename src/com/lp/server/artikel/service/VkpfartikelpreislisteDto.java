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


public class VkpfartikelpreislisteDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String mandantCNr;
	private Integer iSort;
	private String cNr;
	private Short bPreislisteaktiv;
	private String waehrungCNr;
	private String cFremdsystemnr;
	
	
	
	private Integer webshopIId;
	
	
	public Integer getWebshopIId() {
		return webshopIId;
	}

	public void setWebshopIId(Integer webshopIId) {
		this.webshopIId = webshopIId;
	}

	
	private BigDecimal nStandardrabattsatz;
	
	public BigDecimal getNStandardrabattsatz() {
		return nStandardrabattsatz;
	}

	public void setNStandardrabattsatz(BigDecimal standardrabattsatz) {
		nStandardrabattsatz = standardrabattsatz;
	}

	public String getCFremdsystemnr() {
		return cFremdsystemnr;
	}

	public void setCFremdsystemnr(String fremdsystemnr) {
		cFremdsystemnr = fremdsystemnr;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Integer getISort() {
		return iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public Short getBPreislisteaktiv() {
		return bPreislisteaktiv;
	}

	public void setBPreislisteaktiv(Short bPreislisteaktiv) {
		this.bPreislisteaktiv = bPreislisteaktiv;
	}

	public boolean isPreislisteaktiv() {
		if(null == bPreislisteaktiv) return false ;
		return bPreislisteaktiv == (short) 1 ;
	}
	
	public String getWaehrungCNr() {
		return waehrungCNr;
	}

	public void setWaehrungCNr(String waehrungCNr) {
		this.waehrungCNr = waehrungCNr;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof VkpfartikelpreislisteDto))
			return false;
		VkpfartikelpreislisteDto that = (VkpfartikelpreislisteDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.mandantCNr == null ? this.mandantCNr == null
				: that.mandantCNr.equals(this.mandantCNr)))
			return false;
		if (!(that.iSort == null ? this.iSort == null : that.iSort
				.equals(this.iSort)))
			return false;
		if (!(that.cNr == null ? this.cNr == null : that.cNr.equals(this.cNr)))
			return false;
		if (!(that.bPreislisteaktiv == null ? this.bPreislisteaktiv == null
				: that.bPreislisteaktiv.equals(this.bPreislisteaktiv)))
			return false;
		if (!(that.waehrungCNr == null ? this.waehrungCNr == null
				: that.waehrungCNr.equals(this.waehrungCNr)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.iSort.hashCode();
		result = 37 * result + this.cNr.hashCode();
		result = 37 * result + this.bPreislisteaktiv.hashCode();
		result = 37 * result + this.waehrungCNr.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + mandantCNr;
		returnString += ", " + iSort;
		returnString += ", " + cNr;
		returnString += ", " + bPreislisteaktiv;
		returnString += ", " + waehrungCNr;
		return returnString;
	}
}

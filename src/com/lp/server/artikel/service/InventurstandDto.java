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

public class InventurstandDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer inventurIId;
	private Integer artikelIId;
	private Integer lagerIId;
	private BigDecimal nInventurmenge;
	private BigDecimal nInventurpreis;
	private BigDecimal nAbgewerteterpreis;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getInventurIId() {
		return inventurIId;
	}

	public void setInventurIId(Integer inventurIId) {
		this.inventurIId = inventurIId;
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Integer getLagerIId() {
		return lagerIId;
	}

	public void setLagerIId(Integer lagerIId) {
		this.lagerIId = lagerIId;
	}

	public BigDecimal getNInventurmenge() {
		return nInventurmenge;
	}

	public void setNInventurmenge(BigDecimal nInventurmenge) {
		this.nInventurmenge = nInventurmenge;
	}

	public BigDecimal getNInventurpreis() {
		return nInventurpreis;
	}

	public void setNInventurpreis(BigDecimal nInventurpreis) {
		this.nInventurpreis = nInventurpreis;
	}

	public BigDecimal getNAbgewerteterpreis() {
		return nAbgewerteterpreis;
	}

	public void setNAbgewerteterpreis(BigDecimal nAbgewerteterpreis) {
		this.nAbgewerteterpreis = nAbgewerteterpreis;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof InventurstandDto))
			return false;
		InventurstandDto that = (InventurstandDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.inventurIId == null ? this.inventurIId == null
				: that.inventurIId.equals(this.inventurIId)))
			return false;
		if (!(that.artikelIId == null ? this.artikelIId == null
				: that.artikelIId.equals(this.artikelIId)))
			return false;
		if (!(that.lagerIId == null ? this.lagerIId == null : that.lagerIId
				.equals(this.lagerIId)))
			return false;
		if (!(that.nInventurmenge == null ? this.nInventurmenge == null
				: that.nInventurmenge.equals(this.nInventurmenge)))
			return false;
		if (!(that.nInventurpreis == null ? this.nInventurpreis == null
				: that.nInventurpreis.equals(this.nInventurpreis)))
			return false;
		if (!(that.nAbgewerteterpreis == null ? this.nAbgewerteterpreis == null
				: that.nAbgewerteterpreis.equals(this.nAbgewerteterpreis)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.inventurIId.hashCode();
		result = 37 * result + this.artikelIId.hashCode();
		result = 37 * result + this.lagerIId.hashCode();
		result = 37 * result + this.nInventurmenge.hashCode();
		result = 37 * result + this.nInventurpreis.hashCode();
		result = 37 * result + this.nAbgewerteterpreis.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + inventurIId;
		returnString += ", " + artikelIId;
		returnString += ", " + lagerIId;
		returnString += ", " + nInventurmenge;
		returnString += ", " + nInventurpreis;
		returnString += ", " + nAbgewerteterpreis;
		return returnString;
	}
}

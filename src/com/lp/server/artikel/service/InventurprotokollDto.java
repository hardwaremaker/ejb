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
import java.sql.Timestamp;

public class InventurprotokollDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer inventurIId;
	private Integer iId;
	private Integer inventurlisteIId;
	private Timestamp tZeitpunkt;
	private BigDecimal nKorrekturmenge;
	private Timestamp tAendern;
	private Integer personalIIdAendern;

	public Integer getIId() {
		return iId;
	}

	private BigDecimal nInventurpreis;

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getInventurlisteIId() {
		return inventurlisteIId;
	}

	public void setInventurlisteIId(Integer inventurlisteIId) {
		this.inventurlisteIId = inventurlisteIId;
	}

	public Timestamp getTZeitpunkt() {
		return tZeitpunkt;
	}

	public void setTZeitpunkt(Timestamp tZeitpunkt) {
		this.tZeitpunkt = tZeitpunkt;
	}

	public BigDecimal getNKorrekturmenge() {
		return nKorrekturmenge;
	}

	public void setNKorrekturmenge(BigDecimal nKorrekturmenge) {
		this.nKorrekturmenge = nKorrekturmenge;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public Integer getInventurIId() {
		return inventurIId;
	}

	public BigDecimal getNInventurpreis() {
		return nInventurpreis;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public void setInventurIId(Integer inventurIId) {
		this.inventurIId = inventurIId;
	}

	public void setNInventurpreis(BigDecimal nInventurpreis) {
		this.nInventurpreis = nInventurpreis;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof InventurprotokollDto))
			return false;
		InventurprotokollDto that = (InventurprotokollDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.inventurlisteIId == null ? this.inventurlisteIId == null
				: that.inventurlisteIId.equals(this.inventurlisteIId)))
			return false;
		if (!(that.tZeitpunkt == null ? this.tZeitpunkt == null
				: that.tZeitpunkt.equals(this.tZeitpunkt)))
			return false;
		if (!(that.nKorrekturmenge == null ? this.nKorrekturmenge == null
				: that.nKorrekturmenge.equals(this.nKorrekturmenge)))
			return false;
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.inventurlisteIId.hashCode();
		result = 37 * result + this.tZeitpunkt.hashCode();
		result = 37 * result + this.nKorrekturmenge.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + inventurlisteIId;
		returnString += ", " + tZeitpunkt;
		returnString += ", " + nKorrekturmenge;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdAendern;
		return returnString;
	}
}

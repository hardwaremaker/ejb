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

public class ArtikelkommentarartDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String cNr;
	private Integer brancheId;

	public Integer getBrancheId() {
		return brancheId;
	}

	public void setBrancheId(Integer brancheId) {
		this.brancheId = brancheId;
	}

	public Integer getIId() {
		return iId;
	}

	private Short bWebshop;

	public Short getBWebshop() {
		return this.bWebshop;
	}

	private Short bTooltip;

	public Short getBTooltip() {
		return bTooltip;
	}

	public void setBTooltip(Short bTooltip) {
		this.bTooltip = bTooltip;
	}
	private Short bDetail;

	public Short getBDetail() {
		return bDetail;
	}

	public void setBDetail(Short bDetail) {
		this.bDetail = bDetail;
	}
	public String getBezeichnung() {
		if (getArtikelkommentartartsprDto() != null) {
			if (getArtikelkommentartartsprDto().getCBez() != null) {
				return getArtikelkommentartartsprDto().getCBez();
			} else {
				return getCNr().trim();
			}
		} else {
			return getCNr().trim();
		}
	}
	
	/**
	 * Ist es ein Kommentarart fuer den Webshop
	 * 
	 * @return true wenn es eine Kommentarart fuer einen Webshop ist, ansonsten
	 *         false
	 */
	public boolean isWebshop() {
		return bWebshop != null && bWebshop > 0;
	}

	public void setBWebshop(Short bWebshop) {
		this.bWebshop = bWebshop;
	}

	private ArtikelkommentarartsprDto artikelkommentartartsprDto;

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCNr() {
		return cNr;
	}

	public ArtikelkommentarartsprDto getArtikelkommentartartsprDto() {
		return artikelkommentartartsprDto;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public void setArtikelkommentartartsprDto(
			ArtikelkommentarartsprDto artikelkommentartartsprDto) {
		this.artikelkommentartartsprDto = artikelkommentartartsprDto;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ArtikelkommentarartDto))
			return false;
		ArtikelkommentarartDto that = (ArtikelkommentarartDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.cNr == null ? this.cNr == null : that.cNr.equals(this.cNr)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.cNr.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + cNr;
		return returnString;
	}
}

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
package com.lp.server.system.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class WechselkursDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String waehrungCNrVon;
	private String waehrungCNrZu;
	private Date tDatum;
	private BigDecimal nKurs;
	private Timestamp tAnlegen;
	private Integer personalIIdAnlegen;
	private Timestamp tAendern;
	private Integer personalIIdAendern;

	public String getWaehrungCNrVon() {
		return waehrungCNrVon;
	}

	public void setWaehrungCNrVon(String waehrungCNrVon) {
		this.waehrungCNrVon = waehrungCNrVon;
	}

	public String getWaehrungCNrZu() {
		return waehrungCNrZu;
	}

	public void setWaehrungCNrZu(String waehrungCNrZu) {
		this.waehrungCNrZu = waehrungCNrZu;
	}

	public Date getTDatum() {
		return tDatum;
	}

	public void setTDatum(Date tDatum) {
		this.tDatum = tDatum;
	}

	public BigDecimal getNKurs() {
		return nKurs;
	}

	public void setNKurs(BigDecimal nKurs) {
		this.nKurs = nKurs;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
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

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof WechselkursDto))
			return false;
		WechselkursDto that = (WechselkursDto) obj;
		if (!(that.waehrungCNrVon == null ? this.waehrungCNrVon == null
				: that.waehrungCNrVon.equals(this.waehrungCNrVon)))
			return false;
		if (!(that.waehrungCNrZu == null ? this.waehrungCNrZu == null
				: that.waehrungCNrZu.equals(this.waehrungCNrZu)))
			return false;
		if (!(that.tDatum == null ? this.tDatum == null : that.tDatum
				.equals(this.tDatum)))
			return false;
		if (!(that.nKurs == null ? this.nKurs == null : that.nKurs
				.equals(this.nKurs)))
			return false;
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen
				.equals(this.tAnlegen)))
			return false;
		if (!(that.personalIIdAnlegen == null ? this.personalIIdAnlegen == null
				: that.personalIIdAnlegen.equals(this.personalIIdAnlegen)))
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
		result = 37 * result + this.waehrungCNrVon.hashCode();
		result = 37 * result + this.waehrungCNrZu.hashCode();
		result = 37 * result + this.tDatum.hashCode();
		result = 37 * result + this.nKurs.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += waehrungCNrVon;
		returnString += ", " + waehrungCNrZu;
		returnString += ", " + tDatum;
		returnString += ", " + nKurs;
		returnString += ", " + tAnlegen;
		returnString += ", " + personalIIdAnlegen;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdAendern;
		return returnString;
	}
}

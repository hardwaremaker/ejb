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
package com.lp.server.projekt.service;

import java.io.Serializable;
import java.sql.Timestamp;

public class HistoryDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer personalIId;
	private Timestamp tBelegDatum;
	private String xText;
	private Integer iId;
	private Integer projektIId;
	private Integer historyartIId;
	private String cTitel;
	private Short  bHtml ;
	
	public Integer getPersonalIId() {
		return personalIId;
	}
	public Integer getHistoryartIId() {
		return historyartIId;
	}

	public void setHistoryartIId(Integer historyartIId) {
		this.historyartIId = historyartIId;
	}

	public String getCTitel() {
		return cTitel;
	}

	public void setCTitel(String cTitel) {
		this.cTitel = cTitel;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

	public Timestamp getTBelegDatum() {
		return tBelegDatum;
	}

	public void setTBelegDatum(Timestamp tBelegDatum) {
		this.tBelegDatum = tBelegDatum;
	}

	public String getXText() {
		return xText;
	}

	public void setXText(String xText) {
		this.xText = xText;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getProjektIId() {
		return projektIId;
	}

	public void setProjektIId(Integer projektIId) {
		this.projektIId = projektIId;
	}

	public Short getBHtml() {
		return bHtml;
	}
	public void setBHtml(Short bHtml) {
		this.bHtml = bHtml;
	}

	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof HistoryDto))
			return false;
		HistoryDto that = (HistoryDto) obj;
		if (!(that.personalIId == null ? this.personalIId == null
				: that.personalIId.equals(this.personalIId)))
			return false;
		if (!(that.tBelegDatum == null ? this.tBelegDatum == null
				: that.tBelegDatum.equals(this.tBelegDatum)))
			return false;
		if (!(that.xText == null ? this.xText == null : that.xText
				.equals(this.xText)))
			return false;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.projektIId == null ? this.projektIId == null
				: that.projektIId.equals(this.projektIId)))
			return false;
		if (!(that.bHtml == null ? this.bHtml == null
				: that.bHtml.equals(this.bHtml)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.personalIId.hashCode();
		result = 37 * result + this.tBelegDatum.hashCode();
		result = 37 * result + this.xText.hashCode();
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.projektIId.hashCode();
		result = 37 * result + this.bHtml.hashCode() ;
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + personalIId;
		returnString += ", " + tBelegDatum;
		returnString += ", " + xText;
		returnString += ", " + projektIId;
		return returnString;
	}
}

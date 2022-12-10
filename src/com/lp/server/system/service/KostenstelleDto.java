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

import com.lp.server.util.ICNr;

public class KostenstelleDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String mandantCNr;
	private String cNr;
	private String cBez;
	private Short bProfitcenter;
	private Integer kontoIId;
	private String xBemerkung;
	private Timestamp tAnlegen;
	private Timestamp tAendern;
	private Integer personalIIdAnlegen;
	private Integer personalIIdAendern;
	private String cSubirectory;
	private Short bVersteckt;
	private Integer lagerIIdOhneabbuchung;
	
	public Integer getLagerIIdOhneabbuchung() {
		return lagerIIdOhneabbuchung;
	}

	public void setLagerIIdOhneabbuchung(Integer lagerIIdOhneabbuchung) {
		this.lagerIIdOhneabbuchung = lagerIIdOhneabbuchung;
	}

	public String formatKostenstellenbezeichnung() {
		StringBuffer sbBez = new StringBuffer();
		sbBez.append(getCNr());
		if (getCBez() != null) {

			if (getCBez() != null && getCBez().length() > 0) {
				sbBez.append(" " + getCBez());
			}
		}

		return sbBez.toString();
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

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public Short getBProfitcenter() {
		return bProfitcenter;
	}

	public void setBProfitcenter(Short bProfitcenter) {
		this.bProfitcenter = bProfitcenter;
	}

	public Integer getKontoIId() {
		return kontoIId;
	}

	public void setKontoCNr(Integer kontoIId) {
		this.kontoIId = kontoIId;
	}

	public String getxBemerkung() {
		return xBemerkung;
	}

	public void setxBemerkung(String xBemerkung) {
		this.xBemerkung = xBemerkung;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public void setKontoIId(Integer kontoIId) {
		this.kontoIId = kontoIId;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public Short getBVersteckt() {
		return bVersteckt;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	public String getCSubdirectory() {
		return cSubirectory;
	}

	public void setCSubdirectory(String cSubirectory) {
		this.cSubirectory = cSubirectory;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof KostenstelleDto)) {
			return false;
		}
		KostenstelleDto that = (KostenstelleDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.mandantCNr == null ? this.mandantCNr == null
				: that.mandantCNr.equals(this.mandantCNr))) {
			return false;
		}
		if (!(that.cNr == null ? this.cNr == null : that.cNr.equals(this.cNr))) {
			return false;
		}
		if (!(that.cBez == null ? this.cBez == null : that.cBez
				.equals(this.cBez))) {
			return false;
		}

		if (!(that.bProfitcenter == null ? this.bProfitcenter == null
				: that.bProfitcenter.equals(this.bProfitcenter))) {
			return false;
		}
		if (!(that.kontoIId == null ? this.kontoIId == null : that.kontoIId
				.equals(this.kontoIId))) {
			return false;
		}
		if (!(that.xBemerkung == null ? this.xBemerkung == null
				: that.xBemerkung.equals(this.xBemerkung))) {
			return false;
		}
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen
				.equals(this.tAnlegen))) {
			return false;
		}
		if (!(that.personalIIdAnlegen == null ? this.personalIIdAnlegen == null
				: that.personalIIdAnlegen.equals(this.personalIIdAnlegen))) {
			return false;
		}
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern))) {
			return false;
		}
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern))) {
			return false;
		}
		if (!(that.cSubirectory == null ? this.cSubirectory == null
				: that.cSubirectory.equals(this.cSubirectory))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.cNr.hashCode();
		result = 37 * result + this.cBez.hashCode();
		result = 37 * result + this.bProfitcenter.hashCode();
		result = 37 * result + this.kontoIId.hashCode();
		result = 37 * result + this.xBemerkung.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.cSubirectory.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + mandantCNr;
		returnString += ", " + cNr;
		returnString += ", " + cBez;
		returnString += ", " + bProfitcenter;
		returnString += ", " + kontoIId;
		returnString += ", " + xBemerkung;
		returnString += ", " + tAnlegen;
		returnString += ", " + personalIIdAnlegen;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + cSubirectory;
		return returnString;
	}
}

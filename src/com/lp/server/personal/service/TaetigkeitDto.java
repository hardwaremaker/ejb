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
package com.lp.server.personal.service;

import java.io.Serializable;
import java.sql.Timestamp;

public class TaetigkeitDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iId;
	private String cNr;
	private String taetigkeitartCNr;
	private Short bBdebuchbar;
	private Short bFeiertag;
	private TaetigkeitsprDto taetigkeitsprDto;
	private Short bTagbuchbar;
	private Integer iSort;
	private Integer personalIIdAendern;
	private Timestamp tAendern;
	private Short bVersteckt;

	private String cImportkennzeichen;

	public String getCImportkennzeichen() {
		return cImportkennzeichen;
	}

	public void setCImportkennzeichen(String cImportkennzeichen) {
		this.cImportkennzeichen = cImportkennzeichen;
	}

	private Double fbezahlt;

	public Double getFBezahlt() {
		return fbezahlt;
	}

	public void setFBezahlt(Double fbezahlt) {
		this.fbezahlt = fbezahlt;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public String getTaetigkeitartCNr() {
		return taetigkeitartCNr;
	}

	public void setTaetigkeitartCNr(String taetigkeitartCNr) {
		this.taetigkeitartCNr = taetigkeitartCNr;
	}

	public Short getBBdebuchbar() {
		return bBdebuchbar;
	}

	public void setBBdebuchbar(Short bBdebuchbar) {
		this.bBdebuchbar = bBdebuchbar;
	}

	public Short getBFeiertag() {
		return bFeiertag;
	}

	public void setBFeiertag(Short bFeiertag) {
		this.bFeiertag = bFeiertag;
	}

	public Short getBTagbuchbar() {
		return bTagbuchbar;
	}

	public void setBTagbuchbar(Short bTagbuchbar) {
		this.bTagbuchbar = bTagbuchbar;
	}

	public Integer getISort() {
		return iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public TaetigkeitsprDto getTaetigkeitsprDto() {
		return taetigkeitsprDto;
	}

	public String getBezeichnung() {
		if (getTaetigkeitsprDto() != null) {
			if (getTaetigkeitsprDto().getCBez() != null) {
				return getTaetigkeitsprDto().getCBez();
			} else {
				return getCNr().trim();
			}
		} else {
			return getCNr().trim();
		}
	}

	private Short bUnterbrichtwarnmeldung;

	public Short getBUnterbrichtwarnmeldung() {
		return bUnterbrichtwarnmeldung;
	}

	public void setBUnterbrichtwarnmeldung(Short bUnterbrichtwarnmeldung) {
		this.bUnterbrichtwarnmeldung = bUnterbrichtwarnmeldung;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public void setTaetigkeitsprDto(TaetigkeitsprDto taetigkeitsprDto) {
		this.taetigkeitsprDto = taetigkeitsprDto;
	}

	private Integer iWarnmeldunginkalendertagen;

	public Integer getIWarnmeldunginkalendertagen() {
		return iWarnmeldunginkalendertagen;
	}

	public void setIWarnmeldunginkalendertagen(
			Integer iWarnmeldunginkalendertagen) {
		this.iWarnmeldunginkalendertagen = iWarnmeldunginkalendertagen;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	public Short getBVersteckt() {
		return bVersteckt;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof TaetigkeitDto))
			return false;
		TaetigkeitDto that = (TaetigkeitDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.cNr == null ? this.cNr == null : that.cNr.equals(this.cNr)))
			return false;
		if (!(that.taetigkeitartCNr == null ? this.taetigkeitartCNr == null
				: that.taetigkeitartCNr.equals(this.taetigkeitartCNr)))
			return false;
		if (!(that.bBdebuchbar == null ? this.bBdebuchbar == null
				: that.bBdebuchbar.equals(this.bBdebuchbar)))
			return false;
		if (!(that.bFeiertag == null ? this.bFeiertag == null : that.bFeiertag
				.equals(this.bFeiertag)))
			return false;
		if (!(that.bTagbuchbar == null ? this.bTagbuchbar == null
				: that.bTagbuchbar.equals(this.bTagbuchbar)))
			return false;
		if (!(that.iSort == null ? this.iSort == null : that.iSort
				.equals(this.iSort)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern)))
			return false;
		if (!(that.bVersteckt == null ? this.bVersteckt == null
				: that.bVersteckt.equals(this.bVersteckt)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.cNr.hashCode();
		result = 37 * result + this.taetigkeitartCNr.hashCode();
		result = 37 * result + this.bBdebuchbar.hashCode();
		result = 37 * result + this.bFeiertag.hashCode();
		result = 37 * result + this.bTagbuchbar.hashCode();
		result = 37 * result + this.iSort.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.bVersteckt.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + cNr;
		returnString += ", " + taetigkeitartCNr;
		returnString += ", " + bBdebuchbar;
		returnString += ", " + bFeiertag;
		returnString += ", " + bTagbuchbar;
		returnString += ", " + iSort;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + tAendern;
		return returnString;
	}
}

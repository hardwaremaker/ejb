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
package com.lp.server.personal.service;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;

public class ZeitmodelltagDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer zeitmodellIId;
	private Integer tagesartIId;
	private java.sql.Time uSollzeit;
	private Time uMindestpause;
	private Time uMindestpause2;
	private Time uMindestpause3;
	private Integer iMindestpausenanzahl;
	private Time uBeginn;
	private Time uEnde;
	private Time uAutopauseab;
	private Time uAutopauseab2;
	private Time uAutopauseab3;
	private Time uUeberstd;
	private Time uMehrstd;
	private Integer personalIIdAendern;
	private Timestamp tAendern;
	private Time uErlaubteanwesenheitszeit;
	private Integer iRundungbeginn;
	private Integer iRundungende;
	private Short bRundesondertaetigkeiten;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getZeitmodellIId() {
		return zeitmodellIId;
	}

	public void setZeitmodellIId(Integer zeitmodellIId) {
		this.zeitmodellIId = zeitmodellIId;
	}

	public Integer getTagesartIId() {
		return tagesartIId;
	}

	public void setTagesartIId(Integer tagesartIId) {
		this.tagesartIId = tagesartIId;
	}

	public Time getUMindestpause2() {
		return uMindestpause2;
	}

	public void setUMindestpause2(Time uMindestpause2) {
		this.uMindestpause2 = uMindestpause2;
	}

	public Time getUAutopauseab2() {
		return uAutopauseab2;
	}

	public void setUAutopauseab2(Time uAutopauseab2) {
		this.uAutopauseab2 = uAutopauseab2;
	}
	public Time getUMindestpause3() {
		return uMindestpause3;
	}

	public void setUMindestpause3(Time uMindestpause3) {
		this.uMindestpause3 = uMindestpause3;
	}

	public Time getUAutopauseab3() {
		return uAutopauseab3;
	}

	public void setUAutopauseab3(Time uAutopauseab3) {
		this.uAutopauseab3 = uAutopauseab3;
	}
	public java.sql.Time getUSollzeit() {
		return uSollzeit;
	}

	public void setUSollzeit(java.sql.Time uSollzeit) {
		this.uSollzeit = uSollzeit;
	}

	public Time getUMindestpause() {
		return uMindestpause;
	}

	public void setUMindestpause(Time uMindestpause) {
		this.uMindestpause = uMindestpause;
	}

	public Integer getIMindestpausenanzahl() {
		return iMindestpausenanzahl;
	}

	public void setIMindestpausenanzahl(Integer iMindestpausenanzahl) {
		this.iMindestpausenanzahl = iMindestpausenanzahl;
	}

	public Time getUBeginn() {
		return uBeginn;
	}

	public void setUBeginn(Time uBeginn) {
		this.uBeginn = uBeginn;
	}

	public Time getUEnde() {
		return uEnde;
	}

	public void setUEnde(Time uEnde) {
		this.uEnde = uEnde;
	}

	public Time getUUeberstd() {
		return uUeberstd;
	}

	public void setUUeberstd(Time uUeberstd) {
		this.uUeberstd = uUeberstd;
	}

	public Time getUMehrstd() {
		return uMehrstd;
	}

	public void setUMehrstd(Time uMehrstd) {
		this.uMehrstd = uMehrstd;
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

	public Time getUErlaubteanwesenheitszeit() {
		return uErlaubteanwesenheitszeit;
	}

	public Time getUAutopauseab() {
		return uAutopauseab;
	}

	public Integer getIRundungbeginn() {
		return iRundungbeginn;
	}

	public Integer getIRundungende() {
		return iRundungende;
	}

	public Short getBRundesondertaetigkeiten() {
		return bRundesondertaetigkeiten;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public void setUErlaubteanwesenheitszeit(Time uErlaubteanwesenheitszeit) {
		this.uErlaubteanwesenheitszeit = uErlaubteanwesenheitszeit;
	}

	public void setUAutopauseab(Time uAutopauseab) {
		this.uAutopauseab = uAutopauseab;
	}

	public void setIRundungbeginn(Integer iRundungbeginn) {
		this.iRundungbeginn = iRundungbeginn;
	}

	public void setIRundungende(Integer iRundungende) {
		this.iRundungende = iRundungende;
	}

	public void setBRundesondertaetigkeiten(Short bRundesondertaetigkeiten) {
		this.bRundesondertaetigkeiten = bRundesondertaetigkeiten;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ZeitmodelltagDto))
			return false;
		ZeitmodelltagDto that = (ZeitmodelltagDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.zeitmodellIId == null ? this.zeitmodellIId == null
				: that.zeitmodellIId.equals(this.zeitmodellIId)))
			return false;
		if (!(that.tagesartIId == null ? this.tagesartIId == null
				: that.tagesartIId.equals(this.tagesartIId)))
			return false;
		if (!(that.uSollzeit == null ? this.uSollzeit == null : that.uSollzeit
				.equals(this.uSollzeit)))
			return false;
		if (!(that.uMindestpause == null ? this.uMindestpause == null
				: that.uMindestpause.equals(this.uMindestpause)))
			return false;
		if (!(that.iMindestpausenanzahl == null ? this.iMindestpausenanzahl == null
				: that.iMindestpausenanzahl.equals(this.iMindestpausenanzahl)))
			return false;
		if (!(that.uBeginn == null ? this.uBeginn == null : that.uBeginn
				.equals(this.uBeginn)))
			return false;
		if (!(that.uEnde == null ? this.uEnde == null : that.uEnde
				.equals(this.uEnde)))
			return false;
		if (!(that.uUeberstd == null ? this.uUeberstd == null : that.uUeberstd
				.equals(this.uUeberstd)))
			return false;
		if (!(that.uMehrstd == null ? this.uMehrstd == null : that.uMehrstd
				.equals(this.uMehrstd)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.zeitmodellIId.hashCode();
		result = 37 * result + this.tagesartIId.hashCode();
		result = 37 * result + this.uSollzeit.hashCode();
		result = 37 * result + this.uMindestpause.hashCode();
		result = 37 * result + this.iMindestpausenanzahl.hashCode();
		result = 37 * result + this.uBeginn.hashCode();
		result = 37 * result + this.uEnde.hashCode();
		result = 37 * result + this.uUeberstd.hashCode();
		result = 37 * result + this.uMehrstd.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + zeitmodellIId;
		returnString += ", " + tagesartIId;
		returnString += ", " + uSollzeit;
		returnString += ", " + uMindestpause;
		returnString += ", " + iMindestpausenanzahl;
		returnString += ", " + uBeginn;
		returnString += ", " + uEnde;
		returnString += ", " + uUeberstd;
		returnString += ", " + uMehrstd;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + tAendern;
		return returnString;
	}
}

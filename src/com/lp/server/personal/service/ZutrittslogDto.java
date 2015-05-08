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
import java.sql.Timestamp;

public class ZutrittslogDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String cAusweis;
	private String cPerson;
	private String cZutrittscontroller;
	private String cZutrittsobjekt;
	private Timestamp tZeitpunkt;
	private String mandantCNr;
	private Short bErlaubt;
	private String mandantCNrObjekt;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCAusweis() {
		return cAusweis;
	}

	public void setCAusweis(String cAusweis) {
		this.cAusweis = cAusweis;
	}

	public String getCPerson() {
		return cPerson;
	}

	public void setCPerson(String cPerson) {
		this.cPerson = cPerson;
	}

	public String getCZutrittscontroller() {
		return cZutrittscontroller;
	}

	public void setCZutrittscontroller(String cZutrittscontroller) {
		this.cZutrittscontroller = cZutrittscontroller;
	}

	public String getCZutrittsobjekt() {
		return cZutrittsobjekt;
	}

	public void setCZutrittsobjekt(String cZutrittsobjekt) {
		this.cZutrittsobjekt = cZutrittsobjekt;
	}

	public Timestamp getTZeitpunkt() {
		return tZeitpunkt;
	}

	public void setTZeitpunkt(Timestamp tZeitpunkt) {
		this.tZeitpunkt = tZeitpunkt;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Short getBErlaubt() {
		return bErlaubt;
	}

	public String getMandantCNrObjekt() {
		return mandantCNrObjekt;
	}

	public void setBErlaubt(Short bErlaubt) {
		this.bErlaubt = bErlaubt;
	}

	public void setMandantCNrObjekt(String mandantCNrObjekt) {
		this.mandantCNrObjekt = mandantCNrObjekt;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ZutrittslogDto))
			return false;
		ZutrittslogDto that = (ZutrittslogDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.cAusweis == null ? this.cAusweis == null : that.cAusweis
				.equals(this.cAusweis)))
			return false;
		if (!(that.cPerson == null ? this.cPerson == null : that.cPerson
				.equals(this.cPerson)))
			return false;
		if (!(that.cZutrittscontroller == null ? this.cZutrittscontroller == null
				: that.cZutrittscontroller.equals(this.cZutrittscontroller)))
			return false;
		if (!(that.cZutrittsobjekt == null ? this.cZutrittsobjekt == null
				: that.cZutrittsobjekt.equals(this.cZutrittsobjekt)))
			return false;
		if (!(that.tZeitpunkt == null ? this.tZeitpunkt == null
				: that.tZeitpunkt.equals(this.tZeitpunkt)))
			return false;
		if (!(that.mandantCNr == null ? this.mandantCNr == null
				: that.mandantCNr.equals(this.mandantCNr)))
			return false;
		if (!(that.bErlaubt == null ? this.bErlaubt == null : that.bErlaubt
				.equals(this.bErlaubt)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.cAusweis.hashCode();
		result = 37 * result + this.cPerson.hashCode();
		result = 37 * result + this.cZutrittscontroller.hashCode();
		result = 37 * result + this.cZutrittsobjekt.hashCode();
		result = 37 * result + this.tZeitpunkt.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.bErlaubt.hashCode();
		return result;
	}

	public String toString() {
		StringBuffer returnStringBuffer = new StringBuffer(288);
		returnStringBuffer.append("[");
		returnStringBuffer.append("iId:").append(iId);
		returnStringBuffer.append("cAusweis:").append(cAusweis);
		returnStringBuffer.append("cPerson:").append(cPerson);
		returnStringBuffer.append("cZutrittscontroller:").append(
				cZutrittscontroller);
		returnStringBuffer.append("cZutrittsobjekt:").append(cZutrittsobjekt);
		returnStringBuffer.append("tZeitpunkt:").append(tZeitpunkt);
		returnStringBuffer.append("mandantCNr:").append(mandantCNr);
		returnStringBuffer.append("bErlaubt:").append(bErlaubt);
		returnStringBuffer.append("]");
		return returnStringBuffer.toString();
	}
}

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
package com.lp.server.finanz.service;

import java.io.Serializable;
import java.sql.Timestamp;

public class KontolaenderartDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer kontoIId;
	private String laenderartCNr;
	private Integer finanzamtIId;
	private String mandantCNr;
	private Integer kontoIIdUebersetzt;
	private Timestamp tAnlegen;
	private Integer personalIIdAnlegen;
	private Timestamp tAendern;
	private Integer personalIIdAendern;
	private Integer reversechargeartIId ;
	private Timestamp tGueltigAb;
	
	public Integer getIId() {
		return iId;
	}
	
	public void setIId(Integer newId) {
		this.iId = newId;
	}
	
	public Integer getKontoIId() {
		return kontoIId;
	}

	public void setKontoIId(Integer kontoIId) {
		this.kontoIId = kontoIId;
	}

	public String getLaenderartCNr() {
		return laenderartCNr;
	}

	public void setLaenderartCNr(String laenderartCNr) {
		this.laenderartCNr = laenderartCNr;
	}

	public Integer getKontoIIdUebersetzt() {
		return kontoIIdUebersetzt;
	}

	public void setKontoIIdUebersetzt(Integer kontoIIdUebersetzt) {
		this.kontoIIdUebersetzt = kontoIIdUebersetzt;
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

	public void setFinanzamtIId(Integer finanzamtIId) {
		this.finanzamtIId = finanzamtIId;
	}

	public Integer getFinanzamtIId() {
		return finanzamtIId;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof KontolaenderartDto))
			return false;
		KontolaenderartDto that = (KontolaenderartDto) obj;
		if (!(that.kontoIId == null ? this.kontoIId == null : that.kontoIId
				.equals(this.kontoIId)))
			return false;
		if (!(that.laenderartCNr == null ? this.laenderartCNr == null
				: that.laenderartCNr.equals(this.laenderartCNr)))
			return false;
		if (!(that.finanzamtIId == null ? this.finanzamtIId == null : that.finanzamtIId
				.equals(this.finanzamtIId)))
			return false;
		if (!(that.mandantCNr == null ? this.mandantCNr == null
				: that.mandantCNr.equals(this.mandantCNr)))
			return false;
		if (!(that.kontoIIdUebersetzt == null ? this.kontoIIdUebersetzt == null
				: that.kontoIIdUebersetzt.equals(this.kontoIIdUebersetzt)))
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
		if (!(that.reversechargeartIId  == null ? this.reversechargeartIId == null : that.reversechargeartIId
				.equals(this.reversechargeartIId)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.kontoIId.hashCode();
		result = 37 * result + this.laenderartCNr.hashCode();
		result = 37 * result + this.finanzamtIId.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.kontoIIdUebersetzt.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.reversechargeartIId.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += kontoIId;
		returnString += ", " + laenderartCNr;
		returnString += ", " + finanzamtIId;
		returnString += ", " + mandantCNr;
		returnString += ", " + kontoIIdUebersetzt;
		returnString += ", " + tAnlegen;
		returnString += ", " + personalIIdAnlegen;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdAendern;
		return returnString;
	}

	public Integer getReversechargeartIId() {
		return reversechargeartIId;
	}

	public void setReversechargeartIId(Integer reversechargeartId) {
		this.reversechargeartIId = reversechargeartId;
	}

	public Timestamp getTGueltigAb() {
		return tGueltigAb;
	}

	public void setTGueltigAb(Timestamp tGueltigAb) {
		this.tGueltigAb = tGueltigAb;
	}
}

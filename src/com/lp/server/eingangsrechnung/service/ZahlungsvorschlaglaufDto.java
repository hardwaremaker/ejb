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
package com.lp.server.eingangsrechnung.service;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class ZahlungsvorschlaglaufDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String mandantCNr;
	private Timestamp tAnlegen;
	private Date tZahlungsstichtag;
	private Date tNaechsterzahlungslauf;
	private Short bMitskonto;
	private Integer iSkontoueberziehungsfristintagen;
	private Integer bankverbindungIId;
	private Integer personalIIdAnlegen;

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

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Date getTZahlungsstichtag() {
		return tZahlungsstichtag;
	}

	public void setTZahlungsstichtag(Date tZahlungsstichtag) {
		this.tZahlungsstichtag = tZahlungsstichtag;
	}

	public Date getTNaechsterzahlungslauf() {
		return tNaechsterzahlungslauf;
	}

	public void setTNaechsterzahlungslauf(Date tNaechsterzahlungslauf) {
		this.tNaechsterzahlungslauf = tNaechsterzahlungslauf;
	}

	public Short getBMitskonto() {
		return bMitskonto;
	}

	public void setBMitskonto(Short bMitskonto) {
		this.bMitskonto = bMitskonto;
	}

	public Integer getISkontoueberziehungsfristintagen() {
		return iSkontoueberziehungsfristintagen;
	}

	public void setISkontoueberziehungsfristintagen(
			Integer iSkontoueberziehungsfristintagen) {
		this.iSkontoueberziehungsfristintagen = iSkontoueberziehungsfristintagen;
	}

	public Integer getBankverbindungIId() {
		return bankverbindungIId;
	}

	public void setBankverbindungIId(Integer bankverbindungIId) {
		this.bankverbindungIId = bankverbindungIId;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ZahlungsvorschlaglaufDto))
			return false;
		ZahlungsvorschlaglaufDto that = (ZahlungsvorschlaglaufDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.mandantCNr == null ? this.mandantCNr == null
				: that.mandantCNr.equals(this.mandantCNr)))
			return false;
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen
				.equals(this.tAnlegen)))
			return false;
		if (!(that.tZahlungsstichtag == null ? this.tZahlungsstichtag == null
				: that.tZahlungsstichtag.equals(this.tZahlungsstichtag)))
			return false;
		if (!(that.tNaechsterzahlungslauf == null ? this.tNaechsterzahlungslauf == null
				: that.tNaechsterzahlungslauf
						.equals(this.tNaechsterzahlungslauf)))
			return false;
		if (!(that.bMitskonto == null ? this.bMitskonto == null
				: that.bMitskonto.equals(this.bMitskonto)))
			return false;
		if (!(that.iSkontoueberziehungsfristintagen == null ? this.iSkontoueberziehungsfristintagen == null
				: that.iSkontoueberziehungsfristintagen
						.equals(this.iSkontoueberziehungsfristintagen)))
			return false;
		if (!(that.bankverbindungIId == null ? this.bankverbindungIId == null
				: that.bankverbindungIId.equals(this.bankverbindungIId)))
			return false;
		if (!(that.personalIIdAnlegen == null ? this.personalIIdAnlegen == null
				: that.personalIIdAnlegen.equals(this.personalIIdAnlegen)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.tZahlungsstichtag.hashCode();
		result = 37 * result + this.tNaechsterzahlungslauf.hashCode();
		result = 37 * result + this.bMitskonto.hashCode();
		result = 37 * result + this.iSkontoueberziehungsfristintagen.hashCode();
		result = 37 * result + this.bankverbindungIId.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		return result;
	}

	public String toString() {
		StringBuffer returnStringBuffer = new StringBuffer(288);
		returnStringBuffer.append("[");
		returnStringBuffer.append("iId:").append(iId);
		returnStringBuffer.append("mandantCNr:").append(mandantCNr);
		returnStringBuffer.append("tAnlegen:").append(tAnlegen);
		returnStringBuffer.append("tZahlungsstichtag:").append(
				tZahlungsstichtag);
		returnStringBuffer.append("tNaechsterzahlungslauf:").append(
				tNaechsterzahlungslauf);
		returnStringBuffer.append("bMitskonto:").append(bMitskonto);
		returnStringBuffer.append("iSkontoueberziehungsfristintagen:").append(
				iSkontoueberziehungsfristintagen);
		returnStringBuffer.append("bankverbindungIId:").append(
				bankverbindungIId);
		returnStringBuffer.append("personalIIdAnlegen:").append(
				personalIIdAnlegen);
		returnStringBuffer.append("]");
		return returnStringBuffer.toString();
	}
}

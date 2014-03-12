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
package com.lp.server.finanz.service;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class MahnungDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer mahnlaufIId;
	private Integer mahnstufeIId;
	private Integer rechnungIId;
	private Date tMahndatum;
	private Timestamp tGedruckt;
	private Integer personalIIdGedruckt;
	private Timestamp tAnlegen;
	private Integer personalIIdAnlegen;
	private Timestamp tAendern;
	private Integer personalIIdAendern;
	private Integer mahnstufeIIdLetztemahnstufe;
	private Date tLetztesmahndatum;
	private String mandantCNr;

	
	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getMahnlaufIId() {
		return mahnlaufIId;
	}

	public void setMahnlaufIId(Integer mahnlaufIId) {
		this.mahnlaufIId = mahnlaufIId;
	}

	public Integer getMahnstufeIId() {
		return mahnstufeIId;
	}

	public void setMahnstufeIId(Integer mahnstufeIId) {
		this.mahnstufeIId = mahnstufeIId;
	}

	public Integer getRechnungIId() {
		return rechnungIId;
	}

	public void setRechnungIId(Integer rechnungIId) {
		this.rechnungIId = rechnungIId;
	}

	public Date getTMahndatum() {
		return tMahndatum;
	}

	public void setTMahndatum(Date tMahndatum) {
		this.tMahndatum = tMahndatum;
	}

	public Timestamp getTGedruckt() {
		return tGedruckt;
	}

	public void setTGedruckt(Timestamp tGedruckt) {
		this.tGedruckt = tGedruckt;
	}

	public Integer getPersonalIIdGedruckt() {
		return personalIIdGedruckt;
	}

	public void setPersonalIIdGedruckt(Integer personalIIdGedruckt) {
		this.personalIIdGedruckt = personalIIdGedruckt;
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

	public Integer getMahnstufeIIdLetztemahnstufe() {
		return mahnstufeIIdLetztemahnstufe;
	}

	public void setMahnstufeIIdLetztemahnstufe(
			Integer mahnstufeIIdLetztemahnstufe) {
		this.mahnstufeIIdLetztemahnstufe = mahnstufeIIdLetztemahnstufe;
	}

	public Date getTLetztesmahndatum() {
		return tLetztesmahndatum;
	}

	public void setTLetztesmahndatum(Date tLetztesmahndatum) {
		this.tLetztesmahndatum = tLetztesmahndatum;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof MahnungDto))
			return false;
		MahnungDto that = (MahnungDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.mahnlaufIId == null ? this.mahnlaufIId == null
				: that.mahnlaufIId.equals(this.mahnlaufIId)))
			return false;
		if (!(that.mahnstufeIId == null ? this.mahnstufeIId == null
				: that.mahnstufeIId.equals(this.mahnstufeIId)))
			return false;
		if (!(that.rechnungIId == null ? this.rechnungIId == null
				: that.rechnungIId.equals(this.rechnungIId)))
			return false;
		if (!(that.tMahndatum == null ? this.tMahndatum == null
				: that.tMahndatum.equals(this.tMahndatum)))
			return false;
		if (!(that.tGedruckt == null ? this.tGedruckt == null : that.tGedruckt
				.equals(this.tGedruckt)))
			return false;
		if (!(that.personalIIdGedruckt == null ? this.personalIIdGedruckt == null
				: that.personalIIdGedruckt.equals(this.personalIIdGedruckt)))
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
		if (!(that.mahnstufeIIdLetztemahnstufe == null ? this.mahnstufeIIdLetztemahnstufe == null
				: that.mahnstufeIIdLetztemahnstufe
						.equals(this.mahnstufeIIdLetztemahnstufe)))
			return false;
		if (!(that.tLetztesmahndatum == null ? this.tLetztesmahndatum == null
				: that.tLetztesmahndatum.equals(this.tLetztesmahndatum)))
			return false;
		if (!(that.mandantCNr == null ? this.mandantCNr == null
				: that.mandantCNr.equals(this.mandantCNr)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.mahnlaufIId.hashCode();
		result = 37 * result + this.mahnstufeIId.hashCode();
		result = 37 * result + this.rechnungIId.hashCode();
		result = 37 * result + this.tMahndatum.hashCode();
		result = 37 * result + this.tGedruckt.hashCode();
		result = 37 * result + this.personalIIdGedruckt.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.mahnstufeIIdLetztemahnstufe.hashCode();
		result = 37 * result + this.tLetztesmahndatum.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + mahnlaufIId;
		returnString += ", " + mahnstufeIId;
		returnString += ", " + rechnungIId;
		returnString += ", " + tMahndatum;
		returnString += ", " + tGedruckt;
		returnString += ", " + personalIIdGedruckt;
		returnString += ", " + tAnlegen;
		returnString += ", " + personalIIdAnlegen;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + mahnstufeIIdLetztemahnstufe;
		returnString += ", " + tLetztesmahndatum;
		returnString += ", " + mandantCNr;
		return returnString;
	}
}

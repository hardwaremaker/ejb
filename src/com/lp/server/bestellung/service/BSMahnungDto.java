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
package com.lp.server.bestellung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class BSMahnungDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String mandantCNr;
	private Integer mahnlaufIId;
	private Integer mahnstufeIId;
	private Integer bestellungIId;
	private Date tMahndatum;
	private Timestamp tGedruckt;
	private Integer personalIIdGedruckt;
	private Timestamp tAnlegen;
	private Timestamp tAendern;
	private Integer personalIIdAnlegen;
	private Integer personalIIdAendern;
	private Integer bestellpositionIId;
	private BigDecimal nOffeneMenge;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public BigDecimal getNOffeneMenge() {
		return nOffeneMenge;
	}

	public void setNOffeneMenge(BigDecimal nOffeneMenge) {
		this.nOffeneMenge = nOffeneMenge;
	}

	public Integer getBestellpositionIId() {
		return bestellpositionIId;
	}

	public void setBestellpositionIId(Integer bestellpositionIId) {
		this.bestellpositionIId = bestellpositionIId;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
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

	public Integer getBestellungIId() {
		return bestellungIId;
	}

	public void setBestellungIId(Integer bestellungIId) {
		this.bestellungIId = bestellungIId;
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

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
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

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof BSMahnungDto))
			return false;
		BSMahnungDto that = (BSMahnungDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.mandantCNr == null ? this.mandantCNr == null
				: that.mandantCNr.equals(this.mandantCNr)))
			return false;
		if (!(that.mahnlaufIId == null ? this.mahnlaufIId == null
				: that.mahnlaufIId.equals(this.mahnlaufIId)))
			return false;
		if (!(that.mahnstufeIId == null ? this.mahnstufeIId == null
				: that.mahnstufeIId.equals(this.mahnstufeIId)))
			return false;
		if (!(that.bestellungIId == null ? this.bestellungIId == null
				: that.bestellungIId.equals(this.bestellungIId)))
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
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern)))
			return false;
		if (!(that.personalIIdAnlegen == null ? this.personalIIdAnlegen == null
				: that.personalIIdAnlegen.equals(this.personalIIdAnlegen)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		if (!(that.bestellpositionIId == null ? this.bestellpositionIId == null
				: that.bestellpositionIId.equals(this.bestellpositionIId)))
			return false;
		if (!(that.nOffeneMenge == null ? this.nOffeneMenge == null
				: that.nOffeneMenge.equals(this.nOffeneMenge)))
			return false;

		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.mahnlaufIId.hashCode();
		result = 37 * result + this.mahnstufeIId.hashCode();
		result = 37 * result + this.bestellungIId.hashCode();
		result = 37 * result + this.tMahndatum.hashCode();
		result = 37 * result + this.tGedruckt.hashCode();
		result = 37 * result + this.personalIIdGedruckt.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.bestellpositionIId.hashCode();
		result = 37 * result + this.nOffeneMenge.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + mandantCNr;
		returnString += ", " + mahnlaufIId;
		returnString += ", " + mahnstufeIId;
		returnString += ", " + bestellungIId;
		returnString += ", " + tMahndatum;
		returnString += ", " + tGedruckt;
		returnString += ", " + personalIIdGedruckt;
		returnString += ", " + tAnlegen;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdAnlegen;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + bestellpositionIId;
		returnString += ", " + nOffeneMenge;
		return returnString;
	}
}

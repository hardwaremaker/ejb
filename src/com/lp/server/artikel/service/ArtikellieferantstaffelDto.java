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
package com.lp.server.artikel.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.lp.util.Helper;

public class ArtikellieferantstaffelDto implements Serializable {
	public Short getBRabattbehalten() {
		return bRabattbehalten;
	}

	public void setBRabattbehalten(Short rabattbehalten) {
		bRabattbehalten = rabattbehalten;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer artikellieferantIId;
	private BigDecimal nMenge;
	private Double fRabatt;
	private BigDecimal nNettopreis;
	private Timestamp tPreisgueltigab;
	private Timestamp tPreisgueltigbis;
	private Short bRabattbehalten;
	private Integer personalIIdAendern;
	private Timestamp tAendern;
	private Integer iWiederbeschaffungszeit;
	
	public void setIWiederbeschaffungszeit(Integer iWiederbeschaffungszeit) {
		this.iWiederbeschaffungszeit = iWiederbeschaffungszeit;
	}

	public Integer getIWiederbeschaffungszeit() {
		return iWiederbeschaffungszeit;
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

	public void setTAendern(Timestamp aendern) {
		tAendern = aendern;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getArtikellieferantIId() {
		return artikellieferantIId;
	}

	public void setArtikellieferantIId(Integer artikellieferantIId) {
		this.artikellieferantIId = artikellieferantIId;
	}

	public BigDecimal getNMenge() {
		return nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public Double getFRabatt() {
		return fRabatt;
	}

	public void setFRabatt(Double fRabatt) {
		this.fRabatt = fRabatt;
	}

	public BigDecimal getNNettopreis() {
		return nNettopreis;
	}

	public void setNNettopreis(BigDecimal nNettopreis) {
		this.nNettopreis = nNettopreis;
	}

	public Timestamp getTPreisgueltigab() {
		return tPreisgueltigab;
	}

	public void setTPreisgueltigab(Timestamp tPreisgueltigab) {
		this.tPreisgueltigab = Helper.cutTimestamp(tPreisgueltigab);
	}

	public Timestamp getTPreisgueltigbis() {
		return tPreisgueltigbis;
	}

	public void setTPreisgueltigbis(Timestamp tPreisgueltigbis) {
		this.tPreisgueltigbis = Helper.cutTimestamp(tPreisgueltigbis);
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ArtikellieferantstaffelDto))
			return false;
		ArtikellieferantstaffelDto that = (ArtikellieferantstaffelDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.artikellieferantIId == null ? this.artikellieferantIId == null
				: that.artikellieferantIId.equals(this.artikellieferantIId)))
			return false;
		if (!(that.nMenge == null ? this.nMenge == null : that.nMenge
				.equals(this.nMenge)))
			return false;
		if (!(that.fRabatt == null ? this.fRabatt == null : that.fRabatt
				.equals(this.fRabatt)))
			return false;
		if (!(that.nNettopreis == null ? this.nNettopreis == null
				: that.nNettopreis.equals(this.nNettopreis)))
			return false;
		if (!(that.tPreisgueltigab == null ? this.tPreisgueltigab == null
				: that.tPreisgueltigab.equals(this.tPreisgueltigab)))
			return false;
		if (!(that.tPreisgueltigbis == null ? this.tPreisgueltigbis == null
				: that.tPreisgueltigbis.equals(this.tPreisgueltigbis)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.artikellieferantIId.hashCode();
		result = 37 * result + this.nMenge.hashCode();
		result = 37 * result + this.fRabatt.hashCode();
		result = 37 * result + this.nNettopreis.hashCode();
		result = 37 * result + this.tPreisgueltigab.hashCode();
		result = 37 * result + this.tPreisgueltigbis.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + artikellieferantIId;
		returnString += ", " + nMenge;
		returnString += ", " + fRabatt;
		returnString += ", " + nNettopreis;
		returnString += ", " + tPreisgueltigab;
		returnString += ", " + tPreisgueltigbis;
		return returnString;
	}
}

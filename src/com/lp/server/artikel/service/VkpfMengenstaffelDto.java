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
import java.sql.Date;
import java.sql.Timestamp;

public class VkpfMengenstaffelDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer artikelIId;
	private BigDecimal nMenge;
	private Double fArtikelstandardrabattsatz;
	private BigDecimal nArtikelfixpreis;
	private Date tPreisgueltigab;
	private Date tPreisgueltigbis;
	private Integer personalIIdAendern;
	private Timestamp tAendern;

	private Integer vkpfartikelpreislisteIId;

	public Integer getVkpfartikelpreislisteIId() {
		return vkpfartikelpreislisteIId;
	}

	public void setVkpfartikelpreislisteIId(Integer vkpfartikelpreislisteIId) {
		this.vkpfartikelpreislisteIId = vkpfartikelpreislisteIId;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public BigDecimal getNMenge() {
		return nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public Double getFArtikelstandardrabattsatz() {
		return fArtikelstandardrabattsatz;
	}

	public void setFArtikelstandardrabattsatz(Double fArtikelstandardrabattsatz) {
		this.fArtikelstandardrabattsatz = fArtikelstandardrabattsatz;
	}

	public BigDecimal getNArtikelfixpreis() {
		return nArtikelfixpreis;
	}

	public void setNArtikelfixpreis(BigDecimal nArtikelfixpreis) {
		this.nArtikelfixpreis = nArtikelfixpreis;
	}

	public Date getTPreisgueltigab() {
		return tPreisgueltigab;
	}

	public void setTPreisgueltigab(Date tPreisgueltigab) {
		this.tPreisgueltigab = tPreisgueltigab;
	}

	public Date getTPreisgueltigbis() {
		return tPreisgueltigbis;
	}

	public void setTPreisgueltigbis(Date tPreisgueltigbis) {
		this.tPreisgueltigbis = tPreisgueltigbis;
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

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	private Short bAllepreislisten;

	public Short getBAllepreislisten() {
		return bAllepreislisten;
	}

	public void setBAllepreislisten(Short bAllepreislisten) {
		this.bAllepreislisten = bAllepreislisten;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof VkpfMengenstaffelDto))
			return false;
		VkpfMengenstaffelDto that = (VkpfMengenstaffelDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.artikelIId == null ? this.artikelIId == null
				: that.artikelIId.equals(this.artikelIId)))
			return false;
		if (!(that.nMenge == null ? this.nMenge == null : that.nMenge
				.equals(this.nMenge)))
			return false;
		if (!(that.fArtikelstandardrabattsatz == null ? this.fArtikelstandardrabattsatz == null
				: that.fArtikelstandardrabattsatz
						.equals(this.fArtikelstandardrabattsatz)))
			return false;
		if (!(that.nArtikelfixpreis == null ? this.nArtikelfixpreis == null
				: that.nArtikelfixpreis.equals(this.nArtikelfixpreis)))
			return false;
		if (!(that.tPreisgueltigab == null ? this.tPreisgueltigab == null
				: that.tPreisgueltigab.equals(this.tPreisgueltigab)))
			return false;
		if (!(that.tPreisgueltigbis == null ? this.tPreisgueltigbis == null
				: that.tPreisgueltigbis.equals(this.tPreisgueltigbis)))
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
		result = 37 * result + this.artikelIId.hashCode();
		result = 37 * result + this.nMenge.hashCode();
		result = 37 * result + this.fArtikelstandardrabattsatz.hashCode();
		result = 37 * result + this.nArtikelfixpreis.hashCode();
		result = 37 * result + this.tPreisgueltigab.hashCode();
		result = 37 * result + this.tPreisgueltigbis.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + artikelIId;
		returnString += ", " + nMenge;
		returnString += ", " + fArtikelstandardrabattsatz;
		returnString += ", " + nArtikelfixpreis;
		returnString += ", " + tPreisgueltigab;
		returnString += ", " + tPreisgueltigbis;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + tAendern;
		return returnString;
	}
}

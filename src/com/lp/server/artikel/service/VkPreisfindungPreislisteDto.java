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
package com.lp.server.artikel.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class VkPreisfindungPreislisteDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer vkpfartikelpreislisteIId;
	private Integer artikelIId;
	private Date tPreisgueltigab;
	private BigDecimal nArtikelstandardrabattsatz;
	private BigDecimal nArtikelfixpreis;
	private Timestamp tAendern;
	private Integer personalIIdAendern;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getVkpfartikelpreislisteIId() {
		return vkpfartikelpreislisteIId;
	}

	public void setVkpfartikelpreislisteIId(Integer vkpfartikelpreislisteIId) {
		this.vkpfartikelpreislisteIId = vkpfartikelpreislisteIId;
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Date getTPreisgueltigab() {
		return tPreisgueltigab;
	}

	public void setTPreisgueltigab(Date tPreisgueltigab) {
		this.tPreisgueltigab = tPreisgueltigab;
	}

	public BigDecimal getNArtikelstandardrabattsatz() {
		return nArtikelstandardrabattsatz;
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

	public void setNArtikelstandardrabattsatz(
			BigDecimal nArtikelstandardrabattsatz) {
		this.nArtikelstandardrabattsatz = nArtikelstandardrabattsatz;
	}

	public BigDecimal getNArtikelfixpreis() {
		return nArtikelfixpreis;
	}

	public void setNArtikelfixpreis(BigDecimal nArtikelfixpreis) {
		this.nArtikelfixpreis = nArtikelfixpreis;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof VkPreisfindungPreislisteDto)) {
			return false;
		}
		VkPreisfindungPreislisteDto that = (VkPreisfindungPreislisteDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.vkpfartikelpreislisteIId == null ? this.vkpfartikelpreislisteIId == null
				: that.vkpfartikelpreislisteIId
						.equals(this.vkpfartikelpreislisteIId))) {
			return false;
		}
		if (!(that.artikelIId == null ? this.artikelIId == null
				: that.artikelIId.equals(this.artikelIId))) {
			return false;
		}
		if (!(that.tPreisgueltigab == null ? this.tPreisgueltigab == null
				: that.tPreisgueltigab.equals(this.tPreisgueltigab))) {
			return false;
		}
		if (!(that.nArtikelstandardrabattsatz == null ? this.nArtikelstandardrabattsatz == null
				: that.nArtikelstandardrabattsatz
						.equals(this.nArtikelstandardrabattsatz))) {
			return false;
		}
		if (!(that.nArtikelfixpreis == null ? this.nArtikelfixpreis == null
				: that.nArtikelfixpreis.compareTo(this.nArtikelfixpreis) == 0)) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.vkpfartikelpreislisteIId.hashCode();
		result = 37 * result + this.artikelIId.hashCode();
		result = 37 * result + this.tPreisgueltigab.hashCode();
		result = 37 * result + this.nArtikelstandardrabattsatz.hashCode();
		result = 37 * result + this.nArtikelfixpreis.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + vkpfartikelpreislisteIId;
		returnString += ", " + artikelIId;
		returnString += ", " + tPreisgueltigab;
		returnString += ", " + nArtikelstandardrabattsatz;
		returnString += ", " + nArtikelfixpreis;
		return returnString;
	}

	/**
	 * Eine exakte Kopie erstellen.
	 * 
	 * @throws CloneNotSupportedException
	 * @return Object
	 */
	public Object clone() {
		VkPreisfindungPreislisteDto clone = new VkPreisfindungPreislisteDto();
		clone.iId = this.iId;
		clone.vkpfartikelpreislisteIId = this.vkpfartikelpreislisteIId;
		clone.artikelIId = this.artikelIId;
		clone.tPreisgueltigab = this.tPreisgueltigab;
		clone.nArtikelstandardrabattsatz = this.nArtikelstandardrabattsatz;
		clone.nArtikelfixpreis = this.nArtikelfixpreis;
		return clone;
	}
}

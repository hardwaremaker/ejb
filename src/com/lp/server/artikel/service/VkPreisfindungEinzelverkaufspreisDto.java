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

public class VkPreisfindungEinzelverkaufspreisDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String mandantCNr;
	private Integer artikelIId;
	private BigDecimal nVerkaufspreisbasis;
	private Date tVerkaufspreisbasisgueltigab;
	private Timestamp tAendern;
	private Integer personalIIdAendern;
	
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

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
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

	
	public BigDecimal getNVerkaufspreisbasis() {
		return nVerkaufspreisbasis;
	}

	public void setNVerkaufspreisbasis(BigDecimal nVerkaufspreisbasis) {
		this.nVerkaufspreisbasis = nVerkaufspreisbasis;
	}

	public Date getTVerkaufspreisbasisgueltigab() {
		return tVerkaufspreisbasisgueltigab;
	}

	public void setTVerkaufspreisbasisgueltigab(
			Date tVerkaufspreisbasisgueltigab) {
		this.tVerkaufspreisbasisgueltigab = tVerkaufspreisbasisgueltigab;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof VkPreisfindungEinzelverkaufspreisDto)) {
			return false;
		}
		VkPreisfindungEinzelverkaufspreisDto that = (VkPreisfindungEinzelverkaufspreisDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.artikelIId == null ? this.artikelIId == null
				: that.artikelIId.equals(this.artikelIId))) {
			return false;
		}
		if (!(that.nVerkaufspreisbasis == null ? this.nVerkaufspreisbasis == null
				:
				// cloneit: vergleich fuer BigDecimal auf value and scale
				that.nVerkaufspreisbasis.compareTo(this.nVerkaufspreisbasis) == 0)) {
			return false;
		}
		if (!(that.tVerkaufspreisbasisgueltigab == null ? this.tVerkaufspreisbasisgueltigab == null
				: that.tVerkaufspreisbasisgueltigab
						.equals(this.tVerkaufspreisbasisgueltigab))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.artikelIId.hashCode();
		result = 37 * result + this.nVerkaufspreisbasis.hashCode();
		result = 37 * result + this.tVerkaufspreisbasisgueltigab.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + artikelIId;
		returnString += ", " + nVerkaufspreisbasis;
		returnString += ", " + tVerkaufspreisbasisgueltigab;
		return returnString;
	}

	/**
	 * Eine exakte Kopie erstellen.
	 * 
	 * @throws CloneNotSupportedException
	 * @return Object
	 */
	public Object clone() {
		VkPreisfindungEinzelverkaufspreisDto clone = new VkPreisfindungEinzelverkaufspreisDto();
		clone.iId = this.iId;
		clone.mandantCNr = this.mandantCNr;
		clone.artikelIId = this.artikelIId;
		clone.nVerkaufspreisbasis = this.nVerkaufspreisbasis;
		clone.tVerkaufspreisbasisgueltigab = this.tVerkaufspreisbasisgueltigab;
		return clone;
	}
}

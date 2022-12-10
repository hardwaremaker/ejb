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

import com.lp.server.system.service.HvDtoLogClass;
import com.lp.server.util.IIId;

@HvDtoLogClass(name=HvDtoLogClass.ARTIKEL_SPR, filtername=HvDtoLogClass.ARTIKEL)
public class ArtikelsprDto implements Serializable ,IIId {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer artikelIId;
	private String localeCNr;
	private String cKbez;
	private String cBez;
	private String cZbez;
	private String cZbez2;
	private Timestamp tAendern;
	private Integer personalIIdAendern;

	
	public Integer getIId() {
		return artikelIId;
	}

	public void setIId(Integer iId) {
		
	}
	
	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public String getLocaleCNr() {
		return localeCNr;
	}

	public void setLocaleCNr(String localeCNr) {
		this.localeCNr = localeCNr;
	}

	public String getCKbez() {
		return cKbez;
	}

	public void setCKbez(String cKbez) {
		this.cKbez = cKbez;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public String getCZbez() {
		return cZbez;
	}

	public void setCZbez(String cZbez) {
		this.cZbez = cZbez;
	}

	public String getCZbez2() {
		return cZbez2;
	}

	public void setCZbez2(String cZbez2) {
		this.cZbez2 = cZbez2;
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
	
	private BigDecimal nSiwert;

	public BigDecimal getNSiwert() {
		return nSiwert;
	}

	public void setNSiwert(BigDecimal nSiwert) {
		this.nSiwert = nSiwert;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ArtikelsprDto)) {
			return false;
		}
		ArtikelsprDto that = (ArtikelsprDto) obj;
		if (!(that.artikelIId == null ? this.artikelIId == null
				: that.artikelIId.equals(this.artikelIId))) {
			return false;
		}
		if (!(that.localeCNr == null ? this.localeCNr == null : that.localeCNr
				.equals(this.localeCNr))) {
			return false;
		}
		if (!(that.cKbez == null ? this.cKbez == null : that.cKbez
				.equals(this.cKbez))) {
			return false;
		}
		if (!(that.cBez == null ? this.cBez == null : that.cBez
				.equals(this.cBez))) {
			return false;
		}
		if (!(that.cZbez == null ? this.cZbez == null : that.cZbez
				.equals(this.cZbez))) {
			return false;
		}
		if (!(that.cZbez2 == null ? this.cZbez2 == null : that.cZbez2
				.equals(this.cZbez2))) {
			return false;
		}
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern))) {
			return false;
		}
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.artikelIId.hashCode();
		result = 37 * result + this.localeCNr.hashCode();
		result = 37 * result + this.cKbez.hashCode();
		result = 37 * result + this.cBez.hashCode();
		result = 37 * result + this.cZbez.hashCode();
		result = 37 * result + this.cZbez2.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += artikelIId;
		returnString += ", " + localeCNr;
		returnString += ", " + cKbez;
		returnString += ", " + cBez;
		returnString += ", " + cZbez;
		returnString += ", " + cZbez2;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdAendern;
		return returnString;
	}
}

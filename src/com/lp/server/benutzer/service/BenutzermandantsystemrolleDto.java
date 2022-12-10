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
package com.lp.server.benutzer.service;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;

import com.lp.server.system.service.HvDtoLogClass;
import com.lp.server.util.IIId;

@HvDtoLogClass(name = HvDtoLogClass.BENUTZERMANDANTSYSTEMROLLE)
public class BenutzermandantsystemrolleDto implements Serializable,IIId{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer benutzerIId;
	private Integer systemrolleIId;
	private String mandantCNr;
	private Timestamp tAnlegen;
	private Integer personalIIdAnlegen;
	private Timestamp tAendern;
	private Integer personalIIdAendern;
	private Integer personalIIdZugeordnet;
	private Integer systemrolleIIdRestapi;

	public Integer getSystemrolleIIdRestapi() {
		return systemrolleIIdRestapi;
	}

	public void setSystemrolleIIdRestapi(Integer systemrolleIIdRestapi) {
		this.systemrolleIIdRestapi = systemrolleIIdRestapi;
	}

	public Integer getSystemrolleIIdHvma() {
		return systemrolleIIdHvma;
	}

	public void setSystemrolleIIdHvma(Integer systemrolleIIdHvma) {
		this.systemrolleIIdHvma = systemrolleIIdHvma;
	}

	
	private Integer systemrolleIIdHvma;

	private SystemrolleDto systemrolleDto;
	private SystemrolleDto systemrolleDtoRestapi ;
	
	public Integer getIId() {
		return iId;
	}


	public void setIId(Integer iId) {
		this.iId = iId;
	}

	private BenutzerDto benutzerDto;

	public Integer getBenutzerIId() {
		return benutzerIId;
	}

	public void setBenutzerIId(Integer benutzerIId) {
		this.benutzerIId = benutzerIId;
	}

	public Integer getSystemrolleIId() {
		return systemrolleIId;
	}

	public void setSystemrolleIId(Integer systemrolleIId) {
		this.systemrolleIId = systemrolleIId;
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

	public SystemrolleDto getSystemrolleDto() {
		return systemrolleDto;
	}

	public BenutzerDto getBenutzerDto() {
		return benutzerDto;
	}

	public Integer getPersonalIIdZugeordnet() {
		return personalIIdZugeordnet;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public void setSystemrolleDto(SystemrolleDto systemrolleDto) {
		this.systemrolleDto = systemrolleDto;
	}

	public void setBenutzerDto(BenutzerDto benutzerDto) {
		this.benutzerDto = benutzerDto;
	}

	public void setPersonalIIdZugeordnet(Integer personalIIdZugeordnet) {
		this.personalIIdZugeordnet = personalIIdZugeordnet;
	}


	
	public SystemrolleDto getSystemrolleDtoRestapi() {
		return systemrolleDtoRestapi;
	}
	
	public void setSystemrolleDtoRestapi(SystemrolleDto systemrolleDtoRestapi) {
		this.systemrolleDtoRestapi = systemrolleDtoRestapi ;
	}
	
	
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BenutzermandantsystemrolleDto)) {
			return false;
		}
		BenutzermandantsystemrolleDto that = (BenutzermandantsystemrolleDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.benutzerIId == null ? this.benutzerIId == null
				: that.benutzerIId.equals(this.benutzerIId))) {
			return false;
		}
		if (!(that.systemrolleIId == null ? this.systemrolleIId == null
				: that.systemrolleIId.equals(this.systemrolleIId))) {
			return false;
		}
		if (!(that.mandantCNr == null ? this.mandantCNr == null
				: that.mandantCNr.equals(this.mandantCNr))) {
			return false;
		}
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen
				.equals(this.tAnlegen))) {
			return false;
		}
		if (!(that.personalIIdAnlegen == null ? this.personalIIdAnlegen == null
				: that.personalIIdAnlegen.equals(this.personalIIdAnlegen))) {
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
		if (!(that.systemrolleIIdRestapi == null ? this.systemrolleIIdRestapi == null
				: that.systemrolleIIdRestapi.equals(this.systemrolleIIdRestapi))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.benutzerIId.hashCode();
		result = 37 * result + this.systemrolleIId.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.systemrolleIIdRestapi.hashCode() ;
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + benutzerIId;
		returnString += ", " + systemrolleIId;
		returnString += ", " + systemrolleIIdRestapi;
		returnString += ", " + mandantCNr;
		returnString += ", " + tAnlegen;
		returnString += ", " + personalIIdAnlegen;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdAendern;
		return returnString;
	}
}

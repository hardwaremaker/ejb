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
package com.lp.server.system.service;

import java.io.Serializable;
import java.sql.Timestamp;

public class GeschaeftsjahrMandantDto implements Serializable {
	private static final long serialVersionUID = -5571814702094044330L;

	private Integer iId ;
	private Integer iGeschaeftsjahr;
	private Timestamp dBeginndatum;
	private Timestamp tAnlegen;
	private Integer personalIIdAnlegen;
	private Timestamp tSperre;
	private Integer personalIIdSperre;
	private String mandantCnr ;
	

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getMandantCnr() {
		return mandantCnr;
	}

	public void setMandantCnr(String mandantCnr) {
		this.mandantCnr = mandantCnr;
	}

	public Integer getIGeschaeftsjahr() {
		return iGeschaeftsjahr;
	}

	public void setIGeschaeftsjahr(Integer iGeschaeftsjahr) {
		this.iGeschaeftsjahr = iGeschaeftsjahr;
	}

	public Timestamp getDBeginndatum() {
		return dBeginndatum;
	}

	public void setDBeginndatum(Timestamp dBeginndatum) {
		this.dBeginndatum = dBeginndatum;
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

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof GeschaeftsjahrMandantDto))
			return false;
		GeschaeftsjahrMandantDto that = (GeschaeftsjahrMandantDto) obj;
		if (!(that.iGeschaeftsjahr == null ? this.iGeschaeftsjahr == null
				: that.iGeschaeftsjahr.equals(this.iGeschaeftsjahr)))
			return false;
		if (!(that.dBeginndatum == null ? this.dBeginndatum == null
				: that.dBeginndatum.equals(this.dBeginndatum)))
			return false;
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen
				.equals(this.tAnlegen)))
			return false;
		if (!(that.personalIIdAnlegen == null ? this.personalIIdAnlegen == null
				: that.personalIIdAnlegen.equals(this.personalIIdAnlegen)))
			return false;
		if (!(that.tSperre == null ? this.tSperre == null : that.tSperre
				.equals(this.tSperre)))
			return false;
		if (!(that.personalIIdSperre == null ? this.personalIIdSperre == null
				: that.personalIIdSperre.equals(this.personalIIdSperre)))
			return false;
		
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iGeschaeftsjahr.hashCode();
		result = 37 * result + this.dBeginndatum.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tSperre.hashCode();
		result = 37 * result + this.personalIIdSperre.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iGeschaeftsjahr;
		returnString += ", " + dBeginndatum;
		returnString += ", " + tAnlegen;
		returnString += ", " + personalIIdAnlegen;
		returnString += ", " + tSperre;
		returnString += ", " + personalIIdSperre;
		return returnString;
	}

	public void setTSperre(Timestamp tSperre) {
		this.tSperre = tSperre;
	}

	public Timestamp getTSperre() {
		return tSperre;
	}

	public void setPersonalIIdSperre(Integer personalIIdSperre) {
		this.personalIIdSperre = personalIIdSperre;
	}

	public Integer getPersonalIIdSperre() {
		return personalIIdSperre;
	}
}

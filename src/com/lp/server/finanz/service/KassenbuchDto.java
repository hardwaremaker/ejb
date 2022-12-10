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
package com.lp.server.finanz.service;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import com.lp.server.artikel.ejb.Material;
import com.lp.server.finanz.ejb.Konto;
import com.lp.server.system.service.HvDtoLogClass;
import com.lp.server.system.service.HvDtoLogIdCnr;
import com.lp.server.util.ICBez;
import com.lp.server.util.IIId;



@HvDtoLogClass(name = HvDtoLogClass.KASSENBUCH)
public class KassenbuchDto implements Serializable, IIId, ICBez {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String mandantCNr;
	private String cBez;
	private Integer kontoIId;
	private Short bNegativErlaubt;
	private Short bHauptkassenbuch;
	private Date dGueltigVon;
	private Date dGueltigBis;
	private Timestamp tAnlegen;
	private Integer personalIIdAnlegen;
	private Timestamp tAendern;
	private Integer personalIIdaendern;

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

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	
	@HvDtoLogIdCnr(entityClass = Konto.class)
	public Integer getKontoIId() {
		return kontoIId;
	}

	public void setKontoIId(Integer kontoIId) {
		this.kontoIId = kontoIId;
	}

	public Short getBNegativErlaubt() {
		return bNegativErlaubt;
	}

	public void setBNegativErlaubt(Short bNegativErlaubt) {
		this.bNegativErlaubt = bNegativErlaubt;
	}

	public Short getBHauptkassenbuch() {
		return bHauptkassenbuch;
	}

	public void setBHauptkassenbuch(Short bHauptkassenbuch) {
		this.bHauptkassenbuch = bHauptkassenbuch;
	}

	public Date getDGueltigVon() {
		return dGueltigVon;
	}

	public void setDGueltigVon(Date dGueltigVon) {
		this.dGueltigVon = dGueltigVon;
	}

	public Date getDGueltigBis() {
		return dGueltigBis;
	}

	public void setDGueltigBis(Date dGueltigBis) {
		this.dGueltigBis = dGueltigBis;
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

	public Integer getPersonalIIdaendern() {
		return personalIIdaendern;
	}

	public void setPersonalIIdaendern(Integer personalIIdaendern) {
		this.personalIIdaendern = personalIIdaendern;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof KassenbuchDto)) {
			return false;
		}
		KassenbuchDto that = (KassenbuchDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.mandantCNr == null ? this.mandantCNr == null
				: that.mandantCNr.equals(this.mandantCNr))) {
			return false;
		}
		if (!(that.cBez == null ? this.cBez == null : that.cBez
				.equals(this.cBez))) {
			return false;
		}
		if (!(that.kontoIId == null ? this.kontoIId == null : that.kontoIId
				.equals(this.kontoIId))) {
			return false;
		}
		if (!(that.bNegativErlaubt == null ? this.bNegativErlaubt == null
				: that.bNegativErlaubt.equals(this.bNegativErlaubt))) {
			return false;
		}
		if (!(that.bHauptkassenbuch == null ? this.bHauptkassenbuch == null
				: that.bHauptkassenbuch.equals(this.bHauptkassenbuch))) {
			return false;
		}
		if (!(that.dGueltigVon == null ? this.dGueltigVon == null
				: that.dGueltigVon.equals(this.dGueltigVon))) {
			return false;
		}
		if (!(that.dGueltigBis == null ? this.dGueltigBis == null
				: that.dGueltigBis.equals(this.dGueltigBis))) {
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
		if (!(that.personalIIdaendern == null ? this.personalIIdaendern == null
				: that.personalIIdaendern.equals(this.personalIIdaendern))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.cBez.hashCode();
		result = 37 * result + this.kontoIId.hashCode();
		result = 37 * result + this.bNegativErlaubt.hashCode();
		result = 37 * result + this.bHauptkassenbuch.hashCode();
		result = 37 * result + this.dGueltigVon.hashCode();
		result = 37 * result + this.dGueltigBis.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdaendern.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + mandantCNr;
		returnString += ", " + cBez;
		returnString += ", " + kontoIId;
		returnString += ", " + bNegativErlaubt;
		returnString += ", " + bHauptkassenbuch;
		returnString += ", " + dGueltigVon;
		returnString += ", " + dGueltigBis;
		returnString += ", " + tAnlegen;
		returnString += ", " + personalIIdAnlegen;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdaendern;
		return returnString;
	}
}

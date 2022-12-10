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
package com.lp.server.auftrag.service;

import java.io.Serializable;
import java.sql.Timestamp;

public class AuftragteilnehmerDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer iSort;
	private Integer auftragIId;
	private Integer partnerIIdAuftragteilnehmer;
	private Short bIstExternerTeilnehmer;
	private Integer auftragteilnehmerfunktionIId;
	private Timestamp tAnlegen;
	private Integer personalIIdAnlegen;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getISort() {
		return iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public Integer getAuftragIId() {
		return auftragIId;
	}

	public void setAuftragIId(Integer auftragIId) {
		this.auftragIId = auftragIId;
	}

	public Integer getPartnerIIdAuftragteilnehmer() {
		return partnerIIdAuftragteilnehmer;
	}

	public void setPartnerIIdAuftragteilnehmer(
			Integer partnerIIdAuftragteilnehmer) {
		this.partnerIIdAuftragteilnehmer = partnerIIdAuftragteilnehmer;
	}

	public Short getBIstExternerTeilnehmer() {
		return bIstExternerTeilnehmer;
	}

	public void setBIstExternerTeilnehmer(Short bIstExternerTeilnehmer) {
		this.bIstExternerTeilnehmer = bIstExternerTeilnehmer;
	}

	public Integer getAuftragteilnehmerfunktionIId() {
		return auftragteilnehmerfunktionIId;
	}

	public void setAuftragteilnehmerfunktionIId(
			Integer auftragteilnehmerfunktionIId) {
		this.auftragteilnehmerfunktionIId = auftragteilnehmerfunktionIId;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Integer getPersonalIIDAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	private Integer kostenstelleIId;

	public Integer getKostenstelleIId() {
		return this.kostenstelleIId;
	}

	public void setKostenstelleIId(Integer kostenstelleIId) {
		this.kostenstelleIId = kostenstelleIId;
	}
	
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AuftragteilnehmerDto)) {
			return false;
		}
		AuftragteilnehmerDto that = (AuftragteilnehmerDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.iSort == null ? this.iSort == null : that.iSort
				.equals(this.iSort))) {
			return false;
		}
		if (!(that.auftragIId == null ? this.auftragIId == null
				: that.auftragIId.equals(this.auftragIId))) {
			return false;
		}
		if (!(that.partnerIIdAuftragteilnehmer == null ? this.partnerIIdAuftragteilnehmer == null
				: that.partnerIIdAuftragteilnehmer
						.equals(this.partnerIIdAuftragteilnehmer))) {
			return false;
		}
		if (!(that.auftragteilnehmerfunktionIId == null ? this.auftragteilnehmerfunktionIId == null
				: that.auftragteilnehmerfunktionIId
						.equals(this.auftragteilnehmerfunktionIId))) {
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
		if (!(that.bIstExternerTeilnehmer == null ? this.bIstExternerTeilnehmer == null
				: that.bIstExternerTeilnehmer
						.equals(this.bIstExternerTeilnehmer))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.iSort.hashCode();
		result = 37 * result + this.auftragIId.hashCode();
		result = 37 * result + this.partnerIIdAuftragteilnehmer.hashCode();
		result = 37 * result + this.auftragteilnehmerfunktionIId.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.bIstExternerTeilnehmer.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + iSort;
		returnString += ", " + auftragIId;
		returnString += ", " + partnerIIdAuftragteilnehmer;
		returnString += ", " + auftragteilnehmerfunktionIId;
		returnString += ", " + tAnlegen;
		returnString += ", " + personalIIdAnlegen;
		returnString += ", " + bIstExternerTeilnehmer;
		return returnString;
	}
}

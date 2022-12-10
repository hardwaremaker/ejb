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
package com.lp.server.fertigung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class InternebestellungDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String mandantCNr;
	private String belegartCNr;
	private Integer iBelegiid;
	private Integer stuecklisteIId;
	private BigDecimal nMenge;
	private Date tLiefertermin;
	private Integer personalIIdAendern;
	private Timestamp tAendern;
	private Integer internebestellungIIdElternlos;
	private Integer iBelegpositionIId;

	private Double fLagermindest;

	public Double getFLagermindest() {
		return this.fLagermindest;
	}

	public void setFLagermindest(Double fLagermindest) {
		this.fLagermindest = fLagermindest;
	}

	private String xAusloeser;

	public String getXAusloeser() {
		return xAusloeser;
	}

	public void setXAusloeser(String xAusloeser) {
		this.xAusloeser = xAusloeser;
	}

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

	public String getBelegartCNr() {
		return belegartCNr;
	}

	public void setBelegartCNr(String belegartCNr) {
		this.belegartCNr = belegartCNr;
	}

	public Integer getIBelegiid() {
		return iBelegiid;
	}

	public void setIBelegiid(Integer iBelegiid) {
		this.iBelegiid = iBelegiid;
	}

	public Integer getStuecklisteIId() {
		return stuecklisteIId;
	}

	public void setStuecklisteIId(Integer stuecklisteIId) {
		this.stuecklisteIId = stuecklisteIId;
	}

	public BigDecimal getNMenge() {
		return nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public Date getTLiefertermin() {
		return tLiefertermin;
	}

	public void setTLiefertermin(Date tLiefertermin) {
		this.tLiefertermin = tLiefertermin;
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

	public Integer getInternebestellungIIdElternlos() {
		return internebestellungIIdElternlos;
	}

	public void setInternebestellungIIdElternlos(Integer internebestellungIIdElternlos) {
		this.internebestellungIIdElternlos = internebestellungIIdElternlos;
	}

	public Integer getIBelegpositionIId() {
		return iBelegpositionIId;
	}

	public void setIBelegpositionIId(Integer iBelegpositionIId) {
		this.iBelegpositionIId = iBelegpositionIId;
	}

	private Date tProduktionsbeginn;

	public Date getTProduktionsbeginn() {
		return tProduktionsbeginn;
	}

	public void setTProduktionsbeginn(Date tProduktionsbeginn) {
		this.tProduktionsbeginn = tProduktionsbeginn;
	}

	private Integer partnerIIdStandort;

	public Integer getPartnerIIdStandort() {
		return partnerIIdStandort;
	}

	public void setPartnerIIdStandort(Integer partnerIIdStandort) {
		this.partnerIIdStandort = partnerIIdStandort;
	}

	private Integer auftragIIdKopfauftrag;

	public Integer getAuftragIIdKopfauftrag() {
		return auftragIIdKopfauftrag;
	}

	public void setAuftragIIdKopfauftrag(Integer auftragIIdKopfauftrag) {
		this.auftragIIdKopfauftrag = auftragIIdKopfauftrag;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof InternebestellungDto))
			return false;
		InternebestellungDto that = (InternebestellungDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.mandantCNr == null ? this.mandantCNr == null : that.mandantCNr.equals(this.mandantCNr)))
			return false;
		if (!(that.belegartCNr == null ? this.belegartCNr == null : that.belegartCNr.equals(this.belegartCNr)))
			return false;
		if (!(that.iBelegiid == null ? this.iBelegiid == null : that.iBelegiid.equals(this.iBelegiid)))
			return false;
		if (!(that.stuecklisteIId == null ? this.stuecklisteIId == null
				: that.stuecklisteIId.equals(this.stuecklisteIId)))
			return false;
		if (!(that.nMenge == null ? this.nMenge == null : that.nMenge.equals(this.nMenge)))
			return false;
		if (!(that.tLiefertermin == null ? this.tLiefertermin == null : that.tLiefertermin.equals(this.tLiefertermin)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern.equals(this.tAendern)))
			return false;
		if (!(that.internebestellungIIdElternlos == null ? this.internebestellungIIdElternlos == null
				: that.internebestellungIIdElternlos.equals(this.internebestellungIIdElternlos)))
			return false;
		if (!(that.iBelegpositionIId == null ? this.iBelegpositionIId == null
				: that.iBelegpositionIId.equals(this.iBelegpositionIId)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.belegartCNr.hashCode();
		result = 37 * result + this.iBelegiid.hashCode();
		result = 37 * result + this.stuecklisteIId.hashCode();
		result = 37 * result + this.nMenge.hashCode();
		result = 37 * result + this.tLiefertermin.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.internebestellungIIdElternlos.hashCode();
		result = 37 * result + this.iBelegpositionIId.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + mandantCNr;
		returnString += ", " + belegartCNr;
		returnString += ", " + iBelegiid;
		returnString += ", " + stuecklisteIId;
		returnString += ", " + nMenge;
		returnString += ", " + tLiefertermin;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + tAendern;
		returnString += ", " + internebestellungIIdElternlos;
		returnString += ", " + iBelegpositionIId;
		return returnString;
	}
}

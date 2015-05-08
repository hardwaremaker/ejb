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
import java.sql.Timestamp;

public class WiederholendeloseDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String mandantCNr;
	private Integer kostenstelleIId;
	private String cProjekt;
	private Integer stuecklisteIId;
	private BigDecimal nLosgroesse;
	private Integer partnerIIdFertigungsort;
	private Integer personalIIdAendern;
	private Timestamp tAendern;
	private Timestamp tTermin;
	private Integer lagerIIdZiel;
	private Integer iTagevoreilend;
	private Integer fertigungsgruppeIId;
	private String auftragwiederholungsintervallCNr;
	private Short bVersteckt;
	private Integer iSort;

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

	public Integer getKostenstelleIId() {
		return kostenstelleIId;
	}

	public void setKostenstelleIId(Integer kostenstelleIId) {
		this.kostenstelleIId = kostenstelleIId;
	}

	public String getCProjekt() {
		return cProjekt;
	}

	public void setCProjekt(String cProjekt) {
		this.cProjekt = cProjekt;
	}

	public Integer getStuecklisteIId() {
		return stuecklisteIId;
	}

	public void setStuecklisteIId(Integer stuecklisteIId) {
		this.stuecklisteIId = stuecklisteIId;
	}

	public BigDecimal getNLosgroesse() {
		return nLosgroesse;
	}

	public void setNLosgroesse(BigDecimal nLosgroesse) {
		this.nLosgroesse = nLosgroesse;
	}

	public Integer getPartnerIIdFertigungsort() {
		return partnerIIdFertigungsort;
	}

	public void setPartnerIIdFertigungsort(Integer partnerIIdFertigungsort) {
		this.partnerIIdFertigungsort = partnerIIdFertigungsort;
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

	public Timestamp getTTermin() {
		return tTermin;
	}

	public void setTTermin(Timestamp tTermin) {
		this.tTermin = tTermin;
	}

	public Integer getLagerIIdZiel() {
		return lagerIIdZiel;
	}

	public void setLagerIIdZiel(Integer lagerIIdZiel) {
		this.lagerIIdZiel = lagerIIdZiel;
	}

	public Integer getITagevoreilend() {
		return iTagevoreilend;
	}

	public void setITagevoreilend(Integer iTagevoreilend) {
		this.iTagevoreilend = iTagevoreilend;
	}

	public Integer getFertigungsgruppeIId() {
		return fertigungsgruppeIId;
	}

	public void setFertigungsgruppeIId(Integer fertigungsgruppeIId) {
		this.fertigungsgruppeIId = fertigungsgruppeIId;
	}

	public String getAuftragwiederholungsintervallCNr() {
		return auftragwiederholungsintervallCNr;
	}

	public void setAuftragwiederholungsintervallCNr(
			String auftragwiederholungsintervallCNr) {
		this.auftragwiederholungsintervallCNr = auftragwiederholungsintervallCNr;
	}

	public Short getBVersteckt() {
		return bVersteckt;
	}

	public Integer getISort() {
		return iSort;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof WiederholendeloseDto))
			return false;
		WiederholendeloseDto that = (WiederholendeloseDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.mandantCNr == null ? this.mandantCNr == null
				: that.mandantCNr.equals(this.mandantCNr)))
			return false;
		if (!(that.kostenstelleIId == null ? this.kostenstelleIId == null
				: that.kostenstelleIId.equals(this.kostenstelleIId)))
			return false;
		if (!(that.cProjekt == null ? this.cProjekt == null : that.cProjekt
				.equals(this.cProjekt)))
			return false;
		if (!(that.stuecklisteIId == null ? this.stuecklisteIId == null
				: that.stuecklisteIId.equals(this.stuecklisteIId)))
			return false;
		if (!(that.nLosgroesse == null ? this.nLosgroesse == null
				: that.nLosgroesse.equals(this.nLosgroesse)))
			return false;
		if (!(that.partnerIIdFertigungsort == null ? this.partnerIIdFertigungsort == null
				: that.partnerIIdFertigungsort
						.equals(this.partnerIIdFertigungsort)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern)))
			return false;
		if (!(that.tTermin == null ? this.tTermin == null : that.tTermin
				.equals(this.tTermin)))
			return false;
		if (!(that.lagerIIdZiel == null ? this.lagerIIdZiel == null
				: that.lagerIIdZiel.equals(this.lagerIIdZiel)))
			return false;
		if (!(that.iTagevoreilend == null ? this.iTagevoreilend == null
				: that.iTagevoreilend.equals(this.iTagevoreilend)))
			return false;
		if (!(that.fertigungsgruppeIId == null ? this.fertigungsgruppeIId == null
				: that.fertigungsgruppeIId.equals(this.fertigungsgruppeIId)))
			return false;
		if (!(that.auftragwiederholungsintervallCNr == null ? this.auftragwiederholungsintervallCNr == null
				: that.auftragwiederholungsintervallCNr
						.equals(this.auftragwiederholungsintervallCNr)))
			return false;
		if (!(that.bVersteckt == null ? this.bVersteckt == null
				: that.bVersteckt.equals(this.bVersteckt)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.kostenstelleIId.hashCode();
		result = 37 * result + this.cProjekt.hashCode();
		result = 37 * result + this.stuecklisteIId.hashCode();
		result = 37 * result + this.nLosgroesse.hashCode();
		result = 37 * result + this.partnerIIdFertigungsort.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.tTermin.hashCode();
		result = 37 * result + this.lagerIIdZiel.hashCode();
		result = 37 * result + this.iTagevoreilend.hashCode();
		result = 37 * result + this.fertigungsgruppeIId.hashCode();
		result = 37 * result + this.auftragwiederholungsintervallCNr.hashCode();
		result = 37 * result + this.bVersteckt.hashCode();
		return result;
	}

	public String toString() {
		StringBuffer returnStringBuffer = new StringBuffer(480);
		returnStringBuffer.append("[");
		returnStringBuffer.append("iId:").append(iId);
		returnStringBuffer.append("mandantCNr:").append(mandantCNr);
		returnStringBuffer.append("kostenstelleIId:").append(kostenstelleIId);
		returnStringBuffer.append("cProjekt:").append(cProjekt);
		returnStringBuffer.append("stuecklisteIId:").append(stuecklisteIId);
		returnStringBuffer.append("nLosgroesse:").append(nLosgroesse);
		returnStringBuffer.append("partnerIIdFertigungsort:").append(
				partnerIIdFertigungsort);
		returnStringBuffer.append("personalIIdAendern:").append(
				personalIIdAendern);
		returnStringBuffer.append("tAendern:").append(tAendern);
		returnStringBuffer.append("tTermin:").append(tTermin);
		returnStringBuffer.append("lagerIIdZiel:").append(lagerIIdZiel);
		returnStringBuffer.append("iTagevoreilend:").append(iTagevoreilend);
		returnStringBuffer.append("fertigungsgruppeIId:").append(
				fertigungsgruppeIId);
		returnStringBuffer.append("auftragwiederholungsintervallCNr:").append(
				auftragwiederholungsintervallCNr);
		returnStringBuffer.append("bVersteckt:").append(bVersteckt);
		returnStringBuffer.append("]");
		return returnStringBuffer.toString();
	}
}

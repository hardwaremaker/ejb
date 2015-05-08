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
package com.lp.server.partner.service;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class AnsprechpartnerDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer partnerIId;
	private Integer partnerIIdAnsprechpartner;
	private Date dGueltigab;
	private Integer ansprechpartnerfunktionIId;
	private String xBemerkung;
	private Integer iSort;
	private Timestamp tAendern;
	private Integer personalIIdAendern;
	private boolean newsletterEmpfaenger;

	private String cFax;
	private String cTelefon;
	private String cHandy;
	private String cDirektfax;

	private String cEmail;

	private String cAbteilung;

	public String getCAbteilung() {
		return cAbteilung;
	}

	public void setCAbteilung(String cAbteilung) {
		this.cAbteilung = cAbteilung;
	}

	public boolean isNewsletterEmpfaenger() {
		return newsletterEmpfaenger;
	}

	public void setNewsletterEmpfaenger(boolean newsletterEmpfaenger) {
		this.newsletterEmpfaenger = newsletterEmpfaenger;
	}

	public String getCFax() {
		return cFax;
	}

	public void setCFax(String cFax) {
		this.cFax = cFax;
	}

	public String getCTelefon() {
		return cTelefon;
	}

	public void setCTelefon(String cTelefon) {
		this.cTelefon = cTelefon;
	}

	public String getCDirektfax() {
		return cDirektfax;
	}

	public void setCDirektfax(String cDirektfax) {
		this.cDirektfax = cDirektfax;
	}

	public String getCEmail() {
		return cEmail;
	}

	public void setCEmail(String cEmail) {
		this.cEmail = cEmail;
	}

	public String getCHandy() {
		return cHandy;
	}

	public void setCHandy(String cHandy) {
		this.cHandy = cHandy;
	}

	private PartnerDto partnerDto = new PartnerDto();
	private Short bVersteckt;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getPartnerIId() {
		return partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	public Integer getPartnerIIdAnsprechpartner() {
		return partnerIIdAnsprechpartner;
	}

	public void setPartnerIIdAnsprechpartner(Integer partnerIIdAnsprechpartner) {
		this.partnerIIdAnsprechpartner = partnerIIdAnsprechpartner;
	}

	private String cFremdsystemnr;

	public String getCFremdsystemnr() {
		return cFremdsystemnr;
	}

	public void setCFremdsystemnr(String fremdsystemnr) {
		cFremdsystemnr = fremdsystemnr;
	}

	public Date getDGueltigab() {
		return dGueltigab;
	}

	public void setDGueltigab(Date dGueltigab) {
		this.dGueltigab = dGueltigab;
	}

	public Integer getISort() {
		return iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
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

	public PartnerDto getPartnerDto() {
		return partnerDto;
	}

	public Integer getAnsprechpartnerfunktionIId() {
		return ansprechpartnerfunktionIId;
	}

	private String cKennwort;

	public String getCKennwort() {
		return this.cKennwort;
	}

	public void setCKennwort(String cKennwort) {
		this.cKennwort = cKennwort;
	}

	public String getXBemerkung() {
		return xBemerkung;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public void setPartnerDto(PartnerDto partnerDto) {
		this.partnerDto = partnerDto;
	}

	public void setAnsprechpartnerfunktionIId(Integer ansprechpartnerfunktionIId) {
		this.ansprechpartnerfunktionIId = ansprechpartnerfunktionIId;
	}

	public void setXBemerkung(String xBemerkung) {
		this.xBemerkung = xBemerkung;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AnsprechpartnerDto)) {
			return false;
		}
		AnsprechpartnerDto that = (AnsprechpartnerDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.partnerIId == null ? this.partnerIId == null
				: that.partnerIId.equals(this.partnerIId))) {
			return false;
		}
		if (!(that.partnerIIdAnsprechpartner == null ? this.partnerIIdAnsprechpartner == null
				: that.partnerIIdAnsprechpartner
						.equals(this.partnerIIdAnsprechpartner))) {
			return false;
		}
		if (!(that.dGueltigab == null ? this.dGueltigab == null
				: that.dGueltigab.equals(this.dGueltigab))) {
			return false;
		}
		if (!(that.iSort == null ? this.iSort == null : that.iSort
				.equals(this.iSort))) {
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
		if (that.newsletterEmpfaenger != this.newsletterEmpfaenger) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.partnerIId.hashCode();
		result = 37 * result + this.partnerIIdAnsprechpartner.hashCode();
		result = 37 * result + this.dGueltigab.hashCode();
		result = 37 * result + this.iSort.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + partnerIId;
		returnString += ", " + partnerIIdAnsprechpartner;
		returnString += ", " + dGueltigab;
		returnString += ", " + iSort;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdAendern;
		return returnString;
	}

	/**
	 * AnsprechpartnerDto
	 */
	public AnsprechpartnerDto() {
		partnerDto = new PartnerDto();
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	public Short getBVersteckt() {
		return bVersteckt;
	}

}

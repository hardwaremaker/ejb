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
import java.sql.Timestamp;
import java.util.BitSet;

public class KurzbriefDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int iF = 0;
	public int iFiId = iF++;
	private Integer iId;

	public int iFipartnerIId = iF++;
	private Integer partnerIId;

	public int iFiansprechpartnerIId = iF++;
	private Integer ansprechpartnerIId;

	public int iFicBetreff = iF++;
	private String cBetreff;

	public int iFitAnlegen = iF++;
	private Timestamp tAnlegen;

	public int iFipersonalIIdAnlegen = iF++;
	private Integer personalIIdAnlegen;

	public int iFitAendern = iF++;
	private Timestamp tAendern;

	public int iFitpersonalIIdAendern = iF++;
	private Integer personalIIdAendern;

	public int iFixText = iF++;
	private String xText;

	private BitSet bsIndikator = new BitSet(iF);
	private String belegartCNr;
	private Short bHtml ;

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

	public Integer getAnsprechpartnerIId() {
		return ansprechpartnerIId;
	}

	public void setAnsprechpartnerIId(Integer ansprechpartnerIId) {
		bsIndikator.set(iFiansprechpartnerIId, true);
		this.ansprechpartnerIId = ansprechpartnerIId;
	}

	public String getCBetreff() {
		return cBetreff;
	}

	public void setCBetreff(String cBetreff) {
		bsIndikator.set(iFicBetreff, true);
		this.cBetreff = cBetreff;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		bsIndikator.set(iFitAnlegen, true);
		this.tAnlegen = tAnlegen;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		bsIndikator.set(iFipersonalIIdAnlegen, true);
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		bsIndikator.set(iFitAendern, true);
		this.tAendern = tAendern;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		bsIndikator.set(iFitpersonalIIdAendern, true);
		this.personalIIdAendern = personalIIdAendern;
	}

	public String getXText() {
		return xText;
	}

	public String getBelegartCNr() {
		return belegartCNr;
	}

	public void setXText(String xText) {
		bsIndikator.set(iFixText, true);
		this.xText = xText;
	}

	public void setBelegartCNr(String belegartCNr) {
		this.belegartCNr = belegartCNr;
	}

	public Short getBHtml() {
		return bHtml;
	}

	public void setBHtml(Short b_html) {
		this.bHtml = b_html;
	}

	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof KurzbriefDto))
			return false;
		KurzbriefDto that = (KurzbriefDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.partnerIId == null ? this.partnerIId == null
				: that.partnerIId.equals(this.partnerIId)))
			return false;
		if (!(that.ansprechpartnerIId == null ? this.ansprechpartnerIId == null
				: that.ansprechpartnerIId.equals(this.ansprechpartnerIId)))
			return false;
		if (!(that.cBetreff == null ? this.cBetreff == null : that.cBetreff
				.equals(this.cBetreff)))
			return false;
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen
				.equals(this.tAnlegen)))
			return false;
		if (!(that.personalIIdAnlegen == null ? this.personalIIdAnlegen == null
				: that.personalIIdAnlegen.equals(this.personalIIdAnlegen)))
			return false;
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		if (!(that.xText == null ? this.xText == null : that.xText
				.equals(this.xText)))
			return false;
		if(!(that.bHtml == null ? this.bHtml == null : that.bHtml.equals(this.bHtml))) return false ;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.partnerIId.hashCode();
		result = 37 * result + this.ansprechpartnerIId.hashCode();
		result = 37 * result + this.cBetreff.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.xText.hashCode();
		result = 37 * result + this.bHtml.hashCode() ;
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + partnerIId;
		returnString += ", " + ansprechpartnerIId;
		returnString += ", " + cBetreff;
		returnString += ", " + tAnlegen;
		returnString += ", " + personalIIdAnlegen;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + xText;
		return returnString;
	}
}

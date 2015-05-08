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
package com.lp.server.personal.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class ReiseDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iId;
	private Integer personalIId;
	private Timestamp tZeit;
	private Short bBeginn;
	private Integer diaetenIId;
	private Integer partnerIId;
	private Integer ansprechpartnerIId;
	private Integer iKmbeginn;
	private Integer iKmende;
	private BigDecimal nSpesen;
	private String cFahrzeug;
	private String cKommentar;
	private Timestamp tAendern;
	private Integer personalIIdAendern;
	private Integer fahrzeugIId;
	private Integer iBelegartid;
	private String belegartCNr;
	private Double fFaktor;
	
	
	//NUR INTERN VERWENDET
	private BigDecimal nKostenDesAbschnitts;
	
	public BigDecimal getNKostenDesAbschnitts() {
		return nKostenDesAbschnitts;
	}

	public void setNKostenDesAbschnitts(BigDecimal nKostenDesAbschnitts) {
		this.nKostenDesAbschnitts = nKostenDesAbschnitts;
	}

	public Integer getFahrzeugIId() {
		return fahrzeugIId;
	}

	public void setFahrzeugIId(Integer fahrzeugIId) {
		this.fahrzeugIId = fahrzeugIId;
	}

	public Integer getIBelegartid() {
		return iBelegartid;
	}

	public void setIBelegartid(Integer iBelegartid) {
		this.iBelegartid = iBelegartid;
	}

	public String getBelegartCNr() {
		return belegartCNr;
	}

	public void setBelegartCNr(String belegartCNr) {
		this.belegartCNr = belegartCNr;
	}

	public Double getFFaktor() {
		return fFaktor;
	}

	public void setFFaktor(Double fFaktor) {
		this.fFaktor = fFaktor;
	}
	
	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getPersonalIId() {
		return personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

	public Timestamp getTZeit() {
		return tZeit;
	}

	public void setTZeit(Timestamp tZeit) {
		this.tZeit = tZeit;
	}

	public Short getBBeginn() {
		return bBeginn;
	}

	public void setBBeginn(Short bBeginn) {
		this.bBeginn = bBeginn;
	}

	public Integer getDiaetenIId() {
		return diaetenIId;
	}

	public void setDiaetenIId(Integer diaetenIId) {
		this.diaetenIId = diaetenIId;
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
		this.ansprechpartnerIId = ansprechpartnerIId;
	}

	public Integer getIKmbeginn() {
		return iKmbeginn;
	}

	public void setIKmbeginn(Integer iKmbeginn) {
		this.iKmbeginn = iKmbeginn;
	}

	public Integer getIKmende() {
		return iKmende;
	}

	public void setIKmende(Integer iKmende) {
		this.iKmende = iKmende;
	}

	public BigDecimal getNSpesen() {
		return nSpesen;
	}

	public void setNSpesen(BigDecimal nSpesen) {
		this.nSpesen = nSpesen;
	}

	public String getCFahrzeug() {
		return cFahrzeug;
	}

	public void setCFahrzeug(String cFahrzeug) {
		this.cFahrzeug = cFahrzeug;
	}

	public String getCKommentar() {
		return cKommentar;
	}

	public void setCKommentar(String cKommentar) {
		this.cKommentar = cKommentar;
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

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ReiseDto))
			return false;
		ReiseDto that = (ReiseDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.personalIId == null ? this.personalIId == null
				: that.personalIId.equals(this.personalIId)))
			return false;
		if (!(that.tZeit == null ? this.tZeit == null : that.tZeit
				.equals(this.tZeit)))
			return false;
		if (!(that.bBeginn == null ? this.bBeginn == null : that.bBeginn
				.equals(this.bBeginn)))
			return false;
		if (!(that.diaetenIId == null ? this.diaetenIId == null
				: that.diaetenIId.equals(this.diaetenIId)))
			return false;
		if (!(that.partnerIId == null ? this.partnerIId == null
				: that.partnerIId.equals(this.partnerIId)))
			return false;
		if (!(that.ansprechpartnerIId == null ? this.ansprechpartnerIId == null
				: that.ansprechpartnerIId.equals(this.ansprechpartnerIId)))
			return false;
		if (!(that.iKmbeginn == null ? this.iKmbeginn == null : that.iKmbeginn
				.equals(this.iKmbeginn)))
			return false;
		if (!(that.iKmende == null ? this.iKmende == null : that.iKmende
				.equals(this.iKmende)))
			return false;
		if (!(that.nSpesen == null ? this.nSpesen == null : that.nSpesen
				.equals(this.nSpesen)))
			return false;
		if (!(that.cFahrzeug == null ? this.cFahrzeug == null : that.cFahrzeug
				.equals(this.cFahrzeug)))
			return false;
		if (!(that.cKommentar == null ? this.cKommentar == null
				: that.cKommentar.equals(this.cKommentar)))
			return false;
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.personalIId.hashCode();
		result = 37 * result + this.tZeit.hashCode();
		result = 37 * result + this.bBeginn.hashCode();
		result = 37 * result + this.diaetenIId.hashCode();
		result = 37 * result + this.partnerIId.hashCode();
		result = 37 * result + this.ansprechpartnerIId.hashCode();
		result = 37 * result + this.iKmbeginn.hashCode();
		result = 37 * result + this.iKmende.hashCode();
		result = 37 * result + this.nSpesen.hashCode();
		result = 37 * result + this.cFahrzeug.hashCode();
		result = 37 * result + this.cKommentar.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		return result;
	}

	public String toString() {
		StringBuffer returnStringBuffer = new StringBuffer(448);
		returnStringBuffer.append("[");
		returnStringBuffer.append("iId:").append(iId);
		returnStringBuffer.append("personalIId:").append(personalIId);
		returnStringBuffer.append("tZeit:").append(tZeit);
		returnStringBuffer.append("bBeginn:").append(bBeginn);
		returnStringBuffer.append("diaetenIId:").append(diaetenIId);
		returnStringBuffer.append("partnerIId:").append(partnerIId);
		returnStringBuffer.append("ansprechpartnerIId:").append(
				ansprechpartnerIId);
		returnStringBuffer.append("iKmbeginn:").append(iKmbeginn);
		returnStringBuffer.append("iKmende:").append(iKmende);
		returnStringBuffer.append("nSpesen:").append(nSpesen);
		returnStringBuffer.append("cFahrzeug:").append(cFahrzeug);
		returnStringBuffer.append("cKommentar:").append(cKommentar);
		returnStringBuffer.append("tAendern:").append(tAendern);
		returnStringBuffer.append("personalIIdAendern:").append(
				personalIIdAendern);
		returnStringBuffer.append("]");
		return returnStringBuffer.toString();
	}
}

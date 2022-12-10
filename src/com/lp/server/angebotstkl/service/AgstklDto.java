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
package com.lp.server.angebotstkl.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;

public class AgstklDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String mandantCNr;
	private String cNr;
	private Timestamp tBelegdatum;
	private String belegartCNr;
	private Integer kundeIId;
	private Integer ansprechpartnerIIdKunde;
	private String cBez;
	private String waehrungCNr;
	private Double fWechselkursmandantwaehrungzuagstklwaehrung;
	private Integer personalIIdAnlegen;
	private Timestamp tAnlegen;
	private Integer personalIIdAendern;
	private Timestamp tAendern;

	private BigDecimal nInitialkosten;
	
	public BigDecimal getNInitialkosten() {
		return nInitialkosten;
	}

	public void setNInitialkosten(BigDecimal nInitialkosten) {
		this.nInitialkosten = nInitialkosten;
	}
	
	private String cDateiname;

	public String getCDateiname() {
		return cDateiname;
	}

	public void setCDateiname(String cDateiname) {
		this.cDateiname = cDateiname;
	}


	private String cZeichnungsnummer;

	public String getCZeichnungsnummer() {
		return cZeichnungsnummer;
	}

	public void setCZeichnungsnummer(String cZeichnungsnummer) {
		this.cZeichnungsnummer = cZeichnungsnummer;
	}
 
	private String datenformatCNr;

	public String getDatenformatCNr() {
		return datenformatCNr;
	}

	public void setDatenformatCNr(String datenformatCNr) {
		this.datenformatCNr = datenformatCNr;
	}

	private byte[] oMedia;

	public byte[] getOMedia() {
		return oMedia;
	}

	public void setOMedia(byte[] oMedia) {
		this.oMedia = oMedia;
	}

	private Integer iHoeheDialog;

	public Integer getIHoeheDialog() {
		return iHoeheDialog;
	}

	public void setIHoeheDialog(Integer iHoeheDialog) {
		this.iHoeheDialog = iHoeheDialog;
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

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	private Integer projektIId;

	public Integer getProjektIId() {
		return projektIId;
	}

	public void setProjektIId(Integer projektIId) {
		this.projektIId = projektIId;
	}

	private Short bDatengeaendert;

	public Short getBDatengeaendert() {
		return bDatengeaendert;
	}

	public void setBDatengeaendert(Short bDatengeaendert) {
		this.bDatengeaendert = bDatengeaendert;
	}

	public String getBelegartCNr() {
		return belegartCNr;
	}

	public void setBelegartCNr(String belegartCNr) {
		this.belegartCNr = belegartCNr;
	}

	public Integer getKundeIId() {
		return kundeIId;
	}

	public void setKundeIId(Integer kundeIId) {
		this.kundeIId = kundeIId;
	}

	public Integer getAnsprechpartnerIIdKunde() {
		return ansprechpartnerIIdKunde;
	}

	public void setAnsprechpartnerIIdKunde(Integer ansprechpartnerIIdKunde) {
		this.ansprechpartnerIIdKunde = ansprechpartnerIIdKunde;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public String getWaehrungCNr() {
		return waehrungCNr;
	}

	public void setWaehrungCNr(String waehrungCNr) {
		this.waehrungCNr = waehrungCNr;
	}

	public Double getFWechselkursmandantwaehrungzuagstklwaehrung() {
		return fWechselkursmandantwaehrungzuagstklwaehrung;
	}

	public void setFWechselkursmandantwaehrungzuagstklwaehrung(Double fWechselkursmandantwaehrungzuagstklwaehrung) {
		this.fWechselkursmandantwaehrungzuagstklwaehrung = fWechselkursmandantwaehrungzuagstklwaehrung;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	private Short bVorlage;

	public Short getBVorlage() {
		return bVorlage;
	}

	public void setBVorlage(Short bVorlage) {
		this.bVorlage = bVorlage;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
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

	public Timestamp getTBelegdatum() {
		return tBelegdatum;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public void setTBelegdatum(Timestamp tBelegdatum) {
		this.tBelegdatum = tBelegdatum;
	}

	private Integer iEkpreisbasis;

	public Integer getIEkpreisbasis() {
		return iEkpreisbasis;
	}

	public void setIEkpreisbasis(Integer iEkpreisbasis) {
		this.iEkpreisbasis = iEkpreisbasis;
	}

	private Integer stuecklisteIId;

	public Integer getStuecklisteIId() {
		return this.stuecklisteIId;
	}

	public void setStuecklisteIId(Integer stuecklisteIId) {
		this.stuecklisteIId = stuecklisteIId;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof AgstklDto))
			return false;
		AgstklDto that = (AgstklDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.mandantCNr == null ? this.mandantCNr == null : that.mandantCNr.equals(this.mandantCNr)))
			return false;
		if (!(that.cNr == null ? this.cNr == null : that.cNr.equals(this.cNr)))
			return false;
		if (!(that.belegartCNr == null ? this.belegartCNr == null : that.belegartCNr.equals(this.belegartCNr)))
			return false;
		if (!(that.kundeIId == null ? this.kundeIId == null : that.kundeIId.equals(this.kundeIId)))
			return false;
		if (!(that.ansprechpartnerIIdKunde == null ? this.ansprechpartnerIIdKunde == null
				: that.ansprechpartnerIIdKunde.equals(this.ansprechpartnerIIdKunde)))
			return false;
		if (!(that.cBez == null ? this.cBez == null : that.cBez.equals(this.cBez)))
			return false;
		if (!(that.waehrungCNr == null ? this.waehrungCNr == null : that.waehrungCNr.equals(this.waehrungCNr)))
			return false;
		if (!(that.fWechselkursmandantwaehrungzuagstklwaehrung == null
				? this.fWechselkursmandantwaehrungzuagstklwaehrung == null
				: that.fWechselkursmandantwaehrungzuagstklwaehrung
						.equals(this.fWechselkursmandantwaehrungzuagstklwaehrung)))
			return false;
		if (!(that.personalIIdAnlegen == null ? this.personalIIdAnlegen == null
				: that.personalIIdAnlegen.equals(this.personalIIdAnlegen)))
			return false;
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen.equals(this.tAnlegen)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern.equals(this.tAendern)))
			return false;
		if (!(that.tBelegdatum == null ? this.tBelegdatum == null : that.tBelegdatum.equals(this.tBelegdatum)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.cNr.hashCode();
		result = 37 * result + this.belegartCNr.hashCode();
		result = 37 * result + this.kundeIId.hashCode();
		result = 37 * result + this.ansprechpartnerIIdKunde.hashCode();
		result = 37 * result + this.cBez.hashCode();
		result = 37 * result + this.waehrungCNr.hashCode();
		result = 37 * result + this.fWechselkursmandantwaehrungzuagstklwaehrung.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.tBelegdatum.hashCode();
		return result;
	}

	public String toString() {
		StringBuffer returnStringBuffer = new StringBuffer(448);
		returnStringBuffer.append("[");
		returnStringBuffer.append("iId:").append(iId);
		returnStringBuffer.append("mandantCNr:").append(mandantCNr);
		returnStringBuffer.append("cNr:").append(cNr);
		returnStringBuffer.append("belegartCNr:").append(belegartCNr);
		returnStringBuffer.append("kundeIId:").append(kundeIId);
		returnStringBuffer.append("ansprechpartnerIIdKunde:").append(ansprechpartnerIIdKunde);
		returnStringBuffer.append("cBez:").append(cBez);
		returnStringBuffer.append("waehrungCNr:").append(waehrungCNr);
		returnStringBuffer.append("fWechselkursmandantwaehrungzuagstklwaehrung:")
				.append(fWechselkursmandantwaehrungzuagstklwaehrung);
		returnStringBuffer.append("personalIIdAnlegen:").append(personalIIdAnlegen);
		returnStringBuffer.append("tAnlegen:").append(tAnlegen);
		returnStringBuffer.append("personalIIdAendern:").append(personalIIdAendern);
		returnStringBuffer.append("tAendern:").append(tAendern);
		returnStringBuffer.append("tBelegdatum:").append(tBelegdatum);
		returnStringBuffer.append("]");
		return returnStringBuffer.toString();
	}
}

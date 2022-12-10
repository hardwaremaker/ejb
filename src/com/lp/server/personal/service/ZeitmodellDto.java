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
import java.sql.Time;
import java.sql.Timestamp;

import javax.persistence.Column;

import com.lp.util.Helper;

public class ZeitmodellDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String mandantCNr;
	private String cNr;
	private Short bTeilzeit;
	private Integer personalIIdAendern;
	private Timestamp tAendern;
	private ZeitmodellsprDto zeitmodellsprDto;
	private Short bVersteckt;
	private Double fUrlaubstageprowoche;

	
	private Short bFeiertagAmNaechstenTag;

	public Short getBFeiertagAmNaechstenTag() {
		return bFeiertagAmNaechstenTag;
	}

	public void setBFeiertagAmNaechstenTag(Short bFeiertagAmNaechstenTag) {
		this.bFeiertagAmNaechstenTag = bFeiertagAmNaechstenTag;
	}
	
	private Short bUnproduktivAlsPause;

	
	public Short getBUnproduktivAlsPause() {
		return bUnproduktivAlsPause;
	}

	public void setBUnproduktivAlsPause(Short bUnproduktivAlsPause) {
		this.bUnproduktivAlsPause = bUnproduktivAlsPause;
	}
	private BigDecimal nMaximaleMehrzeit;

	public BigDecimal getNMaximaleMehrzeit() {
		return nMaximaleMehrzeit;
	}

	public void setNMaximaleMehrzeit(BigDecimal nMaximaleMehrzeit) {
		this.nMaximaleMehrzeit = nMaximaleMehrzeit;
	}

	private BigDecimal nMaximalesWochenist;

	public BigDecimal getNMaximalesWochenist() {
		return nMaximalesWochenist;
	}

	public void setNMaximalesWochenist(BigDecimal nMaximalesWochenist) {
		this.nMaximalesWochenist = nMaximalesWochenist;
	}

	private Short bFeiertagssollAddieren;

	public Short getBFeiertagssollAddieren() {
		return bFeiertagssollAddieren;
	}

	public void setBFeiertagssollAddieren(Short bFeiertagssollAddieren) {
		this.bFeiertagssollAddieren = bFeiertagssollAddieren;
	}

	private Integer iMinutenabzug;

	public Integer getIMinutenabzug() {
		return iMinutenabzug;
	}

	public void setIMinutenabzug(Integer iMinutenabzug) {
		this.iMinutenabzug = iMinutenabzug;
	}

	private BigDecimal nSollstundenfix;

	public BigDecimal getNSollstundenfix() {
		return nSollstundenfix;
	}

	public void setNSollstundenfix(BigDecimal nSollstundenfix) {
		this.nSollstundenfix = nSollstundenfix;
	}

	public Double getFUrlaubstageprowoche() {
		return fUrlaubstageprowoche;
	}

	public void setFUrlaubstageprowoche(Double fUrlaubstageprowoche) {
		this.fUrlaubstageprowoche = fUrlaubstageprowoche;
	}

	private Time uGutschriftKommt;

	private Time uGutschriftGeht;
	
	public Time getUGutschriftKommt() {
		return uGutschriftKommt;
	}

	public void setUGutschriftKommt(Time uGutschriftKommt) {
		this.uGutschriftKommt = uGutschriftKommt;
	}

	public Time getUGutschriftGeht() {
		return uGutschriftGeht;
	}

	public void setUGutschriftGeht(Time uGutschriftGeht) {
		this.uGutschriftGeht = uGutschriftGeht;
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

	private Integer schichtIId;

	public Integer getSchichtIId() {
		return schichtIId;
	}

	public void setSchichtIId(Integer schichtIId) {
		this.schichtIId = schichtIId;
	}

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public String getBezeichnung() {
		String bezeichnung = getCNr();

		if (getZeitmodellsprDto() != null
				&& getZeitmodellsprDto().getCBez() != null) {
			bezeichnung += " " + getZeitmodellsprDto().getCBez();
		}
		return bezeichnung;
	}

	public Short getBTeilzeit() {
		return bTeilzeit;
	}

	public void setBTeilzeit(Short bTeilzeit) {
		this.bTeilzeit = bTeilzeit;
	}

	private Short bFirmenzeitmodell;

	public Short getBFirmenzeitmodell() {
		return bFirmenzeitmodell;
	}

	public void setBFirmenzeitmodell(Short bFirmenzeitmodell) {
		this.bFirmenzeitmodell = bFirmenzeitmodell;
	}

	private Short bFixepauseTrotzkommtgeht;

	public Short getBFixepauseTrotzkommtgeht() {
		return bFixepauseTrotzkommtgeht;
	}

	public void setBFixepauseTrotzkommtgeht(Short bFixepauseTrotzkommtgeht) {
		this.bFixepauseTrotzkommtgeht = bFixepauseTrotzkommtgeht;
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

	public ZeitmodellsprDto getZeitmodellsprDto() {
		return zeitmodellsprDto;
	}

	public Short getBVersteckt() {
		return bVersteckt;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public void setZeitmodellsprDto(ZeitmodellsprDto zeitmodellsprDto) {
		this.zeitmodellsprDto = zeitmodellsprDto;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	private Short bDynamisch;

	public Short getBDynamisch() {
		return bDynamisch;
	}

	public void setBDynamisch(Short bDynamisch) {
		this.bDynamisch = bDynamisch;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ZeitmodellDto))
			return false;
		ZeitmodellDto that = (ZeitmodellDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.mandantCNr == null ? this.mandantCNr == null
				: that.mandantCNr.equals(this.mandantCNr)))
			return false;
		if (!(that.cNr == null ? this.cNr == null : that.cNr.equals(this.cNr)))
			return false;
		if (!(that.bTeilzeit == null ? this.bTeilzeit == null : that.bTeilzeit
				.equals(this.bTeilzeit)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.cNr.hashCode();
		result = 37 * result + this.bTeilzeit.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + mandantCNr;
		returnString += ", " + cNr;
		returnString += ", " + bTeilzeit;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + tAendern;
		return returnString;
	}
}

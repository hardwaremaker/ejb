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
package com.lp.server.instandhaltung.service;

import java.io.Serializable;
import java.math.BigDecimal;



public class GeraetDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer iId;
	private String cBez;
	private String mandantCNr;
	private String xBemerkung;
	public String getCLeistung() {
		return cLeistung;
	}
	private Integer herstellerIId;
	private Integer gewerkIId;

	public Integer getHerstellerIId() {
		return herstellerIId;
	}

	public void setHerstellerIId(Integer herstellerIId) {
		this.herstellerIId = herstellerIId;
	}

	public Integer getGewerkIId() {
		return gewerkIId;
	}

	public void setGewerkIId(Integer gewerkIId) {
		this.gewerkIId = gewerkIId;
	}
	public void setCLeistung(String cLeistung) {
		this.cLeistung = cLeistung;
	}

	private String cLeistung;
	private Integer iAnzahl;

	public Integer getIAnzahl() {
		return iAnzahl;
	}

	public void setIAnzahl(Integer iAnzahl) {
		this.iAnzahl = iAnzahl;
	}
	private String cFabrikat;

	public String getCFabrikat() {
		return cFabrikat;
	}

	public void setCFabrikat(String cFabrikat) {
		this.cFabrikat = cFabrikat;
	}
	private Short bVersteckt;

	public Short getBVersteckt() {
		return bVersteckt;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}
	public String getXBemerkung() {
		return xBemerkung;
	}

	public void setXBemerkung(String xBemerkung) {
		this.xBemerkung = xBemerkung;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	private Integer standortIId;
	private Integer halleIId;
	private Integer anlageIId;
	private Integer ismaschineIId;
	private Integer geraetetypIId;
	private String cStandort;
	private String cGeraetesnr;
	private String cVersorgungskreis;
	private Short bAufwand;
	private Short bMesswertabsolut;
	private BigDecimal nGrenzwertmin;
	private BigDecimal nGrenzwertmax;

	public Integer getStandortIId() {
		return standortIId;
	}

	public void setStandortIId(Integer standortIId) {
		this.standortIId = standortIId;
	}

	public Integer getHalleIId() {
		return halleIId;
	}

	public void setHalleIId(Integer halleIId) {
		this.halleIId = halleIId;
	}

	public Integer getAnlageIId() {
		return anlageIId;
	}

	public void setAnlageIId(Integer anlageIId) {
		this.anlageIId = anlageIId;
	}

	public Integer getIsmaschineIId() {
		return ismaschineIId;
	}

	public void setIsmaschineIId(Integer ismaschineIId) {
		this.ismaschineIId = ismaschineIId;
	}

	public Integer getGeraetetypIId() {
		return geraetetypIId;
	}

	public void setGeraetetypIId(Integer geraetetypIId) {
		this.geraetetypIId = geraetetypIId;
	}

	public String getCStandort() {
		return cStandort;
	}

	public void setCStandort(String cStandort) {
		this.cStandort = cStandort;
	}

	public String getCGeraetesnr() {
		return cGeraetesnr;
	}

	public void setCGeraetesnr(String cGeraetesnr) {
		this.cGeraetesnr = cGeraetesnr;
	}

	public String getCVersorgungskreis() {
		return cVersorgungskreis;
	}

	public void setCVersorgungskreis(String cVersorgungskreis) {
		this.cVersorgungskreis = cVersorgungskreis;
	}

	public Short getBAufwand() {
		return bAufwand;
	}

	public void setBAufwand(Short bAufwand) {
		this.bAufwand = bAufwand;
	}

	public Short getBMesswertabsolut() {
		return bMesswertabsolut;
	}

	public void setBMesswertabsolut(Short bMesswertabsolut) {
		this.bMesswertabsolut = bMesswertabsolut;
	}

	public BigDecimal getNGrenzwertmin() {
		return nGrenzwertmin;
	}

	public void setNGrenzwertmin(BigDecimal nGrenzwertmin) {
		this.nGrenzwertmin = nGrenzwertmin;
	}

	public BigDecimal getNGrenzwertmax() {
		return nGrenzwertmax;
	}

	public void setNGrenzwertmax(BigDecimal nGrenzwertmax) {
		this.nGrenzwertmax = nGrenzwertmax;
	}

	public BigDecimal getNGrenzwert() {
		return nGrenzwert;
	}

	public void setNGrenzwert(BigDecimal nGrenzwert) {
		this.nGrenzwert = nGrenzwert;
	}

	private BigDecimal nGrenzwert;

}

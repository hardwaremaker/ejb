/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
package com.lp.server.instandhaltung.ejb;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({ @NamedQuery(name = "GeraetFindByMandantCNrCBez", query = "SELECT OBJECT(o) FROM Geraet o WHERE o.mandantCNr = ?1 AND o.cBez = ?2") })
@Entity
@Table(name = "IS_GERAET")
public class Geraet implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "STANDORT_I_ID")
	private Integer standortIId;

	@Column(name = "HALLE_I_ID")
	private Integer halleIId;

	@Column(name = "ANLAGE_I_ID")
	private Integer anlageIId;

	@Column(name = "ISMASCHINE_I_ID")
	private Integer ismaschineIId;

	@Column(name = "GERAETETYP_I_ID")
	private Integer geraetetypIId;

	@Column(name = "HERSTELLER_I_ID")
	private Integer herstellerIId;

	@Column(name = "GEWERK_I_ID")
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

	@Column(name = "I_ANZAHL")
	private Integer iAnzahl;

	public Integer getIAnzahl() {
		return iAnzahl;
	}

	public void setIAnzahl(Integer iAnzahl) {
		this.iAnzahl = iAnzahl;
	}

	@Column(name = "C_STANDORT")
	private String cStandort;

	@Column(name = "C_GERAETESNR")
	private String cGeraetesnr;

	@Column(name = "C_VERSORGUNGSKREIS")
	private String cVersorgungskreis;

	public String getCLeistung() {
		return cLeistung;
	}

	public void setCLeistung(String cLeistung) {
		this.cLeistung = cLeistung;
	}

	@Column(name = "C_LEISTUNG")
	private String cLeistung;
	
	@Column(name = "C_FABRIKAT")
	private String cFabrikat;

	public String getCFabrikat() {
		return cFabrikat;
	}

	public void setCFabrikat(String cFabrikat) {
		this.cFabrikat = cFabrikat;
	}

	@Column(name = "B_AUFWAND")
	private Short bAufwand;

	@Column(name = "B_MESSWERTABSOLUT")
	private Short bMesswertabsolut;

	@Column(name = "N_GRENZWERTMIN")
	private BigDecimal nGrenzwertmin;

	@Column(name = "N_GRENZWERTMAX")
	private BigDecimal nGrenzwertmax;

	@Column(name = "N_GRENZWERT")
	private BigDecimal nGrenzwert;

	@Column(name = "X_BEMERKUNG")
	private String xBemerkung;
	
	public String getXBemerkung() {
		return xBemerkung;
	}

	public void setXBemerkung(String xBemerkung) {
		this.xBemerkung = xBemerkung;
	}

	@Column(name = "B_VERSTECKT")
	private Short bVersteckt;

	public Short getBVersteckt() {
		return bVersteckt;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	private static final long serialVersionUID = 1L;

	public Geraet() {
		super();
	}

	public Geraet(Integer id, String mandantCNr, 
			Integer standortIId, Integer geraetetypIId, Short bAufwand,
			Short bMesswertabsolut, Short bVersteckt) {
		setIId(id);
		setMandantCNr(mandantCNr);
		setStandortIId(standortIId);
		setGeraetetypIId(geraetetypIId);
		setBAufwand(bAufwand);
		setBMesswertabsolut(bMesswertabsolut);
		setBVersteckt(bVersteckt);

	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Integer getStandortIId() {
		return standortIId;
	}

	public void setStandortIId(Integer standortIId) {
		this.standortIId = standortIId;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
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

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

}

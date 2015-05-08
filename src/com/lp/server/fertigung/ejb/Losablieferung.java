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
package com.lp.server.fertigung.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.util.Helper;

@NamedQueries( {
		@NamedQuery(name = "LosablieferungfindByLosIId", query = "SELECT OBJECT(o) FROM Losablieferung o WHERE o.losIId=?1 ORDER BY o.tAendern ASC"),
		@NamedQuery(name = "LosablieferungfindByLosIIdTAendernBis", query = "SELECT OBJECT(o) FROM Losablieferung o WHERE o.losIId=?1 AND o.tAendern>=?2") })
@Entity
@Table(name = "FERT_LOSABLIEFERUNG")
public class Losablieferung implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "N_MENGE")
	private BigDecimal nMenge;

	@Column(name = "N_GESTEHUNGSPREIS")
	private BigDecimal nGestehungspreis;

	@Column(name = "N_MATERIALWERT")
	private BigDecimal nMaterialwert;

	@Column(name = "N_ARBEITSZEITWERT")
	private BigDecimal nArbeitszeitwert;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "N_MATERIALWERTDETAILLIERT")
	private BigDecimal nMaterialwertdetailliert;

	@Column(name = "N_ARBEITSZEITWERTDETAILLIERT")
	private BigDecimal nArbeitszeitwertdetailliert;

	@Column(name = "B_GESTEHUNGSPREISNEUBERECHNEN")
	private Short bGestehungspreisneuberechnen;

	@Column(name = "LOS_I_ID")
	private Integer losIId;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	private static final long serialVersionUID = 1L;

	public Losablieferung() {
		super();
	}

	public Losablieferung(Integer id, 
			Integer losIId2, 
			BigDecimal menge,
			BigDecimal gestehungspreis, 
			BigDecimal materialwert,
			BigDecimal arbeitszeitwert,
			Integer personalIIdAendern2,
			BigDecimal materialwertdetailliert,
			BigDecimal arbeitszeitwertdetailliert) {
		setIId(id);
		setLosIId(losIId2);
		setNMenge(menge);
		setNGestehungspreis(gestehungspreis);
		setNMaterialwert(materialwert);
		setNArbeitszeitwert(arbeitszeitwert);
		setPersonalIIdAendern(personalIIdAendern2);
		setNMaterialwertdetailliert(materialwertdetailliert);
		setNArbeitszeitwertdetailliert(arbeitszeitwertdetailliert);
	    // Setzen der NOT NULL felder
	    Timestamp now = new Timestamp(System.currentTimeMillis());
	    this.setTAendern(now);
	    setBGestehungspreisneuberechnen(Helper.boolean2Short(false));
	}
	
	public Losablieferung(Integer id, 
			Integer losIId2, 
			BigDecimal menge,
			BigDecimal gestehungspreis, 
			BigDecimal materialwert,
			BigDecimal arbeitszeitwert,
			Integer personalIIdAendern2,
			BigDecimal materialwertdetailliert,
			BigDecimal arbeitszeitwertdetailliert,
			Short gestehungspreisneuberechnen) {
		setIId(id);
		setLosIId(losIId2);
		setNMenge(menge);
		setNGestehungspreis(gestehungspreis);
		setNMaterialwert(materialwert);
		setNArbeitszeitwert(arbeitszeitwert);
		setPersonalIIdAendern(personalIIdAendern2);
		setNMaterialwertdetailliert(materialwertdetailliert);
		setNArbeitszeitwertdetailliert(arbeitszeitwertdetailliert);
	    // Setzen der NOT NULL felder
	    Timestamp now = new Timestamp(System.currentTimeMillis());
	    this.setTAendern(now);
	    setBGestehungspreisneuberechnen(gestehungspreisneuberechnen);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public BigDecimal getNMenge() {
		return this.nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public BigDecimal getNGestehungspreis() {
		return this.nGestehungspreis;
	}

	public void setNGestehungspreis(BigDecimal nGestehungspreis) {
		this.nGestehungspreis = nGestehungspreis;
	}

	public BigDecimal getNMaterialwert() {
		return this.nMaterialwert;
	}

	public void setNMaterialwert(BigDecimal nMaterialwert) {
		this.nMaterialwert = nMaterialwert;
	}

	public BigDecimal getNArbeitszeitwert() {
		return this.nArbeitszeitwert;
	}

	public void setNArbeitszeitwert(BigDecimal nArbeitszeitwert) {
		this.nArbeitszeitwert = nArbeitszeitwert;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public BigDecimal getNMaterialwertdetailliert() {
		return this.nMaterialwertdetailliert;
	}

	public void setNMaterialwertdetailliert(BigDecimal nMaterialwertdetailliert) {
		this.nMaterialwertdetailliert = nMaterialwertdetailliert;
	}

	public BigDecimal getNArbeitszeitwertdetailliert() {
		return this.nArbeitszeitwertdetailliert;
	}

	public void setNArbeitszeitwertdetailliert(
			BigDecimal nArbeitszeitwertdetailliert) {
		this.nArbeitszeitwertdetailliert = nArbeitszeitwertdetailliert;
	}

	public Short getBGestehungspreisneuberechnen() {
		return this.bGestehungspreisneuberechnen;
	}

	public void setBGestehungspreisneuberechnen(
			Short bGestehungspreisneuberechnen) {
		this.bGestehungspreisneuberechnen = bGestehungspreisneuberechnen;
	}

	public Integer getLosIId() {
		return this.losIId;
	}

	public void setLosIId(Integer losIId) {
		this.losIId = losIId;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

}

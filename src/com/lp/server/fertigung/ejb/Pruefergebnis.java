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

@NamedQueries({
		@NamedQuery(name = "PruefergebnisFindByLosablieferungIIdLospruefplanIId", query = "SELECT OBJECT(o) FROM Pruefergebnis o WHERE o.losablieferungIId = ?1 AND  o.lospruefplanIId = ?2 "),
		@NamedQuery(name = "PruefergebnisFindByLosablieferungIId", query = "SELECT OBJECT(o) FROM Pruefergebnis o WHERE o.losablieferungIId = ?1") })
@Entity
@Table(name = "FERT_PRUEFERGEBINS")
public class Pruefergebnis implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "LOSABLIEFERUNG_I_ID")
	private Integer losablieferungIId;

	@Column(name = "LOSPRUEFPLAN_I_ID")
	private Integer lospruefplanIId;

	@Column(name = "N_CRIMPHOEHE_DRAHT")
	private BigDecimal nCrimphoeheDraht;

	@Column(name = "N_CRIMPHOEHE_ISOLATION")
	private BigDecimal nCrimphoeheIsolation;

	@Column(name = "N_CRIMPBREITE_DRAHT")
	private BigDecimal nCrimpbreitDraht;

	@Column(name = "N_WERT")
	private BigDecimal nWert;

	@Column(name = "N_ABZUGSKRAFT_LITZE")
	private BigDecimal nAbzugskraftLitze;
	@Column(name = "N_ABZUGSKRAFT_LITZE2")
	private BigDecimal nAbzugskraftLitze2;
	
	@Column(name = "C_WERT")
	private String cWert;
	
	public BigDecimal getNAbzugskraftLitze() {
		return nAbzugskraftLitze;
	}

	public void setNAbzugskraftLitze(BigDecimal nAbzugskraftLitze) {
		this.nAbzugskraftLitze = nAbzugskraftLitze;
	}

	public BigDecimal getNAbzugskraftLitze2() {
		return nAbzugskraftLitze2;
	}

	public void setNAbzugskraftLitze2(BigDecimal nAbzugskraftLitze2) {
		this.nAbzugskraftLitze2 = nAbzugskraftLitze2;
	}
	
	@Column(name = "B_WERT")
	private Short bWert;

	public BigDecimal getNWert() {
		return nWert;
	}

	public void setNWert(BigDecimal nWert) {
		this.nWert = nWert;
	}

	public Short getBWert() {
		return bWert;
	}

	public void setBWert(Short bWert) {
		this.bWert = bWert;
	}

	@Column(name = "N_CRIMPBREITE_ISOLATION")
	private BigDecimal nCrimpbreiteIsolation;

	public BigDecimal getNCrimpbreiteIsolation() {
		return nCrimpbreiteIsolation;
	}

	public void setNCrimpbreiteIsolation(BigDecimal nCrimpbreiteIsolation) {
		this.nCrimpbreiteIsolation = nCrimpbreiteIsolation;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
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

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	private static final long serialVersionUID = 1L;

	public Pruefergebnis() {
		super();
	}

	public Pruefergebnis(Integer id, Integer losablieferungIId,
			Integer lospruefplanIId,

			Integer personalIIdAnlegen, Timestamp tAnlegen,
			Integer personalIIdAendern, Timestamp tAendern) {
		setIId(id);

		setLosablieferungIId(losablieferungIId);
		setLospruefplanIId(lospruefplanIId);

		setPersonalIIdAendern(personalIIdAendern);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setTAendern(tAendern);
		setTAnlegen(tAnlegen);

	}

	public Integer getLosablieferungIId() {
		return losablieferungIId;
	}

	public void setLosablieferungIId(Integer losablieferungIId) {
		this.losablieferungIId = losablieferungIId;
	}

	public Integer getLospruefplanIId() {
		return lospruefplanIId;
	}

	public void setLospruefplanIId(Integer lospruefplanIId) {
		this.lospruefplanIId = lospruefplanIId;
	}

	public BigDecimal getNCrimphoeheDraht() {
		return nCrimphoeheDraht;
	}

	public void setNCrimphoeheDraht(BigDecimal nCrimphoeheDraht) {
		this.nCrimphoeheDraht = nCrimphoeheDraht;
	}

	public BigDecimal getNCrimphoeheIsolation() {
		return nCrimphoeheIsolation;
	}

	public void setNCrimphoeheIsolation(BigDecimal nCrimphoeheIsolation) {
		this.nCrimphoeheIsolation = nCrimphoeheIsolation;
	}

	public BigDecimal getNCrimpbreitDraht() {
		return nCrimpbreitDraht;
	}

	public void setNCrimpbreitDraht(BigDecimal nCrimpbreitDraht) {
		this.nCrimpbreitDraht = nCrimpbreitDraht;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCWert() {
		return cWert;
	}
	
	public void setCWert(String cWert) {
		this.cWert = cWert;
	}
}

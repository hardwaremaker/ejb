
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
package com.lp.server.angebotstkl.ejb;

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
		@NamedQuery(name = "AgstklmengenstaffelFindByAgstklIIdNMenge", query = "SELECT OBJECT (o) FROM Agstklmengenstaffel o WHERE o.agstklIId = ?1 AND o.nMenge = ?2"),
		@NamedQuery(name = "AgstklmengenstaffelFindByAgstklIIdKleinerGleichNMenge", query = "SELECT OBJECT (o) FROM Agstklmengenstaffel o WHERE o.agstklIId = ?1 AND o.nMenge <= ?2 ORDER BY o.nMenge DESC"),
		@NamedQuery(name = "AgstklmengenstaffelFindByAgstklIId", query = "SELECT OBJECT (o) FROM Agstklmengenstaffel o WHERE o.agstklIId = ?1 ORDER BY o.nMenge ASC")
		 })
@Entity
@Table(name = "AS_AGSTKLMENGENSTAFFEL")
public class Agstklmengenstaffel implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "AGSTKL_I_ID")
	private Integer agstklIId;

	
	@Column(name = "N_MENGE")
	private BigDecimal nMenge;

	
	@Column(name = "N_MATERIALEINSATZ_LIEF1")
	private BigDecimal nMaterialeinsatzLief1;

	@Column(name = "N_AZEINSATZ_LIEF1")
	private BigDecimal nAzeinsatzLief1;

	@Column(name = "N_VKPREIS")
	private BigDecimal nVkpreis;

	@Column(name = "N_VKPREIS_GEWAEHLT")
	private BigDecimal nVkpreisGewaehlt;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;
	
	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;


	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}
	

	public BigDecimal getNMaterialeinsatzLief1() {
		return nMaterialeinsatzLief1;
	}

	public void setNMaterialeinsatzLief1(BigDecimal nMaterialeinsatzLief1) {
		this.nMaterialeinsatzLief1 = nMaterialeinsatzLief1;
	}

	public BigDecimal getNAzeinsatzLief1() {
		return nAzeinsatzLief1;
	}

	public void setNAzeinsatzLief1(BigDecimal nAzeinsatzLief1) {
		this.nAzeinsatzLief1 = nAzeinsatzLief1;
	}

	public BigDecimal getNVkpreis() {
		return nVkpreis;
	}

	public void setNVkpreis(BigDecimal nVkpreis) {
		this.nVkpreis = nVkpreis;
	}

	public BigDecimal getNVkpreisGewaehlt() {
		return nVkpreisGewaehlt;
	}

	public void setNVkpreisGewaehlt(BigDecimal nVkpreisGewaehlt) {
		this.nVkpreisGewaehlt = nVkpreisGewaehlt;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}
	
	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	private static final long serialVersionUID = 1L;

	public Agstklmengenstaffel() {

	}

	public Agstklmengenstaffel(Integer iId, Integer agstklIId, BigDecimal nMenge,Integer personalIIdAendern, Timestamp tAendern) {
		setIId(iId);
		setAgstklIId(agstklIId);
		setNMenge(nMenge);
		setTAendern(tAendern);
		setPersonalIIdAendern(personalIIdAendern);
		
	}

	public Integer getAgstklIId() {
		return agstklIId;
	}

	public void setAgstklIId(Integer agstklIId) {
		this.agstklIId = agstklIId;
	}

	public BigDecimal getNMenge() {
		return this.nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}


}

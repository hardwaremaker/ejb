package com.lp.server.personal.ejb;

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
		@NamedQuery(name = "ReiseverrechnetFindByRechnungpositionIId", query = "SELECT OBJECT(o) FROM Reiseverrechnet o WHERE o.rechnungpositionIId=?1"),
		@NamedQuery(name = "ReiseverrechnetFindByReiseIId", query = "SELECT OBJECT(o) FROM Reiseverrechnet o WHERE o.reiseIId=?1") })
@Entity
@Table(name = "PERS_REISEVERRECHNET")
public class Reiseverrechnet implements Serializable {
	public Integer getReiseIId() {
		return reiseIId;
	}

	public void setReiseIId(Integer reiseIId) {
		this.reiseIId = reiseIId;
	}

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "N_BETRAG")
	private BigDecimal nBetrag;
	@Column(name = "N_KILOMETER")
	private BigDecimal nKilometer;
	
	@Column(name = "N_SPESEN")
	private BigDecimal nSpesen;

	public BigDecimal getNSpesen() {
		return nSpesen;
	}

	public void setNSpesen(BigDecimal nSpesen) {
		this.nSpesen = nSpesen;
	}

	public BigDecimal getNKilometer() {
		return nKilometer;
	}

	public void setNKilometer(BigDecimal nKilometer) {
		this.nKilometer = nKilometer;
	}

	public BigDecimal getNBetrag() {
		return nBetrag;
	}

	public void setNBetrag(BigDecimal nBetrag) {
		this.nBetrag = nBetrag;
	}

	public Integer getRechnungpositionIId() {
		return rechnungpositionIId;
	}

	public void setRechnungpositionIId(Integer rechnungpositionIId) {
		this.rechnungpositionIId = rechnungpositionIId;
	}

	@Column(name = "REISE_I_ID")
	private Integer reiseIId;

	@Column(name = "RECHNUNGPOSITION_I_ID")
	private Integer rechnungpositionIId;

	private static final long serialVersionUID = 1L;

	public Reiseverrechnet() {
		super();
	}

	public Reiseverrechnet(Integer id, Integer rechnungpositionIId, Integer reiseIId, BigDecimal nBetrag, BigDecimal nKilometer, BigDecimal nSpesen) {
		setIId(id);
		setReiseIId(reiseIId);
		setRechnungpositionIId(rechnungpositionIId);
		setNBetrag(nBetrag);
		setNKilometer(nKilometer);
		setNSpesen(nSpesen);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

}

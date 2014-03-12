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
package com.lp.server.personal.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "GleitzeitsaldofindByPersonalIIdIJahrIMonat", query = "SELECT OBJECT(C) FROM Gleitzeitsaldo c WHERE c.personalIId = ?1 AND c.iJahr = ?2 AND c.iMonat = ?3"),
		@NamedQuery(name = "GleitzeitsaldofindLetztenGleitzeitsaldo", query = "SELECT OBJECT(C) FROM Gleitzeitsaldo c WHERE c.personalIId = ?1 AND c.iJahr <= ?2 ORDER BY c.iJahr DESC,c.iMonat DESC") })
@Entity
@Table(name = "PERS_GLEITZEITSALDO")
public class Gleitzeitsaldo implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "I_JAHR")
	private Integer iJahr;

	@Column(name = "I_MONAT")
	private Integer iMonat;

	@Column(name = "T_ABRECHNUNGSSTICHTAG")
	private Timestamp tAbrechnungsstichtag;

	@Column(name = "N_SALDOMEHRSTUNDEN")
	private BigDecimal nSaldomehrstunden;

	@Column(name = "N_SALDOUESTFREI50")
	private BigDecimal nSaldouestfrei50;

	@Column(name = "N_SALDOUESTPFLICHTIG50")
	private BigDecimal nSaldouestpflichtig50;

	@Column(name = "N_SALDOUESTFREI100")
	private BigDecimal nSaldouestfrei100;

	@Column(name = "N_SALDOUESTPFLICHTIG100")
	private BigDecimal nSaldouestpflichtig100;

	@Column(name = "B_GESPERRT")
	private Short bGesperrt;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "N_SALDO")
	private BigDecimal nSaldo;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID")
	private Integer personalIId;

	public BigDecimal getNSaldouest200() {
		return nSaldouest200;
	}

	public void setNSaldouest200(BigDecimal saldouest200) {
		nSaldouest200 = saldouest200;
	}

	@Column(name = "N_SALDOUEST200")
	private BigDecimal nSaldouest200;
	
	private static final long serialVersionUID = 1L;

	public Gleitzeitsaldo() {
		super();
	}

	public Gleitzeitsaldo(Integer id, 
			Integer personalIId2,
			Integer jahr,
			Integer monat,
			Integer personalIIdAendern2,
			BigDecimal saldo) {
		setIId(id);
		setPersonalIId(personalIId2);
		setIJahr(jahr);
		setIMonat(monat);
		setPersonalIIdAendern(personalIIdAendern2);
		setTAendern(new Timestamp(System.currentTimeMillis()));
		setNSaldomehrstunden(new BigDecimal(0));
		setNSaldouestfrei100(new BigDecimal(0));
		setNSaldouestfrei50(new BigDecimal(0));
		setNSaldouestpflichtig100(new BigDecimal(0));
		setNSaldouestpflichtig50(new BigDecimal(0));
		setNSaldouest200(new BigDecimal(0));
		setBGesperrt(new Short((short) 0));
		setNSaldo(saldo);
	}
	
	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getIJahr() {
		return this.iJahr;
	}

	public void setIJahr(Integer iJahr) {
		this.iJahr = iJahr;
	}

	public Integer getIMonat() {
		return this.iMonat;
	}

	public void setIMonat(Integer iMonat) {
		this.iMonat = iMonat;
	}

	public Timestamp getTAbrechnungsstichtag() {
		return this.tAbrechnungsstichtag;
	}

	public void setTAbrechnungsstichtag(Timestamp tAbrechnungsstichtag) {
		this.tAbrechnungsstichtag = tAbrechnungsstichtag;
	}

	public BigDecimal getNSaldomehrstunden() {
		return this.nSaldomehrstunden;
	}

	public void setNSaldomehrstunden(BigDecimal nSaldomehrstunden) {
		this.nSaldomehrstunden = nSaldomehrstunden;
	}

	public BigDecimal getNSaldouestfrei50() {
		return this.nSaldouestfrei50;
	}

	public void setNSaldouestfrei50(BigDecimal nSaldouestfrei50) {
		this.nSaldouestfrei50 = nSaldouestfrei50;
	}

	public BigDecimal getNSaldouestpflichtig50() {
		return this.nSaldouestpflichtig50;
	}

	public void setNSaldouestpflichtig50(BigDecimal nSaldouestpflichtig50) {
		this.nSaldouestpflichtig50 = nSaldouestpflichtig50;
	}

	public BigDecimal getNSaldouestfrei100() {
		return this.nSaldouestfrei100;
	}

	public void setNSaldouestfrei100(BigDecimal nSaldouestfrei100) {
		this.nSaldouestfrei100 = nSaldouestfrei100;
	}

	public BigDecimal getNSaldouestpflichtig100() {
		return this.nSaldouestpflichtig100;
	}

	public void setNSaldouestpflichtig100(BigDecimal nSaldouestpflichtig100) {
		this.nSaldouestpflichtig100 = nSaldouestpflichtig100;
	}

	public Short getBGesperrt() {
		return this.bGesperrt;
	}

	public void setBGesperrt(Short bGesperrt) {
		this.bGesperrt = bGesperrt;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public BigDecimal getNSaldo() {
		return this.nSaldo;
	}

	public void setNSaldo(BigDecimal nSaldo) {
		this.nSaldo = nSaldo;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getPersonalIId() {
		return this.personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}
}

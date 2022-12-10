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
package com.lp.server.stueckliste.ejb;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.util.Helper;

@NamedQueries({
		@NamedQuery(name = "StklparameterFindByStuecklisteIId", query = "SELECT OBJECT (o) FROM Stklparameter o WHERE o.stuecklisteIId=?1 ORDER BY o.iSort"),
		@NamedQuery(name = "StklparameterFindByStuecklisteIIdCNr", query = "SELECT OBJECT (o) FROM Stklparameter o WHERE o.stuecklisteIId=?1 AND o.cNR=?2"),
		@NamedQuery(name = "StklparameterSelectNextReihung", query = "SELECT MAX (o.iSort) FROM Stklparameter o WHERE o.stuecklisteIId = ?1") })
@Entity
@Table(name = "STK_STKLPARAMETER")
public class Stklparameter implements Serializable {

	public String getCNr() {
		return cNR;
	}

	public void setCNr(String cNR) {
		this.cNR = cNR;
	}

	public String getCTyp() {
		return cTyp;
	}

	public void setCTyp(String cTyp) {
		this.cTyp = cTyp;
	}

	public BigDecimal getNMin() {
		return nMin;
	}

	public void setnMin(BigDecimal nMin) {
		this.nMin = nMin;
	}

	public BigDecimal getNMax() {
		return nMax;
	}

	public void setNMax(BigDecimal nMax) {
		this.nMax = nMax;
	}

	public Integer getISort() {
		return iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public Integer getStuecklisteIId() {
		return stuecklisteIId;
	}

	public void setStuecklisteIId(Integer stuecklisteIId) {
		this.stuecklisteIId = stuecklisteIId;
	}

	@Column(name = "C_WERT")
	private String cWert;

	public String getCWert() {
		return this.cWert;
	}

	public void setCWert(String cWert) {
		this.cWert = cWert;
	}


	
	@Column(name = "B_MANDATORY")
	private Short bMandatory;

	public Short getBMandatory() {
		return bMandatory;
	}

	public void setBMandatory(Short bMandatory) {
		this.bMandatory = bMandatory;
	}

	@Column(name = "B_COMBOBOX")
	private Short bCombobox;

	public Short getBCombobox() {
		return bCombobox;
	}

	public void setBCombobox(Short bCombobox) {
		this.bCombobox = bCombobox;
	}

	@Column(name = "C_BEREICH")
	private String cBereich;

	public String getCBereich() {
		return cBereich;
	}

	public void setCBereich(String cBereich) {
		this.cBereich = cBereich;
	}

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNR;

	@Column(name = "C_TYP")
	private String cTyp;

	@Column(name = "N_MIN")
	private BigDecimal nMin;

	@Column(name = "N_MAX")
	private BigDecimal nMax;

	@Column(name = "I_SORT")
	private Integer iSort;

	@Column(name = "STUECKLISTE_I_ID")
	private Integer stuecklisteIId;

	private static final long serialVersionUID = 1L;

	public Stklparameter() {
		super();
	}

	public Stklparameter(Integer id, Integer stuecklisteIId, String cNr,
			String typ, Integer iSort, Short bCombobox, Short bMandatory) {
		setIId(id);
		setStuecklisteIId(stuecklisteIId);
		setCNr(cNr);
		setCTyp(typ);
		setISort(iSort);
		setBCombobox(bCombobox);
		setBMandatory(bMandatory);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

}

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
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "LospruefplanFindByLosIId", query = "SELECT OBJECT(o) FROM Lospruefplan o WHERE o.losIId = ?1 ORDER BY o.iSort ASC"),
		@NamedQuery(name = "LospruefplanejbSelectNextReihung", query = "SELECT MAX (o.iSort) FROM Lospruefplan o WHERE o.losIId = ?1")})
@Entity
@Table(name = "FERT_LOSPRUEFPLAN")
public class Lospruefplan implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "LOS_I_ID")
	private Integer losIId;

	@Column(name = "PRUEFKOMBINATION_I_ID")
	private Integer pruefkombinationId;
	
	@Column(name = "B_DOPPELANSCHLAG")
	private Short bDoppelanschlag;

	public Short getBDoppelanschlag() {
		return bDoppelanschlag;
	}

	public void setBDoppelanschlag(Short bDoppelanschlag) {
		this.bDoppelanschlag = bDoppelanschlag;
	}
	
	public Integer getPruefkombinationId() {
		return pruefkombinationId;
	}

	public void setPruefkombinationId(Integer pruefkombinationId) {
		this.pruefkombinationId = pruefkombinationId;
	}
	
	@Column(name = "I_SORT")
	private Integer iSort;

	
	public Integer getISort() {
		return iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}
	
	
	@Column(name = "PRUEFART_I_ID")
	private Integer pruefartIId;

	public Integer getPruefartIId() {
		return pruefartIId;
	}

	public void setPruefartIId(Integer pruefartIId) {
		this.pruefartIId = pruefartIId;
	}

	@Column(name = "LOSSOLLMATERIAL_I_ID_KONTAKT")
	private Integer lossollmaterialIIdKontakt;

	@Column(name = "LOSSOLLMATERIAL_I_ID_LITZE")
	private Integer lossollmaterialIIdLitze;

	
	@Column(name = "LOSSOLLMATERIAL_I_ID_LITZE2")
	private Integer lossollmaterialIIdLitze2;

	public Integer getLossollmaterialIIdLitze2() {
		return lossollmaterialIIdLitze2;
	}

	public void setLossollmaterialIIdLitze2(Integer lossollmaterialIIdLitze2) {
		this.lossollmaterialIIdLitze2 = lossollmaterialIIdLitze2;
	}

	@Column(name = "VERSCHLEISSTEIL_I_ID")
	private Integer verschleissteilIId;

	public Integer getVerschleissteilIId() {
		return verschleissteilIId;
	}

	public void setVerschleissteilIId(Integer verschleissteilIId) {
		this.verschleissteilIId = verschleissteilIId;
	}

	public Integer getLosIId() {
		return losIId;
	}

	public void setLosIId(Integer losIId) {
		this.losIId = losIId;
	}

	public Integer getLossollmaterialIIdKontakt() {
		return lossollmaterialIIdKontakt;
	}

	public void setLossollmaterialIIdKontakt(Integer lossollmaterialIIdKontakt) {
		this.lossollmaterialIIdKontakt = lossollmaterialIIdKontakt;
	}

	public Integer getLossollmaterialIIdLitze() {
		return lossollmaterialIIdLitze;
	}

	public void setLossollmaterialIIdLitze(Integer lossollmaterialIIdLitze) {
		this.lossollmaterialIIdLitze = lossollmaterialIIdLitze;
	}

	public Integer getWerkzeugIId() {
		return werkzeugIId;
	}

	public void setWerkzeugIId(Integer werkzeugIId) {
		this.werkzeugIId = werkzeugIId;
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

	@Column(name = "WERKZEUG_I_ID")
	private Integer werkzeugIId;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	private static final long serialVersionUID = 1L;

	public Lospruefplan() {
		super();
	}

	public Lospruefplan(Integer id, Integer losIId, Integer pruefartIId,
			Integer lossollmaterialIIdKontakt,Short bDoppelanschlag, Integer iSort,
			Integer personalIIdAnlegen, Timestamp tAnlegen,
			Integer personalIIdAendern, Timestamp tAendern) {
		setIId(id);
		setLosIId(losIId);
		setPruefartIId(pruefartIId);
		setLossollmaterialIIdKontakt(lossollmaterialIIdKontakt);
		setBDoppelanschlag(bDoppelanschlag);
		setISort(iSort);

		setPersonalIIdAendern(personalIIdAendern);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setTAendern(tAendern);
		setTAnlegen(tAnlegen);

	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

}

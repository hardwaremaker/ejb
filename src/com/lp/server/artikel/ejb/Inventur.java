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
package com.lp.server.artikel.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "InventurfindByTInventurdatumMandantCNr", query = "SELECT OBJECT (o) FROM Inventur o WHERE o.tInventurdatum=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "InventurfindDurchgefuehrteInventurenEinesZeitraums", query = "SELECT OBJECT (o) FROM Inventur o WHERE o.tInventurdatum>=?1 AND  o.tInventurdatum<?2 AND o.bInventurdurchgefuehrt=1 AND o.mandantCNr=?3 ORDER BY o.tInventurdatum ASC"),
		@NamedQuery(name = "InventurfindInventurenNachDatum", query = "SELECT OBJECT (o) FROM Inventur o WHERE o.tInventurdatum > ?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = InventurQuery.ByMandantCNrOffeneInventuren, query="SELECT OBJECT (o) from Inventur o WHERE o.mandantCNr=:mandant AND o.bInventurdurchgefuehrt=0")})
@Entity
@Table(name = "WW_INVENTUR")
public class Inventur implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "T_INVENTURDATUM")
	private Timestamp tInventurdatum;

	@Column(name = "B_INVENTURDURCHGEFUEHRT")
	private Short bInventurdurchgefuehrt;

	@Column(name = "T_INVENTURDURCHGEFUEHRT")
	private Timestamp tInventurdurchgefuehrt;

	@Column(name = "B_ABWERTUNGDURCHGEFUEHRT")
	private Short bAbwertungdurchgefuehrt;

	@Column(name = "T_ABWERTUNGDURCHGEFUEHRT")
	private Timestamp tAbwertungdurchgefuehrt;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "PERSONAL_I_ID_ABWERTUNGDURCHGEFUEHRT")
	private Integer personalIIdAbwertungdurchgefuehrt;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_INVENTURDURCHGEFUEHRT")
	private Integer personalIIdInventurdurchgefuehrt;

	@Column(name = "LAGER_I_ID")
	private Integer lagerIId;

	private static final long serialVersionUID = 1L;

	public Inventur() {
		super();
	}

	public Inventur(Integer id,
			Timestamp inventurdatum, 
			Short inventurdurchgefuehrt,
			Short abwertungdurchgefuehrt,
			String bez,
			Integer personalIIdAendern2,
			String mandantCNr2) {
		setIId(id);
		setTInventurdatum(inventurdatum);
		setCBez(bez);
		setPersonalIIdAendern(personalIIdAendern2);
		setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));
		setMandantCNr(mandantCNr2);
		setBInventurdurchgefuehrt(inventurdurchgefuehrt);
		setBAbwertungdurchgefuehrt(abwertungdurchgefuehrt);

	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Timestamp getTInventurdatum() {
		return this.tInventurdatum;
	}

	public void setTInventurdatum(Timestamp tInventurdatum) {
		this.tInventurdatum = tInventurdatum;
	}

	public Short getBInventurdurchgefuehrt() {
		return this.bInventurdurchgefuehrt;
	}

	public void setBInventurdurchgefuehrt(Short bInventurdurchgefuehrt) {
		this.bInventurdurchgefuehrt = bInventurdurchgefuehrt;
	}

	public Timestamp getTInventurdurchgefuehrt() {
		return this.tInventurdurchgefuehrt;
	}

	public void setTInventurdurchgefuehrt(Timestamp tInventurdurchgefuehrt) {
		this.tInventurdurchgefuehrt = tInventurdurchgefuehrt;
	}

	public Short getBAbwertungdurchgefuehrt() {
		return this.bAbwertungdurchgefuehrt;
	}

	public void setBAbwertungdurchgefuehrt(Short bAbwertungdurchgefuehrt) {
		this.bAbwertungdurchgefuehrt = bAbwertungdurchgefuehrt;
	}

	public Timestamp getTAbwertungdurchgefuehrt() {
		return this.tAbwertungdurchgefuehrt;
	}

	public void setTAbwertungdurchgefuehrt(Timestamp tAbwertungdurchgefuehrt) {
		this.tAbwertungdurchgefuehrt = tAbwertungdurchgefuehrt;
	}

	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Integer getPersonalIIdAbwertungdurchgefuehrt() {
		return this.personalIIdAbwertungdurchgefuehrt;
	}

	public void setPersonalIIdAbwertungdurchgefuehrt(
			Integer personalIIdAbwertungdurchgefuehrt) {
		this.personalIIdAbwertungdurchgefuehrt = personalIIdAbwertungdurchgefuehrt;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getPersonalIIdInventurdurchgefuehrt() {
		return this.personalIIdInventurdurchgefuehrt;
	}

	public void setPersonalIIdInventurdurchgefuehrt(
			Integer personalIIdInventurdurchgefuehrt) {
		this.personalIIdInventurdurchgefuehrt = personalIIdInventurdurchgefuehrt;
	}

	public Integer getLagerIId() {
		return this.lagerIId;
	}

	public void setLagerIId(Integer lagerIId) {
		this.lagerIId = lagerIId;
	}

}

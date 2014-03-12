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
import java.sql.Time;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "ZeitmodelltagfindSollzeitZuTagesart", query = "SELECT OBJECT(o) FROM Zeitmodelltag o WHERE o.zeitmodellIId = ?1 AND o.tagesartIId = ?2"),
		@NamedQuery(name = "ZeitmodelltagfindByZeitmodellIId", query = "SELECT OBJECT(o) FROM Zeitmodelltag o WHERE o.zeitmodellIId = ?1"),
		@NamedQuery(name = "ZeitmodelltagfindByZeitmodellIIdTagesartIId", query = "SELECT OBJECT(C) FROM Zeitmodelltag c WHERE c.zeitmodellIId = ?1 AND  c.tagesartIId = ?2") })
@Entity
@Table(name = "PERS_ZEITMODELLTAG")
public class Zeitmodelltag implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "I_MINDESTPAUSENANZAHL")
	private Integer iMindestpausenanzahl;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "U_SOLLZEIT")
	private Time uSollzeit;

	@Column(name = "U_MINDESTPAUSE")
	private Time uMindestpause;

	
	
	@Column(name = "U_BEGINN")
	private Time uBeginn;

	@Column(name = "U_ENDE")
	private Time uEnde;

	@Column(name = "U_UEBERSTD")
	private Time uUeberstd;

	@Column(name = "U_MEHRSTD")
	private Time uMehrstd;

	@Column(name = "U_ERLAUBTEANWESENHEITSZEIT")
	private Time uErlaubteanwesenheitszeit;

	@Column(name = "U_AUTOPAUSEAB")
	private Time uAutopauseab;

	@Column(name = "U_MINDESTPAUSE2")
	private Time uMindestpause2;

	
	@Column(name = "U_AUTOPAUSEAB2")
	private Time uAutopauseab2;

	
	public Time getUMindestpause2() {
		return uMindestpause2;
	}

	public void setUMindestpause2(Time uMindestpause2) {
		this.uMindestpause2 = uMindestpause2;
	}

	public Time getUAutopauseab2() {
		return uAutopauseab2;
	}

	public void setUAutopauseab2(Time uAutopauseab2) {
		this.uAutopauseab2 = uAutopauseab2;
	}

	
	
	@Column(name = "U_MINDESTPAUSE3")
	private Time uMindestpause3;

	
	@Column(name = "U_AUTOPAUSEAB3")
	private Time uAutopauseab3;

	
	public Time getUMindestpause3() {
		return uMindestpause3;
	}

	public void setUMindestpause3(Time uMindestpause3) {
		this.uMindestpause3 = uMindestpause3;
	}

	public Time getUAutopauseab3() {
		return uAutopauseab3;
	}

	public void setUAutopauseab3(Time uAutopauseab3) {
		this.uAutopauseab3 = uAutopauseab3;
	}
	
	
	
	@Column(name = "I_RUNDUNGBEGINN")
	private Integer iRundungbeginn;

	@Column(name = "I_RUNDUNGENDE")
	private Integer iRundungende;

	@Column(name = "B_RUNDESONDERTAETIGKEITEN")
	private Short bRundesondertaetigkeiten;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "TAGESART_I_ID")
	private Integer tagesartIId;

	@Column(name = "ZEITMODELL_I_ID")
	private Integer zeitmodellIId;

	private static final long serialVersionUID = 1L;

	public Zeitmodelltag() {
		super();
	}

	public Zeitmodelltag(Integer id,
			Integer zeitmodellIId,
			Integer tagesartIId,
			Integer personalIIdAendern2,
			Time sollzeit, 
			Integer rundungbeginn,
			Integer rundungende,
			Short rundesondertaetigkeiten) {
		setIId(id);
		setZeitmodellIId(zeitmodellIId);
		setTagesartIId(tagesartIId);
		setUSollzeit(sollzeit);
		setPersonalIIdAendern(personalIIdAendern2);
		setTAendern(new Timestamp(System.currentTimeMillis()));
		setIRundungbeginn(rundungbeginn);
		setIRundungende(rundungende);
		setBRundesondertaetigkeiten(rundesondertaetigkeiten);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getIMindestpausenanzahl() {
		return this.iMindestpausenanzahl;
	}

	public void setIMindestpausenanzahl(Integer iMindestpausenanzahl) {
		this.iMindestpausenanzahl = iMindestpausenanzahl;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Time getUSollzeit() {
		return this.uSollzeit;
	}

	public void setUSollzeit(Time uSollzeit) {
		this.uSollzeit = uSollzeit;
	}

	public Time getUMindestpause() {
		return this.uMindestpause;
	}

	public void setUMindestpause(Time uMindestpause) {
		this.uMindestpause = uMindestpause;
	}

	public Time getUBeginn() {
		return this.uBeginn;
	}

	public void setUBeginn(Time uBeginn) {
		this.uBeginn = uBeginn;
	}

	public Time getUEnde() {
		return this.uEnde;
	}

	public void setUEnde(Time uEnde) {
		this.uEnde = uEnde;
	}

	public Time getUUeberstd() {
		return this.uUeberstd;
	}

	public void setUUeberstd(Time uUeberstd) {
		this.uUeberstd = uUeberstd;
	}

	public Time getUMehrstd() {
		return this.uMehrstd;
	}

	public void setUMehrstd(Time uMehrstd) {
		this.uMehrstd = uMehrstd;
	}

	public Time getUErlaubteanwesenheitszeit() {
		return this.uErlaubteanwesenheitszeit;
	}

	public void setUErlaubteanwesenheitszeit(Time uErlaubteanwesenheitszeit) {
		this.uErlaubteanwesenheitszeit = uErlaubteanwesenheitszeit;
	}

	public Time getUAutopauseab() {
		return this.uAutopauseab;
	}

	public void setUAutopauseab(Time uAutopauseab) {
		this.uAutopauseab = uAutopauseab;
	}

	public Integer getIRundungbeginn() {
		return this.iRundungbeginn;
	}

	public void setIRundungbeginn(Integer iRundungbeginn) {
		this.iRundungbeginn = iRundungbeginn;
	}

	public Integer getIRundungende() {
		return this.iRundungende;
	}

	public void setIRundungende(Integer iRundungende) {
		this.iRundungende = iRundungende;
	}

	public Short getBRundesondertaetigkeiten() {
		return this.bRundesondertaetigkeiten;
	}

	public void setBRundesondertaetigkeiten(Short bRundesondertaetigkeiten) {
		this.bRundesondertaetigkeiten = bRundesondertaetigkeiten;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getTagesartIId() {
		return this.tagesartIId;
	}

	public void setTagesartIId(Integer tagesartIId) {
		this.tagesartIId = tagesartIId;
	}

	public Integer getZeitmodellIId() {
		return this.zeitmodellIId;
	}

	public void setZeitmodellIId(Integer zeitmodellIId) {
		this.zeitmodellIId = zeitmodellIId;
	}

}

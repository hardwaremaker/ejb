
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
package com.lp.server.personal.ejb;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lp.server.system.service.ITablenames;

@Entity
@Table(name = ITablenames.PERS_NACHRICHTEN)
public class Nachrichten implements Serializable {
	
	
	
	
	
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_ERLEDIGT")
	private Timestamp tErledigt;

	@Column(name = "PERSONAL_I_ID_ABSENDER")
	private Integer personalIIdAbsender;
	
	@Column(name = "PERSONAL_I_ID_ERLEDIGT")
	private Integer personalIIdErledigt;

	@Column(name = "NACHRICHTENART_I_ID")
	private Integer nachrichtenartIId;
	
	@Column(name = "C_BETREFF")
	private String cBetreff;

	@Column(name = "X_TEXT")
	private String xText;

	@Column(name = "C_BELEGARTNR")
	private String cBelegartnr;

	@Column(name = "BELEG_I_ID")
	private Integer belegIId;
	
	@Column(name = "BELEGPOSITION_I_ID")
	private Integer belegpositionIId;
	

	private static final long serialVersionUID = 1L;

	public Nachrichten() {
		super();
	}

	public Nachrichten(Integer id,Integer nachrichtenartIId, Timestamp tAnlegen, Integer personalIIdAbsender) {
		setIId(id);
		setNachrichtenartIId(nachrichtenartIId);
		setTAnlegen(tAnlegen);
		setPersonalIIdAbsender(personalIIdAbsender);
		
	}

	

	public Timestamp gettAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Timestamp getTErledigt() {
		return tErledigt;
	}

	public void setTErledigt(Timestamp tErledigt) {
		this.tErledigt = tErledigt;
	}

	public Integer getPersonalIIdAbsender() {
		return personalIIdAbsender;
	}

	public void setPersonalIIdAbsender(Integer personalIIdAbsender) {
		this.personalIIdAbsender = personalIIdAbsender;
	}

	public Integer getPersonalIIdErledigt() {
		return personalIIdErledigt;
	}

	public void setPersonalIIdErledigt(Integer personalIIdErledigt) {
		this.personalIIdErledigt = personalIIdErledigt;
	}

	public Integer getNachrichtenartIId() {
		return nachrichtenartIId;
	}

	public void setNachrichtenartIId(Integer nachrichtenartIId) {
		this.nachrichtenartIId = nachrichtenartIId;
	}

	public String getCBetreff() {
		return cBetreff;
	}

	public void setCBetreff(String cBetreff) {
		this.cBetreff = cBetreff;
	}

	public String getXText() {
		return xText;
	}

	public void setXText(String xText) {
		this.xText = xText;
	}

	public String getCBelegartnr() {
		return cBelegartnr;
	}

	public void setCBelegartnr(String cBelegartnr) {
		this.cBelegartnr = cBelegartnr;
	}

	public Integer getBelegIId() {
		return belegIId;
	}

	public void setBelegIId(Integer belegIId) {
		this.belegIId = belegIId;
	}

	public Integer getBelegpositionIId() {
		return belegpositionIId;
	}

	public void setBelegpositionIId(Integer belegpositionIId) {
		this.belegpositionIId = belegpositionIId;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	

}

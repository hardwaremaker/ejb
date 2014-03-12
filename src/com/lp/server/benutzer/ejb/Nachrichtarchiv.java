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
package com.lp.server.benutzer.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import javax.persistence.Table;

@Entity
@Table(name = "PERS_NACHRICHTARCHIV")
public class Nachrichtarchiv implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "NACHRICHTART_I_ID")
	private Integer nachrichtartIId;
	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;
	@Column(name = "PERSONAL_I_ID_BEARBEITER")
	private Integer personalIIdBearbeiter;
	@Column(name = "PERSONAL_I_ID_ERLEDIGT")
	private Integer personalIIdErledigt;
	
	public Integer getPersonalIIdErledigt() {
		return personalIIdErledigt;
	}

	public void setPersonalIIdErledigt(Integer personalIIdErledigt) {
		this.personalIIdErledigt = personalIIdErledigt;
	}

	@Column(name = "C_NACHRICHT")
	private String cNachricht;

	@Column(name = "T_ZEIT")
	private Timestamp tZeit;
	@Column(name = "T_ERLEDIGT")
	private Timestamp tEledigt;
	@Column(name = "T_BEARBEITUNG")
	private Timestamp tBearbeitung;
	@Column(name = "C_ERLEDIGUNGSGRUND")
	private String cErledigungsgrund;

	
	
	public String getCErledigungsgrund() {
		return cErledigungsgrund;
	}

	public void setCErledigungsgrund(String erledigungsgrund) {
		cErledigungsgrund = erledigungsgrund;
	}

	private static final long serialVersionUID = 1L;

	public Nachrichtarchiv() {
		super();
	}

	public Nachrichtarchiv(Integer IId, Integer nachrichtartIId,
			Integer personalIIdAnlegen, String cNachricht, Timestamp tZeit) {
		setIId(IId);
		setNachrichtartIId(nachrichtartIId);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setCNachricht(cNachricht);
		setTZeit(tZeit);

	}

	public Integer getNachrichtartIId() {
		return nachrichtartIId;
	}

	public void setNachrichtartIId(Integer nachrichtartIId) {
		this.nachrichtartIId = nachrichtartIId;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Integer getPersonalIIdBearbeiter() {
		return personalIIdBearbeiter;
	}

	public void setPersonalIIdBearbeiter(Integer personalIIdBearbeiter) {
		this.personalIIdBearbeiter = personalIIdBearbeiter;
	}

	public String getCNachricht() {
		return cNachricht;
	}

	public void setCNachricht(String nachricht) {
		cNachricht = nachricht;
	}

	public Timestamp getTZeit() {
		return tZeit;
	}

	public void setTZeit(Timestamp zeit) {
		tZeit = zeit;
	}

	public Timestamp getTEledigt() {
		return tEledigt;
	}

	public void setTEledigt(Timestamp eledigt) {
		tEledigt = eledigt;
	}

	public Timestamp getTBearbeitung() {
		return tBearbeitung;
	}

	public void setTBearbeitung(Timestamp bearbeitung) {
		tBearbeitung = bearbeitung;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer id) {
		iId = id;
	}

}

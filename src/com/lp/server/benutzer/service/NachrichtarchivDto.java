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
package com.lp.server.benutzer.service;

import java.io.Serializable;
import java.sql.Timestamp;

public class NachrichtarchivDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer nachrichtartIId;
	private Integer personalIIdAnlegen;
	private Integer personalIIdBearbeiter;
	private String cNachricht;
	private Timestamp tZeit;
	private Timestamp tEledigt;
	private Integer personalIIdErledigt;
	private String cErledigungsgrund;
	
	public String getCErledigungsgrund() {
		return cErledigungsgrund;
	}

	public void setCErledigungsgrund(String erledigungsgrund) {
		cErledigungsgrund = erledigungsgrund;
	}

	public Integer getPersonalIIdErledigt() {
		return personalIIdErledigt;
	}

	public void setPersonalIIdErledigt(Integer personalIIdErledigt) {
		this.personalIIdErledigt = personalIIdErledigt;
	}

	public NachrichtarchivDto() {
	}
	
	public NachrichtarchivDto(Integer nachrichtartIIdI, Integer personalIIdAnlegenI, 
			String cNachrichtI) {
		this.setNachrichtartIId(nachrichtartIIdI);
		this.setPersonalIIdAnlegen(personalIIdAnlegenI);
		this.setCNachricht(cNachrichtI);
		this.setTZeit(new Timestamp(System.currentTimeMillis()));
	}
	
	public Integer getIId() {
		return iId;
	}
	public void setIId(Integer id) {
		iId = id;
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
	private Timestamp tBearbeitung;
}

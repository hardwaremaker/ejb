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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "BereitschafttagfindByBereitschaftartIId", query = "SELECT OBJECT(o) FROM Bereitschafttag o WHERE o.bereitschaftartIId = ?1"),
		@NamedQuery(name = "BereitschafttagfindByBereitschaftartIIdTagesartIId", query = "SELECT OBJECT(C) FROM Bereitschafttag c WHERE c.bereitschaftartIId = ?1 AND  c.tagesartIId = ?2"),
		@NamedQuery(name = "BereitschafttagfindByBereitschaftartIIdTagesartIIdUBeginn", query = "SELECT OBJECT(C) FROM Bereitschafttag c WHERE c.bereitschaftartIId = ?1 AND  c.tagesartIId = ?2 AND c.uBeginn = ?3") })
@Entity
@Table(name = "PERS_BEREITSCHAFTTAG")
public class Bereitschafttag implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "U_BEGINN")
	private Time uBeginn;

	@Column(name = "U_ENDE")
	private Time uEnde;

	@Column(name = "TAGESART_I_ID")
	private Integer tagesartIId;

	@Column(name = "BEREITSCHAFTART_I_ID")
	private Integer bereitschaftartIId;
	
	@Column(name = "B_ENDEDESTAGES")
	private Short bEndedestages;

	public Short getBEndedestages() {
		return bEndedestages;
	}

	public void setBEndedestages(Short bEndedestages) {
		this.bEndedestages = bEndedestages;
	}

	private static final long serialVersionUID = 1L;

	public Bereitschafttag() {
		super();
	}

	public Bereitschafttag(Integer id, Integer bereitschaftartIId,
			Integer tagesartIId, Time tBeginn, Short bEndedestages) {
		setIId(id);
		setBereitschaftartIId(bereitschaftartIId);
		setTagesartIId(tagesartIId);
		setUBeginn(tBeginn);
		setBEndedestages(bEndedestages);
	}

	public Integer getBereitschaftartIId() {
		return bereitschaftartIId;
	}

	public void setBereitschaftartIId(Integer bereitschaftartIId) {
		this.bereitschaftartIId = bereitschaftartIId;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
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

	public Integer getTagesartIId() {
		return this.tagesartIId;
	}

	public void setTagesartIId(Integer tagesartIId) {
		this.tagesartIId = tagesartIId;
	}

}

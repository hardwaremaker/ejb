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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@NamedQueries({
	@NamedQuery(name = "SchichtzeitfindBySchichtIId", query = "SELECT OBJECT(o) FROM Schichtzeit o WHERE o.schichtIId = ?1"),
	
	@NamedQuery(name = "SchichtzeitfindBySchichtIIdUBeginn", query = "SELECT OBJECT(C) FROM Schichtzeit c WHERE c.schichtIId = ?1 AND c.uBeginn = ?2") })

@Entity
@Table(name = "PERS_SCHICHTZEIT")
public class Schichtzeit implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "U_BEGINN")
	private Time uBeginn;

	public Time getuBeginn() {
		return uBeginn;
	}

	public void setuBeginn(Time uBeginn) {
		this.uBeginn = uBeginn;
	}

	public Time getuEnde() {
		return uEnde;
	}

	public void setuEnde(Time uEnde) {
		this.uEnde = uEnde;
	}

	@Column(name = "U_ENDE")
	private Time uEnde;

	@Column(name = "B_ENDEDESTAGES")
	private Short bEndedestages;

	public Short getBEndedestages() {
		return bEndedestages;
	}

	public void setBEndedestages(Short bEndedestages) {
		this.bEndedestages = bEndedestages;
	}

	@Column(name = "SCHICHT_I_ID")
	private Integer schichtIId;

	public Integer getSchichtIId() {
		return schichtIId;
	}

	public void setSchichtIId(Integer schichtIId) {
		this.schichtIId = schichtIId;
	}

	public Integer getSchichtzuschlagIId() {
		return schichtzuschlagIId;
	}

	public void setSchichtzuschlagIId(Integer schichtzuschlagIId) {
		this.schichtzuschlagIId = schichtzuschlagIId;
	}

	@Column(name = "SCHICHTZUSCHLAG_I_ID")
	private Integer schichtzuschlagIId;

	private static final long serialVersionUID = 1L;

	public Schichtzeit() {
		super();
	}

	public Schichtzeit(Integer id, Integer schichtIId,
			Integer schichtzuschlagIId, Time uBeginn, Short bEndedestages) {

		setIId(id);

		setSchichtIId(schichtIId);
		setSchichtzuschlagIId(schichtzuschlagIId);
		setuBeginn(uBeginn);
		setBEndedestages(bEndedestages);

	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

}

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

@NamedQueries( {
		@NamedQuery(name = "KollektivuestdfindByKollektivIIdTagesartIId", query = "SELECT OBJECT(o) FROM Kollektivuestd o WHERE o.kollektivIId = ?1 AND o.tagesartIId = ?2"),
		@NamedQuery(name = "KollektivuestdfindByKollektivIId", query = "SELECT OBJECT(o) FROM Kollektivuestd o WHERE o.kollektivIId = ?1") })
@Entity
@Table(name = "PERS_KOLLEKTIVUESTD")
public class Kollektivuestd implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "B_RESTDESTAGES")
	private Short bRestdestages;

	@Column(name = "B_UNTERIGNORIEREN")
	private Short bUnterignorieren;

	public Short getBUnterignorieren() {
		return bUnterignorieren;
	}

	public void setBUnterignorieren(Short bUnterignorieren) {
		this.bUnterignorieren = bUnterignorieren;
	}

	@Column(name = "U_AB")
	private Time uAb;

	@Column(name = "U_BIS")
	private Time uBis;

	@Column(name = "KOLLEKTIV_I_ID")
	private Integer kollektivIId;

	@Column(name = "TAGESART_I_ID")
	private Integer tagesartIId;

	private static final long serialVersionUID = 1L;

	public Kollektivuestd() {
		super();
	}

	public Kollektivuestd(Integer id,
			Integer kollektivIId,
			Short restdestages,
			Integer tagesartIId,
			Time ab, Short bUnterignorieren) {
		setIId(id);
		setKollektivIId(kollektivIId);
		setUAb(ab);
		setTagesartIId(tagesartIId);
		setBRestdestages(restdestages);
		setBUnterignorieren(bUnterignorieren);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Short getBRestdestages() {
		return this.bRestdestages;
	}

	public void setBRestdestages(Short bRestdestages) {
		this.bRestdestages = bRestdestages;
	}

	public Time getUAb() {
		return this.uAb;
	}

	public void setUAb(Time uAb) {
		this.uAb = uAb;
	}

	public Time getUBis() {
		return this.uBis;
	}

	public void setUBis(Time uBis) {
		this.uBis = uBis;
	}

	public Integer getKollektivIId() {
		return this.kollektivIId;
	}

	public void setKollektivIId(Integer kollektivIId) {
		this.kollektivIId = kollektivIId;
	}

	public Integer getTagesartIId() {
		return this.tagesartIId;
	}

	public void setTagesartIId(Integer tagesartIId) {
		this.tagesartIId = tagesartIId;
	}

}

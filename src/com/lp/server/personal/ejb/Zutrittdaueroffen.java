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

@NamedQueries( { @NamedQuery(name = "ZutrittdaueroffenfindByZutrittsobjektIIdTagesartIId", query = "SELECT OBJECT(C) FROM Zutrittdaueroffen c WHERE c.zutrittsobjektIId = ?1 AND c.tagesartIId = ?2") })
@Entity
@Table(name = "PERS_ZUTRITTDAUEROFFEN")
public class Zutrittdaueroffen implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "U_OFFENVON")
	private Time uOffenvon;

	@Column(name = "U_OFFENBIS")
	private Time uOffenbis;

	@Column(name = "TAGESART_I_ID")
	private Integer tagesartIId;

	@Column(name = "ZUTRITTSOBJEKT_I_ID")
	private Integer zutrittsobjektIId;

	private static final long serialVersionUID = 1L;

	public Zutrittdaueroffen() {
		super();
	}

	public Zutrittdaueroffen(Integer id, Integer tagesartIId,
			Integer zutrittsobjektIId, Time offenvon, Time offenbis) {
		setIId(id);
		setTagesartIId(tagesartIId);
		setZutrittsobjektIId(zutrittsobjektIId);
		setUOffenvon(offenvon);
		setUOffenbis(offenbis);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Time getUOffenvon() {
		return this.uOffenvon;
	}

	public void setUOffenvon(Time uOffenvon) {
		this.uOffenvon = uOffenvon;
	}

	public Time getUOffenbis() {
		return this.uOffenbis;
	}

	public void setUOffenbis(Time uOffenbis) {
		this.uOffenbis = uOffenbis;
	}

	public Integer getTagesartIId() {
		return this.tagesartIId;
	}

	public void setTagesartIId(Integer tagesartIId) {
		this.tagesartIId = tagesartIId;
	}

	public Integer getZutrittsobjektIId() {
		return this.zutrittsobjektIId;
	}

	public void setZutrittsobjektIId(Integer zutrittsobjektIId) {
		this.zutrittsobjektIId = zutrittsobjektIId;
	}

}

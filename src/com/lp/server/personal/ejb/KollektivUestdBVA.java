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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "KollektivuestdBVAfindByKollektivIIdTagesartIId", query = "SELECT OBJECT(o) FROM KollektivUestdBVA o WHERE o.kollektivIId = ?1 AND o.tagesartIId = ?2"),
		@NamedQuery(name = "KollektivuestdBVAfindByKollektivIId", query = "SELECT OBJECT(o) FROM KollektivUestdBVA o WHERE o.kollektivIId = ?1") })
@Entity
@Table(name = "PERS_KOLLEKTIVUESTD_BVA")
public class KollektivUestdBVA implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "U_100_ENDE")
	private Time u100Ende;

	@Column(name = "U_50_BEGINN")
	private Time u50Beginn;

	@Column(name = "U_50_ENDE")
	private Time u50Ende;

	@Column(name = "U_100_BEGINN")
	private Time u100Beginn;

	@Column(name = "U_GLEITZEIT_BIS")
	private Time uGleitzeitBis;

	@Column(name = "KOLLEKTIV_I_ID")
	private Integer kollektivIId;

	@Column(name = "TAGESART_I_ID")
	private Integer tagesartIId;

	private static final long serialVersionUID = 1L;

	public KollektivUestdBVA() {
		super();
	}

	public KollektivUestdBVA(Integer id, Integer kollektivIId2,
			Integer tagesartIId2, Time u100Ende, Time u50Beginn, Time u50Ende,
			Time u100Beginn, Time uGleitzeitBis) {
		setIId(id);
		setKollektivIId(kollektivIId2);
		setU100Ende(u100Ende);
		setU50Beginn(u50Beginn);
		setU50Ende(u50Ende);
		setU100Beginn(u100Beginn);
		setUGleitzeitBis(uGleitzeitBis);
		setTagesartIId(tagesartIId2);

	}

	public Time getU100Ende() {
		return u100Ende;
	}

	public void setU100Ende(Time u100Ende) {
		this.u100Ende = u100Ende;
	}

	public Time getU50Beginn() {
		return u50Beginn;
	}

	public void setU50Beginn(Time u50Beginn) {
		this.u50Beginn = u50Beginn;
	}

	public Time getU50Ende() {
		return u50Ende;
	}

	public void setU50Ende(Time u50Ende) {
		this.u50Ende = u50Ende;
	}

	public Time getU100Beginn() {
		return u100Beginn;
	}

	public void setU100Beginn(Time u100Beginn) {
		this.u100Beginn = u100Beginn;
	}

	public Time getUGleitzeitBis() {
		return uGleitzeitBis;
	}

	public void setUGleitzeitBis(Time uGleitzeitBis) {
		this.uGleitzeitBis = uGleitzeitBis;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
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

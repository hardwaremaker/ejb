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
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "FeiertagfindByMandantCNr", query = "SELECT OBJECT(C) FROM Feiertag c WHERE c.mandantCNr = ?1")
 })
@Entity
@Table(name = "PERS_FEIERTAG")
public class Feiertag implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_BEZ")
	private String cBez;


	@Column(name = "I_TAG")
	private Integer iTag;

	@Column(name = "I_MONAT")
	private Integer iMonat;


	public Integer getiTag() {
		return iTag;
	}

	public void setiTag(Integer iTag) {
		this.iTag = iTag;
	}

	public Integer getiMonat() {
		return iMonat;
	}

	public void setiMonat(Integer iMonat) {
		this.iMonat = iMonat;
	}

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "RELIGION_I_ID")
	private Integer religionIId;

	@Column(name = "TAGESART_I_ID")
	private Integer tagesartIId;
	
	@Column(name = "I_OFFSET_OSTERSONNTAG")
	private Integer iOffsetOstersonntag;

	private static final long serialVersionUID = 1L;

	public Feiertag() {
		super();
	}

	public Feiertag(Integer id,
			String mandantCNr,String cBez,
			Integer tagesartIId) {
		setIId(id);
		setMandantCNr(mandantCNr);
		setCBez(cBez);
		setTagesartIId(tagesartIId);
	}

	public Integer getIOffsetOstersonntag() {
		return iOffsetOstersonntag;
	}

	public void setIOffsetOstersonntag(Integer iOffsetOstersonntag) {
		this.iOffsetOstersonntag = iOffsetOstersonntag;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}


	public Integer getReligionIId() {
		return this.religionIId;
	}

	public void setReligionIId(Integer religionIId) {
		this.religionIId = religionIId;
	}

	public Integer getTagesartIId() {
		return this.tagesartIId;
	}

	public void setTagesartIId(Integer tagesartIId) {
		this.tagesartIId = tagesartIId;
	}

}

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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "ZeitstiftfindByMandantCNr", query = "SELECT OBJECT(C) FROM Zeitstift c WHERE  c.mandantCNr = ?1 ORDER BY c.cNr ASC"),
		@NamedQuery(name = "ZeitstiftfindByCNr", query = "SELECT OBJECT(C) FROM Zeitstift c WHERE  c.cNr = ?1"),
		@NamedQuery(name = "ZeitstiftfindByPersonalIId", query = "SELECT OBJECT(C) FROM Zeitstift c WHERE  c.personalIId = ?1"),
		@NamedQuery(name = "ZeitstiftfindByPersonalIIdCTyp", query = "SELECT OBJECT(C) FROM Zeitstift c WHERE  c.personalIId = ?1 AND c.cTyp = ?2")})
@Entity
@Table(name = "PERS_ZEITSTIFT")
public class Zeitstift implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "B_MEHRFACHSTIFT")
	private Short bMehrfachstift;

	@Column(name = "B_PERSONENZUORDNUNG")
	private Short bPersonenzuordnung;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "PERSONAL_I_ID")
	private Integer personalIId;

	@Column(name = "C_TYP")
	private String cTyp;

	public String getCTyp() {
		return cTyp;
	}

	public void setCTyp(String typ) {
		cTyp = typ;
	}

	private static final long serialVersionUID = 1L;

	public Zeitstift() {
		super();
	}

	public Zeitstift(Integer id, String nr, String mandantCNr2,
			Short mehrfachstift, Short personenzuordnung, String cTyp) {
		setIId(id);
		setCNr(nr);
		setBMehrfachstift(mehrfachstift);
		setMandantCNr(mandantCNr2);
		setBPersonenzuordnung(personenzuordnung);
		setCTyp(cTyp);

	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCNr() {
		return this.cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public Short getBMehrfachstift() {
		return this.bMehrfachstift;
	}

	public void setBMehrfachstift(Short bMehrfachstift) {
		this.bMehrfachstift = bMehrfachstift;
	}

	public Short getBPersonenzuordnung() {
		return this.bPersonenzuordnung;
	}

	public void setBPersonenzuordnung(Short bPersonenzuordnung) {
		this.bPersonenzuordnung = bPersonenzuordnung;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Integer getPersonalIId() {
		return this.personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

}

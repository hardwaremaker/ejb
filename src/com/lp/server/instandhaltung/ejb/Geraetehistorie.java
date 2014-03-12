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
package com.lp.server.instandhaltung.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({ @NamedQuery(name = "GeraetehistorieFindByGeraetIIdPersonalIIdTechnikerTWartung", query = "SELECT OBJECT(o) FROM Geraetehistorie o WHERE o.geraetIId = ?1 AND o.personalIIdTechniker = ?2 AND o.tWartung = ?3") })
@Entity
@Table(name = "IS_GERAETEHISTORIE")
public class Geraetehistorie implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "GERAET_I_ID")
	private Integer geraetIId;

	@Column(name = "PERSONAL_I_ID_TECHNIKER")
	private Integer personalIIdTechniker;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "T_WARTUNG")
	private Timestamp tWartung;

	@Column(name = "B_NICHTMOEGLICH")
	private Short bNichtmoeglich;

	private static final long serialVersionUID = 1L;

	public Geraetehistorie() {
		super();
	}

	public Geraetehistorie(Integer id, Integer geraetIId,
			Integer personalIIdTechniker, Integer personalIIdAendern,
			Timestamp tAendern, Timestamp tWartung, Short bNichtmoeglich) {
		setIId(id);
		setGeraetIId(geraetIId);
		setPersonalIIdTechniker(personalIIdTechniker);
		setPersonalIIdAendern(personalIIdAendern);
		setTAendern(tAendern);
		setTWartung(tWartung);
		setBNichtmoeglich(bNichtmoeglich);

	}

	public Integer getGeraetIId() {
		return geraetIId;
	}

	public void setGeraetIId(Integer geraetIId) {
		this.geraetIId = geraetIId;
	}

	public Integer getPersonalIIdTechniker() {
		return personalIIdTechniker;
	}

	public void setPersonalIIdTechniker(Integer personalIIdTechniker) {
		this.personalIIdTechniker = personalIIdTechniker;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Timestamp getTWartung() {
		return tWartung;
	}

	public void setTWartung(Timestamp tWartung) {
		this.tWartung = tWartung;
	}

	public Short getBNichtmoeglich() {
		return bNichtmoeglich;
	}

	public void setBNichtmoeglich(Short bNichtmoeglich) {
		this.bNichtmoeglich = bNichtmoeglich;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

}

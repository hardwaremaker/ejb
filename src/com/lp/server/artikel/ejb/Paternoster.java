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
package com.lp.server.artikel.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
	@NamedQuery(name = "PaternosterAll", query = "SELECT OBJECT (o) FROM Paternoster o ORDER BY o.cNr"),
	@NamedQuery(name = "PaternosterfindByCPaternostertyp", query = "SELECT OBJECT (o) FROM Paternoster o WHERE o.cPaternostertyp=?1"),
	@NamedQuery(name = PaternosterQuery.ByLagerIIds, query = "SELECT OBJECT(o) FROM Paternoster o WHERE o.lagerIId IN (?1)")})

@Entity
@Table(name = "WW_PATERNOSTER")
public class Paternoster implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6548084425865803025L;

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "C_PATERNOSTERTYP")
	private String cPaternostertyp;

	@Column(name = "LAGER_I_ID")
	private Integer lagerIId;
	
	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;
	
	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getIId() {
		return iId;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public String getCNr() {
		return cNr;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCPaternostertyp(String cPaternostertyp) {
		this.cPaternostertyp = cPaternostertyp;
	}

	public String getCPaternostertyp() {
		return cPaternostertyp;
	}

	public void setLagerIId(Integer lagerIId) {
		this.lagerIId = lagerIId;
	}

	public Integer getLagerIId() {
		return lagerIId;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}
	
}

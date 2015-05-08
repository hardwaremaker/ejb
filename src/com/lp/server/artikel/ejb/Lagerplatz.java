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
	@NamedQuery(name = "LagerplatzfindAll", query = "SELECT OBJECT (o) FROM Lagerplatz o ORDER BY o.cLagerplatz"),
	@NamedQuery(name = "LagerplatzfindByLagerIIdCLagerplatz", query = "SELECT OBJECT (o) FROM Lagerplatz o WHERE o.lagerIId=?1 AND o.cLagerplatz=?2"),
	@NamedQuery(name = "LagerplatzfindAllByPaternosterIId", query = "SELECT OBJECT (o) FROM Lagerplatz o WHERE o.paternosterIId=?1" )})

@Entity
@Table(name = "WW_LAGERPLATZ")
public class Lagerplatz implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_LAGERPLATZ")
	private String cLagerplatz;

	@Column(name = "I_MAXMENGE")
	private Integer iMaxmenge;

	@Column(name = "LAGER_I_ID")
	private Integer lagerIId;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;
	
	@Column(name = "PATERNOSTER_I_ID")
	private Integer paternosterIId;
	
	private static final long serialVersionUID = 1L;

	public Integer getPaternosterIId() {
		return paternosterIId;
	}

	public void setPaternosterIId(Integer paternosterIId) {
		this.paternosterIId = paternosterIId;
	}

	public Lagerplatz() {
		super();
	}

	public Lagerplatz(Integer id, String lagerplatz,Integer maxmenge, Integer lagerIId2,Integer personalIIdAendern, Timestamp tAendern) {
		setIId(id);
		setCLagerplatz(lagerplatz);
		setLagerIId(lagerIId2);
		setIMaxmenge(maxmenge);
		setPersonalIIdAendern(personalIIdAendern);
		setTAendern(tAendern);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCLagerplatz() {
		return this.cLagerplatz;
	}
	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp aendern) {
		tAendern = aendern;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public void setCLagerplatz(String cLagerplatz) {
		this.cLagerplatz = cLagerplatz;
	}

	public Integer getIMaxmenge() {
		return this.iMaxmenge;
	}

	public void setIMaxmenge(Integer iMaxmenge) {
		this.iMaxmenge = iMaxmenge;
	}

	public Integer getLagerIId() {
		return this.lagerIId;
	}

	public void setLagerIId(Integer lagerIId) {
		this.lagerIId = lagerIId;
	}

}

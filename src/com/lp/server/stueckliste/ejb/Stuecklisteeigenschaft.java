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
package com.lp.server.stueckliste.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "StuecklisteeigenschaftfindByStuecklisteIIdStuecklisteeigenschaftartIId", query = "SELECT OBJECT(o) FROM Stuecklisteeigenschaft o WHERE o.stuecklisteIId=?1 AND o.stuecklisteeigenschaftartIId =?2"),
		@NamedQuery(name = "StuecklisteeigenschaftfindByStuecklisteIId", query = "SELECT OBJECT(o) FROM Stuecklisteeigenschaft o WHERE o.stuecklisteIId=?1") })
@Entity
@Table(name = "STK_STUECKLISTEEIGENSCHAFT")
public class Stuecklisteeigenschaft implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "STUECKLISTE_I_ID")
	private Integer stuecklisteIId;

	@Column(name = "STUECKLISTEEIGENSCHAFTART_I_ID")
	private Integer stuecklisteeigenschaftartIId;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	private static final long serialVersionUID = 1L;

	public Stuecklisteeigenschaft() {
		super();
	}

	public Stuecklisteeigenschaft(Integer id, Integer stuecklisteIId,
			Integer stuecklisteeigenschaftartIId, String bez,
			Integer personalIIdAendern, Timestamp tAendern) {
		setIId(id);
		setStuecklisteIId(stuecklisteIId);
		setStuecklisteeigenschaftartIId(stuecklisteeigenschaftartIId);
		setCBez(bez);
		setTAendern(tAendern);
		setPersonalIIdAendern(personalIIdAendern);

	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
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

	public Integer getStuecklisteIId() {
		return this.stuecklisteIId;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public void setStuecklisteIId(Integer stuecklisteIId) {
		this.stuecklisteIId = stuecklisteIId;
	}

	public Integer getStuecklisteeigenschaftartIId() {
		return this.stuecklisteeigenschaftartIId;
	}

	public void setStuecklisteeigenschaftartIId(
			Integer stuecklisteeigenschaftartIId) {
		this.stuecklisteeigenschaftartIId = stuecklisteeigenschaftartIId;
	}

}

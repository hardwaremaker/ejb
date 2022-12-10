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

@NamedQueries({
		@NamedQuery(name = Fasession.FindByPersonalIId, query = "SELECT OBJECT(C) FROM Fasession c WHERE c.personalIId = ?1"),
		@NamedQuery(name = Fasession.FindOffeneSession, query = "SELECT OBJECT(C) FROM Fasession c WHERE c.personalIId = ?1 AND c.tGedruckt IS NULL") })
@Entity
@Table(name = "WW_FASESSION")
public class Fasession implements Serializable {
	public static final String FindByPersonalIId = "FasessionFindByPersonalIId";
	public static final String FindOffeneSession = "FasessionFindOffeneSession";
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "T_BEGINN")
	private Timestamp tBeginn;
	@Column(name = "T_GEDRUCKT")
	private Timestamp tGedruckt;
	@Column(name = "PERSONAL_I_ID")
	private Integer personalIId;

	public Integer getPersonalIId() {
		return personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

	private static final long serialVersionUID = 1L;

	public Fasession() {
		super();
	}

	public Fasession(Integer id, Integer personalIId, Timestamp tBeginn) {
		setIId(id);
		setPersonalIId(personalIId);
		setTBeginn(tBeginn);

	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Timestamp getTBeginn() {
		return tBeginn;
	}

	public void setTBeginn(Timestamp tBeginn) {
		this.tBeginn = tBeginn;
	}

	public Timestamp getTGedruckt() {
		return tGedruckt;
	}

	public void setTGedruckt(Timestamp tGedruckt) {
		this.tGedruckt = tGedruckt;
	}

}

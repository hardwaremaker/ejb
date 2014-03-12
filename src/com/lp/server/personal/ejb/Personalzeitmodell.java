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

@NamedQueries({
		@NamedQuery(name = "PersonalzeitmodellfindZeitmodellZuDatum", query = "SELECT OBJECT(o) FROM Personalzeitmodell o WHERE o.personalIId = ?1 AND o.tGueltigab <= ?2 ORDER BY o.tGueltigab DESC"),
		@NamedQuery(name = "PersonalzeitmodellfindByPersonalIIdTDatum", query = "SELECT OBJECT(o) FROM Personalzeitmodell o WHERE o.personalIId = ?1 AND o.tGueltigab = ?2"),
		@NamedQuery(name = "PersonalzeitmodellfindZeitmodellImZeitraum", query = "SELECT OBJECT(o) FROM Personalzeitmodell o WHERE o.personalIId = ?1 AND o.tGueltigab >= ?2 AND o.tGueltigab <= ?3 ORDER BY o.tGueltigab ASC"),
		@NamedQuery(name = "PersonalzeitmodellAnzahlfindByPersonalIId", query = "SELECT COUNT (o) FROM Personalzeitmodell o WHERE o.personalIId = ?1") })
@Entity
@Table(name = "PERS_PERSONALZEITMODELL")
public class Personalzeitmodell implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "T_GUELTIGAB")
	private Timestamp tGueltigab;

	@Column(name = "PERSONAL_I_ID")
	private Integer personalIId;

	@Column(name = "ZEITMODELL_I_ID")
	private Integer zeitmodellIId;

	private static final long serialVersionUID = 1L;

	public Personalzeitmodell() {
		super();
	}

	public Personalzeitmodell(Integer id, Integer personalIId2,
			Integer zeitmodellIId2, Timestamp gueltigab) {
		setIId(id);
		setPersonalIId(personalIId2);
		setZeitmodellIId(zeitmodellIId2);
		setTGueltigab(gueltigab);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Timestamp getTGueltigab() {
		return this.tGueltigab;
	}

	public void setTGueltigab(Timestamp tGueltigab) {
		this.tGueltigab = tGueltigab;
	}

	public Integer getPersonalIId() {
		return this.personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

	public Integer getZeitmodellIId() {
		return this.zeitmodellIId;
	}

	public void setZeitmodellIId(Integer zeitmodellIId) {
		this.zeitmodellIId = zeitmodellIId;
	}

}

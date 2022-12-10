package com.lp.server.personal.ejb;

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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({ @NamedQuery(name = "NachrichtengruppeteilnehmerfindByNachrichtengruppeIIdPersonalIId", query = "SELECT OBJECT(o) FROM Nachrichtengruppeteilnehmer o WHERE o.nachrichtengruppeIId=?1 AND o.personalIId=?2"),
	 @NamedQuery(name = "NachrichtengruppeteilnehmerfindByNachrichtengruppeIId", query = "SELECT OBJECT(o) FROM Nachrichtengruppeteilnehmer o WHERE o.nachrichtengruppeIId=?1")})
@Entity
@Table(name = "PERS_NACHRICHTENGRUPPETEILNEHMER")
public class Nachrichtengruppeteilnehmer implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "NACHRICHTENGRUPPE_I_ID")
	private Integer nachrichtengruppeIId;

	@Column(name = "PERSONAL_I_ID")
	private Integer personalIId;

	private static final long serialVersionUID = 1L;

	public Nachrichtengruppeteilnehmer() {
		super();
	}

	public Nachrichtengruppeteilnehmer(Integer id,
			Integer nachrichtengruppeIId, Integer personalIId) {
		setIId(id);
		setPersonalIId(personalIId);
		setNachrichtengruppeIId(nachrichtengruppeIId);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getNachrichtengruppeIId() {
		return nachrichtengruppeIId;
	}

	public void setNachrichtengruppeIId(Integer nachrichtengruppeIId) {
		this.nachrichtengruppeIId = nachrichtengruppeIId;
	}

	public Integer getPersonalIId() {
		return personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

}

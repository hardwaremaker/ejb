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

@NamedQueries( { @NamedQuery(name = "NachrichtartenfindByCNr", query = "SELECT OBJECT (o) FROM Nachrichtenart o WHERE o.cNr=?1") })
@Entity
@Table(name = "PERS_NACHRICHTENART")
public class Nachrichtenart implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "PERSONAL_I_ID_ESKALATION")
	private Integer personalIIdEskalation;

	@Column(name = "B_MUSS_ERLEDIGT_WERDEN")
	private Short bMussErledigtWerden;

	@Column(name = "U_ESKALATION")
	private Time uEskalation;
	
	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer id) {
		iId = id;
	}

	

	private static final long serialVersionUID = 1L;

	public Nachrichtenart() {
		super();
	}

	public Nachrichtenart(Integer iId, String cNr,
			Short bMussErledigtWerden) {
		setIId(iId);
		setCNr(cNr);
		setBMussErledigtWerden(bMussErledigtWerden);
		
	}

	public Integer getPersonalIIdEskalation() {
		return personalIIdEskalation;
	}

	public void setPersonalIIdEskalation(Integer personalIIdEskalation) {
		this.personalIIdEskalation = personalIIdEskalation;
	}

	public Short getBMussErledigtWerden() {
		return bMussErledigtWerden;
	}

	public void setBMussErledigtWerden(Short bMussErledigtWerden) {
		this.bMussErledigtWerden = bMussErledigtWerden;
	}

	public Time getUEskalation() {
		return uEskalation;
	}

	public void setUEskalation(Time uEskalation) {
		this.uEskalation = uEskalation;
	}

	public String getCNr() {
		return this.cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}



}

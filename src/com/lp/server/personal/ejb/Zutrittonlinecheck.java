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
		@NamedQuery(name = "ZutrittonlinecheckfindByCPincodeTGueltigabTGueltigbis", query = "SELECT OBJECT(C) FROM Zutrittonlinecheck c WHERE  c.cPincode = ?1 AND c.tGueltigab >= ?2 AND c.tGueltigbis <= ?3"),
		@NamedQuery(name = "ZutrittonlinecheckfindByCAusweisTGueltigabTGueltigbis", query = "SELECT OBJECT(C) FROM Zutrittonlinecheck c WHERE  c.cAusweis = ?1 AND c.tGueltigab >= ?2 AND c.tGueltigbis <= ?3") })
@Entity
@Table(name = "PERS_ZUTRITTONLINECHECK")
public class Zutrittonlinecheck implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_PINCODE")
	private String cPincode;

	@Column(name = "C_AUSWEIS")
	private String cAusweis;

	@Column(name = "T_GUELTIGAB")
	private Timestamp tGueltigab;

	@Column(name = "T_GUELTIGBIS")
	private Timestamp tGueltigbis;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "ZUTRITTSKLASSE_I_ID")
	private Integer zutrittsklasseIId;

	private static final long serialVersionUID = 1L;

	public Zutrittonlinecheck() {
		super();
	}

	public Zutrittonlinecheck(Integer id,
			Integer zutrittsklasseIId,
			String mandantCNr,
			Timestamp gueltigab, 
			Timestamp gueltigbis) {
		setIId(id);
		setZutrittsklasseIId(zutrittsklasseIId);
		setTGueltigab(gueltigab);
		setTGueltigbis(gueltigbis);
		setMandantCNr(mandantCNr);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCPincode() {
		return this.cPincode;
	}

	public void setCPincode(String cPincode) {
		this.cPincode = cPincode;
	}

	public String getCAusweis() {
		return this.cAusweis;
	}

	public void setCAusweis(String cAusweis) {
		this.cAusweis = cAusweis;
	}

	public Timestamp getTGueltigab() {
		return this.tGueltigab;
	}

	public void setTGueltigab(Timestamp tGueltigab) {
		this.tGueltigab = tGueltigab;
	}

	public Timestamp getTGueltigbis() {
		return this.tGueltigbis;
	}

	public void setTGueltigbis(Timestamp tGueltigbis) {
		this.tGueltigbis = tGueltigbis;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Integer getZutrittsklasseIId() {
		return this.zutrittsklasseIId;
	}

	public void setZutrittsklasseIId(Integer zutrittsklasseIId) {
		this.zutrittsklasseIId = zutrittsklasseIId;
	}

}

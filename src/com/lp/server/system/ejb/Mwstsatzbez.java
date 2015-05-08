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
package com.lp.server.system.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "MwstsatzbezfindAllByMandant", query = "SELECT OBJECT (o) FROM Mwstsatzbez o WHERE o.mandantCNr=?1 AND o.bHandeingabe=null ORDER BY o.cBezeichnung"),
		@NamedQuery(name = "MwstsatzbezfindAllByMandantInklHandeingabe", query = "SELECT OBJECT (o) FROM Mwstsatzbez o WHERE o.mandantCNr=?1 ORDER BY o.cBezeichnung"),
		@NamedQuery(name = "MwstsatzbezfindByMandantCBezeichnung", query = "SELECT OBJECT (o) FROM Mwstsatzbez o WHERE o.mandantCNr=?1 AND o.cBezeichnung=?2"),
		@NamedQuery(name = "MwstsatzbezfindByMandantCDruckname", query = "SELECT OBJECT (o) FROM Mwstsatzbez o WHERE o.mandantCNr=?1 AND o.cDruckname=?2")})
@Entity
@Table(name = "LP_MWSTSATZBEZ")
public class Mwstsatzbez implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_BEZEICHNUNG")
	private String cBezeichnung;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name="B_HANDEINGABE")
	private Short bHandeingabe;
	
	@Column(name="C_DRUCKNAME")
	private String cDruckname;

	@Column(name = "FINANZAMT_I_ID")
	private Integer finanzamtIId;


	public void setFinanzamtIId(Integer finanzamtIId) {
		this.finanzamtIId = finanzamtIId;
	}

	public Integer getFinanzamtIId() {
		return finanzamtIId;
	}

	
	
	private static final long serialVersionUID = 1L;

	public Mwstsatzbez() {
		super();
	}

	public Mwstsatzbez(Integer id, String bezeichnung, String mandantCNr,Short bHandeingabe, String cDruckname) {
		setIId(id);
		setCBezeichnung(bezeichnung);
		setMandantCNr(mandantCNr);
		setBHandeingabe(bHandeingabe);
		setCDruckname(cDruckname);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCBezeichnung() {
		return this.cBezeichnung;
	}

	public void setCBezeichnung(String cBezeichnung) {
		this.cBezeichnung = cBezeichnung;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandant) {
		this.mandantCNr = mandant;
	}
	
	public Short getBHandeingabe() {
		return this.bHandeingabe;
	}

	public void setBHandeingabe(Short bHandeingabe) {
		this.bHandeingabe = bHandeingabe;
	}

	public String getCDruckname() {
		return cDruckname;
	}

	public void setCDruckname(String druckname) {
		cDruckname = druckname;
	}

}

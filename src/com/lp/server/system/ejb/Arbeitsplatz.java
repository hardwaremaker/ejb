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

@NamedQueries( { @NamedQuery(name = "ArbeitsplatzfindByCPcname", query = "SELECT OBJECT (o) FROM Arbeitsplatz o WHERE o.cPcname = ?1"),
	             @NamedQuery(name = "ArbeitsplatzFindByCTypCGeraetecode", query = "SELECT OBJECT(o) FROM Arbeitsplatz o WHERE o.cTyp = ?1 AND o.cGeraetecode = ?2")})
@Entity
@Table(name = "LP_ARBEITSPLATZ")
public class Arbeitsplatz implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_STANDORT")
	private String cStandort;

	@Column(name = "C_BEMERKUNG")
	private String cBemerkung;

	@Column(name = "C_PCNAME")
	private String cPcname;

	@Column(name= "C_TYP")
	private String cTyp;
	
	@Column(name= "C_GERAETECODE")
	private String cGeraetecode;
	
	@Column(name= "O_HASH")
	private byte[] oHash;

	@Column(name= "O_CODE")
	private byte[] oCode;
	
	private static final long serialVersionUID = 1L;

	public Arbeitsplatz() {
		super();
	}

	public Arbeitsplatz(Integer id, String pcname) {
		setIId(id);
		setCPcname(pcname);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCStandort() {
		return this.cStandort;
	}

	public void setCStandort(String cStandort) {
		this.cStandort = cStandort;
	}

	public String getCBemerkung() {
		return this.cBemerkung;
	}

	public void setCBemerkung(String cBemerkung) {
		this.cBemerkung = cBemerkung;
	}

	public String getCPcname() {
		return this.cPcname;
	}

	public void setCPcname(String cPcname) {
		this.cPcname = cPcname;
	}

	public void setCTyp(String cTyp) {
		this.cTyp = cTyp;
	}

	public String getCTyp() {
		return cTyp;
	}

	public void setCGeraetecode(String cGeraetecode) {
		this.cGeraetecode = cGeraetecode;
	}

	public String getCGeraetecode() {
		return cGeraetecode;
	}

	public byte[] getOHash() {
		return oHash;
	}

	public byte[] getOCode() {
		return oCode;
	}

}

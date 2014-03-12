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
package com.lp.server.system.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LP_DOKUMENT")
public class Dokument implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "O_INHALT")
	private byte[] oInhalt;

	@Column(name = "C_DATEINAME")
	private String cDateiname;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "DATENFORMAT_C_NR")
	private String datenformatCNr;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	private static final long serialVersionUID = 1L;

	public Dokument() {
		super();
	}

	public Dokument(Integer id, byte[] inhalt, String datenformatCNr2,
			Integer personalIIdAnlegen2) {
		setIId(id);
		setOInhalt(inhalt);
		setDatenformatCNr(datenformatCNr2);
		setPersonalIIdAnlegen(personalIIdAnlegen2);
		setTAnlegen(new Timestamp(System.currentTimeMillis()));
	}
	
	public Dokument(Integer id, Byte inhalt, String datenformatCNr2,
			Integer personalIIdAnlegen2) {
		this(id,new byte[]{inhalt},datenformatCNr2,personalIIdAnlegen2);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public byte[] getOInhalt() {
		return this.oInhalt;
	}

	public void setOInhalt(byte[] oInhalt) {
		this.oInhalt = oInhalt;
	}

	public String getCDateiname() {
		return this.cDateiname;
	}

	public void setCDateiname(String cDateiname) {
		this.cDateiname = cDateiname;
	}

	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public Timestamp getTAnlegen() {
		return this.tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public String getDatenformatCNr() {
		return this.datenformatCNr;
	}

	public void setDatenformatCNr(String datenformat) {
		this.datenformatCNr = datenformat;
	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

}

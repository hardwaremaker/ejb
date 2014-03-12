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
		@NamedQuery(name = "PersonalfingerfindByFingerid", query = "SELECT OBJECT(C) FROM Personalfinger c WHERE c.iFingerid = ?1"),
		@NamedQuery(name = "PersonalfingerfindByPersonalIIdFingerartIId", query = "SELECT OBJECT(C) FROM Personalfinger c WHERE c.personalIId = ?1 AND c.fingerartIId = ?2"),
		@NamedQuery(name = "PersonalfingerfindByTAendern", query = "SELECT OBJECT(C) FROM Personalfinger c WHERE c.tAendern >= ?1 ORDER BY c.iId ASC"),
		@NamedQuery(name = "PersonalfingerfindAll", query = "SELECT OBJECT(C) FROM Personalfinger c ORDER BY c.iId ASC") })
@Entity
@Table(name = "PERS_PERSONALFINGER")
public class Personalfinger implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "PERSONAL_I_ID")
	private Integer personalIId;

	@Column(name = "I_FINGERID")
	private Integer iFingerid;

	@Column(name = "O_TEMPLATE1")
	private byte[] oTemplate1;

	@Column(name = "O_TEMPLATE2")
	private byte[] oTemplate2;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "FINGERART_I_ID")
	private Integer fingerartIId;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	private static final long serialVersionUID = 1L;

	public Personalfinger() {
		super();
	}

	public Personalfinger(Integer id, 
			Integer personalIId2,
			Integer fingerid,
			byte[] template1, 
			Timestamp aendern, 
			Integer personalIIdAendern2,
			Integer fingerartIId) {
		setIId(id);
		setPersonalIId(personalIId2);
		setIFingerid(fingerid);
		setOTemplate1(template1);
		setTAendern(aendern);
		setPersonalIIdAendern(personalIIdAendern2);
		setFingerartIId(fingerartIId);
	}
	
	public Personalfinger(Integer id, 
			Integer personalIId2,
			Integer fingerartIId,
			Integer fingerid,
			Byte template1, 
			Integer personalIIdAendern2	) {
		this(id,personalIId2,fingerid,new byte[]{template1},new Timestamp(System.currentTimeMillis()),personalIIdAendern2,fingerartIId);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getPersonalIId() {
		return this.personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

	public Integer getIFingerid() {
		return this.iFingerid;
	}

	public void setIFingerid(Integer iFingerid) {
		this.iFingerid = iFingerid;
	}

	public byte[] getOTemplate1() {
		return this.oTemplate1;
	}

	public void setOTemplate1(byte[] oTemplate1) {
		this.oTemplate1 = oTemplate1;
	}

	public byte[] getOTemplate2() {
		return this.oTemplate2;
	}

	public void setOTemplate2(byte[] oTemplate2) {
		this.oTemplate2 = oTemplate2;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getFingerartIId() {
		return this.fingerartIId;
	}

	public void setFingerartIId(Integer fingerartIId) {
		this.fingerartIId = fingerartIId;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

}

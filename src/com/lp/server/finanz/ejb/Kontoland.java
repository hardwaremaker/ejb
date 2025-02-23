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
package com.lp.server.finanz.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.util.Validator;


@NamedQueries({
	@NamedQuery(
			name = KontolandQuery.ByCompound,
			query = "SELECT OBJECT(o) FROM Kontoland o " +
				"WHERE o.kontoIId=?1 AND o.landIId=?2 AND o.tGueltigAb=?3"),
	@NamedQuery(
			name = KontolandQuery.ByDate,
			query = "SELECT OBJECT(o) FROM Kontoland o " +
				"WHERE o.kontoIId=?1 AND o.landIId=?2 AND " +
				"o.tGueltigAb<=?3 ORDER BY o.tGueltigAb DESC")
})

@Entity
@Table(name = "FB_KONTOLAND")
public class Kontoland implements Serializable {
	private static final long serialVersionUID = 1L;

//	@EmbeddedId
//	private KontolandPK pk;

	@Id
	@Column(name = "I_ID")
	private Integer iId;
	
	@Column(name = "KONTO_I_ID_UEBERSETZT")
	private Integer kontoIIdUebersetzt;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "KONTO_I_ID")
	private Integer kontoIId;

	@Column(name = "LAND_I_ID")
	private Integer landIId;

	@Column(name = "T_GUELTIGAB")
	private Timestamp tGueltigAb;
		
	
	public Kontoland() {
		super();
	}

	public Kontoland(Integer kontoIId, Integer landIId,
			Integer kontoIIdUebersetzt, 
			Integer personalIIdAnlegen, 
			Integer personalIIdAendern,
			Timestamp gueltigAb) {
//		pk = new KontolandPK();
		setKontoIId(kontoIId);
		setLandIId(landIId);
		setKontoIIdUebersetzt(kontoIIdUebersetzt);
		Timestamp t = new Timestamp(System.currentTimeMillis());
		setTAnlegen(t);
		setTAendern(t);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setPersonalIIdAendern(personalIIdAendern);
		setTGueltigAb(gueltigAb);
	}

//	public KontolandPK getPk() {
//		return this.pk;
//	}

//	public void setPk(KontolandPK pk) {
//		this.pk = pk;
//	}

	public Integer getKontoIIdUebersetzt() {
		return this.kontoIIdUebersetzt;
	}

	public void setKontoIIdUebersetzt(Integer kontoIIdUebersetzt) {
		this.kontoIIdUebersetzt = kontoIIdUebersetzt;
	}

	public Timestamp getTAnlegen() {
		return this.tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getKontoIId() {
		return kontoIId;
	}

	public void setKontoIId(Integer kontoIId) {
		this.kontoIId = kontoIId;
	}

	public Integer getLandIId() {
		return landIId;
	}

	public void setLandIId(Integer landIId) {
		this.landIId = landIId;
	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
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

	public Timestamp getTGueltigAb() {
		return tGueltigAb;
	}
	
	public void setTGueltigAb(Timestamp gueltigAb) {
		Validator.notNull(gueltigAb, "gueltigAb");
		tGueltigAb = gueltigAb;
	}	
}

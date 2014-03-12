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
	@NamedQuery(name = "MaschinenzeitdatenfindLetzeOffeneMaschinenzeitdaten", query = "SELECT OBJECT(c) FROM Maschinenzeitdaten c WHERE c.maschineIId = ?1 AND c.tBis IS NULL ORDER BY c.tVon DESC"),
	@NamedQuery(name = "MaschinenzeitdatenfindByLossollarbeitsplanIId", query = "SELECT OBJECT(c) FROM Maschinenzeitdaten c WHERE c.lossollarbeitsplanIId = ?1"),
	@NamedQuery(name = "MaschinenzeitdatenfindByPersonalIIdGestartet", query = "SELECT OBJECT(c) FROM Maschinenzeitdaten c WHERE c.personalIIdGestartet = ?1 AND c.tVon <?2 AND c.tBis IS NULL")})


@Entity
@Table(name = "PERS_MASCHINENZEITDATEN")
public class Maschinenzeitdaten implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "T_VON")
	private Timestamp tVon;

	@Column(name = "T_BIS")
	private Timestamp tBis;

	@Column(name = "C_BEMERKUNG")
	private String cBemerkung;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "MASCHINE_I_ID")
	private Integer maschineIId;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "PERSONAL_I_ID_GESTARTET")
	private Integer personalIIdGestartet;

	
	
	public Integer getPersonalIIdGestartet() {
		return personalIIdGestartet;
	}

	public void setPersonalIIdGestartet(Integer personalIIdGestartet) {
		this.personalIIdGestartet = personalIIdGestartet;
	}

	@Column(name = "LOSSOLLARBEITSPLAN_I_ID")
	private Integer lossollarbeitsplanIId;

	private static final long serialVersionUID = 1L;

	public Maschinenzeitdaten() {
		super();
	}

	public Maschinenzeitdaten(Integer id, Integer maschineIId, Timestamp tVon,
			Integer personalIIdAnlegen2,
			Integer personalIIdAendern2, Integer lossollarbeitsplanIId,
			java.sql.Timestamp tAendern, java.sql.Timestamp tAnlegen, Integer personalIIdGestartet) {
		setIId(id);
		setLossollarbeitsplanIId(lossollarbeitsplanIId);
		setTVon(tVon);
		setMaschineIId(maschineIId);
		setPersonalIIdAnlegen(personalIIdAnlegen2);
		setPersonalIIdAendern(personalIIdAendern2);
		setTAendern(tAendern);
		setTAnlegen(tAnlegen);
		setPersonalIIdGestartet(personalIIdGestartet);

	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
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

	public Integer getMaschineIId() {
		return this.maschineIId;
	}

	public void setMaschineIId(Integer maschineIId) {
		this.maschineIId = maschineIId;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Timestamp getTVon() {
		return tVon;
	}

	public Integer getLossollarbeitsplanIId() {
		return lossollarbeitsplanIId;
	}

	public void setLossollarbeitsplanIId(Integer lossollarbeitsplanIId) {
		this.lossollarbeitsplanIId = lossollarbeitsplanIId;
	}

	public void setTVon(Timestamp von) {
		tVon = von;
	}

	public Timestamp getTBis() {
		return tBis;
	}

	public void setTBis(Timestamp bis) {
		tBis = bis;
	}

	public String getCBemerkung() {
		return cBemerkung;
	}

	public void setCBemerkung(String bemerkung) {
		cBemerkung = bemerkung;
	}

}

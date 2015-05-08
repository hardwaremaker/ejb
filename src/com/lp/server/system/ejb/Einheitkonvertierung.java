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
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "EinheitKonvertierungfindByVonZu", query = "SELECT OBJECT(o) FROM Einheitkonvertierung o WHERE o.einheitCNrVon=?1 AND o.einheitCNrZu=?2"),
		@NamedQuery(name = "EinheitKonvertierungfindAll", query = "SELECT OBJECT(o) FROM Einheitkonvertierung o") })
@Entity
@Table(name = "LP_EINHEITKONVERTIERUNG")
public class Einheitkonvertierung implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "N_FAKTOR")
	private BigDecimal nFaktor;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "EINHEIT_C_NR_VON")
	private String einheitCNrVon;

	@Column(name = "EINHEIT_C_NR_ZU")
	private String einheitCNrZu;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	private static final long serialVersionUID = 1L;

	public Einheitkonvertierung() {
		super();
	}

	public Einheitkonvertierung(Integer id, 
			String einheitCNrVon2,
			String einheitCNrZu2,
			BigDecimal faktor,
			Integer personalIIdAnlegen2,
			Integer personalIIdAendern2	) {
		setIId(id);
	    Timestamp t = new Timestamp(System.currentTimeMillis());
	    setTAendern(t);
	    setTAnlegen(t);
		setPersonalIIdAnlegen(personalIIdAnlegen2);
		setPersonalIIdAendern(personalIIdAendern2);
		setNFaktor(faktor);
		setEinheitCNrZu(einheitCNrZu2);
		setEinheitCNrVon(einheitCNrVon2);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public BigDecimal getNFaktor() {
		return this.nFaktor;
	}

	public void setNFaktor(BigDecimal nFaktor) {
		this.nFaktor = nFaktor;
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

	public String getEinheitCNrVon() {
		return this.einheitCNrVon;
	}

	public void setEinheitCNrVon(String einheitCNrVon) {
		this.einheitCNrVon = einheitCNrVon;
	}

	public String getEinheitCNrZu() {
		return this.einheitCNrZu;
	}

	public void setEinheitCNrZu(String einheitCNrZu) {
		this.einheitCNrZu = einheitCNrZu;
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

}

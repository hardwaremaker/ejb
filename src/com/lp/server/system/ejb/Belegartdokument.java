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
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "BelegartdokumentejbSelectMaxISort", query = "SELECT MAX (o.iSort) FROM Belegartdokument o WHERE o.belegartCNr = ?1 AND o.iBelegartid = ?2"),
		@NamedQuery(name = "BelegartdokumentfindByBelegartCNrIBelegartid", query = "SELECT OBJECT (o) FROM Belegartdokument o WHERE o.belegartCNr=?1 AND o.iBelegartid=?2"),
		@NamedQuery(name = "BelegartdokumentfindAll", query = "SELECT OBJECT (o) FROM Belegartdokument o")
		
})
@Entity
@Table(name = "LP_BELEGARTDOKUMENT")
public class Belegartdokument implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "I_BELEGARTID")
	private Integer iBelegartid;

	@Column(name = "I_SORT")
	private Integer iSort;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "BELEGART_C_NR")
	private String belegartCNr;

	@Column(name = "DOKUMENT_I_ID")
	private Integer dokumentIId;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	private static final long serialVersionUID = 1L;

	public Belegartdokument() {
		super();
	}

	public Belegartdokument(Integer id, String belegartCNr2,
			Integer belegartid, Integer dokumentIId2, Integer sort,
			Integer personalIIdAnlegen2) {
		setIId(id);
		setBelegartCNr(belegartCNr2);
		setIBelegartid(belegartid);
		setDokumentIId(dokumentIId2);
		setISort(sort);
		setPersonalIIdAnlegen(personalIIdAnlegen2);
		setTAnlegen(new Timestamp(System.currentTimeMillis()));
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getIBelegartid() {
		return this.iBelegartid;
	}

	public void setIBelegartid(Integer iBelegartid) {
		this.iBelegartid = iBelegartid;
	}

	public Integer getISort() {
		return this.iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public Timestamp getTAnlegen() {
		return this.tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public String getBelegartCNr() {
		return this.belegartCNr;
	}

	public void setBelegartCNr(String belegart) {
		this.belegartCNr = belegart;
	}

	public Integer getDokumentIId() {
		return this.dokumentIId;
	}

	public void setDokumentIId(Integer dokument) {
		this.dokumentIId = dokument;
	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

}

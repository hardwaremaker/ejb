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
package com.lp.server.auftrag.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "AuftragseriennrnfindByAuftragpositionIId", query = "SELECT OBJECT (o) FROM Auftragseriennrn o WHERE o.auftragpositionIId=?1"),
		@NamedQuery(name = "AuftragseriennrnfindByCSeriennrArtikelIId", query = "SELECT OBJECT (o) FROM Auftragseriennrn o WHERE o.cSeriennr=?1 AND o.artikelIId=?2"),
		@NamedQuery(name = "AuftragseriennrnejbSelectMaxVersionNr", query = "SELECT MAX (o.iVersionNr) FROM Auftragseriennrn AS o WHERE o.cSeriennr=?1 AND o.artikelIId = ?2"),
		@NamedQuery(name = "AuftragseriennrnejbSelectMaxISort", query = "SELECT MAX (o.iSort) FROM Auftragseriennrn AS o WHERE o.artikelIId = ?1") })
		
@Entity
@Table(name = "AUFT_AUFTRAGSERIENNRN")
public class Auftragseriennrn implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_SERIENNR")
	private String cSeriennr;

	@Column(name = "I_SORT")
	private Integer iSort;

	@Column(name = "C_KOMMENTAR")
	private String cKommentar;

	@Column(name = "AUFTRAGPOSITION_I_ID")
	private Integer auftragpositionIId;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;
	
	@Column(name = "VERSION_NR")
	private Integer iVersionNr;
	
	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;
	
	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;
	

	private static final long serialVersionUID = 1L;

	public Auftragseriennrn(Integer iId, Integer iSort, Integer artikelIId,
			String cSeriennr, Integer auftragpositionIId,Timestamp tAnlegen,Integer personalIIdAnlegen,Integer iVersionNr) {
		setIId(iId);
		setISort(iSort);
		setArtikelIId(artikelIId);
		setCSeriennr(cSeriennr);
		setAuftragpositionIId(auftragpositionIId);
		setTAnlegen(tAnlegen);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setIVersionNr(iVersionNr);
	}
	
	public Auftragseriennrn() {
		
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCSeriennr() {
		return this.cSeriennr;
	}

	public void setCSeriennr(String cSeriennr) {
		this.cSeriennr = cSeriennr;
	}

	public Integer getISort() {
		return this.iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public String getCKommentar() {
		return this.cKommentar;
	}

	public void setCKommentar(String cKommentar) {
		this.cKommentar = cKommentar;
	}

	public Integer getAuftragpositionIId() {
		return this.auftragpositionIId;
	}

	public void setAuftragpositionIId(Integer auftragpositionIId) {
		this.auftragpositionIId = auftragpositionIId;
	}

	public Integer getArtikelIId() {
		return this.artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Timestamp getTAnlegen() {
		return this.tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}
	
	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}
	
	public Integer getIVersionNr() {
		return this.iVersionNr;
	}

	public void setIVersionNr(Integer iVersionNr) {
		this.iVersionNr = iVersionNr;
	}
	
}

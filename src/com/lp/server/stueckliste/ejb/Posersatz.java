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
package com.lp.server.stueckliste.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "PosersatzfindByStuecklistepositionIIdArtikelIIdErsatz", query = "SELECT OBJECT(o) FROM Posersatz o WHERE o.stuecklistepositionIId = ?1 AND o.artikelIIdErsatz = ?2"),
		@NamedQuery(name = "PosersatzfindByStuecklistepositionIId", query = "SELECT OBJECT(o) FROM Posersatz o WHERE o.stuecklistepositionIId = ?1 ORDER BY o.iSort ASC"),
		@NamedQuery(name = "PosersatzejbSelectNextReihung", query = "SELECT MAX (o.iSort) FROM Posersatz o WHERE o.stuecklistepositionIId = ?1")})
@Entity
@Table(name = "STK_POSERSATZ")
public class Posersatz implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;
	
	@Column(name = "STUECKLISTEPOSITION_I_ID")
	private Integer stuecklistepositionIId;
	
	@Column(name = "ARTIKEL_I_ID_ERSATZ")
	private Integer artikelIIdErsatz;

	@Column(name = "I_SORT")
	private Integer iSort;

	private static final long serialVersionUID = 1L;

	public Posersatz() {
		super();
	}

	public Posersatz(Integer id, Integer stuecklistepositionIId,  Integer artikelIIdErsatz,  Integer sort) {
		setIId(id);
		setStuecklistepositionIId(stuecklistepositionIId);
		setArtikelIIdErsatz(artikelIIdErsatz);
		setISort(sort);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}


	public Integer getISort() {
		return this.iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public Integer getStuecklistepositionIId() {
		return stuecklistepositionIId;
	}

	public void setStuecklistepositionIId(Integer stuecklistepositionIId) {
		this.stuecklistepositionIId = stuecklistepositionIId;
	}

	public Integer getArtikelIIdErsatz() {
		return artikelIIdErsatz;
	}

	public void setArtikelIIdErsatz(Integer artikelIIdErsatz) {
		this.artikelIIdErsatz = artikelIIdErsatz;
	}

	

}

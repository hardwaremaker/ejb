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
package com.lp.server.projekt.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.lp.server.angebotstkl.ejb.Webabfrage;
import com.lp.server.artikel.ejb.Artikel;

@NamedQueries({
		@NamedQuery(name = "ProjekttaetigkeitfindByProjektIIdArtikelIId", query = "SELECT OBJECT(o) FROM Projekttaetigkeit o WHERE o.projektIId=?1 AND o.artikelIId=?2"),
		@NamedQuery(name = "ProjekttaetigkeitfindByProjektIId", query = "SELECT OBJECT(o) FROM Projekttaetigkeit o JOIN o.artikel a WHERE o.projektIId=?1 ORDER BY a.cNr") })
@Entity
@Table(name = "PROJ_PROJEKTTAETIGKEIT")
public class Projekttaetigkeit implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "PROJEKT_I_ID")
	private Integer projektIId;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ARTIKEL_I_ID", referencedColumnName = "I_ID", insertable = false, updatable = false)
	private Artikel artikel;

	private static final long serialVersionUID = 1L;

	public Projekttaetigkeit() {
		super();
	}

	public Projekttaetigkeit(Integer id, Integer projektIId, Integer artikelIId) {
		setIId(id);
		setProjektIId(projektIId);
		setArtikelIId(artikelIId);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Integer getProjektIId() {
		return this.projektIId;
	}

	public void setProjektIId(Integer projektIId) {
		this.projektIId = projektIId;
	}

}

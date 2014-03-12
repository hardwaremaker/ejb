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
package com.lp.server.projekt.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "KategoriefindAll", query = "SELECT OBJECT(o) FROM Kategorie o ORDER BY o.iSort ASC"),
		@NamedQuery(name = "KategoriefindAllByMandantCNr", query = "SELECT OBJECT(o) FROM Kategorie o WHERE o.pk.mandantCNr=?1 ORDER BY o.iSort ASC") })
@Entity
@Table(name = "PROJ_KATEGORIE")
public class Kategorie implements Serializable {
	public Kategorie() {
		// TODO Auto-generated constructor stub
	}

	@EmbeddedId
	private KategoriePK pk;

	@Column(name = "I_SORT")
	private Integer iSort;



	private static final long serialVersionUID = 1L;

	public Kategorie(String cNr,Integer iSort, String mandantCNr) {
		KategoriePK pk = new KategoriePK(cNr, mandantCNr);
		setPk(pk);
		setISort(iSort);
	}

	public KategoriePK getPk() {
		return this.pk;
	}

	public void setPk(KategoriePK pk) {
		this.pk = pk;
	}

	public Integer getISort() {
		return this.iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

}

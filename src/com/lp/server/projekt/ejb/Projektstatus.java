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
		@NamedQuery(name = "ProjektStatusfindAll", query = "SELECT OBJECT (o) FROM Projektstatus o ORDER BY o.iSort ASC"),
		@NamedQuery(name = "ProjektStatusfindAllByMandantCNr", query = "SELECT OBJECT (o) FROM Projektstatus o WHERE o.pk.mandantCNr=?1"),
		@NamedQuery(name = "ProjektStatusfindMandantCNrCNr", query = "SELECT OBJECT (o) FROM Projektstatus o WHERE o.pk.mandantCNr=?1 AND o.pk.cNr=?2"),
		@NamedQuery(name = "ProjektStatusejbSelectNextReihung", query = "SELECT MAX (o.iSort) FROM Projektstatus o WHERE o.pk.mandantCNr=?1")})
@Entity
@Table(name = "PROJ_PROJEKTSTATUS")
public class Projektstatus implements Serializable {
	public Projektstatus() {
		// TODO Auto-generated constructor stub
	}

	@EmbeddedId
	private ProjektstatusPK pk;

	@Column(name = "I_SORT")
	private Integer iSort;


	private static final long serialVersionUID = 1L;

	public Projektstatus( Integer iSort,String mandantCNr,String cNr,Short bAenderungprotokollieren) {
		ProjektstatusPK pk = new ProjektstatusPK(cNr, mandantCNr);
		setPk(pk);
		setBAenderungprotokollieren(bAenderungprotokollieren);
		setISort(iSort);
	}

	@Column(name = "B_AENDERUNGPROTOKOLLIEREN")
	private Short bAenderungprotokollieren;
	
	public Short getBAenderungprotokollieren() {
		return bAenderungprotokollieren;
	}

	public void setBAenderungprotokollieren(Short bAenderungprotokollieren) {
		this.bAenderungprotokollieren = bAenderungprotokollieren;
	}

	public ProjektstatusPK getPk() {
		return this.pk;
	}

	public void setPk(ProjektstatusPK pk) {
		this.pk = pk;
	}

	public Integer getISort() {
		return this.iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

}

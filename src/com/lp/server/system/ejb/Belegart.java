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
package com.lp.server.system.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.util.ICNr;

@NamedQueries( {
		@NamedQuery(name = "BelegartfindAll", query = "SELECT OBJECT(k) FROM Belegart k"),
		@NamedQuery(name = "BelegartfindBycNr", query = "SELECT OBJECT (o) FROM Belegart o WHERE o.cNr = ?1") })
@Entity
@Table(name = "LP_BELEGART")
public class Belegart implements Serializable, ICNr {
	@Id
	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "I_SORT")
	private Integer iSort;

	@Column(name = "C_KBEZ")
	private String cKbez;

	@Column(name = "I_STANDARDERLEDIGUNGSZEITINTAGEN")
	private Integer iStandarderledigungszeitintagen;



	private static final long serialVersionUID = 1L;

	public Belegart() {
		super();
	}

	public Belegart(String nr, Integer standarderledigungszeitInTagen,
			String kurzbezeichnung, Integer sort) {
		setCNr(nr);
		setIStandarderledigungszeitintagen(standarderledigungszeitInTagen);
		setCKbez(kurzbezeichnung);
		setISort(sort);
	}

	public String getCNr() {
		return this.cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public Integer getISort() {
		return this.iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public String getCKbez() {
		return this.cKbez;
	}

	public void setCKbez(String cKbez) {
		this.cKbez = cKbez;
	}

	public Integer getIStandarderledigungszeitintagen() {
		return this.iStandarderledigungszeitintagen;
	}

	public void setIStandarderledigungszeitintagen(
			Integer iStandarderledigungszeitintagen) {
		this.iStandarderledigungszeitintagen = iStandarderledigungszeitintagen;
	}


}

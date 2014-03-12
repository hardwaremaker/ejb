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

@NamedQueries( { @NamedQuery(name = "LandplzortfindByLandIIdByCPlzByOrtIId", query = "SELECT OBJECT (o) FROM Landplzort o WHERE o.landIId = ?1 AND o.cPlz = ?2 AND o.ortIId = ?3") })
@Entity
@Table(name = "LP_LANDPLZORT")
public class Landplzort implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_PLZ")
	private String cPlz;

	@Column(name = "LAND_I_ID")
	private Integer landIId;

	@Column(name = "ORT_I_ID")
	private Integer ortIId;

	private static final long serialVersionUID = 1L;

	public Landplzort() {
		super();
	}

	public Landplzort(Integer id, String plz, Integer ortIId, Integer ilandID) {
		setIId(id);
		setCPlz(plz);
		setOrtIId(ortIId);
		setLandIId(ilandID);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCPlz() {
		return this.cPlz;
	}

	public void setCPlz(String cPlz) {
		this.cPlz = cPlz;
	}

	public Integer getLandIId() {
		return this.landIId;
	}

	public void setLandIId(Integer landIId) {
		this.landIId = landIId;
	}

	public Integer getOrtIId() {
		return this.ortIId;
	}

	public void setOrtIId(Integer ort) {
		this.ortIId = ort;
	}

}

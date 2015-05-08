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
		@NamedQuery(name = "MwstsatzfindAll", query = "SELECT OBJECT (o) FROM Mwstsatz o"),
		@NamedQuery(name = "MwstsatzfindByMwstbez", query = "SELECT OBJECT (o) FROM Mwstsatz o WHERE o.mwstsatzbezIId=?1 ORDER BY o.dGueltigab ASC"),
		@NamedQuery(name = "MwstsatzfindByTGueltigabMwstsatzbezIId", query = "SELECT OBJECT (o) FROM Mwstsatz o WHERE o.dGueltigab<=?1 AND o.mwstsatzbezIId=?2 ORDER BY o.dGueltigab DESC") })
@Entity
@Table(name = "LP_MWSTSATZ")
public class Mwstsatz implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "F_MWSTSATZ")
	private Double fMwstsatz;

	@Column(name = "D_GUELTIGAB")
	private Timestamp dGueltigab;

	@Column(name = "MWSTSATZBEZ_I_ID")
	private Integer mwstsatzbezIId;
	
	@Column(name="I_FIBUMWSTCODE")
	private Integer iFibumwstcode;

	private static final long serialVersionUID = 1L;

	public Mwstsatz() {
		super();
	}

	public Mwstsatz(Integer id,Double mwstsatz,  Timestamp gueltigab,Integer mwstsatzbezId
			) {
		setIId(id);
		setMwstsatzbezIId(mwstsatzbezId);
		setDGueltigab(gueltigab);
		setFMwstsatz(mwstsatz);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Double getFMwstsatz() {
		return this.fMwstsatz;
	}

	public void setFMwstsatz(Double fMwstsatz) {
		this.fMwstsatz = fMwstsatz;
	}
	
	public Integer getIFibumwstcode(){
		return this.iFibumwstcode;
	}
	
	public void setIFibumwstcode(Integer iFibumwstcode){
		this.iFibumwstcode = iFibumwstcode;
	}

	public Timestamp getDGueltigab() {
		return this.dGueltigab;
	}

	public void setDGueltigab(Timestamp dGueltigab) {
		this.dGueltigab = dGueltigab;
	}

	public Integer getMwstsatzbezIId() {
		return this.mwstsatzbezIId;
	}

	public void setMwstsatzbezIId(Integer mwstsatzbez) {
		this.mwstsatzbezIId = mwstsatzbez;
	}
}

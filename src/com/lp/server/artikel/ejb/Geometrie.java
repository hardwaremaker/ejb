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
package com.lp.server.artikel.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WW_GEOMETRIE")
public class Geometrie implements Serializable {
	@Id
	@Column(name = "ARTIKEL_I_ID", insertable = false, updatable = false)
	private Integer artikelIId;

	@Column(name = "F_BREITE")
	private Double fBreite;

	@Column(name = "C_BREITETEXT")
	private String cBreitetext;

	@Column(name = "F_HOEHE")
	private Double fHoehe;

	@Column(name = "F_TIEFE")
	private Double fTiefe;


	private static final long serialVersionUID = 1L;

	public Geometrie() {
		super();
	}

	public Geometrie(Integer artikelIId) {
		setArtikelIId(artikelIId);
	}

	public Integer getArtikelIId() {
		return this.artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Double getFBreite() {
		return this.fBreite;
	}

	public void setFBreite(Double fBreite) {
		this.fBreite = fBreite;
	}

	public String getCBreitetext() {
		return this.cBreitetext;
	}

	public void setCBreitetext(String cBreitetext) {
		this.cBreitetext = cBreitetext;
	}

	public Double getFHoehe() {
		return this.fHoehe;
	}

	public void setFHoehe(Double fHoehe) {
		this.fHoehe = fHoehe;
	}

	public Double getFTiefe() {
		return this.fTiefe;
	}

	public void setFTiefe(Double fTiefe) {
		this.fTiefe = fTiefe;
	}



}

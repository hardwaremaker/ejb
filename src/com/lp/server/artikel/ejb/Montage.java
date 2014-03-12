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
@Table(name = "WW_MONTAGE")
public class Montage implements Serializable {
	@Id
	@Column(name = "ARTIKEL_I_ID", insertable = false, updatable = false)
	private Integer artikelIId;

	@Column(name = "F_RASTERLIEGEND")
	private Double fRasterliegend;

	@Column(name = "F_RASTERSTEHEND")
	private Double fRasterstehend;

	@Column(name = "B_HOCHSTELLEN")
	private Short bHochstellen;

	@Column(name = "B_HOCHSETZEN")
	private Short bHochsetzen;

	@Column(name = "B_POLARISIERT")
	private Short bPolarisiert;


	private static final long serialVersionUID = 1L;

	public Montage() {
		super();
	}

	public Montage(Integer artikelIid) {
		setArtikelIId(artikelIid);
	}

	public Integer getArtikelIId() {
		return this.artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Double getFRasterliegend() {
		return this.fRasterliegend;
	}

	public void setFRasterliegend(Double fRasterliegend) {
		this.fRasterliegend = fRasterliegend;
	}

	public Double getFRasterstehend() {
		return this.fRasterstehend;
	}

	public void setFRasterstehend(Double fRasterstehend) {
		this.fRasterstehend = fRasterstehend;
	}

	public Short getBHochstellen() {
		return this.bHochstellen;
	}

	public void setBHochstellen(Short bHochstellen) {
		this.bHochstellen = bHochstellen;
	}

	public Short getBHochsetzen() {
		return this.bHochsetzen;
	}

	public void setBHochsetzen(Short bHochsetzen) {
		this.bHochsetzen = bHochsetzen;
	}

	public Short getBPolarisiert() {
		return this.bPolarisiert;
	}

	public void setBPolarisiert(Short bPolarisiert) {
		this.bPolarisiert = bPolarisiert;
	}

}


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
package com.lp.server.angebotstkl.ejb;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "AgstklmaterialfindByAgstklIId", query = "SELECT OBJECT (o) FROM Agstklmaterial o WHERE o.agstklIId=?1 ORDER BY o.iSort"),
		@NamedQuery(name = "AgstklmaterialejbSelectMaxISort", query = "SELECT MAX (o.iSort) FROM Agstklmaterial o WHERE o.agstklIId=?1") })
@Entity
@Table(name = "AS_AGSTKLMATERIAL")
public class Agstklmaterial implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "I_SORT")
	private Integer iSort;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "AGSTKL_I_ID")
	private Integer agstklIId;

	@Column(name = "MATERIAL_I_ID")
	private Integer materialIId;

	@Column(name = "N_DIMENSION1")
	private BigDecimal nDimension1;

	public BigDecimal getNDimension1() {
		return nDimension1;
	}

	public void setNDimension1(BigDecimal nDimension1) {
		this.nDimension1 = nDimension1;
	}

	public BigDecimal getNDimension2() {
		return nDimension2;
	}

	public void setNDimension2(BigDecimal nDimension2) {
		this.nDimension2 = nDimension2;
	}

	public BigDecimal getNDimension3() {
		return nDimension3;
	}

	public void setNDimension3(BigDecimal nDimension3) {
		this.nDimension3 = nDimension3;
	}

	public String getCMaterialtyp() {
		return cMaterialtyp;
	}

	public void setCMaterialtyp(String cMaterialtyp) {
		this.cMaterialtyp = cMaterialtyp;
	}

	
	@Column(name = "N_GEWICHT")
	private BigDecimal nGewicht;
	
	public BigDecimal getNGewicht() {
		return nGewicht;
	}

	public void setNGewicht(BigDecimal nGewicht) {
		this.nGewicht = nGewicht;
	}


	@Column(name = "N_GEWICHTPREIS")
	private BigDecimal nGewichtpreis;

	public BigDecimal getNGewichtpreis() {
		return nGewichtpreis;
	}

	public void setNGewichtpreis(BigDecimal nGewichtpreis) {
		this.nGewichtpreis = nGewichtpreis;
	}

	@Column(name = "N_DIMENSION2")
	private BigDecimal nDimension2;
	@Column(name = "N_DIMENSION3")
	private BigDecimal nDimension3;

	@Column(name = "C_MATERIALTYP")
	private String cMaterialtyp;

	private static final long serialVersionUID = 1L;

	public Agstklmaterial() {
		super();
	}

	public Agstklmaterial(Integer id, Integer agstklIId, Integer materialIId, Integer iSort) {
		setIId(id);
		setAgstklIId(agstklIId);
		setMaterialIId(materialIId);
		setISort(iSort);
	
	}

	public Integer getMaterialIId() {
		return materialIId;
	}

	public void setMaterialIId(Integer materialIId) {
		this.materialIId = materialIId;
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

	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	

	public Integer getAgstklIId() {
		return this.agstklIId;
	}

	public void setAgstklIId(Integer agstklIId) {
		this.agstklIId = agstklIId;
	}

	

}

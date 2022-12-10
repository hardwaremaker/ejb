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
package com.lp.server.partner.ejb;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({ @NamedQuery(name = "KundematerialfindByKundeIIdMaterialIId", query = "SELECT OBJECT (o) FROM Kundematerial o WHERE o.kundeIId=?1 AND o.materialIId=?2")

})
@Entity
@Table(name = "PART_KUNDEMATERIAL")
public class Kundematerial implements Serializable {
	public Kundematerial() {
	}

	@Id
	@Column(name = "I_ID")
	private Integer iId;
	@Column(name = "KUNDE_I_ID")
	private Integer kundeIId;

	public Integer getMaterialIId() {
		return materialIId;
	}

	public void setMaterialIId(Integer materialIId) {
		this.materialIId = materialIId;
	}

	public Integer getMaterialIIdNotierung() {
		return materialIIdNotierung;
	}

	public void setMaterialIIdNotierung(Integer materialIIdNotierung) {
		this.materialIIdNotierung = materialIIdNotierung;
	}

	public BigDecimal getNMaterialbasis() {
		return nMaterialbasis;
	}

	public void setNMaterialbasis(BigDecimal nMaterialbasis) {
		this.nMaterialbasis = nMaterialbasis;
	}

	public BigDecimal getNAufschlagBetrag() {
		return nAufschlagBetrag;
	}

	public void setNAufschlagBetrag(BigDecimal nAufschlagBetrag) {
		this.nAufschlagBetrag = nAufschlagBetrag;
	}

	public Short getBMaterialInklusive() {
		return bMaterialInklusive;
	}

	public void setBMaterialInklusive(Short bMaterialInklusive) {
		this.bMaterialInklusive = bMaterialInklusive;
	}

	public Double getFAufschlagProzent() {
		return fAufschlagProzent;
	}

	public void setFAufschlagProzent(Double fAufschlagProzent) {
		this.fAufschlagProzent = fAufschlagProzent;
	}

	@Column(name = "MATERIAL_I_ID")
	private Integer materialIId;
	@Column(name = "MATERIAL_I_ID_NOTIERUNG")
	private Integer materialIIdNotierung;

	@Column(name = "N_MATERIALBASIS")
	private BigDecimal nMaterialbasis;
	@Column(name = "N_AUFSCHLAG_BETRAG")
	private BigDecimal nAufschlagBetrag;

	@Column(name = "B_MATERIAL_INKLUSIVE")
	private Short bMaterialInklusive;

	@Column(name = "F_AUFSCHLAG_PROZENT")
	private Double fAufschlagProzent;

	private static final long serialVersionUID = 1L;

	public Kundematerial(Integer iId, Integer kundeIId, Integer materialIId, Integer materialIIdNotierung, BigDecimal nMaterialbasis, Short bMaterialInklusive) {
		setIId(iId);
		setKundeIId(kundeIId);
		setMaterialIId(materialIId);
		setMaterialIIdNotierung(materialIIdNotierung);
		setNMaterialbasis(nMaterialbasis);
		setBMaterialInklusive(bMaterialInklusive);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getKundeIId() {
		return this.kundeIId;
	}

	public void setKundeIId(Integer kundeIId) {
		this.kundeIId = kundeIId;
	}

}

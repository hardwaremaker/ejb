
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
package com.lp.server.personal.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "MaschineleistungsfaktorfindAktuellenLeistungsfaktor", query = "SELECT OBJECT(o) FROM Maschineleistungsfaktor o WHERE o.maschineIId = ?1 AND  o.materialIId = ?2 AND o.tGueltigab <= ?3 ORDER BY o.tGueltigab DESC"),
		@NamedQuery(name = "MaschineleistungsfaktorfindByMaschineIIdMaterialIIdTGueltigab", query = "SELECT OBJECT(o) FROM Maschineleistungsfaktor o WHERE o.maschineIId = ?1 AND  o.materialIId = ?2 AND  o.tGueltigab = ?3 ORDER BY o.tGueltigab DESC") })
@Entity
@Table(name = "PERS_MASCHINELEISTUNGSFAKTOR")
public class Maschineleistungsfaktor implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "MASCHINE_I_ID")
	private Integer maschineIId;

	public Integer getMaschineIId() {
		return maschineIId;
	}

	public void setMaschineIId(Integer maschineIId) {
		this.maschineIId = maschineIId;
	}

	@Column(name = "MATERIAL_I_ID")
	private Integer materialIId;

	@Column(name = "T_GUELTIGAB")
	private Timestamp tGueltigab;
	@Column(name = "N_FAKTOR_IN_PROZENT")
	private BigDecimal nFaktorInProzent;

	public BigDecimal getNFaktorInProzent() {
		return nFaktorInProzent;
	}

	public void setNFaktorInProzent(BigDecimal nFaktorInProzent) {
		this.nFaktorInProzent = nFaktorInProzent;
	}

	private static final long serialVersionUID = 1L;

	public Maschineleistungsfaktor() {
		super();
	}

	public Maschineleistungsfaktor(Integer iId, Integer maschineIId, Integer materialIId, Timestamp gueltigab,
			BigDecimal nFaktorInProzent) {
		setMaschineIId(maschineIId);
		setMaterialIId(materialIId);
		setTGueltigab(gueltigab);
		setIId(iId);
		setNFaktorInProzent(nFaktorInProzent);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Timestamp getTGueltigab() {
		return this.tGueltigab;
	}

	public void setTGueltigab(Timestamp tGueltigab) {
		this.tGueltigab = tGueltigab;
	}

	public Integer getMaterialIId() {
		return this.materialIId;
	}

	public void setMaterialIId(Integer materialIId) {
		this.materialIId = materialIId;
	}

}

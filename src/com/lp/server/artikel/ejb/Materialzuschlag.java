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
package com.lp.server.artikel.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "MaterialzuschlagfindAktuellenZuschlag", query = "SELECT OBJECT(o) FROM Materialzuschlag o WHERE o.materialIId = ?1 AND o.mandantCNr = ?2 AND o.tGueltigab <= ?3 ORDER BY o.tGueltigab DESC"),
		@NamedQuery(name = "MaterialzuschlagfindByMaterialIIdMandantCNrTGueltigab", query = "SELECT OBJECT(o) FROM Materialzuschlag o WHERE o.materialIId = ?1 AND o.mandantCNr = ?2 AND o.tGueltigab = ?3 ORDER BY o.tGueltigab DESC") })
@Entity
@Table(name = "WW_MATERIALZUSCHLAG")
public class Materialzuschlag implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "T_GUELTIGAB")
	private Timestamp tGueltigab;

	@Column(name = "N_ZUSCHLAG")
	private BigDecimal nZuschlag;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "MATERIAL_I_ID")
	private Integer materialIId;

	private static final long serialVersionUID = 1L;

	public Materialzuschlag() {
		super();
	}

	public Materialzuschlag(Integer materialIId2,
			String mandantCNr2,
			Timestamp gueltigab, 
			Integer id) {
		setMaterialIId(materialIId2);
		setMandantCNr(mandantCNr2);
		setTGueltigab(gueltigab);
		setIId(id);
		setNZuschlag(new BigDecimal(0));
	}
	
	public Materialzuschlag(Integer id,
			Integer materialIId2,
			String mandantCNr2,
			Timestamp gueltigab, 
			BigDecimal zuschlag
			) {
		setMaterialIId(materialIId2);
		setMandantCNr(mandantCNr2);
		setTGueltigab(gueltigab);
		setIId(id);
		setNZuschlag(zuschlag);
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

	public BigDecimal getNZuschlag() {
		return this.nZuschlag;
	}

	public void setNZuschlag(BigDecimal nZuschlag) {
		this.nZuschlag = nZuschlag;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Integer getMaterialIId() {
		return this.materialIId;
	}

	public void setMaterialIId(Integer materialIId) {
		this.materialIId = materialIId;
	}

}

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
package com.lp.server.finanz.ejb;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({ @NamedQuery(name = "MahnspesenFindByIMahnstufeMandantCNrWaehrungCNr", query = "SELECT OBJECT(o) FROM Mahnspesen o WHERE o.iMahnstufe=?1 AND o.mandantCNr=?2 AND o.waehrungCNr=?3") })
@Entity
@Table(name = "FB_MAHNSPESEN")
public class Mahnspesen implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "MAHNSTUFE_I_ID")
	private Integer iMahnstufe;

	@Column(name = "N_MAHNSPESEN")
	private BigDecimal nMahnspesen;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "WAEHRUNG_C_NR")
	private String waehrungCNr;

	private static final long serialVersionUID = 1L;

	public Mahnspesen() {
		super();
	}

	public Mahnspesen(Integer id, String mandantCNr, Integer mahnstufe,
			String waehrungCNr, BigDecimal mahnspesen) {
		setIId(id);
		setMandantCNr(mandantCNr);
		setIMahnstufe(mahnstufe);
		setWaehrungCNr(waehrungCNr);
		setNMahnspesen(mahnspesen);
	}

	public Integer getIMahnstufe() {
		return iMahnstufe;
	}

	public void setIMahnstufe(Integer iMahnstufe) {
		this.iMahnstufe = iMahnstufe;
	}

	public String getWaehrungCNr() {
		return waehrungCNr;
	}

	public void setWaehrungCNr(String waehrungCNr) {
		this.waehrungCNr = waehrungCNr;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public BigDecimal getNMahnspesen() {
		return this.nMahnspesen;
	}

	public void setNMahnspesen(BigDecimal nMahnspesen) {
		this.nMahnspesen = nMahnspesen;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

}

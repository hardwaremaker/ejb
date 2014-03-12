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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( { @NamedQuery(name = "MahnstufefindByMandantCNr", query = "SELECT OBJECT(o) FROM Mahnstufe o WHERE o.mandantCNr=?1 ORDER BY o.pk.iId ASC") })
@Entity
@Table(name = "FB_MAHNSTUFE")
public class Mahnstufe implements Serializable {
	@EmbeddedId
	private MahnstufePK pk;

	@Column(name = "I_TAGE")
	private Integer iTage;

	@Column(name = "N_MAHNSPESEN")
	private BigDecimal nMahnspesen;

	@Column(name = "F_ZINSSATZ")
	private Float fZinssatz;

	@Column(name = "MANDANT_C_NR", insertable = false, updatable = false)
	private String mandantCNr;

	@Column(name = "I_ID", insertable = false, updatable = false)
	private Integer IId;


	private static final long serialVersionUID = 1L;

	public Mahnstufe() {
		super();
	}

	public Mahnstufe(Integer id, String mandantCNr, Integer tage) {
		setPk(new MahnstufePK(id,mandantCNr));
		setITage(tage);
	}

	public MahnstufePK getPk() {
		return this.pk;
	}

	public void setPk(MahnstufePK pk) {
		this.pk = pk;
	}

	public Integer getIId() {
		return this.IId;
	}

	public void setIId(Integer IId) {
		this.IId = IId;
	}

	public Integer getITage() {
		return this.iTage;
	}

	public void setITage(Integer iTage) {
		this.iTage = iTage;
	}

	public BigDecimal getNMahnspesen() {
		return this.nMahnspesen;
	}

	public void setNMahnspesen(BigDecimal nMahnspesen) {
		this.nMahnspesen = nMahnspesen;
	}

	public Float getFZinssatz() {
		return this.fZinssatz;
	}

	public void setFZinssatz(Float fZinssatz) {
		this.fZinssatz = fZinssatz;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

}

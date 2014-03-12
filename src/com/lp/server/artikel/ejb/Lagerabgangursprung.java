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
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "LagerabgangursprungfindByILagerbewegungIIdursprung", query = "SELECT OBJECT(C) FROM Lagerabgangursprung c WHERE c.pk.iLagerbewegungidursprung = ?1"),
		@NamedQuery(name = "LagerabgangursprungfindByILagerbewegungid", query = "SELECT OBJECT(C) FROM Lagerabgangursprung c WHERE c.pk.iLagerbewegungid = ?1") })
@Entity
@Table(name = "WW_LAGERABGANGURSPRUNG")
public class Lagerabgangursprung implements Serializable {
	@EmbeddedId
	private LagerabgangursprungPK pk;

	@Column(name = "N_VERBRAUCHTEMENGE")
	private BigDecimal nVerbrauchtemenge;

	@Column(name = "N_GESTEHUNGSPREIS")
	private BigDecimal nGestehungspreis;

	private static final long serialVersionUID = 1L;

	public Lagerabgangursprung() {
		super();
	}

	public Lagerabgangursprung(Integer lagerbewegungid,
			Integer lagerbewegungidursprung, BigDecimal verbrauchtemenge,
			BigDecimal gestehungspreis) {
		pk = new LagerabgangursprungPK(lagerbewegungid, lagerbewegungidursprung);
		setNVerbrauchtemenge(verbrauchtemenge);
		setNGestehungspreis(gestehungspreis);
	}

	public LagerabgangursprungPK getPk() {
		return this.pk;
	}

	public void setPk(LagerabgangursprungPK pk) {
		this.pk = pk;
	}

	public BigDecimal getNVerbrauchtemenge() {
		return this.nVerbrauchtemenge;
	}

	public void setNVerbrauchtemenge(BigDecimal nVerbrauchtemenge) {
		this.nVerbrauchtemenge = nVerbrauchtemenge;
	}

	public BigDecimal getNGestehungspreis() {
		return this.nGestehungspreis;
	}

	public void setNGestehungspreis(BigDecimal nGestehungspreis) {
		this.nGestehungspreis = nGestehungspreis;
	}

}

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

@NamedQueries( {
		@NamedQuery(name = "KundesokomengenstaffelfindByKundesokoIId", query = "SELECT OBJECT (o) FROM Kundesokomengenstaffel o WHERE o.kundesokoIId=?1 ORDER BY o.nMenge"),
		@NamedQuery(name = "KundesokomengenstaffelfindByKundesokoIIdNMenge", query = "SELECT OBJECT (o) FROM Kundesokomengenstaffel o WHERE o.kundesokoIId=?1 AND o.nMenge<=?2 ORDER BY o.nMenge"),
		@NamedQuery(name = "KundesokomengenstaffelfindByUniqueKey", query = "SELECT OBJECT (o) FROM Kundesokomengenstaffel o WHERE o.kundesokoIId=?1 AND o.nMenge=?2"), })
@Entity
@Table(name = "PART_KUNDESOKOMENGENSTAFFEL")
public class Kundesokomengenstaffel implements Serializable {
	public Kundesokomengenstaffel() {
		// TODO Auto-generated constructor stub
	}

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "N_MENGE")
	private BigDecimal nMenge;

	@Column(name = "F_ARTIKELSTANDARDRABATTSATZ")
	private Double fArtikelstandardrabattsatz;

	@Column(name = "N_ARTIKELFIXPREIS")
	private BigDecimal nArtikelfixpreis;

	@Column(name = "KUNDESOKO_I_ID")
	private Integer kundesokoIId;

	private static final long serialVersionUID = 1L;

	public Kundesokomengenstaffel(Integer iId, Integer kundesokoIId,
			BigDecimal nMenge) {
		setIId(iId);
		setKundesokoIId(kundesokoIId);
		setNMenge(nMenge);
		setFArtikelstandardrabattsatz(new Double(0));
	}
	
	public Kundesokomengenstaffel(Integer iId, Integer kundesokoIId,
			BigDecimal nMenge, Double artikelstandardrabattsatz) {
		setIId(iId);
		setKundesokoIId(kundesokoIId);
		setNMenge(nMenge);
		setFArtikelstandardrabattsatz(artikelstandardrabattsatz);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public BigDecimal getNMenge() {
		return this.nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public Double getFArtikelstandardrabattsatz() {
		return this.fArtikelstandardrabattsatz;
	}

	public void setFArtikelstandardrabattsatz(Double fArtikelstandardrabattsatz) {
		this.fArtikelstandardrabattsatz = fArtikelstandardrabattsatz;
	}

	public BigDecimal getNArtikelfixpreis() {
		return this.nArtikelfixpreis;
	}

	public void setNArtikelfixpreis(BigDecimal nArtikelfixpreis) {
		this.nArtikelfixpreis = nArtikelfixpreis;
	}

	public Integer getKundesokoIId() {
		return this.kundesokoIId;
	}

	public void setKundesokoIId(Integer kundesokoIId) {
		this.kundesokoIId = kundesokoIId;
	}

}

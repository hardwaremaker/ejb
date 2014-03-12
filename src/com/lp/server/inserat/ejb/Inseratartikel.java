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
package com.lp.server.inserat.ejb;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "InseratartikelfindInseratIId", query = "SELECT OBJECT (o) FROM Inseratartikel o WHERE o.inseratIId=?1"),
		@NamedQuery(name = "InseratartikelfindByBestellpositionIId", query = "SELECT OBJECT (o) FROM Inseratartikel o WHERE o.bestellpositionIId=?1") })
@Entity
@Table(name = "IV_INSERATARTIKEL")
public class Inseratartikel implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "INSERAT_I_ID")
	private Integer inseratIId;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@Column(name = "N_MENGE")
	private BigDecimal nMenge;

	@Column(name = "N_NETTOEINZELPREIS_EK")
	private BigDecimal nNettoeinzelpreisEk;

	@Column(name = "N_NETTOEINZELPREIS_VK")
	private BigDecimal nNettoeinzelpreisVk;

	@Column(name = "BESTELLPOSITION_I_ID")
	private Integer bestellpositionIId;

	public Integer getBestellpositionIId() {
		return bestellpositionIId;
	}

	public void setBestellpositionIId(Integer bestellpositionIId) {
		this.bestellpositionIId = bestellpositionIId;
	}

	public Integer getInseratIId() {
		return inseratIId;
	}

	public void setInseratIId(Integer inseratIId) {
		this.inseratIId = inseratIId;
	}

	private static final long serialVersionUID = 1L;

	public Inseratartikel(Integer id, Integer inseratIId, Integer artikelIId,
			BigDecimal nMenge, BigDecimal nNettoeinzelpreisEk,
			BigDecimal nNettoeinzelpreisVk) {
		setIId(id);
		setInseratIId(inseratIId);
		setArtikelIId(artikelIId);
		setNMenge(nMenge);
		setNNettoeinzelpreisEk(nNettoeinzelpreisEk);
		setNNettoeinzelpreisVk(nNettoeinzelpreisVk);
	}

	public BigDecimal getNMenge() {
		return nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public BigDecimal getNNettoeinzelpreisEk() {
		return nNettoeinzelpreisEk;
	}

	public void setNNettoeinzelpreisEk(BigDecimal nNettoeinzelpreisEk) {
		this.nNettoeinzelpreisEk = nNettoeinzelpreisEk;
	}

	public BigDecimal getNNettoeinzelpreisVk() {
		return nNettoeinzelpreisVk;
	}

	public void setNNettoeinzelpreisVk(BigDecimal nNettoeinzelpreisVk) {
		this.nNettoeinzelpreisVk = nNettoeinzelpreisVk;
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Inseratartikel() {

	}

}

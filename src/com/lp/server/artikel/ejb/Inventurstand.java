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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({ @NamedQuery(name = "InventurstandfindByInventurIIdArtikelIIdLagerIId", query = "SELECT OBJECT (o) FROM Inventurstand o WHERE o.inventurIId=?1 AND o.artikelIId=?2 AND o.lagerIId=?3"),
	@NamedQuery(name = "InventurstandfindByArtikelIId", query = "SELECT OBJECT (o) FROM Inventurstand o WHERE o.artikelIId=?1")})
@Entity
@Table(name = "WW_INVENTURSTAND")
public class Inventurstand implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "N_INVENTURMENGE")
	private BigDecimal nInventurmenge;

	@Column(name = "N_INVENTURPREIS")
	private BigDecimal nInventurpreis;

	@Column(name = "N_ABGEWERTETERPREIS")
	private BigDecimal nAbgewerteterpreis;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@Column(name = "INVENTUR_I_ID")
	private Integer inventurIId;

	@Column(name = "LAGER_I_ID")
	private Integer lagerIId;

	@Column(name = "N_BASISPREIS")
	private BigDecimal nBasispreis;

	@Column(name = "F_ABWERTUNG")
	private Double fAbwertung;

	
	@Column(name = "C_KOMMENTAR")
	private String cKommentar;
	
	public String getCKommentar() {
		return cKommentar;
	}

	public void setCKommentar(String cKommentar) {
		this.cKommentar = cKommentar;
	}

	public BigDecimal getNBasispreis() {
		return nBasispreis;
	}

	public void setNBasispreis(BigDecimal nBasispreis) {
		this.nBasispreis = nBasispreis;
	}

	public Double getFAbwertung() {
		return fAbwertung;
	}

	public void setFAbwertung(Double fAbwertung) {
		this.fAbwertung = fAbwertung;
	}

	private static final long serialVersionUID = 1L;

	public Inventurstand() {
		super();
	}

	public Inventurstand(Integer id, Integer inventurIId2, Integer artikelIId2,
			Integer lagerIId2, BigDecimal inventurmenge,
			BigDecimal inventurpreis, BigDecimal nBasispreis) {
		setIId(id);
		setInventurIId(inventurIId2);
		setArtikelIId(artikelIId2);
		setLagerIId(lagerIId2);
		setNInventurmenge(inventurmenge);
		setNInventurpreis(inventurpreis);
		setNBasispreis(nBasispreis);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public BigDecimal getNInventurmenge() {
		return this.nInventurmenge;
	}

	public void setNInventurmenge(BigDecimal nInventurmenge) {
		this.nInventurmenge = nInventurmenge;
	}

	public BigDecimal getNInventurpreis() {
		return this.nInventurpreis;
	}

	public void setNInventurpreis(BigDecimal nInventurpreis) {
		this.nInventurpreis = nInventurpreis;
	}

	public BigDecimal getNAbgewerteterpreis() {
		return this.nAbgewerteterpreis;
	}

	public void setNAbgewerteterpreis(BigDecimal nAbgewerteterpreis) {
		this.nAbgewerteterpreis = nAbgewerteterpreis;
	}

	public Integer getArtikelIId() {
		return this.artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Integer getInventurIId() {
		return this.inventurIId;
	}

	public void setInventurIId(Integer inventurIId) {
		this.inventurIId = inventurIId;
	}

	public Integer getLagerIId() {
		return this.lagerIId;
	}

	public void setLagerIId(Integer lagerIId) {
		this.lagerIId = lagerIId;
	}

}

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
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "ArtikellagerplaetzefindByArtikelIIdLagerplatzIId", query = "SELECT OBJECT(C) FROM Artikellagerplaetze c WHERE c.artikelIId = ?1 AND c.lagerplatzIId = ?2"),
		@NamedQuery(name = "ArtikellagerplaetzefindByArtikelIId", query = "SELECT OBJECT(C) FROM Artikellagerplaetze c WHERE c.artikelIId = ?1"),
		@NamedQuery(name = "ArtikellagerplaetzefindByArtikelIIdOrderByISort", query = "SELECT OBJECT(C) FROM Artikellagerplaetze c WHERE c.artikelIId = ?1 ORDER BY c.iSort"),
		@NamedQuery(name = "ArtikellagerplaetzeAnzahlDerLagerorteEinesArtikels", query = "SELECT COUNT(o) FROM Artikellagerplaetze o WHERE o.artikelIId = ?1"),
		@NamedQuery(name = "ArtikellagerplaetzeAnzahlVerwendungEinesLagerplatz", query = "SELECT COUNT(o) FROM Artikellagerplaetze o WHERE o.lagerplatzIId = ?1"),
		@NamedQuery(name = "ArtikellagerplaetzefindByLagerplatzIId", query = "SELECT OBJECT(C) FROM Artikellagerplaetze c WHERE c.lagerplatzIId = ?1"),
		@NamedQuery(name = "ArtikellagerplaetzeejbSelectNextReihung", query = "SELECT MAX (o.iSort) FROM Artikellagerplaetze o WHERE o.artikelIId = ?1") })
@Entity
@Table(name = "WW_ARTIKELLAGERPLAETZE")
public class Artikellagerplaetze implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@Column(name = "LAGERPLATZ_I_ID")
	private Integer lagerplatzIId;

	@Column(name = "N_LAGERSTANDPATERNOSTER")
	private BigDecimal nLagerstandPaternoster;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	public Integer getiSort() {
		return iSort;
	}

	public void setiSort(Integer iSort) {
		this.iSort = iSort;
	}

	@Column(name = "I_SORT")
	private Integer iSort;

	private static final long serialVersionUID = 1L;

	public Artikellagerplaetze() {
		super();
	}

	public Artikellagerplaetze(Integer id, Integer artikelIId,
			Integer lagerplatzIId, Integer iSort) {
		setIId(id);
		setArtikelIId(artikelIId);
		setLagerplatzIId(lagerplatzIId);
		setiSort(iSort);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getArtikelIId() {
		return this.artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Integer getLagerplatzIId() {
		return this.lagerplatzIId;
	}

	public void setLagerplatzIId(Integer lagerplatzIId) {
		this.lagerplatzIId = lagerplatzIId;
	}

	public void setNLagerstandPaternoster(BigDecimal nLagerstandPaternoster) {
		this.nLagerstandPaternoster = nLagerstandPaternoster;
	}

	public BigDecimal getNLagerstandPaternoster() {
		return nLagerstandPaternoster;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

}

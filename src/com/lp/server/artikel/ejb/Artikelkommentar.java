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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "ArtikelkommentarfindByArtikelIIdArtikelkommentarartIIdDatenformatCNr", query = "SELECT OBJECT (o) FROM Artikelkommentar o WHERE o.artikelIId=?1 AND o.artikelkommentarartIId=?2 AND o.datenformatCNr=?3"),
		@NamedQuery(name = "ArtikelkommentarfindByArtikelIIdArtikelkommentarartIId", query = "SELECT OBJECT (o) FROM Artikelkommentar o WHERE o.artikelIId=?1 AND o.artikelkommentarartIId=?2"),
		@NamedQuery(name = "ArtikelkommentarfindByArtikelIId", query = "SELECT OBJECT (o) FROM Artikelkommentar o WHERE o.artikelIId=?1"),
		@NamedQuery(name = "ArtikelkommentarfindByArtikelIIdBDefaultbild", query = "SELECT OBJECT (o) FROM Artikelkommentar o WHERE o.artikelIId=?1 AND o.bDefaultbild=?2"),
		@NamedQuery(name = "ArtikelkommentarejbSelectNextReihung", query = "SELECT MAX (o.iSort) FROM Artikelkommentar o WHERE o.artikelIId = ?1"),
		@NamedQuery(name = "ArtikelkommentarfindByArtikelIIdIArt", query = "SELECT OBJECT (o) FROM Artikelkommentar o WHERE o.artikelIId=?1 AND o.iArt=?2") })
@Entity
@Table(name = "WW_ARTIKELKOMMENTAR")
public class Artikelkommentar implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "B_DEFAULTBILD")
	private Short bDefaultbild;

	@Column(name = "I_ART")
	private Integer iArt;

	@Column(name = "I_SORT")
	private Integer iSort;
	
	@Column(name = "DATENFORMAT_C_NR")
	private String datenformatCNr;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@Column(name = "ARTIKELKOMMENTARART_I_ID")
	private Integer artikelkommentarartIId;

	private static final long serialVersionUID = 1L;

	public Artikelkommentar() {
		super();
	}

	public Artikelkommentar(Integer id, 
			Integer artikelIId2,
			Integer artikelkommentarartIId2,
			String datenformatCNr2,
			Short defaultbild,
			Integer iArt, Integer iSort) {
		setIId(id);
		setArtikelIId(artikelIId2);
		setArtikelkommentarartIId(artikelkommentarartIId2);
		setBDefaultbild(defaultbild);
		setDatenformatCNr(datenformatCNr2);
		setIArt(iArt);
		setISort(iSort);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Short getBDefaultbild() {
		return this.bDefaultbild;
	}

	public void setBDefaultbild(Short bDefaultbild) {
		this.bDefaultbild = bDefaultbild;
	}

	public Integer getIArt() {
		return this.iArt;
	}

	public void setIArt(Integer iArt) {
		this.iArt = iArt;
	}

	public String getDatenformatCNr() {
		return this.datenformatCNr;
	}

	public void setDatenformatCNr(String datenformatCNr) {
		this.datenformatCNr = datenformatCNr;
	}

	public Integer getArtikelIId() {
		return this.artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Integer getArtikelkommentarartIId() {
		return this.artikelkommentarartIId;
	}

	public void setArtikelkommentarartIId(Integer artikelkommentarartIId) {
		this.artikelkommentarartIId = artikelkommentarartIId;
	}
	public Integer getISort() {
		return iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

}

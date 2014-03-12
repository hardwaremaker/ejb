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
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "ArtikelsprfindByArtikelIId", query = "SELECT OBJECT(C) FROM Artikelspr c WHERE c.pk.artikelIId = ?1"),
		@NamedQuery(name = "ArtikelsprfindByArtikelIIdLocaleCNr", query = "SELECT OBJECT (o) FROM Artikelspr o WHERE o.pk.artikelIId=?1 AND o.pk.localeCNr=?2"),
		@NamedQuery(name = ArtikelsprQuery.ByChangedDate, query="SELECT OBJECT (o) FROM Artikelspr o WHERE o.tAendern >= :changed") })
@Entity
@Table(name = "WW_ARTIKELSPR")
public class Artikelspr implements Serializable {
	@EmbeddedId
	private ArtikelsprPK pk;

	@Column(name = "C_KBEZ")
	private String cKbez;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "C_ZBEZ")
	private String cZbez;

	@Column(name = "C_ZBEZ2")
	private String cZbez2;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "C_SIWERT")
	private String cSiwert;

	
	public String getCSiwert() {
		return cSiwert;
	}

	public void setCSiwert(String cSiwert) {
		this.cSiwert = cSiwert;
	}


	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;



	private static final long serialVersionUID = 1L;

	public Artikelspr() {
		super();
	}

	public Artikelspr(Integer id, String locUiAsString, Integer personal) {
		super();
		pk = new ArtikelsprPK();
		pk.setArtikelIId(id);
		pk.setLocaleCNr(locUiAsString);
		setPersonalIIdAendern(personal);
	    setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));
	}

	public ArtikelsprPK getPk() {
		return this.pk;
	}

	public void setPk(ArtikelsprPK pk) {
		this.pk = pk;
	}

	public String getCKbez() {
		return this.cKbez;
	}

	public void setCKbez(String cKbez) {
		this.cKbez = cKbez;
	}

	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public String getCZbez() {
		return this.cZbez;
	}

	public void setCZbez(String cZbez) {
		this.cZbez = cZbez;
	}

	public String getCZbez2() {
		return this.cZbez2;
	}

	public void setCZbez2(String cZbez2) {
		this.cZbez2 = cZbez2;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}


	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}


}

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

@NamedQueries( {
		@NamedQuery(name = "InventurlistefindByInventurIIdLagerIIdArtikelIId", query = "SELECT OBJECT (o) FROM Inventurliste o WHERE o.inventurIId=?1 AND o.lagerIId=?2 AND o.artikelIId=?3"),
		@NamedQuery(name = "InventurlistefindByInventurIIdArtikelIId", query = "SELECT OBJECT (o) FROM Inventurliste o WHERE o.inventurIId=?1 AND o.artikelIId=?2"),
		@NamedQuery(name = "InventurlistefindByInventurIIdLagerIIdArtikelIIdCSeriennrchargennr", query = "SELECT OBJECT (o) FROM Inventurliste o WHERE o.inventurIId=?1 AND o.lagerIId=?2 AND o.artikelIId=?3 AND o.cSeriennrchargennr=?4") })
@Entity
@Table(name = "WW_INVENTURLISTE")
public class Inventurliste implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_SERIENNRCHARGENNR")
	private String cSeriennrchargennr;

	@Column(name = "N_INVENTURMENGE")
	private BigDecimal nInventurmenge;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@Column(name = "INVENTUR_I_ID")
	private Integer inventurIId;

	@Column(name = "LAGER_I_ID")
	private Integer lagerIId;

	private static final long serialVersionUID = 1L;

	public Inventurliste() {
		super();
	}

	public Inventurliste(Integer id, Integer inventurIId, Integer lagerIId,
			Integer artikelIId, BigDecimal inventurmenge,
			Integer personalIIdAendern2) {
		setIId(id);
		setInventurIId(inventurIId);
		setLagerIId(lagerIId);
		setArtikelIId(artikelIId);
		setNInventurmenge(inventurmenge);
		setPersonalIIdAendern(personalIIdAendern2);
		setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCSeriennrchargennr() {
		return this.cSeriennrchargennr;
	}

	public void setCSeriennrchargennr(String cSeriennrchargennr) {
		this.cSeriennrchargennr = cSeriennrchargennr;
	}

	public BigDecimal getNInventurmenge() {
		return this.nInventurmenge;
	}

	public void setNInventurmenge(BigDecimal nInventurmenge) {
		this.nInventurmenge = nInventurmenge;
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

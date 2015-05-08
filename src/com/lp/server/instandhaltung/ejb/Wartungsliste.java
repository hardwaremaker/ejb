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
package com.lp.server.instandhaltung.ejb;

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
		@NamedQuery(name = "WartungslisteejbSelectNextReihung", query = "SELECT MAX (o.iSort) FROM Wartungsliste o WHERE o.geraetIId = ?1"),
		@NamedQuery(name = "WartungslistefindByGeraetIId", query = "SELECT OBJECT(o) FROM Wartungsliste o WHERE o.geraetIId=?1 ORDER BY o.iSort") })
@Entity
@Table(name = "IS_WARTUNGSLISTE")
public class Wartungsliste implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "N_MENGE")
	private BigDecimal nMenge;

	@Column(name = "B_WARTUNGSMATERIAL")
	private Short bWartungsmaterial;

	@Column(name = "B_VERRECHENBAR")
	private Short bVerrechenbar;

	@Column(name = "C_VERALTET")
	private String cVeraltet;

	@Column(name = "X_BEMERKUNG")
	private String xBemerkung;

	@Column(name = "I_SORT")
	private Integer iSort;

	@Column(name = "T_PERSONAL_VERALTET")
	private Timestamp tPersonalVeraltet;

	@Column(name = "T_VERALTET")
	private Timestamp tVeraltet;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "PERSONAL_I_ID_VERALTET")
	private Integer personalIIdVeraltet;

	@Column(name = "GERAET_I_ID")
	private Integer geraetIId;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	private static final long serialVersionUID = 1L;

	public Wartungsliste() {
		super();
	}

	public Wartungsliste(Integer id, Integer geraetIId, BigDecimal menge,
			Short bWartungsmaterial, Short bVerrechenbar, Integer sort) {
		setIId(id);
		setGeraetIId(geraetIId);

		setNMenge(menge);
		setBWartungsmaterial(bWartungsmaterial);
		setBVerrechenbar(bVerrechenbar);
		setISort(sort);
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

	public Integer getISort() {
		return this.iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public Short getBWartungsmaterial() {
		return bWartungsmaterial;
	}

	public void setBWartungsmaterial(Short bWartungsmaterial) {
		this.bWartungsmaterial = bWartungsmaterial;
	}

	public Short getBVerrechenbar() {
		return bVerrechenbar;
	}

	public void setBVerrechenbar(Short bVerrechenbar) {
		this.bVerrechenbar = bVerrechenbar;
	}

	public String getCVeraltet() {
		return cVeraltet;
	}

	public void setCVeraltet(String cVeraltet) {
		this.cVeraltet = cVeraltet;
	}

	public String getXBemerkung() {
		return xBemerkung;
	}

	public void setXBemerkung(String xBemerkung) {
		this.xBemerkung = xBemerkung;
	}

	public Timestamp getTPersonalVeraltet() {
		return tPersonalVeraltet;
	}

	public void setTPersonalVeraltet(Timestamp tPersonalVeraltet) {
		this.tPersonalVeraltet = tPersonalVeraltet;
	}

	public Timestamp getTVeraltet() {
		return tVeraltet;
	}

	public void settVeraltet(Timestamp tVeraltet) {
		this.tVeraltet = tVeraltet;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public Integer getPersonalIIdVeraltet() {
		return personalIIdVeraltet;
	}

	public void setPersonalIIdVeraltet(Integer personalIIdVeraltet) {
		this.personalIIdVeraltet = personalIIdVeraltet;
	}

	public Integer getGeraetIId() {
		return this.geraetIId;
	}

	public void setGeraetIId(Integer geraetIId) {
		this.geraetIId = geraetIId;
	}

	public Integer getArtikelIId() {
		return this.artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

}

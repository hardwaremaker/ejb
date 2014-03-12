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
		@NamedQuery(name = "ArtikellieferantstaffelfindByArtikellieferantIIdFMenge", query = "SELECT OBJECT(o) FROM Artikellieferantstaffel o WHERE o.artikellieferantIId = ?1 AND o.nMenge <= ?2 AND o.tPreisgueltigab <= ?3 AND (o.tPreisgueltigbis >= ?3  OR o.tPreisgueltigbis IS NULL) ORDER BY o.nMenge DESC"),
		@NamedQuery(name = "ArtikellieferantstaffelfindbyArtikellieferantIIdFMengeUK", query = "SELECT OBJECT(o) FROM Artikellieferantstaffel o WHERE o.artikellieferantIId = ?1 AND o.nMenge=?2"),
		@NamedQuery(name = "ArtikellieferantstaffelfindByArtikellieferantIId", query = "SELECT OBJECT(o) FROM Artikellieferantstaffel o WHERE o.artikellieferantIId = ?1 ORDER BY o.nMenge DESC"),
		@NamedQuery(name = "ArtikellieferantstaffelfindByArtikellieferantIIdFMengeTPreisgueltigab", query = "SELECT OBJECT(o) FROM Artikellieferantstaffel o WHERE o.artikellieferantIId = ?1 AND o.nMenge=?2 AND o.tPreisgueltigab=?3"),
		@NamedQuery(name = "ArtikellieferantstaffelfindByArtikellieferantIIdFMengeTPreisgueltigabKleiner", query = "SELECT OBJECT(o) FROM Artikellieferantstaffel o WHERE o.artikellieferantIId = ?1 AND o.nMenge=?2 AND o.tPreisgueltigab<=?3 AND (o.tPreisgueltigbis >= ?3  OR o.tPreisgueltigbis IS NULL) ORDER BY o.tPreisgueltigab DESC") })
@Entity
@Table(name = "WW_ARTIKELLIEFERANTSTAFFEL")
public class Artikellieferantstaffel implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "N_MENGE")
	private BigDecimal nMenge;

	@Column(name = "I_WIEDERBESCHAFFUNGSZEIT")
	private Integer iWiederbeschaffungszeit;

	@Column(name = "F_RABATT")
	private Double fRabatt;

	@Column(name = "N_NETTOPREIS")
	private BigDecimal nNettopreis;

	@Column(name = "T_PREISGUELTIGAB")
	private Timestamp tPreisgueltigab;

	@Column(name = "T_PREISGUELTIGBIS")
	private Timestamp tPreisgueltigbis;

	@Column(name = "ARTIKELLIEFERANT_I_ID")
	private Integer artikellieferantIId;

	@Column(name = "B_RABATTBEHALTEN")
	private Short bRabattbehalten;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp aendern) {
		tAendern = aendern;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Short getBRabattbehalten() {
		return this.bRabattbehalten;
	}

	public void setBRabattbehalten(Short bRabattbehalten) {
		this.bRabattbehalten = bRabattbehalten;
	}

	private static final long serialVersionUID = 1L;

	public Artikellieferantstaffel() {
		super();
	}

	public Artikellieferantstaffel(Integer id, Integer artikellieferantIId,
			BigDecimal menge, Double rabatt, BigDecimal nettopreis,
			Timestamp preisgueltigab, Short rabattbehalten,
			Integer personalIIdAendern, Timestamp tAendern) {
		setIId(id);
		setArtikellieferantIId(artikellieferantIId);
		setNMenge(menge);
		setFRabatt(rabatt);
		setNNettopreis(nettopreis);
		setTPreisgueltigab(preisgueltigab);
		setBRabattbehalten(rabattbehalten);
		setPersonalIIdAendern(personalIIdAendern);
		setTAendern(tAendern);
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

	public Double getFRabatt() {
		return this.fRabatt;
	}

	public void setFRabatt(Double fRabatt) {
		this.fRabatt = fRabatt;
	}

	public BigDecimal getNNettopreis() {
		return this.nNettopreis;
	}

	public void setNNettopreis(BigDecimal nNettopreis) {
		this.nNettopreis = nNettopreis;
	}

	public Integer getIWiederbeschaffungszeit() {
		return this.iWiederbeschaffungszeit;
	}

	public void setIWiederbeschaffungszeit(Integer iWiederbeschaffungszeit) {
		this.iWiederbeschaffungszeit = iWiederbeschaffungszeit;
	}

	public Timestamp getTPreisgueltigab() {
		return this.tPreisgueltigab;
	}

	public void setTPreisgueltigab(Timestamp tPreisgueltigab) {
		this.tPreisgueltigab = tPreisgueltigab;
	}

	public Timestamp getTPreisgueltigbis() {
		return this.tPreisgueltigbis;
	}

	public void setTPreisgueltigbis(Timestamp tPreisgueltigbis) {
		this.tPreisgueltigbis = tPreisgueltigbis;
	}

	public Integer getArtikellieferantIId() {
		return this.artikellieferantIId;
	}

	public void setArtikellieferantIId(Integer artikellieferantIId) {
		this.artikellieferantIId = artikellieferantIId;
	}

}

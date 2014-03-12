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
package com.lp.server.bestellung.ejb;

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
		@NamedQuery(name = "WareneingangfindByWareneingang", query = "SELECT OBJECT (o) FROM Wareneingang o WHERE o.bestellungIId=?1 ORDER BY o.iSort"),
		@NamedQuery(name = "WareneingangfindByBestellungIId", query = "SELECT OBJECT (o) FROM Wareneingang o WHERE o.bestellungIId=?1"),
		@NamedQuery(name = "WareneingangfindByBestellungIIdTWareneingangsdatum", query = "SELECT OBJECT (o) FROM Wareneingang o WHERE o.bestellungIId=?1 AND o.tWareneingangsdatum=?2"),
		@NamedQuery(name = "WareneingangfindByEingangsrechnungIId", query = "SELECT OBJECT (o) FROM Wareneingang o WHERE o.eingangsrechnungIId=?1"),
		@NamedQuery(name = "WareneingangejbSelectMaxISort", query = "SELECT MAX (o.iSort) FROM Wareneingang o WHERE o.bestellungIId = ?1") })
@Entity
@Table(name = "BES_WARENEINGANG")
public class Wareneingang implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "I_SORT")
	private Integer iSort;

	@Column(name = "C_LIEFERSCHEINNR")
	private String cLieferscheinnr;

	@Column(name = "T_LIEFERSCHEINDATUM")
	private Timestamp tLieferscheindatum;

	@Column(name = "N_TRANSPORTKOSTEN")
	private BigDecimal nTransportkosten;

	@Column(name = "N_ZOLLKOSTEN")
	private BigDecimal nZollkosten;
	public BigDecimal getNZollkosten() {
		return nZollkosten;
	}

	public void setNZollkosten(BigDecimal nZollkosten) {
		this.nZollkosten = nZollkosten;
	}

	public BigDecimal getNBankspesen() {
		return nBankspesen;
	}

	public void setNBankspesen(BigDecimal nBankspesen) {
		this.nBankspesen = nBankspesen;
	}

	public BigDecimal getNSonstigespesen() {
		return nSonstigespesen;
	}

	public void setNSonstigespesen(BigDecimal nSonstigespesen) {
		this.nSonstigespesen = nSonstigespesen;
	}

	@Column(name = "N_BANKSPESEN")
	private BigDecimal nBankspesen;
	@Column(name = "N_SONSTIGESPESEN")
	private BigDecimal nSonstigespesen;

	
	
	@Column(name = "F_GEMEINKOSTENFAKTOR")
	private Double fGemeinkostenfaktor;

	@Column(name = "F_RABATTSATZ")
	private Double fRabattsatz;

	@Column(name = "T_WARENEINGANGSDATUM")
	private Timestamp tWareneingangsdatum;

	@Column(name = "N_WECHSELKURS")
	private BigDecimal nWechselkurs;

	@Column(name = "BESTELLUNG_I_ID")
	private Integer bestellungIId;

	@Column(name = "EINGANGSRECHNUNG_I_ID")
	private Integer eingangsrechnungIId;

	@Column(name = "LAGER_I_ID")
	private Integer lagerIId;

	private static final long serialVersionUID = 1L;

	public Wareneingang() {
		super();
	}

	public Wareneingang(Integer id, Integer sort, String lieferscheinnr,
			Timestamp lieferscheindatum, BigDecimal transportkosten,
			Double gemeinkostenfaktor, Timestamp wareneingangsdatum,
			Integer bestellungIId, Integer lagerIId, BigDecimal wechselkurs) {
		setIId(id);
	    setISort(sort);
	    setCLieferscheinnr(lieferscheinnr);
	    setTLieferscheindatum(lieferscheindatum);
	    setNTransportkosten(transportkosten);
	    setBestellungIId(bestellungIId);
	    setTWareneingangsdatum(wareneingangsdatum);
	    setLagerIId(lagerIId);
	    setNWechselkurs(wechselkurs);
	    setFGemeinkostenfaktor(gemeinkostenfaktor);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getISort() {
		return this.iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public String getCLieferscheinnr() {
		return this.cLieferscheinnr;
	}

	public void setCLieferscheinnr(String cLieferscheinnr) {
		this.cLieferscheinnr = cLieferscheinnr;
	}

	public Timestamp getTLieferscheindatum() {
		return this.tLieferscheindatum;
	}

	public void setTLieferscheindatum(Timestamp tLieferscheindatum) {
		this.tLieferscheindatum = tLieferscheindatum;
	}

	public BigDecimal getNTransportkosten() {
		return this.nTransportkosten;
	}

	public void setNTransportkosten(BigDecimal nTransportkosten) {
		this.nTransportkosten = nTransportkosten;
	}

	public Double getFGemeinkostenfaktor() {
		return this.fGemeinkostenfaktor;
	}

	public void setFGemeinkostenfaktor(Double fGemeinkostenfaktor) {
		this.fGemeinkostenfaktor = fGemeinkostenfaktor;
	}

	public Double getFRabattsatz() {
		return this.fRabattsatz;
	}

	public void setFRabattsatz(Double fRabattsatz) {
		this.fRabattsatz = fRabattsatz;
	}

	public Timestamp getTWareneingangsdatum() {
		return this.tWareneingangsdatum;
	}

	public void setTWareneingangsdatum(Timestamp tWareneingangsdatum) {
		this.tWareneingangsdatum = tWareneingangsdatum;
	}

	public BigDecimal getNWechselkurs() {
		return this.nWechselkurs;
	}

	public void setNWechselkurs(BigDecimal nWechselkurs) {
		this.nWechselkurs = nWechselkurs;
	}

	public Integer getBestellungIId() {
		return this.bestellungIId;
	}

	public void setBestellungIId(Integer bestellungIId) {
		this.bestellungIId = bestellungIId;
	}

	public Integer getEingangsrechnungIId() {
		return this.eingangsrechnungIId;
	}

	public void setEingangsrechnungIId(Integer eingangsrechnungIId) {
		this.eingangsrechnungIId = eingangsrechnungIId;
	}

	public Integer getLagerIId() {
		return this.lagerIId;
	}

	public void setLagerIId(Integer lagerIId) {
		this.lagerIId = lagerIId;
	}

}

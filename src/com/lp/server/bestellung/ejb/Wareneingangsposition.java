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

import com.lp.util.Helper;

@NamedQueries( {
		@NamedQuery(name = "WareneingangspositionfindByBestellpositionIId", query = "SELECT OBJECT (o) FROM Wareneingangsposition o WHERE o.bestellpositionIId=?1"),
		@NamedQuery(name = "WareneingangspositionfindByWareneingangIId", query = "SELECT OBJECT (o) FROM Wareneingangsposition o WHERE o.wareneingangIId=?1"),
		@NamedQuery(name = "WareneingangspositionfindByWareneingangIIdAndBestellpositionIId", query = "SELECT OBJECT (o) FROM Wareneingangsposition o WHERE o.wareneingangIId=?1 AND o.bestellpositionIId=?2") })
@Entity
@Table(name = "BES_WARENEINGANGSPOSITION")
public class Wareneingangsposition implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "N_GELIEFERTEMENGE")
	private BigDecimal nGeliefertemenge;

	@Column(name = "N_GELIEFERTERPREIS")
	private BigDecimal nGelieferterpreis;

	@Column(name = "N_RABATTWERT")
	private BigDecimal nRabattwert;

	@Column(name = "N_ANTEILIGETRANSPORTKOSTEN")
	private BigDecimal nAnteiligetransportkosten;

	@Column(name = "N_EINSTANDSPREIS")
	private BigDecimal nEinstandspreis;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "X_EXTERNERKOMMENTAR")
	private String xExternerkommentar;

	@Column(name = "X_INTERNERKOMMENTAR")
	private String xInternerkommentar;

	@Column(name = "T_MANUELLERLEDIGT")
	private Timestamp tManuellerledigt;

	@Column(name = "N_ANTEILIGEFIXKOSTEN")
	private BigDecimal nAnteiligefixkosten;

	@Column(name = "B_PREISEERFASST")
	private Short bPreiseerfasst;

	@Column(name = "B_VERRAEUMT")
	private Short bVerraeumt;

	
	public Short getBVerraeumt() {
		return bVerraeumt;
	}

	public void setBVerraeumt(Short bVerraeumt) {
		this.bVerraeumt = bVerraeumt;
	}

	@Column(name = "BESTELLPOSITION_I_ID")
	private Integer bestellpositionIId;

	@Column(name = "WARENEINGANG_I_ID")
	private Integer wareneingangIId;

	@Column(name = "EINGANGSRECHNUNG_I_ID_UEBERSTEUERT")
	private Integer eingangsrechnungIIdUebersteuert;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	private static final long serialVersionUID = 1L;

	public Wareneingangsposition() {
		super();
	}

	public Wareneingangsposition(Integer id, BigDecimal geliefertemenge,
			BigDecimal gelieferterpreis, BigDecimal einstandspreis,
			Integer bestellpositionIId, Integer personalIIdAendern,
			Integer wareneingangIId, Integer personalIIdAnlegen) {
		setIId(id);
		setNGeliefertemenge(geliefertemenge);
		setNGelieferterpreis(gelieferterpreis);
		setNEinstandspreis(einstandspreis);
		setBestellpositionIId(bestellpositionIId);
		setPersonalIIdAendern(personalIIdAendern);
		setWareneingangIId(wareneingangIId);
		setPersonalIIdAnlegen(personalIIdAnlegen);
	    //die ts anlegen, aendern nur am server
	    setTAnlegen(new Timestamp(System.currentTimeMillis()));
	    setTAendern(new Timestamp(System.currentTimeMillis()));
	    setBPreiseerfasst(Helper.boolean2Short(false));
	    setBVerraeumt(Helper.boolean2Short(false));
	}
	
	public Wareneingangsposition(Integer id,
			Integer wareneingangIId, 
			Integer bestellpositionIId,		
			Integer personalIIdAendern,
			Integer personalIIdAnlegen,
			Short preiseerfasst,Short verraeumt) {
		setIId(id);
		setBestellpositionIId(bestellpositionIId);
		setPersonalIIdAendern(personalIIdAendern);
		setWareneingangIId(wareneingangIId);
		setPersonalIIdAnlegen(personalIIdAnlegen);
	    //die ts anlegen, aendern nur am server
	    setTAnlegen(new Timestamp(System.currentTimeMillis()));
	    setTAendern(new Timestamp(System.currentTimeMillis()));
	    setBPreiseerfasst(preiseerfasst);
	    setBVerraeumt(verraeumt);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public BigDecimal getNGeliefertemenge() {
		return this.nGeliefertemenge;
	}

	public void setNGeliefertemenge(BigDecimal nGeliefertemenge) {
		this.nGeliefertemenge = nGeliefertemenge;
	}

	public BigDecimal getNGelieferterpreis() {
		return this.nGelieferterpreis;
	}

	public void setNGelieferterpreis(BigDecimal nGelieferterpreis) {
		this.nGelieferterpreis = nGelieferterpreis;
	}

	public BigDecimal getNRabattwert() {
		return this.nRabattwert;
	}

	public void setNRabattwert(BigDecimal nRabattwert) {
		this.nRabattwert = nRabattwert;
	}

	public BigDecimal getNAnteiligetransportkosten() {
		return this.nAnteiligetransportkosten;
	}

	public void setNAnteiligetransportkosten(
			BigDecimal nAnteiligetransportkosten) {
		this.nAnteiligetransportkosten = nAnteiligetransportkosten;
	}

	public BigDecimal getNEinstandspreis() {
		return this.nEinstandspreis;
	}

	public void setNEinstandspreis(BigDecimal nEinstandspreis) {
		this.nEinstandspreis = nEinstandspreis;
	}

	public Timestamp getTAnlegen() {
		return this.tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public String getXExternerkommentar() {
		return this.xExternerkommentar;
	}

	public void setXExternerkommentar(String xExternerkommentar) {
		this.xExternerkommentar = xExternerkommentar;
	}

	public String getXInternerkommentar() {
		return this.xInternerkommentar;
	}

	public void setXInternerkommentar(String xInternerkommentar) {
		this.xInternerkommentar = xInternerkommentar;
	}

	public Timestamp getTManuellerledigt() {
		return this.tManuellerledigt;
	}

	public void setTManuellerledigt(Timestamp tManuellerledigt) {
		this.tManuellerledigt = tManuellerledigt;
	}

	public BigDecimal getNAnteiligefixkosten() {
		return this.nAnteiligefixkosten;
	}

	public void setNAnteiligefixkosten(BigDecimal nAnteiligefixkosten) {
		this.nAnteiligefixkosten = nAnteiligefixkosten;
	}

	public Short getBPreiseerfasst() {
		return this.bPreiseerfasst;
	}

	public void setBPreiseerfasst(Short bPreiseerfasst) {
		this.bPreiseerfasst = bPreiseerfasst;
	}

	public Integer getBestellpositionIId() {
		return this.bestellpositionIId;
	}

	public void setBestellpositionIId(Integer bestellpositionIId) {
		this.bestellpositionIId = bestellpositionIId;
	}

	public Integer getWareneingangIId() {
		return this.wareneingangIId;
	}

	public void setWareneingangIId(Integer wareneingangIId) {
		this.wareneingangIId = wareneingangIId;
	}

	public Integer getEingangsrechnungIIdUebersteuert() {
		return this.eingangsrechnungIIdUebersteuert;
	}

	public void setEingangsrechnungIIdUebersteuert(
			Integer eingangsrechnungIIdUebersteuert) {
		this.eingangsrechnungIIdUebersteuert = eingangsrechnungIIdUebersteuert;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

}

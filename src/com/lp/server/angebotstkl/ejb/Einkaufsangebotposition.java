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
package com.lp.server.angebotstkl.ejb;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "EinkaufsangebotpositionejbSelectMaxISort", query = "SELECT MAX (o.iSort) FROM Einkaufsangebotposition AS o WHERE o.einkaufsangebotIId=?1"),
		@NamedQuery(name = "EinkaufsangebotpositionfindByEinkaufsangebotIId", query = "SELECT OBJECT (o) FROM Einkaufsangebotposition o WHERE o.einkaufsangebotIId=?1 ORDER BY o.iSort") })
@Entity
@Table(name = "AS_EINKAUFSANGEBOTPOSITION")
public class Einkaufsangebotposition implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "I_SORT")
	private Integer iSort;

	@Column(name = "B_MITDRUCKEN")
	private Short bMitdrucken;

	public Short getBMitdrucken() {
		return bMitdrucken;
	}

	public void setBMitdrucken(Short mitdrucken) {
		bMitdrucken = mitdrucken;
	}

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "C_ZBEZ")
	private String cZbez;

	@Column(name = "B_ARTIKELBEZEICHNUNGUEBERSTEUERT")
	private Short bArtikelbezeichnunguebersteuert;

	@Column(name = "I_WIEDERBESCHAFFUNGSZEIT")
	private Integer iWiederbeschaffungszeit;

	@Column(name = "I_VERPACKUNGSEINHEIT")
	private Integer iVerpackungseinheit;

	@Column(name = "F_MINDESTBESTELLMENGE")
	private Double fMindestbestellmenge;

	@Column(name = "N_MENGE")
	private BigDecimal nMenge;

	@Column(name = "N_PREIS1")
	private BigDecimal nPreis1;

	@Column(name = "N_PREIS2")
	private BigDecimal nPreis2;

	@Column(name = "N_PREIS3")
	private BigDecimal nPreis3;

	@Column(name = "N_PREIS4")
	private BigDecimal nPreis4;

	@Column(name = "N_PREIS5")
	private BigDecimal nPreis5;

	@Column(name = "C_BEMERKUNG")
	private String cBemerkung;

	@Column(name = "AGSTKLPOSITIONSART_C_NR")
	private String agstklpositionsartCNr;

	@Column(name = "EINKAUFSANGEBOT_I_ID")
	private Integer einkaufsangebotIId;

	@Column(name = "EINHEIT_C_NR")
	private String einheitCNr;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@Column(name = "C_POSITION")
	private String cPosition;

	@Column(name = "C_INTERNEBEMERKUNG")
	private String cInternebemerkung;
	
	@Column(name = "C_KOMMENTAR1")
	private String cKommentar1;

	@Column(name = "C_KOMMENTAR2")
	private String cKommentar2;

	public String getCKommentar1() {
		return cKommentar1;
	}

	public void setCKommentar1(String kommentar1) {
		cKommentar1 = kommentar1;
	}

	public String getCKommentar2() {
		return cKommentar2;
	}

	public void setCKommentar2(String kommentar2) {
		cKommentar2 = kommentar2;
	}

	public String getCInternebemerkung() {
		return cInternebemerkung;
	}

	public void setCInternebemerkung(String internebemerkung) {
		cInternebemerkung = internebemerkung;
	}

	public String getCPosition() {
		return cPosition;
	}

	public void setCPosition(String position) {
		cPosition = position;
	}

	private static final long serialVersionUID = 1L;

	public Einkaufsangebotposition() {
		super();
	}

	public Einkaufsangebotposition(Integer id, Integer belegIId,
			String agstklpositionsartCNr, Short artikelbezeichnunguebersteuert,
			Short bMitdrucken) {
		setIId(id);
		setEinkaufsangebotIId(belegIId);
		setAgstklpositionsartCNr(agstklpositionsartCNr);
		setBArtikelbezeichnunguebersteuert(artikelbezeichnunguebersteuert);
		setBMitdrucken(bMitdrucken);
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

	public Short getBArtikelbezeichnunguebersteuert() {
		return this.bArtikelbezeichnunguebersteuert;
	}

	public void setBArtikelbezeichnunguebersteuert(
			Short bArtikelbezeichnunguebersteuert) {
		this.bArtikelbezeichnunguebersteuert = bArtikelbezeichnunguebersteuert;
	}

	public Integer getIWiederbeschaffungszeit() {
		return this.iWiederbeschaffungszeit;
	}

	public void setIWiederbeschaffungszeit(Integer iWiederbeschaffungszeit) {
		this.iWiederbeschaffungszeit = iWiederbeschaffungszeit;
	}

	public Integer getIVerpackungseinheit() {
		return this.iVerpackungseinheit;
	}

	public void setIVerpackungseinheit(Integer iVerpackungseinheit) {
		this.iVerpackungseinheit = iVerpackungseinheit;
	}

	public Double getFMindestbestellmenge() {
		return this.fMindestbestellmenge;
	}

	public void setFMindestbestellmenge(Double fMindestbestellmenge) {
		this.fMindestbestellmenge = fMindestbestellmenge;
	}

	public BigDecimal getNMenge() {
		return this.nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public BigDecimal getNPreis1() {
		return this.nPreis1;
	}

	public void setNPreis1(BigDecimal nPreis1) {
		this.nPreis1 = nPreis1;
	}

	public BigDecimal getNPreis2() {
		return this.nPreis2;
	}

	public void setNPreis2(BigDecimal nPreis2) {
		this.nPreis2 = nPreis2;
	}

	public BigDecimal getNPreis3() {
		return this.nPreis3;
	}

	public void setNPreis3(BigDecimal nPreis3) {
		this.nPreis3 = nPreis3;
	}

	public BigDecimal getNPreis4() {
		return this.nPreis4;
	}

	public void setNPreis4(BigDecimal nPreis4) {
		this.nPreis4 = nPreis4;
	}

	public BigDecimal getNPreis5() {
		return this.nPreis5;
	}

	public void setNPreis5(BigDecimal nPreis5) {
		this.nPreis5 = nPreis5;
	}

	public String getCBemerkung() {
		return this.cBemerkung;
	}

	public void setCBemerkung(String cBemerkung) {
		this.cBemerkung = cBemerkung;
	}

	public String getAgstklpositionsartCNr() {
		return this.agstklpositionsartCNr;
	}

	public void setAgstklpositionsartCNr(String agstklpositionsartCNr) {
		this.agstklpositionsartCNr = agstklpositionsartCNr;
	}

	public Integer getEinkaufsangebotIId() {
		return this.einkaufsangebotIId;
	}

	public void setEinkaufsangebotIId(Integer einkaufsangebot) {
		this.einkaufsangebotIId = einkaufsangebot;
	}

	public String getEinheitCNr() {
		return this.einheitCNr;
	}

	public void setEinheitCNr(String einheitCNr) {
		this.einheitCNr = einheitCNr;
	}

	public Integer getArtikelIId() {
		return this.artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

}

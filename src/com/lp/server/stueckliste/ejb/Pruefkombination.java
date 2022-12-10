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
package com.lp.server.stueckliste.ejb;

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
		@NamedQuery(name = "PruefkombinationFindByPruefartIIdArtikelIIdKontaktArtikelIIdLitzeVerschleissteilIId", query = "SELECT OBJECT(o) FROM Pruefkombination o WHERE o.pruefartIId = ?1 AND o.artikelIIdKontakt = ?2 AND  o.artikelIIdLitze = ?3 AND  o.verschleissteilIId = ?4 "),
		@NamedQuery(name = "PruefkombinationFindByArtikelIIdKontaktArtikelIIdLitze", query = "SELECT OBJECT(o) FROM Pruefkombination o WHERE o.artikelIIdKontakt = ?1 AND  o.artikelIIdLitze = ?2") })
@Entity
@Table(name = "STK_PRUEFKOMBINATION")
public class Pruefkombination implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	public Integer getArtikelIIdLitze2() {
		return artikelIIdLitze2;
	}

	public void setArtikelIIdLitze2(Integer artikelIIdLitze2) {
		this.artikelIIdLitze2 = artikelIIdLitze2;
	}

	public Integer getVerschleissteilIId() {
		return verschleissteilIId;
	}

	public void setVerschleissteilIId(Integer verschleissteilIId) {
		this.verschleissteilIId = verschleissteilIId;
	}

	public Short getBDoppelanschlag() {
		return bDoppelanschlag;
	}

	public void setBDoppelanschlag(Short bDoppelanschlag) {
		this.bDoppelanschlag = bDoppelanschlag;
	}

	public BigDecimal getNAbzugskraftLitze() {
		return nAbzugskraftLitze;
	}

	public void setNAbzugskraftLitze(BigDecimal nAbzugskraftLitze) {
		this.nAbzugskraftLitze = nAbzugskraftLitze;
	}

	public BigDecimal getNAbzugskraftLitze2() {
		return nAbzugskraftLitze2;
	}

	public void setNAbzugskraftLitze2(BigDecimal nAbzugskraftLitze2) {
		this.nAbzugskraftLitze2 = nAbzugskraftLitze2;
	}

	@Column(name = "PRUEFART_I_ID")
	private Integer pruefartIId;

	public Integer getPruefartIId() {
		return pruefartIId;
	}

	public void setPruefartIId(Integer pruefartIId) {
		this.pruefartIId = pruefartIId;
	}

	@Column(name = "ARTIKEL_I_ID_KONTAKT")
	private Integer artikelIIdKontakt;

	@Column(name = "ARTIKEL_I_ID_LITZE")
	private Integer artikelIIdLitze;

	@Column(name = "ARTIKEL_I_ID_LITZE2")
	private Integer artikelIIdLitze2;

	public Integer getArtikelIIdKontakt() {
		return artikelIIdKontakt;
	}

	public void setArtikelIIdKontakt(Integer artikelIIdKontakt) {
		this.artikelIIdKontakt = artikelIIdKontakt;
	}

	public Integer getArtikelIIdLitze() {
		return artikelIIdLitze;
	}

	public void setArtikelIIdLitze(Integer artikelIIdLitze) {
		this.artikelIIdLitze = artikelIIdLitze;
	}


	public Short getBStandard() {
		return bStandard;
	}

	public void setBStandard(Short bStandard) {
		this.bStandard = bStandard;
	}

	public BigDecimal getNCrimphoeheDraht() {
		return nCrimphoeheDraht;
	}

	public void setNCrimphoeheDraht(BigDecimal nCrimphoeheDraht) {
		this.nCrimphoeheDraht = nCrimphoeheDraht;
	}

	public BigDecimal getNCrimphoeheIsolation() {
		return nCrimphoeheIsolation;
	}

	public void setNCrimphoeheIsolation(BigDecimal nCrimphoeheIsolation) {
		this.nCrimphoeheIsolation = nCrimphoeheIsolation;
	}

	public BigDecimal getNCrimpbreitDraht() {
		return nCrimpbreitDraht;
	}

	public void setNCrimpbreitDraht(BigDecimal nCrimpbreitDraht) {
		this.nCrimpbreitDraht = nCrimpbreitDraht;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	@Column(name = "VERSCHLEISSTEIL_I_ID")
	private Integer verschleissteilIId;

	@Column(name = "B_STANDARD")
	private Short bStandard;

	@Column(name = "B_DOPPELANSCHLAG")
	private Short bDoppelanschlag;
	@Column(name = "N_ABZUGSKRAFT_LITZE")
	private BigDecimal nAbzugskraftLitze;
	@Column(name = "N_ABZUGSKRAFT_LITZE2")
	private BigDecimal nAbzugskraftLitze2;
	

	@Column(name = "N_CRIMPHOEHE_DRAHT")
	private BigDecimal nCrimphoeheDraht;

	@Column(name = "N_CRIMPHOEHE_ISOLATION")
	private BigDecimal nCrimphoeheIsolation;

	@Column(name = "N_CRIMPBREITE_DRAHT")
	private BigDecimal nCrimpbreitDraht;

	@Column(name = "N_CRIMPBREITE_ISOLATION")
	private BigDecimal nCrimpbreiteIsolation;

	@Column(name = "N_TOLERANZ_CRIMPHOEHE_DRAHT")
	private BigDecimal nToleranzCrimphoeheDraht;

	@Column(name = "N_TOLERANZ_CRIMPHOEHE_ISOLATION")
	private BigDecimal nToleranzCrimphoeheIsolation;

	@Column(name = "N_TOLERANZ_CRIMPBREITE_DRAHT")
	private BigDecimal nToleranzCrimpbreitDraht;

	@Column(name = "N_TOLERANZ_CRIMPBREITE_ISOLATION")
	private BigDecimal nToleranzCrimpbreiteIsolation;

	@Column(name = "N_WERT")
	private BigDecimal nWert;
	@Column(name = "N_TOLERANZ_WERT")
	private BigDecimal nToleranzWert;

	public BigDecimal getNToleranzCrimphoeheDraht() {
		return nToleranzCrimphoeheDraht;
	}

	public void setNToleranzCrimphoeheDraht(BigDecimal nToleranzCrimphoeheDraht) {
		this.nToleranzCrimphoeheDraht = nToleranzCrimphoeheDraht;
	}

	public BigDecimal getNToleranzCrimphoeheIsolation() {
		return nToleranzCrimphoeheIsolation;
	}

	public void setNToleranzCrimphoeheIsolation(
			BigDecimal nToleranzCrimphoeheIsolation) {
		this.nToleranzCrimphoeheIsolation = nToleranzCrimphoeheIsolation;
	}

	public BigDecimal getNToleranzCrimpbreitDraht() {
		return nToleranzCrimpbreitDraht;
	}

	public void setNToleranzCrimpbreitDraht(BigDecimal nToleranzCrimpbreitDraht) {
		this.nToleranzCrimpbreitDraht = nToleranzCrimpbreitDraht;
	}

	public BigDecimal getNToleranzCrimpbreiteIsolation() {
		return nToleranzCrimpbreiteIsolation;
	}

	public void setNToleranzCrimpbreiteIsolation(
			BigDecimal nToleranzCrimpbreiteIsolation) {
		this.nToleranzCrimpbreiteIsolation = nToleranzCrimpbreiteIsolation;
	}

	public BigDecimal getNWert() {
		return nWert;
	}

	public void setNWert(BigDecimal nWert) {
		this.nWert = nWert;
	}

	public BigDecimal getNToleranzWert() {
		return nToleranzWert;
	}

	public void setNToleranzWert(BigDecimal nToleranzWert) {
		this.nToleranzWert = nToleranzWert;
	}

	public BigDecimal getNCrimpbreiteIsolation() {
		return nCrimpbreiteIsolation;
	}

	public void setNCrimpbreiteIsolation(BigDecimal nCrimpbreiteIsolation) {
		this.nCrimpbreiteIsolation = nCrimpbreiteIsolation;
	}

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	private static final long serialVersionUID = 1L;

	public Pruefkombination() {
		super();
	}

	public Pruefkombination(Integer id, Integer pruefartIId,
			Short bStandard,
			Integer personalIIdAnlegen, Timestamp tAnlegen,
			Integer personalIIdAendern, Timestamp tAendern,Short bBoppelanschlag) {
		setIId(id);
		setPruefartIId(pruefartIId);
	

		setBStandard(bStandard);
		setBDoppelanschlag(bBoppelanschlag);
		setPersonalIIdAendern(personalIIdAendern);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setTAendern(tAendern);
		setTAnlegen(tAnlegen);

	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

}

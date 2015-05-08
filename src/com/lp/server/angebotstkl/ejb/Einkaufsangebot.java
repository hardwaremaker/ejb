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
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "EinkaufsangebotfindByAnsprechpartnerIId", query = "SELECT OBJECT (o) FROM Einkaufsangebot o WHERE o.ansprechpartnerIId=?1"),
		@NamedQuery(name = "EinkaufsangebotfindByKundeIId", query = "SELECT OBJECT (o) FROM Einkaufsangebot o WHERE o.kundeIId=?1") })
@Entity
@Table(name = "AS_EINKAUFSANGEBOT")
public class Einkaufsangebot implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "T_BELEGDATUM")
	private Timestamp tBelegdatum;

	@Column(name = "C_PROJEKT")
	private String cProjekt;

	@Column(name = "N_MENGE1")
	private BigDecimal nMenge1;

	@Column(name = "N_MENGE2")
	private BigDecimal nMenge2;

	@Column(name = "N_MENGE3")
	private BigDecimal nMenge3;

	@Column(name = "N_MENGE4")
	private BigDecimal nMenge4;

	@Column(name = "N_MENGE5")
	private BigDecimal nMenge5;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "ANSPRECHPARTNER_I_ID")
	private Integer ansprechpartnerIId;

	@Column(name = "KUNDE_I_ID")
	private Integer kundeIId;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	private static final long serialVersionUID = 1L;

	public Einkaufsangebot() {
		super();
	}

	public Einkaufsangebot(Integer id, java.lang.String mandantCNr,
			java.lang.String nr, Timestamp belegdatum, Integer kundeIId,
			BigDecimal menge1, BigDecimal menge2, BigDecimal menge3,
			BigDecimal menge4, BigDecimal menge5, Integer personalIIdAnlegen2,
			Integer personalIIdAendern2) {
		setIId(id);
		setMandantCNr(mandantCNr);
		setCNr(nr);
		setTBelegdatum(belegdatum);
		setKundeIId(kundeIId);
		setNMenge1(menge1);
		setNMenge2(menge2);
		setNMenge3(menge3);
		setNMenge4(menge4);
		setNMenge5(menge5);
		setPersonalIIdAnlegen(personalIIdAnlegen2);
		Timestamp t = new Timestamp(System.currentTimeMillis());
		setTAnlegen(t);
		setPersonalIIdAendern(personalIIdAendern2);
		setTAendern(t);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCNr() {
		return this.cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public Timestamp getTBelegdatum() {
		return this.tBelegdatum;
	}

	public void setTBelegdatum(Timestamp tBelegdatum) {
		this.tBelegdatum = tBelegdatum;
	}

	public String getCProjekt() {
		return this.cProjekt;
	}

	public void setCProjekt(String cProjekt) {
		this.cProjekt = cProjekt;
	}

	public BigDecimal getNMenge1() {
		return this.nMenge1;
	}

	public void setNMenge1(BigDecimal nMenge1) {
		this.nMenge1 = nMenge1;
	}

	public BigDecimal getNMenge2() {
		return this.nMenge2;
	}

	public void setNMenge2(BigDecimal nMenge2) {
		this.nMenge2 = nMenge2;
	}

	public BigDecimal getNMenge3() {
		return this.nMenge3;
	}

	public void setNMenge3(BigDecimal nMenge3) {
		this.nMenge3 = nMenge3;
	}

	public BigDecimal getNMenge4() {
		return this.nMenge4;
	}

	public void setNMenge4(BigDecimal nMenge4) {
		this.nMenge4 = nMenge4;
	}

	public BigDecimal getNMenge5() {
		return this.nMenge5;
	}

	public void setNMenge5(BigDecimal nMenge5) {
		this.nMenge5 = nMenge5;
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

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandant) {
		this.mandantCNr = mandant;
	}

	public Integer getAnsprechpartnerIId() {
		return this.ansprechpartnerIId;
	}

	public void setAnsprechpartnerIId(Integer ansprechpartnerIId) {
		this.ansprechpartnerIId = ansprechpartnerIId;
	}

	public Integer getKundeIId() {
		return this.kundeIId;
	}

	public void setKundeIId(Integer kundeIId) {
		this.kundeIId = kundeIId;
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

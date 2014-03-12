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
package com.lp.server.eingangsrechnung.ejb;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ER_ZAHLUNGSVORSCHLAGLAUF")
public class Zahlungsvorschlaglauf implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_ZAHLUNGSSTICHTAG")
	private Date tZahlungsstichtag;

	@Column(name = "T_NAECHSTERZAHLUNGSLAUF")
	private Date tNaechsterzahlungslauf;

	@Column(name = "B_MITSKONTO")
	private Short bMitskonto;

	@Column(name = "I_SKONTOUEBERZIEHUNGSFRISTINTAGEN")
	private Integer iSkontoueberziehungsfristintagen;

	@Column(name = "BANKVERBINDUNG_I_ID")
	private Integer bankverbindungIId;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	private static final long serialVersionUID = 1L;

	public Zahlungsvorschlaglauf() {
		super();
	}

	public Zahlungsvorschlaglauf(Integer id, String mandantCNr,
			Date zahlungsstichtag, Date naechsterzahlungslauf,
			Short mitskonto, Integer skontoueberziehungsfristintagen,
			Integer bankverbindungIId, Integer personalIIdAnlegen) {
		setIId(id);
		setMandantCNr(mandantCNr);
		setTZahlungsstichtag(zahlungsstichtag);
		setTNaechsterzahlungslauf(naechsterzahlungslauf);
		setBMitskonto(mitskonto);
		setISkontoueberziehungsfristintagen(skontoueberziehungsfristintagen);
		setBankverbindungIId(bankverbindungIId);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		// anlegen = jetzt
	    setTAnlegen(new java.sql.Timestamp(System.currentTimeMillis()));
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Timestamp getTAnlegen() {
		return this.tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Date getTZahlungsstichtag() {
		return this.tZahlungsstichtag;
	}

	public void setTZahlungsstichtag(Date tZahlungsstichtag) {
		this.tZahlungsstichtag = tZahlungsstichtag;
	}

	public Date getTNaechsterzahlungslauf() {
		return this.tNaechsterzahlungslauf;
	}

	public void setTNaechsterzahlungslauf(Date tNaechsterzahlungslauf) {
		this.tNaechsterzahlungslauf = tNaechsterzahlungslauf;
	}

	public Short getBMitskonto() {
		return this.bMitskonto;
	}

	public void setBMitskonto(Short bMitskonto) {
		this.bMitskonto = bMitskonto;
	}

	public Integer getISkontoueberziehungsfristintagen() {
		return this.iSkontoueberziehungsfristintagen;
	}

	public void setISkontoueberziehungsfristintagen(
			Integer iSkontoueberziehungsfristintagen) {
		this.iSkontoueberziehungsfristintagen = iSkontoueberziehungsfristintagen;
	}

	public Integer getBankverbindungIId() {
		return this.bankverbindungIId;
	}

	public void setBankverbindungIId(Integer bankverbindungIId) {
		this.bankverbindungIId = bankverbindungIId;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

}

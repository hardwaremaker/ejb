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
package com.lp.server.finanz.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "BankverbindungfindByKontoIId", query = "SELECT OBJECT(o) FROM Bankverbindung o WHERE o.kontoIId=?1"),
		@NamedQuery(name = "BankverbindungfindByBankIId", query = "SELECT OBJECT(o) FROM Bankverbindung o WHERE o.bankIId=?1"),
		@NamedQuery(name = BankverbindungQuery.ByBankIIdMandantCNrCKontonummer, query = "SELECT OBJECT(o) FROM Bankverbindung o WHERE o.bankIId=?1 AND o.mandantCNr=?2 AND o.cKontonummer=?3"),
		@NamedQuery(name = Bankverbindung.BankverbindungFindForLiquiditaetsvorschau, query = "SELECT OBJECT(o) FROM Bankverbindung o WHERE o.bInLiquiditaetsvorschau=1 AND o.mandantCNr=:mandant"),
		@NamedQuery(name = BankverbindungQuery.ByMandantCNr, query = "SELECT OBJECT(o) FROM Bankverbindung o WHERE o.mandantCNr=:mandant"),
		@NamedQuery(name = BankverbindungQuery.ByMandantCNrBFuerSepaLastschrift, query = "SELECT OBJECT(o) FROM Bankverbindung o WHERE o.mandantCNr=:mandant AND o.bFuerSepaLastschrift=:fuerSepaLastschrift"),
		@NamedQuery(name = BankverbindungQuery.ByBankIIdMandantCNrCIban, query = "SELECT OBJECT(o) FROM Bankverbindung o WHERE o.bankIId=:bank AND o.mandantCNr=:mandant AND o.cIban=:iban")})
@Entity
@Table(name = "FB_BANKVERBINDUNG")
public class Bankverbindung implements Serializable {
	
	public static final String BankverbindungFindForLiquiditaetsvorschau = "BankverbindungFindForLiquiditaetsvorschau";
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_KONTONUMMER")
	private String cKontonummer;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "C_IBAN")
	private String cIban;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "KONTO_I_ID")
	private Integer kontoIId;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "BANK_I_ID")
	private Integer bankIId;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;
	
	@Column(name = "B_IN_LIQUIDITAETSVORSCHAU")
	private Short bInLiquiditaetsvorschau;
	
	@Column(name = "C_SEPAVERZEICHNIS")
	private String cSepaVerzeichnis;
	
	@Column(name = "B_FUER_SEPALASTSCHRIFT")
	private Short bFuerSepaLastschrift;
	
	@Column(name = "B_ALS_GELDTRANSITKONTO")
	private Short bAlsGeldtransitkonto;

	@Column(name = "I_STELLEN_AUSZUGSNUMMER")
	private Integer iStellenAuszugsnummer;
	
	public Integer getIStellenAuszugsnummer() {
		return iStellenAuszugsnummer;
	}

	public void setIStellenAuszugsnummer(Integer iStellenAuszugsnummer) {
		this.iStellenAuszugsnummer = iStellenAuszugsnummer;
	}

	private static final long serialVersionUID = 1L;

	public Bankverbindung() {
		super();
	}

	public Bankverbindung(Integer id, java.lang.String mandantCNr,
			Integer bankIId, Integer kontoIId,
			Integer personalIIdAnlegen, Integer personalIIdAendern,
			Short bFuerSepaLastschrift) {
		setIId(id);
		// Setzen der NOT NULL FELDER
	    Timestamp now=new Timestamp(System.currentTimeMillis());
	    this.setTAendern(now);
	    this.setTAnlegen(now);
		setMandantCNr(mandantCNr);
		setBankIId(bankIId);
		setKontoIId(kontoIId);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setPersonalIIdAendern(personalIIdAendern);
		setbFuerSepaLastschrift(bFuerSepaLastschrift);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCKontonummer() {
		return this.cKontonummer;
	}

	public void setCKontonummer(String cKontonummer) {
		this.cKontonummer = cKontonummer;
	}

	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public String getCIban() {
		return this.cIban;
	}

	public void setCIban(String cIban) {
		this.cIban = cIban;
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

	public Integer getKontoIId() {
		return this.kontoIId;
	}

	public void setKontoIId(Integer kontoIId) {
		this.kontoIId = kontoIId;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandant) {
		this.mandantCNr = mandant;
	}

	public Integer getBankIId() {
		return this.bankIId;
	}

	public void setBankIId(Integer bankIId) {
		this.bankIId = bankIId;
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
	
	public Short getbInLiquiditaetsvorschau() {
		return bInLiquiditaetsvorschau;
	}
	
	public void setbInLiquiditaetsvorschau(Short bInLiquiditaetsvorschau) {
		this.bInLiquiditaetsvorschau = bInLiquiditaetsvorschau;
	}

	public String getCSepaVerzeichnis() {
		return cSepaVerzeichnis;
	}

	public void setCSepaVerzeichnis(String cSepaVerzeichnis) {
		this.cSepaVerzeichnis = cSepaVerzeichnis;
	}
	
	public Short getbFuerSepaLastschrift() {
		return bFuerSepaLastschrift;
	}
	
	public void setbFuerSepaLastschrift(Short bFuerSepaLastschrift) {
		this.bFuerSepaLastschrift = bFuerSepaLastschrift;
	}
	
	public Short getbAlsGeldtransitkonto() {
		return bAlsGeldtransitkonto;
	}
	
	public void setbAlsGeldtransitkonto(Short bAlsGeldtransitkonto) {
		this.bAlsGeldtransitkonto = bAlsGeldtransitkonto;
	}
}

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
package com.lp.server.rechnung.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( { @NamedQuery(name = "RechnungzahlungfindByRechnungIId", query = "SELECT OBJECT(o) FROM Rechnungzahlung o WHERE o.rechnungIId=?1 ORDER BY o.tZahldatum ASC, o.iId ASC"),
	@NamedQuery(name = "RechnungzahlungfindByRechnungIIdAbsteigendSortiert", query = "SELECT OBJECT(o) FROM Rechnungzahlung o WHERE o.rechnungIId=?1 ORDER BY o.tZahldatum DESC, o.iId DESC"),
	@NamedQuery(name = "RechnungzahlungfindByRechnungIIdZahlungsartCNr", query = "SELECT OBJECT(o) FROM Rechnungzahlung o WHERE o.rechnungIId=?1 AND o.zahlungsartCNr=?2")
	})
@Entity
@Table(name = "RECH_RECHNUNGZAHLUNG")
public class Rechnungzahlung implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "T_ZAHLDATUM")
	private Date tZahldatum;

	@Column(name = "I_AUSZUG")
	private Integer iAuszug;

	@Column(name = "N_KURS")
	private BigDecimal nKurs;

	@Column(name = "N_BETRAG")
	private BigDecimal nBetrag;

	@Column(name = "N_BETRAGFW")
	private BigDecimal nBetragfw;

	@Column(name = "N_BETRAGUST")
	private BigDecimal nBetragust;

	@Column(name = "N_BETRAGUSTFW")
	private BigDecimal nBetragustfw;

	@Column(name = "F_SKONTO")
	private Double fSkonto;

	@Column(name = "T_WECHSELFAELLIGAM")
	private Date tWechselfaelligam;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "BANKVERBINDUNG_I_ID")
	private Integer bankverbindungIId;

	@Column(name = "KASSENBUCH_I_ID")
	private Integer kassenbuchIId;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "RECHNUNG_I_ID_GUTSCHRIFT")
	private Integer rechnungIIdGutschrift;

	@Column(name = "RECHNUNG_I_ID")
	private Integer rechnungIId;

	@Column(name = "RECHNUNGZAHLUNG_I_ID_GUTSCHRIFT")
	private Integer rechnungzahlungIIdGutschrift;

	@Column(name = "BUCHUNGDETAIL_I_ID")
	private Integer buchungdetailIId;
	
	public Integer getBuchungdetailIId() {
		return buchungdetailIId;
	}
	
	public void setBuchungdetailIId(Integer buchungdetailIId) {
		this.buchungdetailIId = buchungdetailIId;
	}
	
	public Integer getEingangsrechnungIId() {
		return eingangsrechnungIId;
	}

	public void setEingangsrechnungIId(Integer eingangsrechnungIId) {
		this.eingangsrechnungIId = eingangsrechnungIId;
	}

	@Column(name = "EINGANGSRECHNUNG_I_ID")
	private Integer eingangsrechnungIId;
	
	
	@Column(name = "ZAHLUNGSART_C_NR")
	private String zahlungsartCNr;

	private static final long serialVersionUID = 1L;

	public Rechnungzahlung() {
		super();
	}

	public Rechnungzahlung(Integer id, Integer rechnungIId,
			Date zahldatum, String zahlungsartCNr, BigDecimal kurs,
			BigDecimal betrag, BigDecimal betragfw, BigDecimal betragUst,
			BigDecimal betragUstfw, Integer personalIIdAnlegen,
			Integer personalIIdAendern) {
		setIId(id);
		setRechnungIId(rechnungIId);
		setTZahldatum(zahldatum);
		setZahlungsartCNr(zahlungsartCNr);
		setNKurs(kurs);
		setNBetrag(betrag);
		setNBetragfw(betragfw);
		setNBetragust(betragUst);
		setNBetragustfw(betragUstfw);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setPersonalIIdAendern(personalIIdAendern);
		// NOT NULL Felder
		Timestamp now = new Timestamp(System.currentTimeMillis());
		setTAnlegen(now);
		setTAendern(now);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Date getTZahldatum() {
		return this.tZahldatum;
	}

	public void setTZahldatum(Date tZahldatum) {
		this.tZahldatum = tZahldatum;
	}


	public Integer getIAuszug() {
		return this.iAuszug;
	}

	public void setIAuszug(Integer iAuszug) {
		this.iAuszug = iAuszug;
	}
	public BigDecimal getNKurs() {
		return this.nKurs;
	}

	public void setNKurs(BigDecimal nKurs) {
		this.nKurs = nKurs;
	}

	public BigDecimal getNBetrag() {
		return this.nBetrag;
	}

	public void setNBetrag(BigDecimal nBetrag) {
		this.nBetrag = nBetrag;
	}

	public BigDecimal getNBetragfw() {
		return this.nBetragfw;
	}

	public void setNBetragfw(BigDecimal nBetragfw) {
		this.nBetragfw = nBetragfw;
	}

	public BigDecimal getNBetragust() {
		return this.nBetragust;
	}

	public void setNBetragust(BigDecimal nBetragust) {
		this.nBetragust = nBetragust;
	}

	public BigDecimal getNBetragustfw() {
		return this.nBetragustfw;
	}

	public void setNBetragustfw(BigDecimal nBetragustfw) {
		this.nBetragustfw = nBetragustfw;
	}

	public Double getFSkonto() {
		return this.fSkonto;
	}

	public void setFSkonto(Double fSkonto) {
		this.fSkonto = fSkonto;
	}

	public Date getTWechselfaelligam() {
		return this.tWechselfaelligam;
	}

	public void setTWechselfaelligam(Date tWechselfaelligam) {
		this.tWechselfaelligam = tWechselfaelligam;
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

	public Integer getBankverbindungIId() {
		return this.bankverbindungIId;
	}

	public void setBankverbindungIId(Integer bankverbindungIId) {
		this.bankverbindungIId = bankverbindungIId;
	}

	public Integer getKassenbuchIId() {
		return this.kassenbuchIId;
	}

	public void setKassenbuchIId(Integer kassenbuchIId) {
		this.kassenbuchIId = kassenbuchIId;
	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getRechnungIIdGutschrift() {
		return this.rechnungIIdGutschrift;
	}

	public void setRechnungIIdGutschrift(Integer rechnungIIdGutschrift) {
		this.rechnungIIdGutschrift = rechnungIIdGutschrift;
	}

	public Integer getRechnungIId() {
		return this.rechnungIId;
	}

	public void setRechnungIId(Integer rechnungIId) {
		this.rechnungIId = rechnungIId;
	}

	public Integer getRechnungzahlungIIdGutschrift() {
		return this.rechnungzahlungIIdGutschrift;
	}

	public void setRechnungzahlungIIdGutschrift(
			Integer rechnungzahlungIIdGutschrift) {
		this.rechnungzahlungIIdGutschrift = rechnungzahlungIIdGutschrift;
	}

	public String getZahlungsartCNr() {
		return this.zahlungsartCNr;
	}

	public void setZahlungsartCNr(String zahlungsartCNr) {
		this.zahlungsartCNr = zahlungsartCNr;
	}

}

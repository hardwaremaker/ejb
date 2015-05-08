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
package com.lp.server.eingangsrechnung.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( { @NamedQuery(name = "ZahlungsvorschlagfindByZahlungsvorschlaglaufIId", query = "SELECT OBJECT(o) FROM Zahlungsvorschlag o WHERE o.zahlungsvorschlaglaufIId=?1") })
@Entity
@Table(name = "ER_ZAHLUNGSVORSCHLAG")
public class Zahlungsvorschlag implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "B_BEZAHLEN")
	private Short bBezahlen;

	@Column(name = "T_FAELLIG")
	private Date tFaellig;

	@Column(name = "N_ANGEWANDTERSKONTOSATZ")
	private BigDecimal nAngewandterskontosatz;

	@Column(name = "N_ER_BRUTTO_BETRAG")
	private BigDecimal nErBruttoBetrag;
	@Column(name = "N_BEREITS_BEZAHLT")
	private BigDecimal nBereitsBezahlt;
	@Column(name = "N_ZAHLBETRAG")
	private BigDecimal nZahlbetrag;
	@Column(name = "B_WAERE_VOLLSTAENDIG_BEZAHLT")
	private Short bWaereVollstaendigBezahlt;
	
	

	@Column(name = "EINGANGSRECHNUNG_I_ID")
	private Integer eingangsrechnungIId;

	@Column(name = "ZAHLUNGSVORSCHLAGLAUF_I_ID")
	private Integer zahlungsvorschlaglaufIId;

	private static final long serialVersionUID = 1L;

	public Zahlungsvorschlag() {
		super();
	}

	public Zahlungsvorschlag(Integer id, Integer zahlungsvorschlaglaufIId,
			Integer eingangsrechnungIId, Short bezahlen, Date faellig,
			BigDecimal angewandterskontosatz, BigDecimal erBruttoBetrag, BigDecimal bereitsBezahlt, BigDecimal zahlbetrag,Short waereVollstaendigBezahlt) {
		setIId(id);
		setZahlungsvorschlaglaufIId(zahlungsvorschlaglaufIId);
		setEingangsrechnungIId(eingangsrechnungIId);
		setBBezahlen(bezahlen);
		setTFaellig(faellig);
		setNAngewandterskontosatz(angewandterskontosatz);
		setNErBruttoBetrag(erBruttoBetrag);
		setNBereitsBezahlt(bereitsBezahlt);
		setNZahlbetrag(zahlbetrag);
		setBWaereVollstaendigBezahlt(waereVollstaendigBezahlt);
	}

	public BigDecimal getNErBruttoBetrag() {
		return nErBruttoBetrag;
	}

	public void setNErBruttoBetrag(BigDecimal nErBruttoBetrag) {
		this.nErBruttoBetrag = nErBruttoBetrag;
	}

	public BigDecimal getNBereitsBezahlt() {
		return nBereitsBezahlt;
	}

	public void setNBereitsBezahlt(BigDecimal nBereitsBezahlt) {
		this.nBereitsBezahlt = nBereitsBezahlt;
	}

	public BigDecimal getNZahlbetrag() {
		return nZahlbetrag;
	}

	public void setNZahlbetrag(BigDecimal nZahlbetrag) {
		this.nZahlbetrag = nZahlbetrag;
	}

	public Short getBWaereVollstaendigBezahlt() {
		return bWaereVollstaendigBezahlt;
	}

	public void setBWaereVollstaendigBezahlt(Short bWaereVollstaendigBezahlt) {
		this.bWaereVollstaendigBezahlt = bWaereVollstaendigBezahlt;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Short getBBezahlen() {
		return this.bBezahlen;
	}

	public void setBBezahlen(Short bBezahlen) {
		this.bBezahlen = bBezahlen;
	}

	public Date getTFaellig() {
		return this.tFaellig;
	}

	public void setTFaellig(Date tFaellig) {
		this.tFaellig = tFaellig;
	}

	public BigDecimal getNAngewandterskontosatz() {
		return this.nAngewandterskontosatz;
	}

	public void setNAngewandterskontosatz(BigDecimal nAngewandterskontosatz) {
		this.nAngewandterskontosatz = nAngewandterskontosatz;
	}


	public Integer getEingangsrechnungIId() {
		return this.eingangsrechnungIId;
	}

	public void setEingangsrechnungIId(Integer eingangsrechnungIId) {
		this.eingangsrechnungIId = eingangsrechnungIId;
	}

	public Integer getZahlungsvorschlaglaufIId() {
		return this.zahlungsvorschlaglaufIId;
	}

	public void setZahlungsvorschlaglaufIId(Integer zahlungsvorschlaglaufIId) {
		this.zahlungsvorschlaglaufIId = zahlungsvorschlaglaufIId;
	}

}

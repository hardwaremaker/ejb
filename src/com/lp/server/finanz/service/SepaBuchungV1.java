/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2016 HELIUM V IT-Solutions GmbH
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
package com.lp.server.finanz.service;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class SepaBuchungV1 implements Serializable {

	/**
	 * Diese Id darf NIE geaendert werden
	 */
	private static final long serialVersionUID = 3974636058641967234L;

	private String cWaehrung;
	private String cStatus; 
	private Date dBuchungsdatum; 
	private Date dValutadatum; 
	private String cBankreferenz; 
	private SepaBetragV1 betrag;
	private SepaBankTransactionCodeV1 buchungscode;
	private List<SepaZahlungV1> zahlungen;
	
	public SepaBuchungV1() {
	}

	public String getCWaehrung() {
		return cWaehrung;
	}

	public void setCWaehrung(String cWaehrung) {
		this.cWaehrung = cWaehrung;
	}

	public String getCStatus() {
		return cStatus;
	}

	public void setCStatus(String cStatus) {
		this.cStatus = cStatus;
	}

	public Date getDBuchungsdatum() {
		return dBuchungsdatum;
	}

	public void setDBuchungsdatum(Date dBuchungsdatum) {
		this.dBuchungsdatum = dBuchungsdatum;
	}

	public Date getDValutadatum() {
		return dValutadatum;
	}

	public void setDValutadatum(Date dValutadatum) {
		this.dValutadatum = dValutadatum;
	}

	public String getCBankreferenz() {
		return cBankreferenz;
	}

	public void setCBankreferenz(String cBankreferenz) {
		this.cBankreferenz = cBankreferenz;
	}

	public SepaBetragV1 getBetrag() {
		return betrag;
	}

	public void setBetrag(SepaBetragV1 betrag) {
		this.betrag = betrag;
	}

	public SepaBankTransactionCodeV1 getBuchungscode() {
		return buchungscode;
	}

	public void setBuchungscode(SepaBankTransactionCodeV1 buchungscode) {
		this.buchungscode = buchungscode;
	}

	public List<SepaZahlungV1> getZahlungen() {
		return zahlungen;
	}

	public void setZahlungen(List<SepaZahlungV1> zahlungen) {
		this.zahlungen = zahlungen;
	}
	
}

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

public class SepaZahlungV1 implements Serializable {

	/**
	 * Diese Id darf NIE geaendert werden
	 */
	private static final long serialVersionUID = -3287043789354941243L;

	private SepaBetragV1 betrag;
	private String cVerwendungszweck; 
	private String cZahlungsreferenz;
	private String cAuftraggeberreferenz;
	private String cBankreferenz;
	private SepaKontoinformationV1 kontoinformation;
	private SepaBankTransactionCodeV1 buchungscode;
	
	public SepaZahlungV1() {
	}

	public SepaBetragV1 getBetrag() {
		return betrag;
	}

	public void setBetrag(SepaBetragV1 betrag) {
		this.betrag = betrag;
	}

	public String getcVerwendungszweck() {
		return cVerwendungszweck;
	}

	public void setcVerwendungszweck(String cVerwendungszweck) {
		this.cVerwendungszweck = cVerwendungszweck;
	}

	public String getcZahlungsreferenz() {
		return cZahlungsreferenz;
	}

	public void setcZahlungsreferenz(String cZahlungsreferenz) {
		this.cZahlungsreferenz = cZahlungsreferenz;
	}

	public String getcAuftraggeberreferenz() {
		return cAuftraggeberreferenz;
	}

	public void setcAuftraggeberreferenz(String cAuftraggeberreferenz) {
		this.cAuftraggeberreferenz = cAuftraggeberreferenz;
	}

	public String getcBankreferenz() {
		return cBankreferenz;
	}

	public void setcBankreferenz(String cBankreferenz) {
		this.cBankreferenz = cBankreferenz;
	}

	public SepaKontoinformationV1 getKontoinformation() {
		return kontoinformation;
	}

	public void setKontoinformation(SepaKontoinformationV1 kontoinformation) {
		this.kontoinformation = kontoinformation;
	}

	public SepaBankTransactionCodeV1 getBuchungscode() {
		return buchungscode;
	}

	public void setBuchungscode(SepaBankTransactionCodeV1 buchungscode) {
		this.buchungscode = buchungscode;
	}

}

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
package com.lp.server.eingangsrechnung.service;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lp.util.Helper;

public abstract class VendidataPaymentInfo implements Serializable, IDiscountPaymentDrawee {

	private static final long serialVersionUID = -5603181242018970791L;

	public enum SettledPaymentType {
		SINGLE_PAYMENT,
		FLATCHARGE_PAYMENT,
		PAYMENT;
	}
	
	private BigDecimal betrag;
	private String periodenTyp;
	private String periodenNummer;
	private String periodenJahr;
	private Double steuersatz;
	private String uebersteuerteBezeichnung;

	public VendidataPaymentInfo() {
	}

	public BigDecimal getBetrag() {
		return betrag;
	}

	public void setBetrag(BigDecimal betrag) {
		this.betrag = betrag;
	}

	public abstract String getKommentar();

	public abstract String getBezeichnung();
	
	public abstract boolean isBezeichnungToken();
	
	public String getPeriodenTyp() {
		return periodenTyp;
	}

	public void setPeriodenTyp(String periodenTyp) {
		this.periodenTyp = periodenTyp;
	}

	public String getPeriodenNummer() {
		return periodenNummer;
	}

	public void setPeriodenNummer(String periodenNummer) {
		this.periodenNummer = periodenNummer;
	}

	public String getPeriodenJahr() {
		return periodenJahr;
	}

	public void setPeriodenJahr(String periodenJahr) {
		this.periodenJahr = periodenJahr;
	}
	
	public Boolean isEmpty() {
		return getBetrag() == null || BigDecimal.ZERO.compareTo(getBetrag()) == 0;
	}

	public Double getSteuersatz() {
		return steuersatz;
	}
	
	public void setSteuersatz(Double steuersatz) {
		this.steuersatz = steuersatz;
	}
	
	public boolean hasUebersteuertenSteuersatz() {
		//PJ20371
		return getSteuersatz() != null
				&& Helper.isBetweenStartExcluded(getSteuersatz(), new Double("0"), new Double("19"));
	}
	
	public void setUebersteuerteBezeichnung(String uebersteuerteBezeichnung) {
		this.uebersteuerteBezeichnung = uebersteuerteBezeichnung;
	}
	
	public String getUebersteuerteBezeichnung() {
		return uebersteuerteBezeichnung;
	}
	
	public boolean hasUebersteuerteBezeichnung() {
		return !Helper.isStringEmpty(getUebersteuerteBezeichnung());
	}
}

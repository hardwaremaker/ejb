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
 *******************************************************************************/
package com.lp.server.finanz.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.lp.server.util.Validator;

public class BuchungsjournalReportParameter implements Serializable {
	private static final long serialVersionUID = 7318941251122213380L;

	private Integer buchungsjournalIId ;
	private Date dVon ;
	private  Date dBis ;
	private boolean storniert ;
	private boolean bDatumsfilterIstBuchungsdatum ;
	private String buchungsText ;
	private String belegnummer ;
	private BigDecimal betrag ;
	private String kontonummer ;
	private String buchungsart ; 
	private String belegart ;
	
	public BuchungsjournalReportParameter(Integer buchungsJournalIId) {
		setBuchungsjournalIId(buchungsJournalIId); 
	}

	public Integer getBuchungsjournalIId() {
		return buchungsjournalIId;
	}

	public void setBuchungsjournalIId(Integer buchungsjournalIId) {
		Validator.notNull(buchungsjournalIId, "buchungsJournalIId");
		this.buchungsjournalIId = buchungsjournalIId;
	}

	public Date getdVon() {
		return dVon;
	}

	public void setdVon(Date dVon) {
		this.dVon = dVon;
	}

	public Date getdBis() {
		return dBis;
	}

	public void setdBis(Date dBis) {
		this.dBis = dBis;
	}

	public boolean isStorniert() {
		return storniert;
	}

	public void setStorniert(boolean storniert) {
		this.storniert = storniert;
	}

	public boolean isbDatumsfilterIstBuchungsdatum() {
		return bDatumsfilterIstBuchungsdatum;
	}

	public void setbDatumsfilterIstBuchungsdatum(
			boolean bDatumsfilterIstBuchungsdatum) {
		this.bDatumsfilterIstBuchungsdatum = bDatumsfilterIstBuchungsdatum;
	}

	public String getBuchungsText() {
		return buchungsText;
	}

	public void setBuchungsText(String buchungsText) {
		this.buchungsText = buchungsText;
	}

	public String getBelegnummer() {
		return belegnummer;
	}

	public void setBelegnummer(String belegnummer) {
		this.belegnummer = belegnummer;
	}

	public BigDecimal getBetrag() {
		return betrag;
	}

	public void setBetrag(BigDecimal betrag) {
		this.betrag = betrag;
	}

	public String getKontonummer() {
		return kontonummer;
	}

	public void setKontonummer(String kontonummer) {
		this.kontonummer = kontonummer;
	}

	public String getBuchungsart() {
		return buchungsart;
	}

	public void setBuchungsart(String buchungsart) {
		this.buchungsart = buchungsart;
	}

	public String getBelegart() {
		return belegart;
	}

	public void setBelegart(String belegart) {
		this.belegart = belegart;
	}	
}

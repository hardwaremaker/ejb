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
package com.lp.server.finanz.bl.datevexport;

import java.math.BigDecimal;
import java.util.Date;

public class BuchungsjournalExportDatevBuchung {
	
	private BigDecimal umsatz;
	private boolean soll;
	private String konto;
	private String gegenkonto;
	private String buSchluessel;
	private Date belegdatum;
	private String beleg;
	private String buchungstext;
	private String uid;
	private BigDecimal euSteuersatz;
	private String belegInfo1;
	private String belegInfo2;
	
	public BigDecimal getUmsatz() {
		return umsatz;
	}
	public void setUmsatz(BigDecimal umsatz) {
		this.umsatz = umsatz;
	}
	public boolean isSoll() {
		return soll;
	}
	public void setSoll(boolean soll) {
		this.soll = soll;
	}
	public String getKonto() {
		return konto;
	}
	public void setKonto(String konto) {
		this.konto = konto;
	}
	public String getGegenkonto() {
		return gegenkonto;
	}
	public void setGegenkonto(String gegenkonto) {
		this.gegenkonto = gegenkonto;
	}
	public String getBuSchluessel() {
		return buSchluessel;
	}
	public void setBuSchluessel(String steuerschluessel) {
		this.buSchluessel = steuerschluessel;
	}
	public Date getBelegdatum() {
		return belegdatum;
	}
	public void setBelegdatum(Date belegdatum) {
		this.belegdatum = belegdatum;
	}
	public String getBeleg() {
		return beleg;
	}
	public void setBeleg(String beleg) {
		this.beleg = beleg;
	}
	public String getBuchungstext() {
		return buchungstext;
	}
	public void setBuchungstext(String buchungstext) {
		this.buchungstext = buchungstext;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public BigDecimal getEuSteuersatz() {
		return euSteuersatz;
	}
	public void setEuSteuersatz(BigDecimal euSteuersatz) {
		this.euSteuersatz = euSteuersatz;
	}
	public String getBelegInfo1() {
		return belegInfo1;
	}
	public void setBelegInfo1(String belegInfo1) {
		this.belegInfo1 = belegInfo1;
	}
	public String getBelegInfo2() {
		return belegInfo2;
	}
	public void setBelegInfo2(String belegInfo2) {
		this.belegInfo2 = belegInfo2;
	}
	
}

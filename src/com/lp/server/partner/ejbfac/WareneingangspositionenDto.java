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
package com.lp.server.partner.ejbfac;

import java.io.*;
import java.math.*;
import java.util.*;

public class WareneingangspositionenDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sWaehrung = null;
	private String sIdent = null;
	private BigDecimal bdWert = null;
	private BigDecimal bdMenge = null;
	private String sWas = null;
	private String sNr = null;
	private Date dBelegdatum = null;
	private String sBezeichnung = null;
	private BigDecimal bdPreis = null;
	private String sEinheit = null;

	public WareneingangspositionenDto() {
	}

	public void setSWaehrung(String sWaehrung) {
		this.sWaehrung = sWaehrung;
	}

	public void setSIdent(String sIdent) {
		this.sIdent = sIdent;
	}

	public void setBdWert(BigDecimal bdWert) {
		this.bdWert = bdWert;
	}

	public void setBdMenge(BigDecimal bdMenge) {
		this.bdMenge = bdMenge;
	}

	public void setSWas(String sWas) {
		this.sWas = sWas;
	}

	public void setSNr(String sNr) {
		this.sNr = sNr;
	}

	public void setDBelegdatum(Date dBelegdatum) {
		this.dBelegdatum = dBelegdatum;
	}

	public void setSBezeichnung(String sBezeichnung) {
		this.sBezeichnung = sBezeichnung;
	}

	public void setBdPreis(BigDecimal bdPreis) {
		this.bdPreis = bdPreis;
	}

	public void setSEinheit(String sEinheit) {
		this.sEinheit = sEinheit;
	}

	public String getSWaehrung() {
		return sWaehrung;
	}

	public String getSIdent() {
		return sIdent;
	}

	public BigDecimal getBdWert() {
		return bdWert;
	}

	public BigDecimal getBdMenge() {
		return bdMenge;
	}

	public String getSWas() {
		return sWas;
	}

	public String getSNr() {
		return sNr;
	}

	public Date getDBelegdatum() {
		return dBelegdatum;
	}

	public String getSBezeichnung() {
		return sBezeichnung;
	}

	public BigDecimal getBdPreis() {
		return bdPreis;
	}

	public String getSEinheit() {
		return sEinheit;
	}
}

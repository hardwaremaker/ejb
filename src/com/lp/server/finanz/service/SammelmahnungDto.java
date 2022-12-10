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
package com.lp.server.finanz.service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * Diese Klasse kuemmert sich um ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 29.09.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: heidi $
 *         </p>
 * 
 * @version not attributable Date $Date: 2008/08/07 12:57:11 $
 */
public class SammelmahnungDto {
	private String sRechnungsnummer = null;
	private Date dBelegdatum = null;
	private Date dFaelligkeitsdatum = null;
	private BigDecimal bdOffen = null;
	private BigDecimal bdWert = null;
	private Integer iMahnstufe = null;
	private BigDecimal bdZinsen = null;
	
	public BigDecimal getBdOffenBelegWaehrung() {
		return bdOffenBelegWaehrung;
	}

	public void setBdOffenBelegWaehrung(BigDecimal bdOffenBelegWaehrung) {
		this.bdOffenBelegWaehrung = bdOffenBelegWaehrung;
	}

	public BigDecimal getBdWertBelegwaehrung() {
		return bdWertBelegwaehrung;
	}

	public void setBdWertBelegwaehrung(BigDecimal bdWertBelegwaehrung) {
		this.bdWertBelegwaehrung = bdWertBelegwaehrung;
	}

	public String getBelegwaehrung() {
		return belegwaehrung;
	}

	public void setBelegwaehrung(String belegwaehrung) {
		this.belegwaehrung = belegwaehrung;
	}

	private BigDecimal bdOffenBelegWaehrung = null;
	private BigDecimal bdWertBelegwaehrung = null;
	private String belegwaehrung = null;
	

	public BigDecimal getBdOffen() {
		return bdOffen;
	}

	public BigDecimal getBdZinsen() {
		return bdZinsen;
	}

	public BigDecimal getBdWert() {
		return bdWert;
	}

	public Date getDBelegdatum() {
		return dBelegdatum;
	}

	public Date getDFaelligkeitsdatum() {
		return dFaelligkeitsdatum;
	}

	public Integer getIMahnstufe() {
		return iMahnstufe;
	}

	public String getSRechnungsnummer() {
		return sRechnungsnummer;
	}

	public void setBdOffen(BigDecimal bdOffen) {
		this.bdOffen = bdOffen;
	}

	public void setBdWert(BigDecimal bdWert) {
		this.bdWert = bdWert;
	}

	public void setBdZinsen(BigDecimal bdZinsen) {
		this.bdZinsen = bdZinsen;
	}

	public void setDBelegdatum(Date dBelegdatum) {
		this.dBelegdatum = dBelegdatum;
	}

	public void setIMahnstufe(Integer iMahnstufe) {
		this.iMahnstufe = iMahnstufe;
	}

	public void setDFaelligkeitsdatum(Date dFaelligkeitsdatum) {
		this.dFaelligkeitsdatum = dFaelligkeitsdatum;
	}

	public void setSRechnungsnummer(String sRechnungsnummer) {
		this.sRechnungsnummer = sRechnungsnummer;
	}
}

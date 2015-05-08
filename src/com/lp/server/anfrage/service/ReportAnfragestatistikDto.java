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
package com.lp.server.anfrage.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class ReportAnfragestatistikDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iIndex;
	private String anfrageCNr;
	private String kundenname;
	private String belegdatumCNr;
	private BigDecimal nAngefragtemenge;
	private BigDecimal nAngebotenemenge;
	private BigDecimal nAngebotenerpreis;
	private Integer iLieferzeit;

	public Integer getIIndex() {
		return iIndex;
	}

	public void setIIndex(Integer iIndexI) {
		this.iIndex = iIndexI;
	}

	public String getAnfrageCNr() {
		return anfrageCNr;
	}

	public void setAnfrageCNr(String anfrageCNrI) {
		this.anfrageCNr = anfrageCNrI;
	}

	public String getKundenname() {
		return kundenname;
	}

	public void setKundenname(String kundennameI) {
		this.kundenname = kundennameI;
	}

	public String getBelegdatumCNr() {
		return belegdatumCNr;
	}

	public void setBelegdatumCNr(String belegdatumCNrI) {
		this.belegdatumCNr = belegdatumCNrI;
	}

	public BigDecimal getNAngefragtemenge() {
		return nAngefragtemenge;
	}

	public void setNAngefragtemenge(BigDecimal nAngefragtemengeI) {
		this.nAngefragtemenge = nAngefragtemengeI;
	}

	public BigDecimal getNAngebotenemenge() {
		return this.nAngebotenemenge;
	}

	public void setNAngebotenemenge(BigDecimal nAngebotenemengeI) {
		this.nAngebotenemenge = nAngebotenemengeI;
	}

	public BigDecimal getNAngebotenerpreis() {
		return this.nAngebotenerpreis;
	}

	public void setNAngebotenerpreis(BigDecimal nAngebotenerpreisI) {
		this.nAngebotenerpreis = nAngebotenerpreisI;
	}

	public Integer getILieferzeit() {
		return iLieferzeit;
	}

	public void setILieferzeit(Integer iLieferzeitI) {
		this.iLieferzeit = iLieferzeitI;
	}
}

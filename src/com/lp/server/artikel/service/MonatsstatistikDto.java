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
package com.lp.server.artikel.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

public class MonatsstatistikDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal zugangwert;
	private BigDecimal zugangmenge;
	private BigDecimal abgangmenge;
	private BigDecimal abgangwert;
	private Date dDatum;

	public MonatsstatistikDto() {

		zugangwert = new BigDecimal(0);
		abgangwert = new BigDecimal(0);
		zugangmenge = new BigDecimal(0);

		abgangmenge = new BigDecimal(0);
	}

	public BigDecimal getAbgangwert() {
		return abgangwert;
	}

	public BigDecimal getAbgangmenge() {
		return abgangmenge;
	}

	public BigDecimal getZugangmenge() {
		return zugangmenge;
	}

	public BigDecimal getZugangwert() {
		return zugangwert;
	}

	public Date getDDatum() {
		return dDatum;
	}

	public void setAbgangwert(BigDecimal abgangwert) {
		this.abgangwert = abgangwert;
	}

	public void setAbgangmenge(BigDecimal abgangmenge) {
		this.abgangmenge = abgangmenge;
	}

	public void setZugangmenge(BigDecimal zugangmenge) {
		this.zugangmenge = zugangmenge;
	}

	public void setZugangwert(BigDecimal zugangwert) {
		this.zugangwert = zugangwert;
	}

	public void setDDatum(Date dDatum) {
		this.dDatum = dDatum;
	}

}

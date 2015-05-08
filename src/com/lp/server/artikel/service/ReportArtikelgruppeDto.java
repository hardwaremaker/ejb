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

import java.math.BigDecimal;
import java.util.HashMap;

public class ReportArtikelgruppeDto {
	private HashMap<Integer,ReportArtikelgruppeDto> kinder = new HashMap<Integer,ReportArtikelgruppeDto>();
	private BigDecimal umsatz = new BigDecimal(0);
	private BigDecimal menge = new BigDecimal(0);
	public HashMap<Integer,ReportArtikelgruppeDto> getSubGruppen() {
		return kinder;
	}
	public void setSubGruppen(HashMap<Integer,ReportArtikelgruppeDto> vatergruppe) {
		this.kinder = vatergruppe;
	}
	private BigDecimal gestwert = new BigDecimal(0);
	public BigDecimal getUmsatz() {
		return umsatz;
	}
	public void add2Umsatz(BigDecimal umsatz) {
		this.umsatz =this.umsatz.add(umsatz);
	}
	public BigDecimal getGestwert() {
		return gestwert;
	}
	public void add2Gestwert(BigDecimal gestwert) {
		this.gestwert = this.gestwert.add(gestwert);
	}
	public void add2Menge(BigDecimal menge) {
		this.menge = this.menge.add(menge);
	}
	public BigDecimal getMenge() {
		return menge;
	}
	
	
	
}

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
package com.lp.server.auftrag.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class AuftragUebersichtTabelleDto extends TabelleDto implements
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal bdFreieauftraege1;
	private BigDecimal bdAbrufauftraege1;
	private BigDecimal bdRahmenauftrage1;
	private String sEmpty = TabelleDto.LEERESPALTE;
	private BigDecimal bdFreieauftraege2;
	private BigDecimal bdAbrufauftraege2;
	private BigDecimal bdRahmenauftrage2;

	public BigDecimal getBdFreieauftraege1() {
		return bdFreieauftraege1;
	}

	public void setBdFreieauftraege1(BigDecimal bdFreieauftraege1) {
		this.bdFreieauftraege1 = bdFreieauftraege1;
	}

	public BigDecimal getBdAbrufauftraege1() {
		return bdAbrufauftraege1;
	}

	public void setBdAbrufauftraege1(BigDecimal bdAbrufauftraege1) {
		this.bdAbrufauftraege1 = bdAbrufauftraege1;
	}

	public BigDecimal getBdRahmenauftrage1() {
		return bdRahmenauftrage1;
	}

	public void setBdRahmenauftrage1(BigDecimal bdRahmenauftrage1) {
		this.bdRahmenauftrage1 = bdRahmenauftrage1;
	}

	public String getSEmpty() {
		return sEmpty;
	}

	public void setSEmpty(String sEmpty) {
		this.sEmpty = sEmpty;
	}

	public BigDecimal getBdFreieauftraege2() {
		return bdFreieauftraege2;
	}

	public void setBdFreieauftraege2(BigDecimal bdFreieauftraege2) {
		this.bdFreieauftraege2 = bdFreieauftraege2;
	}

	public BigDecimal getBdAbrufauftraege2() {
		return bdAbrufauftraege2;
	}

	public void setBdAbrufauftraege2(BigDecimal bdAbrufauftraege2) {
		this.bdAbrufauftraege2 = bdAbrufauftraege2;
	}

	public BigDecimal getBdRahmenauftrage2() {
		return bdRahmenauftrage2;
	}

	public void setBdRahmenauftrage2(BigDecimal bdRahmenauftrage2) {
		this.bdRahmenauftrage2 = bdRahmenauftrage2;
	}
}

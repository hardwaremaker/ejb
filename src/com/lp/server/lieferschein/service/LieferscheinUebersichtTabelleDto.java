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
package com.lp.server.lieferschein.service;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lp.server.auftrag.service.TabelleDto;

public class LieferscheinUebersichtTabelleDto extends TabelleDto implements
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal bdUmsatzJahr0;
	private BigDecimal bdUmsatzJahr1;
	private BigDecimal bdUmsatzJahr2;
	private BigDecimal bdUmsatzJahr3;
	private BigDecimal bdUmsatzJahr4;
	private BigDecimal bdUmsatzJahr5;
	private BigDecimal bdUmsatzJahr6;
	private BigDecimal bdUmsatzJahr7;
	private BigDecimal bdUmsatzJahr8;
	private BigDecimal bdUmsatzJahr9;

	public BigDecimal getBdUmsatzJahr0() {
		return bdUmsatzJahr0;
	}

	public void setBdUmsatzJahr0(BigDecimal bdUmsatzJahr0) {
		this.bdUmsatzJahr0 = bdUmsatzJahr0;
	}

	public BigDecimal getBdUmsatzJahr1() {
		return bdUmsatzJahr1;
	}

	public void setBdUmsatzJahr1(BigDecimal bdUmsatzJahr1) {
		this.bdUmsatzJahr1 = bdUmsatzJahr1;
	}

	public BigDecimal getBdUmsatzJahr2() {
		return bdUmsatzJahr2;
	}

	public void setBdUmsatzJahr2(BigDecimal bdUmsatzJahr2) {
		this.bdUmsatzJahr2 = bdUmsatzJahr2;
	}

	public BigDecimal getBdUmsatzJahr3() {
		return bdUmsatzJahr3;
	}

	public void setBdUmsatzJahr3(BigDecimal bdUmsatzJahr3) {
		this.bdUmsatzJahr3 = bdUmsatzJahr3;
	}

	public BigDecimal getBdUmsatzJahr4() {
		return bdUmsatzJahr4;
	}

	public void setBdUmsatzJahr4(BigDecimal bdUmsatzJahr4) {
		this.bdUmsatzJahr4 = bdUmsatzJahr4;
	}

	public BigDecimal getBdUmsatzJahr5() {
		return bdUmsatzJahr5;
	}

	public void setBdUmsatzJahr5(BigDecimal bdUmsatzJahr5) {
		this.bdUmsatzJahr5 = bdUmsatzJahr5;
	}

	public BigDecimal getBdUmsatzJahr6() {
		return bdUmsatzJahr6;
	}

	public void setBdUmsatzJahr6(BigDecimal bdUmsatzJahr6) {
		this.bdUmsatzJahr6 = bdUmsatzJahr6;
	}

	public BigDecimal getBdUmsatzJahr7() {
		return bdUmsatzJahr7;
	}

	public void setBdUmsatzJahr7(BigDecimal bdUmsatzJahr7) {
		this.bdUmsatzJahr7 = bdUmsatzJahr7;
	}

	public BigDecimal getBdUmsatzJahr8() {
		return bdUmsatzJahr8;
	}

	public void setBdUmsatzJahr8(BigDecimal bdUmsatzJahr8) {
		this.bdUmsatzJahr8 = bdUmsatzJahr8;
	}

	public BigDecimal getBdUmsatzJahr9() {
		return bdUmsatzJahr9;
	}

	public void setBdUmsatzJahr9(BigDecimal bdUmsatzJahr9) {
		this.bdUmsatzJahr9 = bdUmsatzJahr9;
	}
}

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
package com.lp.server.fertigung.service;

import java.math.BigDecimal;

public class ReportTheoretischeFehlmengenDto {
	private Integer artikelIId = null;
	private BigDecimal bdMenge = null;
	private BigDecimal bdAusgegebeneMenge = null;
	private Integer artikelIIdOriginal=null;
	public Integer getArtikelIIdOriginal() {
		return artikelIIdOriginal;
	}

	public void setArtikelIIdOriginal(Integer artikelIIdOriginal) {
		this.artikelIIdOriginal = artikelIIdOriginal;
	}

	private LossollmaterialDto[] ersatztypen=null;

	public LossollmaterialDto[] getErsatztypen() {
		return ersatztypen;
	}

	public void setErsatztypen(LossollmaterialDto[] ersatztypen) {
		this.ersatztypen = ersatztypen;
	}

	public ReportTheoretischeFehlmengenDto(Integer artikelIId) {
		this.artikelIId = artikelIId;
		this.bdMenge = new BigDecimal(0);
		this.bdAusgegebeneMenge = new BigDecimal(0);
	}

	public void addiereZuMenge(BigDecimal bdMenge2Add) {
		bdMenge = bdMenge.add(bdMenge2Add);
	}

	public BigDecimal getMenge() {
		return bdMenge;
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public BigDecimal getBdAusgegebeneMenge() {
		return bdAusgegebeneMenge;
	}

	public void addiereZuAusgegebenerMenge(BigDecimal bdMenge2Add) {
		if (bdMenge2Add != null) {
			bdAusgegebeneMenge = bdAusgegebeneMenge.add(bdMenge2Add);
		}
	}

}

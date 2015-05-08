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
package com.lp.server.fertigung.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class KapazitaetsvorschauDetailDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer artikelgruppeIId = null;
	private Integer maschinengruppeIId = null;
	private Integer lossollarbeitsplanIId = null;
	private Integer losIId = null;
	private Integer artikelIIdTaetigkeit = null;
	private BigDecimal bdDauer = null;

	public Integer getArtikelgruppeIId() {
		return artikelgruppeIId;
	}

	public Integer getArtikelIIdTaetigkeit() {
		return artikelIIdTaetigkeit;
	}

	public BigDecimal getBdDauer() {
		return bdDauer;
	}

	public Integer getLosIId() {
		return losIId;
	}

	public Integer getLossollarbeitsplanIId() {
		return lossollarbeitsplanIId;
	}

	public Integer getMaschinengruppeIId() {
		return maschinengruppeIId;
	}

	public void setArtikelgruppeIId(Integer artikelgruppeIId) {
		this.artikelgruppeIId = artikelgruppeIId;
	}

	public void setArtikelIIdTaetigkeit(Integer artikelIIdTaetigkeit) {
		this.artikelIIdTaetigkeit = artikelIIdTaetigkeit;
	}

	public void setBdDauer(BigDecimal bdDauer) {
		this.bdDauer = bdDauer;
	}

	public void setLosIId(Integer losIId) {
		this.losIId = losIId;
	}

	public void setLossollarbeitsplanIId(Integer lossollarbeitsplanIId) {
		this.lossollarbeitsplanIId = lossollarbeitsplanIId;
	}

	public void setMaschinengruppeIId(Integer maschinengruppeIId) {
		this.maschinengruppeIId = maschinengruppeIId;
	}
}

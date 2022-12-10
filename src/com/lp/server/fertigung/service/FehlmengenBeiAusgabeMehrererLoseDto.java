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

public class FehlmengenBeiAusgabeMehrererLoseDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private String lager;
	private String artikelnummer;
	private String bezeichnung;
	private BigDecimal sollmenge;
	private BigDecimal lagerstand;
	private Integer artikelIId;

	public static final int SPALTE_LAGER = 1;
	public static final int SPALTE_ARTIKEL = 2;
	public static final int SPALTE_BEZEICHNUNG = 3;
	public static final int SPALTE_SOLLMENGE = 4;
	public static final int SPALTE_LAGERSTAND = 5;
	public static final int SPALTE_ARTIKEL_ID = 6;

	public FehlmengenBeiAusgabeMehrererLoseDto(String lager,
			String artikelnummer, String bezeichnung, BigDecimal sollmenge,
			BigDecimal lagerstand, Integer artikelIId) {
		this.lager = lager;
		this.artikelnummer = artikelnummer;
		this.bezeichnung = bezeichnung;
		this.sollmenge = sollmenge;
		this.lagerstand = lagerstand;
		this.artikelIId = artikelIId;

	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public String getLager() {
		return lager;
	}

	public void setLager(String lager) {
		this.lager = lager;
	}

	public String getArtikelnummer() {
		return artikelnummer;
	}

	public void setArtikelnummer(String artikelnummer) {
		this.artikelnummer = artikelnummer;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public BigDecimal getSollmenge() {
		return sollmenge;
	}

	public void setSollmenge(BigDecimal sollmenge) {
		this.sollmenge = sollmenge;
	}

	public BigDecimal getLagerstand() {
		return lagerstand;
	}

	public void setLagerstand(BigDecimal lagerstand) {
		this.lagerstand = lagerstand;
	}

}

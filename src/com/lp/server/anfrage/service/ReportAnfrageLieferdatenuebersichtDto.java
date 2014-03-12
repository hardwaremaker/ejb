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
package com.lp.server.anfrage.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class ReportAnfrageLieferdatenuebersichtDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cNrArtikelcnr;
	private BigDecimal nLiefermenge;
	private String einheitCNr;
	private Integer artikelIId;
	private BigDecimal nLieferpreisInAnfragewaehrung;
	private Integer anfrageIId;
	private Integer iAnlieferzeit;
	private String lieferantName;
	private String anfrageCNr;
	private String anfrageCBez;
	private String waehrungCNr;
	private Double fWechselkursMandantwaehrungZuAnfragewaehrung;

	public String getCNrArtikelcnr() {
		return cNrArtikelcnr;
	}

	public void setCNrArtikelcnr(String cNrArtikelcnr) {
		this.cNrArtikelcnr = cNrArtikelcnr;
	}

	public BigDecimal getNLiefermenge() {
		return nLiefermenge;
	}

	public void setNLiefermenge(BigDecimal nLiefermenge) {
		this.nLiefermenge = nLiefermenge;
	}

	public String getEinheitCNr() {
		return einheitCNr;
	}

	public void setEinheitCNr(String einheitCNr) {
		this.einheitCNr = einheitCNr;
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public BigDecimal getNLieferpreisInAnfragewaehrung() {
		return nLieferpreisInAnfragewaehrung;
	}

	public void setNLieferpreisInAnfragewaehrung(
			BigDecimal nLieferpreisInAnfragewaehrung) {
		this.nLieferpreisInAnfragewaehrung = nLieferpreisInAnfragewaehrung;
	}

	public Integer getAnfrageIId() {
		return anfrageIId;
	}

	public void setAnfrageIId(Integer anfrageIId) {
		this.anfrageIId = anfrageIId;
	}

	public Integer getIAnlieferzeit() {
		return iAnlieferzeit;
	}

	public void setIAnlieferzeit(Integer iAnlieferzeit) {
		this.iAnlieferzeit = iAnlieferzeit;
	}

	public String getLieferantName() {
		return lieferantName;
	}

	public void setLieferantName(String lieferantName) {
		this.lieferantName = lieferantName;
	}

	public String getAnfrageCNr() {
		return anfrageCNr;
	}

	public void setAnfrageCNr(String anfrageCNr) {
		this.anfrageCNr = anfrageCNr;
	}

	public String getAnfrageCBez() {
		return anfrageCBez;
	}

	public void setAnfrageCBez(String anfrageCBezI) {
		this.anfrageCBez = anfrageCBezI;
	}

	public String getWaehrungCNr() {
		return this.waehrungCNr;
	}

	public void setWaehrungCNr(String waehrungCNrI) {
		waehrungCNr = waehrungCNrI;
	}

	public Double getFWechselkursMandantwaehrungZuAnfragewaehrung() {
		return this.fWechselkursMandantwaehrungZuAnfragewaehrung;
	}

	public void setFWechselkursMandantwaehrungZuAnfragewaehrung(
			Double fWechselkursI) {
		this.fWechselkursMandantwaehrungZuAnfragewaehrung = fWechselkursI;
	}
}

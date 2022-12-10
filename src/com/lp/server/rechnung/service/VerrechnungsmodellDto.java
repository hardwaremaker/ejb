package com.lp.server.rechnung.service;

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

import java.io.Serializable;

import javax.persistence.Column;

public class VerrechnungsmodellDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String mandantCNr;
	private String cBez;

	private Short bNachArtikelVerdichten;
	
	
	public Short getBNachArtikelVerdichten() {
		return bNachArtikelVerdichten;
	}

	public void setBNachArtikelVerdichten(Short bNachArtikelVerdichten) {
		this.bNachArtikelVerdichten = bNachArtikelVerdichten;
	}

	
	private Short bPreiseAusAuftrag;

	public Short getBPreiseAusAuftrag() {
		return bPreiseAusAuftrag;
	}

	public void setBPreiseAusAuftrag(Short bPreiseAusAuftrag) {
		this.bPreiseAusAuftrag = bPreiseAusAuftrag;
	}

	private Short bVersteckt;

	public Short getBVersteckt() {
		return this.bVersteckt;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	private Integer artikelIIdReisespesen;
	private Integer artikelIIdReisekilometer;
	private Integer artikelIIdTelefon;
	private Integer artikelIIdEr;

	public Integer getArtikelIIdReisespesen() {
		return artikelIIdReisespesen;
	}

	public void setArtikelIIdReisespesen(Integer artikelIIdReisespesen) {
		this.artikelIIdReisespesen = artikelIIdReisespesen;
	}

	public Integer getArtikelIIdReisekilometer() {
		return artikelIIdReisekilometer;
	}

	public void setArtikelIIdReisekilometer(Integer artikelIIdReisekilometer) {
		this.artikelIIdReisekilometer = artikelIIdReisekilometer;
	}

	public Integer getArtikelIIdTelefon() {
		return artikelIIdTelefon;
	}

	public void setArtikelIIdTelefon(Integer artikelIIdTelefon) {
		this.artikelIIdTelefon = artikelIIdTelefon;
	}

	public Integer getArtikelIIdEr() {
		return artikelIIdEr;
	}

	public void setArtikelIIdEr(Integer artikelIIdEr) {
		this.artikelIIdEr = artikelIIdEr;
	}

	public Double getFAufschlagEr() {
		return fAufschlagEr;
	}

	public void setFAufschlagEr(Double fAufschlagEr) {
		this.fAufschlagEr = fAufschlagEr;
	}

	private Double fAufschlagEr;

}

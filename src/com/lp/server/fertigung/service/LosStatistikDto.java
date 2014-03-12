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

import java.io.Serializable;
import java.math.BigDecimal;

public class LosStatistikDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LosStatistikDto(LosDto losDto) {
		this.losDto = losDto;
	}

	private BigDecimal abgelieferteMenge;

	private boolean bIstPerson = true;

	private Integer maschineIId;

	private LosDto losDto;
	private String artikelnummer;
	private String artikelbezeichnung;
	private String personMaschine;
	private BigDecimal Sollmenge;
	private BigDecimal Sollpreis;
	private BigDecimal Istmenge;
	private BigDecimal Istpreis;
	private String gruppe;
	private boolean bMaterial;
	private BigDecimal vkpreisStueckliste;
	private java.sql.Timestamp buchungszeit;

	public java.sql.Timestamp getBuchungszeit() {
		return buchungszeit;
	}

	public void setBuchungszeit(java.sql.Timestamp buchungszeit) {
		this.buchungszeit = buchungszeit;
	}

	public LosDto getLosDto() {
		return losDto;
	}

	public String getArtikelnummer() {
		return artikelnummer;
	}

	public String getArtikelbezeichnung() {
		return artikelbezeichnung;
	}

	public String getPersonMaschine() {
		return personMaschine;
	}

	public String getGruppe() {
		return gruppe;
	}

	public boolean isBMaterial() {
		return bMaterial;
	}

	public Integer getMaschineIId() {
		return maschineIId;
	}

	public boolean isBIstPerson() {
		return bIstPerson;
	}

	public BigDecimal getAbgelieferteMenge() {
		return abgelieferteMenge;
	}

	public BigDecimal getVkpreisStueckliste() {
		return vkpreisStueckliste;
	}

	public BigDecimal getIstpreis() {
		return Istpreis;
	}

	public BigDecimal getIstmenge() {
		return Istmenge;
	}

	public BigDecimal getSollpreis() {
		return Sollpreis;
	}

	public BigDecimal getSollmenge() {
		return Sollmenge;
	}

	public void setLosDto(LosDto losDto) {
		this.losDto = losDto;
	}

	public void setArtikelnummer(String artikelnummer) {
		this.artikelnummer = artikelnummer;
	}

	public void setArtikelbezeichnung(String artikelbezeichnung) {
		this.artikelbezeichnung = artikelbezeichnung;
	}

	public void setPersonMaschine(String personMaschine) {
		this.personMaschine = personMaschine;
	}

	public void setGruppe(String gruppe) {
		this.gruppe = gruppe;
	}

	public void setBMaterial(boolean bMaterial) {
		this.bMaterial = bMaterial;
	}

	public void setMaschineIId(Integer maschineIId) {
		this.maschineIId = maschineIId;
	}

	public void setBIstPerson(boolean bIstPerson) {
		this.bIstPerson = bIstPerson;
	}

	public void setAbgelieferteMenge(BigDecimal abgelieferteMenge) {
		this.abgelieferteMenge = abgelieferteMenge;
	}

	public void setVkpreisStueckliste(BigDecimal vkpreisStueckliste) {
		this.vkpreisStueckliste = vkpreisStueckliste;
	}

	public void setIstpreis(BigDecimal Istpreis) {
		this.Istpreis = Istpreis;
	}

	public void setIstmenge(BigDecimal Istmenge) {
		this.Istmenge = Istmenge;
	}

	public void setSollpreis(BigDecimal Sollpreis) {
		this.Sollpreis = Sollpreis;
	}

	public void setSollmenge(BigDecimal Sollmenge) {
		this.Sollmenge = Sollmenge;
	}
}

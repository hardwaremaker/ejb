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
import java.sql.Timestamp;
import java.util.Date;


public class RahmenbestelltReportDto implements Serializable {

	private static final long serialVersionUID = 1L;
	BigDecimal preis;
	public BigDecimal getPreis() {
		return preis;
	}
	public void setPreis(BigDecimal preis) {
		this.preis = preis;
	}
	public BigDecimal getRahmenmenge() {
		return rahmenmenge;
	}
	public void setRahmenmenge(BigDecimal rahmenmenge) {
		this.rahmenmenge = rahmenmenge;
	}
	public BigDecimal getOffenmenge() {
		return offenmenge;
	}
	public void setOffenmenge(BigDecimal offenmenge) {
		this.offenmenge = offenmenge;
	}
	public String getBestellnummer() {
		return bestellnummer;
	}
	public void setBestellnummer(String bestellnummer) {
		this.bestellnummer = bestellnummer;
	}
	public String getLieferant() {
		return lieferant;
	}
	public void setLieferant(String lieferant) {
		this.lieferant = lieferant;
	}
	public String getProjekt() {
		return projekt;
	}
	public void setProjekt(String projekt) {
		this.projekt = projekt;
	}
	public String getAbNummer() {
		return abNummer;
	}
	public void setAbNummer(String abNummer) {
		this.abNummer = abNummer;
	}
	public Timestamp getTLiefertermin() {
		return tLiefertermin;
	}
	public void setTLiefertermin(Timestamp tLiefertermin) {
		this.tLiefertermin = tLiefertermin;
	}
	public String getAbKommentar() {
		return abKommentar;
	}
	public void setAbKommentar(String abKommentar) {
		this.abKommentar = abKommentar;
	}
	public Date gettAbTermin() {
		return tAbTermin;
	}
	public void settAbTermin(Date tAbTermin) {
		this.tAbTermin = tAbTermin;
	}
	BigDecimal rahmenmenge;
	BigDecimal offenmenge;
	String bestellnummer;
	String lieferant;
	String projekt;
	String abNummer;
	Timestamp tLiefertermin;
	String abKommentar;
	Date tAbTermin;

}

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
package com.lp.server.artikel.service;


import java.io.Serializable;

import com.lp.util.Helper;


public class ArtikelImportDto implements Serializable {

	

	private static final long serialVersionUID = 1L;
	
	
	private String artikelnummer="";
	private String bezeichnung="";
	private String kurzbezeichnung="";
	private String zusatzbezeichnung="";
	private String zusatzbezeichnung2="";
	private String artikelgruppe="";
	private String artikelklasse="";
	private String artikelart="";
	
	private String vkpreisbasisgueltigab="";
	private String vkpreisbasis="";
	public String getVkpreisbasis() {
		return vkpreisbasis;
	}
	public void setVkpreisbasis(String vkpreisbasis) {
		this.vkpreisbasis = vkpreisbasis;
	}
	private String gueltigabpreisliste1="";
	private String gueltigabpreisliste2="";
	private String gueltigabpreisliste3="";
	
	private String fixpreispreisliste1="";
	private String fixpreispreisliste2="";
	private String fixpreispreisliste3="";
	
	private String revision="";
	private String index="";
	private String chargenbehaftet="";
	private String snrbehaftet="";
	
	public String getRevision() {
		return revision;
	}
	public void setRevision(String revision) {
		this.revision = revision;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getChargenbehaftet() {
		return chargenbehaftet;
	}
	public void setChargenbehaftet(String chargenbehaftet) {
		this.chargenbehaftet = chargenbehaftet;
	}
	public String getSnrbehaftet() {
		return snrbehaftet;
	}
	public void setSnrbehaftet(String snrbehaftet) {
		this.snrbehaftet = snrbehaftet;
	}
	public String getVkpreisbasisgueltigab() {
		return vkpreisbasisgueltigab;
	}
	public void setVkpreisbasisgueltigab(String vkpreisbasisgueltigab) {
		this.vkpreisbasisgueltigab = vkpreisbasisgueltigab;
	}
	public String getGueltigabpreisliste1() {
		return gueltigabpreisliste1;
	}
	public void setGueltigabpreisliste1(String gueltigabpreisliste1) {
		this.gueltigabpreisliste1 = gueltigabpreisliste1;
	}
	public String getGueltigabpreisliste2() {
		return gueltigabpreisliste2;
	}
	public void setGueltigabpreisliste2(String gueltigabpreisliste2) {
		this.gueltigabpreisliste2 = gueltigabpreisliste2;
	}
	public String getGueltigabpreisliste3() {
		return gueltigabpreisliste3;
	}
	public void setGueltigabpreisliste3(String gueltigabpreisliste3) {
		this.gueltigabpreisliste3 = gueltigabpreisliste3;
	}
	public String getFixpreispreisliste1() {
		return fixpreispreisliste1;
	}
	public void setFixpreispreisliste1(String fixpreispreisliste1) {
		this.fixpreispreisliste1 = fixpreispreisliste1;
	}
	public String getFixpreispreisliste2() {
		return fixpreispreisliste2;
	}
	public void setFixpreispreisliste2(String fixpreispreisliste2) {
		this.fixpreispreisliste2 = fixpreispreisliste2;
	}
	public String getFixpreispreisliste3() {
		return fixpreispreisliste3;
	}
	public void setFixpreispreisliste3(String fixpreispreisliste3) {
		this.fixpreispreisliste3 = fixpreispreisliste3;
	}
	public String getRabattsatzpreisliste1() {
		return rabattsatzpreisliste1;
	}
	public void setRabattsatzpreisliste1(String rabattsatzpreisliste1) {
		this.rabattsatzpreisliste1 = rabattsatzpreisliste1;
	}
	public String getRabattsatzpreisliste2() {
		return rabattsatzpreisliste2;
	}
	public void setRabattsatzpreisliste2(String rabattsatzpreisliste2) {
		this.rabattsatzpreisliste2 = rabattsatzpreisliste2;
	}
	public String getRabattsatzpreisliste3() {
		return rabattsatzpreisliste3;
	}
	public void setRabattsatzpreisliste3(String rabattsatzpreisliste3) {
		this.rabattsatzpreisliste3 = rabattsatzpreisliste3;
	}
	private String rabattsatzpreisliste1="";
	private String rabattsatzpreisliste2="";
	private String rabattsatzpreisliste3="";
	
	
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
	public String getKurzbezeichnung() {
		return kurzbezeichnung;
	}
	public void setKurzbezeichnung(String kurzbezeichnung) {
		this.kurzbezeichnung = kurzbezeichnung;
	}
	public String getZusatzbezeichnung() {
		return zusatzbezeichnung;
	}
	public void setZusatzbezeichnung(String zusatzbezeichnung) {
		this.zusatzbezeichnung = zusatzbezeichnung;
	}
	public String getZusatzbezeichnung2() {
		return zusatzbezeichnung2;
	}
	public void setZusatzbezeichnung2(String zusatzbezeichnung2) {
		this.zusatzbezeichnung2 = zusatzbezeichnung2;
	}
	public String getArtikelgruppe() {
		return artikelgruppe;
	}
	public void setArtikelgruppe(String artikelgruppe) {
		this.artikelgruppe = artikelgruppe;
	}
	public String getArtikelklasse() {
		return artikelklasse;
	}
	public void setArtikelklasse(String artikelklasse) {
		this.artikelklasse = artikelklasse;
	}
	public String getArtikelart() {
		return artikelart;
	}
	public void setArtikelart(String artikelart) {
		this.artikelart = artikelart;
	}
	public String getEinheit() {
		return einheit;
	}
	public void setEinheit(String einheit) {
		this.einheit = einheit;
	}
	public String getMwstsatz() {
		return mwstsatz;
	}
	public void setMwstsatz(String mwstsatz) {
		this.mwstsatz = mwstsatz;
	}
	public String getReferenznummer() {
		return referenznummer;
	}
	public void setReferenznummer(String referenznummer) {
		this.referenznummer = referenznummer;
	}
	private String einheit="";
	private String mwstsatz="";
	private String referenznummer="";


	
}

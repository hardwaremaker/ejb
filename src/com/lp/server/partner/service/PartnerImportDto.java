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
package com.lp.server.partner.service;

import java.io.Serializable;

import com.lp.util.Helper;


public class PartnerImportDto implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String anrede="";
	public String getPartnerklasse() {
		return partnerklasse;
	}
	public void setPartnerklasse(String partnerklasse) {
		this.partnerklasse = partnerklasse;
	}
	public String getBranche() {
		return branche;
	}
	public void setBranche(String branche) {
		this.branche = branche;
	}
	public String getIln() {
		return iln;
	}
	public void setIln(String iln) {
		this.iln = iln;
	}
	public String getFilialnr() {
		return filialnr;
	}
	public void setFilialnr(String filialnr) {
		this.filialnr = filialnr;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getAnsprechpartnerDirektfax() {
		return ansprechpartnerDirektfax;
	}
	public void setAnsprechpartnerDirektfax(String ansprechpartnerDirektfax) {
		this.ansprechpartnerDirektfax = ansprechpartnerDirektfax;
	}
	private String titel="";
	private String name1="";
	private String name2="";
	private String name3="";
	
	
	private String partnerklasse="";
	private String branche="";
	private String iln="";
	private String filialnr="";
	private String uid="";
	
	private String strasse="";
	private String land="";
	private String plz="";
	private String ort="";
	private String telefon="";
	private String fax="";
	private String email="";
	private String homepage="";
	private String bemerkung="";
	private String selektion="";
	private String ansprechpartnerFunktion="";
	private String ansprechpartnerAnrede="";
	private String ansprechpartnerTitel="";
	private String ansprechpartnerVorname="";
	private String ansprechpartnerNachname="";
	private String ansprechpartnerTelefonDW="";
	private String ansprechpartnerFaxDW="";
	private String ansprechpartnerEmail="";
	private String ansprechpartnerDirektfax="";
	
	private String gmtversatz="";
	
	
	public String getGmtversatz() {
		return gmtversatz;
	}
	public void setGmtversatz(String gmtversatz) {
		this.gmtversatz = gmtversatz;
	}
	private java.sql.Timestamp geburtsdatumansprechpartner=Helper.cutTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
	private String firmenbuchnummer="";
	private String gerichtsstand="";
	private String postfach="";
	
	
	public java.sql.Timestamp getGeburtsdatumansprechpartner() {
		return geburtsdatumansprechpartner;
	}
	public void setGeburtsdatumansprechpartner(java.sql.Timestamp geburtsdatumansprechpartner) {
		this.geburtsdatumansprechpartner = geburtsdatumansprechpartner;
	}
	public String getFirmenbuchnummer() {
		return firmenbuchnummer;
	}
	public void setFirmenbuchnummer(String firmenbuchnummer) {
		this.firmenbuchnummer = firmenbuchnummer;
	}
	public String getGerichtsstand() {
		return gerichtsstand;
	}
	public void setGerichtsstand(String gerichtsstand) {
		this.gerichtsstand = gerichtsstand;
	}
	public String getPostfach() {
		return postfach;
	}
	public void setPostfach(String postfach) {
		this.postfach = postfach;
	}
	public String getAnsprechpartnerEmail() {
		return ansprechpartnerEmail;
	}
	public void setAnsprechpartnerEmail(String ansprechpartnerEmail) {
		this.ansprechpartnerEmail = ansprechpartnerEmail;
	}
	private String ansprechpartnerMobil="";
	private java.sql.Timestamp ansprechpartnerGueltigab=Helper.cutTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
	private String ansprechpartnerBemerkung="";
	public String getAnrede() {
		return anrede;
	}
	public void setAnrede(String anrede) {
		this.anrede = anrede;
	}
	public String getTitel() {
		return titel;
	}
	public void setTitel(String titel) {
		this.titel = titel;
	}
	public String getName1() {
		return name1;
	}
	public void setName1(String name1) {
		this.name1 = name1;
	}
	public String getName2() {
		return name2;
	}
	public void setName2(String name2) {
		this.name2 = name2;
	}
	public String getName3() {
		return name3;
	}
	public void setName3(String name3) {
		this.name3 = name3;
	}
	public String getStrasse() {
		return strasse;
	}
	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}
	public String getLand() {
		return land;
	}
	public void setLand(String land) {
		this.land = land;
	}
	public String getPlz() {
		return plz;
	}
	public void setPlz(String plz) {
		this.plz = plz;
	}
	public String getOrt() {
		return ort;
	}
	public void setOrt(String ort) {
		this.ort = ort;
	}
	public String getTelefon() {
		return telefon;
	}
	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getHomepage() {
		return homepage;
	}
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
	public String getBemerkung() {
		return bemerkung;
	}
	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}
	public String getSelektion() {
		return selektion;
	}
	public void setSelektion(String selektion) {
		this.selektion = selektion;
	}
	public String getAnsprechpartnerFunktion() {
		return ansprechpartnerFunktion;
	}
	public void setAnsprechpartnerFunktion(String ansprechpartnerFunktion) {
		this.ansprechpartnerFunktion = ansprechpartnerFunktion;
	}
	public String getAnsprechpartnerAnrede() {
		return ansprechpartnerAnrede;
	}
	public void setAnsprechpartnerAnrede(String ansprechpartnerAnrede) {
		this.ansprechpartnerAnrede = ansprechpartnerAnrede;
	}
	public String getAnsprechpartnerTitel() {
		return ansprechpartnerTitel;
	}
	public void setAnsprechpartnerTitel(String ansprechpartnerTitel) {
		this.ansprechpartnerTitel = ansprechpartnerTitel;
	}
	public String getAnsprechpartnerVorname() {
		return ansprechpartnerVorname;
	}
	public void setAnsprechpartnerVorname(String ansprechpartnerVorname) {
		this.ansprechpartnerVorname = ansprechpartnerVorname;
	}
	public String getAnsprechpartnerNachname() {
		return ansprechpartnerNachname;
	}
	public void setAnsprechpartnerNachname(String ansprechpartnerNachname) {
		this.ansprechpartnerNachname = ansprechpartnerNachname;
	}
	public String getAnsprechpartnerTelefonDW() {
		return ansprechpartnerTelefonDW;
	}
	public void setAnsprechpartnerTelefonDW(String ansprechpartnerTelefonDW) {
		this.ansprechpartnerTelefonDW = ansprechpartnerTelefonDW;
	}
	public String getAnsprechpartnerFaxDW() {
		return ansprechpartnerFaxDW;
	}
	public void setAnsprechpartnerFaxDW(String ansprechpartnerFaxDW) {
		this.ansprechpartnerFaxDW = ansprechpartnerFaxDW;
	}
	public String getAnsprechpartnerMobil() {
		return ansprechpartnerMobil;
	}
	public void setAnsprechpartnerMobil(String ansprechpartnerMobil) {
		this.ansprechpartnerMobil = ansprechpartnerMobil;
	}
	public java.sql.Timestamp getAnsprechpartnerGueltigab() {
		return ansprechpartnerGueltigab;
	}
	public void setAnsprechpartnerGueltigab(
			java.sql.Timestamp ansprechpartnerGueltigab) {
		this.ansprechpartnerGueltigab = ansprechpartnerGueltigab;
	}
	public String getAnsprechpartnerBemerkung() {
		return ansprechpartnerBemerkung;
	}
	public void setAnsprechpartnerBemerkung(String ansprechpartnerBemerkung) {
		this.ansprechpartnerBemerkung = ansprechpartnerBemerkung;
	}
	
}

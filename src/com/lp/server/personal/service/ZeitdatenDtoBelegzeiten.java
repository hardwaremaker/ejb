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
package com.lp.server.personal.service;

public class ZeitdatenDtoBelegzeiten {
	private static final long serialVersionUID = 1L;

	private Integer arbeitsgang=null;
	private Integer unterarbeitsgang=null;
	private String sArtikelgruppe=null;
	
	public String getSArtikelgruppe() {
		return sArtikelgruppe;
	}

	public void setSArtikelgruppe(String sArtikelgruppe) {
		this.sArtikelgruppe = sArtikelgruppe;
	}

	public Integer getArbeitsgang() {
		return arbeitsgang;
	}

	public void setArbeitsgang(Integer arbeitsgang) {
		this.arbeitsgang = arbeitsgang;
	}

	public Integer getUnterarbeitsgang() {
		return unterarbeitsgang;
	}

	public void setUnterarbeitsgang(Integer unterarbeitsgang) {
		this.unterarbeitsgang = unterarbeitsgang;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getArtikel() {
		return artikel;
	}

	public void setArtikel(String artikel) {
		this.artikel = artikel;
	}

	private String sPersonalKurzzeichen;
	
	public String getSPersonalKurzzeichen() {
		return sPersonalKurzzeichen;
	}

	public void setSPersonalKurzzeichen(String sPersonalKurzzeichen) {
		this.sPersonalKurzzeichen = sPersonalKurzzeichen;
	}
	
	public String getPersonalnummer() {
		return personalnummer;
	}

	public void setPersonalnummer(String personalnummer) {
		this.personalnummer = personalnummer;
	}
	private String personalnummer=null;
	private String person = "";
	private String personNachnameVorname = "";
	public String getPersonNachnameVorname() {
		return personNachnameVorname;
	}

	public void setPersonNachnameVorname(String personNachnameVorname) {
		this.personNachnameVorname = personNachnameVorname;
	}
	private boolean bOffen=false;
	public boolean isbOffen() {
		return bOffen;
	}

	public void setbOffen(boolean bOffen) {
		this.bOffen = bOffen;
	}
	private String artikel = "";
	private String bezeichnung="";
	public String getBezeichnung() {
		return bezeichnung;
	}
	private String zusatzbezeichnung="";
	public String getZusatzbezeichnung() {
		return zusatzbezeichnung;
	}

	public void setZusatzbezeichnung(String zusatzbezeichnung) {
		this.zusatzbezeichnung = zusatzbezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	private ZeitdatenDto zeitdatenDto=null;
	private Integer artklaIId=null;
	public Integer getArtklaIId() {
		return artklaIId;
	}

	public void setArtklaIId(Integer artklaIId) {
		this.artklaIId = artklaIId;
	}

	public Integer getArtgruIId() {
		return artgruIId;
	}

	public void setArtgruIId(Integer artgruIId) {
		this.artgruIId = artgruIId;
	}
	private Integer artgruIId=null;
	
	public ZeitdatenDto getZeitdatenDto() {
		return zeitdatenDto;
	}

	public void setZeitdatenDto(ZeitdatenDto zeitdatenDto) {
		this.zeitdatenDto = zeitdatenDto;
	}

}

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
package com.lp.server.angebotstkl.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class ImportLumiQuoteXlsxDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	

	private String position="";
	
	
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	private String bezeichnung=null;
	
	public String getBezeichnung() {
		return bezeichnung;
	}
	public void setBezeichnung(String bezeichnung) {
		if(bezeichnung !=null && bezeichnung.length()>80) {
			this.bezeichnung = bezeichnung.substring(0, 80);
		}else {
			this.bezeichnung = bezeichnung;
		}
	}
	private String lieferadresse;

	
	public String getLieferadresse() {
		return lieferadresse;
	}
	public void setLieferadresse(String lieferadresse) {
		this.lieferadresse = lieferadresse;
	}
	private String bemerkungLF = null;
	public String getBemerkungLF() {
		return bemerkungLF;
	}
	public void setBemerkungLF(String bemerkungLF) {
		this.bemerkungLF = bemerkungLF;
	}
	public BigDecimal getLagerstandLF() {
		return lagerstandLF;
	}
	public void setLagerstandLF(BigDecimal lagerstandLF) {
		this.lagerstandLF = lagerstandLF;
	}
	private BigDecimal lagerstandLF = null;
	
	private String lf_artikelnummer = null;
	
	public String getLf_artikelnummer() {
		return lf_artikelnummer;
	}
	public void setLf_artikelnummer(String lf_artikelnummer) {
		this.lf_artikelnummer = lf_artikelnummer;
	}
	private String hv_artikelnummer = null;
	public String getHVArtikelnummer() {
		return hv_artikelnummer;
	}
	public void setHVArtikelnummer(String hv_artikelnummer) {
		this.hv_artikelnummer = hv_artikelnummer;
	}

	
	private int iLieferzeitInTagen;
	public int getiLieferzeitInTagen() {
		return iLieferzeitInTagen;
	}
	public void setiLieferzeitInTagen(int iLieferzeitInTagen) {
		this.iLieferzeitInTagen = iLieferzeitInTagen;
	}
	
	public String getHerstellernummer() {
		return herstellernummer;
	}
	public void setHerstellernummer(String herstellernummer) {
		this.herstellernummer = herstellernummer;
	}

	private String herstellernummer;
	
	

	public BigDecimal getMenge() {
		return menge;
	}
	public void setMenge(BigDecimal menge) {
		this.menge = menge;
	}
	public BigDecimal getPreis() {
		return preis;
	}
	public void setPreis(BigDecimal preis) {
		this.preis = preis;
	}
	
	private BigDecimal menge = null;
	private BigDecimal preis = null;
	

}

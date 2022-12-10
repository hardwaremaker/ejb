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

public class BelegInfos implements Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Double verleihfaktor;
	private Integer verleihtage;
	
	private BigDecimal bdMaterialzuschlag;
	
	public BigDecimal getBdMaterialzuschlag() {
		return bdMaterialzuschlag;
	}

	public void setBdMaterialzuschlag(BigDecimal bdMaterialzuschlag) {
		this.bdMaterialzuschlag = bdMaterialzuschlag;
	}

	public Double getVerleihfaktor() {
		return verleihfaktor;
	}

	public void setVerleihfaktor(Double verleihfaktor) {
		this.verleihfaktor = verleihfaktor;
	}

	public Integer getVerleihtage() {
		return verleihtage;
	}

	public void setVerleihtage(Integer verleihtage) {
		this.verleihtage = verleihtage;
	}

	private String zusatz;
	
	public String getZusatz() {
		return zusatz;
	}

	public void setZusatz(String zusatz) {
		this.zusatz = zusatz;
	}

	private Integer belegartpositionIId;
	
	public Integer getBelegartpositionIId() {
		return belegartpositionIId;
	}

	public void setBelegartpositionIId(Integer belegartpositionIId) {
		this.belegartpositionIId = belegartpositionIId;
	}

	private String belegart;
	public String getBelegart() {
		return belegart;
	}

	public void setBelegart(String belegart) {
		this.belegart = belegart;
	}

	private String belegnummer;
	private String kundeLieferant;
	private String Belegbezeichnung;

	private String mandantCNr;
	
	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getBelegnummer() {
		return belegnummer;
	}

	private Timestamp belegdatum;
	private Integer partnerIId;

	public void setKundeLieferant(String kundeLieferant) {
		this.kundeLieferant = kundeLieferant;
	}

	public void setBelegdatum(Timestamp belegdatum) {
		this.belegdatum = belegdatum;
	}

	public void setBelegnummer(String belegnummer) {
		this.belegnummer = belegnummer;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	public void setBelegbezeichnung(String Belegbezeichnung) {
		this.Belegbezeichnung = Belegbezeichnung;
	}

	public String formatBelegnummerBezeichnung() {
		String s = getBelegnummer();
		if (getBelegbezeichnung() != null) {
			s += " " + getBelegbezeichnung();
		}
		return s;
	}

	public String getKundeLieferant() {
		return kundeLieferant;
	}

	public Timestamp getBelegdatum() {
		return belegdatum;
	}

	public Integer getPartnerIId() {
		return partnerIId;
	}

	public String getBelegbezeichnung() {
		return Belegbezeichnung;
	}

}

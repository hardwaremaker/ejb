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
package com.lp.server.finanz.service;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich um die Temporaere Speicherung von Intrastat-Daten
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur GmbH (c) 2004 - 2007
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 29.08.07
 * </p>
 * 
 * <p>
 * 
 * @author $Author: sebastian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2008/10/14 14:07:18 $
 */
public class IntrastatDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WarenverkehrsnummerDto warenverkehrsnummerDto = null;
	private String beistell = null;
	private BigDecimal menge = null;
	private BigDecimal statistischerWert = null;
	private BigDecimal wert = null;
	private String verfahren = null;
	private int verkehrszweig = FinanzReportFac.INTRASTAT_VERKEHRSZWEIG;
	private PartnerDto partnerDto = null;
	private ArtikelDto artikelDto = null;
	private String belegart = null;
	private String belegnummer = null;
	private BigDecimal einzelpreis = null;
	private BigDecimal bdGewicht = null;

	public String getBeistell() {
		return beistell;
	}

	/*public BigDecimal getGewichtInKg() {
		BigDecimal bdGewicht;
		if (getArtikelDto().getFGewichtkg() != null) {
			bdGewicht = menge.multiply(new BigDecimal(getArtikelDto().getFGewichtkg()));
		} else {
			bdGewicht = new BigDecimal(0);
		}
		return bdGewicht;
	}*/
	
	public void setGewichtInKg(BigDecimal gewicht){
		this.bdGewicht = gewicht;
	}
	
	public BigDecimal getGewichtInKg(){
		return bdGewicht;
	}

	public BigDecimal getMenge() {
		return menge;
	}

	public BigDecimal getStatistischerWert() {
		return statistischerWert;
	}

	public String getText() {
		return warenverkehrsnummerDto.getCBez();
	}
	

	public String getUid() {
		String sUID = "";
		if (getPartnerDto() != null && getPartnerDto().getCUid() != null) {
			sUID = getPartnerDto().getCUid();
		}
		return sUID;
	}

	public String getUrsprung() {
		return Helper.getAllStartCharacters(getUid()); // gleich, da nichts anderes feststellbar
	}

	public String getVerfahren() {
		return verfahren;
	}

	public int getVerkehrszweig() {
		return verkehrszweig;
	}

	public WarenverkehrsnummerDto getWarenverkehrsnummerDto() {
		return warenverkehrsnummerDto;
	}

	public BigDecimal getWert() {
		return wert;
	}

	public PartnerDto getPartnerDto() {
		return partnerDto;
	}

	public ArtikelDto getArtikelDto() {
		return artikelDto;
	}

	public String getBelegart() {
		return belegart;
	}

	public String getBelegnummer() {
		return belegnummer;
	}

	public BigDecimal getEinzelpreis() {
		return einzelpreis;
	}

	public void setBeistell(String beistell) {
		this.beistell = beistell;
	}

	public void setMenge(BigDecimal menge) {
		this.menge = menge;
	}

	public void setStatistischerWert(BigDecimal statistischerWert) {
		this.statistischerWert = statistischerWert;
	}

	public void setWert(BigDecimal wert) {
		this.wert = wert;
	}

	public void setVerfahren(String verfahren) {
		this.verfahren = verfahren;
	}

	public void setWarenverkehrsnummerDto(
			WarenverkehrsnummerDto warenverkehrsnummerDto) {
		this.warenverkehrsnummerDto = warenverkehrsnummerDto;
	}

	public void setPartnerDto(PartnerDto partnerDto) {
		this.partnerDto = partnerDto;
	}

	public void setArtikelDto(ArtikelDto artikelDto) {
		this.artikelDto = artikelDto;
	}

	public void setBelegart(String belegart) {
		this.belegart = belegart;
	}

	public void setBelegnummer(String belegnummer) {
		this.belegnummer = belegnummer;
	}

	public void setEinzelpreis(BigDecimal einzelpreis) {
		this.einzelpreis = einzelpreis;
	}
}

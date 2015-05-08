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
package com.lp.server.util;

import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * <p>
 * Diese Klasse repraesentiert ein zum Drucken fertiges Belegpositions-Objekt
 * der Positionsart Ident / Handeingabe
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur GmbH (c) 2004 - 2007
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 11.07.07
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2010/11/05 07:15:08 $
 */
public class BelegPositionDruckIdentDto {
	private String sArtikelInfo = null;
	private String sIdentnummer = null;
	private String sWarenverkehrsnummer = null;
	private String sBezeichnung = null;
	private String sZusatzBezeichnung = null;
	private String sKurzbezeichnung = null;
	private String sVerkaufseannr = null;
	private String sVerpackungseannr = null;
	private String sReferenznr = null;
	private String sArtikelkommentar = null;
	private String sArtikelZusatzBezeichnung2 = null;
	private String sIdentTexteingabe = null;

	private BufferedImage oImageKommentar = null;

	public BufferedImage getOImageKommentar() {
		return oImageKommentar;
	}

	public String getSArtikelInfo() {
		return sArtikelInfo;
	}

	public String getSIdentnummer() {
		return sIdentnummer;
	}

	public String getSWarenverkehrsnummer() {
		return sWarenverkehrsnummer;
	}

	public String getSBezeichnung() {
		return sBezeichnung;
	}

	public String getSZusatzBezeichnung() {
		return sZusatzBezeichnung;
	}

	public String getSKurzbezeichnung() {
		return sKurzbezeichnung;
	}

	public String getSVerkaufseannr() {
		return sVerkaufseannr;
	}

	public String getSVerpackungseannr() {
		return sVerpackungseannr;
	}

	public String getSReferenznr() {
		return sReferenznr;
	}

	public String getSArtikelkommentar() {
		return sArtikelkommentar;
	}

	public String getSArtikelZusatzBezeichnung2() {
		return sArtikelZusatzBezeichnung2;
	}

	public void setOImageKommentar(BufferedImage oImageKommentar) {
		this.oImageKommentar = oImageKommentar;
	}

	public void setSArtikelInfo(String sArtikelInfo) {
		this.sArtikelInfo = sArtikelInfo;
	}

	public void setSIdentnummer(String sIdentnummer) {
		this.sIdentnummer = sIdentnummer;
	}

	public void setSWarenverkehrsnummer(String sWarenverkehrsnummer) {
		this.sWarenverkehrsnummer = sWarenverkehrsnummer;
	}

	public void setSBezeichnung(String sBezeichnung) {
		this.sBezeichnung = sBezeichnung;
	}

	public void setSZusatzBezeichnung(String sZusatzBezeichnung) {
		this.sZusatzBezeichnung = sZusatzBezeichnung;
	}

	public void setSKurzbezeichnung(String sKurzbezeichnung) {
		this.sKurzbezeichnung = sKurzbezeichnung;
	}

	public void setSVerkaufseannr(String sVerkaufseannr) {
		this.sVerkaufseannr = sVerkaufseannr;
	}

	public void setSVerpackungseannr(String sVerpackungseannr) {
		this.sVerpackungseannr = sVerpackungseannr;
	}

	public void setSReferenznr(String cReferenznr) {
		this.sReferenznr = cReferenznr;
	}

	public void setSArtikelkommentar(String sArtikelkommentar) {
		this.sArtikelkommentar = sArtikelkommentar;
	}

	public void setSArtikelZusatzBezeichnung2(String sArtikelZusatzBezeichnung2) {
		this.sArtikelZusatzBezeichnung2 = sArtikelZusatzBezeichnung2;
	}
	
	public String getSIdentTexteingabe() {
		return sIdentTexteingabe;
	}
	public void setSIdentTexteingabe(String sIdentTexteingabe) {
		this.sIdentTexteingabe = sIdentTexteingabe;
	}
	
	String sEccn;

	public String getSEccn() {
		return sEccn;
	}

	public void setSEccn(String sEccn) {
		this.sEccn = sEccn;
	}

	
}

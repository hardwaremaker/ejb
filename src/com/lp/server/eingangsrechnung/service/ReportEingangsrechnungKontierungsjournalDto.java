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
package com.lp.server.eingangsrechnung.service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * Diese Klasse kuemmert sich um ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Vorname Nachname; dd.mm.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2013/01/28 09:53:48 $
 */
public class ReportEingangsrechnungKontierungsjournalDto {
	private String sKostenstellenummer = null;
	private String sKostenstelleBezeichnung = null;
	private String sKontonummer = null;
	private String sKontobezeichnung = null;
	private String sEingangsrechnungsnummer = null;
	private java.util.Date dEingangsrechnungsdatum = null;
	private String sLieferant = null;
	private String sPartnerartLieferant = null;
	private String sArt;
	public String getSPartnerartLieferant() {
		return sPartnerartLieferant;
	}

	public void setSPartnerartLieferant(String sPartnerartLieferant) {
		this.sPartnerartLieferant = sPartnerartLieferant;
	}

	private String sSteuerkategorie = null;
	
	public String getSSteuerkategorie() {
		return sSteuerkategorie;
	}

	public void setSSteuerkategorie(String sSteuerkategorie) {
		this.sSteuerkategorie = sSteuerkategorie;
	}

	private String sUVAArt = null;
	
	public String getSUVAArt() {
		return sUVAArt;
	}

	public void setSUVAArt(String sUVAArt) {
		this.sUVAArt = sUVAArt;
	}


	private String sEingangsrechnungText = null;
	private String sEingangsrechnungWeartikel = null;
	
	public String getSEingangsrechnungWeartikel() {
		return sEingangsrechnungWeartikel;
	}

	public void setSEingangsrechnungWeartikel(String sEingangsrechnungWeartikel) {
		this.sEingangsrechnungWeartikel = sEingangsrechnungWeartikel;
	}

	private String waehrungCNr = null;
	
	public String getWaehrungCNr() {
		return waehrungCNr;
	}

	public void setWaehrungCNr(String waehrungCNr) {
		this.waehrungCNr = waehrungCNr;
	}

	private BigDecimal bdWertFW = null;
	private BigDecimal bdUstFW = null;
	private BigDecimal bdBezahltFW = null;
	
	public BigDecimal getBdWertFW() {
		return bdWertFW;
	}

	public void setBdWertFW(BigDecimal bdWertFW) {
		this.bdWertFW = bdWertFW;
	}

	public BigDecimal getBdUstFW() {
		return bdUstFW;
	}

	public void setBdUstFW(BigDecimal bdUstFW) {
		this.bdUstFW = bdUstFW;
	}

	public BigDecimal getBdBezahltFW() {
		return bdBezahltFW;
	}

	public void setBdBezahltFW(BigDecimal bdBezahltFW) {
		this.bdBezahltFW = bdBezahltFW;
	}

	private BigDecimal bdWert = null;
	private BigDecimal bdUst = null;
	private BigDecimal bdBezahlt = null;
	private BigDecimal bdBezahltzuERKurs = null;
	private String sKontoart = null;
	private java.util.Date dLetzesZahldatum = null;
	private BigDecimal bdErrechneterSteuersatz = null;
	private String sLieferantenrechnungsnummer = null;
	private java.util.Date dFreigabedatum = null;
	private String sKreditorennummer = null;
	private BigDecimal bdERKurs = null;

	
	public BigDecimal getBdERKurs() {
		return bdERKurs;
	}

	public void setBdERKurs(BigDecimal bdERKurs) {
		this.bdERKurs = bdERKurs;
	}

	public BigDecimal getBdBezahltzuERKurs() {
		return bdBezahltzuERKurs;
	}

	public void setBdBezahltzuERKurs(BigDecimal bdBezahltzuERKurs) {
		this.bdBezahltzuERKurs = bdBezahltzuERKurs;
	}
	
	public BigDecimal getBdBezahlt() {
		return bdBezahlt;
	}

	public BigDecimal getBdUst() {
		return bdUst;
	}

	public Date getDEingangsrechnungsdatum() {
		return dEingangsrechnungsdatum;
	}

	public BigDecimal getBdWert() {
		return bdWert;
	}

	public String getSEingangsrechnungsnummer() {
		return sEingangsrechnungsnummer;
	}

	public String getSEingangsrechnungText() {
		return sEingangsrechnungText;
	}

	public String getSKontobezeichnung() {
		return sKontobezeichnung;
	}

	public String getSKontonummer() {
		return sKontonummer;
	}

	public String getSKostenstellenummer() {
		return sKostenstellenummer;
	}

	public String getSKostenstelleBezeichnung() {
		return sKostenstelleBezeichnung;
	}

	public String getSLieferant() {
		return sLieferant;
	}

	public void setBdBezahlt(BigDecimal bdBezahlt) {
		this.bdBezahlt = bdBezahlt;
	}

	public void setBdUst(BigDecimal bdUst) {
		this.bdUst = bdUst;
	}

	public void setBdWert(BigDecimal bdWert) {
		this.bdWert = bdWert;
	}

	public void setDEingangsrechnungsdatum(Date dEingangsrechnungsdatum) {
		this.dEingangsrechnungsdatum = dEingangsrechnungsdatum;
	}

	public void setSEingangsrechnungsnummer(String sEingangsrechnungsnummer) {
		this.sEingangsrechnungsnummer = sEingangsrechnungsnummer;
	}

	public void setSEingangsrechnungText(String sEingangsrechnungText) {
		this.sEingangsrechnungText = sEingangsrechnungText;
	}

	public void setSKontobezeichnung(String sKontobezeichnung) {
		this.sKontobezeichnung = sKontobezeichnung;
	}

	public void setSKontonummer(String sKontonummer) {
		this.sKontonummer = sKontonummer;
	}

	public void setSKostenstelleBezeichnung(String sKostenstelleBezeichnung) {
		this.sKostenstelleBezeichnung = sKostenstelleBezeichnung;
	}

	public void setSKostenstellenummer(String sKostenstellenummer) {
		this.sKostenstellenummer = sKostenstellenummer;
	}

	public void setSLieferant(String sLieferant) {
		this.sLieferant = sLieferant;
	}

	public void setSKontoart(String sKontoart) {
		this.sKontoart = sKontoart;
	}

	public void setDLetzesZahldatum(Date dLetzesZahldatum) {
		this.dLetzesZahldatum = dLetzesZahldatum;
	}

	public void setBdErrechneterSteuersatz(BigDecimal bdErrechneterSteuersatz) {
		this.bdErrechneterSteuersatz = bdErrechneterSteuersatz;
	}

	public void setSLieferantenrechnungsnummer(
			String sLieferantenrechnungsnummer) {
		this.sLieferantenrechnungsnummer = sLieferantenrechnungsnummer;
	}

	public void setDFreigabedatum(Date dFreigabedatum) {
		this.dFreigabedatum = dFreigabedatum;
	}

	public String getSKontoart() {
		return sKontoart;
	}

	public Date getDLetzesZahldatum() {
		return dLetzesZahldatum;
	}

	public BigDecimal getBdErrechneterSteuersatz() {
		return bdErrechneterSteuersatz;
	}

	public String getSLieferantenrechnungsnummer() {
		return sLieferantenrechnungsnummer;
	}

	public Date getDFreigabedatum() {
		return dFreigabedatum;
	}

	public String getSKreditorennummer() {
		return this.sKreditorennummer;
	}

	public void setSKreditorennummer(String sKreditorennummer) {
		this.sKreditorennummer = sKreditorennummer;
	}

	public String getSArt() {
		return sArt;
	}

	public void setSArt(String sArt) {
		this.sArt = sArt;
	}
	

}

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
package com.lp.server.partner.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;

/**
 * <p>
 * Diese Klasse kuemmert sich um ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
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
 * @version not attributable Date $Date: 2013/02/07 10:14:30 $
 */
public class KundeLieferstatistikDto {
	public final static int REPORT_STATISTIK_RECHNUNGSNUMMER = 0;
	public final static int REPORT_STATISTIK_LIEFERSCHEINNUMMER = 1;
	public final static int REPORT_STATISTIK_DATUM = 2;
	public final static int REPORT_STATISTIK_IDENT = 3;
	public final static int REPORT_STATISTIK_BEZEICHNUNG = 4;
	public final static int REPORT_STATISTIK_MENGE = 5;
	public final static int REPORT_STATISTIK_PREIS = 6;
	public final static int REPORT_STATISTIK_SERIENNUMMER = 7;
	public final static int REPORT_STATISTIK_SETARTIKEL_TYP = 8;
	public final static int REPORT_STATISTIK_MATERIALZUSCHLAG = 9;

	private String sRechnungsnummer = null;
	private String sLieferscheinnummer = null;
	private Date dWarenausgangsdatum = null;
	private String sIdent = "";
	private String sBezeichnung = null;
	private BigDecimal nMenge = null;
	private BigDecimal nPreis = null;
	private BigDecimal nMaterialzuschlag = null;
	
	private String sWarenausgangverursacher = null;
	private BigDecimal nMwstsatz = null;
	private String sKonto;
	private String sKundenname;
	private String sVertreter;
	private String sProvisionsempfaenger;
	private String sStatistikadresse;
	private String sLand;
	private String sPlz;
	private String sArtikelklasse;
	private String sArtikelgruppe;
	private String sOrt;
	private String sOrtStatistikadresse;
	private String sPlzStatistikadresse;
	private String sLandStatistikadresse;
	private String sSetartikelTyp;
	private BigDecimal nWert ;
	
	public String getSSetartikelTyp() {
		return sSetartikelTyp;
	}

	public void setSSetartikelTyp(String sSetartikelTyp) {
		this.sSetartikelTyp = sSetartikelTyp;
	}

	private Integer iVerleihtage;
	private Double dVerleihfaktor;
	private List<SeriennrChargennrMitMengeDto> snrs;
	
	public List<SeriennrChargennrMitMengeDto> getSnrs() {
		return snrs;
	}

	public void setSnrs(List<SeriennrChargennrMitMengeDto> snrs) {
		this.snrs = snrs;
	}

	public Integer getiVerleihtage() {
		return iVerleihtage;
	}

	public void setiVerleihtage(Integer iVerleihtage) {
		this.iVerleihtage = iVerleihtage;
	}

	public Double getdVerleihfaktor() {
		return dVerleihfaktor;
	}

	public void setdVerleihfaktor(Double dVerleihfaktor) {
		this.dVerleihfaktor = dVerleihfaktor;
	}

	public Date getDWarenausgangsdatum() {
		return dWarenausgangsdatum;
	}

	public BigDecimal getNMenge() {
		return nMenge;
	}

	public BigDecimal getNPreis() {
		return nPreis;
	}

	public String getSBezeichnung() {
		return sBezeichnung;
	}

	public String getSIdent() {
		return sIdent;
	}

	public String getSLieferscheinnummer() {
		return sLieferscheinnummer;
	}

	public String getSRechnungsnummer() {
		return sRechnungsnummer;
	}

	public String getSWarenausgangverursacher() {
		return sWarenausgangverursacher;
	}

	public BigDecimal getNMwstsatz() {
		return nMwstsatz;
	}

	public String getSKonto() {
		return sKonto;
	}

	public String getSKundenname() {
		return sKundenname;
	}

	public String getSVertreter() {
		return sVertreter;
	}

	public String getSProvisionsempfaenger() {
		return sProvisionsempfaenger;
	}

	public String getSStatistikadresse() {
		return sStatistikadresse;
	}

	public String getSLand() {
		return sLand;
	}

	public String getSPlz() {
		return sPlz;
	}

	public String getSArtikelklasse() {
		return sArtikelklasse;
	}

	public String getSArtikelgruppe() {
		return sArtikelgruppe;
	}

	public String getSOrt() {
		return sOrt;
	}

	public String getSOrtStatistikadresse() {
		return sOrtStatistikadresse;
	}

	public String getSPlzStatistikadresse() {
		return sPlzStatistikadresse;
	}

	public String getSLandStatistikadresse() {
		return sLandStatistikadresse;
	}

	public void setDWarenausgangsdatum(Date dWarenausgangsdatum) {
		this.dWarenausgangsdatum = dWarenausgangsdatum;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public void setNPreis(BigDecimal nPreis) {
		this.nPreis = nPreis;
	}

	public void setSBezeichnung(String sBezeichnung) {
		this.sBezeichnung = sBezeichnung;
	}

	public void setSIdent(String sIdent) {
		this.sIdent = sIdent;
	}

	public void setSLieferscheinnummer(String sLieferscheinnummer) {
		this.sLieferscheinnummer = sLieferscheinnummer;
	}

	public void setSRechnungsnummer(String sRechnungsnummer) {
		this.sRechnungsnummer = sRechnungsnummer;
	}

	public void setSWarenausgangverursacher(String sWarenausgangverursacher) {
		this.sWarenausgangverursacher = sWarenausgangverursacher;
	}

	public void setNMwstsatz(BigDecimal nMwstsatz) {
		this.nMwstsatz = nMwstsatz;
	}

	public void setSKonto(String sKonto) {
		this.sKonto = sKonto;
	}

	public void setSKundenname(String sKundenname) {
		this.sKundenname = sKundenname;
	}

	public void setSVertreter(String sVertreter) {
		this.sVertreter = sVertreter;
	}

	public void setSProvisionsempfaenger(String sProvisionsempfaenger) {
		this.sProvisionsempfaenger = sProvisionsempfaenger;
	}

	public void setSStatistikadresse(String sStatistikadresse) {
		this.sStatistikadresse = sStatistikadresse;
	}

	public void setSLand(String sLand) {
		this.sLand = sLand;
	}

	public void setSPlz(String sPlz) {
		this.sPlz = sPlz;
	}

	public void setSArtikelklasse(String sArtikelklasse) {
		this.sArtikelklasse = sArtikelklasse;
	}

	public void setSArtikelgruppe(String sArtikelgruppe) {
		this.sArtikelgruppe = sArtikelgruppe;
	}

	public void setSOrt(String sOrt) {
		this.sOrt = sOrt;
	}

	public void setSOrtStatistikadresse(String sOrtStatistikadresse) {
		this.sOrtStatistikadresse = sOrtStatistikadresse;
	}

	public void setSPlzStatistikadresse(String sPlzStatistikadresse) {
		this.sPlzStatistikadresse = sPlzStatistikadresse;
	}

	public void setSLandStatistikadresse(String sLandStatistikadresse) {
		this.sLandStatistikadresse = sLandStatistikadresse;
	}

	public BigDecimal getNMaterialzuschlag() {
		return nMaterialzuschlag;
	}

	public void setNMaterialzuschlag(BigDecimal nMaterialzuschlag) {
		this.nMaterialzuschlag = nMaterialzuschlag;
	}

	public BigDecimal getNWert() {
		return nWert;
	}

	public void setNWert(BigDecimal nWert) {
		this.nWert = nWert;
	}


}

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
package com.lp.server.angebot.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class ReportAngebotpositionJournalDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Kopfdaten Angebot
	private Integer iId = null;
	private String cNr = null;
	private String kundeCName1 = null;
	private String kundeCName2 = null;
	private String kundeCName3 = null;
	private String kundeStrasse = null;
	private String kundeLkz = null;
	private String kundePlz = null;
	private String kundeOrt = null;
	
	
	private String ansprechpartnerVorname = null;
	private String ansprechpartnerNachname = null;
	private String ansprechpartnerTitel = null;
	private String ansprechpartnerAnrede = null;
	private String ansprechpartnerTelefon = null;
	private String ansprechpartnerTelefonDw = null;
	private String ansprechpartnerFax = null;
	private String ansprechpartnerFaxDw = null;
	private String ansprechpartnerEmail = null;

	
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

	public String getAnsprechpartnerTitel() {
		return ansprechpartnerTitel;
	}

	public void setAnsprechpartnerTitel(String ansprechpartnerTitel) {
		this.ansprechpartnerTitel = ansprechpartnerTitel;
	}

	public String getAnsprechpartnerAnrede() {
		return ansprechpartnerAnrede;
	}

	public void setAnsprechpartnerAnrede(String ansprechpartnerAnrede) {
		this.ansprechpartnerAnrede = ansprechpartnerAnrede;
	}

	public String getAnsprechpartnerTelefon() {
		return ansprechpartnerTelefon;
	}

	public void setAnsprechpartnerTelefon(String ansprechpartnerTelefon) {
		this.ansprechpartnerTelefon = ansprechpartnerTelefon;
	}

	public String getAnsprechpartnerTelefonDw() {
		return ansprechpartnerTelefonDw;
	}

	public void setAnsprechpartnerTelefonDw(String ansprechpartnerTelefonDw) {
		this.ansprechpartnerTelefonDw = ansprechpartnerTelefonDw;
	}

	public String getAnsprechpartnerFax() {
		return ansprechpartnerFax;
	}

	public void setAnsprechpartnerFax(String ansprechpartnerFax) {
		this.ansprechpartnerFax = ansprechpartnerFax;
	}

	public String getAnsprechpartnerFaxDw() {
		return ansprechpartnerFaxDw;
	}

	public void setAnsprechpartnerFaxDw(String ansprechpartnerFaxDw) {
		this.ansprechpartnerFaxDw = ansprechpartnerFaxDw;
	}

	public String getAnsprechpartnerEmail() {
		return ansprechpartnerEmail;
	}

	public void setAnsprechpartnerEmail(String ansprechpartnerEmail) {
		this.ansprechpartnerEmail = ansprechpartnerEmail;
	}

	public String getKundeCName2() {
		return kundeCName2;
	}

	public void setKundeCName2(String kundeCName2) {
		this.kundeCName2 = kundeCName2;
	}

	public String getKundeCName3() {
		return kundeCName3;
	}

	public void setKundeCName3(String kundeCName3) {
		this.kundeCName3 = kundeCName3;
	}

	public String getKundeStrasse() {
		return kundeStrasse;
	}

	public void setKundeStrasse(String kundeStrasse) {
		this.kundeStrasse = kundeStrasse;
	}

	public String getKundeLkz() {
		return kundeLkz;
	}

	public void setKundeLkz(String kundeLkz) {
		this.kundeLkz = kundeLkz;
	}

	public String getKundePlz() {
		return kundePlz;
	}

	public void setKundePlz(String kundePlz) {
		this.kundePlz = kundePlz;
	}

	public String getKundeOrt() {
		return kundeOrt;
	}

	public void setKundeOrt(String kundeOrt) {
		this.kundeOrt = kundeOrt;
	}

	public String getKundeTelefon() {
		return kundeTelefon;
	}

	public void setKundeTelefon(String kundeTelefon) {
		this.kundeTelefon = kundeTelefon;
	}

	public String getKundeFax() {
		return kundeFax;
	}

	public void setKundeFax(String kundeFax) {
		this.kundeFax = kundeFax;
	}

	public String getKundeEmail() {
		return kundeEmail;
	}

	public void setKundeEmail(String kundeEmail) {
		this.kundeEmail = kundeEmail;
	}

	public String getKundeHomepage() {
		return kundeHomepage;
	}

	public void setKundeHomepage(String kundeHomepage) {
		this.kundeHomepage = kundeHomepage;
	}

	private String kundeTelefon = null;
	private String kundeFax = null;
	private String kundeEmail = null;
	private String kundeHomepage = null;
	
	
	private String kostenstelleCNr = null;
	private String vertreterCName1 = null;
	private String projektBez = null;
	private String realisierungstermin = null;
	private String angeboterledigungsgrundCNr = null;
	private String erledigungsgrundABNr = null;
	
	// Konditionen Angebot
	private Double dAuftragwahrscheinlichkeit = null;
	private BigDecimal nWert = null;

	// Positionsdaten
	private String artikelCNr = null;
	private String artikelCBez = null;
	private BigDecimal nMenge = null;
	private String einheitCNr = null;
	private BigDecimal nPreis = null;
	
	
	

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iIdI) {
		this.iId = iIdI;
	}

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public String getKundeCName1() {
		return kundeCName1;
	}

	public void setKundeCName1(String kundeCName1I) {
		this.kundeCName1 = kundeCName1I;
	}

	public String getKostenstelleCNr() {
		return kostenstelleCNr;
	}

	public void setKostenstelleCNr(String kostenstelleCNrI) {
		this.kostenstelleCNr = kostenstelleCNrI;
	}

	public String getVertreterCName1() {
		return this.vertreterCName1;
	}

	public void setVertreterCName1(String vertreterCName1I) {
		this.vertreterCName1 = vertreterCName1I;
	}

	public String getProejktBez() {
		return this.projektBez;
	}

	public void setProjektBez(String projektBezI) {
		this.projektBez = projektBezI;
	}

	public String getRealisierungstermin() {
		return this.realisierungstermin;
	}

	public void setRealisierungstermin(String realisierungsterminI) {
		realisierungstermin = realisierungsterminI;
	}

	public BigDecimal getNMenge() {
		return this.nMenge;
	}

	public void setNMenge(BigDecimal nMengeI) {
		this.nMenge = nMengeI;
	}

	public String getEinheitCNr() {
		return this.einheitCNr;
	}

	public void setEinheitCNr(String einheitCNrI) {
		this.einheitCNr = einheitCNrI;
	}

	public BigDecimal getNPreis() {
		return this.nPreis;
	}

	public void setNPreis(BigDecimal nPreisI) {
		this.nPreis = nPreisI;
	}

	public BigDecimal getNWert() {
		return this.nWert;
	}

	public void setNWert(BigDecimal nWert) {
		this.nWert = nWert;
	}
	
	public Double getDAuftragwahrscheinlichkeit() {
		return this.dAuftragwahrscheinlichkeit;
	}

	public void setDAuftragwahrscheinlichkeit(Double dAuftragwahrscheinlichkeit) {
		this.dAuftragwahrscheinlichkeit = dAuftragwahrscheinlichkeit;
	}

	public String getArtkelCBez() {
		return this.artikelCBez;
	}

	public void setArtikelCBez(String artikelCBezI) {
		this.artikelCBez = artikelCBezI;
	}

	public String getArtikelCNr() {
		return this.artikelCNr;
	}

	public void setArtikelCNr(String artikelCNrI) {
		this.artikelCNr = artikelCNrI;
	}

	public String getAngeboterledigungsgrundCNr() {
		return angeboterledigungsgrundCNr;
	}

	public void setAngeboterledigungsgrundCNr(String angeboterledigungsgrundCNr) {
		this.angeboterledigungsgrundCNr = angeboterledigungsgrundCNr;
	}
	
	public String getErledigungsgrundABNr() {
		return erledigungsgrundABNr;
	}

	public void setErledigungsgrundABNr(String erledigungsgrundABNr) {
		this.erledigungsgrundABNr = erledigungsgrundABNr;
	}
}

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

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.lp.server.auftrag.service.AuftragzeitenDto;

public class ArbeitszeitstatistikDto {
	private Integer partnerIId;
	private Integer artikelgruppeIId;
	private Integer artikelklasseIId;
	private String sbeleg;
	private AuftragzeitenDto auftragzeitenDto;
	private String sKunde;
	private String sArtikelgruppe;
	private String sArtikelklasse;
	private String sKostenstelle;
	private String sFertigungsgruppe;
	private String sVertreter;
	private String sBelegbezeichnung;
	private String sKundenbestellnummer;
	private String sProjektStatus = null;
	private String sPersonalKurzzeichen = null;
	public String getSPersonalKurzzeichen() {
		return sPersonalKurzzeichen;
	}

	public void setSPersonalKurzzeichen(String sPersonalKurzzeichen) {
		this.sPersonalKurzzeichen = sPersonalKurzzeichen;
	}

	private BigDecimal bdBereitsverrechnet = null;
	
	public BigDecimal getBdBereitsverrechnet() {
		return bdBereitsverrechnet;
	}

	public void setBdBereitsverrechnet(BigDecimal bdBereitsverrechnet) {
		this.bdBereitsverrechnet = bdBereitsverrechnet;
	}

	private BigDecimal bdProjektGesamtdauer = null;
	
	
	private String sProjektTyp = null;
	public String getSProjektTyp() {
		return sProjektTyp;
	}

	public void setSProjektTyp(String sProjektTyp) {
		this.sProjektTyp = sProjektTyp;
	}

	public Integer getSProjektVerrechenbar() {
		return sProjektVerrechenbar;
	}

	public void setsProjektVerrechenbar(Integer sProjektVerrechenbar) {
		this.sProjektVerrechenbar = sProjektVerrechenbar;
	}

	public Timestamp getTProjektInternErledigt() {
		return tProjektInternErledigt;
	}

	public void setTProjektInternErledigt(Timestamp tProjektInternErledigt) {
		this.tProjektInternErledigt = tProjektInternErledigt;
	}

	private Integer  sProjektVerrechenbar = null;
	private Timestamp  tProjektInternErledigt = null;
	
	public BigDecimal getBdProjektGesamtdauer() {
		return bdProjektGesamtdauer;
	}

	public void setBdProjektGesamtdauer(BigDecimal bdProjektGesamtdauer) {
		this.bdProjektGesamtdauer = bdProjektGesamtdauer;
	}

	public String getSProjektStatus() {
		return sProjektStatus;
	}

	public void setSProjektStatus(String sProjektStatus) {
		this.sProjektStatus = sProjektStatus;
	}

	public String getSProjektKategorie() {
		return sProjektKategorie;
	}

	public void setSProjektKategorie(String sProjektKategorie) {
		this.sProjektKategorie = sProjektKategorie;
	}

	private Integer iProjektIId=null;
	
	public Integer getIProjektIId() {
		return iProjektIId;
	}

	public void setIProjektIId(Integer iProjektIId) {
		this.iProjektIId = iProjektIId;
	}

	public String getSProjektBereich() {
		return sProjektBereich;
	}

	public void setSProjektBereich(String sProjektBereich) {
		this.sProjektBereich = sProjektBereich;
	}

	private String sProjektBereich = null;
	
	private String sProjektKategorie = null;
	
	
	public String getSKundenbestellnummer() {
		return sKundenbestellnummer;
	}

	public void setSKundenbestellnummer(String sKundenbestellnummer) {
		this.sKundenbestellnummer = sKundenbestellnummer;
	}

	public String getSVertreter() {
		return sVertreter;
	}

	public void setSVertreter(String sVertreter) {
		this.sVertreter = sVertreter;
	}

	public String getsFertigungsgruppe() {
		return sFertigungsgruppe;
	}

	public void setsFertigungsgruppe(String sFertigungsgruppe) {
		this.sFertigungsgruppe = sFertigungsgruppe;
	}

	public String getSKostenstelle() {
		return sKostenstelle;
	}

	public void setSKostenstelle(String sKostenstelle) {
		this.sKostenstelle = sKostenstelle;
	}

	private String sGruppierung;

	public ArbeitszeitstatistikDto() {
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	public void setArtikelgruppeIId(Integer artikelgruppeIId) {
		this.artikelgruppeIId = artikelgruppeIId;
	}

	public void setArtikelklasseIId(Integer artikelklasseIId) {
		this.artikelklasseIId = artikelklasseIId;
	}

	public void setSbeleg(String sbeleg) {
		if (sbeleg == null) {
			sbeleg = "";
		}

		this.sbeleg = sbeleg;
	}

	public void setAuftragzeitenDto(AuftragzeitenDto auftragzeitenDto) {
		this.auftragzeitenDto = auftragzeitenDto;
	}

	public void setSKunde(String sKunde) {
		if (sKunde == null) {
			sKunde = "";
		}

		this.sKunde = sKunde;
	}

	public void setSArtikelgruppe(String sArtikelgruppe) {
		if (sArtikelgruppe == null) {
			sArtikelgruppe = "";
		}
		this.sArtikelgruppe = sArtikelgruppe;
	}

	public void setSArtikelklasse(String sArtikelklasse) {
		if (sArtikelklasse == null) {
			sArtikelklasse = "";
		}

		this.sArtikelklasse = sArtikelklasse;
	}

	public void setSGruppierung(String sGruppierung) {
		this.sGruppierung = sGruppierung;
	}

	public Integer getPartnerIId() {
		return partnerIId;
	}

	public Integer getArtikelgruppeIId() {
		return artikelgruppeIId;
	}

	public Integer getArtikelklasseIId() {
		return artikelklasseIId;
	}

	public String getSbeleg() {
		return sbeleg;
	}

	public AuftragzeitenDto getAuftragzeitenDto() {
		return auftragzeitenDto;
	}

	public String getSKunde() {
		return sKunde;
	}

	public String getSArtikelgruppe() {
		return sArtikelgruppe;
	}

	public String getSArtikelklasse() {
		return sArtikelklasse;
	}

	public String getSGruppierung() {
		return sGruppierung;
	}

	public void setSBelegbezeichnung(String sBelegbezeichnung) {
		this.sBelegbezeichnung = sBelegbezeichnung;
	}

	public String getSBelegbezeichnung() {
		return sBelegbezeichnung;
	}
}

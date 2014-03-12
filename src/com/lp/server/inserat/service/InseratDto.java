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
package com.lp.server.inserat.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.lp.util.Helper;

public class InseratDto implements Serializable {
	private Integer iId;
	private String mandantCNr;
	private String cNr;
	private Timestamp tBelegdatum;
	private Double fKdRabatt;
	private Double fKdZusatzrabatt;
	private Double fKdNachlass;
	private String cBez;
	private String cRubrik;
	private String cRubrik2;
	private String cMedium;
	private String xAnhang;
	private Integer lieferantIId;
	private Integer ansprechpartnerIIdLieferant;
	private Double fLFRabatt;
	private Double fLfZusatzrabatt;
	private Double fLfNachlass;
	private String xAnhangLf;
	private Timestamp tTermin;
	private Integer artikelIIdInseratart;
	private String statusCNr;
	private Integer personalIIdVertreter;
	private BigDecimal nMenge;
	private BigDecimal nNettoeinzelpreisEk;
	private BigDecimal nNettoeinzelpreisVk;
	private Timestamp tErschienen;
	private Integer personalIIdErschienen;
	private Timestamp tVerrechnen;
	private Integer personalIIdVerrechnen;

	private Timestamp tManuellverrechnen;

	public Timestamp getTManuellverrechnen() {
		return tManuellverrechnen;
	}

	public void setTManuellverrechnen(Timestamp tManuellverrechnen) {
		this.tManuellverrechnen = tManuellverrechnen;
	}

	public Integer getPersonalIIdManuellverrechnen() {
		return personalIIdManuellverrechnen;
	}

	public void setPersonalIIdManuellverrechnen(
			Integer personalIIdManuellverrechnen) {
		this.personalIIdManuellverrechnen = personalIIdManuellverrechnen;
	}

	private Integer personalIIdManuellverrechnen;

	private Integer personalIIdGestoppt;
	private Timestamp tGestoppt;

	public Integer getPersonalIIdGestoppt() {
		return personalIIdGestoppt;
	}

	public void setPersonalIIdGestoppt(Integer personalIIdGestoppt) {
		this.personalIIdGestoppt = personalIIdGestoppt;
	}

	public Timestamp getTGestoppt() {
		return tGestoppt;
	}

	public void setTGestoppt(Timestamp tGestoppt) {
		this.tGestoppt = tGestoppt;
	}

	private Short bWertaufteilen;

	public Short getBWertaufteilen() {
		return bWertaufteilen;
	}

	public void setBWertaufteilen(Short bWertaufteilen) {
		this.bWertaufteilen = bWertaufteilen;
	}

	private Integer personalIIdManuellerledigt;

	private Timestamp tManuellerledigt;

	public Timestamp getTManuellerledigt() {
		return this.tManuellerledigt;
	}

	public void setTManuellerledigt(Timestamp tManuellerledigt) {
		this.tManuellerledigt = tManuellerledigt;
	}

	public Integer getPersonalIIdManuellerledigt() {
		return this.personalIIdManuellerledigt;
	}

	public void setPersonalIIdManuellerledigt(Integer personalIIdManuellerledigt) {
		this.personalIIdManuellerledigt = personalIIdManuellerledigt;
	}

	private String cGestoppt;
	private Timestamp tAnlegen;
	private Timestamp tAendern;
	private Integer personalIIdAendern;
	private Integer personalIIdAnlegen;

	private InseratrechnungDto inseratrechnungDto;

	public InseratrechnungDto getInseratrechnungDto() {
		return inseratrechnungDto;
	}

	public void setInseratrechnungDto(InseratrechnungDto inseratrechnungDto) {
		this.inseratrechnungDto = inseratrechnungDto;
	}

	private static final long serialVersionUID = 1L;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public Timestamp getTBelegdatum() {
		return tBelegdatum;
	}

	public void setTBelegdatum(Timestamp tBelegdatum) {
		this.tBelegdatum = tBelegdatum;
	}

	public Double getFKdRabatt() {
		return fKdRabatt;
	}

	public void setFKdRabatt(Double fKdRabatt) {
		this.fKdRabatt = fKdRabatt;
	}

	public Double getFKdZusatzrabatt() {
		return fKdZusatzrabatt;
	}

	public void setFKdZusatzrabatt(Double fKdZusatzrabatt) {
		this.fKdZusatzrabatt = fKdZusatzrabatt;
	}

	public Double getFKdNachlass() {
		return fKdNachlass;
	}

	public void setFKdNachlass(Double fKdNachlass) {
		this.fKdNachlass = fKdNachlass;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public String getCRubrik() {
		return cRubrik;
	}

	public void setCRubrik(String cRubrik) {
		this.cRubrik = cRubrik;
	}

	public String getCRubrik2() {
		return cRubrik2;
	}

	public void setCRubrik2(String cRubrik2) {
		this.cRubrik2 = cRubrik2;
	}

	private String cStichwort;

	private String cStichwort2;

	public String getCStichwort() {
		return cStichwort;
	}

	public void setCStichwort(String cStichwort) {
		this.cStichwort = cStichwort;
	}

	public String getCStichwort2() {
		return cStichwort2;
	}

	public void setCStichwort2(String cStichwort2) {
		this.cStichwort2 = cStichwort2;
	}

	public String getCMedium() {
		return cMedium;
	}

	public void setCMedium(String cMedium) {
		this.cMedium = cMedium;
	}

	public String getXAnhang() {
		return xAnhang;
	}

	public void setXAnhang(String xAnhang) {
		this.xAnhang = xAnhang;
	}

	public Integer getLieferantIId() {
		return lieferantIId;
	}

	public void setLieferantIId(Integer lieferantIId) {
		this.lieferantIId = lieferantIId;
	}

	public Integer getAnsprechpartnerIIdLieferant() {
		return ansprechpartnerIIdLieferant;
	}

	public void setAnsprechpartnerIIdLieferant(
			Integer ansprechpartnerIIdLieferant) {
		this.ansprechpartnerIIdLieferant = ansprechpartnerIIdLieferant;
	}

	public Double getFLFRabatt() {
		return fLFRabatt;
	}

	public void setFLFRabatt(Double fLFRabatt) {
		this.fLFRabatt = fLFRabatt;
	}

	public Double getFLfZusatzrabatt() {
		return fLfZusatzrabatt;
	}

	public void setFLfZusatzrabatt(Double fLfZusatzrabatt) {
		this.fLfZusatzrabatt = fLfZusatzrabatt;
	}

	public Double getFLfNachlass() {
		return fLfNachlass;
	}

	public void setFLfNachlass(Double fLfNachlass) {
		this.fLfNachlass = fLfNachlass;
	}

	public String getXAnhangLf() {
		return xAnhangLf;
	}

	public void setXAnhangLf(String xAnhangLf) {
		this.xAnhangLf = xAnhangLf;
	}

	public Timestamp getTTermin() {
		return tTermin;
	}

	public void setTTermin(Timestamp tTermin) {
		this.tTermin = tTermin;
	}

	public Integer getArtikelIIdInseratart() {
		return artikelIIdInseratart;
	}

	public void setArtikelIIdInseratart(Integer artikelIIdInseratart) {
		this.artikelIIdInseratart = artikelIIdInseratart;
	}

	public String getStatusCNr() {
		return statusCNr;
	}

	public void setStatusCNr(String statusCNr) {
		this.statusCNr = statusCNr;
	}

	public Integer getPersonalIIdVertreter() {
		return personalIIdVertreter;
	}

	public void setPersonalIIdVertreter(Integer personalIIdVertreter) {
		this.personalIIdVertreter = personalIIdVertreter;
	}

	public BigDecimal getNMenge() {
		return nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public BigDecimal getNNettoeinzelpreisEk() {
		return nNettoeinzelpreisEk;
	}

	public void setNNettoeinzelpreisEk(BigDecimal nNettoeinzelpreisEk) {
		this.nNettoeinzelpreisEk = nNettoeinzelpreisEk;
	}

	public BigDecimal getNNettoeinzelpreisVk() {
		return nNettoeinzelpreisVk;
	}

	public void setNNettoeinzelpreisVk(BigDecimal nNettoeinzelpreisVk) {
		this.nNettoeinzelpreisVk = nNettoeinzelpreisVk;
	}

	public Timestamp getTErschienen() {
		return tErschienen;
	}

	public void setTErschienen(Timestamp tErschienen) {
		this.tErschienen = tErschienen;
	}

	public Integer getPersonalIIdErschienen() {
		return personalIIdErschienen;
	}

	public void setPersonalIIdErschienen(Integer personalIIdErschienen) {
		this.personalIIdErschienen = personalIIdErschienen;
	}

	public Timestamp getTVerrechnen() {
		return tVerrechnen;
	}

	public void setTVerrechnen(Timestamp tVerrechnen) {
		this.tVerrechnen = tVerrechnen;
	}

	public Integer getPersonalIIdVerrechnen() {
		return personalIIdVerrechnen;
	}

	public void setPersonalIIdVerrechnen(Integer personalIIdVerrechnen) {
		this.personalIIdVerrechnen = personalIIdVerrechnen;
	}

	public String getCGestoppt() {
		return cGestoppt;
	}

	public void setCGestoppt(String cGestoppt) {
		this.cGestoppt = cGestoppt;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	private Integer bestellpositionIId;

	public Integer getBestellpositionIId() {
		return bestellpositionIId;
	}

	public void setBestellpositionIId(Integer bestellpositionIId) {
		this.bestellpositionIId = bestellpositionIId;
	}

	private Short bDruckBerstellungLf;
	private Short bDruckBerstellungKd;
	private Short bDruckRechnungKd;
	private Timestamp tTerminBis;

	public Short getBDruckBestellungLf() {
		return bDruckBerstellungLf;
	}

	public void setBDruckBestellungLf(Short bDruckBerstellungLf) {
		this.bDruckBerstellungLf = bDruckBerstellungLf;
	}

	public Short getBDruckBestellungKd() {
		return bDruckBerstellungKd;
	}

	public void setBDruckBestellungKd(Short bDruckBerstellungKd) {
		this.bDruckBerstellungKd = bDruckBerstellungKd;
	}

	public Short getBDruckRechnungKd() {
		return bDruckRechnungKd;
	}

	public void setBDruckRechnungKd(Short bDruckRechnungKd) {
		this.bDruckRechnungKd = bDruckRechnungKd;
	}

	public Timestamp getTTerminBis() {
		return tTerminBis;
	}

	public void setTTerminBis(Timestamp tTerminBis) {
		this.tTerminBis = tTerminBis;
	}

	public BigDecimal getErrechneterWertEK(int iNachkommastellen) {

		// EK
		Double dRabattGesamt = Helper.berechneRabattsatzMehrererRabatte(
				getFLFRabatt(), getFLfZusatzrabatt(), getFLfNachlass());

		BigDecimal bdErrechneterPreisEK = getNNettoeinzelpreisEk().subtract(
				Helper.getProzentWert(getNNettoeinzelpreisEk(), new BigDecimal(
						dRabattGesamt), iNachkommastellen));

		return bdErrechneterPreisEK.multiply(getNMenge());

	}

	public BigDecimal getErrechneterWertVK(int iNachkommastellen) {

		// EK
		Double dRabattGesamt = Helper.berechneRabattsatzMehrererRabatte(
				getFKdRabatt(), getFKdZusatzrabatt(), getFKdNachlass());

		BigDecimal bdErrechneterPreisVK = getNNettoeinzelpreisVk().subtract(
				Helper.getProzentWert(getNNettoeinzelpreisVk(), new BigDecimal(
						dRabattGesamt), iNachkommastellen));

		return bdErrechneterPreisVK.multiply(getNMenge());

	}
	
	
	public Integer inseratIId_Kopiertvon;
	

}

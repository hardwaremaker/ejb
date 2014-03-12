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
package com.lp.server.auftrag.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class AuftragNachkalkulationDto extends TabelleDto implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AuftragNachkalkulationDto(AuftragDto auftragDto) {
		this.auftragDto = auftragDto;
	}

	private AuftragDto auftragDto;

	private String sBelegart;
	private String sBelegnummer;
	private String sBelegkundename;
	private String sRechnungsart;

	
	public String getSRechnungsart() {
		return sRechnungsart;
	}

	public void setSRechnungsart(String sRechnungsart) {
		this.sRechnungsart = sRechnungsart;
	}

	private String sEingangsrechnungtext;
	private String sRechnungsnummerLSVerrechnet;
	public String getSRechnungsnummerLSVerrechnet() {
		return sRechnungsnummerLSVerrechnet;
	}

	public void setSRechnungsnummerLSVerrechnet(String sRechnungsnummerLSVerrechnet) {
		this.sRechnungsnummerLSVerrechnet = sRechnungsnummerLSVerrechnet;
	}

	private BigDecimal bdVkwArbeitsoll;
	private BigDecimal bdVkwMaterialsoll;
	private BigDecimal bdVkwArbeitist;
	private BigDecimal bdVkwMaterialist;
	private String cArtikelbezeichnungArbeitszeit;

	private BigDecimal bdGestehungswertarbeitsoll;
	private BigDecimal bdGestehungswertmaterialsoll;
	private BigDecimal bdGestehungswertarbeitist;
	private BigDecimal bdGestehungswertmaterialist;

	private Double ddArbeitszeitsoll;
	private Double ddArbeitszeitist;

	private Double ddMaschinenzeitsoll;
	private Double ddMaschinenzeitist;
	private String cArtikelnummerArbeitszeit;
	
	public String getErLieferant() {
		return erLieferant;
	}

	public void setErLieferant(String erLieferant) {
		this.erLieferant = erLieferant;
	}

	public String getReBestellnummer() {
		return reBestellnummer;
	}

	public void setReBestellnummer(String reBestellnummer) {
		this.reBestellnummer = reBestellnummer;
	}

	private String erLieferant;
	private String reBestellnummer;

	
	public String getSLosstuecklistenartikelnummer() {
		return sLosstuecklistenartikelnummer;
	}

	public void setSLosstuecklistenartikelnummer(String losstuecklistenartikelnummer) {
		sLosstuecklistenartikelnummer = losstuecklistenartikelnummer;
	}

	public String getSLosstuecklistenartikelbezeichnung() {
		return sLosstuecklistenartikelbezeichnung;
	}

	public void setSLosstuecklistenartikelbezeichnung(
			String losstuecklistenartikelbezeichnung) {
		sLosstuecklistenartikelbezeichnung = losstuecklistenartikelbezeichnung;
	}

	public String getSLosstuecklistenartikelzusatzbezeichnung() {
		return sLosstuecklistenartikelzusatzbezeichnung;
	}

	public void setSLosstuecklistenartikelzusatzbezeichnung(
			String losstuecklistenartikelzusatzbezeichnung) {
		sLosstuecklistenartikelzusatzbezeichnung = losstuecklistenartikelzusatzbezeichnung;
	}

	public String getSLoskommentar() {
		return sLoskommentar;
	}

	public void setSLoskommentar(String loskommentar) {
		sLoskommentar = loskommentar;
	}

	public String getSLosprojekt() {
		return sLosprojekt;
	}

	public void setSLosprojekt(String losprojekt) {
		sLosprojekt = losprojekt;
	}

	private String sLosstuecklistenartikelnummer;
	private String sLosstuecklistenartikelbezeichnung;
	private String sLosstuecklistenartikelzusatzbezeichnung;

	private String sLoskommentar;
	private String sLosprojekt;

	
	private Double dLosbewertung;
	private Timestamp tLoserledigungsdatum;

	
	
	public Double getdLosbewertung() {
		return dLosbewertung;
	}

	public void setdLosbewertung(Double dLosbewertung) {
		this.dLosbewertung = dLosbewertung;
	}

	public Timestamp gettLoserledigungsdatum() {
		return tLoserledigungsdatum;
	}

	public void settLoserledigungsdatum(Timestamp tLoserledigungsdatum) {
		this.tLoserledigungsdatum = tLoserledigungsdatum;
	}

	public String getSBelegart() {
		return sBelegart;
	}

	public void setSBelegart(String sBelegart) {
		this.sBelegart = sBelegart;
	}

	public String getSBelegnummer() {
		return sBelegnummer;
	}

	public void setSBelegnummer(String sBelegnummer) {
		this.sBelegnummer = sBelegnummer;
	}

	public String getSBelegkundename() {
		return sBelegkundename;
	}

	public void setSBelegkundename(String sBelegkundename) {
		this.sBelegkundename = sBelegkundename;
	}

	public String getSEingangsrechnungtext() {
		return sEingangsrechnungtext;
	}

	public void setSEingangsrechnungtext(String sEingangsrechnungtext) {
		this.sEingangsrechnungtext = sEingangsrechnungtext;
	}

	public BigDecimal getBdVkwArbeitsoll() {
		return bdVkwArbeitsoll;
	}

	public void setBdVkwArbeitsoll(BigDecimal bdVkwArbeitsoll) {
		this.bdVkwArbeitsoll = bdVkwArbeitsoll;
	}

	public BigDecimal getBdVkwMaterialsoll() {
		return bdVkwMaterialsoll;
	}

	public void setBdVkwMaterialsoll(BigDecimal bdVkwMaterialsoll) {
		this.bdVkwMaterialsoll = bdVkwMaterialsoll;
	}

	public BigDecimal getBdVkwArbeitist() {
		return bdVkwArbeitist;
	}

	public void setBdVkwArbeitist(BigDecimal bdVkwArbeitist) {
		this.bdVkwArbeitist = bdVkwArbeitist;
	}

	public BigDecimal getBdVkwMaterialist() {
		return bdVkwMaterialist;
	}

	public void setBdVkwMaterialist(BigDecimal bdVkwMaterialist) {
		this.bdVkwMaterialist = bdVkwMaterialist;
	}

	public BigDecimal getBdGestehungswertarbeitsoll() {
		return bdGestehungswertarbeitsoll;
	}

	public void setBdGestehungswertarbeitsoll(
			BigDecimal bdGestehungswertarbeitsoll) {
		this.bdGestehungswertarbeitsoll = bdGestehungswertarbeitsoll;
	}

	public BigDecimal getBdGestehungswertmaterialsoll() {
		return bdGestehungswertmaterialsoll;
	}

	public void setBdGestehungswertmaterialsoll(
			BigDecimal bdGestehungswertmaterialsoll) {
		this.bdGestehungswertmaterialsoll = bdGestehungswertmaterialsoll;
	}

	public BigDecimal getBdGestehungswertarbeitist() {
		return bdGestehungswertarbeitist;
	}

	public void setBdGestehungswertarbeitist(
			BigDecimal bdGestehungswertarbeitist) {
		this.bdGestehungswertarbeitist = bdGestehungswertarbeitist;
	}

	public BigDecimal getBdGestehungswertmaterialist() {
		return bdGestehungswertmaterialist;
	}

	public void setBdGestehungswertmaterialist(
			BigDecimal bdGestehungswertmaterialist) {
		this.bdGestehungswertmaterialist = bdGestehungswertmaterialist;
	}

	public Double getDdArbeitszeitsoll() {
		return ddArbeitszeitsoll;
	}

	public void setDdArbeitszeitsoll(Double ddArbeitszeitsoll) {
		this.ddArbeitszeitsoll = ddArbeitszeitsoll;
	}

	public Double getDdArbeitszeitist() {
		return ddArbeitszeitist;
	}

	public Double getDdMaschinenzeitsoll() {
		return ddMaschinenzeitsoll;
	}

	public Double getDdMaschinenzeitist() {
		return ddMaschinenzeitist;
	}

	public String getCArtikelnummerArbeitszeit() {
		return cArtikelnummerArbeitszeit;
	}

	public String getCArtikelbezeichnungArbeitszeit() {
		return cArtikelbezeichnungArbeitszeit;
	}

	public AuftragDto getAuftragDto() {
		return auftragDto;
	}

	public void setDdArbeitszeitist(Double ddArbeitszeitist) {
		this.ddArbeitszeitist = ddArbeitszeitist;
	}

	public void setDdMaschinenzeitsoll(Double ddMaschinenzeitsoll) {
		this.ddMaschinenzeitsoll = ddMaschinenzeitsoll;
	}

	public void setDdMaschinenzeitist(Double ddMaschinenzeitist) {
		this.ddMaschinenzeitist = ddMaschinenzeitist;
	}

	public void setCArtikelnummerArbeitszeit(String cArtikelnummerArbeitszeit) {
		this.cArtikelnummerArbeitszeit = cArtikelnummerArbeitszeit;
	}

	public void setCArtikelbezeichnungArbeitszeit(
			String cArtikelbezeichnungArbeitszeit) {
		this.cArtikelbezeichnungArbeitszeit = cArtikelbezeichnungArbeitszeit;
	}

	public void setAuftragDto(AuftragDto auftragDto) {
		this.auftragDto = auftragDto;
	}
}

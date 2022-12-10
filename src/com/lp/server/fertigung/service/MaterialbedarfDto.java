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
package com.lp.server.fertigung.service;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lp.server.partner.service.PartnerDto;

public class MaterialbedarfDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * I_ID des benoetigten Artikels.
	 */
	private Integer iArtikelId = null;
	/**
	 * benoetigte Menge.
	 */
	private BigDecimal nMenge = null;
	/**
	 * Termin an dem das Material benoetigt wird.
	 */
	private java.sql.Date tTermin = null;

	private String sBelegartCNr = null;
	private Integer iBelegIId = null;
	private Integer iBelegpositionIId = null;
	private PartnerDto partnerDto = null;
	private String cBelegnummer = null;
	private boolean bTemporaererEintrag = false;

	private String xAusloeser;

	public String getXAusloeser() {
		return xAusloeser;
	}

	public void setXAusloeser(String xAusloeser) {
		this.xAusloeser = xAusloeser;
	}

	private Integer partnerIIdStandort = null;

	public Integer getPartnerIIdStandort() {
		return partnerIIdStandort;
	}

	public void setPartnerIIdStandort(Integer partnerIIdStandort) {
		this.partnerIIdStandort = partnerIIdStandort;
	}

	private Integer auftragIIdKopfauftrag;

	public Integer getAuftragIIdKopfauftrag() {
		return auftragIIdKopfauftrag;
	}

	public void setAuftragIIdKopfauftrag(Integer auftragIIdKopfauftrag) {
		this.auftragIIdKopfauftrag = auftragIIdKopfauftrag;
	}

	/**
	 * um die stuecklistenhierarchie in die loshierarchie uebertragen zu koennen.
	 * nur fuer das fertigungsmodul relevant.
	 */
	private Integer iInternebestellungIIdElternlos = null;
	private Integer iStuecklisteIId = null;

	public Integer getIArtikelId() {
		return iArtikelId;
	}

	public void setIArtikelId(Integer iArtikelId) {
		this.iArtikelId = iArtikelId;
	}

	public BigDecimal getNMenge() {
		return nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public java.sql.Date getTTermin() {
		return tTermin;
	}

	public Integer getIBelegIId() {
		return iBelegIId;
	}

	public Integer getIBelegpositionIId() {
		return iBelegpositionIId;
	}

	public Integer getIInternebestellungIIdElternlos() {
		return iInternebestellungIIdElternlos;
	}

	public Integer getIStuecklisteIId() {
		return iStuecklisteIId;
	}

	public String getSBelegartCNr() {
		return sBelegartCNr;
	}

	public void setTTermin(java.sql.Date tTermin) {
		this.tTermin = tTermin;
	}

	public void setIBelegIId(Integer iBelegIId) {
		this.iBelegIId = iBelegIId;
	}

	public void setIBelegpositionIId(Integer iBelegpositionIId) {
		this.iBelegpositionIId = iBelegpositionIId;
	}

	public void setIInternebestellungIIdElternlos(Integer iInternebestellungIIdElternlos) {
		this.iInternebestellungIIdElternlos = iInternebestellungIIdElternlos;
	}

	public void setIStuecklisteIId(Integer iStuecklisteIId) {
		this.iStuecklisteIId = iStuecklisteIId;
	}

	public void setSBelegartCNr(String sBelegartCNr) {
		this.sBelegartCNr = sBelegartCNr;
	}

	public void setPartnerDto(PartnerDto partnerDto) {
		this.partnerDto = partnerDto;
	}

	public PartnerDto getPartnerDto() {
		return partnerDto;
	}

	public void setCBelegnummer(String cBelegnummer) {
		this.cBelegnummer = cBelegnummer;
	}

	public String getCBelegnummer() {
		return cBelegnummer;
	}

	public boolean getBTemporaererEintrag() {
		return bTemporaererEintrag;
	}

	public void setBTemporaererEintrag(boolean bTemporaererEintrag) {
		this.bTemporaererEintrag = bTemporaererEintrag;
	}

	private Integer projektIId;

	public Integer getProjektIId() {
		return projektIId;
	}

	public void setProjektIId(Integer projektIId) {
		this.projektIId = projektIId;
	}

	private String xTextinhalt = null;

	public final String getXTextinhalt() {
		return this.xTextinhalt;
	}

	public final void setXTextinhalt(String xTextinhalt) {
		this.xTextinhalt = xTextinhalt;
	}

	private BigDecimal nEinkaufpreis;

	public BigDecimal getNEinkaufpreis() {
		return nEinkaufpreis;
	}

	public void setNEinkaufpreis(BigDecimal bdEinkaufpreis) {
		this.nEinkaufpreis = bdEinkaufpreis;
	}

	private Integer lieferantIId;

	public Integer getLieferantIId() {
		return lieferantIId;
	}

	public void setLieferantIId(Integer lieferantIId) {
		this.lieferantIId = lieferantIId;
	}
	
	private Double fLagermindest;

	public Double getFLagermindest() {
		return this.fLagermindest;
	}

	public void setFLagermindest(Double fLagermindest) {
		this.fLagermindest = fLagermindest;
	}
	

}

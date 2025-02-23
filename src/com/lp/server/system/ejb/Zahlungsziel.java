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
package com.lp.server.system.ejb;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.util.ICBez;

@NamedQueries({
		@NamedQuery(name = "ZahlungszielfindAllByMandant", query = "SELECT OBJECT (o) FROM Zahlungsziel o WHERE o.mandantCNr = ?1"),
		@NamedQuery(name = "ZahlungszielfindByCBezMandantCNr", query = "SELECT OBJECT (o) FROM Zahlungsziel o WHERE o.mandantCNr = ?1 AND o.cBez = ?2") })
@Entity
@Table(name = "LP_ZAHLUNGSZIEL")
public class Zahlungsziel implements Serializable,ICBez {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "I_ANZAHLZIELTAGEFUERNETTO")
	private Integer iAnzahlzieltagefuernetto;
	
	@Column(name = "I_FOLGEMONAT_SKONTOTAGE1")
	private Integer iFolgemonatSkontotage1;
	
	public Integer getIFolgemonatSkontotage1() {
		return iFolgemonatSkontotage1;
	}

	public void setIFolgemonatSkontotage1(Integer iFolgemonatSkontotage1) {
		this.iFolgemonatSkontotage1 = iFolgemonatSkontotage1;
	}

	public Integer getIFolgemonatSkontotage2() {
		return iFolgemonatSkontotage2;
	}

	public void setIFolgemonatSkontotage2(Integer iFolgemonatSkontotage2) {
		this.iFolgemonatSkontotage2 = iFolgemonatSkontotage2;
	}

	@Column(name = "I_FOLGEMONAT_SKONTOTAGE2")
	private Integer iFolgemonatSkontotage2;

	@Column(name = "N_SKONTOPROZENTSATZ1")
	private BigDecimal nSkontoprozentsatz1;

	@Column(name = "N_ANZAHLUNG_PROZENT")
	private BigDecimal nAnzahlungProzent;
	
	
	public BigDecimal getNAnzahlungProzent() {
		return nAnzahlungProzent;
	}

	public void setNAnzahlungProzent(BigDecimal nAnzahlungProzent) {
		this.nAnzahlungProzent = nAnzahlungProzent;
	}

	@Column(name = "I_SKONTOANZAHLTAGE1")
	private Integer iSkontoanzahltage1;

	@Column(name = "N_SKONTOPROZENTSATZ2")
	private BigDecimal nSkontoprozentsatz2;

	@Column(name = "I_SKONTOANZAHLTAGE2")
	private Integer iSkontoanzahltage2;

	@Column(name = "B_VERSTECKT")
	private Short bVersteckt;

	@Column(name = "B_INZAHLUNGSVORSCHLAGBERUECKSICHTIGEN")
	private Short bInzahlungsvorschlagberuecksichtigen;

	@Column(name = "B_STICHTAG")
	private Short bStichtag;

	@Column(name = "B_STICHTAG_MONATSLETZTER")
	private Short bStichtagMonatsletzter;

	@Column(name = "I_STICHTAG")
	private Integer iStichtag;
	@Column(name = "I_FOLGEMONAT")
	private Integer iFolgemonat;
	
	@Column(name = "B_LASTSCHRIFT")
	private Short bLastschrift;
	

	public Short getBLastschrift() {
		return bLastschrift;
	}

	public void setBLastschrift(Short bLastschrift) {
		this.bLastschrift = bLastschrift;
	}

	public Short getBStichtag() {
		return bStichtag;
	}

	public void setBStichtag(Short bStichtag) {
		this.bStichtag = bStichtag;
	}

	public Short getBStichtagMonatsletzter() {
		return bStichtagMonatsletzter;
	}

	public void setBStichtagMonatsletzter(Short bStichtagMonatsletzter) {
		this.bStichtagMonatsletzter = bStichtagMonatsletzter;
	}

	public Integer getIStichtag() {
		return iStichtag;
	}

	public void setIStichtag(Integer iStichtag) {
		this.iStichtag = iStichtag;
	}

	public Integer getIFolgemonat() {
		return iFolgemonat;
	}

	public void setIFolgemonat(Integer iFolgemonat) {
		this.iFolgemonat = iFolgemonat;
	}

	public Short getBInzahlungsvorschlagberuecksichtigen() {
		return bInzahlungsvorschlagberuecksichtigen;
	}

	public void setBInzahlungsvorschlagberuecksichtigen(
			Short bInzahlungsvorschlagberuecksichtigen) {
		this.bInzahlungsvorschlagberuecksichtigen = bInzahlungsvorschlagberuecksichtigen;
	}

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	private static final long serialVersionUID = 1L;

	public Zahlungsziel() {
		super();
	}

	public Zahlungsziel(Integer idZahlungszielO, String mandantCNr, String bez,
			Short versteckt, Integer iAnzahlzieltagefuernetto,
			Short bInzahlungsvorschlagberuecksichtigen, Short bStichtag,
			Short bStichtagMonatsletzter, Short bLastschrift) {
		setIId(idZahlungszielO);
		setMandantCNr(mandantCNr);
		setCBez(bez);
		setBVersteckt(versteckt);
		setIAnzahlzieltagefuernetto(iAnzahlzieltagefuernetto);
		setBInzahlungsvorschlagberuecksichtigen(bInzahlungsvorschlagberuecksichtigen);
		setBStichtag(bStichtag);
		setBStichtagMonatsletzter(bStichtagMonatsletzter);
		setBLastschrift(bLastschrift);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public Integer getIAnzahlzieltagefuernetto() {
		return this.iAnzahlzieltagefuernetto;
	}

	public void setIAnzahlzieltagefuernetto(Integer iAnzahlzieltagefuernetto) {
		this.iAnzahlzieltagefuernetto = iAnzahlzieltagefuernetto;
	}

	public BigDecimal getNSkontoprozentsatz1() {
		return this.nSkontoprozentsatz1;
	}

	public void setNSkontoprozentsatz1(BigDecimal nSkontoprozentsatz1) {
		this.nSkontoprozentsatz1 = nSkontoprozentsatz1;
	}

	public Integer getISkontoanzahltage1() {
		return this.iSkontoanzahltage1;
	}

	public void setISkontoanzahltage1(Integer iSkontoanzahltage1) {
		this.iSkontoanzahltage1 = iSkontoanzahltage1;
	}

	public BigDecimal getNSkontoprozentsatz2() {
		return this.nSkontoprozentsatz2;
	}

	public void setNSkontoprozentsatz2(BigDecimal nSkontoprozentsatz2) {
		this.nSkontoprozentsatz2 = nSkontoprozentsatz2;
	}

	public Integer getISkontoanzahltage2() {
		return this.iSkontoanzahltage2;
	}

	public void setISkontoanzahltage2(Integer iSkontoanzahltage2) {
		this.iSkontoanzahltage2 = iSkontoanzahltage2;
	}

	public Short getBVersteckt() {
		return this.bVersteckt;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandant) {
		this.mandantCNr = mandant;
	}

}

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
package com.lp.server.angebotstkl.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.system.service.ITablenames;

@NamedQueries({
		@NamedQuery(name = "AgstklfindByCNrMandantCNr", query = "SELECT OBJECT(o) FROM Agstkl o WHERE o.cNr = ?1 AND o.mandantCNr = ?2"),
		@NamedQuery(name = "AgstklfindByKundeIIdMandantCNr", query = "SELECT OBJECT(O) FROM Agstkl o WHERE o.kundeIId = ?1 AND o.mandantCNr = ?2"),
		@NamedQuery(name = "AgstklfindByAnsprechpartnerIIdKunde", query = "SELECT OBJECT(O) FROM Agstkl o WHERE o.ansprechpartnerIIdKunde = ?1") })
@Entity
@Table(name = ITablenames.AS_AGSTKL)
public class Agstkl implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "C_BEZ")
	private String cBez;
	
	@Column(name = "C_ZEICHNUNGSNUMMER")
	private String cZeichnungsnummer;

	public String getCZeichnungsnummer() {
		return cZeichnungsnummer;
	}

	public void setCZeichnungsnummer(String cZeichnungsnummer) {
		this.cZeichnungsnummer = cZeichnungsnummer;
	}

	@Column(name = "N_INITIALKOSTEN")
	private BigDecimal nInitialkosten;
	
	public BigDecimal getNInitialkosten() {
		return nInitialkosten;
	}

	public void setNInitialkosten(BigDecimal nInitialkosten) {
		this.nInitialkosten = nInitialkosten;
	}

	@Column(name = "C_DATEINAME")
	private String cDateiname;

	public String getCDateiname() {
		return cDateiname;
	}

	public void setCDateiname(String cDateiname) {
		this.cDateiname = cDateiname;
	}

	@Column(name = "T_BELEGDATUM")
	private Timestamp tBelegdatum;

	@Column(name = "B_VORLAGE")
	private Short bVorlage;

	public Short getBVorlage() {
		return bVorlage;
	}

	public void setBVorlage(Short bVorlage) {
		this.bVorlage = bVorlage;
	}

	@Column(name = "DATENFORMAT_C_NR")
	private String datenformatCNr;

	public String getDatenformatCNr() {
		return datenformatCNr;
	}

	public void setDatenformatCNr(String datenformatCNr) {
		this.datenformatCNr = datenformatCNr;
	}

	@Column(name = "O_MEDIA")
	private byte[] oMedia;

	public byte[] getOMedia() {
		return oMedia;
	}

	public void setOMEdia(byte[] oMedia) {
		this.oMedia = oMedia;
	}

	@Column(name = "I_HOEHE_DIALOG")
	private Integer iHoeheDialog;

	public Integer getIHoeheDialog() {
		return iHoeheDialog;
	}

	public void setIHoeheDialog(Integer iHoeheDialog) {
		this.iHoeheDialog = iHoeheDialog;
	}

	@Column(name = "B_DATENGEAENDERT")
	private Short bDatengeaendert;

	public Short getBDatengeaendert() {
		return bDatengeaendert;
	}

	public void setBDatengeaendert(Short bDatengeaendert) {
		this.bDatengeaendert = bDatengeaendert;
	}

	@Column(name = "F_WECHSELKURSMANDANTWAEHRUNGZUAGSTKLWAEHRUNG")
	private Double fWechselkursmandantwaehrungzuagstklwaehrung;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "BELEGART_C_NR")
	private String belegartCNr;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "WAEHRUNG_C_NR")
	private String waehrungCNr;

	@Column(name = "ANSPRECHPARTNER_I_ID_KUNDE")
	private Integer ansprechpartnerIIdKunde;

	@Column(name = "KUNDE_I_ID")
	private Integer kundeIId;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "STUECKLISTE_I_ID")
	private Integer stuecklisteIId;

	public Integer getStuecklisteIId() {
		return this.stuecklisteIId;
	}

	public void setStuecklisteIId(Integer stuecklisteIId) {
		this.stuecklisteIId = stuecklisteIId;
	}

	

	@Column(name = "PROJEKT_I_ID")
	private Integer projektIId;

	public Integer getProjektIId() {
		return projektIId;
	}

	public void setProjektIId(Integer projektIId) {
		this.projektIId = projektIId;
	}

	@Column(name = "I_EKPREISBASIS")
	private Integer iEkpreisbasis;

	public Integer getIEkpreisbasis() {
		return iEkpreisbasis;
	}

	public void setIEkpreisbasis(Integer iEkpreisbasis) {
		this.iEkpreisbasis = iEkpreisbasis;
	}

	private static final long serialVersionUID = 1L;

	public Agstkl() {
		super();
	}

	public Agstkl(Integer id, java.lang.String mandantCNr, java.lang.String nr, java.lang.String belegartCNr,
			Integer kundeIId, Timestamp belegdatum, java.lang.String waehrungCNr,
			Double wechselkursmandantwaehrungzuagstklwaehrung, Integer personalIIdAnlegen, Integer personalIIdAendern,
			Integer iEkpreisbasis, Short bDatengeaendert, Short bVorlage) {
		setIId(id);
		setMandantCNr(mandantCNr);
		setCNr(nr);
		setBelegartCNr(belegartCNr);
		setKundeIId(kundeIId);
		setWaehrungCNr(waehrungCNr);
		setFWechselkursmandantwaehrungzuagstklwaehrung(wechselkursmandantwaehrungzuagstklwaehrung);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setPersonalIIdAendern(personalIIdAendern);
		Timestamp t = new Timestamp(System.currentTimeMillis());
		setTAnlegen(t);
		setTAendern(t);
		setTBelegdatum(belegdatum);
		setIEkpreisbasis(iEkpreisbasis);
		setBDatengeaendert(bDatengeaendert);
		setBVorlage(bVorlage);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCNr() {
		return this.cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public Timestamp getTBelegdatum() {
		return this.tBelegdatum;
	}

	public void setTBelegdatum(Timestamp tBelegdatum) {
		this.tBelegdatum = tBelegdatum;
	}

	public Double getFWechselkursmandantwaehrungzuagstklwaehrung() {
		return this.fWechselkursmandantwaehrungzuagstklwaehrung;
	}

	public void setFWechselkursmandantwaehrungzuagstklwaehrung(Double fWechselkursmandantwaehrungzuagstklwaehrung) {
		this.fWechselkursmandantwaehrungzuagstklwaehrung = fWechselkursmandantwaehrungzuagstklwaehrung;
	}

	public Timestamp getTAnlegen() {
		return this.tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public String getBelegartCNr() {
		return this.belegartCNr;
	}

	public void setBelegartCNr(String belegartCNr) {
		this.belegartCNr = belegartCNr;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandant) {
		this.mandantCNr = mandant;
	}

	public String getWaehrungCNr() {
		return this.waehrungCNr;
	}

	public void setWaehrungCNr(String waehrungCNr) {
		this.waehrungCNr = waehrungCNr;
	}

	public Integer getAnsprechpartnerIIdKunde() {
		return this.ansprechpartnerIIdKunde;
	}

	public void setAnsprechpartnerIIdKunde(Integer ansprechpartnerIIdKunde) {
		this.ansprechpartnerIIdKunde = ansprechpartnerIIdKunde;
	}

	public Integer getKundeIId() {
		return this.kundeIId;
	}

	public void setKundeIId(Integer kundeIId) {
		this.kundeIId = kundeIId;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

}

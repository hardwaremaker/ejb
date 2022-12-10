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
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.system.service.ITablenames;

@NamedQueries( { @NamedQuery(name = "LandfindByLkzLandName", query = "SELECT OBJECT (o) FROM Land o WHERE o.cLkz = ?1 and o.cName = ?2"),
				 @NamedQuery(name = "LandfindByLkz", query = "SELECT OBJECT (o) FROM Land o WHERE o.cLkz = ?1")
	})
@Entity
@Table(name = ITablenames.LP_LAND)
public class Land implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_LKZ")
	private String cLkz;

	@Column(name = "C_NAME")
	private String cName;

	@Column(name = "C_TELVORWAHL")
	private String cTelvorwahl;

	@Column(name = "T_EUMITGLIED_VON")
	private Date tEumitgliedVon;

	@Column(name = "N_UIDNUMMERPRUEFENABBETRAG")
	private BigDecimal nUidnummerpruefenabbetrag;

	@Column(name = "I_LAENGEUIDNUMMER")
	private Integer iLaengeuidnummer;

	@Column(name = "WAEHRUNG_C_NR")
	private String waehrungCNr;

	@Column(name = "C_USTCODE")
	private String cUstcode;
	
	@Column(name = "B_SEPA")
	private Short bSepa;
	
	@Column(name = "B_PRAEFERENZBEGUENSTIGT")
	private Short bPraeferenzbeguenstigt;
	
	public Short getBPraeferenzbeguenstigt() {
		return bPraeferenzbeguenstigt;
	}

	public void setBPraeferenzbeguenstigt(Short bPraeferenzbeguenstigt) {
		this.bPraeferenzbeguenstigt = bPraeferenzbeguenstigt;
	}

	@Column(name = "B_POSTFACHMITSTRASSE")
	private Short bPostfachmitstrasse;
	
	public Short getBPostfachmitstrasse() {
		return bPostfachmitstrasse;
	}

	public void setBPostfachmitstrasse(Short bPostfachmitstrasse) {
		this.bPostfachmitstrasse = bPostfachmitstrasse;
	}

	@Column(name = "B_PLZNACHORT")
	private Short bPlznachort;
	
	
	public Short getBPlznachort() {
		return bPlznachort;
	}

	public void setBPlznachort(Short bPlznachort) {
		this.bPlznachort = bPlznachort;
	}

	@Column(name = "F_GMTVERSATZ")
	private Double fGmtversatz;
	
	@Column(name = "N_MUENZRUNDUNG")
	private BigDecimal nMuenzRundung ;
	
	@Column(name = "LAND_I_ID_GEMEINSAMESPOSTLAND")
	private Integer landIIdGemeinsamespostland;
	
	@Column(name = "B_MWSTMUENZRUNDUNG")
	private Short bMwstMuenzrundung;

	@Column(name = "T_EUMITGLIED_BIS")
	private Date tEumitgliedBis;

	public Integer getLandIIdGemeinsamespostland() {
		return landIIdGemeinsamespostland;
	}

	public void setLandIIdGemeinsamespostland(Integer landIIdGemeinsamespostland) {
		this.landIIdGemeinsamespostland = landIIdGemeinsamespostland;
	}

	public Short getBSepa() {
		return bSepa;
	}

	public void setBSepa(Short sepa) {
		bSepa = sepa;
	}

	public Double getFGmtversatz() {
		return fGmtversatz;
	}

	public void setFGmtversatz(Double gmtversatz) {
		fGmtversatz = gmtversatz;
	}

	private static final long serialVersionUID = 1L;

	public Land() {
		super();
	}

	public Land(Integer iid, String lkz, String name, Integer laengeuidnummer, Short bSepa, Short bPlznachort,Short bPostfachmitstrasse, Short bMwstMuenzrundung, Short bPraeferenzbeguenstigt) {
		setIId(iid);
		setCLkz(lkz);
		setCName(name);
		setILaengeuidnummer(laengeuidnummer);
		setBSepa(bSepa);
		setBPlznachort(bPlznachort);
		setBPostfachmitstrasse(bPostfachmitstrasse);
		setBMwstMuenzrundung(bMwstMuenzrundung);
		setBPraeferenzbeguenstigt(bPraeferenzbeguenstigt);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCLkz() {
		return this.cLkz;
	}

	public void setCLkz(String cLkz) {
		this.cLkz = cLkz;
	}

	public String getCName() {
		return this.cName;
	}

	public void setCName(String cName) {
		this.cName = cName;
	}

	public String getCTelvorwahl() {
		return this.cTelvorwahl;
	}

	public void setCTelvorwahl(String cTelvorwahl) {
		this.cTelvorwahl = cTelvorwahl;
	}

	public Date getTEumitgliedVon() {
		return this.tEumitgliedVon;
	}

	public void setTEumitgliedVon(Date tEumitgliedVon) {
		this.tEumitgliedVon = tEumitgliedVon;
	}

	public BigDecimal getNUidnummerpruefenabbetrag() {
		return this.nUidnummerpruefenabbetrag;
	}

	public void setNUidnummerpruefenabbetrag(
			BigDecimal nUidnummerpruefenabbetrag) {
		this.nUidnummerpruefenabbetrag = nUidnummerpruefenabbetrag;
	}

	public Integer getILaengeuidnummer() {
		return this.iLaengeuidnummer;
	}

	public void setILaengeuidnummer(Integer iLaengeuidnummer) {
		this.iLaengeuidnummer = iLaengeuidnummer;
	}

	public String getWaehrungCNr() {
		return this.waehrungCNr;
	}

	public void setWaehrungCNr(String waehrung) {
		this.waehrungCNr = waehrung;
	}

	public void setCUstcode(String cUstcode) {
		this.cUstcode = cUstcode;
	}

	public String getCUstcode() {
		return cUstcode;
	}

	/**
	 * Den Rundungsbetrag fuer die Muenzrundung ermitteln
	 * @return null wenn keine Rundung erwuenscht ist, ansonsten den Betrag (0.05CHF/EUR fuer Schweiz/Finnland)
	 */
	public BigDecimal getNMuenzRundung() {
		return nMuenzRundung;
	}

	/**
	 * Den Rundungsbetrag fuer die Muenzrundung setzen
	 * 
	 * @param nMuenzRundung ist null wenn keine Rundung 
	 * 	erwuenscht ist, ansonsten der gewuenschte Betrag
	 */
	public void setNMuenzRundung(BigDecimal nMuenzRundung) {
		this.nMuenzRundung = nMuenzRundung;
	}
	
	/**
	 * Soll auch der Mehrwertsteuerbetrag mittels M&uuml;nzrundung gerundet werden?
	 * @return true wenn der Mehrwertsteuerbetrag gerundet werden soll
	 */
	public Short getBMwstMuenzrundung() {
		return bMwstMuenzrundung;
	}

	/**
	 * Rundung des Mehrwertsteuerbetrages aktivieren
	 * 
	 * @param bMwstMuenzrundung true wenn der Mehrwertsteuerbetrag ebenfalls nach
	 *   der M&uuml;nzrundung gerundet werden soll
	 */
	public void setBMwstMuenzrundung(Short bMwstMuenzrundung) {
		this.bMwstMuenzrundung = bMwstMuenzrundung;
	}
	
	public Date getTEumitgliedBis() {
		return tEumitgliedBis;
	}
	
	public void setTEumitgliedBis(Date tEumitgliedBis) {
		this.tEumitgliedBis = tEumitgliedBis;
	}
}

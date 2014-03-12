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
import java.sql.Time;
import java.sql.Timestamp;

public class AuftragzeitenDto extends TabelleDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String maschinengruppe;
	
	public String getMaschinengruppe() {
		return maschinengruppe;
	}


	private Integer iArbeitsgang=null;
	private Integer iUnterarbeitsgang=null;
	
	public Integer getiArbeitsgang() {
		return iArbeitsgang;
	}


	public void setiArbeitsgang(Integer iArbeitsgang) {
		this.iArbeitsgang = iArbeitsgang;
	}


	public Integer getiUnterarbeitsgang() {
		return iUnterarbeitsgang;
	}


	public void setiUnterarbeitsgang(Integer iUnterarbeitsgang) {
		this.iUnterarbeitsgang = iUnterarbeitsgang;
	}


	public void setMaschinengruppe(String maschinengruppe) {
		this.maschinengruppe = maschinengruppe;
	}

	private String sPersonalnummer;
	private String sPersonalMaschinenname;
	private String sPersonNachnameVorname = "";

	public String getsPersonNachnameVorname() {
		return sPersonNachnameVorname;
	}


	public void setsPersonNachnameVorname(String sPersonNachnameVorname) {
		this.sPersonNachnameVorname = sPersonNachnameVorname;
	}

	private String sArtikelcnr;
	private String sArtikelbezeichnung;
	private String sArtikelzusatzbezeichnung;
	public String getSArtikelzusatzbezeichnung() {
		return sArtikelzusatzbezeichnung;
	}


	public void setSArtikelzusatzbezeichnung(String sArtikelzusatzbezeichnung) {
		this.sArtikelzusatzbezeichnung = sArtikelzusatzbezeichnung;
	}

	private String sZeitbuchungtext;
	private String sKommentar;
	private Integer zeitdatenIIdBelegbuchung;

	public Integer getZeitdatenIIdBelegbuchung() {
		return zeitdatenIIdBelegbuchung;
	}

	public void setZeitdatenIIdBelegbuchung(Integer zeitdatenIIdBelegbuchung) {
		this.zeitdatenIIdBelegbuchung = zeitdatenIIdBelegbuchung;
	}

	public String getSKommentar() {
		return sKommentar;
	}

	public void setSKommentar(String kommentar) {
		sKommentar = kommentar;
	}

	private boolean bPerson;
	private Integer artikelIId;
	private Timestamp tsBeginn;
	private Timestamp tsEnde;
	private String sBewegungsart;
	private Double ddDauer;
	private Time tDauer;
	private BigDecimal bdKosten;
	private Integer artikelgruppeIId;
	private Integer artikelklasseIId;
	private Integer iPersonalMaschinenId;
	private Integer belegpositionIId;

	public Integer getBelegpositionIId() {
		return belegpositionIId;
	}

	public void setBelegpositionIId(Integer belegpositionIId) {
		this.belegpositionIId = belegpositionIId;
	}

	public String getSPersonalnummer() {
		return sPersonalnummer;
	}

	public void setSPersonalnummer(String sPersonalnummer) {
		this.sPersonalnummer = sPersonalnummer;
	}

	public String getSPersonalMaschinenname() {
		return sPersonalMaschinenname;
	}

	public void setSPersonalMaschinenname(String sPersonalMaschinenname) {
		this.sPersonalMaschinenname = sPersonalMaschinenname;
	}

	public String getSArtikelcnr() {
		return sArtikelcnr;
	}

	public void setSArtikelcnr(String sArtikelcnr) {
		this.sArtikelcnr = sArtikelcnr;
	}

	public String getSArtikelbezeichnung() {
		return sArtikelbezeichnung;
	}

	public void setSArtikelbezeichnung(String sArtikelbezeichnung) {
		this.sArtikelbezeichnung = sArtikelbezeichnung;
	}

	public String getSZeitbuchungtext() {
		return sZeitbuchungtext;
	}

	public void setSZeitbuchungtext(String sZeitbuchungtext) {
		this.sZeitbuchungtext = sZeitbuchungtext;
	}

	public Timestamp getTsBeginn() {
		return tsBeginn;
	}

	public void setTsBeginn(Timestamp tsBeginn) {
		this.tsBeginn = tsBeginn;
	}

	public Double getDdDauer() {
		return ddDauer;
	}

	public void setDdDauer(Double ddDauer) {
		this.ddDauer = ddDauer;
	}

	public BigDecimal getBdKosten() {
		return bdKosten;
	}

	public Timestamp getTsEnde() {
		return tsEnde;
	}

	public String getSBewegungsart() {
		return sBewegungsart;
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public Time getTDauer() {
		return tDauer;
	}

	public boolean isBPerson() {
		return bPerson;
	}

	public Integer getArtikelgruppeIId() {
		return artikelgruppeIId;
	}

	public Integer getArtikelklasseIId() {
		return artikelklasseIId;
	}

	public Integer getIPersonalMaschinenId() {
		return iPersonalMaschinenId;
	}

	public void setSBewegungsart(String sBewegungsart) {
		this.sBewegungsart = sBewegungsart;
	}

	public void setBdKosten(BigDecimal bdKosten) {
		this.bdKosten = bdKosten;
	}

	public void setTsEnde(Timestamp tsEnde) {
		this.tsEnde = tsEnde;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public void setTDauer(Time tDauer) {
		this.tDauer = tDauer;
	}

	public void setBPerson(boolean bPerson) {
		this.bPerson = bPerson;
	}

	public void setArtikelgruppeIId(Integer artikelgruppeIId) {
		this.artikelgruppeIId = artikelgruppeIId;
	}

	public void setArtikelklasseIId(Integer artikelklasseIId) {
		this.artikelklasseIId = artikelklasseIId;
	}

	public void setIPersonalMaschinenId(Integer iPersonalMaschinenId) {
		this.iPersonalMaschinenId = iPersonalMaschinenId;
	}
}

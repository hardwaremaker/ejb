/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2016 HELIUM V IT-Solutions GmbH
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
package com.lp.server.finanz.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class SepaKontoauszugV1 implements Serializable {

	/**
	 * Diese Id darf NIE geaendert werden
	 */
	private static final long serialVersionUID = 4012512437648015725L;

	private SepaKontoauszugVersionEnum eVersion;
	private String cCamtVersion;
	private Date dErstellung;
	private String cEmpfaenger;
	private String cMessageId;
	private String cAuszugId;
	private Integer iSeitennummer;
	private Boolean bIstLetzteSeite;
	private BigDecimal bdElektronischeAuszugsNr;
	private BigDecimal bdAuszugsNr;
	private SepaKontoinformationV1 kontoinformation;
	private List<SepaSaldoV1> salden;
	private List<SepaBuchungV1> buchungen;
	
	public SepaKontoauszugV1() {
	}

	public SepaKontoauszugVersionEnum getEVersion() {
		return eVersion;
	}

	public void setEVersion(SepaKontoauszugVersionEnum eVersion) {
		this.eVersion = eVersion;
	}

	public String getCCampVersion() {
		return cCamtVersion;
	}

	public void setCCampVersion(String cCamtVersion) {
		this.cCamtVersion = cCamtVersion;
	}

	public Date getDErstellung() {
		return dErstellung;
	}

	public void setDErstellung(Date dErstellung) {
		this.dErstellung = dErstellung;
	}

	public String getCEmpfaenger() {
		return cEmpfaenger;
	}

	public void setCEmpfaenger(String cEmpfaenger) {
		this.cEmpfaenger = cEmpfaenger;
	}

	public String getCMessageId() {
		return cMessageId;
	}

	public void setCMessageId(String cMessageId) {
		this.cMessageId = cMessageId;
	}

	public String getCAuszugId() {
		return cAuszugId;
	}

	public void setCAuszugId(String cAuszugId) {
		this.cAuszugId = cAuszugId;
	}

	public Integer getISeitennummer() {
		return iSeitennummer;
	}

	public void setISeitennummer(Integer iSeitennummer) {
		this.iSeitennummer = iSeitennummer;
	}

	public Boolean getBIstLetzteSeite() {
		return bIstLetzteSeite;
	}

	public void setBIstLetzteSeite(Boolean bIstLetzteSeite) {
		this.bIstLetzteSeite = bIstLetzteSeite;
	}

	public BigDecimal getBdElektronischeAuszugsNr() {
		return bdElektronischeAuszugsNr;
	}

	public void setBdElektronischeAuszugsNr(BigDecimal bdElektronischeAuszugsNr) {
		this.bdElektronischeAuszugsNr = bdElektronischeAuszugsNr;
	}

	public BigDecimal getBdAuszugsNr() {
		return bdAuszugsNr;
	}

	public void setBdAuszugsNr(BigDecimal bdAuszugsNr) {
		this.bdAuszugsNr = bdAuszugsNr;
	}

	public SepaKontoinformationV1 getKontoinformation() {
		return kontoinformation;
	}

	public void setKontoinformation(SepaKontoinformationV1 kontoinformation) {
		this.kontoinformation = kontoinformation;
	}

	public List<SepaSaldoV1> getSalden() {
		return salden;
	}

	public void setSalden(List<SepaSaldoV1> salden) {
		this.salden = salden;
	}

	public List<SepaBuchungV1> getBuchungen() {
		return buchungen;
	}

	public void setBuchungen(List<SepaBuchungV1> buchungen) {
		this.buchungen = buchungen;
	}
	
}

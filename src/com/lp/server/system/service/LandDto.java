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
package com.lp.server.system.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

public class LandDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iID;
	private String cLkz;
	private String cName;
	private String cTelvorwahl;

	private Integer landIIdGemeinsamespostland;

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

	private String waehrungCNr;
	private Date tEUMitglied;
	private BigDecimal nUidnummerpruefenabbetrag;
	private Integer iLaengeuidnummer;
	private String cUstcode;
	private Short bSepa;
	private Double fGmtversatz;
	private BigDecimal nMuenzRundung;

	public void setTEUMitglied(Date tEUMitglied) {
		this.tEUMitglied = tEUMitglied;
	}

	public Date getEUMitglied() {
		return tEUMitglied;
	}

	public String getCLkz() {
		return cLkz;
	}

	public void setCLkz(String cLkz) {
		this.cLkz = cLkz;
	}

	public String getCName() {
		return cName;
	}

	public void setCName(String cName) {
		this.cName = cName;
	}

	public String getCTelvorwahl() {
		return cTelvorwahl;
	}

	public void setCTelvorwahl(String cTelvorwahl) {
		this.cTelvorwahl = cTelvorwahl;
	}

	public String getWaehrungCNr() {
		return waehrungCNr;
	}

	public Integer getIID() {
		return iID;
	}

	public BigDecimal getNUidnummerpruefenabbetrag() {
		return nUidnummerpruefenabbetrag;
	}

	public Integer getILaengeuidnummer() {
		return iLaengeuidnummer;
	}

	public void setWaehrungCNr(String waehrungCNr) {
		this.waehrungCNr = waehrungCNr;
	}

	private Short bPlznachort;

	public Short getBPlznachort() {
		return bPlznachort;
	}

	public void setBPlznachort(Short bPlznachort) {
		this.bPlznachort = bPlznachort;
	}

	public void setIID(Integer iID) {
		this.iID = iID;
	}

	public void setNUidnummerpruefenabbetrag(
			BigDecimal nUidnummerpruefenabbetrag) {
		this.nUidnummerpruefenabbetrag = nUidnummerpruefenabbetrag;
	}

	public void setILaengeuidnummer(Integer iLaengeuidnummer) {
		this.iLaengeuidnummer = iLaengeuidnummer;
	}

	public void setCUstcode(String cUstcode) {
		this.cUstcode = cUstcode;
	}

	public String getCUstcode() {
		return cUstcode;
	}

	public BigDecimal getNMuenzRundung() {
		return nMuenzRundung;
	}

	public void setNMuenzRundung(BigDecimal nMuenzRundung) {
		this.nMuenzRundung = nMuenzRundung;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof LandDto)) {
			return false;
		}
		LandDto that = (LandDto) obj;
		if (!(that.cLkz == null ? this.cLkz == null : that.cLkz
				.equals(this.cLkz))) {
			return false;
		}
		if (!(that.cName == null ? this.cName == null : that.cName
				.equals(this.cName))) {
			return false;
		}
		if (!(that.cTelvorwahl == null ? this.cTelvorwahl == null
				: that.cTelvorwahl.equals(this.cTelvorwahl))) {
			return false;
		}
		if (!(that.waehrungCNr == null ? this.waehrungCNr == null
				: that.waehrungCNr.equals(this.waehrungCNr))) {
			return false;
		}
		if (!(that.tEUMitglied == null ? this.tEUMitglied == null
				: that.tEUMitglied.equals(this.tEUMitglied))) {
			return false;
		}
		if (!(that.nUidnummerpruefenabbetrag == null ? this.nUidnummerpruefenabbetrag == null
				: that.nUidnummerpruefenabbetrag
						.equals(this.nUidnummerpruefenabbetrag))) {
			return false;
		}
		if (!(that.cUstcode == null ? this.cUstcode == null : that.cUstcode
				.equals(this.cUstcode))) {
			return false;
		}
		if (!(that.nMuenzRundung == null ? this.nMuenzRundung == null
				: that.nMuenzRundung.equals(this.nMuenzRundung))) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.cLkz.hashCode();
		result = 37 * result + this.cName.hashCode();
		result = 37 * result + this.cTelvorwahl.hashCode();
		result = 37 * result + this.waehrungCNr.hashCode();
		result = 37 * result + this.tEUMitglied.hashCode();
		result = 37 * result + this.nUidnummerpruefenabbetrag.hashCode();
		result = 37 * result + this.cUstcode.hashCode();
		result = 37 * result + this.nMuenzRundung.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += cLkz;
		returnString += ", " + cName;
		returnString += ", " + cTelvorwahl;
		returnString += ", " + waehrungCNr;
		returnString += ", " + tEUMitglied;
		returnString += ", " + nUidnummerpruefenabbetrag;
		returnString += ", " + cUstcode;
		returnString += ", " + nMuenzRundung;
		return returnString;
	}

}

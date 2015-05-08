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
package com.lp.server.bestellung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class WareneingangDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer iSort;
	private String cLieferscheinnr;
	private Timestamp tLieferscheindatum;
	private BigDecimal nTransportkosten;
	private Double dGemeinkostenfaktor;
	private Double fRabattsatz;
	private Timestamp tWareneingangsdatum;
	private Integer bestellungIId;
	private Integer lagerIId;
	private BigDecimal nWechselkurs;
	private Integer eingangsrechnungIId;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getISort() {
		return iSort;
	}

	public void setNWechselkurs(BigDecimal nWechselkurs) {
		this.nWechselkurs = nWechselkurs;
	}

	public BigDecimal getNWechselkurs() {
		return nWechselkurs;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public String getCLieferscheinnr() {
		return cLieferscheinnr;
	}

	public void setCLieferscheinnr(String cLieferscheinnr) {
		this.cLieferscheinnr = cLieferscheinnr;
	}

	public Timestamp getTLieferscheindatum() {
		return tLieferscheindatum;
	}

	public void setTLieferscheindatum(Timestamp tLieferscheindatum) {
		this.tLieferscheindatum = tLieferscheindatum;
	}

	public BigDecimal getSummeTransportkostenUndSpesen() {
		BigDecimal bdSumme = new BigDecimal(0);

		if (nTransportkosten != null) {
			bdSumme = bdSumme.add(nTransportkosten);
		}
		if (nZollkosten != null) {
			bdSumme = bdSumme.add(nZollkosten);
		}
		if (nBankspesen != null) {
			bdSumme = bdSumme.add(nBankspesen);
		}
		if (nSonstigespesen != null) {
			bdSumme = bdSumme.add(nSonstigespesen);
		}
		return bdSumme;
	}

	private BigDecimal nZollkosten;

	public BigDecimal getNZollkosten() {
		return nZollkosten;
	}

	public void setNZollkosten(BigDecimal nZollkosten) {
		this.nZollkosten = nZollkosten;
	}

	public BigDecimal getNBankspesen() {
		return nBankspesen;
	}

	public void setNBankspesen(BigDecimal nBankspesen) {
		this.nBankspesen = nBankspesen;
	}

	public BigDecimal getNSonstigespesen() {
		return nSonstigespesen;
	}

	public void setNSonstigespesen(BigDecimal nSonstigespesen) {
		this.nSonstigespesen = nSonstigespesen;
	}

	private BigDecimal nBankspesen;
	private BigDecimal nSonstigespesen;

	public BigDecimal getNTransportkosten() {
		return nTransportkosten;
	}

	public void setNTransportkosten(BigDecimal nTransportkosten) {
		this.nTransportkosten = nTransportkosten;
	}

	public Double getDGemeinkostenfaktor() {
		return dGemeinkostenfaktor;
	}

	public void setDGemeinkostenfaktor(Double dGemeinkostenfaktor) {
		this.dGemeinkostenfaktor = dGemeinkostenfaktor;
	}

	public Double getFRabattsatz() {
		return fRabattsatz;
	}

	public void setFRabattsatz(Double fRabattsatz) {
		this.fRabattsatz = fRabattsatz;
	}

	public Timestamp getTWareneingangsdatum() {
		return tWareneingangsdatum;
	}

	public void setTWareneingangsdatum(Timestamp tWareneingangsdatum) {
		this.tWareneingangsdatum = tWareneingangsdatum;
	}

	public Integer getBestellungIId() {
		return bestellungIId;
	}

	public void setBestellungIId(Integer bestellungIId) {
		this.bestellungIId = bestellungIId;
	}

	public Integer getLagerIId() {
		return lagerIId;
	}

	public void setLagerIId(Integer lagerIId) {
		this.lagerIId = lagerIId;
	}

	public Integer getEingangsrechnungIId() {
		return eingangsrechnungIId;
	}

	public void setEingangsrechnungIId(Integer eingangsrechnungIId) {
		this.eingangsrechnungIId = eingangsrechnungIId;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof WareneingangDto)) {
			return false;
		}
		WareneingangDto that = (WareneingangDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.iSort == null ? this.iSort == null : that.iSort
				.equals(this.iSort))) {
			return false;
		}
		if (!(that.cLieferscheinnr == null ? this.cLieferscheinnr == null
				: that.cLieferscheinnr.equals(this.cLieferscheinnr))) {
			return false;
		}
		if (!(that.tLieferscheindatum == null ? this.tLieferscheindatum == null
				: that.tLieferscheindatum.equals(this.tLieferscheindatum))) {
			return false;
		}
		if (!(that.nTransportkosten == null ? this.nTransportkosten == null
				: that.nTransportkosten.equals(this.nTransportkosten))) {
			return false;
		}
		if (!(that.dGemeinkostenfaktor == null ? this.dGemeinkostenfaktor == null
				: that.dGemeinkostenfaktor.equals(this.dGemeinkostenfaktor))) {
			return false;
		}
		if (!(that.fRabattsatz == null ? this.fRabattsatz == null
				: that.fRabattsatz.equals(this.fRabattsatz))) {
			return false;
		}
		if (!(that.tWareneingangsdatum == null ? this.tWareneingangsdatum == null
				: that.tWareneingangsdatum.equals(this.tWareneingangsdatum))) {
			return false;
		}
		if (!(that.bestellungIId == null ? this.bestellungIId == null
				: that.bestellungIId.equals(this.bestellungIId))) {
			return false;
		}
		if (!(that.lagerIId == null ? this.lagerIId == null : that.lagerIId
				.equals(this.lagerIId))) {
			return false;
		}
		if (!(that.nWechselkurs == null ? this.nWechselkurs == null
				: that.nWechselkurs.equals(this.nWechselkurs))) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.iSort.hashCode();
		result = 37 * result + this.cLieferscheinnr.hashCode();
		result = 37 * result + this.tLieferscheindatum.hashCode();
		result = 37 * result + this.nTransportkosten.hashCode();
		result = 37 * result + this.dGemeinkostenfaktor.hashCode();
		result = 37 * result + this.fRabattsatz.hashCode();
		result = 37 * result + this.tWareneingangsdatum.hashCode();
		result = 37 * result + this.bestellungIId.hashCode();
		result = 37 * result + this.lagerIId.hashCode();
		result = 37 * result + this.nWechselkurs.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + iSort;
		returnString += ", " + cLieferscheinnr;
		returnString += ", " + tLieferscheindatum;
		returnString += ", " + nTransportkosten;
		returnString += ", " + dGemeinkostenfaktor;
		returnString += ", " + fRabattsatz;
		returnString += ", " + tWareneingangsdatum;
		returnString += ", " + bestellungIId;
		returnString += ", " + lagerIId;
		returnString += ", " + nWechselkurs;
		return returnString;
	}
}

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
package com.lp.server.personal.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class GleitzeitsaldoDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer personalIId;
	private Integer iJahr;
	private Integer iMonat;
	private BigDecimal nSaldomehrstunden;
	private BigDecimal nSaldouestfrei50;
	private BigDecimal nSaldouestpflichtig50;
	private BigDecimal nSaldouestfrei100;
	private BigDecimal nSaldouestpflichtig100;
	// private BigDecimal nSaldouestdpauschale;
	private Short bGesperrt;
	private Integer personalIIdAendern;
	private Timestamp tAendern;
	private Timestamp dAbrechnungstichtag;
	private BigDecimal nSaldo;
	private BigDecimal nSaldouest200;
	
	
	public BigDecimal getNSaldouest200() {
		return nSaldouest200;
	}

	public void setNSaldouest200(BigDecimal saldouest200) {
		nSaldouest200 = saldouest200;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getPersonalIId() {
		return personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

	public Integer getIJahr() {
		return iJahr;
	}

	public void setIJahr(Integer iJahr) {
		this.iJahr = iJahr;
	}

	public Integer getIMonat() {
		return iMonat;
	}

	public void setIMonat(Integer iMonat) {
		this.iMonat = iMonat;
	}

	public BigDecimal getNSaldomehrstunden() {
		return nSaldomehrstunden;
	}

	public void setNSaldomehrstunden(BigDecimal nSaldomehrstunden) {
		this.nSaldomehrstunden = nSaldomehrstunden;
	}

	public BigDecimal getNSaldouestfrei50() {
		return nSaldouestfrei50;
	}

	public void setNSaldouestfrei50(BigDecimal nSaldouestfrei50) {
		this.nSaldouestfrei50 = nSaldouestfrei50;
	}

	public BigDecimal getNSaldouestpflichtig50() {
		return nSaldouestpflichtig50;
	}

	public void setNSaldouestpflichtig50(BigDecimal nSaldouestpflichtig50) {
		this.nSaldouestpflichtig50 = nSaldouestpflichtig50;
	}

	public BigDecimal getNSaldouestfrei100() {
		return nSaldouestfrei100;
	}

	public void setNSaldouestfrei100(BigDecimal nSaldouestfrei100) {
		this.nSaldouestfrei100 = nSaldouestfrei100;
	}

	public BigDecimal getNSaldouestpflichtig100() {
		return nSaldouestpflichtig100;
	}

	public void setNSaldouestpflichtig100(BigDecimal nSaldouestpflichtig100) {
		this.nSaldouestpflichtig100 = nSaldouestpflichtig100;
	}

	/*
	 * public BigDecimal getNSaldouestdpauschale() { return
	 * nSaldouestdpauschale; }
	 */

	// public void setNSaldouestdpauschale(BigDecimal nSaldouestdpauschale) {
	// this.nSaldouestdpauschale = nSaldouestdpauschale;
	// }
	public Short getBGesperrt() {
		return bGesperrt;
	}

	public void setBGesperrt(Short bGesperrt) {
		this.bGesperrt = bGesperrt;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Timestamp getDAbrechnungstichtag() {
		return dAbrechnungstichtag;
	}

	public BigDecimal getNSaldo() {
		return nSaldo;
	}

	public void setDAbrechnungstichtag(Timestamp dAbrechnungstichtag) {
		this.dAbrechnungstichtag = dAbrechnungstichtag;
	}

	public void setNSaldo(BigDecimal nSaldo) {
		this.nSaldo = nSaldo;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof GleitzeitsaldoDto))
			return false;
		GleitzeitsaldoDto that = (GleitzeitsaldoDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.personalIId == null ? this.personalIId == null
				: that.personalIId.equals(this.personalIId)))
			return false;
		if (!(that.iJahr == null ? this.iJahr == null : that.iJahr
				.equals(this.iJahr)))
			return false;
		if (!(that.iMonat == null ? this.iMonat == null : that.iMonat
				.equals(this.iMonat)))
			return false;
		if (!(that.nSaldomehrstunden == null ? this.nSaldomehrstunden == null
				: that.nSaldomehrstunden.equals(this.nSaldomehrstunden)))
			return false;
		if (!(that.nSaldouestfrei50 == null ? this.nSaldouestfrei50 == null
				: that.nSaldouestfrei50.equals(this.nSaldouestfrei50)))
			return false;
		if (!(that.nSaldouestpflichtig50 == null ? this.nSaldouestpflichtig50 == null
				: that.nSaldouestpflichtig50.equals(this.nSaldouestpflichtig50)))
			return false;
		if (!(that.nSaldouestfrei100 == null ? this.nSaldouestfrei100 == null
				: that.nSaldouestfrei100.equals(this.nSaldouestfrei100)))
			return false;
		if (!(that.nSaldouestpflichtig100 == null ? this.nSaldouestpflichtig100 == null
				: that.nSaldouestpflichtig100
						.equals(this.nSaldouestpflichtig100)))
			return false;
		/*
		 * if (! (that.nSaldouestdpauschale == null ? this.nSaldouestdpauschale
		 * == null :
		 * that.nSaldouestdpauschale.equals(this.nSaldouestdpauschale))) return
		 * false;
		 */
		if (!(that.bGesperrt == null ? this.bGesperrt == null : that.bGesperrt
				.equals(this.bGesperrt)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern)))
			return false;
		if (!(that.dAbrechnungstichtag == null ? this.dAbrechnungstichtag == null
				: that.dAbrechnungstichtag.equals(this.dAbrechnungstichtag)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.personalIId.hashCode();
		result = 37 * result + this.iJahr.hashCode();
		result = 37 * result + this.iMonat.hashCode();
		result = 37 * result + this.nSaldomehrstunden.hashCode();
		result = 37 * result + this.nSaldouestfrei50.hashCode();
		result = 37 * result + this.nSaldouestpflichtig50.hashCode();
		result = 37 * result + this.nSaldouestfrei100.hashCode();
		result = 37 * result + this.nSaldouestpflichtig100.hashCode();
		// result = 37 * result + this.nSaldouestdpauschale.hashCode();
		result = 37 * result + this.bGesperrt.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.dAbrechnungstichtag.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + personalIId;
		returnString += ", " + iJahr;
		returnString += ", " + iMonat;
		returnString += ", " + nSaldomehrstunden;
		returnString += ", " + nSaldouestfrei50;
		returnString += ", " + nSaldouestpflichtig50;
		returnString += ", " + nSaldouestfrei100;
		returnString += ", " + nSaldouestpflichtig100;
		// returnString += ", " + nSaldouestdpauschale;
		returnString += ", " + bGesperrt;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + tAendern;
		returnString += ", " + dAbrechnungstichtag;
		return returnString;
	}
}

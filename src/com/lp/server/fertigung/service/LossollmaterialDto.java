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
package com.lp.server.fertigung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.lp.service.BelegpositionDto;
import com.lp.util.Helper;

public class LossollmaterialDto extends BelegpositionDto implements
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Float fDimension1;
	private Float fDimension2;
	private Float fDimension3;
	private String cPosition;
	private String cKommentar;
	private Integer montageartIId;
	private Integer iLfdnummer;
	private Short bNachtraeglich;
	private Timestamp tAendern;
	private Integer personalIIdAendern;
	private BigDecimal nSollpreis;
	private Integer iBeginnterminoffset;

	public Integer getIBeginnterminoffset() {
		return iBeginnterminoffset;
	}

	public void setIBeginnterminoffset(Integer iBeginnterminoffset) {
		this.iBeginnterminoffset = iBeginnterminoffset;
	}
	
	private Integer lossollmaterialIIdOriginal;
	
	public Integer getLossollmaterialIIdOriginal() {
		return lossollmaterialIIdOriginal;
	}

	public void setLossollmaterialIIdOriginal(Integer lossollmaterialIIdOriginal) {
		this.lossollmaterialIIdOriginal = lossollmaterialIIdOriginal;
	}

	public Integer getLosIId() {
		return getBelegIId();
	}

	public void setLosIId(Integer losIId) {
		setBelegIId(losIId);
	}

	public Float getFDimension1() {
		return fDimension1;
	}

	public void setFDimension1(Float fDimension1) {
		this.fDimension1 = fDimension1;
	}

	public Float getFDimension2() {
		return fDimension2;
	}

	public void setFDimension2(Float fDimension2) {
		this.fDimension2 = fDimension2;
	}

	public Float getFDimension3() {
		return fDimension3;
	}

	public void setFDimension3(Float fDimension3) {
		this.fDimension3 = fDimension3;
	}

	public String getCPosition() {
		return cPosition;
	}

	public void setCPosition(String cPosition) {
		this.cPosition = cPosition;
	}

	public String getCKommentar() {
		return cKommentar;
	}

	public void setCKommentar(String cKommentar) {
		this.cKommentar = cKommentar;
	}

	public Integer getMontageartIId() {
		return montageartIId;
	}

	public void setMontageartIId(Integer montageartIId) {
		this.montageartIId = montageartIId;
	}

	public Integer getILfdnummer() {
		return iLfdnummer;
	}

	public void setILfdnummer(Integer iLfdnummer) {
		this.iLfdnummer = iLfdnummer;
	}

	public Short getBNachtraeglich() {
		return bNachtraeglich;
	}

	public void setBNachtraeglich(Short bNachtraeglich) {
		this.bNachtraeglich = bNachtraeglich;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public BigDecimal getNSollpreis() {
		return nSollpreis;
	}

	public void setNSollpreis(BigDecimal nSollpreis) {
		this.nSollpreis = nSollpreis;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof LossollmaterialDto))
			return false;
		LossollmaterialDto that = (LossollmaterialDto) obj;
		if (!(that.fDimension1 == null ? this.fDimension1 == null
				: that.fDimension1.equals(this.fDimension1)))
			return false;
		if (!(that.fDimension2 == null ? this.fDimension2 == null
				: that.fDimension2.equals(this.fDimension2)))
			return false;
		if (!(that.fDimension3 == null ? this.fDimension3 == null
				: that.fDimension3.equals(this.fDimension3)))
			return false;
		if (!(that.cPosition == null ? this.cPosition == null : that.cPosition
				.equals(this.cPosition)))
			return false;
		if (!(that.cKommentar == null ? this.cKommentar == null
				: that.cKommentar.equals(this.cKommentar)))
			return false;
		if (!(that.montageartIId == null ? this.montageartIId == null
				: that.montageartIId.equals(this.montageartIId)))
			return false;
		if (!(that.iLfdnummer == null ? this.iLfdnummer == null
				: that.iLfdnummer.equals(this.iLfdnummer)))
			return false;
		if (!(that.bNachtraeglich == null ? this.bNachtraeglich == null
				: that.bNachtraeglich.equals(this.bNachtraeglich)))
			return false;
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		if (!(that.nSollpreis == null ? this.nSollpreis == null
				: that.nSollpreis.equals(this.nSollpreis)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.fDimension1.hashCode();
		result = 37 * result + this.fDimension2.hashCode();
		result = 37 * result + this.fDimension3.hashCode();
		result = 37 * result + this.cPosition.hashCode();
		result = 37 * result + this.cKommentar.hashCode();
		result = 37 * result + this.montageartIId.hashCode();
		result = 37 * result + this.iLfdnummer.hashCode();
		result = 37 * result + this.bNachtraeglich.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.nSollpreis.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += ", " + fDimension1;
		returnString += ", " + fDimension2;
		returnString += ", " + fDimension3;
		returnString += ", " + cPosition;
		returnString += ", " + cKommentar;
		returnString += ", " + montageartIId;
		returnString += ", " + iLfdnummer;
		returnString += ", " + bNachtraeglich;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + nSollpreis;
		return returnString;
	}

	/**
	 * rounddto: diese Methode rundet das ganze Dto ACHTUNG die beiden para
	 * 
	 * ACHTUNG: die beiden parameter gibt es nur, weil diese werte nicht
	 * konstant sind (-> mandantenparameter) Fuer alle "fixen" nachkommastellen
	 * sind AUSNAHMSLOS Konstanten zu verwenden
	 * 
	 * @param iStellenMenge
	 *            int
	 * @param iStellenPreis
	 *            int
	 */
	public void round(Integer iStellenMenge, Integer iStellenPreis) {
		// hier aber trotzdem mit 4, sonst vierlier ich genauigkeit
		setNMenge(Helper.rundeKaufmaennisch(getNMenge(), iStellenMenge));
		setNSollpreis(Helper.rundeKaufmaennisch(getNSollpreis(), iStellenPreis));
	}
}

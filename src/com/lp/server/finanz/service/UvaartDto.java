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
package com.lp.server.finanz.service;

import java.io.Serializable;

public class UvaartDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String mandantCNr;
	private String cNr;
	private String cKennzeichen;
	private short bInvertiert;
	
	
	private Short bKeineAuswahlBeiEr;

	public Short getBKeineAuswahlBeiEr() {
		return bKeineAuswahlBeiEr;
	}

	public void setBKeineAuswahlBeiEr(Short bKeineAuswahlBeiEr) {
		this.bKeineAuswahlBeiEr = bKeineAuswahlBeiEr;
	}
	
	private Integer iSort;

	private UvaartsprDto uvaartsprDto = null;

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public Integer getISort() {
		return iSort;
	}

	public UvaartsprDto getUvaartsprDto() {
		return uvaartsprDto;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public void setUvaartsprDto(UvaartsprDto uvaartsprDto) {
		this.uvaartsprDto = uvaartsprDto;
	}
	
	public short getBInvertiert() {
		return bInvertiert;
	}

	public void setBInvertiert(short invertiert) {
		bInvertiert = invertiert;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof UvaartDto))
			return false;
		UvaartDto that = (UvaartDto) obj;
		if (!(that.cNr == null ? this.cNr == null : that.cNr.equals(this.cNr)))
			return false;
		if (!(that.iSort == null ? this.iSort == null : that.iSort
				.equals(this.iSort)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.cNr.hashCode();
		result = 37 * result + this.cKennzeichen.hashCode();
		result = 37 * result + this.iSort.hashCode();
		result = 37 * result + this.bInvertiert;
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + mandantCNr;
		returnString += ", " + cNr;
		returnString += ", " + cKennzeichen;
		returnString += ", " + iSort;
		returnString += ", " + bInvertiert;
		return returnString;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getIId() {
		return iId;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setCKennzeichen(String cKennzeichen) {
		this.cKennzeichen = cKennzeichen;
	}

	public String getCKennzeichen() {
		return cKennzeichen;
	}
	
	public String getCnrUppercase() {
		return getCNr().toUpperCase().replaceAll(" ", "_");
	}
	
	public boolean isNichtZutreffend() {
		return FinanzServiceFac.UVAART_NICHT_ZUTREFFEND.equals(getCNr());
	}
	
	public boolean isInlandNormalsteuer() {
		return FinanzServiceFac.UVAART_INLAND_20.equals(getCNr());
	}
	
	public boolean isInlandReduziert() {
		return FinanzServiceFac.UVAART_INLAND_10.equals(getCNr());
	}
	
	public boolean isInlandSteuerfrei() {
		return FinanzServiceFac.UVAART_INLAND_STEUERFREI.equals(getCNr());
	}
	
	public boolean isEUAuslandMitUiD() {
		return FinanzServiceFac.UVAART_EU_AUSLAND_MIT_UID.equals(getCNr());
	}
	
	public boolean isExportDrittland() {
		return FinanzServiceFac.UVAART_EXPORT_DRITTLAND.equals(getCNr());
	}
	
	public boolean isIGErwerbNormalsteuer() {
		return FinanzServiceFac.UVAART_IG_ERWERB_20.equals(getCNr());
	}
	
	public boolean isIGErwerbReduziert() {
		return FinanzServiceFac.UVAART_IG_ERWERB_10.equals(getCNr());
	}
	
	public boolean isAnzahlungNormalsteuer() {
		return FinanzServiceFac.UVAART_ZAHLUNG_20.equals(getCNr());
	}
	
	public boolean isAnzahlungReduziert() {
		return FinanzServiceFac.UVAART_ZAHLUNG_10.equals(getCNr());
	}
	
	public boolean isNormalsteuer() {
		return isInlandNormalsteuer() ||
				isIGErwerbNormalsteuer() || isAnzahlungNormalsteuer();
	}
	
	public boolean isReduziertsteuer() {
		return isInlandReduziert() ||
				isIGErwerbReduziert() || isAnzahlungReduziert();
	}
	
	public boolean isSteuerfrei() {
		return isNichtZutreffend() || isEUAuslandMitUiD() ||
				isExportDrittland() || isInlandSteuerfrei();
	}
}

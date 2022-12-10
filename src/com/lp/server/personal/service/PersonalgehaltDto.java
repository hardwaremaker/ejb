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

public class PersonalgehaltDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iId;
	private Integer personalIId;
	private Integer iJahr;
	private Integer iMonat;
	private BigDecimal nGehalt;
	private BigDecimal nStundensatz;
	private Double fVerfuegbarkeit;
	private BigDecimal nKmgeld1;
	private Double fBiskilometer;
	private BigDecimal nKmgeld2;
	private Short bKksgebbefreit;
	private String cGrundkksgebbefreit;
	private Short bAlleinverdiener;
	private Short bAlleinerzieher;
	private Integer personalIIdAendern;
	private Timestamp tAendern;
	private Double fUestpauschale;
	private BigDecimal nUestdpuffer;
	private Integer iUestdauszahlen;
	private Double fLeistungswert;

	private BigDecimal nKmgeldMitfahrer;

	public BigDecimal getNKmgeldMitfahrer() {
		return nKmgeldMitfahrer;
	}

	public void setNKmgeldMitfahrer(BigDecimal nKmgeldMitfahrer) {
		this.nKmgeldMitfahrer = nKmgeldMitfahrer;
	}

	private Short bNegativstundenInUrlaubUmwandelnTageweiseBetrachten;

	public Short getBNegativstundenInUrlaubUmwandelnTageweiseBetrachten() {
		return bNegativstundenInUrlaubUmwandelnTageweiseBetrachten;
	}

	public void setBNegativstundenInUrlaubUmwandelnTageweiseBetrachten(
			Short bNegativstundenInUrlaubUmwandelnTageweiseBetrachten) {
		this.bNegativstundenInUrlaubUmwandelnTageweiseBetrachten = bNegativstundenInUrlaubUmwandelnTageweiseBetrachten;
	}

	private BigDecimal nDrzpauschale;

	public BigDecimal getNDrzpauschale() {
		return nDrzpauschale;
	}

	public void setNDrzpauschale(BigDecimal nDrzpauschale) {
		this.nDrzpauschale = nDrzpauschale;
	}

	private BigDecimal nLohnsteuer;

	public BigDecimal getNLohnsteuer() {
		return nLohnsteuer;
	}

	public void setNLohnsteuer(BigDecimal nLohnsteuer) {
		this.nLohnsteuer = nLohnsteuer;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Double getFLeistungswert() {
		return fLeistungswert;
	}

	public void setFLeistungswert(Double fLeistungswert) {
		this.fLeistungswert = fLeistungswert;
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

	public BigDecimal getNGehalt() {
		return nGehalt;
	}

	public void setNGehalt(BigDecimal nGehalt) {
		this.nGehalt = nGehalt;
	}

	public BigDecimal getNStundensatz() {
		return nStundensatz;
	}

	public void setNStundensatz(BigDecimal nStundensatz) {
		this.nStundensatz = nStundensatz;
	}

	public Double getFVerfuegbarkeit() {
		return fVerfuegbarkeit;
	}

	public void setFVerfuegbarkeit(Double fVerfuegbarkeit) {
		this.fVerfuegbarkeit = fVerfuegbarkeit;
	}

	public BigDecimal getNKmgeld1() {
		return nKmgeld1;
	}

	public void setNKmgeld1(BigDecimal nKmgeld1) {
		this.nKmgeld1 = nKmgeld1;
	}

	public Double getFBiskilometer() {
		return fBiskilometer;
	}

	public void setFBiskilometer(Double fBiskilometer) {
		this.fBiskilometer = fBiskilometer;
	}

	public BigDecimal getNKmgeld2() {
		return nKmgeld2;
	}

	public void setNKmgeld2(BigDecimal nKmgeld2) {
		this.nKmgeld2 = nKmgeld2;
	}

	public Short getBKksgebbefreit() {
		return bKksgebbefreit;
	}

	public void setBKksgebbefreit(Short bKksgebbefreit) {
		this.bKksgebbefreit = bKksgebbefreit;
	}

	public String getCGrundkksgebbefreit() {
		return cGrundkksgebbefreit;
	}

	public void setCGrundkksgebbefreit(String cGrundkksgebbefreit) {
		this.cGrundkksgebbefreit = cGrundkksgebbefreit;
	}

	public Short getBAlleinverdiener() {
		return bAlleinverdiener;
	}

	public void setBAlleinverdiener(Short bAlleinverdiener) {
		this.bAlleinverdiener = bAlleinverdiener;
	}

	public Short getBAlleinerzieher() {
		return bAlleinerzieher;
	}

	public void setBAlleinerzieher(Short bAlleinerzieher) {
		this.bAlleinerzieher = bAlleinerzieher;
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

	public Double getFUestpauschale() {
		return fUestpauschale;
	}

	public BigDecimal getNUestdpuffer() {
		return nUestdpuffer;
	}

	public void setFUestpauschale(Double fUestpauschale) {
		this.fUestpauschale = fUestpauschale;
	}

	public void setNUestdpuffer(BigDecimal nUestdpuffer) {
		this.nUestdpuffer = nUestdpuffer;
	}

	public Integer getIUestdauszahlen() {
		return this.iUestdauszahlen;
	}

	public void setIUestdauszahlen(Integer iUestdauszahlen) {
		this.iUestdauszahlen = iUestdauszahlen;
	}

	public BigDecimal getNGehaltNetto() {
		return nGehaltNetto;
	}

	public void setNGehaltNetto(BigDecimal nGehaltNetto) {
		this.nGehaltNetto = nGehaltNetto;
	}

	public BigDecimal getNGehaltBruttobrutto() {
		return nGehaltBruttobrutto;
	}

	public void setNGehaltBruttobrutto(BigDecimal nGehaltBruttobrutto) {
		this.nGehaltBruttobrutto = nGehaltBruttobrutto;
	}

	public BigDecimal getNPraemieBruttobrutto() {
		return nPraemieBruttobrutto;
	}

	public void setNPraemieBruttobrutto(BigDecimal nPraemieBruttobrutto) {
		this.nPraemieBruttobrutto = nPraemieBruttobrutto;
	}

	public Double getFKalkIstJahresstunden() {
		return fKalkIstJahresstunden;
	}

	public void setFKalkIstJahresstunden(Double fKalkIstJahresstunden) {
		this.fKalkIstJahresstunden = fKalkIstJahresstunden;
	}

	public BigDecimal getNLohnmittelstundensatz() {
		return nLohnmittelstundensatz;
	}

	public void setNLohnmittelstundensatz(BigDecimal nLohnmittelstundensatz) {
		this.nLohnmittelstundensatz = nLohnmittelstundensatz;
	}

	public BigDecimal getNAufschlagLohnmittelstundensatz() {
		return nAufschlagLohnmittelstundensatz;
	}

	public void setNAufschlagLohnmittelstundensatz(BigDecimal nAufschlagLohnmittelstundensatz) {
		this.nAufschlagLohnmittelstundensatz = nAufschlagLohnmittelstundensatz;
	}

	public Double getFFaktorLohnmittelstundensatz() {
		return fFaktorLohnmittelstundensatz;
	}

	public void setFFaktorLohnmittelstundensatz(Double fFaktorLohnmittelstundensatz) {
		this.fFaktorLohnmittelstundensatz = fFaktorLohnmittelstundensatz;
	}

	private BigDecimal nGehaltNetto;
	private BigDecimal nGehaltBruttobrutto;
	private BigDecimal nPraemieBruttobrutto;
	private Double fKalkIstJahresstunden;
	private BigDecimal nLohnmittelstundensatz;
	private BigDecimal nAufschlagLohnmittelstundensatz;
	private Double fFaktorLohnmittelstundensatz;
	private Short bStundensatzFixiert;

	public Short getBStundensatzFixiert() {
		return bStundensatzFixiert;
	}

	public void setBStundensatzFixiert(Short bStundensatzFixiert) {
		this.bStundensatzFixiert = bStundensatzFixiert;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof PersonalgehaltDto))
			return false;
		PersonalgehaltDto that = (PersonalgehaltDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.personalIId == null ? this.personalIId == null : that.personalIId.equals(this.personalIId)))
			return false;
		if (!(that.iJahr == null ? this.iJahr == null : that.iJahr.equals(this.iJahr)))
			return false;
		if (!(that.iMonat == null ? this.iMonat == null : that.iMonat.equals(this.iMonat)))
			return false;
		if (!(that.nGehalt == null ? this.nGehalt == null : that.nGehalt.equals(this.nGehalt)))
			return false;
		if (!(that.fUestpauschale == null ? this.fUestpauschale == null
				: that.fUestpauschale.equals(this.fUestpauschale)))
			return false;
		if (!(that.nStundensatz == null ? this.nStundensatz == null : that.nStundensatz.equals(this.nStundensatz)))
			return false;
		if (!(that.fVerfuegbarkeit == null ? this.fVerfuegbarkeit == null
				: that.fVerfuegbarkeit.equals(this.fVerfuegbarkeit)))
			return false;
		if (!(that.nKmgeld1 == null ? this.nKmgeld1 == null : that.nKmgeld1.equals(this.nKmgeld1)))
			return false;
		if (!(that.fBiskilometer == null ? this.fBiskilometer == null : that.fBiskilometer.equals(this.fBiskilometer)))
			return false;
		if (!(that.nKmgeld2 == null ? this.nKmgeld2 == null : that.nKmgeld2.equals(this.nKmgeld2)))
			return false;
		if (!(that.bKksgebbefreit == null ? this.bKksgebbefreit == null
				: that.bKksgebbefreit.equals(this.bKksgebbefreit)))
			return false;
		if (!(that.cGrundkksgebbefreit == null ? this.cGrundkksgebbefreit == null
				: that.cGrundkksgebbefreit.equals(this.cGrundkksgebbefreit)))
			return false;
		if (!(that.bAlleinverdiener == null ? this.bAlleinverdiener == null
				: that.bAlleinverdiener.equals(this.bAlleinverdiener)))
			return false;
		if (!(that.bAlleinerzieher == null ? this.bAlleinerzieher == null
				: that.bAlleinerzieher.equals(this.bAlleinerzieher)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern.equals(this.tAendern)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.personalIId.hashCode();
		result = 37 * result + this.iJahr.hashCode();
		result = 37 * result + this.iMonat.hashCode();
		result = 37 * result + this.nGehalt.hashCode();
		result = 37 * result + this.fUestpauschale.hashCode();
		result = 37 * result + this.nStundensatz.hashCode();
		result = 37 * result + this.fVerfuegbarkeit.hashCode();
		result = 37 * result + this.nKmgeld1.hashCode();
		result = 37 * result + this.fBiskilometer.hashCode();
		result = 37 * result + this.nKmgeld2.hashCode();
		result = 37 * result + this.bKksgebbefreit.hashCode();
		result = 37 * result + this.cGrundkksgebbefreit.hashCode();
		result = 37 * result + this.bAlleinverdiener.hashCode();
		result = 37 * result + this.bAlleinerzieher.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + personalIId;
		returnString += ", " + iJahr;
		returnString += ", " + iMonat;
		returnString += ", " + nGehalt;
		returnString += ", " + fUestpauschale;
		returnString += ", " + nStundensatz;
		returnString += ", " + fVerfuegbarkeit;
		returnString += ", " + nKmgeld1;
		returnString += ", " + fBiskilometer;
		returnString += ", " + nKmgeld2;
		returnString += ", " + bKksgebbefreit;
		returnString += ", " + cGrundkksgebbefreit;
		returnString += ", " + bAlleinverdiener;
		returnString += ", " + bAlleinerzieher;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + tAendern;
		return returnString;
	}
}

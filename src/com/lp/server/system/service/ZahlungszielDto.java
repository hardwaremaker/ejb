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

public class ZahlungszielDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Short bVersteckt;

	private Integer iId;
	private String mandantCNr;
	private String cBez;
	private Integer anzahlZieltageFuerNetto;
	private BigDecimal skontoProzentsatz1;
	private Integer skontoAnzahlTage1;
	private BigDecimal skontoProzentsatz2;
	private Integer skontoAnzahlTage2;
	// stcrud: 7 - sprDto darf hier nicht mit new... initialisiert werden!
	private ZahlungszielsprDto zahlungszielsprDto;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	private Short bInzahlungsvorschlagberuecksichtigen;

	public Short getBInzahlungsvorschlagberuecksichtigen() {
		return bInzahlungsvorschlagberuecksichtigen;
	}

	public void setBInzahlungsvorschlagberuecksichtigen(
			Short bInzahlungsvorschlagberuecksichtigen) {
		this.bInzahlungsvorschlagberuecksichtigen = bInzahlungsvorschlagberuecksichtigen;
	}

	public String getBezeichnung() {
		if(zahlungszielsprDto!=null && zahlungszielsprDto.getCBezeichnung()!=null) {
			return zahlungszielsprDto.getCBezeichnung();
		}else {
			return cBez;
		}
	}
	
	public String getCBez() {
		return cBez;
	}

	public void setCBez(String zahlungszielCNr) {
		this.cBez = zahlungszielCNr;
	}

	public Integer getAnzahlZieltageFuerNetto() {
		return anzahlZieltageFuerNetto;
	}

	public void setAnzahlZieltageFuerNetto(Integer anzahlZieltageFuerNetto) {
		this.anzahlZieltageFuerNetto = anzahlZieltageFuerNetto;
	}

	public BigDecimal getSkontoProzentsatz1() {
		return skontoProzentsatz1;
	}

	public void setSkontoProzentsatz1(BigDecimal skontoProzentsatz1) {
		this.skontoProzentsatz1 = skontoProzentsatz1;
	}

	public Integer getSkontoAnzahlTage1() {
		return skontoAnzahlTage1;
	}

	public void setSkontoAnzahlTage1(Integer skontoAnzahlTage1) {
		this.skontoAnzahlTage1 = skontoAnzahlTage1;
	}

	public BigDecimal getSkontoProzentsatz2() {
		return skontoProzentsatz2;
	}

	public void setSkontoProzentsatz2(BigDecimal skontoProzentsatz2) {
		this.skontoProzentsatz2 = skontoProzentsatz2;
	}

	public Integer getSkontoAnzahlTage2() {
		return skontoAnzahlTage2;
	}

	public ZahlungszielsprDto getZahlungszielsprDto() {
		return zahlungszielsprDto;
	}

	public Short getBVersteckt() {
		return bVersteckt;
	}

	public void setSkontoAnzahlTage2(Integer skontoAnzahlTage2) {
		this.skontoAnzahlTage2 = skontoAnzahlTage2;
	}

	public void setZahlungszielsprDto(ZahlungszielsprDto zahlungszielsprDto) {
		this.zahlungszielsprDto = zahlungszielsprDto;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	private Short bLastschrift;

	public Short getBLastschrift() {
		return bLastschrift;
	}

	public void setBLastschrift(Short bLastschrift) {
		this.bLastschrift = bLastschrift;
	}

	private Short bStichtag;

	private Short bStichtagMonatsletzter;
	private Integer iStichtag;
	private Integer iFolgemonat;

	public Short getBStichtag() {
		return bStichtag;
	}

	public void setBStichtag(Short bStichtag) {
		this.bStichtag = bStichtag;
	}

	public Short getBStichtagMonatsletzter() {
		return bStichtagMonatsletzter;
	}

	public void setBStichtagMonatsletzter(Short bStichtagMonatsletzter) {
		this.bStichtagMonatsletzter = bStichtagMonatsletzter;
	}

	private BigDecimal nAnzahlungProzent;
	
	
	public BigDecimal getNAnzahlungProzent() {
		return nAnzahlungProzent;
	}

	public void setNAnzahlungProzent(BigDecimal nAnzahlungProzent) {
		this.nAnzahlungProzent = nAnzahlungProzent;
	}

	
	public Integer getIStichtag() {
		return iStichtag;
	}

	public void setIStichtag(Integer iStichtag) {
		this.iStichtag = iStichtag;
	}

	public Integer getIFolgemonat() {
		return iFolgemonat;
	}

	public void setIFolgemonat(Integer iFolgemonat) {
		this.iFolgemonat = iFolgemonat;
	}

	private Integer iFolgemonatSkontotage1;
	
	public Integer getIFolgemonatSkontotage1() {
		return iFolgemonatSkontotage1;
	}

	public void setIFolgemonatSkontotage1(Integer iFolgemonatSkontotage1) {
		this.iFolgemonatSkontotage1 = iFolgemonatSkontotage1;
	}

	public Integer getIFolgemonatSkontotage2() {
		return iFolgemonatSkontotage2;
	}

	public void setIFolgemonatSkontotage2(Integer iFolgemonatSkontotage2) {
		this.iFolgemonatSkontotage2 = iFolgemonatSkontotage2;
	}

	private Integer iFolgemonatSkontotage2;
	
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ZahlungszielDto)) {
			return false;
		}
		ZahlungszielDto that = (ZahlungszielDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.mandantCNr == null ? this.mandantCNr == null
				: that.mandantCNr.equals(this.mandantCNr))) {
			return false;
		}
		if (!(that.cBez == null ? this.cBez == null : that.cBez
				.equals(this.cBez))) {
			return false;
		}
		if (!(that.anzahlZieltageFuerNetto == null ? this.anzahlZieltageFuerNetto == null
				: that.anzahlZieltageFuerNetto
						.equals(this.anzahlZieltageFuerNetto))) {
			return false;
		}
		if (!(that.skontoProzentsatz1 == null ? this.skontoProzentsatz1 == null
				: that.skontoProzentsatz1.equals(this.skontoProzentsatz1))) {
			return false;
		}
		if (!(that.skontoAnzahlTage1 == null ? this.skontoAnzahlTage1 == null
				: that.skontoAnzahlTage1.equals(this.skontoAnzahlTage1))) {
			return false;
		}
		if (!(that.skontoProzentsatz2 == null ? this.skontoProzentsatz2 == null
				: that.skontoProzentsatz2.equals(this.skontoProzentsatz2))) {
			return false;
		}
		if (!(that.skontoAnzahlTage2 == null ? this.skontoAnzahlTage2 == null
				: that.skontoAnzahlTage2.equals(this.skontoAnzahlTage2))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.cBez.hashCode();
		result = 37 * result + this.anzahlZieltageFuerNetto.hashCode();
		result = 37 * result + this.skontoProzentsatz1.hashCode();
		result = 37 * result + this.skontoAnzahlTage1.hashCode();
		result = 37 * result + this.skontoProzentsatz2.hashCode();
		result = 37 * result + this.skontoAnzahlTage2.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + mandantCNr;
		returnString += ", " + cBez;
		returnString += ", " + anzahlZieltageFuerNetto;
		returnString += ", " + skontoProzentsatz1;
		returnString += ", " + skontoAnzahlTage1;
		returnString += ", " + skontoProzentsatz2;
		returnString += ", " + skontoAnzahlTage2;
		return returnString;
	}
}

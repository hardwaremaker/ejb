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
import java.sql.Timestamp;

import com.lp.server.partner.service.PartnerDto;
import com.lp.server.util.IModificationData;

public class FinanzamtDto implements Serializable, IModificationData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer partnerIId;
	private String mandantCNr;
	private String cSteuernummer;
	private String cReferat;
	private Timestamp tAnlegen;
	private Integer personalIIdAnlegen;
	private Timestamp tAendern;
	private Integer personalIIdAendern;

	private Integer kontoIIdEbsachkonten;
	private Integer kontoIIdEbdebitoren;
	private Integer kontoIIdEbkreditoren;

	private Integer iFormularnummer;
	private Short bUmsatzRunden;

	private Integer kontoIIdAnzahlungErhaltVerr;
	private Integer kontoIIdAnzahlungErhaltBezahlt;
	private Integer kontoIIdAnzahlungGegebenVerr;
	private Integer kontoIIdAnzahlungGegebenBezahlt;

	// Reverse Charge Konten
	private Integer kontoIIdRCAnzahlungErhaltVerr;
	private Integer kontoIIdRCAnzahlungErhaltBezahlt;
	private Integer kontoIIdRCAnzahlungGegebenVerr;
	private Integer kontoIIdRCAnzahlungGegebenBezahlt;

	public Integer getKontoIIdRCAnzahlungErhaltVerr() {
		return kontoIIdRCAnzahlungErhaltVerr;
	}

	public void setKontoIIdRCAnzahlungErhaltVerr(
			Integer kontoIIdRCAnzahlungErhaltVerr) {
		this.kontoIIdRCAnzahlungErhaltVerr = kontoIIdRCAnzahlungErhaltVerr;
	}

	public Integer getKontoIIdRCAnzahlungErhaltBezahlt() {
		return kontoIIdRCAnzahlungErhaltBezahlt;
	}

	public void setKontoIIdRCAnzahlungErhaltBezahlt(
			Integer kontoIIdRCAnzahlungErhaltBezahlt) {
		this.kontoIIdRCAnzahlungErhaltBezahlt = kontoIIdRCAnzahlungErhaltBezahlt;
	}

	public Integer getKontoIIdRCAnzahlungGegebenVerr() {
		return kontoIIdRCAnzahlungGegebenVerr;
	}

	public void setKontoIIdRCAnzahlungGegebenVerr(
			Integer kontoIIdRCAnzahlungGegebenVerr) {
		this.kontoIIdRCAnzahlungGegebenVerr = kontoIIdRCAnzahlungGegebenVerr;
	}

	public Integer getKontoIIdRCAnzahlungGegebenBezahlt() {
		return kontoIIdRCAnzahlungGegebenBezahlt;
	}

	public void setKontoIIdRCAnzahlungGegebenBezahlt(
			Integer kontoIIdRCAnzahlungGegebenBezahlt) {
		this.kontoIIdRCAnzahlungGegebenBezahlt = kontoIIdRCAnzahlungGegebenBezahlt;
	}

	public Integer getIFormularnummer() {
		return iFormularnummer;
	}

	public void setIFormularnummer(Integer iFormularnummer) {
		this.iFormularnummer = iFormularnummer;
	}

	private PartnerDto partnerDto;

	public Integer getPartnerIId() {
		return partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getCSteuernummer() {
		return cSteuernummer;
	}

	public void setCSteuernummer(String cSteuernummer) {
		this.cSteuernummer = cSteuernummer;
	}

	public String getCReferat() {
		return cReferat;
	}

	public void setCReferat(String cReferat) {
		this.cReferat = cReferat;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
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

	public PartnerDto getPartnerDto() {
		return partnerDto;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public void setPartnerDto(PartnerDto partnerDto) {
		this.partnerDto = partnerDto;
	}

	public Integer getKontoIIdEbsachkonten() {
		return kontoIIdEbsachkonten;
	}

	public void setKontoIIdEbsachkonten(Integer kontoIIdEbsachkonten) {
		this.kontoIIdEbsachkonten = kontoIIdEbsachkonten;
	}

	public Integer getKontoIIdEbdebitoren() {
		return kontoIIdEbdebitoren;
	}

	public void setKontoIIdEbdebitoren(Integer kontoIIdEbdebitoren) {
		this.kontoIIdEbdebitoren = kontoIIdEbdebitoren;
	}

	public Integer getKontoIIdEbkreditoren() {
		return kontoIIdEbkreditoren;
	}

	public void setKontoIIdEbkreditoren(Integer kontoIIdEbkreditoren) {
		this.kontoIIdEbkreditoren = kontoIIdEbkreditoren;
	}

	public void setBUmsatzRunden(Short bUmsatzRunden) {
		this.bUmsatzRunden = bUmsatzRunden;
	}

	public Short getBUmsatzRunden() {
		return bUmsatzRunden;
	}

	public void setKontoIIdAnzahlungErhaltVerr(
			Integer kontoIIdAnzahlungErhaltVerr) {
		this.kontoIIdAnzahlungErhaltVerr = kontoIIdAnzahlungErhaltVerr;
	}

	public Integer getKontoIIdAnzahlungErhaltVerr() {
		return kontoIIdAnzahlungErhaltVerr;
	}

	public void setKontoIIdAnzahlungErhaltBezahlt(
			Integer kontoIIdAnzahlungErhaltBezahlt) {
		this.kontoIIdAnzahlungErhaltBezahlt = kontoIIdAnzahlungErhaltBezahlt;
	}

	public Integer getKontoIIdAnzahlungErhaltBezahlt() {
		return kontoIIdAnzahlungErhaltBezahlt;
	}

	public void setKontoIIdAnzahlungGegebenVerr(
			Integer kontoIIdAnzahlungGegebenVerr) {
		this.kontoIIdAnzahlungGegebenVerr = kontoIIdAnzahlungGegebenVerr;
	}

	public Integer getKontoIIdAnzahlungGegebenVerr() {
		return kontoIIdAnzahlungGegebenVerr;
	}

	public void setKontoIIdAnzahlungGegebenBezahlt(
			Integer kontoIIdAnzahlungGegebenBezahlt) {
		this.kontoIIdAnzahlungGegebenBezahlt = kontoIIdAnzahlungGegebenBezahlt;
	}

	public Integer getKontoIIdAnzahlungGegebenBezahlt() {
		return kontoIIdAnzahlungGegebenBezahlt;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof FinanzamtDto)) {
			return false;
		}
		FinanzamtDto that = (FinanzamtDto) obj;
		if (!(that.partnerIId == null ? this.partnerIId == null
				: that.partnerIId.equals(this.partnerIId))) {
			return false;
		}
		if (!(that.mandantCNr == null ? this.mandantCNr == null
				: that.mandantCNr.equals(this.mandantCNr))) {
			return false;
		}
		if (!(that.cSteuernummer == null ? this.cSteuernummer == null
				: that.cSteuernummer.equals(this.cSteuernummer))) {
			return false;
		}
		if (!(that.cReferat == null ? this.cReferat == null : that.cReferat
				.equals(this.cReferat))) {
			return false;
		}
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen
				.equals(this.tAnlegen))) {
			return false;
		}
		if (!(that.personalIIdAnlegen == null ? this.personalIIdAnlegen == null
				: that.personalIIdAnlegen.equals(this.personalIIdAnlegen))) {
			return false;
		}
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern))) {
			return false;
		}
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern))) {
			return false;
		}
		if (!(that.iFormularnummer == null ? this.iFormularnummer == null
				: that.iFormularnummer.equals(this.iFormularnummer))) {
			return false;
		}
		if (!(that.bUmsatzRunden == null ? this.bUmsatzRunden == null
				: that.bUmsatzRunden.equals(this.bUmsatzRunden))) {
			return false;
		}
		if (!(that.kontoIIdEbdebitoren == null ? this.kontoIIdEbdebitoren == null
				: that.kontoIIdEbdebitoren.equals(this.kontoIIdEbdebitoren))) {
			return false;
		}
		if (!(that.kontoIIdEbkreditoren == null ? this.kontoIIdEbkreditoren == null
				: that.kontoIIdEbkreditoren.equals(this.kontoIIdEbkreditoren))) {
			return false;
		}
		if (!(that.kontoIIdEbsachkonten == null ? this.kontoIIdEbsachkonten == null
				: that.kontoIIdEbsachkonten.equals(this.kontoIIdEbsachkonten))) {
			return false;
		}
		if (!(that.kontoIIdAnzahlungErhaltBezahlt == null ? this.kontoIIdAnzahlungErhaltBezahlt == null
				: that.kontoIIdAnzahlungErhaltBezahlt
						.equals(this.kontoIIdAnzahlungErhaltBezahlt))) {
			return false;
		}
		if (!(that.kontoIIdAnzahlungErhaltVerr == null ? this.kontoIIdAnzahlungErhaltVerr == null
				: that.kontoIIdAnzahlungErhaltVerr
						.equals(this.kontoIIdAnzahlungErhaltVerr))) {
			return false;
		}
		if (!(that.kontoIIdAnzahlungGegebenBezahlt == null ? this.kontoIIdAnzahlungGegebenBezahlt == null
				: that.kontoIIdAnzahlungGegebenBezahlt
						.equals(this.kontoIIdAnzahlungGegebenBezahlt))) {
			return false;
		}
		if (!(that.kontoIIdAnzahlungGegebenVerr == null ? this.kontoIIdAnzahlungGegebenVerr == null
				: that.kontoIIdAnzahlungGegebenVerr
						.equals(this.kontoIIdAnzahlungGegebenVerr))) {
			return false;
		}

		if (!(that.kontoIIdRCAnzahlungErhaltBezahlt == null ? this.kontoIIdRCAnzahlungErhaltBezahlt == null
				: that.kontoIIdRCAnzahlungErhaltBezahlt
						.equals(this.kontoIIdRCAnzahlungErhaltBezahlt))) {
			return false;
		}
		if (!(that.kontoIIdRCAnzahlungErhaltVerr == null ? this.kontoIIdRCAnzahlungErhaltVerr == null
				: that.kontoIIdRCAnzahlungErhaltVerr
						.equals(this.kontoIIdRCAnzahlungErhaltVerr))) {
			return false;
		}
		if (!(that.kontoIIdRCAnzahlungGegebenBezahlt == null ? this.kontoIIdRCAnzahlungGegebenBezahlt == null
				: that.kontoIIdRCAnzahlungGegebenBezahlt
						.equals(this.kontoIIdRCAnzahlungGegebenBezahlt))) {
			return false;
		}
		if (!(that.kontoIIdRCAnzahlungGegebenVerr == null ? this.kontoIIdRCAnzahlungGegebenVerr == null
				: that.kontoIIdRCAnzahlungGegebenVerr
						.equals(this.kontoIIdRCAnzahlungGegebenVerr))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.partnerIId.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.cSteuernummer.hashCode();
		result = 37 * result + this.cReferat.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.iFormularnummer.hashCode();
		result = 37 * result + this.bUmsatzRunden.hashCode();
		result = 37 * result + this.kontoIIdEbdebitoren.hashCode();
		result = 37 * result + this.kontoIIdEbkreditoren.hashCode();
		result = 37 * result + this.kontoIIdEbsachkonten.hashCode();

		result = 37 * result + this.kontoIIdAnzahlungErhaltBezahlt.hashCode();
		result = 37 * result + this.kontoIIdAnzahlungErhaltVerr.hashCode();
		result = 37 * result + this.kontoIIdAnzahlungGegebenBezahlt.hashCode();
		result = 37 * result + this.kontoIIdAnzahlungGegebenVerr.hashCode();

		result = 37 * result + this.kontoIIdRCAnzahlungErhaltBezahlt.hashCode();
		result = 37 * result + this.kontoIIdRCAnzahlungErhaltVerr.hashCode();
		result = 37 * result + this.kontoIIdRCAnzahlungGegebenBezahlt.hashCode();
		result = 37 * result + this.kontoIIdRCAnzahlungGegebenVerr.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += partnerIId;
		returnString += ", " + mandantCNr;
		returnString += ", " + cSteuernummer;
		returnString += ", " + cReferat;
		returnString += ", " + tAnlegen;
		returnString += ", " + personalIIdAnlegen;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + iFormularnummer;
		returnString += ", " + bUmsatzRunden;
		returnString += ", " + kontoIIdEbdebitoren;
		returnString += ", " + kontoIIdEbkreditoren;
		returnString += ", " + kontoIIdEbsachkonten;
		returnString += ", " + kontoIIdAnzahlungErhaltBezahlt;
		returnString += ", " + kontoIIdAnzahlungErhaltVerr;
		returnString += ", " + kontoIIdAnzahlungGegebenBezahlt;
		returnString += ", " + kontoIIdAnzahlungGegebenVerr;
		returnString += ", " + kontoIIdRCAnzahlungErhaltBezahlt;
		returnString += ", " + kontoIIdRCAnzahlungErhaltVerr;
		returnString += ", " + kontoIIdRCAnzahlungGegebenBezahlt;
		returnString += ", " + kontoIIdRCAnzahlungGegebenVerr;
		return returnString;
	}
}

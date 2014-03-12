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
package com.lp.server.system.service;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Arrays;

import com.lp.server.partner.service.PartnerDto;

public class MandantDto extends CryptDto implements Serializable, ICryptDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cNr;
	private String cKbez;
	private Integer partnerIId;
	private String waehrungCNr;
	private Integer spediteurIIdLF;
	private Integer lieferartIIdLF;
	private Integer zahlungszielIIdLF;
	private Integer spediteurIIdKunde;
	private Integer lieferartIIdKunde;
	private Integer zahlungszielIIdKunde;
	private Integer vkpfArtikelpreislisteIId;
	private Timestamp tAnlegen;
	private Integer iAnlegen;
	private Timestamp tAendern;
	private Integer iAendern;
	private Integer iIdKostenstelle;
	private Integer mwstsatzbezIIdStandardinlandmwstsatz;
	private Integer mwstsatzbezIIdStandardauslandmwstsatz;
	private Integer iBankverbindung;
	private Integer partnerIIdLieferadresse;
	private Integer iBenutzermax;
	private Integer mwstsatzIIdStandarddrittlandmwstsatz;
	private Integer kundeIIdStueckliste;
	private Integer partnerIIdFinanzamt;
	private Integer jahreRueckdatierbar;
	
	public void setJahreRueckdatierbar(Integer jahreRueckdatierbar) {
		this.jahreRueckdatierbar = jahreRueckdatierbar;
	}
	
	public Integer getJahreRueckdatierbar() {
		return jahreRueckdatierbar;
	}
	
	public Integer getKundeIIdStueckliste() {
		return kundeIIdStueckliste;
	}

	public void setKundeIIdStueckliste(Integer kundeIIdStueckliste) {
		this.kundeIIdStueckliste = kundeIIdStueckliste;
	}

	public Integer getMwstsatzbezIIdStandarddrittlandmwstsatz() {
		return mwstsatzIIdStandarddrittlandmwstsatz;
	}

	public void setMwstsatzbezIIdStandarddrittlandmwstsatz(
			Integer mwstsatzIIdStandarddrittlandmwstsatz) {
		this.mwstsatzIIdStandarddrittlandmwstsatz = mwstsatzIIdStandarddrittlandmwstsatz;
	}
	transient private byte[] oCode;
	transient private byte[] oHash;
	
	public Integer getPartnerIIdLieferadresse() {
		return partnerIIdLieferadresse;
	}

	public void setPartnerIIdLieferadresse(Integer partnerIIdLieferadresse) {
		this.partnerIIdLieferadresse = partnerIIdLieferadresse;
	}
	private Short bDemo;
	public void setBDemo(Short demo) {
		bDemo = demo;
	}
	// Manuell hinzugefuegt!
	private PartnerDto partnerDto = new PartnerDto();
	private AnwenderDto anwenderDto = new AnwenderDto();

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public Short getBDemo() {
		return bDemo;
	}


	public Integer getIBankverbindung() {
		return iBankverbindung;
	}

	public void setIBankverbindung(Integer iBankverbindung) {
		this.iBankverbindung = iBankverbindung;
	}

	public Integer getMwstsatzbezIIdStandardinlandmwstsatz() {
		return mwstsatzbezIIdStandardinlandmwstsatz;
	}

	public void setMwstsatzbezIIdStandardinlandmwstsatz(
			Integer mwstsatzbezIIdStandardinlandmwstsatz) {
		this.mwstsatzbezIIdStandardinlandmwstsatz = mwstsatzbezIIdStandardinlandmwstsatz;
	}
	
	public Integer getMwstsatzbezIIdStandardauslandmwstsatz() {
		return mwstsatzbezIIdStandardauslandmwstsatz;
	}

	public void setMwstsatzbezIIdStandardauslandmwstsatz(
			Integer mwstsatzbezIIdStandardauslandmwstsatz) {
		this.mwstsatzbezIIdStandardauslandmwstsatz = mwstsatzbezIIdStandardauslandmwstsatz;
	}

	public String getCKbez() {
		return cKbez;
	}

	public void setCKbez(String cKbez) {
		this.cKbez = cKbez;
	}

	public Integer getPartnerIId() {
		return partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	public String getWaehrungCNr() {
		return waehrungCNr;
	}

	public void setWaehrungCNr(String waehrungCNrCurrency) {
		this.waehrungCNr = waehrungCNrCurrency;
	}

	public Integer getSpediteurIIdKunde() {
		return spediteurIIdKunde;
	}

	public void setSpediteurIIdKunde(Integer spediteurIIdKunde) {
		this.spediteurIIdKunde = spediteurIIdKunde;
	}

	public Integer getLieferartIIdKunde() {
		return lieferartIIdKunde;
	}

	public void setLieferartIIdKunde(Integer lieferartIIdKunde) {
		this.lieferartIIdKunde = lieferartIIdKunde;
	}

	public Integer getZahlungszielIIdKunde() {
		return zahlungszielIIdKunde;
	}

	public void setZahlungszielIIdKunde(Integer zahlungszielIIdKunde) {
		this.zahlungszielIIdKunde = zahlungszielIIdKunde;
	}

	public Integer getVkpfArtikelpreislisteIId() {
		return vkpfArtikelpreislisteIId;
	}

	public void setVkpfArtikelpreislisteIId(Integer vkpfArtikelpreislisteIId) {
		this.vkpfArtikelpreislisteIId = vkpfArtikelpreislisteIId;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Integer getIAnlegen() {
		return iAnlegen;
	}

	public void setIAnlegen(Integer iAnlegen) {
		this.iAnlegen = iAnlegen;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getIAendern() {
		return iAendern;
	}

	public PartnerDto getPartnerDto() {
		return partnerDto;
	}

	public AnwenderDto getAnwenderDto() {
		return anwenderDto;
	}

	public Integer getIIdKostenstelle() {
		return iIdKostenstelle;
	}

	public Integer getSpediteurIIdLF() {
		return spediteurIIdLF;
	}

	public Integer getZahlungszielIIdLF() {
		return zahlungszielIIdLF;
	}

	public Integer getLieferartIIdLF() {
		return lieferartIIdLF;
	}

	public void setIAendern(Integer iAendern) {
		this.iAendern = iAendern;
	}

	public void setPartnerDto(PartnerDto partnerDto) {
		this.partnerDto = partnerDto;
	}

	public void setAnwenderDto(AnwenderDto anwenderDto) {
		this.anwenderDto = anwenderDto;
	}

	public void setIIdKostenstelle(Integer iIdKostenstelle) {
		this.iIdKostenstelle = iIdKostenstelle;
	}

	public void setSpediteurIIdLF(Integer spediteurIIdLF) {
		this.spediteurIIdLF = spediteurIIdLF;
	}

	public void setZahlungszielIIdLF(Integer zahlungszielIIdLF) {
		this.zahlungszielIIdLF = zahlungszielIIdLF;
	}

	public void setLieferartIIdLF(Integer lieferartIIdLF) {
		this.lieferartIIdLF = lieferartIIdLF;
	}

	public void setIBenutzermax(Integer iBenutzermax) {
		this.iBenutzermax = iBenutzermax;
	}

	public Integer getIBenutzermax() {
		return iBenutzermax;
	}

	public void setOCode(byte[] oCode) {
		this.oCode = oCode;
	}

	public void setOHash(byte[] oHash) {
		this.oHash = oHash;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof MandantDto)) {
			return false;
		}
		MandantDto that = (MandantDto) obj;
		if (!(that.cNr == null ? this.cNr == null : that.cNr.equals(this.cNr))) {
			return false;
		}
		if (!(that.cKbez == null ? this.cKbez == null : that.cKbez
				.equals(this.cKbez))) {
			return false;
		}
		if (!(that.partnerIId == null ? this.partnerIId == null
				: that.partnerIId.equals(this.partnerIId))) {
			return false;
		}
		if (!(that.waehrungCNr == null ? this.waehrungCNr == null
				: that.waehrungCNr.equals(this.waehrungCNr))) {
			return false;
		}
		if (!(that.spediteurIIdKunde == null ? this.spediteurIIdKunde == null
				: that.spediteurIIdKunde.equals(this.spediteurIIdKunde))) {
			return false;
		}
		if (!(that.spediteurIIdLF == null ? this.spediteurIIdLF == null
				: that.spediteurIIdLF.equals(this.spediteurIIdLF))) {
			return false;
		}
		if (!(that.lieferartIIdKunde == null ? this.lieferartIIdKunde == null
				: that.lieferartIIdKunde.equals(this.lieferartIIdKunde))) {
			return false;
		}
		if (!(that.lieferartIIdLF == null ? this.lieferartIIdLF == null
				: that.lieferartIIdLF.equals(this.lieferartIIdLF))) {
			return false;
		}
		if (!(that.zahlungszielIIdKunde == null ? this.zahlungszielIIdKunde == null
				: that.zahlungszielIIdKunde.equals(this.zahlungszielIIdKunde))) {
			return false;
		}
		if (!(that.zahlungszielIIdLF == null ? this.zahlungszielIIdLF == null
				: that.zahlungszielIIdLF.equals(this.zahlungszielIIdLF))) {
			return false;
		}
		if (!(that.vkpfArtikelpreislisteIId == null ? this.vkpfArtikelpreislisteIId == null
				: that.vkpfArtikelpreislisteIId
						.equals(this.vkpfArtikelpreislisteIId))) {
			return false;
		}
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen
				.equals(this.tAnlegen))) {
			return false;
		}
		if (!(that.iAnlegen == null ? this.iAnlegen == null : that.iAnlegen
				.equals(this.iAnlegen))) {
			return false;
		}
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern))) {
			return false;
		}
		if (!(that.iAendern == null ? this.iAendern == null : that.iAendern
				.equals(this.iAendern))) {
			return false;
		}
		if (!(that.mwstsatzbezIIdStandardinlandmwstsatz == null ? this.mwstsatzbezIIdStandardinlandmwstsatz == null
				: that.mwstsatzbezIIdStandardinlandmwstsatz
						.equals(this.mwstsatzbezIIdStandardinlandmwstsatz))) {
			return false;
		}
		if (!(that.mwstsatzbezIIdStandardauslandmwstsatz == null ? this.mwstsatzbezIIdStandardauslandmwstsatz == null
				: that.mwstsatzbezIIdStandardauslandmwstsatz
						.equals(this.mwstsatzbezIIdStandardauslandmwstsatz))) {
			return false;
		}
		if (!(that.iBankverbindung == null ? this.iBankverbindung == null
				: that.iBankverbindung.equals(this.iBankverbindung))) {
			return false;
		}
		if (!(that.partnerIIdFinanzamt == null ? this.partnerIIdFinanzamt == null
				: that.partnerIIdFinanzamt.equals(this.partnerIIdFinanzamt))) {
			return false;
		}
		if (!(that.jahreRueckdatierbar == null ? this.jahreRueckdatierbar == null
				: that.jahreRueckdatierbar.equals(this.jahreRueckdatierbar))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.cNr.hashCode();
		result = 37 * result + this.cKbez.hashCode();
		result = 37 * result + this.partnerIId.hashCode();
		result = 37 * result + this.waehrungCNr.hashCode();
		result = 37 * result + this.spediteurIIdKunde.hashCode();
		result = 37 * result + this.lieferartIIdKunde.hashCode();
		result = 37 * result + this.zahlungszielIIdKunde.hashCode();
		result = 37 * result + this.spediteurIIdLF.hashCode();
		result = 37 * result + this.lieferartIIdLF.hashCode();
		result = 37 * result + this.zahlungszielIIdLF.hashCode();
		result = 37 * result + this.vkpfArtikelpreislisteIId.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.iAnlegen.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.iAendern.hashCode();
		result = 37 * result + this.mwstsatzbezIIdStandardinlandmwstsatz.hashCode();
		result = 37 * result + this.mwstsatzbezIIdStandardauslandmwstsatz.hashCode();
		result = 37 * result + this.iBankverbindung.hashCode();
		result = 37 * result + this.partnerIIdFinanzamt.hashCode();
		result = 37 * result + this.jahreRueckdatierbar.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += cNr;
		returnString += ", " + cKbez;
		returnString += ", " + partnerIId;
		returnString += ", " + waehrungCNr;
		returnString += ", " + spediteurIIdKunde;
		returnString += ", " + lieferartIIdKunde;
		returnString += ", " + zahlungszielIIdKunde;
		returnString += ", " + vkpfArtikelpreislisteIId;
		returnString += ", " + tAnlegen;
		returnString += ", " + iAnlegen;
		returnString += ", " + tAendern;
		returnString += ", " + iAendern;
		returnString += ", " + mwstsatzbezIIdStandardinlandmwstsatz;
		returnString += ", " + mwstsatzbezIIdStandardauslandmwstsatz;
		returnString += ", " + iBankverbindung;
		returnString += ", " + partnerIIdFinanzamt;
		return returnString;
	}

	public String toValidateString() {
		String returnString = "";
		returnString += cNr;
		returnString += partnerIId;
		returnString += waehrungCNr;
		returnString += tAnlegen;
		returnString += iAnlegen;
		returnString += iBenutzermax;
		returnString += bDemo;
		returnString += Arrays.hashCode(oCode);
		return returnString;
	}
	
	@Override
	public boolean validate() {
		if (this.oHash == null) return false;
		byte[] baDecode = super.decodeRSA(this.oHash, this.oCode);
		if (baDecode == null) return false;
		String sDecode;
		try {
			sDecode = new String(baDecode, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			sDecode = new String(baDecode);
		}
		if (sDecode.compareTo(this.toValidateString())==0) return true;
		return false;
	}

	public void setPartnerIIdFinanzamt(Integer partnerIIdFinanzamt) {
		this.partnerIIdFinanzamt = partnerIIdFinanzamt;
	}

	public Integer getPartnerIIdFinanzamt() {
		return partnerIIdFinanzamt;
	}
}

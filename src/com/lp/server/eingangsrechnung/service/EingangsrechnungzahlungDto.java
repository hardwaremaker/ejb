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
package com.lp.server.eingangsrechnung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class EingangsrechnungzahlungDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer eingangsrechnungIId;
	private Date tZahldatum;
	private String zahlungsartCNr;
	private Integer bankverbindungIId;
	private Integer kassenbuchIId;
	private Integer iAuszug;
	private BigDecimal nKurs;
	private BigDecimal nBetrag;
	private BigDecimal nBetragfw;
	private BigDecimal nBetragust;
	private BigDecimal nBetragustfw;
	private Timestamp tAnlegen;
	private Integer personalIIdAnlegen;
	private Timestamp tAendern;
	private Integer personalIIdAendern;
	private Integer eingangsrechnungIIdGutschriftZahlung;
	private Integer eingangsrechnungIIdGutschrift;
	private Integer rechnungzahlungIId;
	private Short bKursuebersteuert;
	private Integer buchungdetailIId;


	public Integer getRechnungzahlungIId() {
		return rechnungzahlungIId;
	}

	public void setRechnungzahlungIId(Integer rechnungzahlungIId) {
		this.rechnungzahlungIId = rechnungzahlungIId;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getEingangsrechnungIId() {
		return eingangsrechnungIId;
	}

	public void setEingangsrechnungIId(Integer eingangsrechnungIId) {
		this.eingangsrechnungIId = eingangsrechnungIId;
	}

	public Date getTZahldatum() {
		return tZahldatum;
	}

	public void setTZahldatum(Date tZahldatum) {
		this.tZahldatum = tZahldatum;
	}

	public String getZahlungsartCNr() {
		return zahlungsartCNr;
	}

	public void setZahlungsartCNr(String zahlungsartCNr) {
		this.zahlungsartCNr = zahlungsartCNr;
	}

	public Integer getBankverbindungIId() {
		return bankverbindungIId;
	}

	public void setBankverbindungIId(Integer bankverbindungIId) {
		this.bankverbindungIId = bankverbindungIId;
	}

	public Integer getKassenbuchIId() {
		return kassenbuchIId;
	}

	public void setKassenbuchIId(Integer kassenbuchIId) {
		this.kassenbuchIId = kassenbuchIId;
	}

	public Integer getIAuszug() {
		return this.iAuszug;
	}

	public void setIAuszug(Integer iAuszug) {
		this.iAuszug = iAuszug;
	}

	public BigDecimal getNKurs() {
		return nKurs;
	}

	public void setNKurs(BigDecimal nKurs) {
		this.nKurs = nKurs;
	}

	public BigDecimal getNBetrag() {
		return nBetrag;
	}

	public void setNBetrag(BigDecimal nBetrag) {
		this.nBetrag = nBetrag;
	}

	public BigDecimal getNBetragfw() {
		return nBetragfw;
	}

	public void setNBetragfw(BigDecimal nBetragfw) {
		this.nBetragfw = nBetragfw;
	}

	public BigDecimal getNBetragust() {
		return nBetragust;
	}

	public void setNBetragust(BigDecimal nBetragust) {
		this.nBetragust = nBetragust;
	}

	public BigDecimal getNBetragustfw() {
		return nBetragustfw;
	}

	public void setNBetragustfw(BigDecimal nBetragustfw) {
		this.nBetragustfw = nBetragustfw;
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

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getEingangsrechnungIIdGutschriftZahlung() {
		return eingangsrechnungIIdGutschriftZahlung;
	}

	public void setEingangsrechnungIIdGutschriftZahlung(
			Integer eingangsrechnungIIdGutschriftZahlung) {
		this.eingangsrechnungIIdGutschriftZahlung = eingangsrechnungIIdGutschriftZahlung;
	}

	public Integer getEingangsrechnungIIdGutschrift() {
		return eingangsrechnungIIdGutschrift;
	}

	public void setEingangsrechnungIIdGutschrift(
			Integer eingangsrechnungIIdGutschrift) {
		this.eingangsrechnungIIdGutschrift = eingangsrechnungIIdGutschrift;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof EingangsrechnungzahlungDto))
			return false;
		EingangsrechnungzahlungDto that = (EingangsrechnungzahlungDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.eingangsrechnungIId == null ? this.eingangsrechnungIId == null
				: that.eingangsrechnungIId.equals(this.eingangsrechnungIId)))
			return false;
		if (!(that.tZahldatum == null ? this.tZahldatum == null
				: that.tZahldatum.equals(this.tZahldatum)))
			return false;
		if (!(that.zahlungsartCNr == null ? this.zahlungsartCNr == null
				: that.zahlungsartCNr.equals(this.zahlungsartCNr)))
			return false;
		if (!(that.bankverbindungIId == null ? this.bankverbindungIId == null
				: that.bankverbindungIId.equals(this.bankverbindungIId)))
			return false;
		if (!(that.kassenbuchIId == null ? this.kassenbuchIId == null
				: that.kassenbuchIId.equals(this.kassenbuchIId)))
			return false;
		if (!(that.nKurs == null ? this.nKurs == null : that.nKurs
				.equals(this.nKurs)))
			return false;
		if (!(that.nBetrag == null ? this.nBetrag == null : that.nBetrag
				.equals(this.nBetrag)))
			return false;
		if (!(that.nBetragfw == null ? this.nBetragfw == null : that.nBetragfw
				.equals(this.nBetragfw)))
			return false;
		if (!(that.nBetragust == null ? this.nBetragust == null
				: that.nBetragust.equals(this.nBetragust)))
			return false;
		if (!(that.nBetragustfw == null ? this.nBetragustfw == null
				: that.nBetragustfw.equals(this.nBetragustfw)))
			return false;
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen
				.equals(this.tAnlegen)))
			return false;
		if (!(that.personalIIdAnlegen == null ? this.personalIIdAnlegen == null
				: that.personalIIdAnlegen.equals(this.personalIIdAnlegen)))
			return false;
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		if (!(that.eingangsrechnungIIdGutschriftZahlung == null ? this.eingangsrechnungIIdGutschriftZahlung == null
				: that.eingangsrechnungIIdGutschriftZahlung
						.equals(this.eingangsrechnungIIdGutschriftZahlung)))
			return false;
		if (!(that.eingangsrechnungIIdGutschrift == null ? this.eingangsrechnungIIdGutschrift == null
				: that.eingangsrechnungIIdGutschrift
						.equals(this.eingangsrechnungIIdGutschrift)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.eingangsrechnungIId.hashCode();
		result = 37 * result + this.tZahldatum.hashCode();
		result = 37 * result + this.zahlungsartCNr.hashCode();
		result = 37 * result + this.bankverbindungIId.hashCode();
		result = 37 * result + this.kassenbuchIId.hashCode();
		result = 37 * result + this.nKurs.hashCode();
		result = 37 * result + this.nBetrag.hashCode();
		result = 37 * result + this.nBetragfw.hashCode();
		result = 37 * result + this.nBetragust.hashCode();
		result = 37 * result + this.nBetragustfw.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result
				+ this.eingangsrechnungIIdGutschriftZahlung.hashCode();
		result = 37 * result + this.eingangsrechnungIIdGutschrift.hashCode();
		return result;
	}

	public String toString() {
		StringBuffer returnStringBuffer = new StringBuffer(640);
		returnStringBuffer.append("[");
		returnStringBuffer.append("iId:").append(iId);
		returnStringBuffer.append("eingangsrechnungIId:").append(
				eingangsrechnungIId);
		returnStringBuffer.append("tZahldatum:").append(tZahldatum);
		returnStringBuffer.append("zahlungsartCNr:").append(zahlungsartCNr);
		returnStringBuffer.append("bankverbindungIId:").append(
				bankverbindungIId);
		returnStringBuffer.append("kassenbuchIId:").append(kassenbuchIId);
		returnStringBuffer.append("nKurs:").append(nKurs);
		returnStringBuffer.append("nBetrag:").append(nBetrag);
		returnStringBuffer.append("nBetragfw:").append(nBetragfw);
		returnStringBuffer.append("nBetragust:").append(nBetragust);
		returnStringBuffer.append("nBetragustfw:").append(nBetragustfw);
		returnStringBuffer.append("tAnlegen:").append(tAnlegen);
		returnStringBuffer.append("personalIIdAnlegen:").append(
				personalIIdAnlegen);
		returnStringBuffer.append("tAendern:").append(tAendern);
		returnStringBuffer.append("personalIIdAendern:").append(
				personalIIdAendern);
		returnStringBuffer.append("eingangsrechnungIIdGutschriftZahlung:")
				.append(eingangsrechnungIIdGutschriftZahlung);
		returnStringBuffer.append("eingangsrechnungIIdGutschrift:").append(
				eingangsrechnungIIdGutschrift);
		returnStringBuffer.append("]");
		return returnStringBuffer.toString();
	}

	public void setBKursuebersteuert(Short bKursuebersteuert) {
		this.bKursuebersteuert = bKursuebersteuert;
	}

	public Short getBKursuebersteuert() {
		return bKursuebersteuert;
	}

	public void setBuchungdetailIId(Integer buchungdetailIId) {
		this.buchungdetailIId = buchungdetailIId;
	}
	
	public Integer getBuchungdetailIId() {
		return buchungdetailIId;
	}
}

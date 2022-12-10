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
package com.lp.server.rechnung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.lp.server.finanz.service.IBuchungDetailKommentar;
import com.lp.server.util.logger.LogEventPayload;

public class RechnungzahlungDto implements Serializable, IBuchungDetailKommentar, LogEventPayload {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer rechnungIId;
	private Date dZahldatum;
	private String zahlungsartCNr;
	private Integer bankkontoIId;
	private Integer kassenbuchIId;
	private Integer rechnungIIdGutschrift;
	private Integer iAuszug;
	private BigDecimal nKurs;
	private BigDecimal nBetrag;
	private BigDecimal nBetragfw;
	private BigDecimal nBetragUst;
	private BigDecimal nBetragUstfw;
	private Date dWechselFaelligAm;
	private Timestamp tAnlegen;
	private Integer personalIIdAnlegen;
	private Timestamp tAendern;
	private Integer personalIIdAendern;
	private Double fSkonto;
	private Integer rechnungzahlungIIdGutschrift;
	private Integer eingangsrechnungIId;
	private Integer buchungdetailIId;
	private String cKommentar;

	public Integer getBuchungdetailIId() {
		return buchungdetailIId;
	}

	public void setBuchungdetailIId(Integer buchungdetailIId) {
		this.buchungdetailIId = buchungdetailIId;
	}

	public Integer getEingangsrechnungIId() {
		return eingangsrechnungIId;
	}

	public void setEingangsrechnungIId(Integer eingangsrechnungIId) {
		this.eingangsrechnungIId = eingangsrechnungIId;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getRechnungIId() {
		return rechnungIId;
	}

	public void setRechnungIId(Integer rechnungIId) {
		this.rechnungIId = rechnungIId;
	}

	public Date getDZahldatum() {
		return dZahldatum;
	}

	public void setDZahldatum(Date dZahldatum) {
		this.dZahldatum = dZahldatum;
	}

	public String getZahlungsartCNr() {
		return zahlungsartCNr;
	}

	public void setZahlungsartCNr(String zahlungsartCNr) {
		this.zahlungsartCNr = zahlungsartCNr;
	}

	public Integer getBankkontoIId() {
		return bankkontoIId;
	}

	public void setBankkontoIId(Integer bankkontoIId) {
		this.bankkontoIId = bankkontoIId;
	}

	public Integer getKassenbuchIId() {
		return kassenbuchIId;
	}

	public void setKassenbuchIId(Integer kassenbuchIId) {
		this.kassenbuchIId = kassenbuchIId;
	}

	public Integer getRechnungIIdGutschrift() {
		return rechnungIIdGutschrift;
	}

	public void setRechnungIIdGutschrift(Integer rechnungIIdGutschrift) {
		this.rechnungIIdGutschrift = rechnungIIdGutschrift;
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

	public BigDecimal getNBetragUst() {
		return nBetragUst;
	}

	public void setNBetragUst(BigDecimal nBetragUst) {
		this.nBetragUst = nBetragUst;
	}

	public BigDecimal getNBetragUstfw() {
		return nBetragUstfw;
	}

	public void setNBetragUstfw(BigDecimal nBetragUstfw) {
		this.nBetragUstfw = nBetragUstfw;
	}

	public Date getDWechselFaelligAm() {
		return dWechselFaelligAm;
	}

	public void setDWechselFaelligAm(Date dWechselFaelligAm) {
		this.dWechselFaelligAm = dWechselFaelligAm;
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

	public Double getFSkonto() {
		return fSkonto;
	}

	public void setFSkonto(Double d) {
		this.fSkonto = d;
	}

	public Integer getRechnungzahlungIIdGutschrift() {
		return rechnungzahlungIIdGutschrift;
	}

	public void setRechnungzahlungIIdGutschrift(
			Integer rechnungzahlungIIdGutschrift) {
		this.rechnungzahlungIIdGutschrift = rechnungzahlungIIdGutschrift;
	}

	@Override
	public void setCKommentar(String cKommentar) {
		this.cKommentar = cKommentar;
	}

	@Override
	public String getCKommentar() {
		return cKommentar;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof RechnungzahlungDto))
			return false;
		RechnungzahlungDto that = (RechnungzahlungDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.rechnungIId == null ? this.rechnungIId == null
				: that.rechnungIId.equals(this.rechnungIId)))
			return false;
		if (!(that.dZahldatum == null ? this.dZahldatum == null
				: that.dZahldatum.equals(this.dZahldatum)))
			return false;
		if (!(that.zahlungsartCNr == null ? this.zahlungsartCNr == null
				: that.zahlungsartCNr.equals(this.zahlungsartCNr)))
			return false;
		if (!(that.bankkontoIId == null ? this.bankkontoIId == null
				: that.bankkontoIId.equals(this.bankkontoIId)))
			return false;
		if (!(that.kassenbuchIId == null ? this.kassenbuchIId == null
				: that.kassenbuchIId.equals(this.kassenbuchIId)))
			return false;
		if (!(that.rechnungIIdGutschrift == null ? this.rechnungIIdGutschrift == null
				: that.rechnungIIdGutschrift.equals(this.rechnungIIdGutschrift)))
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
		if (!(that.nBetragUst == null ? this.nBetragUst == null
				: that.nBetragUst.equals(this.nBetragUst)))
			return false;
		if (!(that.nBetragUstfw == null ? this.nBetragUstfw == null
				: that.nBetragUstfw.equals(this.nBetragUstfw)))
			return false;
		if (!(that.dWechselFaelligAm == null ? this.dWechselFaelligAm == null
				: that.dWechselFaelligAm.equals(this.dWechselFaelligAm)))
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
		if (!(that.fSkonto == null ? this.fSkonto == null : that.fSkonto
				.equals(this.fSkonto)))
			return false;
		if (!(that.rechnungzahlungIIdGutschrift == null ? this.rechnungzahlungIIdGutschrift == null
				: that.rechnungzahlungIIdGutschrift
						.equals(this.rechnungzahlungIIdGutschrift)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.rechnungIId.hashCode();
		result = 37 * result + this.dZahldatum.hashCode();
		result = 37 * result + this.zahlungsartCNr.hashCode();
		result = 37 * result + this.bankkontoIId.hashCode();
		result = 37 * result + this.kassenbuchIId.hashCode();
		result = 37 * result + this.rechnungIIdGutschrift.hashCode();
		result = 37 * result + this.nKurs.hashCode();
		result = 37 * result + this.nBetrag.hashCode();
		result = 37 * result + this.nBetragfw.hashCode();
		result = 37 * result + this.nBetragUst.hashCode();
		result = 37 * result + this.nBetragUstfw.hashCode();
		result = 37 * result + this.dWechselFaelligAm.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.fSkonto.hashCode();
		result = 37 * result + this.rechnungzahlungIIdGutschrift.hashCode();
		return result;
	}

	public String toString() {
		StringBuffer returnStringBuffer = new StringBuffer(640);
		returnStringBuffer.append("[");
		returnStringBuffer.append("iId:").append(iId);
		returnStringBuffer.append("rechnungIId:").append(rechnungIId);
		returnStringBuffer.append("dZahldatum:").append(dZahldatum);
		returnStringBuffer.append("zahlungsartCNr:").append(zahlungsartCNr);
		returnStringBuffer.append("bankkontoIId:").append(bankkontoIId);
		returnStringBuffer.append("kassenbuchIId:").append(kassenbuchIId);
		returnStringBuffer.append("rechnungIIdGutschrift:").append(
				rechnungIIdGutschrift);
		returnStringBuffer.append("nKurs:").append(nKurs);
		returnStringBuffer.append("nBetrag:").append(nBetrag);
		returnStringBuffer.append("nBetragfw:").append(nBetragfw);
		returnStringBuffer.append("nBetragUst:").append(nBetragUst);
		returnStringBuffer.append("nBetragUstfw:").append(nBetragUstfw);
		returnStringBuffer.append("dWechselFaelligAm:").append(
				dWechselFaelligAm);
		returnStringBuffer.append("tAnlegen:").append(tAnlegen);
		returnStringBuffer.append("personalIIdAnlegen:").append(
				personalIIdAnlegen);
		returnStringBuffer.append("tAendern:").append(tAendern);
		returnStringBuffer.append("personalIIdAendern:").append(
				personalIIdAendern);
		returnStringBuffer.append("fSkonto:").append(fSkonto);
		returnStringBuffer.append("rechnungzahlungIIdGutschrift:").append(
				rechnungzahlungIIdGutschrift);
		returnStringBuffer.append("]");
		return returnStringBuffer.toString();
	}

	public boolean isGutschrift() {
		return RechnungFac.RECHNUNGART_GUTSCHRIFT.equals(getZahlungsartCNr()) ;
	}

	public boolean isRechnung() {
		return RechnungFac.RECHNUNGART_RECHNUNG.equals(getZahlungsartCNr()) ;
	}

	public boolean isBank() {
		return RechnungFac.ZAHLUNGSART_BANK.equals(getZahlungsartCNr()) ;
	}

	public boolean isBar() {
		return RechnungFac.ZAHLUNGSART_BAR.equals(getZahlungsartCNr()) ;
	}

	public boolean isVorauszahlung() {
		return RechnungFac.ZAHLUNGSART_VORAUSZAHLUNG.equals(getZahlungsartCNr()) ;
	}
	
	@Override
	public String asString() {
		return "ARZahlung [" + getDZahldatum() + ", " + getNBetrag().toPlainString() 
				+ ", USt " + getNBetragUst().toPlainString() + ", rechnungId " + getRechnungIId() 
				+ ", gutschriftId " + getRechnungIIdGutschrift() + ", (id:" + iId + ")]" ;
	}
}

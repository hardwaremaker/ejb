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

public class ZahlungsvorschlagDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer zahlungsvorschlaglaufIId;
	private Integer eingangsrechnungIId;
	private Short bBezahlen;
	private Date tFaellig;
	private BigDecimal nAngewandterskontosatz;

	private BigDecimal nErBruttoBetrag;
	private BigDecimal nBereitsBezahlt;
	private BigDecimal nZahlbetrag;
	private Short bWaereVollstaendigBezahlt;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getZahlungsvorschlaglaufIId() {
		return zahlungsvorschlaglaufIId;
	}

	public void setZahlungsvorschlaglaufIId(Integer zahlungsvorschlaglaufIId) {
		this.zahlungsvorschlaglaufIId = zahlungsvorschlaglaufIId;
	}

	public Integer getEingangsrechnungIId() {
		return eingangsrechnungIId;
	}

	public void setEingangsrechnungIId(Integer eingangsrechnungIId) {
		this.eingangsrechnungIId = eingangsrechnungIId;
	}

	public Short getBBezahlen() {
		return bBezahlen;
	}

	public void setBBezahlen(Short bBezahlen) {
		this.bBezahlen = bBezahlen;
	}

	public Date getTFaellig() {
		return tFaellig;
	}

	public void setTFaellig(Date tFaellig) {
		this.tFaellig = tFaellig;
	}

	public BigDecimal getNAngewandterskontosatz() {
		return nAngewandterskontosatz;
	}

	public void setNAngewandterskontosatz(BigDecimal nAngewandterskontosatz) {
		this.nAngewandterskontosatz = nAngewandterskontosatz;
	}

	public BigDecimal getNErBruttoBetrag() {
		return nErBruttoBetrag;
	}

	public void setNErBruttoBetrag(BigDecimal nErBruttoBetrag) {
		this.nErBruttoBetrag = nErBruttoBetrag;
	}

	public BigDecimal getNBereitsBezahlt() {
		return nBereitsBezahlt;
	}

	public void setNBereitsBezahlt(BigDecimal nBereitsBezahlt) {
		this.nBereitsBezahlt = nBereitsBezahlt;
	}

	public BigDecimal getNZahlbetrag() {
		return nZahlbetrag;
	}

	public void setNZahlbetrag(BigDecimal nZahlbetrag) {
		this.nZahlbetrag = nZahlbetrag;
	}

	public Short getBWaereVollstaendigBezahlt() {
		return bWaereVollstaendigBezahlt;
	}

	public void setBWaereVollstaendigBezahlt(Short bWaereVollstaendigBezahlt) {
		this.bWaereVollstaendigBezahlt = bWaereVollstaendigBezahlt;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ZahlungsvorschlagDto))
			return false;
		ZahlungsvorschlagDto that = (ZahlungsvorschlagDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.zahlungsvorschlaglaufIId == null ? this.zahlungsvorschlaglaufIId == null
				: that.zahlungsvorschlaglaufIId
						.equals(this.zahlungsvorschlaglaufIId)))
			return false;
		if (!(that.eingangsrechnungIId == null ? this.eingangsrechnungIId == null
				: that.eingangsrechnungIId.equals(this.eingangsrechnungIId)))
			return false;
		if (!(that.bBezahlen == null ? this.bBezahlen == null : that.bBezahlen
				.equals(this.bBezahlen)))
			return false;
		if (!(that.tFaellig == null ? this.tFaellig == null : that.tFaellig
				.equals(this.tFaellig)))
			return false;
		if (!(that.nAngewandterskontosatz == null ? this.nAngewandterskontosatz == null
				: that.nAngewandterskontosatz
						.equals(this.nAngewandterskontosatz)))
			return false;

		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.zahlungsvorschlaglaufIId.hashCode();
		result = 37 * result + this.eingangsrechnungIId.hashCode();
		result = 37 * result + this.bBezahlen.hashCode();
		result = 37 * result + this.tFaellig.hashCode();
		result = 37 * result + this.nAngewandterskontosatz.hashCode();

		return result;
	}

	public String toString() {
		StringBuffer returnStringBuffer = new StringBuffer(224);
		returnStringBuffer.append("[");
		returnStringBuffer.append("iId:").append(iId);
		returnStringBuffer.append("zahlungsvorschlaglaufIId:").append(
				zahlungsvorschlaglaufIId);
		returnStringBuffer.append("eingangsrechnungIId:").append(
				eingangsrechnungIId);
		returnStringBuffer.append("bBezahlen:").append(bBezahlen);
		returnStringBuffer.append("tFaellig:").append(tFaellig);
		returnStringBuffer.append("nAngewandterskontosatz:").append(
				nAngewandterskontosatz);

		returnStringBuffer.append("]");
		return returnStringBuffer.toString();
	}
}

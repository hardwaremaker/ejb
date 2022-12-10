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

import com.lp.server.util.IModificationData;

public class BankverbindungDto implements Serializable, IModificationData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String mandantCNr;
	private Integer bankIId;
	private String cKontonummer;
	private Integer kontoIId;
	private String cBez;
	private String cIban;
	private Timestamp tAnlegen;
	private Integer personalIIdAnlegen;
	private Timestamp tAendern;
	private Integer personalIIdAendern;
	private boolean bInLiquiditaetsVorschau;
	private String cSepaVerzeichnis;
	private boolean bFuerSepaLastschrift;
	private boolean bAlsGeldtransitkonto;

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

	public Integer getBankIId() {
		return bankIId;
	}

	public void setBankIId(Integer bankIId) {
		this.bankIId = bankIId;
	}

	public String getCKontonummer() {
		return cKontonummer;
	}

	public void setCKontonummer(String cKontonummer) {
		this.cKontonummer = cKontonummer;
	}

	public Integer getKontoIId() {
		return kontoIId;
	}

	public void setKontoIId(Integer kontoIId) {
		this.kontoIId = kontoIId;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public String getCIban() {
		return cIban;
	}

	public void setCIban(String cIban) {
		this.cIban = cIban;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	private Integer iStellenAuszugsnummer;

	public Integer getIStellenAuszugsnummer() {
		return iStellenAuszugsnummer;
	}

	public void setIStellenAuszugsnummer(Integer iStellenAuszugsnummer) {
		this.iStellenAuszugsnummer = iStellenAuszugsnummer;
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

	public boolean isbInLiquiditaetsVorschau() {
		return bInLiquiditaetsVorschau;
	}

	public void setbInLiquiditaetsVorschau(boolean bInLiquiditaetsVorschau) {
		this.bInLiquiditaetsVorschau = bInLiquiditaetsVorschau;
	}

	public String getCSepaVerzeichnis() {
		return cSepaVerzeichnis;
	}

	public void setCSepaVerzeichnis(String cSepaVerzeichnis) {
		this.cSepaVerzeichnis = cSepaVerzeichnis;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (bInLiquiditaetsVorschau ? 1231 : 1237);
		result = prime * result + ((bankIId == null) ? 0 : bankIId.hashCode());
		result = prime * result + ((cBez == null) ? 0 : cBez.hashCode());
		result = prime * result + ((cIban == null) ? 0 : cIban.hashCode());
		result = prime * result + ((cKontonummer == null) ? 0 : cKontonummer.hashCode());
		result = prime * result + ((iId == null) ? 0 : iId.hashCode());
		result = prime * result + ((kontoIId == null) ? 0 : kontoIId.hashCode());
		result = prime * result + ((mandantCNr == null) ? 0 : mandantCNr.hashCode());
		result = prime * result + ((personalIIdAendern == null) ? 0 : personalIIdAendern.hashCode());
		result = prime * result + ((personalIIdAnlegen == null) ? 0 : personalIIdAnlegen.hashCode());
		result = prime * result + ((tAendern == null) ? 0 : tAendern.hashCode());
		result = prime * result + ((tAnlegen == null) ? 0 : tAnlegen.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BankverbindungDto other = (BankverbindungDto) obj;
		if (bInLiquiditaetsVorschau != other.bInLiquiditaetsVorschau)
			return false;
		if (bankIId == null) {
			if (other.bankIId != null)
				return false;
		} else if (!bankIId.equals(other.bankIId))
			return false;
		if (cBez == null) {
			if (other.cBez != null)
				return false;
		} else if (!cBez.equals(other.cBez))
			return false;
		if (cIban == null) {
			if (other.cIban != null)
				return false;
		} else if (!cIban.equals(other.cIban))
			return false;
		if (cKontonummer == null) {
			if (other.cKontonummer != null)
				return false;
		} else if (!cKontonummer.equals(other.cKontonummer))
			return false;
		if (iId == null) {
			if (other.iId != null)
				return false;
		} else if (!iId.equals(other.iId))
			return false;
		if (kontoIId == null) {
			if (other.kontoIId != null)
				return false;
		} else if (!kontoIId.equals(other.kontoIId))
			return false;
		if (mandantCNr == null) {
			if (other.mandantCNr != null)
				return false;
		} else if (!mandantCNr.equals(other.mandantCNr))
			return false;
		if (personalIIdAendern == null) {
			if (other.personalIIdAendern != null)
				return false;
		} else if (!personalIIdAendern.equals(other.personalIIdAendern))
			return false;
		if (personalIIdAnlegen == null) {
			if (other.personalIIdAnlegen != null)
				return false;
		} else if (!personalIIdAnlegen.equals(other.personalIIdAnlegen))
			return false;
		if (tAendern == null) {
			if (other.tAendern != null)
				return false;
		} else if (!tAendern.equals(other.tAendern))
			return false;
		if (tAnlegen == null) {
			if (other.tAnlegen != null)
				return false;
		} else if (!tAnlegen.equals(other.tAnlegen))
			return false;
		return true;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + mandantCNr;
		returnString += ", " + bankIId;
		returnString += ", " + cKontonummer;
		returnString += ", " + kontoIId;
		returnString += ", " + cBez;
		returnString += ", " + cIban;
		returnString += ", " + tAnlegen;
		returnString += ", " + personalIIdAnlegen;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdAendern;
		return returnString;
	}

	public boolean isbFuerSepaLastschrift() {
		return bFuerSepaLastschrift;
	}

	public void setbFuerSepaLastschrift(boolean bFuerSepaLastschrift) {
		this.bFuerSepaLastschrift = bFuerSepaLastschrift;
	}

	public boolean isbAlsGeldtransitkonto() {
		return bAlsGeldtransitkonto;
	}

	public void setbAlsGeldtransitkonto(boolean bAlsGeldtransitkonto) {
		this.bAlsGeldtransitkonto = bAlsGeldtransitkonto;
	}

}

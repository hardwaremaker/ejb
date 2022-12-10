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
package com.lp.server.artikel.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;

public class LagerbewegungDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String cBelegartnr;
	private Integer iBelegartpositionid;
	private Integer iIdBuchung;
	private Integer iBelegartid;
	private Integer lagerIId;
	private Integer artikelIId;
	private BigDecimal nMenge;
	private Integer personalIIdMengegeaendert;
	private Timestamp tBuchungszeit;
	private String cSeriennrchargennr;
	private BigDecimal nVerkaufspreis;
	private Integer personalIIdVerkaufspreisgeaendert;
	private BigDecimal nEinstandspreis;
	private Integer personalIIdEinstandspreisgeaendert;
	private BigDecimal nGestehungspreis;
	private Short bAbgang;
	private Short bVollstaendigverbraucht;
	private Timestamp tBelegdatum;
	private Short bHistorie;
	private Integer herstellerIId;
	private Integer landIId;

	private Integer gebindeIId;

	public Integer getGebindeIId() {
		return gebindeIId;
	}

	public void setGebindeIId(Integer gebindeIId) {
		this.gebindeIId = gebindeIId;
	}

	private BigDecimal nGebindemenge;

	public BigDecimal getNGebindemenge() {
		return nGebindemenge;
	}

	public void setNGebindemenge(BigDecimal nGebindemenge) {
		this.nGebindemenge = nGebindemenge;
	}

	private String cVersion;

	public String getCVersion() {
		return cVersion;
	}

	public void setCVersion(String cVersion) {
		this.cVersion = cVersion;
	}

	public Short getBHistorie() {
		return bHistorie;
	}

	public Integer getHerstellerIId() {
		return herstellerIId;
	}

	public void setHerstellerIId(Integer herstellerIId) {
		this.herstellerIId = herstellerIId;
	}

	public Integer getLandIId() {
		return landIId;
	}

	public void setLandIId(Integer landIId) {
		this.landIId = landIId;
	}

	public void setBHistorie(Short historie) {
		bHistorie = historie;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCBelegartnr() {
		return cBelegartnr;
	}

	public void setCBelegartnr(String cBelegartnr) {
		this.cBelegartnr = cBelegartnr;
	}

	public Integer getIBelegartpositionid() {
		return iBelegartpositionid;
	}

	public void setIBelegartpositionid(Integer iBelegartpositionid) {
		this.iBelegartpositionid = iBelegartpositionid;
	}

	public Integer getIIdBuchung() {
		return iIdBuchung;
	}

	public void setIIdBuchung(Integer iIdBuchung) {
		this.iIdBuchung = iIdBuchung;
	}

	public Integer getLagerIId() {
		return lagerIId;
	}

	public void setLagerIId(Integer lagerIId) {
		this.lagerIId = lagerIId;
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public BigDecimal getNMenge() {
		return nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge.setScale(4, BigDecimal.ROUND_HALF_EVEN);
	}

	public Timestamp getTBuchungszeit() {
		return tBuchungszeit;
	}

	public void setTBuchungszeit(Timestamp tBuchungszeit) {
		this.tBuchungszeit = tBuchungszeit;
	}

	public String getCSeriennrchargennr() {
		return cSeriennrchargennr;
	}

	public void setCSeriennrchargennr(String cSeriennrchargennr) {
		this.cSeriennrchargennr = cSeriennrchargennr;
	}

	public BigDecimal getNVerkaufspreis() {
		return nVerkaufspreis;
	}

	public void setNVerkaufspreis(BigDecimal nVerkaufspreis) {
		this.nVerkaufspreis = nVerkaufspreis;
	}

	public Integer getPersonalIIdVerkaufspreisgeaendert() {
		return personalIIdVerkaufspreisgeaendert;
	}

	public void setPersonalIIdVerkaufspreisgeaendert(
			Integer iPersonVerkaufspreisgeaendert) {
		this.personalIIdVerkaufspreisgeaendert = iPersonVerkaufspreisgeaendert;
	}

	public BigDecimal getNEinstandspreis() {
		return nEinstandspreis;
	}

	public void setNEinstandspreis(BigDecimal nEinstandspreis) {
		this.nEinstandspreis = nEinstandspreis;
	}

	public Integer getPersonalIIdEinstandspreisgeaendert() {
		return personalIIdEinstandspreisgeaendert;
	}

	public void setPersonalIIdEinstandspreisgeaendert(
			Integer iPersonEinstandspreisgeaendert) {
		this.personalIIdEinstandspreisgeaendert = iPersonEinstandspreisgeaendert;
	}

	public BigDecimal getNGestehungspreis() {
		return nGestehungspreis;
	}

	public void setNGestehungspreis(BigDecimal nGestehungspreis) {
		this.nGestehungspreis = nGestehungspreis;
	}

	public Short getBAbgang() {
		return bAbgang;
	}

	public Integer getPersonalIIdMengegeaendert() {
		return personalIIdMengegeaendert;
	}

	public void setBAbgang(Short bAbgang) {
		this.bAbgang = bAbgang;
	}

	public void setPersonalIIdMengegeaendert(Integer iPersonMengegeaendert) {
		this.personalIIdMengegeaendert = iPersonMengegeaendert;
	}

	public Short getBVollstaendigverbraucht() {
		return bVollstaendigverbraucht;
	}

	public Integer getIBelegartid() {
		return iBelegartid;
	}

	public Timestamp getTBelegdatum() {
		return tBelegdatum;
	}

	public void setBVollstaendigverbraucht(Short bVollstaendigverbraucht) {
		this.bVollstaendigverbraucht = bVollstaendigverbraucht;
	}

	public void setIBelegartid(Integer iBelegartid) {
		this.iBelegartid = iBelegartid;
	}

	public void setTBelegdatum(Timestamp tBelegdatum) {
		this.tBelegdatum = tBelegdatum;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof LagerbewegungDto)) {
			return false;
		}
		LagerbewegungDto that = (LagerbewegungDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.cBelegartnr == null ? this.cBelegartnr == null
				: that.cBelegartnr.equals(this.cBelegartnr))) {
			return false;
		}
		if (!(that.iBelegartpositionid == null ? this.iBelegartpositionid == null
				: that.iBelegartpositionid.equals(this.iBelegartpositionid))) {
			return false;
		}
		if (!(that.iIdBuchung == null ? this.iIdBuchung == null
				: that.iIdBuchung.equals(this.iIdBuchung))) {
			return false;
		}
		if (!(that.lagerIId == null ? this.lagerIId == null : that.lagerIId
				.equals(this.lagerIId))) {
			return false;
		}
		if (!(that.artikelIId == null ? this.artikelIId == null
				: that.artikelIId.equals(this.artikelIId))) {
			return false;
		}
		if (!(that.nMenge == null ? this.nMenge == null : that.nMenge
				.equals(this.nMenge))) {
			return false;
		}
		if (!(that.personalIIdMengegeaendert == null ? this.personalIIdMengegeaendert == null
				: that.personalIIdMengegeaendert
						.equals(this.personalIIdMengegeaendert))) {
			return false;
		}
		if (!(that.tBuchungszeit == null ? this.tBuchungszeit == null
				: that.tBuchungszeit.equals(this.tBuchungszeit))) {
			return false;
		}
		if (!(that.cSeriennrchargennr == null ? this.cSeriennrchargennr == null
				: that.cSeriennrchargennr.equals(this.cSeriennrchargennr))) {
			return false;
		}
		if (!(that.nVerkaufspreis == null ? this.nVerkaufspreis == null
				: that.nVerkaufspreis.equals(this.nVerkaufspreis))) {
			return false;
		}
		if (!(that.personalIIdVerkaufspreisgeaendert == null ? this.personalIIdVerkaufspreisgeaendert == null
				: that.personalIIdVerkaufspreisgeaendert
						.equals(this.personalIIdVerkaufspreisgeaendert))) {
			return false;
		}
		if (!(that.nEinstandspreis == null ? this.nEinstandspreis == null
				: that.nEinstandspreis.equals(this.nEinstandspreis))) {
			return false;
		}
		if (!(that.personalIIdEinstandspreisgeaendert == null ? this.personalIIdEinstandspreisgeaendert == null
				: that.personalIIdEinstandspreisgeaendert
						.equals(this.personalIIdEinstandspreisgeaendert))) {
			return false;
		}
		if (!(that.nGestehungspreis == null ? this.nGestehungspreis == null
				: that.nGestehungspreis.equals(this.nGestehungspreis))) {
			return false;
		}
		if (!(that.bAbgang == null ? this.bAbgang == null : that.bAbgang
				.equals(this.bAbgang))) {
			return false;
		}
		if (!(that.bVollstaendigverbraucht == null ? this.bVollstaendigverbraucht == null
				: that.bVollstaendigverbraucht
						.equals(this.bVollstaendigverbraucht))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.cBelegartnr.hashCode();
		result = 37 * result + this.iBelegartpositionid.hashCode();
		result = 37 * result + this.iIdBuchung.hashCode();
		result = 37 * result + this.lagerIId.hashCode();
		result = 37 * result + this.artikelIId.hashCode();
		result = 37 * result + this.nMenge.hashCode();
		result = 37 * result + this.personalIIdMengegeaendert.hashCode();
		result = 37 * result + this.tBuchungszeit.hashCode();
		result = 37 * result + this.cSeriennrchargennr.hashCode();
		result = 37 * result + this.nVerkaufspreis.hashCode();
		result = 37 * result
				+ this.personalIIdVerkaufspreisgeaendert.hashCode();
		result = 37 * result + this.nEinstandspreis.hashCode();
		result = 37 * result
				+ this.personalIIdEinstandspreisgeaendert.hashCode();
		result = 37 * result + this.nGestehungspreis.hashCode();
		result = 37 * result + this.bAbgang.hashCode();
		result = 37 * result + this.bVollstaendigverbraucht.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + cBelegartnr;
		returnString += ", " + iBelegartpositionid;
		returnString += ", " + iIdBuchung;
		returnString += ", " + lagerIId;
		returnString += ", " + artikelIId;
		returnString += ", " + nMenge;
		returnString += ", " + personalIIdMengegeaendert;
		returnString += ", " + tBuchungszeit;
		returnString += ", " + cSeriennrchargennr;
		returnString += ", " + nVerkaufspreis;
		returnString += ", " + personalIIdVerkaufspreisgeaendert;
		returnString += ", " + nEinstandspreis;
		returnString += ", " + personalIIdEinstandspreisgeaendert;
		returnString += ", " + nGestehungspreis;
		returnString += ", " + bAbgang;
		returnString += ", " + bVollstaendigverbraucht;
		return returnString;
	}
}

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

public class ArtgruDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cNr;
	private Integer artgruIId;
	private Integer iId;
	private ArtgrusprDto artgrusprDto;
	private Integer kontoIId;

	private Integer artikelIIdKommentar;

	public Integer getArtikelIIdKommentar() {
		return artikelIIdKommentar;
	}

	public void setArtikelIIdKommentar(Integer artikelIIdKommentar) {
		this.artikelIIdKommentar = artikelIIdKommentar;
	}

	private Short bAufschlagEinzelpreis;

	public Short getBAufschlagEinzelpreis() {
		return bAufschlagEinzelpreis;
	}

	public void setBAufschlagEinzelpreis(Short bAufschlagEinzelpreis) {
		this.bAufschlagEinzelpreis = bAufschlagEinzelpreis;
	}

	private Short bKeinBelegdruckMitRabatt;

	public Short getBKeinBelegdruckMitRabatt() {
		return bKeinBelegdruckMitRabatt;
	}

	public void setBKeinBelegdruckMitRabatt(Short bKeinBelegdruckMitRabatt) {
		this.bKeinBelegdruckMitRabatt = bKeinBelegdruckMitRabatt;
	}

	private Short bSeriennrtragend;
	private Short bChargennrtragend;

	public Short getBSeriennrtragend() {
		return this.bSeriennrtragend;
	}

	public void setBSeriennrtragend(Short bSeriennrtragend) {
		this.bSeriennrtragend = bSeriennrtragend;
	}

	public Short getBChargennrtragend() {
		return this.bChargennrtragend;
	}

	public void setBChargennrtragend(Short bChargennrtragend) {
		this.bChargennrtragend = bChargennrtragend;
	}

	private BigDecimal nEkpreisaufschlag;

	public BigDecimal getNEkpreisaufschlag() {
		return nEkpreisaufschlag;
	}

	public void setNEkpreisaufschlag(BigDecimal nEkpreisaufschlag) {
		this.nEkpreisaufschlag = nEkpreisaufschlag;
	}

	private Integer kostenstelleIId;

	public Integer getKostenstelleIId() {
		return this.kostenstelleIId;
	}

	public void setKostenstelleIId(Integer kostenstelle) {
		this.kostenstelleIId = kostenstelle;
	}

	private Short bKeinevkwarnmeldungimls;

	public Short getBKeinevkwarnmeldungimls() {
		return bKeinevkwarnmeldungimls;
	}

	public void setBKeinevkwarnmeldungimls(Short bKeinevkwarnmeldungimls) {
		this.bKeinevkwarnmeldungimls = bKeinevkwarnmeldungimls;
	}

	private Short bZertifizierung;

	public Short getBZertifizierung() {
		return bZertifizierung;
	}

	public void setBZertifizierung(Short bZertifizierung) {
		this.bZertifizierung = bZertifizierung;
	}

	private Short bRueckgabe;

	public Short getBRueckgabe() {
		return bRueckgabe;
	}

	public void setBRueckgabe(Short rueckgabe) {
		bRueckgabe = rueckgabe;
	}

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}
	
	private Short bFremdfertigung;

	public Short getBFremdfertigung() {
		return this.bFremdfertigung;
	}

	public void setBFremdfertigung(Short bFremdfertigung) {
		this.bFremdfertigung = bFremdfertigung;
	}
	
	private Short bBeiErsterZeitbuchungAbbuchen;
	

	public Short getBBeiErsterZeitbuchungAbbuchen() {
		return bBeiErsterZeitbuchungAbbuchen;
	}

	public void setBBeiErsterZeitbuchungAbbuchen(Short bBeiErsterZeitbuchungAbbuchen) {
		this.bBeiErsterZeitbuchungAbbuchen = bBeiErsterZeitbuchungAbbuchen;
	}

	

	public Integer getArtgruIId() {
		return artgruIId;
	}

	public String getBezeichnung() {
		if (getArtgrusprDto() != null) {
			if (getArtgrusprDto().getCBez() != null) {
				return getArtgrusprDto().getCBez();
			} else {
				return getCNr().trim();
			}
		} else {
			return getCNr().trim();
		}
	}

	public void setArtgruIId(Integer artgruIId) {
		this.artgruIId = artgruIId;
	}

	public ArtgrusprDto getArtgrusprDto() {
		return artgrusprDto;
	}

	public Integer getIId() {
		return iId;
	}

	public Integer getKontoIId() {
		return kontoIId;
	}

	public void setArtgrusprDto(ArtgrusprDto artgrusprDto) {
		this.artgrusprDto = artgrusprDto;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	private String mandantCNr;

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public void setKontoIId(Integer kontoIId) {
		this.kontoIId = kontoIId;
	}

	public boolean isChargennrtragend() {
		if (null == bChargennrtragend)
			return false;
		return bChargennrtragend > 0;
	}

	public boolean isSeriennrtragend() {
		if (null == bSeriennrtragend)
			return false;
		return bSeriennrtragend > 0;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ArtgruDto)) {
			return false;
		}
		ArtgruDto that = (ArtgruDto) obj;
		if (!(that.cNr == null ? this.cNr == null : that.cNr.equals(this.cNr))) {
			return false;
		}
		if (!(that.artgruIId == null ? this.artgruIId == null : that.artgruIId.equals(this.artgruIId))) {
			return false;
		}
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		return true;
	}

	private Integer personalIIdAnlegen;
	private Integer personalIIdAendern;
	private Timestamp tAendern;
	private Timestamp tAnlegen;

	public Timestamp getTAnlegen() {
		return this.tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.cNr.hashCode();
		result = 37 * result + this.artgruIId.hashCode();
		result = 37 * result + this.iId.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += cNr;
		returnString += ", " + artgruIId;
		returnString += ", " + iId;
		return returnString;
	}
}

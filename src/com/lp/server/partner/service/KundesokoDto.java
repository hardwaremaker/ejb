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
package com.lp.server.partner.service;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class KundesokoDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer kundeIId;
	private Integer artikelIId;
	private Integer artgruIId;
	private Date tPreisgueltigab;
	private Date tPreisgueltigbis;
	private String cBemerkung;
	private Short bBemerkungdrucken;
	private Short bRabattsichtbar;
	private Short bDrucken;
	private Integer personalIIdAendern;
	private Timestamp tAendern;
	private String cKundeartikelnummer;

	private Short bWirktNichtFuerPreisfindung;

	public Short getBWirktNichtFuerPreisfindung() {
		return bWirktNichtFuerPreisfindung;
	}

	public void setBWirktNichtFuerPreisfindung(Short bWirktNichtFuerPreisfindung) {
		this.bWirktNichtFuerPreisfindung = bWirktNichtFuerPreisfindung;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getKundeIId() {
		return kundeIId;
	}

	public void setKundeIId(Integer kundeIId) {
		this.kundeIId = kundeIId;
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Integer getArtgruIId() {
		return artgruIId;
	}

	public void setArtgruIId(Integer artgruIId) {
		this.artgruIId = artgruIId;
	}

	public Date getTPreisgueltigab() {
		return tPreisgueltigab;
	}

	public void setTPreisgueltigab(Date tPreisgueltigab) {
		this.tPreisgueltigab = tPreisgueltigab;
	}

	public Date getTPreisgueltigbis() {
		return tPreisgueltigbis;
	}

	public void setTPreisgueltigbis(Date tPreisgueltigbis) {
		this.tPreisgueltigbis = tPreisgueltigbis;
	}

	public String getCBemerkung() {
		return cBemerkung;
	}

	public void setCBemerkung(String cBemerkung) {
		this.cBemerkung = cBemerkung;
	}

	public Short getBBemerkungdrucken() {
		return bBemerkungdrucken;
	}

	public void setBBemerkungdrucken(Short bBemerkungdrucken) {
		this.bBemerkungdrucken = bBemerkungdrucken;
	}

	public Short getBRabattsichtbar() {
		return bRabattsichtbar;
	}

	public void setBRabattsichtbar(Short bRabattsichtbar) {
		this.bRabattsichtbar = bRabattsichtbar;
	}

	public Short getBDrucken() {
		return bDrucken;
	}

	public void setBDrucken(Short bDrucken) {
		this.bDrucken = bDrucken;
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

	public String getCKundeartikelnummer() {
		return cKundeartikelnummer;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public void setCKundeartikelnummer(String cKundeartikelnummer) {
		this.cKundeartikelnummer = cKundeartikelnummer;
	}

	private String cKundeartikelbez;
	private String cKundeartikelzbez;

	public String getCKundeartikelbez() {
		return cKundeartikelbez;
	}

	public void setCKundeartikelbez(String cKundeartikelbez) {
		this.cKundeartikelbez = cKundeartikelbez;
	}

	public String getCKundeartikelzbez() {
		return cKundeartikelzbez;
	}

	public void setCKundeartikelzbez(String cKundeartikelzbez) {
		this.cKundeartikelzbez = cKundeartikelzbez;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof KundesokoDto))
			return false;
		KundesokoDto that = (KundesokoDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.kundeIId == null ? this.kundeIId == null : that.kundeIId
				.equals(this.kundeIId)))
			return false;
		if (!(that.artikelIId == null ? this.artikelIId == null
				: that.artikelIId.equals(this.artikelIId)))
			return false;
		if (!(that.artgruIId == null ? this.artgruIId == null : that.artgruIId
				.equals(this.artgruIId)))
			return false;
		if (!(that.tPreisgueltigab == null ? this.tPreisgueltigab == null
				: that.tPreisgueltigab.equals(this.tPreisgueltigab)))
			return false;
		if (!(that.tPreisgueltigbis == null ? this.tPreisgueltigbis == null
				: that.tPreisgueltigbis.equals(this.tPreisgueltigbis)))
			return false;
		if (!(that.cBemerkung == null ? this.cBemerkung == null
				: that.cBemerkung.equals(this.cBemerkung)))
			return false;
		if (!(that.bBemerkungdrucken == null ? this.bBemerkungdrucken == null
				: that.bBemerkungdrucken.equals(this.bBemerkungdrucken)))
			return false;
		if (!(that.bRabattsichtbar == null ? this.bRabattsichtbar == null
				: that.bRabattsichtbar.equals(this.bRabattsichtbar)))
			return false;
		if (!(that.bDrucken == null ? this.bDrucken == null : that.bDrucken
				.equals(this.bDrucken)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.kundeIId.hashCode();
		result = 37 * result + this.artikelIId.hashCode();
		result = 37 * result + this.artgruIId.hashCode();
		result = 37 * result + this.tPreisgueltigab.hashCode();
		result = 37 * result + this.tPreisgueltigbis.hashCode();
		result = 37 * result + this.cBemerkung.hashCode();
		result = 37 * result + this.bBemerkungdrucken.hashCode();
		result = 37 * result + this.bRabattsichtbar.hashCode();
		result = 37 * result + this.bDrucken.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + kundeIId;
		returnString += ", " + artikelIId;
		returnString += ", " + artgruIId;
		returnString += ", " + tPreisgueltigab;
		returnString += ", " + tPreisgueltigbis;
		returnString += ", " + cBemerkung;
		returnString += ", " + bBemerkungdrucken;
		returnString += ", " + bRabattsichtbar;
		returnString += ", " + bDrucken;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + tAendern;
		return returnString;
	}
}

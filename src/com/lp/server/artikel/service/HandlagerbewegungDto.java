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
package com.lp.server.artikel.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class HandlagerbewegungDto implements Serializable {
	public boolean isBAendereLagerplatz() {
		return bAendereLagerplatz;
	}

	public void setBAendereLagerplatz(boolean aendereLagerplatz) {
		bAendereLagerplatz = aendereLagerplatz;
	}
	private List<SeriennrChargennrMitMengeDto> alSeriennrChargennrMitMenge = null;
	

	public List<SeriennrChargennrMitMengeDto> getSeriennrChargennrMitMenge() {
		return alSeriennrChargennrMitMenge;
	}

	public void setSeriennrChargennrMitMenge(
			List<SeriennrChargennrMitMengeDto> alSeriennrChargennrMitMenge) {
		this.alSeriennrChargennrMitMenge = alSeriennrChargennrMitMenge;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer artikelIId;
	private Integer lagerIId;
	private BigDecimal nMenge;
	private String cKommentar;
	private Timestamp tBuchungszeit;
	private Integer personalIIdAendern;
	private Short bAbgang;
	private BigDecimal nGestehungspreis;
	private BigDecimal nEinstandspreis;
	private BigDecimal nVerkaufspreis;
	private LagerDto lagerDto;
	private ArtikelDto artikelDto;
	private String belegartCNrUrsprung;
	private Integer belegartpositionIIdUrsprung;
	private String cSeriennummerchargennummerUrsprung;
	private Integer lagerplatzIId;
	private Integer herstellerIId;
	private Integer landIId;
	
	
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

	private boolean bAendereLagerplatz=false;

	public Integer getLagerplatzIId() {
		return lagerplatzIId;
	}

	public void setLagerplatzIId(Integer lagerplatzIId) {
		this.lagerplatzIId = lagerplatzIId;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Integer getLagerIId() {
		return lagerIId;
	}

	public void setLagerIId(Integer lagerIId) {
		this.lagerIId = lagerIId;
	}

	public BigDecimal getNMenge() {
		return nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public String getCKommentar() {
		return cKommentar;
	}

	public void setCKommentar(String cKommentar) {
		this.cKommentar = cKommentar;
	}

	public Timestamp getTBuchungszeit() {
		return tBuchungszeit;
	}

	public void setTBuchungszeit(Timestamp tBuchungszeit) {
		this.tBuchungszeit = tBuchungszeit;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Short getBAbgang() {
		return bAbgang;
	}

	public void setBAbgang(Short bAbgang) {
		this.bAbgang = bAbgang;
	}

	public BigDecimal getNGestehungspreis() {
		return nGestehungspreis;
	}

	public void setNGestehungspreis(BigDecimal nGestehungspreis) {
		this.nGestehungspreis = nGestehungspreis;
	}

	public BigDecimal getNEinstandspreis() {
		return nEinstandspreis;
	}

	public void setNEinstandspreis(BigDecimal nEinstandspreis) {
		this.nEinstandspreis = nEinstandspreis;
	}

	public BigDecimal getNVerkaufspreis() {
		return nVerkaufspreis;
	}

	public LagerDto getLagerDto() {
		return lagerDto;
	}

	public ArtikelDto getArtikelDto() {
		return artikelDto;
	}

	public Integer getBelegartpositionIIdUrsprung() {
		return belegartpositionIIdUrsprung;
	}

	public String getBelegartCNrUrsprung() {
		return belegartCNrUrsprung;
	}

	public String getCSeriennummerchargennummerUrsprung() {
		return cSeriennummerchargennummerUrsprung;
	}

	public void setNVerkaufspreis(BigDecimal nVerkaufspreis) {
		this.nVerkaufspreis = nVerkaufspreis;
	}

	public void setLagerDto(LagerDto lagerDto) {
		this.lagerDto = lagerDto;
	}

	public void setArtikelDto(ArtikelDto artikelDto) {
		this.artikelDto = artikelDto;
	}

	public void setBelegartpositionIIdUrsprung(
			Integer belegartpositionIIdUrsprung) {
		this.belegartpositionIIdUrsprung = belegartpositionIIdUrsprung;
	}

	public void setBelegartCNrUrsprung(String belegartCNrUrsprung) {
		this.belegartCNrUrsprung = belegartCNrUrsprung;
	}

	public void setCSeriennummerchargennummerUrsprung(
			String cSeriennummerchargennummerUrsprung) {
		this.cSeriennummerchargennummerUrsprung = cSeriennummerchargennummerUrsprung;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof HandlagerbewegungDto))
			return false;
		HandlagerbewegungDto that = (HandlagerbewegungDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.artikelIId == null ? this.artikelIId == null
				: that.artikelIId.equals(this.artikelIId)))
			return false;
		if (!(that.lagerIId == null ? this.lagerIId == null : that.lagerIId
				.equals(this.lagerIId)))
			return false;
		if (!(that.nMenge == null ? this.nMenge == null : that.nMenge
				.equals(this.nMenge)))
			return false;
		if (!(that.cKommentar == null ? this.cKommentar == null
				: that.cKommentar.equals(this.cKommentar)))
			return false;
		if (!(that.tBuchungszeit == null ? this.tBuchungszeit == null
				: that.tBuchungszeit.equals(this.tBuchungszeit)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		if (!(that.bAbgang == null ? this.bAbgang == null : that.bAbgang
				.equals(this.bAbgang)))
			return false;
		if (!(that.nGestehungspreis == null ? this.nGestehungspreis == null
				: that.nGestehungspreis.equals(this.nGestehungspreis)))
			return false;
		if (!(that.nEinstandspreis == null ? this.nEinstandspreis == null
				: that.nEinstandspreis.equals(this.nEinstandspreis)))
			return false;
		if (!(that.nVerkaufspreis == null ? this.nVerkaufspreis == null
				: that.nVerkaufspreis.equals(this.nVerkaufspreis)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.artikelIId.hashCode();
		result = 37 * result + this.lagerIId.hashCode();
		result = 37 * result + this.nMenge.hashCode();
		result = 37 * result + this.cKommentar.hashCode();
		result = 37 * result + this.tBuchungszeit.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.bAbgang.hashCode();
		result = 37 * result + this.nGestehungspreis.hashCode();
		result = 37 * result + this.nEinstandspreis.hashCode();
		result = 37 * result + this.nVerkaufspreis.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + artikelIId;
		returnString += ", " + lagerIId;
		returnString += ", " + nMenge;
		returnString += ", " + cKommentar;
		returnString += ", " + tBuchungszeit;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + bAbgang;
		returnString += ", " + nGestehungspreis;
		returnString += ", " + nEinstandspreis;
		returnString += ", " + nVerkaufspreis;
		return returnString;
	}
}

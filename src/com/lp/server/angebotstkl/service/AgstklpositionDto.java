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
package com.lp.server.angebotstkl.service;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lp.service.BelegpositionDto;

public class AgstklpositionDto extends BelegpositionDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Double fRabattsatz;
	private BigDecimal nGestehungspreis;
	private Short bRabattsatzuebersteuert;
	private Double fZusatzrabattsatz;
	private BigDecimal nNettoeinzelpreis;
	private BigDecimal nNettogesamtpreis;
	private Short bDrucken;

	public Integer getAgstklIId() {
		return super.getBelegIId();
	}

	public void setAgstklIId(Integer agstklIId) {
		super.setBelegIId(agstklIId);
	}

	public String getAgstklpositionsartCNr() {
		return super.getPositionsartCNr();
	}

	public void setAgstklpositionsartCNr(String agstklpositionsartCNr) {
		super.setPositionsartCNr(agstklpositionsartCNr);
	}

	public Double getFRabattsatz() {
		return fRabattsatz;
	}

	public void setFRabattsatz(Double fRabattsatz) {
		this.fRabattsatz = fRabattsatz;
	}

	public Short getBRabattsatzuebersteuert() {
		return bRabattsatzuebersteuert;
	}

	public void setBRabattsatzuebersteuert(Short bRabattsatzuebersteuert) {
		this.bRabattsatzuebersteuert = bRabattsatzuebersteuert;
	}

	public Double getFZusatzrabattsatz() {
		return fZusatzrabattsatz;
	}

	public void setFZusatzrabattsatz(Double fZusatzrabattsatz) {
		this.fZusatzrabattsatz = fZusatzrabattsatz;
	}

	public BigDecimal getNNettoeinzelpreis() {
		return nNettoeinzelpreis;
	}

	public void setNNettoeinzelpreis(BigDecimal nNettoeinzelpreis) {
		this.nNettoeinzelpreis = nNettoeinzelpreis;
	}

	public BigDecimal getNNettogesamtpreis() {
		return nNettogesamtpreis;
	}

	public void setNNettogesamtpreis(BigDecimal nNettogesamtpreis) {
		this.nNettogesamtpreis = nNettogesamtpreis;
	}

	public Short getBDrucken() {
		return bDrucken;
	}

	public BigDecimal getNGestehungspreis() {
		return nGestehungspreis;
	}

	public String getCZbez() {
		return getCZusatzbez();
	}

	public void setBDrucken(Short bDrucken) {
		this.bDrucken = bDrucken;
	}

	public void setNGestehungspreis(BigDecimal nGestehungspreis) {
		this.nGestehungspreis = nGestehungspreis;
	}

	public void setCZbez(String cZbez) {
		this.setCZusatzbez(cZbez);
	}

	private BigDecimal nAufschlag;
	private Short bAufschlaggesamtFixiert;
	private BigDecimal nNettogesamtmitaufschlag;
	private Double fAufschlag;

	public Short getBAufschlaggesamtFixiert() {
		return bAufschlaggesamtFixiert;
	}

	public void setBAufschlaggesamtFixiert(Short bAufschlaggesamtFixiert) {
		this.bAufschlaggesamtFixiert = bAufschlaggesamtFixiert;
	}

	public BigDecimal getNAufschlag() {
		return nAufschlag;
	}

	public void setNAufschlag(BigDecimal nAufschlag) {
		this.nAufschlag = nAufschlag;
	}

	public BigDecimal getNNettogesamtmitaufschlag() {
		return nNettogesamtmitaufschlag;
	}

	public void setNNettogesamtmitaufschlag(BigDecimal nNettogesamtmitaufschlag) {
		this.nNettogesamtmitaufschlag = nNettogesamtmitaufschlag;
	}

	public Double getFAufschlag() {
		return fAufschlag;
	}

	public void setFAufschlag(Double fAufschlag) {
		this.fAufschlag = fAufschlag;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof AgstklpositionDto))
			return false;
		AgstklpositionDto that = (AgstklpositionDto) obj;
		if (!(that.getIId() == null ? this.getIId() == null : that.getIId()
				.equals(this.getIId())))
			return false;
		if (!(that.getBelegIId() == null ? this.getBelegIId() == null : that
				.getBelegIId().equals(this.getBelegIId())))
			return false;
		if (!(that.getISort() == null ? this.getISort() == null : that
				.getISort().equals(this.getISort())))
			return false;
		if (!(that.getPositionsartCNr() == null ? this.getPositionsartCNr() == null
				: that.getPositionsartCNr().equals(this.getPositionsartCNr())))
			return false;
		if (!(that.getArtikelIId() == null ? this.getArtikelIId() == null
				: that.getArtikelIId().equals(this.getArtikelIId())))
			return false;
		if (!(that.getCBez() == null ? this.getCBez() == null : that.getCBez()
				.equals(this.getCBez())))
			return false;
		if (!(that.getBArtikelbezeichnunguebersteuert() == null ? this
				.getBArtikelbezeichnunguebersteuert() == null : that
				.getBArtikelbezeichnunguebersteuert().equals(
						this.getBArtikelbezeichnunguebersteuert())))
			return false;
		if (!(that.getNMenge() == null ? this.getNMenge() == null : that
				.getNMenge().equals(this.getNMenge())))
			return false;
		if (!(that.getEinheitCNr() == null ? this.getEinheitCNr() == null
				: that.getEinheitCNr().equals(this.getEinheitCNr())))
			return false;
		if (!(that.fRabattsatz == null ? this.fRabattsatz == null
				: that.fRabattsatz.equals(this.fRabattsatz)))
			return false;
		if (!(that.bRabattsatzuebersteuert == null ? this.bRabattsatzuebersteuert == null
				: that.bRabattsatzuebersteuert
						.equals(this.bRabattsatzuebersteuert)))
			return false;
		if (!(that.fZusatzrabattsatz == null ? this.fZusatzrabattsatz == null
				: that.fZusatzrabattsatz.equals(this.fZusatzrabattsatz)))
			return false;
		if (!(that.nNettoeinzelpreis == null ? this.nNettoeinzelpreis == null
				: that.nNettoeinzelpreis.equals(this.nNettoeinzelpreis)))
			return false;
		if (!(that.nNettogesamtpreis == null ? this.nNettogesamtpreis == null
				: that.nNettogesamtpreis.equals(this.nNettogesamtpreis)))
			return false;
		if (!(that.bDrucken == null ? this.bDrucken == null : that.bDrucken
				.equals(this.bDrucken)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.getIId().hashCode();
		result = 37 * result + this.getBelegIId().hashCode();
		result = 37 * result + this.getISort().hashCode();
		result = 37 * result + this.getPositionsartCNr().hashCode();
		result = 37 * result + this.getArtikelIId().hashCode();
		result = 37 * result + this.getCBez().hashCode();
		result = 37 * result
				+ this.getBArtikelbezeichnunguebersteuert().hashCode();
		result = 37 * result + this.getNMenge().hashCode();
		result = 37 * result + this.getEinheitCNr().hashCode();
		result = 37 * result + this.fRabattsatz.hashCode();
		result = 37 * result + this.bRabattsatzuebersteuert.hashCode();
		result = 37 * result + this.fZusatzrabattsatz.hashCode();
		result = 37 * result + this.nNettoeinzelpreis.hashCode();
		result = 37 * result + this.nNettogesamtpreis.hashCode();
		result = 37 * result + this.bDrucken.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += getIId();
		returnString += ", " + getBelegIId();
		returnString += ", " + getISort();
		returnString += ", " + getPositionsartCNr();
		returnString += ", " + getArtikelIId();
		returnString += ", " + getCBez();
		returnString += ", " + getBArtikelbezeichnunguebersteuert();
		returnString += ", " + getNMenge();
		returnString += ", " + getEinheitCNr();
		returnString += ", " + fRabattsatz;
		returnString += ", " + bRabattsatzuebersteuert;
		returnString += ", " + fZusatzrabattsatz;
		returnString += ", " + nNettoeinzelpreis;
		returnString += ", " + nNettogesamtpreis;
		returnString += ", " + bDrucken;
		return returnString;
	}
}

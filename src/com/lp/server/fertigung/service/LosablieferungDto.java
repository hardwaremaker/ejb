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
package com.lp.server.fertigung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.lp.server.artikel.service.GeraetesnrDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;

public class LosablieferungDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer losIId;
	private BigDecimal nMenge;
	private BigDecimal nGestehungspreis;
	private BigDecimal nMaterialwert;
	private BigDecimal nArbeitszeitwert;
	private Integer personalIIdAendern;
	private Timestamp tAendern;
	private BigDecimal nMaterialwertdetailliert;
	private BigDecimal nArbeitszeitwertdetailliert;
	private Short bGestehungspreisNeuBerechnen;
	
	private List<SeriennrChargennrMitMengeDto> alSeriennrChargennrMitMenge = null;
	

	public List<SeriennrChargennrMitMengeDto> getSeriennrChargennrMitMenge() {
		return alSeriennrChargennrMitMenge;
	}

	public void setSeriennrChargennrMitMenge(
			List<SeriennrChargennrMitMengeDto> alSeriennrChargennrMitMenge) {
		this.alSeriennrChargennrMitMenge = alSeriennrChargennrMitMenge;
	}
	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getLosIId() {
		return losIId;
	}

	public void setLosIId(Integer losIId) {
		this.losIId = losIId;
	}

	public BigDecimal getNMenge() {
		return nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public BigDecimal getNGestehungspreis() {
		return nGestehungspreis;
	}

	public void setNGestehungspreis(BigDecimal nGestehungspreis) {
		this.nGestehungspreis = nGestehungspreis;
	}

	public BigDecimal getNMaterialwert() {
		return nMaterialwert;
	}

	public void setNMaterialwert(BigDecimal nMaterialwert) {
		this.nMaterialwert = nMaterialwert;
	}

	public BigDecimal getNArbeitszeitwert() {
		return nArbeitszeitwert;
	}

	public void setNArbeitszeitwert(BigDecimal nArbeitszeitwert) {
		this.nArbeitszeitwert = nArbeitszeitwert;
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

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public BigDecimal getNMaterialwertdetailliert() {
		return nMaterialwertdetailliert;
	}

	public void setNMaterialwertdetailliert(BigDecimal nMaterialwertdetailliert) {
		this.nMaterialwertdetailliert = nMaterialwertdetailliert;
	}

	public BigDecimal getNArbeitszeitwertdetailliert() {
		return nArbeitszeitwertdetailliert;
	}

	public void setNArbeitszeitwertdetailliert(
			BigDecimal nArbeitszeitwertdetailliert) {
		this.nArbeitszeitwertdetailliert = nArbeitszeitwertdetailliert;
	}

	public Short getBGestehungspreisNeuBerechnen() {
		return bGestehungspreisNeuBerechnen;
	}

	public void setBGestehungspreisNeuBerechnen(
			Short bGestehungspreisNeuBerechnen) {
		this.bGestehungspreisNeuBerechnen = bGestehungspreisNeuBerechnen;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof LosablieferungDto))
			return false;
		LosablieferungDto that = (LosablieferungDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.losIId == null ? this.losIId == null : that.losIId
				.equals(this.losIId)))
			return false;
		if (!(that.nMenge == null ? this.nMenge == null : that.nMenge
				.equals(this.nMenge)))
			return false;
		if (!(that.nGestehungspreis == null ? this.nGestehungspreis == null
				: that.nGestehungspreis.equals(this.nGestehungspreis)))
			return false;
		if (!(that.nMaterialwert == null ? this.nMaterialwert == null
				: that.nMaterialwert.equals(this.nMaterialwert)))
			return false;
		if (!(that.nArbeitszeitwert == null ? this.nArbeitszeitwert == null
				: that.nArbeitszeitwert.equals(this.nArbeitszeitwert)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern)))
			return false;
		if (!(that.nMaterialwertdetailliert == null ? this.nMaterialwertdetailliert == null
				: that.nMaterialwertdetailliert
						.equals(this.nMaterialwertdetailliert)))
			return false;
		if (!(that.nArbeitszeitwertdetailliert == null ? this.nArbeitszeitwertdetailliert == null
				: that.nArbeitszeitwertdetailliert
						.equals(this.nArbeitszeitwertdetailliert)))
			return false;
		if (!(that.bGestehungspreisNeuBerechnen == null ? this.bGestehungspreisNeuBerechnen == null
				: that.bGestehungspreisNeuBerechnen
						.equals(this.bGestehungspreisNeuBerechnen)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.losIId.hashCode();
		result = 37 * result + this.nMenge.hashCode();
		result = 37 * result + this.nGestehungspreis.hashCode();
		result = 37 * result + this.nMaterialwert.hashCode();
		result = 37 * result + this.nArbeitszeitwert.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.nMaterialwertdetailliert.hashCode();
		result = 37 * result + this.nArbeitszeitwertdetailliert.hashCode();
		result = 37 * result + this.bGestehungspreisNeuBerechnen.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + losIId;
		returnString += ", " + nMenge;
		returnString += ", " + nGestehungspreis;
		returnString += ", " + nMaterialwert;
		returnString += ", " + nArbeitszeitwert;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + tAendern;
		returnString += ", " + nMaterialwertdetailliert;
		returnString += ", " + nArbeitszeitwertdetailliert;
		returnString += ", " + bGestehungspreisNeuBerechnen;
		return returnString;
	}
}

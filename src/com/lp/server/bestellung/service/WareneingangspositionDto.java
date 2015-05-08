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
package com.lp.server.bestellung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;

public class WareneingangspositionDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer wareneingangIId;
	private BigDecimal nGeliefertemenge;
	private BigDecimal nGelieferterpreis;
	private BigDecimal nRabattwert;
	private BigDecimal nAnteiligetransportkosten;
	private BigDecimal nEinstandspreis;
	private Integer bestellpositionIId;
	private Timestamp tAnlegen;
	private Timestamp tAendern;
	private Integer personalIIdAnlegen;
	private Integer personalIIdAendern;
	private String xInternerKommentar;
	private Timestamp tManuellErledigt;
	private BigDecimal nAnteiligefixkosten;
	private Boolean bPreiseErfasst;
	private Integer herstellerIId;
	private Integer landIId;
	private List<SeriennrChargennrMitMengeDto> alSeriennrChargennrMitMenge = null;
	
	private Integer artikelIIdFuerNeuAnlageAusWEP=null;

	public Integer getArtikelIIdFuerNeuAnlageAusWEP() {
		return artikelIIdFuerNeuAnlageAusWEP;
	}

	public void setArtikelIIdFuerNeuAnlageAusWEP(
			Integer artikelIIdFuerNeuAnlageAusWEP) {
		this.artikelIIdFuerNeuAnlageAusWEP = artikelIIdFuerNeuAnlageAusWEP;
	}

	public List<SeriennrChargennrMitMengeDto> getSeriennrChargennrMitMenge() {
		return alSeriennrChargennrMitMenge;
	}

	public void setSeriennrChargennrMitMenge(
			List<SeriennrChargennrMitMengeDto> alSeriennrChargennrMitMenge) {
		this.alSeriennrChargennrMitMenge = alSeriennrChargennrMitMenge;
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

	// nicht persistent.
	private BigDecimal nFixkosten4Bestellposition;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getXInternerKommentar() {
		return xInternerKommentar;
	}

	public void setXInternerKommentar(String xInternerKommentar) {
		this.xInternerKommentar = xInternerKommentar;
	}

	private Short bVerraeumt;

	
	public Short getBVerraeumt() {
		return bVerraeumt;
	}

	public void setBVerraeumt(Short bVerraeumt) {
		this.bVerraeumt = bVerraeumt;
	}
	
	public Integer getWareneingangIId() {
		return wareneingangIId;
	}

	public void setWareneingangIId(Integer wareneingangIId) {
		this.wareneingangIId = wareneingangIId;
	}

	public BigDecimal getNGeliefertemenge() {
		return nGeliefertemenge;
	}

	public void setNGeliefertemenge(BigDecimal nGeliefertemenge) {
		this.nGeliefertemenge = nGeliefertemenge;
	}

	public BigDecimal getNGelieferterpreis() {
		return nGelieferterpreis;
	}

	public void setNGelieferterpreis(BigDecimal nGelieferterpreis) {
		this.nGelieferterpreis = nGelieferterpreis;
	}

	public BigDecimal getNRabattwert() {
		return nRabattwert;
	}

	public void setNRabattwert(BigDecimal nRabattwert) {
		this.nRabattwert = nRabattwert;
	}

	public BigDecimal getNAnteiligetransportkosten() {
		return nAnteiligetransportkosten;
	}

	public void setNAnteiligetransportkosten(
			BigDecimal nAnteiligetransportkosten) {
		this.nAnteiligetransportkosten = nAnteiligetransportkosten;
	}

	public BigDecimal getNEinstandspreis() {
		return nEinstandspreis;
	}

	public void setNEinstandspreis(BigDecimal nEinstandspreis) {
		this.nEinstandspreis = nEinstandspreis;
	}

	public Integer getBestellpositionIId() {
		return bestellpositionIId;
	}

	public void setBestellpositionIId(Integer bestellpositionIId) {
		this.bestellpositionIId = bestellpositionIId;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public Timestamp getTManuellErledigt() {
		return tManuellErledigt;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public void setTManuellErledigt(Timestamp tManuellErledigt) {
		this.tManuellErledigt = tManuellErledigt;
	}

	public BigDecimal getNAnteiligefixkosten() {
		return nAnteiligefixkosten;
	}

	public void setNAnteiligefixkosten(BigDecimal nAnteiligefixkosten) {
		this.nAnteiligefixkosten = nAnteiligefixkosten;
	}

	public BigDecimal getNFixkosten4Bestellposition() {
		return nFixkosten4Bestellposition;
	}

	public void setNFixkosten4Bestellposition(
			BigDecimal nFixkosten4Bestellposition) {
		this.nFixkosten4Bestellposition = nFixkosten4Bestellposition;
	}

	public Boolean getBPreiseErfasst() {
		return bPreiseErfasst;
	}

	public void setBPreiseErfasst(Boolean bPreiseErfasst) {
		this.bPreiseErfasst = bPreiseErfasst;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof WareneingangspositionDto)) {
			return false;
		}
		WareneingangspositionDto that = (WareneingangspositionDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.wareneingangIId == null ? this.wareneingangIId == null
				: that.wareneingangIId.equals(this.wareneingangIId))) {
			return false;
		}
		if (!(that.nGeliefertemenge == null ? this.nGeliefertemenge == null
				: that.nGeliefertemenge.equals(this.nGeliefertemenge))) {
			return false;
		}
		if (!(that.nGelieferterpreis == null ? this.nGelieferterpreis == null
				: that.nGelieferterpreis.equals(this.nGelieferterpreis))) {
			return false;
		}
		if (!(that.nRabattwert == null ? this.nRabattwert == null
				: that.nRabattwert.equals(this.nRabattwert))) {
			return false;
		}
		if (!(that.nAnteiligetransportkosten == null ? this.nAnteiligetransportkosten == null
				: that.nAnteiligetransportkosten
						.equals(this.nAnteiligetransportkosten))) {
			return false;
		}
		if (!(that.nEinstandspreis == null ? this.nEinstandspreis == null
				: that.nEinstandspreis.equals(this.nEinstandspreis))) {
			return false;
		}
		if (!(that.bestellpositionIId == null ? this.bestellpositionIId == null
				: that.bestellpositionIId.equals(this.bestellpositionIId))) {
			return false;
		}
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen
				.equals(this.tAnlegen))) {
			return false;
		}
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern))) {
			return false;
		}
		if (!(that.personalIIdAnlegen == null ? this.personalIIdAnlegen == null
				: that.personalIIdAnlegen.equals(this.personalIIdAnlegen))) {
			return false;
		}
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern))) {
			return false;
		}
		if (!(that.xInternerKommentar == null ? this.xInternerKommentar == null
				: that.xInternerKommentar.equals(this.xInternerKommentar))) {
			return false;
		}
		if (!(that.tManuellErledigt == null ? this.tManuellErledigt == null
				: that.tManuellErledigt.equals(this.tManuellErledigt))) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.wareneingangIId.hashCode();
		result = 37 * result + this.nGeliefertemenge.hashCode();
		result = 37 * result + this.nGelieferterpreis.hashCode();
		result = 37 * result + this.nRabattwert.hashCode();
		result = 37 * result + this.nAnteiligetransportkosten.hashCode();
		result = 37 * result + this.nEinstandspreis.hashCode();
		result = 37 * result + this.bestellpositionIId.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.xInternerKommentar.hashCode();
		result = 37 * result + this.tManuellErledigt.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + wareneingangIId;
		returnString += ", " + nGeliefertemenge;
		returnString += ", " + nGelieferterpreis;
		returnString += ", " + nRabattwert;
		returnString += ", " + nAnteiligetransportkosten;
		returnString += ", " + nEinstandspreis;
		returnString += ", " + bestellpositionIId;
		returnString += ", " + tAnlegen;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdAnlegen;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + xInternerKommentar;
		returnString += ", " + tManuellErledigt;
		return returnString;
	}
}

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
package com.lp.server.anfrage.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;

import com.lp.util.Helper;

public class AnfragepositionlieferdatenDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer anfragepositionIId;
	private Integer iAnlieferzeit;
	private BigDecimal nAnliefermenge;
	private BigDecimal nNettogesamtpreis;
	private BigDecimal nNettogesamtpreisminusrabatt;
	private Short bErfasst;
	private String cBezbeilieferant;
	private String cArtikelnrlieferant;
	private BigDecimal nVerpackungseinheit;
	private BigDecimal nStandardmenge;
	private BigDecimal nMindestbestellmenge;
	
	private Timestamp tPreisgueltigab;

	
	public Timestamp getTPreisgueltigab() {
		return tPreisgueltigab;
	}

	public void setTPreisgueltigab(Timestamp tPreisgueltigab) {
		this.tPreisgueltigab = tPreisgueltigab;
	}

	
	private Integer zertifikatartIId;
	
	public Integer getZertifikatartIId() {
		return zertifikatartIId;
	}

	public void setZertifikatartIId(Integer zertifikatartIId) {
		this.zertifikatartIId = zertifikatartIId;
	}
	
	public String getCBezbeilieferant() {
		return cBezbeilieferant;
	}

	public void setCBezbeilieferant(String bezbeilieferant) {
		cBezbeilieferant = bezbeilieferant;
	}

	public String getCArtikelnrlieferant() {
		return cArtikelnrlieferant;
	}

	public void setCArtikelnrlieferant(String artikelnrlieferant) {
		cArtikelnrlieferant = artikelnrlieferant;
	}

	public BigDecimal getNVerpackungseinheit() {
		return nVerpackungseinheit;
	}

	public void setNVerpackungseinheit(BigDecimal verpackungseinheit) {
		nVerpackungseinheit = verpackungseinheit;
	}

	public BigDecimal getNStandardmenge() {
		return nStandardmenge;
	}

	public void setNStandardmenge(BigDecimal standardmenge) {
		nStandardmenge = standardmenge;
	}

	public BigDecimal getNMindestbestellmenge() {
		return nMindestbestellmenge;
	}

	public void setNMindestbestellmenge(BigDecimal mindestbestellmenge) {
		nMindestbestellmenge = mindestbestellmenge;
	}

	public void resetContent() {
		iAnlieferzeit = new Integer(0);
		nAnliefermenge = Helper.getBigDecimalNull();
		nNettogesamtpreis = Helper.getBigDecimalNull();
		nNettogesamtpreisminusrabatt = Helper.getBigDecimalNull();
		nMindestbestellmenge=Helper.getBigDecimalNull();
		nStandardmenge=Helper.getBigDecimalNull();
		nVerpackungseinheit=Helper.getBigDecimalNull();
		cBezbeilieferant=null;
		cArtikelnrlieferant=null;
		bErfasst = new Short((short) 0);
		tPreisgueltigab=null;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Short getBErfasst() {
		return bErfasst;
	}

	public void setBErfasst(Short bErfasstI) {
		this.bErfasst = bErfasstI;
	}

	public Integer getAnfragepositionIId() {
		return anfragepositionIId;
	}

	public void setAnfragepositionIId(Integer anfragepositionIId) {
		this.anfragepositionIId = anfragepositionIId;
	}

	public Integer getIAnlieferzeit() {
		return iAnlieferzeit;
	}

	public void setIAnlieferzeit(Integer iAnlieferzeit) {
		this.iAnlieferzeit = iAnlieferzeit;
	}

	public BigDecimal getNAnliefermenge() {
		return nAnliefermenge;
	}

	public void setNAnliefermenge(BigDecimal nAnliefermenge) {
		this.nAnliefermenge = nAnliefermenge;
	}

	public BigDecimal getNNettogesamtpreis() {
		return nNettogesamtpreis;
	}

	public void setNNettogesamtpreis(BigDecimal nNettogesamtpreis) {
		this.nNettogesamtpreis = nNettogesamtpreis;
	}

	public BigDecimal getNNettogesamtpreisminusrabatt() {
		return nNettogesamtpreisminusrabatt;
	}

	public void setNNettogesamtpreisminusrabatt(
			BigDecimal nNettogesamtpreisminusrabatt) {
		this.nNettogesamtpreisminusrabatt = nNettogesamtpreisminusrabatt;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AnfragepositionlieferdatenDto)) {
			return false;
		}
		AnfragepositionlieferdatenDto that = (AnfragepositionlieferdatenDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.anfragepositionIId == null ? this.anfragepositionIId == null
				: that.anfragepositionIId.equals(this.anfragepositionIId))) {
			return false;
		}
		if (!(that.iAnlieferzeit == null ? this.iAnlieferzeit == null
				: that.iAnlieferzeit
						.equals(this.iAnlieferzeit))) {
			return false;
		}
		if (!(that.nAnliefermenge == null ? this.nAnliefermenge == null
				: that.nAnliefermenge.equals(this.nAnliefermenge))) {
			return false;
		}
		if (!(that.nNettogesamtpreis == null ? this.nNettogesamtpreis == null
				: that.nNettogesamtpreis.equals(this.nNettogesamtpreis))) {
			return false;
		}
		if (!(that.nNettogesamtpreisminusrabatt == null ? this.nNettogesamtpreisminusrabatt == null
				: that.nNettogesamtpreisminusrabatt
						.equals(this.nNettogesamtpreisminusrabatt))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.anfragepositionIId.hashCode();
		result = 37 * result + this.iAnlieferzeit.hashCode();
		result = 37 * result + this.nAnliefermenge.hashCode();
		result = 37 * result + this.nNettogesamtpreis.hashCode();
		result = 37 * result + this.nNettogesamtpreisminusrabatt.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + anfragepositionIId;
		returnString += ", " + iAnlieferzeit;
		returnString += ", " + nAnliefermenge;
		returnString += ", " + nNettogesamtpreis;
		returnString += ", " + nNettogesamtpreisminusrabatt;
		return returnString;
	}
}

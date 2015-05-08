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
import com.lp.util.Helper;

public class EinkaufsangebotpositionDto extends BelegpositionDto implements
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal nPreis1;
	private BigDecimal nPreis2;
	private BigDecimal nPreis3;
	private BigDecimal nPreis4;
	private BigDecimal nPreis5;
	private String cBemerkung;
	private Integer iWiederbeschaffungszeit;
	private Integer iVerpackungseinheit;
	private Double fMindestbestellmenge;
	private String cPosition;
	private String cInternebemerkung;

	private String cKommentar1;
	public String getCKommentar1() {
		return cKommentar1;
	}

	public void setCKommentar1(String kommentar1) {
		cKommentar1 = kommentar1;
	}

	public String getCKommentar2() {
		return cKommentar2;
	}

	public void setCKommentar2(String kommentar2) {
		cKommentar2 = kommentar2;
	}

	private String cKommentar2;
	
	private Short bMitdrucken;
	
	public Short getBMitdrucken() {
		return bMitdrucken;
	}

	public void setBMitdrucken(Short mitdrucken) {
		bMitdrucken = mitdrucken;
	}

	public String getCInternebemerkung() {
		return cInternebemerkung;
	}

	public void setCInternebemerkung(String internebemerkung) {
		cInternebemerkung = internebemerkung;
	}

	public String getCPosition() {
		return cPosition;
	}

	public void setCPosition(String position) {
		cPosition = position;
	}

	public BigDecimal getNPreis1() {
		return nPreis1;
	}

	public void setNPreis1(BigDecimal nPreis1) {
		this.nPreis1 = Helper.rundeKaufmaennisch(nPreis1, 4);
	}

	public BigDecimal getNPreis2() {
		return nPreis2;
	}

	public void setNPreis2(BigDecimal nPreis2) {
		this.nPreis2 = Helper.rundeKaufmaennisch(nPreis2, 4);
	}

	public BigDecimal getNPreis3() {
		return nPreis3;
	}

	public void setNPreis3(BigDecimal nPreis3) {
		this.nPreis3 = Helper.rundeKaufmaennisch(nPreis3, 4);
	}

	public BigDecimal getNPreis4() {
		return nPreis4;
	}

	public void setNPreis4(BigDecimal nPreis4) {
		this.nPreis4 = Helper.rundeKaufmaennisch(nPreis4, 4);
	}

	public BigDecimal getNPreis5() {
		return nPreis5;
	}

	public String getCBemerkung() {
		return cBemerkung;
	}

	public Integer getIWiederbeschaffungszeit() {
		return iWiederbeschaffungszeit;
	}

	public Integer getIVerpackungseinheit() {
		return iVerpackungseinheit;
	}

	public Double getFMindestbestellmenge() {
		return fMindestbestellmenge;
	}

	public void setNPreis5(BigDecimal nPreis5) {
		this.nPreis5 = Helper.rundeKaufmaennisch(nPreis5, 4);
	}

	public void setCBemerkung(String cBemerkung) {
		this.cBemerkung = cBemerkung;
	}

	public void setIWiederbeschaffungszeit(Integer iWiederbeschaffungszeit) {
		this.iWiederbeschaffungszeit = iWiederbeschaffungszeit;
	}

	public void setIVerpackungseinheit(Integer iVerpackungseinheit) {
		this.iVerpackungseinheit = iVerpackungseinheit;
	}

	public void setFMindestbestellmenge(Double fMindestbestellmenge) {
		this.fMindestbestellmenge = fMindestbestellmenge;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof EinkaufsangebotpositionDto))
			return false;
		EinkaufsangebotpositionDto that = (EinkaufsangebotpositionDto) obj;
		if (!(that.nPreis1 == null ? this.nPreis1 == null : that.nPreis1
				.equals(this.nPreis1)))
			return false;
		if (!(that.nPreis2 == null ? this.nPreis2 == null : that.nPreis2
				.equals(this.nPreis2)))
			return false;
		if (!(that.nPreis3 == null ? this.nPreis3 == null : that.nPreis3
				.equals(this.nPreis3)))
			return false;
		if (!(that.nPreis4 == null ? this.nPreis4 == null : that.nPreis4
				.equals(this.nPreis4)))
			return false;
		if (!(that.nPreis5 == null ? this.nPreis5 == null : that.nPreis5
				.equals(this.nPreis5)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.nPreis1.hashCode();
		result = 37 * result + this.nPreis2.hashCode();
		result = 37 * result + this.nPreis3.hashCode();
		result = 37 * result + this.nPreis4.hashCode();
		result = 37 * result + this.nPreis5.hashCode();
		return result;
	}

}

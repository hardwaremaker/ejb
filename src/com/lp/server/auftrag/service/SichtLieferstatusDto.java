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
package com.lp.server.auftrag.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class SichtLieferstatusDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iiPosition;
	private String sBelegart;
	private String sBelegnummer;
	private BigDecimal nMengeGesamt;
	private BigDecimal nMengeGeliefert;
	private String sBezeichnung;
	private String sIdent;
	private BigDecimal nMengeOffen;
	public BigDecimal getNMengeOffen() {
		return nMengeOffen;
	}

	public void setNMengeOffen(BigDecimal nMengeOffen) {
		this.nMengeOffen = nMengeOffen;
	}

	private boolean bErledigt;

	public Integer getIiPosition() {
		return iiPosition;
	}

	public void setIiPosition(Integer iiPosition) {
		this.iiPosition = iiPosition;
	}

	public String getSBelegart() {
		return sBelegart;
	}

	public void setSBelegart(String sBelegart) {
		this.sBelegart = sBelegart;
	}

	public String getSBelegnummer() {
		return sBelegnummer;
	}

	public void setSBelegnummer(String sBelegnummer) {
		this.sBelegnummer = sBelegnummer;
	}

	public BigDecimal getNMengeGesamt() {
		return nMengeGesamt;
	}

	public void setNMengeGesamt(BigDecimal nMengeGesamt) {
		this.nMengeGesamt = nMengeGesamt;
	}

	public BigDecimal getNMengeGeliefert() {
		return nMengeGeliefert;
	}

	public void setNMengeGeliefert(BigDecimal nMengeGeliefert) {
		this.nMengeGeliefert = nMengeGeliefert;
	}

	public String getSBezeichnung() {
		return sBezeichnung;
	}

	public void setSBezeichnung(String sBezeichnung) {
		this.sBezeichnung = sBezeichnung;
	}

	public boolean getBErledigt() {
		return bErledigt;
	}

	public String getSIdent() {
		return sIdent;
	}

	public void setBErledigt(boolean bErledigt) {
		this.bErledigt = bErledigt;
	}

	public void setSIdent(String sIdent) {
		this.sIdent = sIdent;
	}
}

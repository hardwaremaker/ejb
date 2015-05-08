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
package com.lp.server.personal.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

public class UrlaubsabrechnungDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private java.math.BigDecimal nAktuellerUrlaubsanspruch;
	private BigDecimal nAktuellerUrlaubsanspruchTage;
	private java.math.BigDecimal nAktuellerUrlaubVerbraucht;
	private java.math.BigDecimal nAlterUrlaubsanspruch;
	private BigDecimal nAlterUrlaubsanspruchTage;
	private BigDecimal nAktuellerUrlaubVerbrauchtTage;
	private java.math.BigDecimal nVerfuegbarerUrlaub;
	private java.math.BigDecimal nGeplanterUrlaub;
	private BigDecimal nAliquoterAnspruchTage;
	private BigDecimal nVerfuegbarerUrlaubTage;
	private BigDecimal nAliquoterAnspruchStunden;
	private BigDecimal nGeplanterUrlaubTage;
	private Date dAbrechnungszeitpunkt;

	public java.math.BigDecimal getNAktuellerUrlaubsanspruchStunden() {
		return nAktuellerUrlaubsanspruch;
	}

	public java.math.BigDecimal getNGeplanterUrlaubStunden() {
		return nGeplanterUrlaub;
	}

	public java.math.BigDecimal getNAktuellerUrlaubVerbrauchtStunden() {
		return nAktuellerUrlaubVerbraucht;
	}

	public java.math.BigDecimal getNAlterUrlaubsanspruchStunden() {
		return nAlterUrlaubsanspruch;
	}

	public java.math.BigDecimal getNVerfuegbarerUrlaubStunden() {
		return nVerfuegbarerUrlaub;
	}

	public Date getDAbrechnungszeitpunkt() {
		return dAbrechnungszeitpunkt;
	}

	public BigDecimal getNAktuellerUrlaubsanspruchTage() {
		return nAktuellerUrlaubsanspruchTage;
	}

	public BigDecimal getNAktuellerUrlaubVerbrauchtTage() {
		return nAktuellerUrlaubVerbrauchtTage;
	}

	public BigDecimal getNAlterUrlaubsanspruchTage() {
		return nAlterUrlaubsanspruchTage;
	}

	public BigDecimal getNGeplanterUrlaubTage() {
		return nGeplanterUrlaubTage;
	}

	public BigDecimal getNVerfuegbarerUrlaubTage() {
		return nVerfuegbarerUrlaubTage;
	}

	public BigDecimal getNAliquoterAnspruchTage() {
		return nAliquoterAnspruchTage;
	}

	public BigDecimal getNAliquoterAnspruchStunden() {
		return nAliquoterAnspruchStunden;
	}

	public void setNAktuellerUrlaubsanspruchStunden(
			java.math.BigDecimal nAktuellerUrlaubsanspruch) {
		this.nAktuellerUrlaubsanspruch = com.lp.util.Helper.rundeKaufmaennisch(
				nAktuellerUrlaubsanspruch, 2);
	}

	public void setNGeplanterUrlaubStunden(java.math.BigDecimal nGeplanterUrlaub) {
		this.nGeplanterUrlaub = com.lp.util.Helper.rundeKaufmaennisch(
				nGeplanterUrlaub, 2);
	}

	public void setNAktuellerUrlaubVerbrauchtStunden(
			java.math.BigDecimal nAktuellerUrlaubVerbraucht) {
		this.nAktuellerUrlaubVerbraucht = com.lp.util.Helper
				.rundeKaufmaennisch(nAktuellerUrlaubVerbraucht, 2);
	}

	public void setNAlterUrlaubsanspruchStunden(
			java.math.BigDecimal nAlterUrlaubsanspruch) {
		this.nAlterUrlaubsanspruch = com.lp.util.Helper.rundeKaufmaennisch(
				nAlterUrlaubsanspruch, 2);
	}

	public void setNVerfuegbarerUrlaubStunden(
			java.math.BigDecimal nVerfuegbarerUrlaub) {
		this.nVerfuegbarerUrlaub = com.lp.util.Helper.rundeKaufmaennisch(
				nVerfuegbarerUrlaub, 2);
	}

	public void setDAbrechnungszeitpunkt(Date dAbrechnungszeitpunkt) {
		this.dAbrechnungszeitpunkt = dAbrechnungszeitpunkt;
	}

	public void setNAktuellerUrlaubsanspruchTage(
			BigDecimal nAktuellerUrlaubsanspruchTage) {
		this.nAktuellerUrlaubsanspruchTage = com.lp.util.Helper
				.rundeKaufmaennisch(nAktuellerUrlaubsanspruchTage, 2);
	}

	public void setNAktuellerUrlaubVerbrauchtTage(
			BigDecimal nAktuellerUrlaubVerbrauchtTage) {
		this.nAktuellerUrlaubVerbrauchtTage = com.lp.util.Helper
				.rundeKaufmaennisch(nAktuellerUrlaubVerbrauchtTage, 2);
	}

	public void setNAlterUrlaubsanspruchTage(
			BigDecimal nAlterUrlaubsanspruchTage) {
		this.nAlterUrlaubsanspruchTage = com.lp.util.Helper.rundeKaufmaennisch(
				nAlterUrlaubsanspruchTage, 2);
	}

	public void setNGeplanterUrlaubTage(BigDecimal nGeplanterUrlaubTage) {
		this.nGeplanterUrlaubTage = com.lp.util.Helper.rundeKaufmaennisch(
				nGeplanterUrlaubTage, 2);
	}

	public void setNVerfuegbarerUrlaubTage(BigDecimal nVerfuegbarerUrlaubTage) {
		this.nVerfuegbarerUrlaubTage = com.lp.util.Helper.rundeKaufmaennisch(
				nVerfuegbarerUrlaubTage, 2);
	}

	public void setNAliquoterAnspruchTage(BigDecimal nAliquoterAnspruchTage) {
		this.nAliquoterAnspruchTage = nAliquoterAnspruchTage;
	}

	public void setNAliquoterAnspruchStunden(
			BigDecimal nAliquoterAnspruchStunden) {
		this.nAliquoterAnspruchStunden = nAliquoterAnspruchStunden;
	}

}

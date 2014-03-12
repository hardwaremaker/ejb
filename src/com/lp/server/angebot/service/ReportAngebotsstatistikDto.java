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
package com.lp.server.angebot.service;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * Diese Klasse kuemmert sich um ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur GmbH (c) 2004, 2005, 2006
 * </p>
 * 
 * <p>
 * Erstellung: Vorname Nachname; dd.mm.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: valentin $
 *         </p>
 * 
 * @version unbekannt Date $Date: 2008/08/05 10:16:42 $
 */
public class ReportAngebotsstatistikDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iIndex;
	private String angebotCNr;
	private String kundenname;
	private String belegdatumCNr;
	private BigDecimal nAngebotenemenge;
	private BigDecimal nAngebotenerpreis;
	private BigDecimal nMaterialzuschlag;
	public BigDecimal getNMaterialzuschlag() {
		return nMaterialzuschlag;
	}

	public void setNMaterialzuschlag(BigDecimal nMaterialzuschlag) {
		this.nMaterialzuschlag = nMaterialzuschlag;
	}

	private Integer iLieferzeit;

	public Integer getIIndex() {
		return iIndex;
	}

	public void setIIndex(Integer iIndexI) {
		this.iIndex = iIndexI;
	}

	public String getAngebotCNr() {
		return angebotCNr;
	}

	public void setAngebotCNr(String angebotCNr) {
		this.angebotCNr = angebotCNr;
	}

	public String getKundenname() {
		return kundenname;
	}

	public void setKundenname(String kundennameI) {
		this.kundenname = kundennameI;
	}

	public String getBelegdatumCNr() {
		return belegdatumCNr;
	}

	public void setBelegdatumCNr(String belegdatumCNrI) {
		this.belegdatumCNr = belegdatumCNrI;
	}

	public BigDecimal getNAngebotenemenge() {
		return this.nAngebotenemenge;
	}

	public void setNAngebotenemenge(BigDecimal nAngebotenemengeI) {
		this.nAngebotenemenge = nAngebotenemengeI;
	}

	public BigDecimal getNAngebotenerpreis() {
		return this.nAngebotenerpreis;
	}

	public void setNAngebotenerpreis(BigDecimal nAngebotenerpreisI) {
		this.nAngebotenerpreis = nAngebotenerpreisI;
	}

	public Integer getILieferzeit() {
		return iLieferzeit;
	}

	public void setILieferzeit(Integer iLieferzeitI) {
		this.iLieferzeit = iLieferzeitI;
	}

}

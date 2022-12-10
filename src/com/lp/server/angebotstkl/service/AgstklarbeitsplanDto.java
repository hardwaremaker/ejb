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

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.service.BelegpositionDto;

public class AgstklarbeitsplanDto extends BelegpositionDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer agstklIId;

	public Integer getAgstklIId() {
		return agstklIId;
	}

	public void setAgstklIId(Integer agstklIId) {
		this.agstklIId = agstklIId;
	}

	private Integer iArbeitsgang;
	private Long lStueckzeit;
	private Long lRuestzeit;
	private String cKommentar;
	private String xLangtext;
	private String agartCNr;
	private Integer iUnterarbeitsgang;

	private BigDecimal nStundensatzMann;

	public BigDecimal getNStundensatzMann() {
		return nStundensatzMann;
	}

	public void setNStundensatzMann(BigDecimal nStundensatzMann) {
		this.nStundensatzMann = nStundensatzMann;
	}

	private BigDecimal nStundensatzMaschine;
	public BigDecimal getNStundensatzMaschine() {
		return nStundensatzMaschine;
	}

	public void setNStundensatzMaschine(BigDecimal nStundensatzMaschine) {
		this.nStundensatzMaschine = nStundensatzMaschine;
	}

	private Short bNurmaschinenzeit;

	public Short getBNurmaschinenzeit() {
		return bNurmaschinenzeit;
	}

	public void setBNurmaschinenzeit(Short nurmaschinenzeit) {
		bNurmaschinenzeit = nurmaschinenzeit;
	}

	public Integer getIUnterarbeitsgang() {
		return iUnterarbeitsgang;
	}

	public void setIUnterarbeitsgang(Integer unterarbeitsgang) {
		iUnterarbeitsgang = unterarbeitsgang;
	}

	public BigDecimal nMengeWennHilfsstueckliste;

	public String getAgartCNr() {
		return agartCNr;
	}

	public void setAgartCNr(String agartCNr) {
		this.agartCNr = agartCNr;
	}

	public Integer getIAufspannung() {
		return iAufspannung;
	}

	public void setIAufspannung(Integer aufspannung) {
		iAufspannung = aufspannung;
	}

	private Integer iAufspannung;

	private Integer maschineIId;

	public Integer getIArbeitsgang() {
		return iArbeitsgang;
	}

	public void setIArbeitsgang(Integer iArbeitsgang) {
		this.iArbeitsgang = iArbeitsgang;
	}

	public Long getLStueckzeit() {
		return lStueckzeit;
	}

	public void setLStueckzeit(Long lStueckzeit) {
		this.lStueckzeit = lStueckzeit;
	}

	public Long getLRuestzeit() {
		return lRuestzeit;
	}

	public void setLRuestzeit(Long lRuestzeit) {
		this.lRuestzeit = lRuestzeit;
	}

	public String getCKommentar() {
		return cKommentar;
	}

	public void setCKommentar(String cKommentar) {
		this.cKommentar = cKommentar;
	}

	public String getXLangtext() {
		return xLangtext;
	}

	public Integer getMaschineIId() {
		return maschineIId;
	}

	public void setXLangtext(String xLangtext) {
		this.xLangtext = xLangtext;
	}

	public void setMaschineIId(Integer maschineIId) {
		this.maschineIId = maschineIId;
	}

}

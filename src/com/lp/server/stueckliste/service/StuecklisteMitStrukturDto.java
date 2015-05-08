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
package com.lp.server.stueckliste.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class StuecklisteMitStrukturDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private StuecklistepositionDto stuecklistepositionDto;
	private int iEbene;
	private boolean bStueckliste = false;
	private boolean bIstFremdfertigung = false; // nicht auf DB; fuer AG

	private boolean bMaschinenzeit = false;
	private boolean bArbeitszeit = false;
	private BigDecimal durchlaufzeit = null;
	private StuecklisteDto stuecklisteDto = null;
	private java.util.Date tFreigabe = null;
	private String cKurzzeichenPersonFreigabe = null;

	private Integer iAnzahlArbeitsschritte=null;
	
	public Integer getiAnzahlArbeitsschritte() {
		return iAnzahlArbeitsschritte;
	}

	public void setIAnzahlArbeitsschritte(Integer iAnzahlArbeitsschritte) {
		this.iAnzahlArbeitsschritte = iAnzahlArbeitsschritte;
	}

	public String getCKurzzeichenPersonFreigabe() {
		return cKurzzeichenPersonFreigabe;
	}

	public void setCKurzzeichenPersonFreigabe(String cKurzzeichenPersonFreigabe) {
		this.cKurzzeichenPersonFreigabe = cKurzzeichenPersonFreigabe;
	}

	public java.util.Date getTFreigabe() {
		return tFreigabe;
	}

	public void setTFreigabe(java.util.Date tFreigabe) {
		this.tFreigabe = tFreigabe;
	}

	public StuecklisteDto getStuecklisteDto() {
		return stuecklisteDto;
	}

	public void setStuecklisteDto(StuecklisteDto stuecklisteDto) {
		this.stuecklisteDto = stuecklisteDto;
	}

	public StuecklisteMitStrukturDto(int iEbene,
			StuecklistepositionDto stuecklistepositionDto) {
		this.iEbene = iEbene;
		this.stuecklistepositionDto = stuecklistepositionDto;
	}

	private StuecklistearbeitsplanDto stuecklistearbeitsplanDto;

	public StuecklistepositionDto getStuecklistepositionDto() {
		return stuecklistepositionDto;
	}

	public int getIEbene() {
		return iEbene;
	}

	public boolean isBStueckliste() {
		return bStueckliste;
	}

	public boolean isBArbeitszeit() {
		return bArbeitszeit;
	}

	public StuecklistearbeitsplanDto getStuecklistearbeitsplanDto() {
		return stuecklistearbeitsplanDto;
	}

	public boolean isBMaschinenzeit() {
		return bMaschinenzeit;
	}

	public BigDecimal getDurchlaufzeit() {
		return durchlaufzeit;
	}

	public boolean isBFremdfertigung() {
		return this.bIstFremdfertigung;
	}

	public void setStuecklistepositionDto(
			StuecklistepositionDto stuecklistepositionDto) {
		this.stuecklistepositionDto = stuecklistepositionDto;
	}

	public void setIEbene(int iEbene) {
		this.iEbene = iEbene;
	}

	public void setBStueckliste(boolean bStueckliste) {
		this.bStueckliste = bStueckliste;
	}

	public void setBIstFremdfertigung(boolean bFremdIstfertiungI) {
		this.bIstFremdfertigung = bFremdIstfertiungI;
	}

	public void setBArbeitszeit(boolean bArbeitszeit) {
		this.bArbeitszeit = bArbeitszeit;
	}

	public void setStuecklistearbeitsplanDto(
			StuecklistearbeitsplanDto stuecklistearbeitsplanDto) {
		this.stuecklistearbeitsplanDto = stuecklistearbeitsplanDto;
	}

	public void setBMaschinenzeit(boolean bMaschinenzeit) {
		this.bMaschinenzeit = bMaschinenzeit;
	}

	public void setDurchlaufzeit(BigDecimal durchlaufzeit) {
		this.durchlaufzeit = durchlaufzeit;
	}
}

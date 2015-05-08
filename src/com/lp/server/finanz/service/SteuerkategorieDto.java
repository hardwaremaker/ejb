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
package com.lp.server.finanz.service;

import java.io.Serializable;

public class SteuerkategorieDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer iId;
	private String cNr;
	private String mandantCNr;
	private Short bReversecharge;
	private String cBez;
	private Integer kontoIIdForderungen;
	private Integer kontoIIdVerbindlichkeiten;
	private Integer finanzamtIId;
	private Integer kontoIIdKursverlust;
	private Integer kontoIIdKursgewinn;
	
	public Integer getIId() {
		return iId;
	}
	public void setIId(Integer iId) {
		this.iId = iId;
	}
	public String getCNr() {
		return cNr;
	}
	public void setCNr(String cNr) {
		this.cNr = cNr;
	}
	public String getMandantCNr() {
		return mandantCNr;
	}
	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}
	public Short getBReversecharge() {
		return bReversecharge;
	}
	public void setBReversecharge(Short bReversecharge) {
		this.bReversecharge = bReversecharge;
	}
	public String getCBez() {
		return cBez;
	}
	public void setCBez(String cBez) {
		this.cBez = cBez;
	}
	public Integer getISort() {
		return iSort;
	}
	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}
	public void setKontoIIdForderungen(Integer kontoIIdForderungen) {
		this.kontoIIdForderungen = kontoIIdForderungen;
	}
	public Integer getKontoIIdForderungen() {
		return kontoIIdForderungen;
	}
	public void setKontoIIdVerbindlichkeiten(Integer kontoIIdVerbindlichkeiten) {
		this.kontoIIdVerbindlichkeiten = kontoIIdVerbindlichkeiten;
	}
	public Integer getKontoIIdVerbindlichkeiten() {
		return kontoIIdVerbindlichkeiten;
	}
	public void setFinanzamtIId(Integer finanzamtIId) {
		this.finanzamtIId = finanzamtIId;
	}
	public Integer getFinanzamtIId() {
		return finanzamtIId;
	}
	public void setKontoIIdKursverlust(Integer kontoIIdKursverlust) {
		this.kontoIIdKursverlust = kontoIIdKursverlust;
	}
	public Integer getKontoIIdKursverlust() {
		return kontoIIdKursverlust;
	}
	public void setKontoIIdKursgewinn(Integer kontoIIdKursgewinn) {
		this.kontoIIdKursgewinn = kontoIIdKursgewinn;
	}
	public Integer getKontoIIdKursgewinn() {
		return kontoIIdKursgewinn;
	}
	private Integer iSort;
	
	
}

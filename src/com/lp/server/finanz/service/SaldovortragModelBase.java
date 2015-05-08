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
import java.util.List;

public class SaldovortragModelBase implements Serializable {
	private static final long serialVersionUID = 7105585052117789769L;

	private Integer kontoIId ;
	private Integer gegenkontoIId ;
	private String  kontoTyp ;
	private int geschaeftsJahr ;
	private List<Integer> buchungDetailsIId ;
	
	private boolean deleteManualEB ;
	
	protected SaldovortragModelBase(int geschaeftsJahr, Integer kontoIId) {
		setGeschaeftsJahr(geschaeftsJahr) ;
		setKontoIId(kontoIId) ;
		setDeleteManualEB(false) ;
	}

	public Integer getKontoIId() {
		return kontoIId;
	}

	public void setKontoIId(Integer kontoIId) {
		if(null == kontoIId) throw new IllegalArgumentException("kontoIId == null") ;
		this.kontoIId = kontoIId;
	}

	public String getKontoTyp() {
		return kontoTyp;
	}

	public void setKontoTyp(String kontoTyp) {
		if(null == kontoTyp || kontoTyp.trim().length() == 0) throw new IllegalArgumentException("kontoTyp == null || empty") ;
		this.kontoTyp = kontoTyp;
	}

	public int getGeschaeftsJahr() {
		return geschaeftsJahr;
	}

	public void setGeschaeftsJahr(int geschaeftsJahr) {
		this.geschaeftsJahr = geschaeftsJahr;
	}
	
	public Integer getGegenkontoIId() {
		return gegenkontoIId;
	}

	/** 
	 * Das Gegenkonto explizit setzen. Falls es mit null vorbesetzt wird, wird
	 * automatisch das Gegenkonto entsprechend dem Kontotyp gew&auml;hlt
	 * 
	 * @param gegenkontoIId
	 */
	public void setGegenkontoIId(Integer gegenkontoIId) {
		this.gegenkontoIId = gegenkontoIId;
	}

	public boolean isDeleteManualEB() {
		return deleteManualEB;
	}

	public void setDeleteManualEB(boolean deleteManualEB) {
		this.deleteManualEB = deleteManualEB;
	}

	public List<Integer> getOPBuchungDetailsIId() {
		return buchungDetailsIId;
	}

	/**
	 * Die zu &uuml;bernehmenden offenen posten
	 * 
	 * @param buchungDetailsIId
	 */
	public void setOPBuchungDetailsIId(List<Integer> buchungDetailsIId) {
		this.buchungDetailsIId = buchungDetailsIId;
	}
}

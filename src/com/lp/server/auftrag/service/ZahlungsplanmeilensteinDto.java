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
 *******************************************************************************/
package com.lp.server.auftrag.service;

import java.io.Serializable;

public class ZahlungsplanmeilensteinDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;

	private Integer meilensteinId;
	private Integer zahlungsplaniId;

	private String cKommentar;
	private String xText;
	private Integer iSort;

	public Integer getISort() {
		return iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	private java.sql.Timestamp tErledigt;

	public java.sql.Timestamp getTErledigt() {
		return tErledigt;
	}

	public void setTErledigt(java.sql.Timestamp tErledigt) {
		this.tErledigt = tErledigt;
	}

	private Integer personalIIdErledigt;

	public Integer getPersonalIIdErledigt() {
		return personalIIdErledigt;
	}

	public void setPersonalIIdErledigt(Integer personalIIdErledigt) {
		this.personalIIdErledigt = personalIIdErledigt;
	}

	public Integer getMeilensteinId() {
		return meilensteinId;
	}

	public void setMeilensteinId(Integer meilensteinId) {
		this.meilensteinId = meilensteinId;
	}

	public Integer getZahlungsplaniId() {
		return zahlungsplaniId;
	}

	public void setZahlungsplaniId(Integer zahlungsplaniId) {
		this.zahlungsplaniId = zahlungsplaniId;
	}

	public String getCKommentar() {
		return cKommentar;
	}

	public void setCKommentar(String cKommentar) {
		this.cKommentar = cKommentar;
	}

	public String getXText() {
		return xText;
	}

	public void setXText(String xText) {
		this.xText = xText;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

}

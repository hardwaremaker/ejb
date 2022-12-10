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

import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.system.service.HvDtoLogClass;
import com.lp.server.system.service.HvDtoLogIdCnr;
import com.lp.server.util.IIId;

@HvDtoLogClass(name=HvDtoLogClass.STUECKLISTE_POSERSATZ, filtername=HvDtoLogClass.STUECKLISTE)
public class PosersatzDto implements Serializable, IIId {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Integer getIId() {
		return iId;
	}
	public void setIId(Integer iId) {
		this.iId = iId;
	}
	public Integer getStuecklistepositionIId() {
		return stuecklistepositionIId;
	}
	public void setStuecklistepositionIId(Integer stuecklistepositionIId) {
		this.stuecklistepositionIId = stuecklistepositionIId;
	}
	
	@HvDtoLogIdCnr(entityClass=Artikel.class)
	public Integer getArtikelIIdErsatz() {
		return artikelIIdErsatz;
	}
	public void setArtikelIIdErsatz(Integer artikelIIdErsatz) {
		this.artikelIIdErsatz = artikelIIdErsatz;
	}
	public Integer getISort() {
		return iSort;
	}
	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}
	private Integer iId;
	private Integer stuecklistepositionIId;
	private Integer artikelIIdErsatz;
	private Integer iSort;
}

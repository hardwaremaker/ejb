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
package com.lp.server.finanz.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FibuRechnungExportData {
	private Integer rechnungId ;
	private List<ErloesKontoData> erloesKontos ;
	private Map<Integer, Integer> erloesKontoMap ;
	private Integer lieferPartnerId ;
	private Integer finanzamtId ;
	private String laenderartCnr ;
	
	public FibuRechnungExportData(Integer rechnungId) {
		this.rechnungId = rechnungId ;
		erloesKontos = new ArrayList<FibuRechnungExportData.ErloesKontoData>() ;
		erloesKontoMap = new HashMap<Integer, Integer>() ;
	}
	
	public Integer getRechnungId() {
		return rechnungId ;
	}


	public Integer getLieferPartnerId() {
		return lieferPartnerId;
	}

	public void setLieferPartnerId(Integer lieferPartnerId) {
		this.lieferPartnerId = lieferPartnerId;
	}

	public Integer getFinanzamtId() {
		return finanzamtId;
	}

	public void setFinanzamtId(Integer finanzamtId) {
		this.finanzamtId = finanzamtId;
	}

	public String getLaenderartCnr() {
		return laenderartCnr;
	}

	public void setLaenderartCnr(String laenderartCnr) {
		this.laenderartCnr = laenderartCnr;
	}

	public void addErloesKonto(Integer positionId, Integer kontoId) {
		erloesKontoMap.put(positionId, kontoId) ;
	}

	public Integer getErloeskontoId(Integer positionId) {
		if(erloesKontoMap.containsKey(positionId)) return erloesKontoMap.get(positionId) ;		
		throw new IndexOutOfBoundsException(positionId.toString());
	}
	
	class ErloesKontoData {
		private Integer positionId ;
		private Integer kontoId ;
	}
}

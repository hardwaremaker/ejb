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
package com.lp.server.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



public class PositionNumberHandlerPaged extends PositionNumberHandler {

	private Map<Integer, Integer> positionNumbers = new HashMap<Integer, Integer>();
	
	public PositionNumberHandlerPaged() {
	}
	
	public PositionNumberHandlerPaged(PositionNumberAdapter adapter) {
		initializePositionNummer(adapter) ;
	}

	@Override
	public Integer getPositionNummer(Integer reposIId, PositionNumberAdapter adapter) {
		if(null == reposIId) return null ;
		
		Integer nr = positionNumbers.get(reposIId) ;
		return nr ;
	}


	/**
	 * Ermittelt f&uuml;r alle im adapter verfuegbaren Position deren Positionsnummer
	 * 
	 * @param adapter
	 */
	protected void initializePositionNummer(PositionNumberAdapter adapter) {
		positionNumbers.clear();

		Iterator<?> iterator = adapter.getPositionsIteratorForAnyPosition(null) ;
		if(null == iterator || iterator.hasNext() == false) return ;

		Integer nr = null ;
		
		while (iterator.hasNext()) {
			adapter.setAdaptee(iterator.next()) ;
			if (adapter.getPositionIId() == null) {
				boolean hasNumber = hasPositionNummer(adapter) ;
				if(hasNumber) {
					if(null == nr) {
						nr = initializeFirstPositionNummer(adapter) ;
//						nr = super.getPositionNummer(adapter.getIId(), adapter) ;
					} else {
						++nr ;
					}
					positionNumbers.put(adapter.getIId(), nr) ;
				}
			}
		}		
	}
	
	protected Integer initializeFirstPositionNummer(PositionNumberAdapter  adapter) {
		return super.getPositionNummer(adapter.getIId(), adapter) ;
	}
}

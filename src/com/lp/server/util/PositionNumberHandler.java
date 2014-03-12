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
package com.lp.server.util;

import java.util.Iterator;

import com.lp.server.system.service.LocaleFac;

public class PositionNumberHandler {
	
	public PositionNumberHandler() {
	}
	
	protected boolean isPositionBegin(PositionNumberAdapter adapter) {
		return LocaleFac.POSITIONSART_POSITION.equals(adapter.getPositionartCNr()) &&
				adapter.getCZbez().equals(LocaleFac.POSITIONBEZ_BEGINN) ;		
	}
	
	protected boolean isIdent(PositionNumberAdapter adapter) {
		return LocaleFac.POSITIONSART_IDENT.equals(adapter.getPositionartCNr()) && 
				adapter.getPositionIIdArtikelset() == null  ;	
	}
	
	public boolean hasPositionNummer(PositionNumberAdapter adapter) {
		if(isPositionBegin(adapter)) return true ;
		
		if(adapter.getTypCNr() != null) return false ;

		if(isIdent(adapter)) return true ;
	
		String positionsartCNr = adapter.getPositionartCNr() ;
		if(LocaleFac.POSITIONSART_HANDEINGABE.equals(positionsartCNr)) return true ;
		if(LocaleFac.POSITIONSART_AGSTUECKLISTE.equals(positionsartCNr)) return true ;
		if(LocaleFac.POSITIONSART_INTELLIGENTE_ZWISCHENSUMME.equals(positionsartCNr)) return true ;
		
		return false ;
	}


	public Integer getPositionNummer(Integer reposIId, PositionNumberAdapter adapter) {
		Integer nr = 0 ;
		Iterator<?> iterator = adapter.getPositionsIteratorForAnyPosition(reposIId) ;
		if(null == iterator) return null ;
		
		while (iterator.hasNext()) {
			adapter.setAdaptee(iterator.next()) ;
			if (adapter.getPositionIId() == null) {
				boolean hasNumber = hasPositionNummer(adapter) ;
				if(hasNumber) {
					++nr ;
				}

				if(reposIId.equals(adapter.getIId())) {
					return hasNumber ? nr : null ;
				}
			}
		}
		
		return null ;
	}

	/**
	 * Liefert die Positionsnummer der angegebenen Position-IId.
	 * Sollte die Position selbst keine Nummer haben, wird die unmittelbar vor dieser
	 * Position liegende letztg&uuml;ltige Nummer geliefert.
	 * 
	 * @param reposIId
	 * @param adapter
	 * @return Die Positionsnummer (1 - n), oder null wenn die Position nicht vorkommt.
	 */
	public Integer getLastPositionNummer(Integer reposIId, PositionNumberAdapter adapter) {
		Integer nr = 0 ;
		Iterator<?> iterator = adapter.getPositionsIteratorForAnyPosition(reposIId) ;
		if(null == iterator) return null ;
		
		while (iterator.hasNext()) {
			adapter.setAdaptee(iterator.next()) ;
			if (adapter.getPositionIId() == null) {
				boolean hasNumber = hasPositionNummer(adapter) ;
				if(hasNumber) {
					++nr ;
				}

				if(reposIId.equals(adapter.getIId())) {
					return nr ;
				}
			}
		}
		
		return nr ;
	}
	
	/**
	 * Liefert die IId der Position fuer die angegebene Positionsnummer zurueck
	 * 
	 * @param rechnungIId
	 * @param position die Positionsnummer f&uuml;r die die IId ermittelt werden soll
	 * @return null wenn es position nicht gibt, ansonsten die IId
	 */
	public Integer getPositionIIdFromPositionNummer(Integer rechnungIId, Integer position, PositionNumberAdapter adapter) {
		Integer foundPosition = 0 ;
		
		Iterator<?> iterator = adapter.getPositionsIteratorForHeadIId(rechnungIId) ;
		if(null == iterator) return null ;
			
		while (iterator.hasNext()) {
			adapter.setAdaptee(iterator.next()) ;
			
			if(adapter.getPositionIId() == null && hasPositionNummer(adapter)) {
				++foundPosition ;
				if(foundPosition.equals(position)) return adapter.getIId() ;
			}
		}

		return null ;
	}	
}

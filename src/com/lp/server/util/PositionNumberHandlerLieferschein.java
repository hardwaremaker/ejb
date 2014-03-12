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

import java.rmi.RemoteException;
import java.util.Iterator;

import com.lp.server.auftrag.service.AuftragpositionFac;
import com.lp.server.lieferschein.service.LieferscheinDto;

public class PositionNumberHandlerLieferschein extends PositionNumberHandler {

	private LieferscheinDto lieferscheinDto ;
	private AuftragpositionFac auftragpositionFac ;
	
	public PositionNumberHandlerLieferschein(AuftragpositionFac auftragpositionFac, LieferscheinDto lieferscheinDto) {
		this.auftragpositionFac = auftragpositionFac ;
		this.lieferscheinDto = lieferscheinDto ;
	}

	@Override
	public boolean hasPositionNummer(PositionNumberAdapter adapter) {
		Integer auftragPositionIId =  adapter.getAuftragPositionIId() ;
		if(null != auftragPositionIId) return true ;
		
		if(null == lieferscheinDto) return false ;
		// Lieferschein ist einem Auftrag zugeordnet, deshalb haben nur die
		// im Auftrag befindlichen Positionen eine Positionsnummer
		if(null != lieferscheinDto.getAuftragIId()) return false ; 
		
		return super.hasPositionNummer(adapter);
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
					Integer auftragpositionIId = adapter.getAuftragPositionIId() ;
					if(null != auftragpositionIId) {
						try {
							return auftragpositionFac.getPositionNummer(auftragpositionIId) ;
						} catch(RemoteException e) {
							return 0 ;
						}
					}
					
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
	 * Liefert die IId der Position fuer die angegebene Positionsnummer zurueck
	 * 
	 * @param rechnungIId
	 * @param position die Positionsnummer f&uuml;r die die IId ermittelt werden soll
	 * @return null wenn es position nicht gibt, ansonsten die IId
	 */
	public Integer getPositionIIdFromPositionNummer(Integer rechnungIId, Integer position, PositionNumberAdapter adapter) {
		if(lieferscheinDto.getAuftragIId() != null) {
			return auftragpositionFac.getPositionIIdFromPositionNummer(lieferscheinDto.getAuftragIId(), position) ;
		}
		return super.getPositionIIdFromPositionNummer(rechnungIId, position, adapter) ;
//		Integer foundPosition = 0 ;
//		
//		Iterator<?> iterator = adapter.getPositionsIteratorForHeadIId(rechnungIId) ;
//		if(null == iterator) return null ;
//			
//		while (iterator.hasNext()) {
//			adapter.setAdaptee(iterator.next()) ;
//			
//			if(adapter.getPositionIId() == null && hasPositionNummer(adapter)) {
//				Integer auftragpositionIId = adapter.getAuftragPositionIId() ;
//				if(null != auftragpositionIId) {
//					return auftragpositionFac.getPositionIIdFromPositionNummer(lieferscheinDto.getAuftragIId(), position) ;
//				}
//
//				++foundPosition ;
//				if(foundPosition.equals(position)) return adapter.getIId() ;
//			}
//		}
//
//		return null ;
	}	
	
}

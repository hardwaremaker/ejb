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

public abstract class PositionNumberAdapter {
	public PositionNumberAdapter() {
	}

	public abstract void setAdaptee(Object adaptee) ;
	
	/**
	 * Die IId der aktuellen Entity ermitteln
	 * @return die IId
	 */
	public abstract Integer getIId() ;
	
	/**
	 * Die IId der Position (Position == Positionsart Position) ermitteln
	 * @return !null wenn es sich um eine Position handelt
	 */
	public abstract Integer getPositionIId() ;

	/**
	 * Die Positionsart
	 * 
	 * @return die Positionsart
	 */
	public abstract String getPositionartCNr() ;
	
	public abstract String getCZbez() ;

	/**
	 * Die CNr
	 * @return die Cnr
	 */
	public abstract String getTypCNr() ;

	/**
	 * Die IId einer eventuell vorhandenen Auftragsposition
	 * 
	 * @return null wenn keine Auftragposition referenziert wird. Ist nur bei 
	 * Lieferscheinpositionen !=null sofern der Lieferscheinposition tatsaechlich
	 * eine Auftragposition zugrunde liegt.
	 */
	public abstract Integer getAuftragPositionIId() ;
	
	/**
	 * Die IId des Kopfsatzes ermitteln, also beispielsweise die Rechnung/Lieferschein/...
	 * @param posIId eine beliebige/direkte Position innerhalb dieser Rechnung
	 * @return die IId des Kopfsatzes
	 */
	public abstract Integer getHeadIIdFromPosition(Integer posIId) ;

	/**
	 * Die IId des (m&ouml;glichen) Setartikel
	 * @return IId wenn es ein Setartikel ist, ansonsten null
	 */
	public abstract Integer getPositionIIdArtikelset() ;
		
	/**
	 * Der Iterator &uuml;ber alle Positionen dieser Rechnung/Lieferschein/Anfrage/...
	 * @param anyPosIId eine beliebige IId einer Enti&auml;t dieser Rechnung/Lieferschein/Anfrage/...
	 * @return Iterator oder null
	 */
	public abstract Iterator<?> getPositionsIteratorForAnyPosition(Integer anyPosIId) ;
	
	/**
	 * Der Iterator &uuml;ber alle Positionen dieser Rechnung/Lieferschein/Anfrage
	 * @param headIId ist die IID der Rechnung/Lieferschein/Anfrage/...
	 * @return Iterator oder null
	 */
	public abstract Iterator<?> getPositionsIteratorForHeadIId(Integer headIId) ;
	
}

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

import java.util.Map;

public interface IFieldnameIndexer {
	/**
	 * Liefert den korrespondierenden Integer Wert zum Feldnamen.
	 * Per Definition kann der Integer Wert nie null enthalten
	 * 
	 * @param fieldname
	 * @return den korrespondierenden Integer zum Feldwert oder {@link IndexOutOfBoundsException} 
	 *  wenn der fieldname nicht bekannt ist.
	 * @throws IndexOutOfBoundsException
	 */
	public Integer getIndex(String fieldname) ;
	
	/**
	 * Den korrespondierenden Stringwert zum Integer.
	 * 
	 * @param fieldnumber
	 * @return Stringwert zum angegebenen Index oder {@link IndexOutOfBoundsException} 
	 *  wenn der fieldnumber nicht bekannt ist.
	 */
	public String  getIndex(Integer fieldnumber) ;
	
	public void setMap(Map<String, Integer> newMap) ;	
}

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
package com.lp.server.finanz.service;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.lp.util.EJBExceptionLP;

public class ParseSqlDateFromGMTString  implements Serializable {
	private static final long serialVersionUID = 1L;

	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * Einen GMT-String in ein Datum umwandeln.
	 * 
	 * @param gmtString
	 * @param message
	 * @return null wenn der String leer war, ansonsten das angegebene Datum
	 */
	public  java.sql.Date parse(String gmtString, String message) throws EJBExceptionLP {
		if(null == gmtString || gmtString.trim().length() == 0) return null ;

		return parseImpl(gmtString, message);
	}

	protected java.sql.Date parseImpl(String gmtString, String message) throws EJBExceptionLP {
		Date d ;
		try {
			d = (Date) formatter.parse(gmtString) ;
		} catch(ParseException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_IMPORT_UNGUELTIGES_DATUMSFORMAT, e) ;
		}

		return new java.sql.Date(d.getTime()) ;
	}
}

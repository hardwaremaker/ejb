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
package com.lp.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class HelperImport {

	private final static String _NULL_ERRMSG = "Null nicht erlaubt in Feld ";
	
	private String lasterror;
	private boolean error;
	private StringBuffer err = new StringBuffer();

	public String getStringCsv(String daten, int zeile, String feldname, int laenge, boolean nullerlaubt, boolean kuerzen) {
		if (daten == null || daten.length() == 0)
			if (!nullerlaubt) {
				error(_NULL_ERRMSG + feldname, zeile);
				return null;
			} else
				return null;
		if (daten.length() > laenge)
			if (kuerzen)
				return daten.substring(0,laenge);
			else {
				error("Feld " + feldname + " zu lang (" + daten.length() + "/" + laenge + ")", zeile);
				return null;
			}
		return daten;
	}

	public Integer getIntegerCsv(String daten, int zeile, String feldname, int laenge, boolean nullerlaubt, boolean kuerzen) {
		if (daten == null || daten.length() == 0)
			if (!nullerlaubt) {
				error(_NULL_ERRMSG + feldname, zeile);
				return null;
			} else
				return null;
		Integer i;
		try {
			i = new Integer(daten);
		} catch (NumberFormatException e) {
			error(e.getMessage() + " Feld " + feldname, zeile);
			return null;
		}
		return i;
	}

	public Double getDoubleCsv(String daten, int zeile, String feldname, boolean nullerlaubt) {
		if (daten == null || daten.length() == 0)
			if (!nullerlaubt) {
				error(_NULL_ERRMSG + feldname, zeile);
				return null;
			} else
				return null;
		Double d;
		try {
			d = new Double(daten);
		} catch (NumberFormatException e) {
			error(e.getMessage() + " Feld " + feldname, zeile);
			return null;
		}
		return d;
	}

	public BigDecimal getBigDecimalCsv(String daten, int zeile, String feldname, boolean nullerlaubt) {
		if (daten == null || daten.length() == 0)
			if (!nullerlaubt) {
				error(_NULL_ERRMSG + feldname, zeile);
				return null;
			} else
				return null;
		BigDecimal d;
		try {
			d = new BigDecimal(daten);
		} catch (NumberFormatException e) {
			error(e.getMessage() + " Feld " + feldname, zeile);
			return null;
		}
		return d;
	}
	
	public java.sql.Timestamp getTimestampCsv(String daten, int zeile, String feldname, boolean nullerlaubt) {
		if (daten == null || daten.length() == 0)
			if (!nullerlaubt) {
				error(_NULL_ERRMSG + feldname, zeile);
				return null;
			} else
				return null;
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		java.sql.Date date = null;
		try {
			date = new java.sql.Date(format.parse(daten).getTime());
		} catch (ParseException e) {
			error(e.getMessage() + " Feld " + feldname, zeile);
			return null;
		}
		Timestamp t = new Timestamp(date.getTime());
		return t;
	}


	public String getLasterror() {
		return lasterror;
	}

	public boolean isError() {
		return error;
	}
	
	public StringBuffer getErrors() {
		return err;
	}

	private void error(String msg, int zeile) {
		lasterror = msg;
		err.append("Zeile:" + zeile + " " + msg + "\r\n");
		error = true;
	}
}

package com.lp.server.eingangsrechnung.bl;

import java.sql.Date;
import java.util.ArrayList;

import javax.xml.datatype.XMLGregorianCalendar;

import com.lp.util.EJBExceptionLP;

public class ParseSqlDateFromXMLGregorian {

	public ParseSqlDateFromXMLGregorian() {
	}

	public Date parse(XMLGregorianCalendar cal, final String message) throws EJBExceptionLP {
		if(cal == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ER_IMPORT_FELD_LEER,
					new ArrayList<Object>(){{add(message);}}, null);
		}
		
		return new Date(cal.toGregorianCalendar().getTimeInMillis());
	}
}

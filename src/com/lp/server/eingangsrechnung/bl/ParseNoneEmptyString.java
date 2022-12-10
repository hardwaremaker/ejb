package com.lp.server.eingangsrechnung.bl;

import java.util.ArrayList;

import com.lp.util.EJBExceptionLP;

public class ParseNoneEmptyString {

	public ParseNoneEmptyString() {
	}

	public String parse(String value, final String message) throws EJBExceptionLP {
		if (isNullOrEmpty(value)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ER_IMPORT_FELD_LEER,
					new ArrayList<Object>(){{add(message);}}, null);
		}
		
		return value.trim();
	}

	private boolean isNullOrEmpty(String value) {
		if (value == null) return true;
		if (value.trim().length() == 0) return true;
		
		return false;
	}
}

package com.lp.server.artikel.service;

import com.lp.util.EJBExceptionLP;

public class PartSearchForbiddenExc extends PartSearchUnexpectedResponseExc {
	private static final long serialVersionUID = -375144445980240502L;

	public PartSearchForbiddenExc(String requestUrl, int status, String statusLine) {
		super(requestUrl, status, statusLine);
		setEjbCode(EJBExceptionLP.FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_RESPONSE_FORBIDDEN);
	}

	public PartSearchForbiddenExc(PartSearchUnexpectedResponseExc exc) {
		super(exc);
		setEjbCode(EJBExceptionLP.FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_RESPONSE_FORBIDDEN);
	}

}

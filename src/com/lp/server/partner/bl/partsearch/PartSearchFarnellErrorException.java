package com.lp.server.partner.bl.partsearch;

import com.lp.server.artikel.service.PartSearchUnexpectedResponseExc;
import com.lp.server.schema.element14.productsearch.FarnellError;

public class PartSearchFarnellErrorException extends PartSearchUnexpectedResponseExc {
	private static final long serialVersionUID = 2573397998591851776L;

	private FarnellError farnellError;
	
	public PartSearchFarnellErrorException(String requestUrl, int status, String statusLine, FarnellError farnellError) {
		super(requestUrl, status, statusLine);
		setFarnellError(farnellError);
	}
	
	public PartSearchFarnellErrorException(PartSearchUnexpectedResponseExc exc) {
		super(exc);
	}

	public void setFarnellError(FarnellError farnellError) {
		this.farnellError = farnellError;
	}
	
	public FarnellError getFarnellError() {
		return farnellError;
	}
}

package com.lp.server.artikel.service;

public class PartSearchPartNotFoundExc extends PartSearchUnexpectedResponseExc {
	private static final long serialVersionUID = -1902506982592376031L;

	public PartSearchPartNotFoundExc(String requestUrl, int status, String statusLine) {
		super(requestUrl, status, statusLine);
	}
	
	public PartSearchPartNotFoundExc(PartSearchUnexpectedResponseExc exc) {
		super(exc);
	}

}

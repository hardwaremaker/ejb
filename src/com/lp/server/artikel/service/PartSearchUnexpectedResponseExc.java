package com.lp.server.artikel.service;

import com.lp.util.EJBExceptionLP;

public class PartSearchUnexpectedResponseExc extends Exception {
	private static final long serialVersionUID = -1750500502837848877L;

	private Integer ejbCode;
	private String searchValue;
	private String requestUrl;
	private int status;
	private String statusLine;
	private String apiError;

	public PartSearchUnexpectedResponseExc(String requestUrl, int status, String statusLine) {
		super("Unexpected Response '" + statusLine + "' after requesting URL: " + requestUrl);
		setRequestUrl(requestUrl);
		setStatus(status);
		setStatusLine(statusLine);
		setEjbCode(EJBExceptionLP.FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_UNERWARTETE_RESPONSE);
	}
	
	public PartSearchUnexpectedResponseExc(PartSearchUnexpectedResponseExc exc) {
		this(exc.getRequestUrl(), exc.getStatus(), exc.getStatusLine());
		setEjbCode(exc.getEjbCode());
		setApiError(exc.getApiError());
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	
	public String getSearchValue() {
		return searchValue;
	}
	
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	
	public String getRequestUrl() {
		return requestUrl;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatusLine(String statusLine) {
		this.statusLine = statusLine;
	}
	
	public String getStatusLine() {
		return statusLine;
	}
	
	public Integer getEjbCode() {
		return ejbCode;
	}
	
	public void setEjbCode(Integer ejbCode) {
		this.ejbCode = ejbCode;
	}
	
	public void setApiError(String apiError) {
		this.apiError = apiError;
	}
	
	public String getApiError() {
		return apiError;
	}
}

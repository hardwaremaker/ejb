package com.lp.server.artikel.service;

public class PartSearchInvalidParamExc extends PartSearchUnexpectedResponseExc {
	private static final long serialVersionUID = 5258468753358538238L;

	private String errorCode;
	private String errorMsg;
	
	public PartSearchInvalidParamExc(String requestUrl, int status, String statusLine, String errorCode, String errorMsg) {
		super(requestUrl, status, statusLine);
		setErrorCode(errorCode);
		setErrorMsg(errorMsg);
	}
	
	public PartSearchInvalidParamExc(PartSearchUnexpectedResponseExc exc) {
		super(exc);
	}

	public String getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
	
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}

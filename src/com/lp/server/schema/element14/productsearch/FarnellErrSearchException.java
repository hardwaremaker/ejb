package com.lp.server.schema.element14.productsearch;

import java.io.Serializable;

public class FarnellErrSearchException implements Serializable {
	private static final long serialVersionUID = 709626379350079446L;

	private String exceptionCode;
	private String exceptionString;
	
	public FarnellErrSearchException() {
	}

	public String getExceptionCode() {
		return exceptionCode;
	}
	
	public void setExceptionCode(String exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
	
	public String getExceptionString() {
		return exceptionString;
	}
	
	public void setExceptionString(String exceptionString) {
		this.exceptionString = exceptionString;
	}

	public String asString() {
		return "searchException: [exceptionCode = '" + getExceptionCode() + "', exceptionString = '" + getExceptionString() + "']";
	}
}

package com.lp.server.util;

import java.io.Serializable;

public class HvHttpHeader implements Serializable {
	private static final long serialVersionUID = 3023652964384646752L;

	private Integer errorCode;
	private Integer errorCodeExtended;
	private String errorCodeDescription;
	private String errorTranslated;
	private Integer statusCode;
	private String errorKey;
	private String errorValue;
	private String additionalErrorKey;
	private String additionalErrorValue;
	private String statusReason;

	public HvHttpHeader() {
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public Integer getErrorCodeExtended() {
		return errorCodeExtended;
	}

	public void setErrorCodeExtended(Integer errorCodeExtended) {
		this.errorCodeExtended = errorCodeExtended;
	}

	public String getErrorCodeDescription() {
		return errorCodeDescription;
	}

	public void setErrorCodeDescription(String errorCodeDescription) {
		this.errorCodeDescription = errorCodeDescription;
	}

	public String getErrorTranslated() {
		return errorTranslated;
	}

	public void setErrorTranslated(String errorTranslated) {
		this.errorTranslated = errorTranslated;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getErrorKey() {
		return errorKey;
	}

	public void setErrorKey(String errorKey) {
		this.errorKey = errorKey;
	}

	public String getErrorValue() {
		return errorValue;
	}

	public void setErrorValue(String errorValue) {
		this.errorValue = errorValue;
	}

	public String getStatusReason() {
		return statusReason;
	}

	public void setStatusReason(String statusReason) {
		this.statusReason = statusReason;
	}
	
	public String getAdditionalErrorKey() {
		return additionalErrorKey;
	}
	
	public void setAdditionalErrorKey(String additionalErrorKey) {
		this.additionalErrorKey = additionalErrorKey;
	}
	
	public String getAdditionalErrorValue() {
		return additionalErrorValue;
	}
	
	public void setAdditionalErrorValue(String additionalErrorValue) {
		this.additionalErrorValue = additionalErrorValue;
	}
}

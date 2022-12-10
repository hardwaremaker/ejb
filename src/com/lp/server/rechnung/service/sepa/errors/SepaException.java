package com.lp.server.rechnung.service.sepa.errors;

public class SepaException extends Exception implements ISepaException {
	private static final long serialVersionUID = -5933672560437041963L;

	private SepaExceptionCode code;
	
	public SepaException() {
	}

	public SepaException(String message, Throwable throwable) {
		super(message, throwable);
		setCode(SepaExceptionCode.UNBEKANNT);
	}

	public SepaException(String message) {
		this(message, null);
	}

	public SepaException(Throwable throwable) {
		this(null, throwable);
	}

	public SepaExceptionCode getCode() {
		return code;
	}

	public void setCode(SepaExceptionCode code) {
		this.code = code;
	}
	
}

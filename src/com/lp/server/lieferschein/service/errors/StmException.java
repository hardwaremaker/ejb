package com.lp.server.lieferschein.service.errors;

public class StmException extends Exception {
	private static final long serialVersionUID = -8270839988194267324L;
	
	private StmExceptionCode excCode;

	public StmException() {
	}

	public StmException(String message) {
		this(message, null);
	}

	public StmException(Throwable throwable) {
		this(null, throwable);
	}

	public StmException(String message, Throwable throwable) {
		super(message, throwable);
		setExcCode(StmExceptionCode.UNBEKANNTER_FEHLER);
	}

	public void setExcCode(StmExceptionCode excCode) {
		this.excCode = excCode;
	}
	
	public StmExceptionCode getExcCode() {
		return excCode;
	}
}

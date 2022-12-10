package com.lp.service.edifact.errors;

public class EdifactException extends Exception {
	private static final long serialVersionUID = -1501010315700075239L;

	public EdifactException() {
	}
	
	public EdifactException(String message) {
		super(message);
	}
	
	public EdifactException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public EdifactException(Throwable cause) {
		super(cause);
	}
}

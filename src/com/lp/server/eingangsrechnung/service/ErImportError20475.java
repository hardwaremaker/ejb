package com.lp.server.eingangsrechnung.service;

import java.io.Serializable;

public class ErImportError20475 implements Serializable {
	private static final long serialVersionUID = -3329137008047262505L;

	public enum Severity {
		ERROR,
		WARNING;
	}
	private String message;
	private Severity severity;
	
	public ErImportError20475(Severity severity, String message) {
		setSeverity(severity);
		setMessage(message);
	}

	public Severity getSeverity() {
		return severity;
	}
	
	public void setSeverity(Severity severity) {
		this.severity = severity;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
}

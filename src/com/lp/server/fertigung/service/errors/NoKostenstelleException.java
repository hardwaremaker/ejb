package com.lp.server.fertigung.service.errors;

public class NoKostenstelleException extends ImportException {
	private static final long serialVersionUID = -5510236730871178713L;

	public NoKostenstelleException() {
		super("Cannot find any 'Kostenstelle'");
	}
}

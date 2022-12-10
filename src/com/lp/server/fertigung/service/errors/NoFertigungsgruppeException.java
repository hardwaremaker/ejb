package com.lp.server.fertigung.service.errors;

public class NoFertigungsgruppeException extends ImportException {
	private static final long serialVersionUID = 6696048886499124279L;

	public NoFertigungsgruppeException() {
		super("Cannot find any 'Fertigungsgruppe'");
	}
}

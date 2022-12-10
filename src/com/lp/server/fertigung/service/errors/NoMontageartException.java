package com.lp.server.fertigung.service.errors;

public class NoMontageartException extends ImportException {
	private static final long serialVersionUID = 4775824367012625125L;

	public NoMontageartException() {
		super("Cannot find any 'Montageart'");
	}
}

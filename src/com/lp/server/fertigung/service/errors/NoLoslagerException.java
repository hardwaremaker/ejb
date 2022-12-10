package com.lp.server.fertigung.service.errors;

public class NoLoslagerException extends ImportException {
	private static final long serialVersionUID = -4308173455352682085L;

	public NoLoslagerException() {
		super("Cannot find a 'Lager' with production sort");
	}
}

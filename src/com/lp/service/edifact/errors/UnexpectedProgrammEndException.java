package com.lp.service.edifact.errors;

public class UnexpectedProgrammEndException extends EdifactException {
	private static final long serialVersionUID = -1321879594703851387L;

	public UnexpectedProgrammEndException() {
		super("Unexpected end of programm!");
	}
}

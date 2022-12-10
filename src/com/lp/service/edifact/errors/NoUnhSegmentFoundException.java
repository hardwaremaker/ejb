package com.lp.service.edifact.errors;

public class NoUnhSegmentFoundException extends EdifactException {
	private static final long serialVersionUID = -5462882884237163320L;

	public NoUnhSegmentFoundException() {
		super("No UNH Segment found");
	}
}

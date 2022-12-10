package com.lp.service.edifact.errors;

public class InvalidSegmentException extends EdifactException {
	private static final long serialVersionUID = 6664613964779050691L;

	private String invalidSegment;
	private Character separator;
	
	public InvalidSegmentException(String invalidSegment, Character separator) {
		super("Invalid Segment-Termination '" + invalidSegment + "' with '" + separator + "'.");
		setInvalidSegment(invalidSegment);
		setSeparator(separator);
	}

	public String getInvalidSegment() {
		return invalidSegment;
	}

	public void setInvalidSegment(String invalidSegment) {
		this.invalidSegment = invalidSegment;
	}

	public Character getSeparator() {
		return separator;
	}

	public void setSeparator(Character separator) {
		this.separator = separator;
	}
}

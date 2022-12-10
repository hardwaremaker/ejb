package com.lp.service.edifact.errors;

public class UnexpectedSegmentException extends EdifactException {
	private static final long serialVersionUID = 252571759425897319L;
	
	private String segment;
	private String[] expectedSegments;
	private int position;
	private int startposition;
	
	public UnexpectedSegmentException(String segment, String[] expectedSegments, int startposition, int position) {
		super("UnexpectedSegment '" + segment + "' starting at " + startposition + ", actual position " + position + ".");
		this.setSegment(segment);
		this.setExpectedSegments(expectedSegments);
		this.setStartposition(startposition);
		this.setPosition(position);
	}

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}

	public String[] getExpectedSegments() {
		return expectedSegments;
	}

	public void setExpectedSegments(String[] expectedSegments) {
		this.expectedSegments = expectedSegments;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getStartposition() {
		return startposition;
	}

	public void setStartposition(int startposition) {
		this.startposition = startposition;
	}
}

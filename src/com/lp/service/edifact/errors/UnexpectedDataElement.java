package com.lp.service.edifact.errors;

public class UnexpectedDataElement extends EdifactException {

	private static final long serialVersionUID = 6691949750943183436L;
	
	private Character elementSeparator;
	private Character expectedElementSeparator;
	private int position;
	private int startposition;
	
	public UnexpectedDataElement(Character elementSeparator, Character expectedElementSeparator, int startposition, int position) {
		super("Unexpected Data-Element-Separator '" + elementSeparator + ", expected '" + expectedElementSeparator + "', starting at " + startposition + ", actual position " + position + ".");
		setElementSeparator(elementSeparator);
		setExpectedElementSeparator(expectedElementSeparator);
		setPosition(position);
		setStartposition(startposition);
	}

	public Character getElementSeparator() {
		return elementSeparator;
	}

	public void setElementSeparator(Character elementSeparator) {
		this.elementSeparator = elementSeparator;
	}

	public Character getExpectedElementSeparator() {
		return expectedElementSeparator;
	}

	public void setExpectedElementSeparator(Character expectedElementSeparator) {
		this.expectedElementSeparator = expectedElementSeparator;
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

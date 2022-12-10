package com.lp.service.edifact.errors;

public class ParseQuantityException extends EdifactException {
	private static final long serialVersionUID = -8498612453534136355L;

	private int position;
	private int startposition;
	
	public ParseQuantityException(Throwable cause, int startposition, int position) {
		super("Quantity format parse Exception", cause);
		setStartposition(startposition);
		setPosition(position);
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

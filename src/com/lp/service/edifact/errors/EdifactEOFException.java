package com.lp.service.edifact.errors;

public class EdifactEOFException extends EdifactException {
	private static final long serialVersionUID = 721857724088948696L;

	private int startposition;
	private int position;
	
	public EdifactEOFException(int startposition, int position) {
		super("EOFException starting at " + startposition + ", actual position " + position + ".");
		this.setStartposition(startposition);
		this.setPosition(position);
	}

	public int getStartposition() {
		return startposition;
	}

	public void setStartposition(int startposition) {
		this.startposition = startposition;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}

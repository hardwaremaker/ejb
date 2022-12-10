package com.lp.service.edifact.errors;

public class UnknownDateFormatException extends EdifactException {
	private static final long serialVersionUID = 5611128472974959326L;
	
	private String dateFormat;
	private int position;
	private int startPosition;
	
	public UnknownDateFormatException(String format, int startposition, int position) {
		super("Unknown Date/Time Format '" + format + "'.");
		setDateFormat(format);
		setStartPosition(startposition);
		setPosition(position);
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}
}

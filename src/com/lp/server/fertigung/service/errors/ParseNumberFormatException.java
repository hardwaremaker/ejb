package com.lp.server.fertigung.service.errors;

public class ParseNumberFormatException extends ImportException {

	private static final long serialVersionUID = 1287966028059138258L;
	private String column;
	private String value;
	
	public ParseNumberFormatException(String column, String value, Throwable cause) {
		super("Cannot parse number, value is '" + value + "'", cause);
		setValue(value);
		setColumn(column);
	}
	
	public void setColumn(String column) {
		this.column = column;
	}
	
	public String getColumn() {
		return column;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}

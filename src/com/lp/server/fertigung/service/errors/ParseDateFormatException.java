package com.lp.server.fertigung.service.errors;

public class ParseDateFormatException extends ImportException {

	private static final long serialVersionUID = 5082368144805671114L;

	private String column;
	private String format;
	private String value;
	
	public ParseDateFormatException(String column, String format, String value, Throwable cause) {
		super("Cannot parse Date/Time with format '" + format + "'. Value is '" + value + "'", cause);
		setColumn(column);
		setFormat(format);
		setValue(value);
	}
	
	public void setColumn(String column) {
		this.column = column;
	}
	
	public String getColumn() {
		return column;
	}
	
	public void setFormat(String format) {
		this.format = format;
	}
	
	public String getFormat() {
		return format;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}

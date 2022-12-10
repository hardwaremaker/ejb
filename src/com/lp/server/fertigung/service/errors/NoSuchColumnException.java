package com.lp.server.fertigung.service.errors;

public class NoSuchColumnException extends ImportException {

	private static final long serialVersionUID = 4743959388143653504L;
	private String column;
	
	public NoSuchColumnException(String column) {
		super("Can not find column '" + column + "'");
		setColumn(column);
	}
	
	public void setColumn(String column) {
		this.column = column;
	}
	
	public String getColumn() {
		return column;
	}
}

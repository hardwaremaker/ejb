package com.lp.server.fertigung.service.errors;

public class ImportException extends Exception {

	private static final long serialVersionUID = 731575397111739276L;
	private Integer linenumber;
	
	public ImportException(String message) {
		super(message);
	}
	
	public ImportException(Throwable throwable) {
		super(throwable);
	}
	
	public ImportException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public void setLinenumber(Integer linenumber) {
		this.linenumber = linenumber;
	}
	
	public Integer getLinenumber() {
		return linenumber;
	}
}

package com.lp.server.fertigung.service.errors;

public class NoSetArticlePositionsException extends ImportException {
	private static final long serialVersionUID = -7703545092135942577L;

	private String partlistnumber;

	public NoSetArticlePositionsException(String partlistnumber) {
		super("Set article '" + partlistnumber + "' has no positions.");
		setPartlistnumber(partlistnumber);
	}

	public void setPartlistnumber(String partlistnumber) {
		this.partlistnumber = partlistnumber;
	}
	
	public String getPartlistnumber() {
		return partlistnumber;
	}
}

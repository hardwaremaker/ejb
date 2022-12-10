package com.lp.server.fertigung.service.errors;

public class NoOrUnknownDefaultArticleException extends ImportException {
	private static final long serialVersionUID = 2412201052417276763L;

	private String articlenumber;
	private String parametername;
	
	public NoOrUnknownDefaultArticleException(String articlenumber, String parametername) {
		super("Unknown articlenumber '" + articlenumber + "' in parameter '" + parametername + "'");
		setArticlenumber(articlenumber);
		setParametername(parametername);
	}

	public NoOrUnknownDefaultArticleException(String parametername) {
		super("No articlenumber defined in parameter '" + parametername + "'");
		setParametername(parametername);
	}

	public void setArticlenumber(String articlenumber) {
		this.articlenumber = articlenumber;
	}
	
	public String getArticlenumber() {
		return articlenumber;
	}
	
	public void setParametername(String parametername) {
		this.parametername = parametername;
	}
	
	public String getParametername() {
		return parametername;
	}
}

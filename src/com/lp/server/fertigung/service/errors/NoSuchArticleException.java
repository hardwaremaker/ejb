package com.lp.server.fertigung.service.errors;

public class NoSuchArticleException extends ImportException {

	private static final long serialVersionUID = 3768770542337455200L;

	private String articlenumber;
	
	public NoSuchArticleException(String articlenumber) {
		super("Cannot find an article with number '" + articlenumber + "'");
		setArticlenumber(articlenumber);
	}
	
	public void setArticlenumber(String articlenumber) {
		this.articlenumber = articlenumber;
	}
	
	public String getArticlenumber() {
		return articlenumber;
	}
}

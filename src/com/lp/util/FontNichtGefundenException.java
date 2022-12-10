package com.lp.util;

public class FontNichtGefundenException extends EJBExceptionData {
	private static final long serialVersionUID = 7889151389990186339L;

	private String fontName;

	public FontNichtGefundenException(String fontName) {
		setFontName(fontName);
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	
	public String getFontName() {
		return fontName;
	}
}

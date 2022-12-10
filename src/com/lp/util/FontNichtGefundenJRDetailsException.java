package com.lp.util;

import net.sf.jasperreports.engine.fill.JRTemplatePrintText;

public class FontNichtGefundenJRDetailsException extends FontNichtGefundenException {
	private static final long serialVersionUID = -5744546017467917896L;

	private JRTemplatePrintText textField;
	
	public FontNichtGefundenJRDetailsException(JRTemplatePrintText textField) {
		super(textField.getFontName());
		setTextField(textField);
	}
	
	public FontNichtGefundenJRDetailsException(JRTemplatePrintText textField, String fontname) {
		super(fontname);
		setTextField(textField);
	}

	public void setTextField(JRTemplatePrintText textField) {
		this.textField = textField;
	}
	
	public JRTemplatePrintText getTextField() {
		return textField;
	}
}

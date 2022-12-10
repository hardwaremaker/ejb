package com.lp.service.edifact;

public class DefaultSeparators extends Separators {
	public DefaultSeparators() {
		setComposite(':');
		setData('+');
		setDecimal('.');
		setEscape('?');
		setSegment('\'');
	}
}

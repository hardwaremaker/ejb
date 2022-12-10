package com.lp.service.edifact.errors;

public class ElementTooLongException extends EdifactException {
	private static final long serialVersionUID = -500702368514602473L;
	private String dataRead;
	private int maxLength;
	
	public ElementTooLongException(int maxLength, String dataRead) {
		super("Element too long! Data='" + dataRead);
		this.setMaxLength(maxLength);
		this.setDataRead(dataRead);
	}

	public String getDataRead() {
		return dataRead;
	}

	public void setDataRead(String dataRead) {
		this.dataRead = dataRead;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
}

package com.lp.server.finanz.service;

import java.io.Serializable;

public enum SepaCamtFormatEnum implements Serializable {
	CAMT052("camt.052"),
	CAMT053("camt.053");
	
	private String value;
	
	SepaCamtFormatEnum(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public static SepaCamtFormatEnum fromString(String value) {
		if (value != null) {
			for (SepaCamtFormatEnum format : SepaCamtFormatEnum.values()) {
				if (value.equals(format.value)) {
					return format;
				}
			}
		}
		throw new IllegalArgumentException("No enum value '" + value + "'");
	}
}

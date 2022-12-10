package com.lp.server.finanz.service;

import java.io.Serializable;

public enum SepaCamtVersionEnum implements Serializable {
	VERSION02("02"),
	VERSION05("05");
	
	private String value;
	
	private SepaCamtVersionEnum(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}

	public static SepaCamtVersionEnum fromString(String value) {
		if (value != null) {
			for (SepaCamtVersionEnum version : SepaCamtVersionEnum.values()) {
				if (value.equals(version.value)) {
					return version;
				}
			}
		}
		throw new IllegalArgumentException("No enum value '" + value + "'");
	}
}

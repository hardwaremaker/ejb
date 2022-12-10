package com.lp.server.finanz.service;

import java.io.Serializable;

public enum SepaSaldoCodeEnum implements Serializable {
	CLBD("CLBD"),
	CLAV("CLAV"),
	FWAV("FWAV"),
	ITBD("ITBD"),
	PRCD("PRCD"),
	INFO("INFO"),
	ITAV("ITAV"),
	OPBD("OPBD");

	private String value;
	
	SepaSaldoCodeEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public static SepaSaldoCodeEnum fromString(String value) {
		if (value != null) {
			for (SepaSaldoCodeEnum format : SepaSaldoCodeEnum.values()) {
				if (value.equals(format.value)) {
					return format;
				}
			}
		}
		throw new IllegalArgumentException("No enum value '" + value + "'");
	}
}

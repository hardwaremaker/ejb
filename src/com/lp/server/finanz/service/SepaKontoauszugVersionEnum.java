package com.lp.server.finanz.service;

import java.io.Serializable;

public enum SepaKontoauszugVersionEnum implements Serializable {
	EINS(1),
	ZWEI(2),
	DREI(3),
	VIER(4),
	FUENF(5),
	SECHS(6),
	SIEBEN(7);
	
	private Integer value;
	
	SepaKontoauszugVersionEnum(Integer value) {
		this.value = value;
	}
	
	public Integer getValue() {
		return this.value;
	}
	
	public static SepaKontoauszugVersionEnum fromInteger(Integer value) {
		if (value != null) {
			for (SepaKontoauszugVersionEnum version : SepaKontoauszugVersionEnum.values()) {
				if (value.equals(version.value)) {
					return version;
				}
			}
		}
		
		throw new IllegalArgumentException("No enum value '" + value + "'");
	}
}

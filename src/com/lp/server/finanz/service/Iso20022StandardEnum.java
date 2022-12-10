package com.lp.server.finanz.service;

public enum Iso20022StandardEnum {
	SEPA,
	SWISS;
	
	public static Iso20022StandardEnum lookup(String id) {
		for (Iso20022StandardEnum std : values()) {
			if (std.name().equals(id.trim())) {
				return std;
			}
		}
		throw new IllegalArgumentException("No enum '" + id + "'");
	}
}

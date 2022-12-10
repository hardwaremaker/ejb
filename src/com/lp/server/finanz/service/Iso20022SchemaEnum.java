package com.lp.server.finanz.service;

public enum Iso20022SchemaEnum {
	PAIN00100103("pain.001.001.03", 3),
	PAIN00100106("pain.001.001.06", 6),
	PAIN00800102("pain.008.001.02", 2);
	
	private String value;
	private Integer version;
	
	private Iso20022SchemaEnum(String value, Integer version) {
		this.value = value;
		this.version = version;
	}
	
	public Integer getVersion() {
		return version;
	}
	
	public static Iso20022SchemaEnum fromValue(String value) {
		if (value != null) {
			for (Iso20022SchemaEnum schema : values()) {
				if (schema.value.equals(value.trim())) {
					return schema;
				}
			}
		}
		throw new IllegalArgumentException("No enum value '" + value + "'");
	}
}

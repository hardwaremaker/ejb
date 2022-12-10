package com.lp.server.schema.element14.productsearch;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FarnellAttribute implements Serializable {
	private static final long serialVersionUID = -7708572289987425605L;

	@JsonProperty("attributeLabel")
	private String label;
	@JsonProperty("attributeUnit")
	private String unit;
	@JsonProperty("attributeValue")
	private String value;
	
	public FarnellAttribute() {
	}

	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getUnit() {
		return unit;
	}
	
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
}

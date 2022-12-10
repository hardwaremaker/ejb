package com.lp.service.edifact.schema;

import java.math.BigInteger;

public class CntInfo {
	private String qualifier;
	private BigInteger value;
	private String measureUnit;
	
	public String getQualifier() {
		return qualifier;
	}
	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}
	public BigInteger getValue() {
		return value;
	}
	public void setValue(BigInteger value) {
		this.value = value;
	}
	public String getMeasureUnit() {
		return measureUnit;
	}
	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}
}

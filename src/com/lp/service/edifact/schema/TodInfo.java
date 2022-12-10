package com.lp.service.edifact.schema;

public class TodInfo {
	private String functionCode;
	private String paymentCode;
	private String transportationCode;
	private String transportationQualifier;
	private String responsibleAgency;
	private String terms1;
	private String terms2;
	
	public String getFunctionCode() {
		return functionCode;
	}
	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public String getPaymentCode() {
		return paymentCode;
	}
	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}
	
	/*
	 * EXW usw.
	 */
	public String getTransportationCode() {
		return transportationCode;
	}
	public void setTransportationCode(String transportationCode) {
		this.transportationCode = transportationCode;
	}
	public String getTransportationQualifier() {
		return transportationQualifier;
	}
	public void setTransportationQualifier(String transportationQualifier) {
		this.transportationQualifier = transportationQualifier;
	}
	public String getResponsibleAgency() {
		return responsibleAgency;
	}
	public void setResponsibleAgency(String responsibleAgency) {
		this.responsibleAgency = responsibleAgency;
	}
	public String getTerms1() {
		return terms1;
	}
	public void setTerms1(String terms1) {
		this.terms1 = terms1;
	}
	public String getTerms2() {
		return terms2;
	}
	public void setTerms2(String terms2) {
		this.terms2 = terms2;
	}
}

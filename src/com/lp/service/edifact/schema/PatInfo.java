package com.lp.service.edifact.schema;

/**
 * Payment Terms Basis
 * @author ghp	
 */
public class PatInfo {
	private String typeQualifier;
	private String identification;
	private String codeQualifier;
	private String agency;
	private String terms;
	private String codedTime;
	private String codedRelation;
	private String codedPeriod;
	private int periods;
	
	
	public String getTypeQualifier() {
		return typeQualifier;
	}
	public void setTypeQualifier(String typeQualifier) {
		this.typeQualifier = typeQualifier;
	}
	public String getIdentification() {
		return identification;
	}
	public void setIdentification(String identification) {
		this.identification = identification;
	}
	public String getCodeQualifier() {
		return codeQualifier;
	}
	public void setCodeQualifier(String codeQualifier) {
		this.codeQualifier = codeQualifier;
	}
	public String getAgency() {
		return agency;
	}
	public void setAgency(String agency) {
		this.agency = agency;
	}
	public String getTerms() {
		return terms;
	}
	public void setTerms(String terms) {
		this.terms = terms;
	}
	public String getCodedTime() {
		return codedTime;
	}
	public void setCodedTime(String codedTime) {
		this.codedTime = codedTime;
	}
	public String getCodedRelation() {
		return codedRelation;
	}
	public void setCodedRelation(String codedRelation) {
		this.codedRelation = codedRelation;
	}
	public String getCodedPeriod() {
		return codedPeriod;
	}
	public void setCodedPeriod(String codedPeriod) {
		this.codedPeriod = codedPeriod;
	}
	public int getPeriods() {
		return periods;
	}
	public void setPeriods(int periods) {
		this.periods = periods;
	}
}

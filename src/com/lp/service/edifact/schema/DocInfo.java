package com.lp.service.edifact.schema;

public class DocInfo {
	private String nameCode;
	private String identificationCode;
	private String responsibleAgencyCode;
	private String name;
	private String identifier;
	private String statusCode;
	private String sourceDescription;
	private String languageName;
	private String version;
	private String revision;
	private String mediumType;
	private Integer copiesQuantity;
	private Integer originalQuantity;
	
	public String getNameCode() {
		return nameCode;
	}
	public void setNameCode(String nameCode) {
		this.nameCode = nameCode;
	}
	public String getIdentificationCode() {
		return identificationCode;
	}
	public void setIdentificationCode(String identificationCode) {
		this.identificationCode = identificationCode;
	}
	public String getResponsibleAgencyCode() {
		return responsibleAgencyCode;
	}
	public void setResponsibleAgencyCode(String responsibleAgencyCode) {
		this.responsibleAgencyCode = responsibleAgencyCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getSourceDescription() {
		return sourceDescription;
	}
	public void setSourceDescription(String sourceDescription) {
		this.sourceDescription = sourceDescription;
	}
	public String getLanguageName() {
		return languageName;
	}
	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getRevision() {
		return revision;
	}
	public void setRevision(String revision) {
		this.revision = revision;
	}
	public String getMediumType() {
		return mediumType;
	}
	public void setMediumType(String mediumType) {
		this.mediumType = mediumType;
	}
	public Integer getCopiesQuantity() {
		return copiesQuantity;
	}
	public void setCopiesQuantity(Integer copiesQuantity) {
		this.copiesQuantity = copiesQuantity;
	}
	public Integer getOriginalQuantity() {
		return originalQuantity;
	}
	public void setOriginalQuantity(Integer originalQuantity) {
		this.originalQuantity = originalQuantity;
	}
}

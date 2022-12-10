package com.lp.service.edifact.schema;

public class PiaInfoItem {
	private String itemIdentifier;
	private String typeIdentificationCode;
	private String identificationCode;
	private String responsibleAgencyCode;
	
	public String getItemIdentifier() {
		return itemIdentifier;
	}
	public void setItemIdentifier(String itemIdentifier) {
		this.itemIdentifier = itemIdentifier;
	}
	public String getTypeIdentificationCode() {
		return typeIdentificationCode;
	}
	public void setTypeIdentificationCode(String typeIdentificationCode) {
		this.typeIdentificationCode = typeIdentificationCode;
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
}

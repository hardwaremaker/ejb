package com.lp.service.edifact.schema;

public class BgmInfo {
	private String documentNameCode;
	private String documentIdentifier;
	private String messageFunction;
	private String responseTypeCode;
	private String identificationCode;
	private String responsibleAgencyCode;
	private String documentName;
	private String versionIdentifier;
	private String revisionIdentifier;
	
	public String getDocumentNameCode() {
		return documentNameCode;
	}

	public void setDocumentNameCode(String documentNameCode) {
		this.documentNameCode = documentNameCode;
	}

	public String getDocumentIdentifier() {
		return documentIdentifier;
	}

	public void setDocumentIdentifier(String documentIdentifier) {
		this.documentIdentifier = documentIdentifier;
	}

	public String getMessageFunction() {
		return messageFunction;
	}

	public void setMessageFunction(String messageFunction) {
		this.messageFunction = messageFunction;
	}

	public String getResponseTypeCode() {
		return responseTypeCode;
	}

	public void setResponseTypeCode(String responseTypeCode) {
		this.responseTypeCode = responseTypeCode;
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

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getVersionIdentifier() {
		return versionIdentifier;
	}

	public void setVersionIdentifier(String versionIdentifier) {
		this.versionIdentifier = versionIdentifier;
	}

	public String getRevisionIdentifier() {
		return revisionIdentifier;
	}

	public void setRevisionIdentifier(String revisionIdentifier) {
		this.revisionIdentifier = revisionIdentifier;
	}
}

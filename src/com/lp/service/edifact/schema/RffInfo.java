package com.lp.service.edifact.schema;

public class RffInfo {
	private String functionCode;
	private String identifier;
	private String documentLineIdentifier;
	private String versionIdentifier;
	private String revisionIdentifier;
	
	public String getFunctionCode() {
		return functionCode;
	}
	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getDocumentLineIdentifier() {
		return documentLineIdentifier;
	}
	public void setDocumentLineIdentifier(String documentLineIdentifier) {
		this.documentLineIdentifier = documentLineIdentifier;
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

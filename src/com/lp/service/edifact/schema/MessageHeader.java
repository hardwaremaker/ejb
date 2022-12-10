package com.lp.service.edifact.schema;

public class MessageHeader {
	
	private String referenceCnr;
	private String type;
	private String version;
	private String release;
	private String agency;
	
	private boolean success;
	
	
	public String getReferenceCnr() {
		return referenceCnr;
	}
	public void setReferenceCnr(String referenceCnr) {
		this.referenceCnr = referenceCnr;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getRelease() {
		return release;
	}
	public void setRelease(String release) {
		this.release = release;
	}
	public String getAgency() {
		return agency;
	}
	public void setAgency(String agency) {
		this.agency = agency;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public void beSuccess() {
		setSuccess(true);
	}
}

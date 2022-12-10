package com.lp.server.projekt.service;

import java.io.Serializable;


public class ProjektFLRDataDto implements IProjektFLRData, Serializable {
	private static final long serialVersionUID = -7275797375955894582L;

	private String address ;
	private String category ;
	private String title ;
	private String internalComment ;
	private String externalComment ;
	private long deadlineMs ;
	private Boolean internalDone ;
	private Integer priority ;
	private Integer partnerId ;
	private String statusCnr ;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getInternalComment() {
		return internalComment;
	}
	public void setInternalComment(String internalComment) {
		this.internalComment = internalComment;
	}
	public String getExternalComment() {
		return externalComment;
	}
	public void setExternalComment(String externalComment) {
		this.externalComment = externalComment;
	}
	public long getDeadlineMs() {
		return deadlineMs;
	}
	public void setDeadlineMs(long deadline) {
		this.deadlineMs = deadline;
	}
	public Boolean isInternalDone() {
		return internalDone;
	}
	public void setInternalDone(Boolean internalDone) {
		this.internalDone = internalDone;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Integer getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}
	@Override
	public String getStatusCnr() {
		return statusCnr ;
	}		
	@Override
	public void setStatusCnr(String statusCnr) {
		this.statusCnr = statusCnr ;
	}
}

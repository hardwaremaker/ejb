package com.lp.service.edifact.schema;

import java.util.Date;

public class UnbInfo {
	private String syntaxIdentifier;
	private String syntaxVersionNumber;
	private String senderIdentification;
	private String receiverIdentification;
	private String dateString;
	private String timeString;
	private String controlReference;
	private Date date;
	private String recipientReference;
	private String applicationReference;
	
	public String getSyntaxIdentifier() {
		return syntaxIdentifier;
	}
	public void setSyntaxIdentifier(String syntaxIdentifier) {
		this.syntaxIdentifier = syntaxIdentifier;
	}
	public String getSyntaxVersionNumber() {
		return syntaxVersionNumber;
	}
	public void setSyntaxVersionNumber(String syntaxVersionNumber) {
		this.syntaxVersionNumber = syntaxVersionNumber;
	}
	public String getSenderIdentification() {
		return senderIdentification;
	}
	public void setSenderIdentification(String senderIdentification) {
		this.senderIdentification = senderIdentification;
	}
	public String getReceiverIdentification() {
		return receiverIdentification;
	}
	public void setReceiverIdentification(String receiverIdentification) {
		this.receiverIdentification = receiverIdentification;
	}
	public String getDateString() {
		return dateString;
	}
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	public String getTimeString() {
		return timeString;
	}
	public void setTimeString(String timeString) {
		this.timeString = timeString;
	}
	public String getControlReference() {
		return controlReference;
	}
	public void setControlReference(String controlReference) {
		this.controlReference = controlReference;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getApplicationReference() {
		return applicationReference;
	}
	public void setApplicationReference(String applicationReference) {
		this.applicationReference = applicationReference;
	}
	public String getRecipientReference() {
		return recipientReference;
	}
	public void setRecipientReference(String recipientReference) {
		this.recipientReference = recipientReference;
	}
}

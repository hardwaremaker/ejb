package com.lp.service.edifact.schema;

public class LinInfo {
	private String lineItemIdentifier;
	private String actionCode;
	private String itemIdentifier;
	private String itemTypeIdentificationCode;
	private String identificationCode;
	private String responsibleAgencyCode;
	
	private String sublineIdenticatorCode;
	private String sublineItemIdentifier;
	
	private Integer configurationLevelNumber;
	private String configurationOperationCode;

	public String getLineItemIdentifier() {
		return lineItemIdentifier;
	}
	public void setLineItemIdentifier(String lineItemIdentifier) {
		this.lineItemIdentifier = lineItemIdentifier;
	}
	public String getActionCode() {
		return actionCode;
	}
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}
	public String getItemIdentifier() {
		return itemIdentifier;
	}
	public void setItemIdentifier(String itemIdentifier) {
		this.itemIdentifier = itemIdentifier;
	}
	public String getItemTypeIdentificationCode() {
		return itemTypeIdentificationCode;
	}
	public void setItemTypeIdentificationCode(String itemTypeIdentificationCode) {
		this.itemTypeIdentificationCode = itemTypeIdentificationCode;
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
	public String getSublineIdenticatorCode() {
		return sublineIdenticatorCode;
	}
	public void setSublineIdenticatorCode(String sublineIdenticatorCode) {
		this.sublineIdenticatorCode = sublineIdenticatorCode;
	}
	public String getSublineItemIdentifier() {
		return sublineItemIdentifier;
	}
	public void setSublineItemIdentifier(String sublineItemIdentifier) {
		this.sublineItemIdentifier = sublineItemIdentifier;
	}
	public Integer getConfigurationLevelNumber() {
		return configurationLevelNumber;
	}
	public void setConfigurationLevelNumber(Integer configurationLevelNumber) {
		this.configurationLevelNumber = configurationLevelNumber;
	}
	public void setConfigurationLevelNumber(String configurationLevelNumber) {
		if(configurationLevelNumber == null || configurationLevelNumber.length() == 0) {
			this.configurationLevelNumber = null;
		} else {
			try {
				this.configurationLevelNumber = Integer.valueOf(configurationLevelNumber);
			} catch(NumberFormatException e) {
				// TODO: NumberformatException loggen und behandeln		
			}			
		}
	}
	
	public String getConfigurationOperationCode() {
		return configurationOperationCode;
	}
	public void setConfigurationOperationCode(String configurationOperationCode) {
		this.configurationOperationCode = configurationOperationCode;
	}
}

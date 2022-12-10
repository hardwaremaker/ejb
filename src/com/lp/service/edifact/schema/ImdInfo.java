package com.lp.service.edifact.schema;

public class ImdInfo {
	private String formatCode;
	private String characteristicCode;
	private String identificationCode;
	private String responsibleAgencyCode;
	
	private String descriptionCode;
	private String descriptionIdentificationCode;
	private String descriptionResponsibleAgencyCode;
	private String[] description;
	private String languageNameCode;
	private String surfaceLayerCode;
	
	public ImdInfo() {
		description = new String[2];
	}
	
	public String getFormatCode() {
		return formatCode;
	}
	public void setFormatCode(String formatCode) {
		this.formatCode = formatCode;
	}
	public String getCharacteristicCode() {
		return characteristicCode;
	}
	public void setCharacteristicCode(String characteristicCode) {
		this.characteristicCode = characteristicCode;
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
	public String getDescriptionCode() {
		return descriptionCode;
	}
	public void setDescriptionCode(String descriptionCode) {
		this.descriptionCode = descriptionCode;
	}
	public String getDescriptionIdentificationCode() {
		return descriptionIdentificationCode;
	}
	public void setDescriptionIdentificationCode(
			String descriptionIdentificationCode) {
		this.descriptionIdentificationCode = descriptionIdentificationCode;
	}
	public String getDescriptionResponsibleAgencyCode() {
		return descriptionResponsibleAgencyCode;
	}
	public void setDescriptionResponsibleAgencyCode(
			String descriptionResponsibleAgencyCode) {
		this.descriptionResponsibleAgencyCode = descriptionResponsibleAgencyCode;
	}
	public String[] getDescriptions() {
		return description;
	}
	public void setDescriptions(String[] description) {
		this.description = description;
	}
	public String getDescription(int index) {
		if(index < 0 || index >= this.description.length) {
			throw new IndexOutOfBoundsException("Index " + index + " is > " + this.description.length);
		}
		return this.description[index];
	}
	
	public void setDescription(int index, String description) {
		if(index < 0 || index >= this.description.length) {
			throw new IndexOutOfBoundsException("Index " + index + " is > " + this.description.length);
		}
		this.description[index] = description;
	}
	
	public String getLanguageNameCode() {
		return languageNameCode;
	}
	public void setLanguageNameCode(String languageNameCode) {
		this.languageNameCode = languageNameCode;
	}
	public String getSurfaceLayerCode() {
		return surfaceLayerCode;
	}
	public void setSurfaceLayerCode(String surfaceLayerCode) {
		this.surfaceLayerCode = surfaceLayerCode;
	}
}

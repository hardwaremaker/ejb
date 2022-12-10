package com.lp.service.edifact.schema;

public class GisInfo {
	private String indicatorDescriptionCode;
	private String identificationCode;
	private String responsibleAgencyCode;
	private String typeDescriptionCode;

	public String getIndicatorDescriptionCode() {
		return indicatorDescriptionCode;
	}
	public void setIndicatorDescriptionCode(String indicatorDescriptionCode) {
		this.indicatorDescriptionCode = indicatorDescriptionCode;
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
	public String getTypeDescriptionCode() {
		return typeDescriptionCode;
	}
	public void setTypeDescriptionCode(String typeDescriptionCode) {
		this.typeDescriptionCode = typeDescriptionCode;
	}
}

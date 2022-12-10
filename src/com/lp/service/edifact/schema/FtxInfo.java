package com.lp.service.edifact.schema;

import java.util.ArrayList;
import java.util.List;

public class FtxInfo {
	private String subjectCode;
	private String functionCode;
	private String valueCode;
	private String identificationCode;
	private String responsibleAgencyCode;	
	private String texts[];
	private String languageNameCode;
	private String formatCode;
	
	public FtxInfo() {
		texts = new String[5];
	}

	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public String getValueCode() {
		return valueCode;
	}

	public void setValueCode(String valueCode) {
		this.valueCode = valueCode;
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

	public String getLanguageNameCode() {
		return languageNameCode;
	}

	public void setLanguageNameCode(String languageNameCode) {
		this.languageNameCode = languageNameCode;
	}

	public String getFormatCode() {
		return formatCode;
	}

	public void setFormatCode(String formatCode) {
		this.formatCode = formatCode;
	}

	public List<String> getTexts() {
		List<String> entries = new ArrayList<String>();
		for(int i = 0; i < texts.length; i++) {
			if(texts[i] != null) {
				entries.add(texts[i]);
			}
		}
		return entries;
	}
	
	public String getText(int index) {
		if(index < 0 || index >= texts.length) {
			throw new IndexOutOfBoundsException("Index " + index + " is > " + texts.length);
		}
		return texts[index];
	}
	
	public void setText(int index, String name) {
		if(index < 0 || index >= texts.length) {
			throw new IndexOutOfBoundsException("Index " + index + " is > " + texts.length);
		}
		this.texts[index] = name;
	}	
}

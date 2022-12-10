package com.lp.util;

public class GTINGenerierungAlleArtikelnummernVergebenException extends EJBExceptionData {
	private static final long serialVersionUID = -3231492862194668609L;

	private String companyPrefix;
	private Integer highestItemReference;
	
	public GTINGenerierungAlleArtikelnummernVergebenException(String companyPrefix, Integer highestItemReference) {
		setCompanyPrefix(companyPrefix);
		setHighestItemReference(highestItemReference);
	}
	
	public String getCompanyPrefix() {
		return companyPrefix;
	}
	
	public void setCompanyPrefix(String companyPrefix) {
		this.companyPrefix = companyPrefix;
	}
	
	public Integer getHighestItemReference() {
		return highestItemReference;
	}
	
	public void setHighestItemReference(Integer highestItemReference) {
		this.highestItemReference = highestItemReference;
	}
}

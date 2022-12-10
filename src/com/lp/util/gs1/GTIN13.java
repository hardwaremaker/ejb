package com.lp.util.gs1;

import java.io.Serializable;

public class GTIN13 implements Serializable {
	private static final long serialVersionUID = 9053390164751929525L;

	private String companyPrefix;
	private String itemReference;
	private Character checkDigit;
	
	public GTIN13() {
	}
	
	public String getCompanyPrefix() {
		return companyPrefix;
	}
	
	public void setCompanyPrefix(String companyPrefix) {
		validatePart(companyPrefix, "companyPrefix");
		this.companyPrefix = companyPrefix;
	}
	
	public String getItemReference() {
		return itemReference;
	}
	
	public void setItemReference(String itemReference) {
		validatePart(itemReference, "itemReference");
		this.itemReference = itemReference;
	}
	
	public Character getCheckDigit() {
		return checkDigit;
	}
	
	public void setCheckDigit(Character checkDigit) {
		this.checkDigit = checkDigit;
	}
	
	public String asString() {
		return getCompanyPrefix() + getItemReference() + getCheckDigit();
	}
	
	public boolean isValid() {
		try {
			validate();
			return true;
		} catch (IllegalArgumentException ex) {
			return false;
		}
	}
	
	public void validate() {
		validateCompanyPrefix();
		validateItemReference();
		validateCheckDigit();
		
		if (asString().length() != 13) {
			throw new IllegalArgumentException("gtin13 has length of " + asString().length());
		}
	}
	
	private void validateCheckDigit() {
		if (getCheckDigit() == null) {
			throw new IllegalArgumentException("checkDigit is null");
		}
		if (!Character.isDigit(getCheckDigit())) {
			throw new IllegalArgumentException("checkDigit is not a digit");
		}
	}

	private void validateCompanyPrefix() {
		validatePart(getCompanyPrefix(), "companyPrefix");
	}
	
	private void validateItemReference() {
		validatePart(getItemReference(), "itemReference");
	}
	
	private void validatePart(String part, String partname) {
		if (part == null) {
			throw new IllegalArgumentException(partname + " is null");
		}
		
		if (part.isEmpty()) {
			throw new IllegalArgumentException(partname + " is empty");
		}
		
		if (!part.matches("[0-9]*")) {
			throw new IllegalArgumentException(partname + " has nonnumeric characters");
		}
	}
	
}

package com.lp.util;

public class BICValidator {

	private String bicPattern = "[A-Z]{6,6}[A-Z2-9][A-NP-Z0-9]([A-Z0-9]{3,3}){0,1}";
	
	public String getBICPattern() {
		return bicPattern;
	}
	
	public boolean isValidBIC(String bic) {
		if (bic == null) return false;
		return bic.matches(getBICPattern());
	}

}

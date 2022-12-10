package com.lp.util.gs1;

public class GTIN13Builder {

	private GS1CheckDigit checkDigitCalculator;
	
	public GTIN13Builder() {
	}
	
	private GS1CheckDigit getGS1Calculator() {
		if (checkDigitCalculator == null) {
			checkDigitCalculator = new GS1CheckDigit();
		}
		return checkDigitCalculator;
	}
	
	public GTIN13 build(String companyPrefix, String itemReference) {
		return buildImpl(companyPrefix, itemReference);
	}
	
	public GTIN13 build(String companyPrefix, Integer itemReference) {
		String itemRefAsString = null;
		if (companyPrefix.length() == 7) {
			itemRefAsString = prefill(itemReference.toString(), '0', 5);
		} else if (companyPrefix.length() == 9) {
			itemRefAsString = prefill(itemReference.toString(), '0', 3);
		} else {
			throw new IllegalArgumentException("companyPrefix has not length of 7 or 9");
		}
		
		return buildImpl(companyPrefix, itemRefAsString);
	}
	
	private GTIN13 buildImpl(String companyPrefix, String itemReference) {
		GTIN13 gtin13 = new GTIN13();
		gtin13.setCompanyPrefix(companyPrefix);
		gtin13.setItemReference(itemReference);
		gtin13.setCheckDigit(getGS1Calculator().getCheckDigit(companyPrefix + itemReference));

		return gtin13;
	}

	private String prefill(String value, Character character, Integer length) {
		if (value.length() > length) return value.substring(0, length);
		
		String fill = "";
		for (int i = value.length(); i < length; i++) {
			fill += character;
		}
		return fill + value;
	}
}

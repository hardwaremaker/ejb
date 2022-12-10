package com.lp.util.gs1;

public class SSCCBuilder {

	private GS1CheckDigit gs1CheckDigit;
	
	private Character extensionDigit;
	private String companyPrefix;
	private String serialReference;
	
	public SSCCBuilder() {
	}
	
	private GS1CheckDigit getGS1Calculator() {
		if (gs1CheckDigit == null) {
			gs1CheckDigit = new GS1CheckDigit();
		}
		return gs1CheckDigit;
	}
	
	public String build() {
		validateParts();
		
		StringBuilder builder = new StringBuilder();
		builder.append(extensionDigit).append(companyPrefix).append(serialReference);
		
		return getGS1Calculator().getGtinNumber(builder.toString());
	}
	
	public SSCCBuilder erweiterungsziffer(Character erweiterungsziffer) {
		this.extensionDigit = erweiterungsziffer;
		return this;
	}
	
	public SSCCBuilder basisnummer(String gs1Basisnummer) {
		this.companyPrefix = gs1Basisnummer;
		return this;
	}
	
	public SSCCBuilder basisnummerInklErweiterungsziffer(String gs1Basisnummer) {
		if (gs1Basisnummer == null || gs1Basisnummer.isEmpty()) return this;
		
		this.extensionDigit = gs1Basisnummer.charAt(0);
		
		if (gs1Basisnummer.length() > 1) {
			this.companyPrefix = gs1Basisnummer.substring(1);
		}
		
		return this;
	}
	
	public SSCCBuilder bezugsnummer(String bezugsnummer) {
		if (bezugsnummer == null) return this;
		
		if (bezugsnummer.toString().length() > 7) {
			this.serialReference = bezugsnummer.toString();
			return this;
		}
		
		this.serialReference = prefill(bezugsnummer.toString(), '0', 7);
		return this;
	}
	
	public SSCCBuilder bezugsnummer(Long bezugsnummer) {
		return bezugsnummer(bezugsnummer.toString());
	}

	private String prefill(String value, Character character, Integer length) {
		if (value.length() > length) return value.substring(0, length);
		
		String fill = "";
		for (int i = value.length(); i < length; i++) {
			fill += character;
		}
		return fill + value;
	}

	private void validateParts() {
		validateErweiterungsziffer();
		validateBasisnummer();
		validateBezugsnummer();
	}
	
	private void validateErweiterungsziffer() {
		validatePart(extensionDigit, "extensionDigit");
	}
	
	private void validateBasisnummer() {
		validatePart(companyPrefix, "companyPrefix", 9);
	}
	
	private void validateBezugsnummer() {
		validatePart(serialReference, "serialReference", 7);
	}
	
	private void validatePart(Character part, String partname) {
		if (part == null) validatePart(null, partname, 1);
		
		validatePart(String.valueOf(part), partname, 1);
	}
	
	private void validatePart(String part, String partname, Integer length) {
		if (part == null) {
			throw new IllegalArgumentException(partname + " is null");
		}
		
		if (part.isEmpty()) {
			throw new IllegalArgumentException(partname + " is empty");
		}
		
		if (!length.equals(part.length())) {
			throw new IllegalArgumentException(partname + " has not length = " + length);
		}
		
		if (!part.matches("[0-9]*")) {
			throw new IllegalArgumentException(partname + " has nonnumeric characters");
		}
	}
}

package com.lp.util.gs1;

/**
 * Klasse f&uuml;r die Berechnung der Pr&uuml;fziffer einer Global Trade Item Number
 * (GTIN)
 * 
 * @author andi
 *
 */
public class GS1CheckDigit {

	public Character getCheckDigit(String gtinNumber) {
		validateGtinNumber(gtinNumber);
		return getCheckDigitImpl(gtinNumber);
	}

	private Character getCheckDigitImpl(String gtinNumber) {
		Integer produktSumme = 0;
		for (int idx = gtinNumber.length() - 1, count = 1; idx >= 0; idx--, count++) {
			int multiplier = count % 2 == 0 ? 1 : 3;
			produktSumme += Character.getNumericValue(gtinNumber.charAt(idx)) * multiplier;
		}
		int unitPosition = produktSumme % 10;
		Integer checkDigit = unitPosition == 0 ? 0 : 10 - produktSumme % 10;
		return Character.forDigit(checkDigit, 10);
	}

	private void validateGtinNumber(String gtinNumber) {
		if (gtinNumber == null) {
			throw new IllegalArgumentException("gtinNumber is null");
		}
		
		if (gtinNumber.isEmpty()) {
			throw new IllegalArgumentException("gtinNumber is empty");
		}
		
		if (!gtinNumber.matches("[0-9]*")) {
			throw new IllegalArgumentException("gtinNumber has not numeric characters");
		}
	}
	
	public String getGtinNumber(String gtinNumber) {
		validateGtinNumber(gtinNumber);
		
		return gtinNumber + getCheckDigitImpl(gtinNumber);
	}
}

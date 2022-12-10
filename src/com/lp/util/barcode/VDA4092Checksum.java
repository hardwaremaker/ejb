package com.lp.util.barcode;

import java.util.HashMap;
import java.util.Map;

public class VDA4092Checksum {

	private HashMap<Character, Integer> mapChar2Value ;
	private HashMap<Integer, Character> mapValue2Char ;
	
	public VDA4092Checksum() {
	}
	
	private HashMap<Character, Integer> getAllowedCharacters() {
		if(mapChar2Value == null) {
			mapChar2Value = new HashMap<Character, Integer>();
			createAllowedCharsMap();
		}
		return mapChar2Value ;
	}

	private HashMap<Integer, Character> getValue2Char() {
		if(mapValue2Char == null) {
			mapValue2Char = new HashMap<Integer, Character>();
			for (Map.Entry<Character, Integer> entry : getAllowedCharacters().entrySet()) {
				mapValue2Char.put(entry.getValue(), entry.getKey());
			};
		}
		return mapValue2Char;
	}
	
	private void createAllowedCharsMap() {
		buildAllowedDigits();
		buildAllowedLetters();
		buildSpecialChars();		
	}
	
	private void buildAllowedDigits() {
		for(char c = '0'; c <= '9'; c++) {
			mapChar2Value.put(new Character(c), c - '0'); 
		}
	}
	
	private void buildAllowedLetters() {
		for(char c = 'A'; c <= 'Z'; c++) {
			mapChar2Value.put(new Character(c), 10 + (c - 'A'));
		}
	}
	
	private void buildSpecialChars() {
		mapChar2Value.put(new Character('-'), 36);
		mapChar2Value.put(new Character('.'), 37);
		mapChar2Value.put(new Character(' '), 38);
		mapChar2Value.put(new Character('$'), 39);
		mapChar2Value.put(new Character('/'), 40);
		mapChar2Value.put(new Character('+'), 41);
		mapChar2Value.put(new Character('%'), 42);
	}
	
	/**
	 * Die Pr&uuml;fziffer f&uuml;r den angegebenen barcode ermitteln und am Ende des Barcodes angeh&auml;ngt 
	 * zur Verf&uuml;gung stellen.
	 * 
	 * @param barcodeText die Nutzziffern des Barcodes
	 * @return barcodeText um die angeh&auml;ngte Pr&uuml;fziffer erweitert
	 */
	public String getBarcode(String barcodeText) {
		validateClearText(barcodeText);
		
		Character c = calculateChecksumCharacter(barcodeText) ;
 		return barcodeText + c ;
	}

	/**
	 * Die Pr&uuml;fziffer f&uuml;r den angegebenen Barcode (enth&auml;lt keine Pr&uuml;fziffer) ermitteln
	 * @param barcodeText 
	 * @return die Pr&uuml;fziffer f&uuml;r den angegebenen Barcode
	 */
	public Character getChecksumCharacter(String barcodeText) {
		validateClearText(barcodeText);
		return calculateChecksumCharacter(barcodeText) ;
	}
	
	private Character calculateChecksumCharacter(String clearText) {
		Integer sum = calculateChecksum(clearText); 
		Integer checksum = sum % 43;
		Character c = getValue2Char().get(checksum);
		if(c == null) {
			throw new IllegalArgumentException(
					"Illegal checksum '" + checksum + "' detected for '" + clearText + "'.");
		}
		return c;
	}
	
	private void validateClearText(String clearText) {
		if(clearText == null) {
			throw new IllegalArgumentException("clearText null");
		}
		if(clearText.length() < 1) {
			throw new IllegalArgumentException("clearText with at least one char");
		}		
	}
	
	protected Integer calculateChecksum(String clearText) {
		Integer sum = 0 ;
		for(int i = 0; i < clearText.length(); i++) {
			Character c = clearText.charAt(i);
			if(!getAllowedCharacters().containsKey(c)) {
				throw new IllegalArgumentException("Invalid char '" + c + "' at position '" + i + "'.");
			}
			sum += getAllowedCharacters().get(c);
		}

		return sum;
	}
}

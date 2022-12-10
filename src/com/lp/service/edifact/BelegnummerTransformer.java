package com.lp.service.edifact;


public class BelegnummerTransformer {
	private String transformSlash(BelegnummerTyp belegtyp, String belegCnr) {
		String replaceWith = "";
		
		switch(belegtyp) {
		case Auftrag: 
			replaceWith = "0";
			break;
			
		case Lieferschein: 
			replaceWith = "1";
			break;
		
		case Rechnung:
			replaceWith = "2";
			break;

		default: 
			replaceWith = "";
			break;
		}

		return belegCnr.replaceAll("/", replaceWith);
	}
	
	private String transformBelegnummer(BelegnummerTyp belegTyp, String belegCnr) {
		String s = transformSlash(belegTyp, belegCnr)
				.replaceAll("[^0-9]", ""); // nur Ziffern 
		return s;			
	}
	
	public String transform(BelegnummerTyp belegtyp, String belegCnr) {
		return transformBelegnummer(belegtyp, belegCnr);
	}
	
	public String transform(BelegnummerTyp belegtyp, String belegCnr, Integer positionNr) {
		String s = transformBelegnummer(belegtyp, belegCnr);
		if (positionNr != null) {
			s = s + ";" + positionNr;
		}
		return s;			
	}
}

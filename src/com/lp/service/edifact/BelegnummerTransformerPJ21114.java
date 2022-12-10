package com.lp.service.edifact;

public class BelegnummerTransformerPJ21114 extends BelegnummerTransformerAbstract {
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
	
	@Override
	protected String transformBelegnummer(BelegnummerTyp belegTyp, String belegCnr) {
		String s = transformSlash(belegTyp, belegCnr)
				.replaceAll("[^0-9]", ""); // nur Ziffern 
		return s;			
	}

}

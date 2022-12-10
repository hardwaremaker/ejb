package com.lp.service.edifact;

public abstract class BelegnummerTransformerAbstract {
	protected abstract String transformBelegnummer(BelegnummerTyp belegTyp, String belegCnr);
	
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

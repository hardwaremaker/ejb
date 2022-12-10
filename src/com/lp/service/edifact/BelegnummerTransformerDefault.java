package com.lp.service.edifact;

public class BelegnummerTransformerDefault extends BelegnummerTransformerAbstract {

	@Override
	protected String transformBelegnummer(BelegnummerTyp belegTyp, String belegCnr) {
		return belegCnr;
	}
}

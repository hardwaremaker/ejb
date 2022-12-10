package com.lp.service.edifact;

public class BelegnummerTransformerFactory {

	public static BelegnummerTransformerAbstract create() {
		return new BelegnummerTransformerPJ21114();
	}
}

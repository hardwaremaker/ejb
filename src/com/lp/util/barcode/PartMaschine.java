package com.lp.util.barcode;

public class PartMaschine extends BarcodePart {

	private final static int Length = 2;
	
	public PartMaschine(String barcode, Integer startIdx) {
		super(barcode, startIdx, Length);
	}

	public PartMaschine(BarcodePart vorgaenger) {
		super(vorgaenger, Length);
	}
}

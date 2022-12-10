package com.lp.util.barcode;

public class PartArbeitsgang extends BarcodePart {

	private final static int Length = 2;
	
	public PartArbeitsgang(String barcode, Integer startIdx) {
		super(barcode, startIdx, Length);
	}
	
	public PartArbeitsgang(BarcodePart predecessor) {
		super(predecessor, Length);
	}

}

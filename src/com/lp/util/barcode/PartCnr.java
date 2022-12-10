package com.lp.util.barcode;

public class PartCnr extends BarcodePart {
	
	public PartCnr(String barcode, Integer startIdx, Integer length) {
		super(barcode, startIdx, length);
	}

	public PartCnr(BarcodePart vorgaenger, Integer length) {
		super(vorgaenger, length);
	}
}

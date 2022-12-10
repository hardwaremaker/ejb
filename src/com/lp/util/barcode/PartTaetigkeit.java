package com.lp.util.barcode;

public class PartTaetigkeit extends BarcodePart {

	public PartTaetigkeit(String barcode, Integer startIdx) {
		super(barcode, startIdx, null);
	}

	public PartTaetigkeit(BarcodePart predecessor) {
		super(predecessor, null);
	}

}

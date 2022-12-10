package com.lp.util.barcode;

public class PartUnterarbeitsgang extends BarcodePart {

	private final static int Length = 1;
	private boolean hasValue = false;

	public PartUnterarbeitsgang(PartArbeitsgang predecessor) {
		super(predecessor, Length);
		
		if (getStartIdx() < getBarcode().length()
				&& getBarcode().charAt(getStartIdx()) == '.') {
			setStartIdx(getStartIdx() + 1);
			hasValue = true;
		}
	}

	@Override
	public String read() throws BarcodeException {
		return hasValue ? super.read() : null;
	}
}

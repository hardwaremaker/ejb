package com.lp.util.barcode;

public class PartLeadIn extends BarcodePart {

	public PartLeadIn(String barcode, HvBarcodeTyp barcodeTyp) {
		super(barcode, 0, barcodeTyp.getText().length());
	}

}

package com.lp.util.barcode;

public class UnknownBarcodeException extends BarcodeException {
	private static final long serialVersionUID = -8275603667153366871L;

	public UnknownBarcodeException(String barcode) {
		super("Unknown barcode '" + barcode + "'");
		setBarcode(barcode);
	}

}

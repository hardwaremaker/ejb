package com.lp.util.barcode;

public class BarcodeException extends Exception {
	private static final long serialVersionUID = 2762094516639456285L;

	private String barcode;
	
	public BarcodeException() {
	}

	public BarcodeException(String message) {
		super(message);
	}

	public BarcodeException(Throwable cause) {
		super(cause);
	}

	public BarcodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
}

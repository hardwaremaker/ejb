package com.lp.util.barcode;

public class EndOfBarcodeException extends BarcodeException {
	private static final long serialVersionUID = 1065695027352574130L;

	private int startIdx;
	private int endIdx;
	
	public EndOfBarcodeException(String barcode, int startIdx, int endIdx, String partName) {
		super("[" + partName + "] Barcode '" + barcode + "' with length '" + barcode.length() + "' is out of index range: "
				+ "startIdx = " + startIdx + ", endIdx = " + endIdx);
		setBarcode(barcode);
		this.startIdx = startIdx;
		this.endIdx = endIdx;
	}
	
	public int getStartIdx() {
		return startIdx;
	}
	
	public int getEndIdx() {
		return endIdx;
	}
	
}

package com.lp.util.barcode;

import com.lp.util.Helper;

public class BarcodePart {

	private String barcode;
	private Integer startIdx;
	private Integer length;
	
	public BarcodePart(String barcode, Integer startIdx, Integer length) {
		this.barcode = barcode;
		this.startIdx = startIdx;
		this.length = length;
	}
	
	public BarcodePart(BarcodePart predecessor, Integer length) {
		setBarcode(predecessor.getBarcode());
		setStartIdx(predecessor.getEndIdx());
		setLength(length);
	}

	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	public Integer getStartIdx() {
		return startIdx;
	}
	public void setStartIdx(Integer startIdx) {
		this.startIdx = startIdx;
	}

	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	
	public Integer getEndIdx() {
		return length != null ? getStartIdx() + getLength() : getBarcode().length();
	}
	
	public String read() throws BarcodeException {
		if (!Helper.isBetween(getStartIdx(), 0, getBarcode().length() - 1)
				|| !Helper.isBetween(getEndIdx(), 0, getBarcode().length())) {
			throw new EndOfBarcodeException(getBarcode(), getStartIdx(), getEndIdx(), this.getClass().getSimpleName());
		}
		
		return barcode.substring(getStartIdx(), getEndIdx());
	}
}

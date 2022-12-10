package com.lp.util.barcode;

import java.io.Serializable;

public class HvBarcodeBase implements Serializable {
	private static final long serialVersionUID = 5428845704482108086L;

	private HvBarcodeTyp barcodeTyp;
	private String barcodeRaw;

	public HvBarcodeBase(HvBarcodeTyp barcodeTyp, String barcodeRaw) {
		setBarcodeTyp(barcodeTyp);
		setBarcodeRaw(barcodeRaw);
	}

	public HvBarcodeTyp getBarcodeTyp() {
		return barcodeTyp;
	}
	
	public void setBarcodeTyp(HvBarcodeTyp barcodeTyp) {
		this.barcodeTyp = barcodeTyp;
	}
	
	public String getBarcodeRaw() {
		return barcodeRaw;
	}
	
	public void setBarcodeRaw(String barcodeRaw) {
		this.barcodeRaw = barcodeRaw;
	}
}

package com.lp.util.barcode;

public class BarcodeLosTaetigkeit extends HvBarcodeBase {
	private static final long serialVersionUID = 9035073179697203700L;

	private String losCnr;
	private String taetigkeitCnr;
	
	public BarcodeLosTaetigkeit(String barcodeRaw) {
		super(HvBarcodeTyp.Los, barcodeRaw);
	}

	public String getLosCnr() {
		return losCnr;
	}
	public void setLosCnr(String losCnr) {
		this.losCnr = losCnr;
	}
	
	public String getTaetigkeitCnr() {
		return taetigkeitCnr;
	}
	public void setTaetigkeitCnr(String taetigkeitCnr) {
		this.taetigkeitCnr = taetigkeitCnr;
	}
}

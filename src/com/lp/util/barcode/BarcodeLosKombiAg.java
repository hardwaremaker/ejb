package com.lp.util.barcode;

public class BarcodeLosKombiAg extends HvBarcodeBase {
	private static final long serialVersionUID = 8804853755260616952L;

	private String losCnr;
	private String maschineCnr;
	private String arbeitsgangNr;
	private String unterarbeitsgangNr;
	
	public BarcodeLosKombiAg(String barcodeRaw) {
		super(HvBarcodeTyp.LosKombiAg, barcodeRaw);
	}

	public String getLosCnr() {
		return losCnr;
	}
	public void setLosCnr(String losCnr) {
		this.losCnr = losCnr;
	}
	
	public String getMaschineCnr() {
		return maschineCnr;
	}
	public void setMaschineCnr(String maschineCnr) {
		this.maschineCnr = maschineCnr;
	}
	
	public String getArbeitsgangNr() {
		return arbeitsgangNr;
	}
	public void setArbeitsgangNr(String arbeitsgangNr) {
		this.arbeitsgangNr = arbeitsgangNr;
	}
	
	public String getUnterarbeitsgangNr() {
		return unterarbeitsgangNr;
	}
	public void setUnterarbeitsgangNr(String unterarbeitsgangNr) {
		this.unterarbeitsgangNr = unterarbeitsgangNr;
	}
}

package com.lp.util.barcode;

import java.io.Serializable;

public class BarcodeMaschineStopp extends HvBarcodeBase implements Serializable {
	private static final long serialVersionUID = -1855440235956871928L;

	private String losCnr;
	private String maschineCnr;
	private String arbeitsgangNr;
	private String unterarbeitsgangNr;
	
	public BarcodeMaschineStopp(String barcodeRaw) {
		super(HvBarcodeTyp.MaschineStopp, barcodeRaw);
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

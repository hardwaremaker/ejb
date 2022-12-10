package com.lp.util.barcode;

import java.io.Serializable;

public class BarcodeLosKombiTaetigkeit extends HvBarcodeBase implements Serializable {
	private static final long serialVersionUID = 3983746272219734068L;

	private String losCnr;
	private String maschineCnr;
	private String taetigkeitCnr;
	
	public BarcodeLosKombiTaetigkeit(String barcodeRaw) {
		super(HvBarcodeTyp.LosKombiTaetigkeit, barcodeRaw);
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
	
	public String getTaetigkeitCnr() {
		return taetigkeitCnr;
	}
	public void setTaetigkeitCnr(String taetigkeitCnr) {
		this.taetigkeitCnr = taetigkeitCnr;
	}
}

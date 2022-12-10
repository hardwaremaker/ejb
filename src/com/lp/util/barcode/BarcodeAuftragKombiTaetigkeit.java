package com.lp.util.barcode;

import java.io.Serializable;

public class BarcodeAuftragKombiTaetigkeit extends HvBarcodeBase implements Serializable {
	private static final long serialVersionUID = 107509161027043167L;

	private String auftragCnr;
	private String taetigkeitCnr;
	
	public BarcodeAuftragKombiTaetigkeit(String barcodeRaw) {
		super(HvBarcodeTyp.AuftragKombiTaetigkeit, barcodeRaw);
	}

	public String getAuftragCnr() {
		return auftragCnr;
	}
	public void setAuftragCnr(String auftragCnr) {
		this.auftragCnr = auftragCnr;
	}
	
	public String getTaetigkeitCnr() {
		return taetigkeitCnr;
	}
	public void setTaetigkeitCnr(String taetigkeitCnr) {
		this.taetigkeitCnr = taetigkeitCnr;
	}
	
}

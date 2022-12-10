package com.lp.util;

public class LinienabrufProduktionStklNichtImMandantExc extends EJBExceptionData {
	
	private static final long serialVersionUID = 4729732935116020300L;

	private String stklArtikelCnr;
	private String mandantCnr;
	
	public LinienabrufProduktionStklNichtImMandantExc(String stklArtikelCnr,
			String mandantCnr) {
		this.stklArtikelCnr = stklArtikelCnr;
		this.mandantCnr = mandantCnr;
	}

	public void setMandantCnr(String mandantCnr) {
		this.mandantCnr = mandantCnr;
	}
	
	public String getMandantCnr() {
		return mandantCnr;
	}
	
	public void setStklArtikelCnr(String stklArtikelCnr) {
		this.stklArtikelCnr = stklArtikelCnr;
	}
	
	public String getStklArtikelCnr() {
		return stklArtikelCnr;
	}
}

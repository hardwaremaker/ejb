package com.lp.server.finanz.service;

import java.io.Serializable;

public class UstUebersetzungDto implements Serializable {
	private static final long serialVersionUID = 6923197403635344327L;

	private String cNr;
	private String mandantCNr;
	private Integer mwstSatzBezIId;
	private Boolean bIgErwerb;
	private Integer reversechargeartIId;
	
	public UstUebersetzungDto() {
	}

	public String getCNr() {
		return cNr;
	}
	
	public void setCNr(String cNr) {
		this.cNr = cNr;
	}
	
	public String getMandantCNr() {
		return mandantCNr;
	}
	
	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}
	
	public Integer getMwstSatzBezIId() {
		return mwstSatzBezIId;
	}
	
	public void setMwstSatzBezIId(Integer mwstSatzBezIId) {
		this.mwstSatzBezIId = mwstSatzBezIId;
	}
	
	public Boolean getBIgErwerb() {
		return bIgErwerb;
	}
	
	public void setBIgErwerb(Boolean bIgErwerb) {
		this.bIgErwerb = bIgErwerb;
	}
	
	public void setReversechargeartIId(Integer reversechargeartIId) {
		this.reversechargeartIId = reversechargeartIId;
	}
	
	public Integer getReversechargeartIId() {
		return reversechargeartIId;
	}
}

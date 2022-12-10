package com.lp.server.system.service;

import java.io.Serializable;

public class VersandwegPartnerDto implements Serializable, IVersandwegPartnerDto {
	private static final long serialVersionUID = 5541604453145011817L;

	private Integer iId;
	private Integer partnerId;
	private Integer versandwegId;
	private String mandantCnr;
	
	
	public Integer getIId() {
		return iId;
	}
	public void setIId(Integer IId) {
		this.iId = IId;
	}
	public Integer getVersandwegId() {
		return versandwegId;
	}
	public void setVersandwegId(Integer versandwegId) {
		this.versandwegId = versandwegId;
	}
	public String getMandantCnr() {
		return mandantCnr;
	}
	public void setMandantCnr(String mandantCnr) {
		this.mandantCnr = mandantCnr;
	}
	@Override
	public Integer getPartnerIId() {
		return partnerId;
	}
	@Override
	public void setPartnerIId(Integer partnerIId) {
		this.partnerId = partnerIId;
	}
}

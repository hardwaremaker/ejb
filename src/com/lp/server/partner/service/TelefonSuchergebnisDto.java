package com.lp.server.partner.service;

import java.io.Serializable;

public class TelefonSuchergebnisDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TelefonSuchergebnisDto(Integer partnerIId,Integer ansprechpartnerIId,String partner,String ansprechpartner, boolean versteckt){
		this.partnerIId=partnerIId;
		this.ansprechpartnerIId=ansprechpartnerIId;
		this.partner=partner;
		this.ansprechpartner=ansprechpartner;
		this.versteckt=versteckt;
	}
	
	public boolean isVersteckt() {
		return versteckt;
	}

	private Integer partnerIId = null;
	public Integer getPartnerIId() {
		return partnerIId;
	}
	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}
	public Integer getAnsprechpartnerIId() {
		return ansprechpartnerIId;
	}
	public void setAnsprechpartnerIId(Integer ansprechpartnerIId) {
		this.ansprechpartnerIId = ansprechpartnerIId;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getAnsprechpartner() {
		return ansprechpartner;
	}
	public void setAnsprechpartner(String ansprechpartner) {
		this.ansprechpartner = ansprechpartner;
	}
	private Integer ansprechpartnerIId = null;
	private String partner = null;
	private String ansprechpartner = null;
	private boolean versteckt=false;

}

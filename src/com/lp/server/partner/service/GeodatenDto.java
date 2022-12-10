package com.lp.server.partner.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class GeodatenDto implements Serializable {
	private static final long serialVersionUID = 4552572091156633303L;

	private Integer iId;
	private Integer partnerIId;
	private BigDecimal laengengrad;
	private BigDecimal breitengrad;
	
	public GeodatenDto() {
	}

	public Integer getIId() {
		return iId;
	}
	
	public void setIId(Integer iId) {
		this.iId = iId;
	}
	
	public Integer getPartnerIId() {
		return partnerIId;
	}
	
	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}
	
	public BigDecimal getLaengengrad() {
		return laengengrad;
	}
	
	public void setLaengengrad(BigDecimal laengengrad) {
		this.laengengrad = laengengrad;
	}
	
	public BigDecimal getBreitengrad() {
		return breitengrad;
	}
	
	public void setBreitengrad(BigDecimal breitengrad) {
		this.breitengrad = breitengrad;
	}
}

package com.lp.server.rechnung.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class RechnungFLRDataDto implements IRechnungFLRData, Serializable {
	private static final long serialVersionUID = -6882915894298598506L;

	private Integer kundeId ;
	private BigDecimal netValue ;
	private BigDecimal grossValue ;
	private BigDecimal openGrossValue ;
	private String project ;
	private String currency ;
	private String statusCnr ;
	
	@Override
	public void setKundeId(Integer kundeId) {
		this.kundeId = kundeId ;
	}

	@Override
	public Integer getKundeId() {
		return kundeId ;
	}

	@Override
	public void setGrossValue(BigDecimal grossValue) {
		this.grossValue = grossValue ;
	}

	@Override
	public BigDecimal getGrossValue() {
		return grossValue ;
	}

	@Override
	public void setNetValue(BigDecimal netValue) {
		this.netValue = netValue ;
	}

	@Override
	public BigDecimal getNetValue() {
		return netValue ;
	}

	@Override
	public String getProject() {
		return project ;
	}	
	
	@Override
	public void setProject(String projectOrOrder) {
		this.project = projectOrOrder ;
	}
	
	@Override
	public String getCurrency() {
		return currency ;
	}
	
	@Override
	public void setCurrency(String currency) {
		this.currency = currency ;
	}

	@Override
	public void setStatusCnr(String statusCnr) {
		this.statusCnr = statusCnr ;
	}

	@Override
	public String getStatusCnr() {
		return statusCnr ;
	}

	@Override
	public BigDecimal getOpenGrossValue() {
		return openGrossValue ;
	}	
	
	@Override
	public void setOpenGrossValue(BigDecimal openValue) {
		this.openGrossValue = openValue ;
	}
}

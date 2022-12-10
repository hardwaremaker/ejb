package com.lp.server.artikel.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class WebPartStock implements Serializable {
	private static final long serialVersionUID = -6326843255088758210L;

	private BigDecimal quantity;
	private Integer leadTime;
	private String warehouse;
	private String region;
	
	public WebPartStock() {
	}

	public BigDecimal getQuantity() {
		return quantity;
	}
	
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	public Integer getLeadTime() {
		return leadTime;
	}
	
	public void setLeadTime(Integer leadTime) {
		this.leadTime = leadTime;
	}
	
	public String getWarehouse() {
		return warehouse;
	}
	
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
	
	public String getRegion() {
		return region;
	}
	
	public void setRegion(String region) {
		this.region = region;
	}
}

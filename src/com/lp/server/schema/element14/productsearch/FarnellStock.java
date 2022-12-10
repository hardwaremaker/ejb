package com.lp.server.schema.element14.productsearch;

import java.io.Serializable;
import java.math.BigDecimal;

public class FarnellStock implements Serializable {
	private static final long serialVersionUID = 9216263577478921549L;

	private BigDecimal level;
	private BigDecimal leastLeadTime;
	private Integer status;
	private Boolean shipsFromMultipleWarehouses;
	
	public FarnellStock() {
	}

	public BigDecimal getLevel() {
		return level;
	}
	
	public void setLevel(BigDecimal level) {
		this.level = level;
	}
	
	public BigDecimal getLeastLeadTime() {
		return leastLeadTime;
	}
	
	public void setLeastLeadTime(BigDecimal leastLeadTime) {
		this.leastLeadTime = leastLeadTime;
	}
	
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Boolean getShipsFromMultipleWarehouses() {
		return shipsFromMultipleWarehouses;
	}
	
	public void setShipsFromMultipleWarehouses(Boolean shipsFromMultipleWarehouses) {
		this.shipsFromMultipleWarehouses = shipsFromMultipleWarehouses;
	}
}

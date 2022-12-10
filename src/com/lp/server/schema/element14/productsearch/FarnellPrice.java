package com.lp.server.schema.element14.productsearch;

import java.io.Serializable;
import java.math.BigDecimal;

public class FarnellPrice implements Serializable {
	private static final long serialVersionUID = -357265725212105053L;

	private BigDecimal from;
	private BigDecimal to;
	private BigDecimal cost;

	public FarnellPrice() {
	}

	public BigDecimal getFrom() {
		return from;
	}
	
	public void setFrom(BigDecimal from) {
		this.from = from;
	}
	
	public BigDecimal getTo() {
		return to;
	}
	
	public void setTo(BigDecimal to) {
		this.to = to;
	}
	
	public BigDecimal getCost() {
		return cost;
	}
	
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
}

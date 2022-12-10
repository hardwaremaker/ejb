package com.lp.server.artikel.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class WebPartPrice implements Serializable {
	private static final long serialVersionUID = 6844291595095339952L;

	private BigDecimal from;
	private BigDecimal to;
	private BigDecimal amount;
	private String currency;
	
	public WebPartPrice() {
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
	
	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
}

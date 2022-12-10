package com.lp.service.edifact.schema;

import java.math.BigDecimal;

/**
 * Currencies
 * 
 * @author ghp
 */
public class CuxInfo {
	private String detailsQualifier;
	private String codedCurrency;
	private String qualifier;
	
	private BigDecimal baseRate;
	private BigDecimal exchangeRate;
	private String exchangeCurrency;
	
	public CuxInfo() {
		setBaseRate(BigDecimal.ONE);
		setExchangeRate(BigDecimal.ONE);
	}
	
	public String getDetailsQualifier() {
		return detailsQualifier;
	}
	public void setDetailsQualifier(String detailsQualifier) {
		this.detailsQualifier = detailsQualifier;
	}
	public String getCodedCurrency() {
		return codedCurrency;
	}
	public void setCodedCurrency(String codedCurrency) {
		this.codedCurrency = codedCurrency;
	}
	public String getQualifier() {
		return qualifier;
	}
	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}
	public BigDecimal getBaseRate() {
		return baseRate;
	}
	public void setBaseRate(BigDecimal baseRate) {
		this.baseRate = baseRate;
	}
	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public String getExchangeCurrency() {
		return exchangeCurrency;
	}
	public void setExchangeCurrency(String exchangeCurrency) {
		this.exchangeCurrency = exchangeCurrency;
	}
}

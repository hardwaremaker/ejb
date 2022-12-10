package com.lp.server.eingangsrechnung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

public class ErImportItem implements Serializable {
	private static final long serialVersionUID = 2686109473975348682L;

	private String cnr;
	private String creditor;
	private String creditorName;
	private Date date;
	private Date expirationDate;
	private String cnrSupplier;
	private String orderNumber;
	private String taxCode;
	private BigDecimal amount;
	private BigDecimal taxAmount;
	private String accountNumber;
	private String currency;
	
	public ErImportItem() {
	}

	public String getCnr() {
		return cnr;
	}

	public void setCnr(String cnr) {
		this.cnr = cnr;
	}

	public String getCreditor() {
		return creditor;
	}

	public void setCreditor(String creditor) {
		this.creditor = creditor;
	}

	public String getCreditorName() {
		return creditorName;
	}

	public void setCreditorName(String creditorName) {
		this.creditorName = creditorName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getCnrSupplier() {
		return cnrSupplier;
	}

	public void setCnrSupplier(String cnrSupplier) {
		this.cnrSupplier = cnrSupplier;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getAccountNumber() {
		return accountNumber;
	}
	
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
}

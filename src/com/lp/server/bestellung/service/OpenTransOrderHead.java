package com.lp.server.bestellung.service;

import java.io.Serializable;
import java.sql.Date;

import com.lp.server.partner.service.PartnerDto;

public class OpenTransOrderHead implements Serializable {
	private static final long serialVersionUID = -838544401496484120L;

	private String orderNumber;
	private Date orderDate;
	private PartnerDto buyerAddress;
	private String buyerSupplierId;
	
	private PartnerDto supplier;
	private String supplierId;
	
	private PartnerDto invoiceAddress;
	private PartnerDto deliveryAddress;
	
	private String currency;
	private String termsAndConditions;
	
	private PartnerDto buyerContact;
	
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public PartnerDto getBuyerAddress() {
		return buyerAddress;
	}
	public void setBuyerAddress(PartnerDto buyerAddress) {
		this.buyerAddress = buyerAddress;
	}
	public String getBuyerSupplierId() {
		return buyerSupplierId;
	}
	public void setBuyerSupplierId(String buyerSupplierId) {
		this.buyerSupplierId = buyerSupplierId;
	}
	public PartnerDto getSupplier() {
		return supplier;
	}
	public void setSupplier(PartnerDto supplier) {
		this.supplier = supplier;
	}
	public String getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}
	public PartnerDto getInvoiceAddress() {
		return invoiceAddress;
	}
	public void setInvoiceAddress(PartnerDto invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}
	public PartnerDto getDeliveryAddress() {
		return deliveryAddress;
	}
	public void setDeliveryAddress(PartnerDto deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getTermsAndConditions() {
		return termsAndConditions;
	}
	public void setTermsAndConditions(String termsAndConditions) {
		this.termsAndConditions = termsAndConditions;
	}
	public PartnerDto getBuyerContact() {
		return buyerContact;
	}
	public void setBuyerContact(PartnerDto buyerContact) {
		this.buyerContact = buyerContact;
	}
}

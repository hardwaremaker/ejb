package com.lp.server.bestellung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import com.lp.server.system.service.UneceUnitCode;

public class OpenTransOrderPosition implements Serializable {
	private static final long serialVersionUID = -229620642399124492L;

	private String lineItemId;
	private String itemNumber;
	private String supplierItemNumber;
	private String descriptionShort;
	private BigDecimal quantity;
	private UneceUnitCode orderUnit;
	private BigDecimal amount;
	private BigDecimal lineAmount;
	private Date deliveryDate;
	
	public String getLineItemId() {
		return lineItemId;
	}
	public void setLineItemId(String lineItemId) {
		this.lineItemId = lineItemId;
	}
	public String getItemNumber() {
		return itemNumber;
	}
	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}
	public String getSupplierItemNumber() {
		return supplierItemNumber;
	}
	public void setSupplierItemNumber(String supplierItemNumber) {
		this.supplierItemNumber = supplierItemNumber;
	}
	public String getDescriptionShort() {
		return descriptionShort;
	}
	public void setDescriptionShort(String descriptionShort) {
		this.descriptionShort = descriptionShort;
	}
	public BigDecimal getQuantity() {
		return quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	public UneceUnitCode getOrderUnit() {
		return orderUnit;
	}
	public void setOrderUnit(UneceUnitCode orderUnit) {
		this.orderUnit = orderUnit;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getLineAmount() {
		return lineAmount;
	}
	public void setLineAmount(BigDecimal lineAmount) {
		this.lineAmount = lineAmount;
	}
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
}

package com.lp.service.edifact.orders;

import java.math.BigDecimal;
import java.util.Date;

public class DeliveryEntry {
	private BigDecimal quantity;
	private Date date;

	public DeliveryEntry() {		
	}

	
	public DeliveryEntry(BigDecimal quantity, Date deliveryDate) {
		setQuantity(quantity);
		setDate(deliveryDate);
	}
	
	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public boolean hasQuantity() {
		return this.quantity != null;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public boolean hasDate() {
		return this.date != null;
	}
}

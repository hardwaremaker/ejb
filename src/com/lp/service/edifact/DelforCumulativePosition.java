package com.lp.service.edifact;

import java.math.BigDecimal;
import java.util.Date;

import com.lp.server.artikel.service.ArtikelDto;

public class DelforCumulativePosition {
	private ArtikelDto itemDto;
	private BigDecimal quantity;
	private Date date;
	private Date startDate;
	private String reference;
	
	public DelforCumulativePosition() {
	}

	public ArtikelDto getItemDto() {
		return itemDto;
	}

	public void setItemDto(ArtikelDto itemDto) {
		this.itemDto = itemDto;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
}

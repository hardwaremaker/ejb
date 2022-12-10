package com.lp.service.edifact;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lp.server.artikel.service.ArtikelDto;

public class DelforPosition {
	private ArtikelDto itemDto;
	private BigDecimal quantity;
	private Date date;
	private List<String> texts;
	private String orderReference;
	private BigDecimal backlogQuantity;
	
	public DelforPosition() {
		setTexts(new ArrayList<String>());
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

	public List<String> getTexts() {
		return texts;
	}

	public void setTexts(List<String> texts) {
		this.texts = texts;
	}
	
	public void addText(String text) {
		texts.add(text);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getOrderReference() {
		return orderReference;
	}

	public void setOrderReference(String orderReference) {
		this.orderReference = orderReference;
	}

	public BigDecimal getBacklogQuantity() {
		return backlogQuantity;
	}

	public void setBacklogQuantity(BigDecimal backlogQuantity) {
		this.backlogQuantity = backlogQuantity;
	}
	
	public String distinctKey() {
		return itemDto.getIId() + "|" + getOrderReference();
	}
}

package com.lp.service.edifact.orders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.util.HvOptional;
import com.lp.service.edifact.schema.ImdInfo;
import com.lp.service.edifact.schema.LinInfo;
import com.lp.service.edifact.schema.PiaInfo;

public class OrdersPosition {
	private HvOptional<ArtikelDto> itemDto = HvOptional.empty();
	private BigDecimal quantity;
	private Date date;
	private List<String> texts;
	private String orderReference;
	private List<DeliveryEntry> deliveries;
	private String unit;
	private List<PiaInfo> piaInfos;
	private List<ImdInfo> imdInfos;
	private BigDecimal netPrice;
	private HvOptional<LinInfo> linInfo = HvOptional.empty();
	
	public OrdersPosition() {
		setTexts(new ArrayList<String>());
		setDeliveries(new ArrayList<DeliveryEntry>());
		setPiaInfos(new ArrayList<PiaInfo>());
		setImdInfos(new ArrayList<ImdInfo>());
	}
	
	public HvOptional<ArtikelDto> getItemDto() {
		return itemDto;
	}

	public void setItemDto(ArtikelDto itemDto) {
		this.itemDto = HvOptional.ofNullable(itemDto);
	}

	public void setLinInfo(LinInfo linInfo) {
		this.linInfo = HvOptional.ofNullable(linInfo);
	}
	
	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity, String unit) {
		setQuantity(quantity);
		setUnit(unit);
	}
	
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public void setNetPrice(BigDecimal price) {
		this.netPrice = price;
	}
	
	public BigDecimal getNetPrice() {
		return netPrice;
	}
	
	public String getUnit() {
		return unit;
	}
	
	public void setUnit(String unit) {
		this.unit = unit;
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
	
//	public String distinctKey() {
//		return itemDto.getIId() + "|" + getOrderReference();
//	}

	public List<DeliveryEntry> getDeliveries() {
		return deliveries;
	}

	public void setDeliveries(List<DeliveryEntry> deliveries) {
		this.deliveries = deliveries;
	}
	
	public void addDeliveryQuantity(BigDecimal quantity, Date deliveryDate) {
		this.deliveries.add(new DeliveryEntry(quantity, deliveryDate));
	}
	
	public void addDeliveryEntry(DeliveryEntry entry) {
		this.deliveries.add(entry);
	}

	public List<PiaInfo> getPiaInfos() {
		return piaInfos;
	}

	public void setPiaInfos(List<PiaInfo> piaInfos) {
		this.piaInfos = piaInfos;
	}

	public void addPiaInfo(PiaInfo piaInfo) {
		this.piaInfos.add(piaInfo);
	}
	
	public List<ImdInfo> getImdInfos() {
		return imdInfos;
	}

	public void setImdInfos(List<ImdInfo> imdInfos) {
		this.imdInfos = imdInfos;
	}
	
	public void addImdInfo(ImdInfo imdInfo) {
		this.imdInfos.add(imdInfo);
	}
	
	public HvOptional<LinInfo> getLinInfo() {
		return this.linInfo;
	}
}

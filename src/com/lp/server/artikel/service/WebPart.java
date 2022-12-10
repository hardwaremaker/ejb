package com.lp.server.artikel.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class WebPart implements Serializable {
	private static final long serialVersionUID = -874263083635292281L;

	private String stockKeepingUnit;
	private String id;
	private String manufacturerPartNumber;
	private BigDecimal packSize;
	private BigDecimal inventory;
	private Integer leastLeadTime;
	private WebPartQuantityScale quantityScale;
	private List<WebPartStock> stocks;
	private Timestamp requestTime;
	private BigDecimal unitPrice;
	private BigDecimal minimumOrderQuantity;
	private String weblink;
	private String unit;
	
	public WebPart() {
		setStocks(new ArrayList<WebPartStock>());
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getStockKeepingUnit() {
		return stockKeepingUnit;
	}
	
	public void setStockKeepingUnit(String stockKeepingUnit) {
		this.stockKeepingUnit = stockKeepingUnit;
	}
	
	public String getManufacturerPartNumber() {
		return manufacturerPartNumber;
	}
	
	public void setManufacturerPartNumber(String manufacturerPartNumber) {
		this.manufacturerPartNumber = manufacturerPartNumber;
	}
	
	public BigDecimal getPackSize() {
		return packSize;
	}
	
	public void setPackSize(BigDecimal packSize) {
		this.packSize = packSize;
	}
	
	public BigDecimal getInventory() {
		return inventory;
	}
	
	public void setInventory(BigDecimal inventory) {
		this.inventory = inventory;
	}
	
	public Integer getLeastLeadTime() {
		return leastLeadTime;
	}
	
	public void setLeastLeadTime(Integer leastLeadTime) {
		this.leastLeadTime = leastLeadTime;
	}
	
	public WebPartQuantityScale getQuantityScale() {
		return quantityScale;
	}
	
	public void setQuantityScale(WebPartQuantityScale quantityScale) {
		this.quantityScale = quantityScale;
	}
	
	public List<WebPartStock> getStocks() {
		return stocks;
	}
	
	public void setStocks(List<WebPartStock> stocks) {
		this.stocks = stocks;
	}
	
	public Timestamp getRequestTime() {
		return requestTime;
	}
	
	public void setRequestTime(Timestamp requestTime) {
		this.requestTime = requestTime;
	}
	
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	public void setMinimumOrderQuantity(BigDecimal minimumOrderQuantity) {
		this.minimumOrderQuantity = minimumOrderQuantity;
	}
	
	public BigDecimal getMinimumOrderQuantity() {
		return minimumOrderQuantity;
	}
	
	public void setWeblink(String weblink) {
		this.weblink = weblink;
	}
	
	public String getWeblink() {
		return weblink;
	}
	
	public String getUnit() {
		return unit;
	}
	
	public void setUnit(String unit) {
		this.unit = unit;
	}
}

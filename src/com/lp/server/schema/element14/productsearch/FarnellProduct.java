package com.lp.server.schema.element14.productsearch;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FarnellProduct implements Serializable {
	private static final long serialVersionUID = -5277194373289582311L;

	@JsonProperty("sku")
	private String stockKeepingUnit;
	private String displayName;
	private String productStatus;
	private String rohsStatusCode;
	private BigDecimal packSize;
	private String unitOfMeasure;
	private String id;
	private List<FarnellDatasheet> datasheets;
	private List<FarnellPrice> prices;
	@JsonProperty("inv")
	private BigDecimal inventory;
	private String vendorId;
	private String vendorName;
	private String brandName;
	private String translatedManufacturerPartNumber;
	private BigDecimal translatedMinimumOrderQuality;
	private List<FarnellAttribute> attributes;
	private FarnellStock stock;
	
	public String getStockKeepingUnit() {
		return stockKeepingUnit;
	}

	public void setStockKeepingUnit(String stockKeepingUnit) {
		this.stockKeepingUnit = stockKeepingUnit;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}

	public String getRohsStatusCode() {
		return rohsStatusCode;
	}

	public void setRohsStatusCode(String rohsStatusCode) {
		this.rohsStatusCode = rohsStatusCode;
	}

	public BigDecimal getPackSize() {
		return packSize;
	}

	public void setPackSize(BigDecimal packSize) {
		this.packSize = packSize;
	}

	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<FarnellDatasheet> getDatasheets() {
		return datasheets;
	}

	public void setDatasheets(List<FarnellDatasheet> datasheets) {
		this.datasheets = datasheets;
	}

	public List<FarnellPrice> getPrices() {
		return prices;
	}

	public void setPrices(List<FarnellPrice> prices) {
		this.prices = prices;
	}

	public BigDecimal getInventory() {
		return inventory;
	}

	public void setInventory(BigDecimal inventory) {
		this.inventory = inventory;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getTranslatedManufacturerPartNumber() {
		return translatedManufacturerPartNumber;
	}

	public void setTranslatedManufacturerPartNumber(String translatedManufacturerPartNumber) {
		this.translatedManufacturerPartNumber = translatedManufacturerPartNumber;
	}

	public BigDecimal getTranslatedMinimumOrderQuality() {
		return translatedMinimumOrderQuality;
	}

	public void setTranslatedMinimumOrderQuality(BigDecimal translatedMinimumOrderQuality) {
		this.translatedMinimumOrderQuality = translatedMinimumOrderQuality;
	}

	public List<FarnellAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<FarnellAttribute> attributes) {
		this.attributes = attributes;
	}

	public FarnellStock getStock() {
		return stock;
	}

	public void setStock(FarnellStock stock) {
		this.stock = stock;
	}

}

package com.lp.server.shop.magento2;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MagOrderItem implements Serializable {
	private static final long serialVersionUID = -3441720095723536702L;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	
	private BigDecimal basePrice;
	private BigDecimal taxPercent;
	private BigDecimal qtyOrdered;
	private BigDecimal baseRowTotal;
	private BigDecimal rowTotalInclTax;
	private boolean isQtyDecimal;

	private BigDecimal originalPrice;
	private String name;
	private Integer productId;
	private Integer orderId;
	private Integer itemId;
	private String sku;
	private Integer storeId;
	
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	@JsonProperty("created_at")
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	@JsonProperty("updated_at")
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	public BigDecimal getBasePrice() {
		return basePrice;
	}
	@JsonProperty("base_price")
	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}
	public BigDecimal getTaxPercent() {
		return taxPercent;
	}
	@JsonProperty("tax_percent")
	public void setTaxPercent(BigDecimal taxPercent) {
		this.taxPercent = taxPercent;
	}
	public BigDecimal getQtyOrdered() {
		return qtyOrdered;
	}
	@JsonProperty("qty_ordered")
	public void setQtyOrdered(BigDecimal qtyOrdered) {
		this.qtyOrdered = qtyOrdered;
	}
	public BigDecimal getBaseRowTotal() {
		return baseRowTotal;
	}
	@JsonProperty("base_row_total")
	public void setBaseRowTotal(BigDecimal baseRowTotal) {
		this.baseRowTotal = baseRowTotal;
	}
	public BigDecimal getRowTotalInclTax() {
		return rowTotalInclTax;
	}
	@JsonProperty("row_total_incl_tax")
	public void setRowTotalInclTax(BigDecimal rowTotalInclTax) {
		this.rowTotalInclTax = rowTotalInclTax;
	}
	public boolean isQtyDecimal() {
		return isQtyDecimal;
	}
	@JsonProperty("is_qty_decimal")
	public void setQtyDecimal(boolean isQtyDecimal) {
		this.isQtyDecimal = isQtyDecimal;
	}
	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}
	@JsonProperty("original_price")
	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getProductId() {
		return productId;
	}
	@JsonProperty("product_id")
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getOrderId() {
		return orderId;
	}
	@JsonProperty("order_id")
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getItemId() {
		return itemId;
	}
	@JsonProperty("item_id")
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public Integer getStoreId() {
		return storeId;
	}
	@JsonProperty("store_id")
	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}
}

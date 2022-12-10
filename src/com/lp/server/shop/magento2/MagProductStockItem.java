package com.lp.server.shop.magento2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MagProductStockItem {
	private Integer itemId;
	private Integer productId;
	private Integer stockId;
	private Integer qty;
	private boolean inStock;
	private boolean qtyDecimal;
	
	public Integer getItemId() {
		return itemId;
	}
	@JsonProperty("item_id")
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public Integer getProductId() {
		return productId;
	}
	@JsonProperty("product_id")
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getStockId() {
		return stockId;
	}
	
	@JsonProperty("stock_id")
	public void setStockId(Integer stockId) {
		this.stockId = stockId;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public boolean isInStock() {
		return inStock;
	}
	
	@JsonProperty("is_in_stock")
	public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}
	public boolean isQtyDecimal() {
		return qtyDecimal;
	}
	
	@JsonProperty("is_qty_decimal")
	public void setQtyDecimal(boolean qtyDecimal) {
		this.qtyDecimal = qtyDecimal;
	}
}

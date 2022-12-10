package com.lp.server.shop.magento2;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MagProductExtensionAttributes {
	private List<MagProductCategoryLink> categoryLinks;
	private MagProductStockItem stockItem;	
	
	public List<MagProductCategoryLink> getCategoryLinks() {
		return categoryLinks;
	}
	@JsonProperty("category_links")
	public void setCategoryLinks(List<MagProductCategoryLink> categoryLinks) {
		this.categoryLinks = categoryLinks;
	}
	public MagProductStockItem getStockItem() {
		return stockItem;
	}
	@JsonProperty("stock_item")
	public void setStockItem(MagProductStockItem stockItem) {
		this.stockItem = stockItem;
	}
}

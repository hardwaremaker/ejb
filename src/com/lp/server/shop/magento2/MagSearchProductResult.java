package com.lp.server.shop.magento2;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MagSearchProductResult {
	private List<MagProduct> items;
	private Integer totalCount;
	
	public List<MagProduct> getItems() {
		return items;
	}
	public void setItems(List<MagProduct> items) {
		this.items = items;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	@JsonProperty("total_count")
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
}

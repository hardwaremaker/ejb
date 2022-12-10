package com.lp.server.shop.magento2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MagProductCategoryLink {
	private Integer position;
	private String categoryId;
	
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	public String getCategoryId() {
		return categoryId;
	}
	@JsonProperty("category_id")
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
}

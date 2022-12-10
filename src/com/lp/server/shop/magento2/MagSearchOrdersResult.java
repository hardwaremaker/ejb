package com.lp.server.shop.magento2;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MagSearchOrdersResult implements Serializable {
	private static final long serialVersionUID = -6039054765022344541L;

	private List<MagOrder> items;
	private Integer totalCount;
	private Map<String, Object> searchCriteria;

	public List<MagOrder> getItems() {
		return items;
	}
	public void setItems(List<MagOrder> items) {
		this.items = items;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	
	@JsonProperty("total_count")
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public Map<String, Object> getSearchCriteria() {
		return searchCriteria;
	}
	@JsonProperty("search_criteria")
	public void setSearchCriteria(Map<String, Object> searchCriteria) {
		this.searchCriteria = searchCriteria;
	}
}

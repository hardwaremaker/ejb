package com.lp.server.schema.element14.productsearch;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FarnellKeywordSearchResult implements Serializable, IFarnellProductSearchResult {
	private static final long serialVersionUID = 2890627292844325225L;

	@JsonProperty("keywordSearchReturn")
	private FarnellSearchResult searchReturn;

	public FarnellKeywordSearchResult() {
	}

	public FarnellSearchResult getSearchResult() {
		return searchReturn;
	}
	
	public void setSearchReturn(FarnellSearchResult searchReturn) {
		this.searchReturn = searchReturn;
	}
}

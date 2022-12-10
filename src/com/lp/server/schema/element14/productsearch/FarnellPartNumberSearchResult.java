package com.lp.server.schema.element14.productsearch;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FarnellPartNumberSearchResult implements Serializable, IFarnellProductSearchResult {
	private static final long serialVersionUID = -6262731968613046071L;

	@JsonProperty("premierFarnellPartNumberReturn")
	private FarnellSearchResult searchReturn;
	
	public FarnellPartNumberSearchResult() {
	}

	public FarnellSearchResult getSearchResult() {
		return searchReturn;
	}
	
	public void setSearchReturn(FarnellSearchResult searchReturn) {
		this.searchReturn = searchReturn;
	}
}

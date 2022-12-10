package com.lp.server.schema.element14.productsearch;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FarnellManufacturerPartNumberSearchResult implements Serializable, IFarnellProductSearchResult {
	private static final long serialVersionUID = 6516526926342129025L;

	@JsonProperty("manufacturerPartNumberSearchReturn")
	private FarnellSearchResult searchReturn;

	public FarnellManufacturerPartNumberSearchResult() {
	}

	public FarnellSearchResult getSearchResult() {
		return searchReturn;
	}

	public void setSearchReturn(FarnellSearchResult searchReturn) {
		this.searchReturn = searchReturn;
	}
}

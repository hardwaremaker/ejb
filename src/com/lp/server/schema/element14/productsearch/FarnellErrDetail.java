package com.lp.server.schema.element14.productsearch;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FarnellErrDetail implements Serializable {
	private static final long serialVersionUID = 5215439569163202112L;

	private FarnellErrSearchException searchException;
	@JsonProperty("SearchServiceException")
	private FarnellErrSearchServiceException searchServiceException;
	
	public FarnellErrDetail() {
	}

	public FarnellErrSearchException getSearchException() {
		return searchException;
	}
	
	public void setSearchException(FarnellErrSearchException searchException) {
		this.searchException = searchException;
	}
	
	public FarnellErrSearchServiceException getSearchServiceException() {
		return searchServiceException;
	}
	
	public void setSearchServiceException(FarnellErrSearchServiceException searchServiceException) {
		this.searchServiceException = searchServiceException;
	}
}

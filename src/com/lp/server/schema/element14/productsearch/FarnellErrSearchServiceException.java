package com.lp.server.schema.element14.productsearch;

import java.io.Serializable;

public class FarnellErrSearchServiceException implements Serializable {
	private static final long serialVersionUID = -1633848904345976506L;

	private FarnellErrSearchException searchException;
	
	public FarnellErrSearchServiceException() {
	}

	public FarnellErrSearchException getSearchException() {
		return searchException;
	}
	
	public void setSearchException(FarnellErrSearchException searchException) {
		this.searchException = searchException;
	}
}

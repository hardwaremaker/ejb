package com.lp.server.schema.element14.productsearch;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FarnellError implements Serializable {
	private static final long serialVersionUID = 1591852688487509155L;

	@JsonProperty("Fault")
	private FarnellErrFault fault;
	
	public FarnellError() {
	}

	public FarnellErrFault getFault() {
		return fault;
	}
	
	public void setFault(FarnellErrFault fault) {
		this.fault = fault;
	}
	
	public String asString() {
		FarnellErrSearchException searchExc = getSearchException();
		return "Farnell Error [" + (searchExc != null ? searchExc.asString() : "unbekannt") + "]";
	}
	
	@JsonIgnore
	public String getExceptionCode() {
		FarnellErrSearchException searchExc = getSearchException();
		return searchExc != null ? searchExc.getExceptionCode() : null;
	}
	
	@JsonIgnore
	public FarnellErrSearchException getSearchException() {
		if (getFault() == null || getFault().getDetail() == null)
			return null;
		
		FarnellErrDetail detail = getFault().getDetail();
		FarnellErrSearchException searchExc = detail.getSearchException() != null 
				? detail.getSearchException()
				: detail.getSearchServiceException() != null 
					? detail.getSearchServiceException().getSearchException()
					: null;
		return searchExc;
	}
}

package com.lp.server.schema.element14.productsearch;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FarnellErrFault implements Serializable {
	private static final long serialVersionUID = -2582161495939533060L;

	@JsonProperty("Detail")
	private FarnellErrDetail detail;
	
	public FarnellErrFault() {
	}

	public FarnellErrDetail getDetail() {
		return detail;
	}
	
	public void setDetail(FarnellErrDetail detail) {
		this.detail = detail;
	}
}

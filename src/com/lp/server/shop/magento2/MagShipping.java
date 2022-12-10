package com.lp.server.shop.magento2;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MagShipping implements Serializable {
	private static final long serialVersionUID = 8711164186024744731L;

	private String method;
	private MagOrderAddress address;
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public MagOrderAddress getAddress() {
		return address;
	}
	public void setAddress(MagOrderAddress address) {
		this.address = address;
	}
}

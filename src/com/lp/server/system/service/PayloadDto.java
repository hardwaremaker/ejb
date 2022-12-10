package com.lp.server.system.service;

import java.io.Serializable;

public class PayloadDto implements Serializable {

	private static final long serialVersionUID = 8135863738599563453L;

	private String reference;
	private Object payload;
	
	public PayloadDto() {
	}

	public PayloadDto(String reference, Object payload) {
		this.reference = reference;
		this.payload = payload;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}
	
}

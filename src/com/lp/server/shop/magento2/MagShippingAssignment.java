package com.lp.server.shop.magento2;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MagShippingAssignment implements Serializable {
	private static final long serialVersionUID = -2783153627607255305L;
	
	private MagShipping shipping;

	public MagShipping getShipping() {
		return shipping;
	}

	public void setShipping(MagShipping shipping) {
		this.shipping = shipping;
	}
}

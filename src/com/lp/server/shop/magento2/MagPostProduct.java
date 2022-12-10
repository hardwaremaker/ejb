package com.lp.server.shop.magento2;

import java.io.Serializable;

public class MagPostProduct implements Serializable {
	private static final long serialVersionUID = -8569459175678607768L;
	
	private MagProduct product;
	
	public MagPostProduct(MagProduct product) {
		setProduct(product);
	}
	
	public MagProduct getProduct() {
		return product;
	}
	public void setProduct(MagProduct product) {
		this.product = product;
	}
}

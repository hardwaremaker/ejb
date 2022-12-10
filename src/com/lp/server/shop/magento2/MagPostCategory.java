package com.lp.server.shop.magento2;

import java.io.Serializable;

public class MagPostCategory implements Serializable {
	private static final long serialVersionUID = -8569459175678607768L;
	
	private MagCategory category;
	
	public MagPostCategory(MagCategory category) {
		setCategory(category);
	}
	
	public MagCategory getCategory() {
		return category;
	}
	public void setCategory(MagCategory category) {
		this.category = category;
	}
}

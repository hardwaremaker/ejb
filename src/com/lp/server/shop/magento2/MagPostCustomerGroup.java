package com.lp.server.shop.magento2;

import java.io.Serializable;

public class MagPostCustomerGroup implements Serializable {
	private static final long serialVersionUID = -8569459175678607768L;
	
	private MagCustomerGroup group;
	
	public MagPostCustomerGroup(MagCustomerGroup group) {
		setGroup(group);
	}
	
	public MagCustomerGroup getGroup() {
		return group;
	}
	
	public void setGroup(MagCustomerGroup group) {
		this.group = group;
	}
}

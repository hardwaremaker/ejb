package com.lp.server.shop.magento2;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MagPutOrderStatus implements Serializable {
	private static final long serialVersionUID = 2877382889861647352L;
	private MagUpdateOrderStatus entity;

	public MagPutOrderStatus(MagUpdateOrderStatus newStatus) {
		setEntity(newStatus);
	}
	
	public MagUpdateOrderStatus getEntity() {
		return entity;
	}

	public void setEntity(MagUpdateOrderStatus entity) {
		this.entity = entity;
	}
}

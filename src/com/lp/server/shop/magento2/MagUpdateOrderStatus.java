package com.lp.server.shop.magento2;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MagUpdateOrderStatus implements Serializable {
	private static final long serialVersionUID = 322851367822065941L;

	private String incrementId;
	private Integer entityId;
	private String state; /* 'new' */
	private String status; /* 'pending' */
	
	public String getIncrementId() {
		return incrementId;
	}
	@JsonProperty("increment_id")
	public void setIncrementId(String incrementId) {
		this.incrementId = incrementId;
	}
	public Integer getEntityId() {
		return entityId;
	}
	@JsonProperty("entity_id")
	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}

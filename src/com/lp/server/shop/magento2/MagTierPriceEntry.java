package com.lp.server.shop.magento2;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MagTierPriceEntry implements Serializable {
	private static final long serialVersionUID = 4747661359990733353L;
	
	private Integer customerGroupId;
	private Integer qty;
	private BigDecimal value;
	
	public Integer getCustomerGroupId() {
		return customerGroupId;
	}
	
	@JsonProperty("customer_group_id")
	public void setCustomerGroupId(Integer customerGroupId) {
		this.customerGroupId = customerGroupId;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
}

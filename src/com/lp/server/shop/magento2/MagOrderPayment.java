package com.lp.server.shop.magento2;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MagOrderPayment implements Serializable {
	private static final long serialVersionUID = -3627734696246256550L;
	private BigDecimal shippingAmount;
	private String creditCardLast4;
	private Integer entityId;
	private BigDecimal baseShippingAmount;
	private Integer parentId;
	private String method;
	private BigDecimal amountOrdered;
	private BigDecimal baseAmountOrdered;
	
	public BigDecimal getShippingAmount() {
		return shippingAmount;
	}
	@JsonProperty("shipping_amount")
	public void setShippingAmount(BigDecimal shippingAmount) {
		this.shippingAmount = shippingAmount;
	}
	public String getCreditCardLast4() {
		return creditCardLast4;
	}
	@JsonProperty("cc_last4")
	public void setCreditCardLast4(String creditCardLast4) {
		this.creditCardLast4 = creditCardLast4;
	}
	public Integer getEntityId() {
		return entityId;
	}
	@JsonProperty("entity_id")
	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}
	public BigDecimal getBaseShippingAmount() {
		return baseShippingAmount;
	}
	@JsonProperty("base_shipping_amount")
	public void setBaseShippingAmount(BigDecimal baseShippingAmount) {
		this.baseShippingAmount = baseShippingAmount;
	}
	public Integer getParentId() {
		return parentId;
	}
	@JsonProperty("parent_id")
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public BigDecimal getAmountOrdered() {
		return amountOrdered;
	}
	@JsonProperty("amount_ordered")
	public void setAmountOrdered(BigDecimal amountOrdered) {
		this.amountOrdered = amountOrdered;
	}
	public BigDecimal getBaseAmountOrdered() {
		return baseAmountOrdered;
	}
	@JsonProperty("base_amount_ordered")
	public void setBaseAmountOrdered(BigDecimal baseAmountOrdered) {
		this.baseAmountOrdered = baseAmountOrdered;
	}
}

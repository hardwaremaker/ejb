package com.lp.server.shop.magento2;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MagOrder implements Serializable {
	private static final long serialVersionUID = 7643296122138797892L;

	private String incrementId;
	
	private Integer entityId;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	
	private String customerFirstname;
	private String customerLastname;
	private String customerEmail;
	private Integer customerId;
	private Integer customerGroupId;
	
	private MagOrderPayment payment;
	
	private String globalCurrencyCode;
	private String orderCurrencyCode;
	
	private BigDecimal grandTotal;
	private Integer totalItemCount;
	private BigDecimal totalQtyOrdered;
	private List<MagOrderItem> items;
	private String shippingDescription;
	
	private String state; /* 'new' */
	private String status; /* 'pending' */
	
	private Integer storeId;
	private BigDecimal totalDue;
	private BigDecimal weight;
	
	private MagOrderAddress billingAddress;
	private Integer billingAddressId;
	
	private MagOrderExtensionAttributes extensionAttributes;
	
	public String getIncrementId() {
		return incrementId;
	}
	@JsonProperty("increment_id")
	public void setIncrementId(String incrementId) {
		this.incrementId = incrementId;
	}
	
	@JsonProperty("entity_id")
	public Integer getEntityId() {
		return entityId;
	}
	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	@JsonProperty("created_at")
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	@JsonProperty("updated_at")
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getCustomerFirstname() {
		return customerFirstname;
	}
	@JsonProperty("customer_firstname")
	public void setCustomerFirstname(String customerFirstname) {
		this.customerFirstname = customerFirstname;
	}
	public String getCustomerLastname() {
		return customerLastname;
	}
	@JsonProperty("customer_lastname")
	public void setCustomerLastname(String customerLastname) {
		this.customerLastname = customerLastname;
	}
	public String getCustomerEmail() {
		return customerEmail;
	}
	
	@JsonProperty("customer_email")
	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	
	@JsonProperty("customer_id")
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public Integer getCustomerGroupId() {
		return customerGroupId;
	}
	
	@JsonProperty("customer_group_id")
	public void setCustomerGroupId(Integer customerGroupId) {
		this.customerGroupId = customerGroupId;
	}
	public String getGlobalCurrencyCode() {
		return globalCurrencyCode;
	}
	
	@JsonProperty("global_currency_code")
	public void setGlobalCurrencyCode(String globalCurrencyCode) {
		this.globalCurrencyCode = globalCurrencyCode;
	}
	public String getOrderCurrencyCode() {
		return orderCurrencyCode;
	}
	
	@JsonProperty("order_currency_code")
	public void setOrderCurrencyCode(String orderCurrencyCode) {
		this.orderCurrencyCode = orderCurrencyCode;
	}
	public BigDecimal getGrandTotal() {
		return grandTotal;
	}
	@JsonProperty("grand_total")
	public void setGrandTotal(BigDecimal grandTotal) {
		this.grandTotal = grandTotal;
	}
	public Integer getTotalItemCount() {
		return totalItemCount;
	}
	@JsonProperty("total_item_count")
	public void setTotalItemCount(Integer totalItemCount) {
		this.totalItemCount = totalItemCount;
	}
	public BigDecimal getTotalQtyOrdered() {
		return totalQtyOrdered;
	}
	@JsonProperty("total_qty_ordered")
	public void setTotalQtyOrdered(BigDecimal totalQtyOrdered) {
		this.totalQtyOrdered = totalQtyOrdered;
	}
	public List<MagOrderItem> getItems() {
		return items;
	}
	public void setItems(List<MagOrderItem> items) {
		this.items = items;
	}
	public String getShippingDescription() {
		return shippingDescription;
	}
	@JsonProperty("shipping_description")
	public void setShippingDescription(String shippingDescription) {
		this.shippingDescription = shippingDescription;
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
	public Integer getStoreId() {
		return storeId;
	}
	
	@JsonProperty("store_id")
	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}
	public BigDecimal getTotalDue() {
		return totalDue;
	}
	@JsonProperty("total_due")
	public void setTotalDue(BigDecimal totalDue) {
		this.totalDue = totalDue;
	}
	public BigDecimal getWeight() {
		return weight;
	}
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	public MagOrderAddress getBillingAddress() {
		return billingAddress;
	}
	@JsonProperty("billing_address")
	public void setBillingAddress(MagOrderAddress billingAddress) {
		this.billingAddress = billingAddress;
	}
	public Integer getBillingAddressId() {
		return billingAddressId;
	}
	@JsonProperty("billing_address_id")
	public void setBillingAddressId(Integer billingAddressId) {
		this.billingAddressId = billingAddressId;
	}
	
	public MagOrderExtensionAttributes getExtensionAttributes() {
		return extensionAttributes;
	}
	
	@JsonProperty("extension_attributes")
	public void setExtensionAttributes(MagOrderExtensionAttributes extensionAttributes) {
		this.extensionAttributes = extensionAttributes;
	}
	public MagOrderPayment getPayment() {
		return payment;
	}
	public void setPayment(MagOrderPayment payment) {
		this.payment = payment;
	}
}

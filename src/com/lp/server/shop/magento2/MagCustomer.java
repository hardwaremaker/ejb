package com.lp.server.shop.magento2;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MagCustomer implements Serializable {
	private static final long serialVersionUID = -552874317122038018L;

	private Integer id;
	private Integer groupId;
	private String prefix;
	private String firstname;
	private String middlename;
	private Integer gender;
	private String lastname;
	private String suffix;
	private String email;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private String defaultShipping;
	private String defaultBilling;
	private String taxvat;
	private List<MagAddress> addresses;
	private Integer storeId;
	private Integer websiteId;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getGroupId() {
		return groupId;
	}
	@JsonProperty("group_id")
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	/**
	 * 1 ... male, 2 ... female, 0 ... unspecified
	 * @return
	 */
	public Integer getGender() {
		return gender;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getDefaultShipping() {
		return defaultShipping;
	}
	@JsonProperty("default_shipping")
	public void setDefaultShipping(String defaultShipping) {
		this.defaultShipping = defaultShipping;
	}
	public String getDefaultBilling() {
		return defaultBilling;
	}
	@JsonProperty("default_billing")
	public void setDefaultBilling(String defaultBilling) {
		this.defaultBilling = defaultBilling;
	}
	public String getTaxvat() {
		return taxvat;
	}
	public void setTaxvat(String taxvat) {
		this.taxvat = taxvat;
	}
	public List<MagAddress> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<MagAddress> addresses) {
		this.addresses = addresses;
	}
	public Integer getStoreId() {
		return storeId;
	}
	@JsonProperty("store_id")
	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}
	public Integer getWebsiteId() {
		return websiteId;
	}
	@JsonProperty("website_id")
	public void setWebsiteId(Integer websiteId) {
		this.websiteId = websiteId;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getMiddlename() {
		return middlename;
	}
	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
}

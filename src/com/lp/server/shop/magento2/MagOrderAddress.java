package com.lp.server.shop.magento2;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Eine Adresse auf Magento2 Seite
 * firstname, lastname, street[0], city, country, state und telephone sind Pflichtfelder
 * company ist optional und kann nicht bei der Account-Anlage, sondern nur bei einer 
 * Addressangabe (f&uuml;r Rechnung- oder Lieferadresse) angegeben werden.
 * 
 * @author gerold
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MagOrderAddress implements Serializable { 
	private static final long serialVersionUID = 7648280963645069232L;

//	private Integer id;
	private String addressType; /* "billing", "shipping" */
	private String city;
	private String countryId;
	private Integer customerAddressId;
	private String email;
	private String firstname;
	private String lastname;
	private String company;
	private String postcode;
	private String region;
	private String regionCode;
	private Integer regionId;
	private List<String> street;
	private String telephone;
	private boolean defaultBilling;
	private boolean defaultShipping;
	
//	public Integer getId() {
//		return id;
//	}
//	public void setId(Integer id) {
//		this.id = id;
//	}
	public String getAddressType() {
		return addressType;
	}
	
	@JsonProperty("address_type")
	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountryId() {
		return countryId;
	}
	@JsonProperty("country_id")
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	public Integer getCustomerAddressId() {
		return customerAddressId;
	}
	@JsonProperty("customer_address_id")
	public void setCustomerAddressId(Integer customerAddressId) {
		this.customerAddressId = customerAddressId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	
	public String getRegionCode() {
		return regionCode;
	}
	@JsonProperty("region_code")
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	public Integer getRegionId() {
		return regionId;
	}
	@JsonProperty("region_id")
	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}
	
	public List<String> getStreet() {
		return street;
	}
	public void setStreet(List<String> street) {
		this.street = street;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public boolean isDefaultBilling() {
		return defaultBilling;
	}
	@JsonProperty("default_billing")
	public void setDefaultBilling(boolean defaultBilling) {
		this.defaultBilling = defaultBilling;
	}
	public boolean isDefaultShipping() {
		return defaultShipping;
	}
	@JsonProperty("default_shipping")
	public void setDefaultShipping(boolean defaultShipping) {
		this.defaultShipping = defaultShipping;
	}	
}

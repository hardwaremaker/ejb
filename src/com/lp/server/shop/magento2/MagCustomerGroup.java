package com.lp.server.shop.magento2;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MagCustomerGroup implements Serializable {
	private static final long serialVersionUID = -532204061622782505L;
	
	private Integer id;
	private String code;
	private Integer taxClassId;
	private String taxClassName;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getTaxClassId() {
		return taxClassId;
	}
	@JsonProperty("tax_class_id")
	public void setTaxClassId(Integer taxClassId) {
		this.taxClassId = taxClassId;
	}
	public String getTaxClassName() {
		return taxClassName;
	}
	@JsonProperty("tax_class_name")
	public void setTaxClassName(String taxClassName) {
		this.taxClassName = taxClassName;
	}
	
}

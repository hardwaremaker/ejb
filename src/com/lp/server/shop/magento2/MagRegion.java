package com.lp.server.shop.magento2;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MagRegion implements Serializable { 
	private static final long serialVersionUID = 7648280963645069232L;

	private String region;
	private String regionCode;
	private Integer regionId;
	
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
}

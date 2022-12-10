package com.lp.server.shop.magento2;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MediaGalleryContent implements Serializable {
	private static final long serialVersionUID = 8546997892741748180L;

	private String base64EncodedData;
	private String type;
	private String name;
	
	public String getBase64EncodedData() {
		return base64EncodedData;
	}
	@JsonProperty("base64_encoded_data")
	public void setBase64EncodedData(String base64EncodedData) {
		this.base64EncodedData = base64EncodedData;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}

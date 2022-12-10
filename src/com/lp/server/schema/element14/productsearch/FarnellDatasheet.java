package com.lp.server.schema.element14.productsearch;

import java.io.Serializable;

public class FarnellDatasheet implements Serializable {
	private static final long serialVersionUID = -1439132488241147822L;

	private String type;
	private String description;
	private String url;
	
	public FarnellDatasheet() {
	}

	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
}
